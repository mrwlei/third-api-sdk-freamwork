package com.third.api.sdk.framework.support.executor;

import com.third.api.sdk.framework.APIConfiguration;
import com.third.api.sdk.framework.core.AbstractApiContext;
import com.third.api.sdk.framework.support.model.parameter.Parameter001;
import com.third.api.sdk.framework.support.model.response.Response001;

/**
 * 请求接口 测试用例
 * @author leiwa
 *
 * @param <T>
 * @param <M>
 */
public class S001Executor<T extends Parameter001, M extends Response001> extends AbstractApiContext<T, M> {

	@Override
	public APIConfiguration getConfiguration() {
		return APIConfiguration.S001;
	}

}
