package com.third.api.sdk.framework.parameter;

/**
 * AES加密参数构造
 * @author leiwa
 *
 */
public interface AESEncryptParameterInjector {

	/**
	 * 加密value
	 * @param key
	 * @param value 输入明文
	 * @return
	 */
	AESEncryptParameterInjector putForAES(String key, Object value);
}
