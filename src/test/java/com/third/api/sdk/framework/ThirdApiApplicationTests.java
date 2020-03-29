package com.third.api.sdk.framework;

import com.third.api.sdk.framework.support.model.parameter.alias.OrderQueryParameter;
import com.third.api.sdk.framework.support.model.response.alias.OrderQueryResponse;

public class ThirdApiApplicationTests {
    public static void main(String[] args) {
        // 示例 示例 示例
        OrderQueryParameter parameter = new OrderQueryParameter();
        parameter.setClientId("1");
        parameter.setBusintype("1");
        parameter.setComefrom("1");
        parameter.setIdentityno("1");
        parameter.setSubtype("1");
        parameter.setOrderNumber("1");
        OrderQueryResponse response = APIServerClient.orderQuery(parameter);


        System.out.println(response);

    }
}
