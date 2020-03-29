package com.third.api.sdk.framework.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.third.api.sdk.framework.util.AESUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * AES加密
 */
public class AESEncodeSerializer extends JsonSerializer<String>{

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		String encodeValue = null;
		if(StringUtils.isNotBlank(value)) {
			encodeValue = AESUtil.encrypt(value);
		}
		jsonGenerator.writeString(encodeValue);
	}

}
