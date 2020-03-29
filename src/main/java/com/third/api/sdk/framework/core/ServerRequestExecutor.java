package com.third.api.sdk.framework.core;

import com.google.common.base.Preconditions;
import com.third.api.sdk.framework.http.HttpAsyncClientUtil;
import com.third.api.sdk.framework.http.HttpRequestMethodType;
import com.third.api.sdk.framework.http.StreamHandlerFunction;
import com.third.api.sdk.framework.http.TimeoutConfiguration;
import com.third.api.sdk.framework.parameter.InternalParameterWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.Builder;
import org.apache.http.HttpHost;

import java.util.Map;

/**
 * builder  &&   executor
 * API请求执行对象
 * @author leiwa
 */
@Slf4j
public class ServerRequestExecutor implements Executor<String>{

	
	/**
	 * 请求地址
	 */
	private final String url;
	
	/**
	 * 请求超时对象
	 */
	private final TimeoutConfiguration timeoutConfig;
	
	/**
	 * http请求方式
	 */
	private final HttpRequestMethodType methodType;
	
	private final HttpHost httphost;
	
	/**
	 * 响应信息处理方法
	 */
	private final StreamHandlerFunction<String> streamHandlerFunction;
	
	public static ServerRequestBuilder newBuilder() {
		return new ServerRequestBuilder();
	}

	private ServerRequestExecutor(String url, TimeoutConfiguration timeoutConfig, HttpRequestMethodType methodType, HttpHost httphost) {
		this.url = url;
		this.timeoutConfig = null == timeoutConfig ? TimeoutConfiguration.DEFAULT_INSTANCE : timeoutConfig;
		this.streamHandlerFunction = StreamHandlerFunction.DEFAULT_INSTANCE;
		this.methodType = methodType;
		this.httphost = httphost;
	}
	
	@Override
	public String execute() {
		log.info("timeoutConfig :  {} ", this.timeoutConfig);
		if(null != this.httphost) {
			log.info("use proxy : {}", httphost);
		}
		if(HttpRequestMethodType.POST.equals(this.methodType)) {
			return HttpAsyncClientUtil.post(this.url, this.streamHandlerFunction, this.timeoutConfig, this.httphost);
		} else {
			return HttpAsyncClientUtil.get(this.url, this.streamHandlerFunction, this.timeoutConfig, this.httphost);
		}
	}
	

	public static class ServerRequestBuilder implements Builder<ServerRequestExecutor> {

		private TimeoutConfiguration timeoutConfig;
		private InternalParameterWrapper parameter;
		private String url;
		private HttpRequestMethodType methodType = HttpRequestMethodType.POST;
		private HttpHost httphost;
		
		public ServerRequestBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		public ServerRequestBuilder timeoutConfig(TimeoutConfiguration timeoutConfig) {
			this.timeoutConfig = timeoutConfig;
			return this;
		}
		
		public ServerRequestBuilder proxy(HttpHost httphost) {
			this.httphost = httphost;
			return this;
		}
		
		public ServerRequestBuilder parameter(Map<String, ?> parameterMap) {
			InternalParameterWrapper parameterWrapper = new InternalParameterWrapper();
			parameterWrapper.putAll(parameterMap);
			this.parameter = parameterWrapper;
			return this;
		}
		
		public ServerRequestBuilder parameter(InternalParameterWrapper parameterWrapper) {
			this.parameter = parameterWrapper;
			return this;
		}
		
		public ServerRequestBuilder methodType(HttpRequestMethodType methodType) {
			this.methodType = methodType;
			return this;
		}
		
		@Override
		public ServerRequestExecutor build() {
			Preconditions.checkNotNull(this.url, "请求url不能为空");
			String parameterString = this.parameter.convertToParameterString();
			StringBuilder stringBuilder = new StringBuilder();
			String realUrl = stringBuilder.append(this.url).append("?").append(parameterString).toString();
			log.info("url : {}", this.url);
			log.debug("realUrl : {}", realUrl);
			log.info("parameters : {}", this.parameter);
			return new ServerRequestExecutor(realUrl, this.timeoutConfig, this.methodType, this.httphost);
		}
	}
	
}
