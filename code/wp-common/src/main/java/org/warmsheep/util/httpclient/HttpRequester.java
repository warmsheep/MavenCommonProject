package org.warmsheep.util.httpclient;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * HTTP请求对象
 * @author Warmsheep
 */
public class HttpRequester {

	private static final String defaultContentEncoding = "UTF-8";

	public HttpRequester() {
	}

	public static JSONObject sendPostJson(String url, JSONObject jsonParam) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		JSONObject jsonResult = null;
		HttpPost method = new HttpPost(url);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), defaultContentEncoding);
				entity.setContentEncoding(defaultContentEncoding);
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, defaultContentEncoding);
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(result.getEntity());
					if (str != null && !"".equals(str)) {
						/** 把json字符串转换成json对象 **/
						jsonResult = JSONObject.parseObject(str);
					}
				} catch (Exception e) {
					System.out.println(("post请求提交失败:" + url + e));
				}
			}
		} catch (IOException e) {
			System.out.println("post请求提交失败:" + url + e);
		}
		return jsonResult;
	}

}
