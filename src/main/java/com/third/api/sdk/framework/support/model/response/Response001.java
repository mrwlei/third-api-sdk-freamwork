package com.third.api.sdk.framework.support.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.third.api.sdk.framework.util.jackson.AESDecodeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 返回数据 示例
 */
@Getter @Setter @ToString(callSuper = true)
public class Response001 extends Response {
	private static final long serialVersionUID = 6491313086673003691L;
	
	@JsonProperty("returnList")
	private List<DataItem> dataItems;
	
	@Getter @Setter @ToString(callSuper = true)
	public static class DataItem {

		@JsonDeserialize(using = AESDecodeDeserializer.class)
		private String identityno;

		private BigDecimal minfare;

		@JsonProperty("max_hold")
		private Integer maxhold;

		private String faretype;

	}

}
