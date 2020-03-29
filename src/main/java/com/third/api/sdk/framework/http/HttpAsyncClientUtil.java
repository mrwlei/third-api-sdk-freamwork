package com.third.api.sdk.framework.http;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.third.api.sdk.framework.exception.ServerConnectionTimeoutException;
import com.third.api.sdk.framework.exception.ServerErrorException;
import com.third.api.sdk.framework.exception.ServerSocketTimeoutException;
import com.third.api.sdk.framework.util.Propagater;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * HttpAsyncClient Util
 * @author leiwa
 */
@Slf4j
public class HttpAsyncClientUtil {

	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLContextBuilder builder = null;

	static {
		try {
			builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1",
					"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", new PlainConnectionSocketFactory())
					.register("https", sslsf)
					.build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);
			cm.setDefaultMaxPerRoute(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * post 请求
	 * @param url 请求地址
	 * @param function 相应信息处理function
	 * @param timeoutConfig 超时配置
	 * @return
	 */
	public static <T> T post(String url, StreamHandlerFunction<T> function, TimeoutConfiguration timeoutConfig, HttpHost httpHost) {
		return sendRequest(url, HttpRequestMethodType.POST, function, timeoutConfig, httpHost);
	}

	/**
	 * get 请求
	 * @param url 请求地址
	 * @param function 相应信息处理function
	 * @param timeoutConfig 超时配置
	 * @return
	 */
	public static <T> T get(String url, StreamHandlerFunction<T> function, TimeoutConfiguration timeoutConfig, HttpHost httpHost) {
		return sendRequest(url, HttpRequestMethodType.GET, function, timeoutConfig, httpHost);
	}

	private static <T> T sendRequest(String url, HttpRequestMethodType method, StreamHandlerFunction<T> function,
			TimeoutConfiguration timeoutConfig, HttpHost httpHost) {
		RequestConfig requestConfig = buildRequestConfig(timeoutConfig, httpHost);

		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		HttpRequestBase httpRequest = HttpRequestMethodType.GET.equals(method) ? new HttpGet(url) : new HttpPost(url);
		try {
			httpclient = HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setSSLSocketFactory(sslsf)
					.setConnectionManager(cm)
					// 设置重试次数
					//.setRetryHandler(new DefaultHttpRequestRetryHandler(HTTP_RETRY_TIMES, true))
					.build();
			//不用连接池
			if(null != httpHost){
				response =httpclient.execute(httpHost,httpRequest);
			}else{
				response =httpclient.execute(httpRequest);
			}
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(!Objects.equal(HttpStatus.SC_OK, statusCode)) {
				//服务器异常
				log.error("httpStatus.code : {}", statusCode);
				throw new ServerErrorException(statusCode, statusLine.getReasonPhrase());
			}
            return function.apply(response.getEntity());
        } catch (Exception e) {
        	log.error("", e);
			List<Throwable> causalChain = Throwables.getCausalChain(e);
			for (Throwable throwable : causalChain) {
				if(throwable instanceof java.net.ConnectException) {
					//连接超时
					throw new ServerConnectionTimeoutException();
				} else if (throwable instanceof java.net.SocketTimeoutException){
					//请求超时
					throw new ServerSocketTimeoutException();
				}
			}
        	throw Propagater.interrupt(e);
        } finally {
			release(response);
			//closeConnect(httpclient);
        }
	}
	
	/**
	 * 创建RequestConfig对象
	 * @param timeoutConfig
	 * @param httpHost 
	 * @return
	 */
	private static RequestConfig buildRequestConfig(TimeoutConfiguration timeoutConfig, HttpHost httpHost) {
		RequestConfig.Builder builder = RequestConfig.custom();
		//从连接池中获取连接超时时间
		builder.setConnectionRequestTimeout(10_000);

		if(null != timeoutConfig) {
			if(null != timeoutConfig.getConnectTimeout()) {
				builder.setConnectTimeout(timeoutConfig.getConnectTimeout());
			}
			if(null != timeoutConfig.getReadTimeout()) {
				builder.setSocketTimeout(timeoutConfig.getReadTimeout());
			}
		}
		if(null != httpHost) {
			builder.setProxy(httpHost);
		}
		return builder.build();
	}

	private static void release(CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (IOException e) {
			// ignore
		}
	}

	private static void closeConnect(CloseableHttpClient httpclient) {
		try {
			if (httpclient != null) {
				httpclient.close();
			}
		} catch (IOException e) {
			// ignore
		}
	}
}
