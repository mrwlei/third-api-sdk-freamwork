package com.third.api.sdk.framework.http;

/**
 * http超时配置
 * @author leiwa
 *
 */
public class TimeoutConfiguration {

	/**
	 * 连接超时时间(毫秒)
	 */
	private final Integer connectTimeout;
	
	/**
	 * 请求超时时间(毫秒)
	 */
	private final Integer readTimeout;

	public TimeoutConfiguration(Integer connectTimeout, Integer readTimeout) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	@Override
	public String toString() {
		return "TimeoutConfig [connectTimeout=" + connectTimeout + "ms, readTimeout=" + readTimeout + "ms]";
	}
	
	/**
	 * 默认的请求超时设置
	 */
	public static final TimeoutConfiguration DEFAULT_INSTANCE = new TimeoutConfiguration(8_000, 8_000); 

}
