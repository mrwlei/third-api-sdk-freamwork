package com.third.api.sdk.framework.exception;

/**
 * 参数构造异常
 * @author leiwa
 *
 */
public class RequestParameterBuildingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6235211591879033455L;

	public RequestParameterBuildingException() {
	}
	
	public RequestParameterBuildingException(String message) {
		super(message);
	}
	
}
