package com.third.api.sdk.framework.listener;

import com.third.api.sdk.framework.APIConfiguration;
import com.third.api.sdk.framework.http.TimeoutConfiguration;
import com.third.api.sdk.framework.parameter.InternalParameterWrapper;
import com.third.api.sdk.framework.util.SpringStopWatch;
import org.slf4j.Logger;

/**
 * 默认的全局listener
 * @author leiwa
 */
public class StopWatchTrackLogListener implements ExecutionCycleListener {

	private final Logger log;
	private SpringStopWatch stopWatch;
	private APIConfiguration apiConfiguration;
	
	public StopWatchTrackLogListener(Logger log) {
		this.log = log;
	}

	/**
	 * 初始化时，打印接口配置信息
	 * @param apiConfiguration
	 */
	@Override
	public void init(APIConfiguration apiConfiguration) {
		this.apiConfiguration = apiConfiguration;
		String apiName = apiConfiguration.name();
		stopWatch = new SpringStopWatch("ID=" + apiName);
		log.info("\n");
		log.info("==============接口- [{}{}]开始 ==============", apiName, this.apiConfiguration.getDescription());
	}

	/**
	 * 请求之前
	 * @param wrapper
	 * @param timeoutConfig
	 */
	@Override
	public void beforeHttpExecution(InternalParameterWrapper wrapper, TimeoutConfiguration timeoutConfig) {
		stopWatch.start("do request");		
	}

	/**
	 * 请求之后
	 * @param responseString
	 */
	@Override
	public void afterHttpExecution(String responseString) {
		log.info("result : {}", responseString);
		stopWatch.stop();		
	}

	/**
	 * 响应信息转换之前
	 * @param responseString
	 */
	@Override
	public void beforResultAnalyzing(String responseString) {
		stopWatch.start("result analyze");
	}

	/**
	 * 响应信息转换之后
	 * @param responseJavaObject
	 */
	@Override
	public void afterResultAnalyzing(Object responseJavaObject) {
		log.debug("javaObject : {}", responseJavaObject);
		stopWatch.stop();
	}

	/**
	 * 整个调用过程完成
	 */
	@Override
	public void complete() {
		log.info("\n {}", stopWatch.prettyPrint());		
		log.info("==============接口- [{}{}]结束 ==============", this.apiConfiguration.name(), this.apiConfiguration.getDescription());
		log.info("\n");
	}


}
