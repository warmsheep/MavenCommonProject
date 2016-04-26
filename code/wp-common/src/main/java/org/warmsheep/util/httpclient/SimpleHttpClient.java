package org.warmsheep.util.httpclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.warmsheep.util.StringUtils;


public class SimpleHttpClient {

	/**
	 * 发送xml数据请求到server端
	 * @param url xml请求数据地址
	 * @param xmlStr 发送的xml数据流
	 * @return null发送失败，否则返回响应内容
	 * @throws Exception 
	 */
	public static String httpPostXmlStr(String url, String xmlStr) throws Exception {
		// 关闭
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");

		// 创建httpclient工具对象
		HttpClient client = new HttpClient();
		// 创建post请求方法
		PostMethod myPost = new PostMethod(url);
		// 设置请求超时时间
		client.setConnectionTimeout(300 * 1000);
		String responseString = null;
		try {
			// 设置请求头部类型
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
			// 设置请求体，即xml文本内容，注：这里写了两种方式，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
			myPost.setRequestBody(xmlStr);
			myPost.setRequestEntity(new StringRequestEntity(xmlStr, "text/xml", "utf-8"));

			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				responseString = new String(strByte, 0, strByte.length, "utf-8");
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			throw e;
		}
		myPost.releaseConnection();
		return responseString;
	}

	/**
	 * 发送xml数据请求到server端
	 * @param url xml请求数据地址
	 * @param xmlFileName 发送的xml数据流
	 * @return null发送失败，否则返回响应内容
	 * @throws Exception 
	 */
	public static String httpPostXmlFile(String url, String xmlFileName) throws Exception {
		// 关闭
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");

		// 创建httpclient工具对象
		HttpClient client = new HttpClient();
		// 创建post请求方法
		PostMethod myPost = new PostMethod(url);
		// 设置请求超时时间
		client.setConnectionTimeout(300 * 1000);
		String responseString = null;
		try {
			// 设置请求头部类型
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");

			InputStream input = new FileInputStream(new File(xmlFileName));
			myPost.setRequestBody(input);
			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				responseString = new String(strByte, 0, strByte.length, "utf-8");
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			throw e;
		}
		myPost.releaseConnection();
		return responseString;
	}

