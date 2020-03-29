package com.third.api.sdk.framework.parameter;

import java.util.Map;

/**
 * key-value类型参数构造
 * @author leiwa
 *
 */
public interface KeyValueParameterInjector {

	/**
	 * key-value参数类型
	 * @param key
	 * @param value
	 * @return
	 */
	KeyValueParameterInjector put(String key, Object value);
	
	/**
	 * key-value参数类型
	 * 如果值为空则不会添加
	 * @param key
	 * @param value
	 * @return
	 */
	KeyValueParameterInjector putIfNotNullValue(String key, Object value);
	
	/**
	 * map参数类型
	 * @param data
	 * @return
	 */
	KeyValueParameterInjector putAll(Map<String, ?> data);
	
}
