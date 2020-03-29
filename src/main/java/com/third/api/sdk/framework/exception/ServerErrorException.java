package com.third.api.sdk.framework.exception;

/**
 * 服务系统错误异常
 * @author leiwa
 */
public class ServerErrorException extends RuntimeException {

	private final int statusCode;
	private static final long serialVersionUID = -2226845565215166870L;

	public ServerErrorException(int statusCode, String reasonPhrase) {
		super(reasonPhrase);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
