package com.third.api.sdk.framework.listener;


import com.third.api.sdk.framework.APIConfiguration;
import com.third.api.sdk.framework.http.TimeoutConfiguration;
import com.third.api.sdk.framework.parameter.InternalParameterWrapper;

/**
 * 执行全过程Listener
 * @author leiwa
 *
 */
public interface ExecutionCycleListener {

	/**
	 * 初始化
	 * @param apiConfiguration
	 */
	void init(APIConfiguration apiConfiguration);
	
	/**
	 * 请求之前
	 * @param wrapper
	 * @param timeoutConfig
	 */
	void beforeHttpExecution(InternalParameterWrapper wrapper, TimeoutConfiguration timeoutConfig);
	
	/**
	 * 请求之后
	 * @param result
	 */
	void afterHttpExecution(String result);
	
	/**
	 * 响应信息转换之前
	 * @param responseString
	 */
	void beforResultAnalyzing(String responseString);
	
	/**
	 * 响应信息转换之后
	 * @param responseJavaObject
	 */
	void afterResultAnalyzing(Object responseJavaObject);
	
	/**
	 * 完成
	 */
	void complete();
	
}
