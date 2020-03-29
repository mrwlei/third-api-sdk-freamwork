package com.third.api.sdk.framework.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * jackson util
 */
@Slf4j
public class JSONUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private final static DateTimeFormatter LOCAL_TIME_PARTTEN = DateTimeFormatter.ofPattern("HH:mm:ss");
	private final static DateTimeFormatter LOCAL_DATE_PARTTEN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final static DateTimeFormatter LOCAL_DATE_TIME_PARTTEN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	static {
		JavaTimeModule timeModule = new JavaTimeModule();
		timeModule
				.addDeserializer(LocalTime.class, new LocalTimeDeserializer(LOCAL_TIME_PARTTEN))
				.addDeserializer(LocalDate.class, new LocalDateDeserializer(LOCAL_DATE_PARTTEN))
				.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(LOCAL_DATE_TIME_PARTTEN))
				.addSerializer(LocalTime.class, new LocalTimeSerializer(LOCAL_TIME_PARTTEN))
				.addSerializer(LocalDate.class, new LocalDateSerializer(LOCAL_DATE_PARTTEN))
				.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(LOCAL_DATE_TIME_PARTTEN));
		getObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.registerModule(timeModule)
				.registerModule(new ParameterNamesModule())
				.registerModule(new Jdk8Module());
	}
	
	
	/**
	 * JavaObject to JSONString
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		if (null == object) {
			return null;
		}
		try {
			return getObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("object : {}", object.toString());
			throw Propagater.interrupt(e);
		}
	}
	
	/**
	 * JavaObject to pretty JSONString
	 * 
	 * @param object
	 * @return
	 */
	public static String toPrettyJSONString(Object object) {
		if (null == object) {
			return null;
		}
		try {
			return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("object : {}", object.toString());
			throw Propagater.interrupt(e);
		}
	}
	
	/**
	 * JSONString to javaObject
	 * 
	 * @param source 
	 * @param clazz
	 * @return
	 */
	public static <T> T toJavaObject(String source, Class<T> clazz) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		try {
			return getObjectMapper().readValue(source, checkNotNull(clazz));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("source :　{}, class : {}", source, clazz);
			throw Propagater.interrupt(e);
		}
	}
	
	/**
	 * JSONString to complex javaObject
	 * 
	 * @param source
	 * @param typeReference
	 * @return
	 */
	public static <T> T toJavaObject(String source, TypeReference<T> typeReference) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		try {
			return getObjectMapper().readValue(source, checkNotNull(typeReference));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("source :　{}, type : {}", source, typeReference.getType());
			throw Propagater.interrupt(e);
		}
	}

	/**
	 * JSONString to list
	 * 
	 * @param source 
	 * @param clazz
	 * @return
	 */
    public static <T> List<T> toList(String source, Class<T> clazz) {  
       return doReadValueIntoComplexJavaType(source, List.class, clazz);
    }
    
    /**
     * JSONString to map
     * 
     * @param source
     * @param keyClazz
     * @param valueClazz
     * @return
     */
    public static <K, V> Map<K, V> toMap(String source, Class<K> keyClazz, Class<V> valueClazz) {
    	return doReadValueIntoComplexJavaType(source, Map.class, keyClazz, valueClazz);
    }
	
	/**
	 * 
	 * @param mapper
	 * @param source
	 * @param parametrized
	 * @param parameterClasses
	 * @return
	 */
	protected static <T> T doReadValueIntoComplexJavaType(String source, Class<?> parametrized, Class<?>... parameterClasses){
		try {
			JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(parametrized, parameterClasses);  
			return getObjectMapper().readValue(source, javaType);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			log.error("source :　{}, parametrized : {}, parameterClasses : {}", source, parametrized, ArrayUtils.toString(parameterClasses));
			throw Propagater.interrupt(e);
		}
	}
	
	private JSONUtil() {
	}

	
	public static void configure(SerializationFeature feature, boolean state) {
		getObjectMapper().configure(feature, state);
	}
	
	public static void configure(DeserializationFeature feature, boolean state) {
		getObjectMapper().configure(feature, state);
	}
	
	public static void registerModule(Module module) {
		getObjectMapper().registerModule(module);
	}
	
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static void setObjectMapper(ObjectMapper objectMapper) {
		JSONUtil.objectMapper = objectMapper;
	}

	
}