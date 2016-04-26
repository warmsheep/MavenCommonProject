package org.warmsheep.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {
	
	private static final Log logger = LogFactory.getLog(Base64.class);
	
	private static BASE64Encoder encoder = new BASE64Encoder();

	private static BASE64Decoder decoder = new BASE64Decoder();
	
	/**
	 * 对字符串进行加密
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		return encode(str.getBytes());
	}
	/**
	 * 对数组进行加密
	 * @param str
	 * @return
	 */
	public static String encode(byte[] b) {
		if(b == null){
			return null;
		}
		return encoder.encode(b);
	}
	/**
	 * 对加密的信息进行解密
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		if(str == null){
			return null;
		}
		try {
			byte[] temp = decoder.decodeBuffer(str);
			String result = new String(temp);
			temp = null;
			return result;
		} catch (IOException e) {
			logger.error("解密[" + str + "]出错" + e);
			 return null;
		}
	}
	/**
	 * 对加密的信息进行解密
	 * @param str
	 * @return
	 */
	public static String decode(byte[] b) {
		return decode(new String(b));
	}

}