package com.third.api.sdk.framework.util;

/**
 * @author leiwa
 */
public class StringAppender {
	private StringAppender() {
	}

	public static String fluentAppend(Object obj, Object... objects){
		StringBuilder builder = new StringBuilder(String.valueOf(obj));
		for (Object object : objects) {
			builder.append(object);
		}
		return builder.toString();
	}
	

	
}
