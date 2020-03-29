package com.third.api.sdk.framework.parameter;

/**
 * javaObject参数构造
 * @author leiwa
 *
 * @param <T>
 */
public interface ModelParameterInjector<T> {

	/**
	 * 添加javaObject参数类型
	 * @param model
	 * @param valid 是否开启JSR-303+验证  true-验证,false-不验证
	 * @return
	 */
	ModelParameterInjector<T> putModel(T model, boolean valid);
	
}
