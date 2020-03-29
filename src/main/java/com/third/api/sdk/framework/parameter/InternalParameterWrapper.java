package com.third.api.sdk.framework.parameter;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * api请求参数对象
 * @author leiwa
 *
 */
@Slf4j
public class InternalParameterWrapper extends TreeMap<String, Object> implements SortedMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 560310735899246913L;

	
	/**
	 * 签名(可重复调用)
	 * @return
	 */
	public InternalParameterWrapper sign() {
		this.filterNullValue();
		this.putIfAbsent("orderNumber", UUID.randomUUID().toString().replace("-", ""));
		if(!this.containsKey("sign")) {
			String paramsString = convertToParameterString();
			log.debug("signing : {}", paramsString);
			String sign = DigestUtils.md5Hex(paramsString);
			log.debug("sign : {}", sign);
			this.doUrlEncode();
			this.putIfAbsent("sign", sign);
		}
		return this;
	}
	
	public String getOrderNumer(){
		return (String) this.get("orderNumber");
	}
	
	/**
	 * 为保证签名正确,过滤null值的键值对
	 */
	public void filterNullValue() {
		Iterator<String> iterator = this.keySet().iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			if(null == this.get(next)) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * map参数 转换成 key1=value1&key2=value2的字符串
	 * @return
	 */
	public String convertToParameterString() {
		return Joiner.on("&").withKeyValueSeparator("=").join(this);
	}
	
	/**
	 * value-encode
	 */
	public void doUrlEncode() {
		for (Map.Entry<String, Object> entry : this.entrySet()) {
			String value = Objects.toString(checkNotNull(entry.getValue()));
			try {
				this.put(entry.getKey(), URLEncoder.encode(value, CharEncoding.UTF_8));
			} catch (UnsupportedEncodingException e) {
				//ignore
			}
		}
	}
}
