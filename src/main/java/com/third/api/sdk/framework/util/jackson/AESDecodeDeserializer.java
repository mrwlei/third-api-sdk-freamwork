package com.third.api.sdk.framework.util.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.third.api.sdk.framework.util.AESUtil;

import java.io.IOException;

/**
 * AES解密
 */
public class AESDecodeDeserializer extends JsonDeserializer<String>{

	@Override
	public String deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		String text = parser.getText();
		if(null == text) {
			return null;
		}
		return AESUtil.decrypt(text);
	}

}
