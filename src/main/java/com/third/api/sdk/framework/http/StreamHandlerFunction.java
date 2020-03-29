package com.third.api.sdk.framework.http;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.third.api.sdk.framework.util.Propagater;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * stream处理方法
 * @author leiwa
 * @param <T>
 * T 方法返回类型
 */
public interface StreamHandlerFunction<T> extends Function<HttpEntity, T> {

	/**
	 * 默认的流处理方式 stream -> string
	 */
	public static StreamHandlerFunction<String> DEFAULT_INSTANCE = entity -> {
		try {
			return EntityUtils.toString(entity, Charsets.UTF_8);
		} catch (IOException e) {
			throw Propagater.interrupt(e);
		}
		
	};
}
