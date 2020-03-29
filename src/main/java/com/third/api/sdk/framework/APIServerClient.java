package com.third.api.sdk.framework;

import com.third.api.sdk.framework.support.executor.alias.OrderQueryExecutor;
import com.third.api.sdk.framework.support.model.parameter.alias.OrderQueryParameter;
import com.third.api.sdk.framework.support.model.response.alias.OrderQueryResponse;

/**
 * @program: third-api-sdk-freamwork
 * @description: 第三方服务客户端调用
 * @author: wanglei
 * @create: 2020-03-29 21:27
 **/
public class APIServerClient {

    public static final boolean ENABLE_VALID = true;

    public static final boolean DISABLE_VALID = false;

    private APIServerClient(){}

    /**
     * 请求示例
     * @param parameter
     * @return
     */
    public static OrderQueryResponse orderQuery(OrderQueryParameter parameter) {
        return new OrderQueryExecutor().putModel(parameter,ENABLE_VALID).execute();
    }


}
