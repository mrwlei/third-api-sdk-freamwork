package com.third.api.sdk.framework.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @program: third-api-sdk-freamwork
 * @description: 定制化实现序列化
 * @author: wanglei
 * @create: 2020-03-29 11:17
 **/
public class CustomerSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //TODO  个性化处理value值
        jsonGenerator.writeString(value);
    }
}
