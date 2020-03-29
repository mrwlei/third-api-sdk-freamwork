package com.third.api.sdk.framework.exception;


import com.third.api.sdk.framework.APIConfiguration;
import com.third.api.sdk.framework.util.StringAppender;

/**
 * 服务不可用自定义异常
 * @author leiwa
 */
public class ServerApiDisabledException extends RuntimeException {

	private static final long serialVersionUID = -6657773883741934590L;

	private final APIConfiguration apiConfiguration;

	public ServerApiDisabledException(APIConfiguration apiConfiguration) {
		super(StringAppender.fluentAppend("ServerApi  [ ", apiConfiguration.name(), " - ", apiConfiguration.getDescription(), " ] is disabled "));
		this.apiConfiguration = apiConfiguration ;
	}

	public APIConfiguration getApiConfiguration() {
		return apiConfiguration;
	}

}
