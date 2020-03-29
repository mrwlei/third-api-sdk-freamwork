package com.third.api.sdk.framework.support.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.third.api.sdk.framework.util.JSONUtil;
import com.third.api.sdk.framework.util.Propagater;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter @Setter @ToString
@Slf4j
public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6426996494283090113L;
	/**
	 * 返回码
	 */
	private String code;
	/**
	 * 消息
	 */
	private String message;
	/**
	 * 请求流水号
	 */
	private String orderNumber;

	/**
	 * 
	 * @return
	 */
	public String toJSONString(){
		try {
			return JSONUtil.toJSONString(this);
		} catch (Exception e) {	
			//ignore
			log.error("TO_JSON_STRING ERROR", e);
			throw Propagater.interrupt(e);
		}
	}
	
	/**
	 * 格式化后的JSONString
	 * @return
	 */
	public String toPrettyJSONString(){
		try {
			return JSONUtil.toPrettyJSONString(this);
		} catch (Exception e) {
			//ignore
			log.error("TO_PRETTY_JSON_STRING ERROR", e);
			throw Propagater.interrupt(e);
		}
	}
	
	/**
	 * 当前请求是否正常返回
	 * @return
	 */
	@JsonIgnore
	public boolean isSuccess() {
		return Objects.equal("0000", this.code);
	}

}
