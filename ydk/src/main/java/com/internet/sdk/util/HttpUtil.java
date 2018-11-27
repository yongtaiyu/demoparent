package com.zenith.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.zenith.client.config.ClientConfig;
/**
 * Http请求包（依赖包：http-client-4.5.3.jar,httpclient-cache-4.3.5.jar,httpcore-4.4.6.jar,httpmime-4.3.5.jar)
 * @author 杨太宇
 * 
 */
public class HttpUtil {

	private static int connectionRequestTimeout = 60000;
	private static int connectTimeout = 60000;
	private static int socketTimeout = 60000;

	/**
	 * get请求
	 * 
	 * @return
	 */
	public static String doGet(String url) {
		CloseableHttpClient client = null;
		HttpGet request = null;
		CloseableHttpResponse response = null;
		try {
			client = HttpClients.createDefault();

			// 发送get请求
			request = new HttpGet(url);
			request.setHeader("appkey", ClientConfig.APP_KEY);

			response = client.execute(request);
			int code = response.getStatusLine().getStatusCode();
			// 请求发送成功，并得到响应
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 读取服务器返回过来的json字符串数据
				String strResult = EntityUtils.toString(response.getEntity());
				return strResult;
			} else {
				return String.format("{\"result\":\"fail\",\"message\":\"%s\"}", "状态码：" + code);
			}

		} catch (Exception e) {
			return String.format("{\"result\":\"fail\",\"message\":\"%s\"}", e.getMessage().replaceAll("\r|\n", ""));
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * post请求(用于key-value格式的参数)
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map<String, String> params) {
		CloseableHttpClient client = null;
		HttpPost request = null;
		CloseableHttpResponse response = null;
		BufferedReader in = null;
		try {
			// 定义HttpClient
			client = HttpClients.createDefault();
			// 实例化HTTP方法
			request = new HttpPost();
			request.setURI(new URI(url));
			request.setConfig(RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
					.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build());

			request.setHeader("appkey", ClientConfig.APP_KEY);
			request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
			// 有些服务器做了限制，访问不到，需要伪装浏览器请求
			request.setHeader(HttpHeaders.USER_AGENT,
					"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
			// 设置参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String value = String.valueOf(params.get(name));
				nvps.add(new BasicNameValuePair(name, value));
			}

			request.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
			response = client.execute(request);

			int code = response.getStatusLine().getStatusCode();
			if (code == 200) { // 请求成功
				in = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}

				in.close();

				return sb.toString();
			} else {
				return String.format("{\"result\":\"9\",\"message\":\"%s\"}", "请求出错，状态码：" + code);
			}

		} catch (Exception e) {
			return String.format("{\"result\":\"9\",\"message\":\"%s\"}", e.getMessage().replaceAll("\r|\n", ""));
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * post请求（用于请求json格式的参数）
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, String params) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost

		httpPost.setHeader(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
		// 有些服务器做了限制，访问不到，需要伪装浏览器请求
		httpPost.setHeader(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");

		httpPost.setHeader("appkey", ClientConfig.APP_KEY);

		StringEntity entity = new StringEntity(params, StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;

		try {

			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
				return jsonString;
			} else {
				return String.format("{\"result\":\"9\",\"message\":\"%s\"}", "请求出错，状态码：" + state);
			}

		} catch (Exception e) {
			return String.format("{\"result\":\"9\",\"message\":\"%s\"}", e.getMessage().replaceAll("\r|\n", ""));
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
