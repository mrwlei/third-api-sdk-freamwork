package com.third.api.sdk.framework.support.model.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.third.api.sdk.framework.util.jackson.AESEncodeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 请求参数 示例
 * @author leiwa
 */
@Getter @Setter @ToString(callSuper = true)
public class Parameter001 extends Parameter {
	private static final long serialVersionUID = -8276253712287384472L;

	@JsonProperty("client_id")
	private String clientId;

	@NotNull
	private String busintype;

	@NotBlank
	private String comefrom;

	@NotBlank
	@JsonSerialize(using = AESEncodeSerializer.class)
	private String identityno;

	private String subtype;

}
