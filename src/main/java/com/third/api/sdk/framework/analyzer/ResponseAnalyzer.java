package com.third.api.sdk.framework.analyzer;

/**
 * 服务响应信息解析
 * @author leiwa
 *
 * @param <T>
 */
public interface ResponseAnalyzer<T> {

	/**
	 * @param result
	 * @return
	 */
	T analyze(String result);
	
}
