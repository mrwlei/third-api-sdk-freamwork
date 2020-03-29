package com.third.api.sdk.framework;

import org.apache.http.HttpHost;

/**
 * 1、设置serverDomain，服务器地址
 * 2、Client 调用
 * 请求接口域名配置信息
 * @author leiwa
 */
public enum APIServerContextConfiguration {

	INSTANCE;
	
	/**
	 * 请求API地址
	 */
	public String serverDomain = "https://localhost:8080/";


	public HttpHost httpHost;
	
	/**
	 * 本地服务是否可用
	 */
	public boolean ENABLE_LOCAL = true;
	
	/**
	 * 服务是否可用
	 */
	public boolean ENABLE_HUNSUN = true;
	
}
