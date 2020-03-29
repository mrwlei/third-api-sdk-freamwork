package com.third.api.sdk.framework.parameter;

import com.third.api.sdk.framework.exception.RequestParameterBuildingException;
import com.third.api.sdk.framework.util.AESUtil;
import com.third.api.sdk.framework.util.JSONUtil;
import com.third.api.sdk.framework.util.StringAppender;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 参数构造器
 * @author leiwa
 */
public class CompositeParameterBuilder implements KeyValueParameterInjector, ModelParameterInjector<Object>, AESEncryptParameterInjector, Builder<InternalParameterWrapper>{
	
	private final InternalParameterWrapper wrapper = new InternalParameterWrapper();
	/**
	 * jsr-303+ validator
	 */
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Override
	public CompositeParameterBuilder put(String key, Object value) {
		this.wrapper.put(key, value);
		return this;
	}
	
	@Override
	public CompositeParameterBuilder putIfNotNullValue(String key, Object value) {
		if(null != value) {
			return put(key, value);
		}
		return this;
	}
	
	@Override
	public CompositeParameterBuilder putAll(Map<String, ?> data) {
		this.wrapper.putAll(data);
		return this;
	}
	
	@Override
	public CompositeParameterBuilder putForAES(String key, Object value) {
		checkNotNull(value);
		this.put(key, AESUtil.encrypt(value.toString()));
		return this;
	}
	
	@Override
	public CompositeParameterBuilder putModel(Object model, boolean valid) {
		/**
		 * 实质上仍然使用map存放数据
		 * javaobject->json->map
		 * 如果验证失败,抛出异常
		 */
		if(null != model) {
			if(valid) {
				//JSR-303+ validation
				Set<ConstraintViolation<Object>> validate = validator.validate(model);
				for (ConstraintViolation<Object> constraintViolation : validate) {
					String messageTemplate = constraintViolation.getMessageTemplate();
					String execeptionMessage;
					if(StringUtils.startsWith(messageTemplate, "{")) {
						String subMessage = constraintViolation.getMessage();
						Path propertyPath = constraintViolation.getPropertyPath();
						execeptionMessage = StringAppender.fluentAppend(propertyPath, subMessage);
					} else {
						execeptionMessage = constraintViolation.getMessage();
					}
					throw new RequestParameterBuildingException(execeptionMessage);
				}
			}
			String modelJSONString = JSONUtil.toJSONString(model);
			Map<String, Object> rawData = JSONUtil.toMap(modelJSONString, String.class, Object.class);
			this.putAll(rawData);
		}
		return this;
	}
	
	@Override
	public InternalParameterWrapper build() {
		return this.wrapper;
	}


}
