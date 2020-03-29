package com.third.api.sdk.framework.core;

import com.google.common.base.Throwables;
import com.google.common.reflect.TypeToken;
import com.third.api.sdk.framework.APIConfiguration;
import com.third.api.sdk.framework.APIServerContextConfiguration;
import com.third.api.sdk.framework.analyzer.JSONStringToJavaObjectAnalyzer;
import com.third.api.sdk.framework.analyzer.ResponseAnalyzer;
import com.third.api.sdk.framework.http.HttpRequestMethodType;
import com.third.api.sdk.framework.http.TimeoutConfiguration;
import com.third.api.sdk.framework.listener.ExecutionCycleListener;
import com.third.api.sdk.framework.listener.StopWatchTrackLogListener;
import com.third.api.sdk.framework.parameter.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 核心请求类
 * @author leiwa
 */
@Slf4j
public abstract class AbstractApiContext<PARAMETER, RESPONSE> implements ModelParameterInjector<PARAMETER>, KeyValueParameterInjector, AESEncryptParameterInjector, Executor<RESPONSE> {

	/**
	 * 参数构造对象
	 */
	protected CompositeParameterBuilder compositeParameterBuilder = new CompositeParameterBuilder();

	/**
	 * 连接超时时间 ms
	 */
	protected Integer connectTimeout = 8_000;
	
	/**
	 * 请求超时时间 ms
	 */
	protected Integer readTimeout = 8_000;
	
	/**
	 * 当前泛型对应的实际TypeToken
	 */
	protected final TypeToken<RESPONSE> token = new TypeToken<RESPONSE>(this.getClass()) {
		private static final long serialVersionUID = 1L;
	};
	
	/**
	 * 当前泛型对应的实际类型
	 */
	protected final Class<? super RESPONSE> responseRealMappingClass = token.getRawType();
	
	/**
	 * 响应信息解析对象
	 */
    protected ResponseAnalyzer<RESPONSE> responseAnalyzer = new JSONStringToJavaObjectAnalyzer<RESPONSE>(token);

    /**
     * 全局执行过程listener
     */
    private ExecutionCycleListener cycleListener = new StopWatchTrackLogListener(log);
    
	/**
	 * 添加参数
	 * proxy
	 */
	@Override
	public AbstractApiContext<PARAMETER, RESPONSE> put(String key, Object value) {
		this.compositeParameterBuilder.put(key, value);
		return this;
	}

	/**
	 * 添加参数
	 * proxy
	 */
	@Override
	public AbstractApiContext<PARAMETER, RESPONSE> putIfNotNullValue(String key, Object value) {
		this.compositeParameterBuilder.putIfNotNullValue(key, value);
		return this;
	}

	/**
	 * 添加参数
	 * proxy
	 */
	@Override
	public AbstractApiContext<PARAMETER, RESPONSE> putAll(Map<String, ?> data) {
		this.compositeParameterBuilder.putAll(data);
		return this;
	}

	/**
	 * 添加参数
	 * proxy
	 */
	@Override
	public AbstractApiContext<PARAMETER, RESPONSE> putModel(PARAMETER model, boolean valid) {
		this.compositeParameterBuilder.putModel(model, valid);
		return this;
	}
	
	/**
	 * 添加参数
	 * proxy
	 */
	@Override
	public AESEncryptParameterInjector putForAES(String key, Object value) {
		this.compositeParameterBuilder.putForAES(key, value);
		return this;
	}
	
	/**
	 * 设置连接超时时间
	 * @param connectTimeout 
	 * @return
	 */
	public AbstractApiContext<PARAMETER, RESPONSE> connectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}
	
	/**
	 * 设置请求超时时间
	 * @param readTimeout
	 * @return
	 */
	public AbstractApiContext<PARAMETER, RESPONSE> readTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}
	
	/**
	 * 设置响应信息解析对象
	 * @param analyzer
	 * @return
	 */
	public AbstractApiContext<PARAMETER, RESPONSE> replaceResponseAnalyzer(ResponseAnalyzer<RESPONSE> analyzer) {
		this.responseAnalyzer = analyzer;
		return this;
	}
	
	/**
	 * 设置全局执行过程listener
	 * @param listener
	 * @return
	 */
	public AbstractApiContext<PARAMETER, RESPONSE> replaceExecutionCycleListener(ExecutionCycleListener listener) {
		this.cycleListener = listener;
		return this;
	}
	
	/**
	 * 执行请求并返回结果
	 * @return
	 */
	@Override
	public RESPONSE execute() {
		APIConfiguration configuration = checkNotNull(this.getConfiguration());
		String urlPath = checkNotNull(configuration.getFullUrlPath());
		//########################START###########
		RESPONSE response;
		try {
			this.cycleListener.init(configuration);
			//########################参数、组件构造###########################################
			TimeoutConfiguration timeoutConfig = new TimeoutConfiguration(this.connectTimeout, this.readTimeout);
			//签名
			InternalParameterWrapper wrapper = this.compositeParameterBuilder.build().sign();
			//创建http请求组件对象
			Executor<String> executor = ServerRequestExecutor.newBuilder()
					.url(urlPath)
					.parameter(wrapper)
					.timeoutConfig(timeoutConfig)
					.proxy(APIServerContextConfiguration.INSTANCE.httpHost)
					.methodType(this.getRequestMethodType())
					.build();
			//########################执行http请求(涉及到请求超时、连接超时的处理)######################
			this.cycleListener.beforeHttpExecution(wrapper, timeoutConfig);
			//http请求
			String result = null;
			try {
				result = executor.execute();
			} catch (Exception e) {
				log.error("http请求失败", e);
				Throwables.throwIfUnchecked(e);
			}
			this.cycleListener.afterHttpExecution(result);
			//########################结果解析(目前值支持stream -> string -> javaObject)###########
			this.cycleListener.beforResultAnalyzing(result);
			response = this.responseAnalyzer.analyze(result);
			this.cycleListener.afterResultAnalyzing(response);
			//########################complete######################
		} finally {
			this.cycleListener.complete();
		}
		return response;
	}
	
	/**
	 * 获取orderNumber
	 * @return
	 */
	public String getOrderNumber() {
		return (String) this.compositeParameterBuilder.build().getOrderNumer();
	}
	
	/**
	 * 获取接口信息
	 * @return
	 */
	public abstract APIConfiguration getConfiguration();
	
	public HttpRequestMethodType getRequestMethodType(){
		return HttpRequestMethodType.POST;
	}
	
}