	/**
	 * 用传统的URI类进行请求
	 * @param urlStr
	 */
	public static Document httpPostXml(String urlStr, String xmlInfo) {
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			
			String s = "";
			if (StringUtils.isNotBlank(xmlInfo))
				s = new String(xmlInfo.getBytes("UTF-8"));
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(s);
			out.flush();
			out.close();
			
			//测试
//			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String line = "";
//			System.out.println("接收数据如下：");
//			for (line = br.readLine(); line != null; line = br.readLine()) {
//				System.out.println("-->"+line);
//			}
//			System.out.println("接收数据END");
			//测试
			
			//解析对方发来的xml数据 
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(con.getInputStream());
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 发送http请求
	 * @param url 请求地址
	 * @param xmlData 请求数据
	 * @return
	 */
	public static Document sendHttpPostXml(String url, String xmlData) {
		HttpURLConnection urlConnection = null;
		try {
			URL aURL = new URL(url);
			urlConnection = (HttpURLConnection) aURL.openConnection();
		    urlConnection.setUseCaches(false);
		    urlConnection.setRequestMethod("POST");
		    urlConnection.setDoInput(true);
		    urlConnection.setDoOutput(true);
		    urlConnection.setUseCaches(false);
		    urlConnection.setRequestProperty("Content-Type", "text/xml; charset=GBK");
		    urlConnection.setRequestProperty("User-Agent", "AppClient");
		    
		    urlConnection.connect();
		    if (StringUtils.isNotBlank(xmlData)){
		        urlConnection.getOutputStream().write(xmlData.getBytes());
			}
		    urlConnection.getResponseCode();
		    int contentLen = urlConnection.getContentLength();
		
		    java.io.DataInputStream in = new java.io.DataInputStream(urlConnection.getInputStream());
		    byte buffer[] = new byte[contentLen];
		    int len = 0;
		    while (len < contentLen) {
		        int remainedLen = contentLen - len;
		        if (remainedLen > 1024)
		            remainedLen = 1024;
		        int readLen = in.read(buffer, len, remainedLen);
		        if (readLen == -1 || readLen == 0) {
		            break;
		        }
		        len = len + readLen;
		    }
		   	String result = new String(buffer, 0, contentLen);
			System.out.println("==> 返回结果："+result);
			//
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		    urlConnection = null;
		}
		return null;
	}
	
	/**
	 * 获取document解析中的具体某个参数值
	 * @param doc
	 * @param tagName
	 * @return
	 */
	public static String getValueByTagName(Document doc, String tagName){
		if(doc == null || StringUtils.isBlank(tagName))
			return "";
		NodeList pl = doc.getElementsByTagName(tagName);
		if(pl != null && pl.getLength() > 0) {
			return pl.item(0).getTextContent();
		}
		return "";
	}
	
	//测试
	public static void main(String[] args) {
		try {
//			final String url = "https://60.212.43.212:7011/APISessionReqServlet?opName=CebankUserLogonOp";
			final String url = "http://www.baidu.com";
			final String xmlInfo = "MIIFcgYJKoZIhvcNAQcCoIIFYzCCBV8CAQExCzAJBgUrDgMCGgUAMFwGCSqGSIb3DQEHAaBPBE0jJHNlcmlhbE5vPUhGLVNMMTQ2MTU3MzE5OTQ1NCMkY3N0Tm89IyRyZXFUaW1lPTIwMTYwNDI1MTYzMzE5IyR1c2VyUFdEPTExMTExMaCCA9YwggPSMIICuqADAgECAhAQAAAAAAAAAAAAACARCSmAMA0GCSqGSIb3DQEBBQUAMFkxCzAJBgNVBAYTAkNOMTAwLgYDVQQKEydDaGluYSBGaW5hbmNpYWwgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkxGDAWBgNVBAMTD0NGQ0EgVEVTVCBPQ0ExMTAeFw0xNjA0MDUwMTQ3MzZaFw0xODA0MDUwMTQ3MzZaMIGHMQswCQYDVQQGEwJDTjEVMBMGA1UEChMMQ0ZDQSBURVNUIENBMQ8wDQYDVQQLEwZDQ0ZDQ0IxFDASBgNVBAsTC0VudGVycHJpc2VzMTowOAYDVQQDFDEwNDFAODM3MDY4NTIyODAwMzY4MkAxMDAwMDAwMDAwMDk3NTBoZnlxQDAwMDAwMDAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCSmIAMt8Z37ZCgBIwN++Yg2l8e346xLreNIlA9Cc+sfhRdBe4MCIlPutrtYtR6WDK6UIEENrJ5l+XbCPJBoQAnrS6KFaMZEkMEW2JMDsljG6AFE0v4aceaIyeMUpPUw3GOwvynPDujdWXXgnnRWb9Cw5fJ9vuGAiesCY9CYrwRwIDAQABo4HqMIHnMB8GA1UdIwQYMBaAFPwLvESaDjGhg6mBhyceBULGv1b4MEgGA1UdIARBMD8wPQYIYIEchu8qAQIwMTAvBggrBgEFBQcCARYjaHR0cDovL3d3dy5jZmNhLmNvbS5jbi91cy91cy0xNS5odG0wOQYDVR0fBDIwMDAuoCygKoYoaHR0cDovLzIxMC43NC40Mi4zL09DQTExL1JTQS9jcmw5ODg3LmNybDALBgNVHQ8EBAMCA+gwHQYDVR0OBBYEFCRAJLxhdOymzM6Ug1u3lJRnveHRMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBBQUAA4IBAQAw/Vwo2xsrN9hcvcwHRm+GwrwQ6Hes9NRN4l2snPKMimRggVlAAgeW3PpBkeK7OM7FjBioIfEsdOGsyrPUk86ciZrD9ZB68RGcY3nZBV3d2GjMCCjyhqWvqn4Nd8BAaI5M7ncYlogQpngP0StNL/UAdGf0smPBUQgItplL+u8tEuU9KKmH/dhyZBwlDONGGmqzpJ6EfLUhnJJwcN9GCNagO6mQ19VyObN2ubOwIW+kfeiYUMrgt4gKoWZ6oocAYyXB77rZbe5FZvqdwFI9ifeWA7sptsx+NRdWWb5OnnmfKbF/PmR3Vjc1ASSlEcW++7iCAfWSyHYXk/y5+Ub84SnMMYIBEzCCAQ8CAQEwbTBZMQswCQYDVQQGEwJDTjEwMC4GA1UEChMnQ2hpbmEgRmluYW5jaWFsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRgwFgYDVQQDEw9DRkNBIFRFU1QgT0NBMTECEBAAAAAAAAAAAAAAIBEJKYAwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASBgKfPxOcaNLfcmNADPCWEt06fK2js6576QXNSvhYkRm88IfMfp1d1i0R4FisRCnUkdir3b+c+sh35BoYsHly+VzZiufVVheC9ZoqW7m3y/a0DyP0jn5ombEE5CMWm+Eq09CA06GGN7lJNtKos4bvJliccV+LgRX0DXQgWKGJWqBn2";
			httpPostXml(url, xmlInfo);
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
