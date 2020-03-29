package com.third.api.sdk.framework.analyzer;

import com.google.common.reflect.TypeToken;
import com.third.api.sdk.framework.util.JSONUtil;

/**
 * 默认解析
 * @author leiwa
 * @param <T>
 */
public class JSONStringToJavaObjectAnalyzer<T> implements ResponseAnalyzer<T>{

	
	private final TypeToken<T> token;
	
	public JSONStringToJavaObjectAnalyzer(TypeToken<T> token) {
		this.token = token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T analyze(String result) {
		return JSONUtil.toJavaObject(result, (Class<T>) token.getRawType());
	}

}
