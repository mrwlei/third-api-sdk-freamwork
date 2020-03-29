package com.third.api.sdk.framework;


import com.third.api.sdk.framework.exception.ServerApiDisabledException;

/**
 * 接口信息
 * @author leiwa
 */
public enum APIConfiguration {

	S001("订单查询", 		"search/orderquery"),
	S002("用户查询", 		"search/userlist"),
	S003("交易确认查询", 		"search/confirmquery"),
	S004("xx信息查询", 		"search/infoquery");


	private final String subUrl, description;
	private APIConfiguration(String description, String subUrl) {
		this.description = description;
		this.subUrl = subUrl;
	}

	/**
	 * 获取接口描述信息
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 获取接口地址，不包含域名
	 * @return
	 */
	public String getSubUrl() {
		if(this.name().startsWith("LOCAL_")) {
			if(!APIServerContextConfiguration.INSTANCE.ENABLE_LOCAL) {
				throw new ServerApiDisabledException(this);
			}
		}else {
			if(!APIServerContextConfiguration.INSTANCE.ENABLE_HUNSUN) {
				throw new ServerApiDisabledException(this);
			}
		}
		return this.subUrl;
	}

	/**
	 * 获取请求完整url地址，域名 + 接口地址
	 * @return 完整url地址
	 */
	public String getFullUrlPath() {
		if(this.getSubUrl().startsWith("http")) {
			return this.getSubUrl();
		}
		return APIServerContextConfiguration.INSTANCE.serverDomain.concat(this.getSubUrl());
	}

}
