package com.third.api.sdk.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * AES加、解密
 */
@Slf4j
public class AESUtil {

	private final static String SECRET_KEY;
	private final static String CHAR_ENCODING = org.apache.commons.codec.CharEncoding.UTF_8;
	private final static String SPEC_INIT_STRING = "16-Bytes--String";

	static {
		Properties properties = new Properties();
		InputStream resourceAsStream = AESUtil.class.getClassLoader().getResourceAsStream("AESKey.properties");
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			log.error("加载AES.SECRET_KEY失败", e);
		}
		SECRET_KEY = properties.getProperty("appAesKey");
	}

	/**
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content) {
		try {
			byte[] byteContent = content.getBytes(CHAR_ENCODING);
			byte[] enCodeFormat = SECRET_KEY.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			byte[] initParam = SPEC_INIT_STRING.getBytes();
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(1, secretKeySpec, ivParameterSpec);
			byte[] encryptedBytes = cipher.doFinal(byteContent);
			String retString = Base64.encodeBase64String(encryptedBytes);
			retString = retString.replaceAll("\r\n", "");
			retString = retString.replaceAll("\r", "");
			retString = retString.replaceAll("\n", "");
			return retString;
		} catch (Exception e) {
			log.error("加密失败 content {}, secretKey : {}", content, SECRET_KEY);
			log.error(e.getMessage(), e);
			return null;
		} 
	}

	/**
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content) {
		try {
			byte[] encryptedBytes = Base64.decodeBase64(content);
			byte[] enCodeFormat = SECRET_KEY.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
			byte[] initParam = SPEC_INIT_STRING.getBytes();
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(2, secretKey, ivParameterSpec);
			byte[] result = cipher.doFinal(encryptedBytes);
			return StringUtils.newStringUtf8(result);
		} catch (Exception e) {
			log.error("解密失败 content {}, secretKey : {}", content, SECRET_KEY);
			log.error(e.getMessage(), e);
			return null;
		} 
	}


	private AESUtil() {
	}
	
}
