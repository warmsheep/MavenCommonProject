package org.warmsheep.framework.web.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * HttpServletResponse帮助类
 */
public final class ResponseUtils {
	
	public static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	/**
	 * 发送文本。使用UTF-8编码。
	 * @param response HttpServletResponse
	 * @param text 发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * @param response HttpServletResponse
	 * @param text 发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	/**
	 * 发送结果编码
	 * @param response
	 * @param code	响应码
	 * @param text	文本
	 * @param nextUrl 下一个跳转页面
	 */
	public static void renderResultJsonNextUrl(HttpServletResponse response,String code, String text,String nextUrl) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", code);
		jsonObj.put("message", text);
		jsonObj.put("nextUrl", nextUrl);
		render(response, "application/json;charset=UTF-8", jsonObj.toString());
	}
	
	/**
	 * 发送结果编码
	 * @param response
	 * @param code	响应码
	 * @param text	文本
	 */
	public static void renderResultJson(HttpServletResponse response,String code, String text) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", code);
		jsonObj.put("message", text);
		render(response, "application/json;charset=UTF-8", jsonObj.toString());
	}
	
	/**
	 * 发送结果编码
	 * @param response
	 * @param code	响应码
	 * @param text	文本
	 */
	public static void renderResultJson(HttpServletResponse response,String code, String text,Object data) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", code);
		jsonObj.put("message", text);
		jsonObj.put("data", data);
		render(response, "application/json;charset=UTF-8", jsonObj.toString());
	}
	
	/**
	 * 发送xml。使用UTF-8编码。
	 * @param response HttpServletResponse
	 * @param text 发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType,
			String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
