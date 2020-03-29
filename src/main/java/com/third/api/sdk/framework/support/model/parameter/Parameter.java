package com.third.api.sdk.framework.support.model.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @ToString
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1510036537108666360L;

	private String orderNumber = UUID.randomUUID().toString().replace("-", "");

}
