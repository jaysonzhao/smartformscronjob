package com.gzsolartech.schedule.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzsolartech.smartforms.exentity.HttpReturnStatus;
import com.gzsolartech.smartforms.utils.HttpClientUtils;

/**
 * HTTP轻量级工具类，和HttpClientUtils工具栏的区别在于doPost，doGet的方法参数带上了httpClient。
 * @author sujialin
 *
 */
public class HttpLightUtils {
	private static final Logger LOG = LoggerFactory
			.getLogger(HttpLightUtils.class);
	private BasicCookieStore cookieStore;
	
	public HttpLightUtils() {
		cookieStore=new BasicCookieStore();
	}
	
	public CloseableHttpClient getClient() {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				.build();
		return httpClient;
	}
	
	public HttpReturnStatus doGet(CloseableHttpClient httpClient, String url,
			Map<String, Object> params) {
		HttpReturnStatus result = new HttpReturnStatus();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			String val = (String) params.get(key);
			val = (val == null) ? "" : val;
			NameValuePair nkv = new BasicNameValuePair(key, val);
			nameValuePairs.add(nkv);
		}
		try {
			String args = EntityUtils.toString(new UrlEncodedFormEntity(
					nameValuePairs, Charset.forName("UTF-8")));
			if (StringUtils.isNotBlank(args)) {
				if (url.indexOf("?") == -1) {
					url += "?" + args;
				} else {
					url = url.endsWith("&") ? url : url + "&";
					url += args;
				}
			}
			HttpGet httpGet = new HttpGet(url); // 设定请求方式
			RequestConfig reqcfg=HttpClientUtils.getRequestConfig(10000);
			httpGet.setConfig(reqcfg);
			httpGet.setHeader("Content-Type", "application/json");
			httpGet.setHeader("Content-Language", "zh-CN");
			//添加Cookie
			HttpContext httpCtx=HttpClientContext.create();
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet, httpCtx);
			LOG.debug("return code: "
					+ httpResponse.getStatusLine().getStatusCode());
			String msg = EntityUtils
					.toString(httpResponse.getEntity(), "UTF-8");
			LOG.debug("return content: " + msg);
			result.setCode(httpResponse.getStatusLine().getStatusCode());
			result.setMsg(msg);
			httpResponse.close();
		} catch (Exception e) {
			LOG.error("get请求失败！", e);
		}
		return result;
	}
	
	/**
	 * 模拟post请求
	 * @param url  请求链接
	 * @param postContent post的内容参数
	 * @return
	 */
	public HttpReturnStatus doPost(CloseableHttpClient httpClient,
			String url, String postContent) {
		HttpReturnStatus result = new HttpReturnStatus();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig reqcfg=HttpClientUtils.getRequestConfig(10000);
		httpPost.setConfig(reqcfg);
		try {
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Language", "zh-CN");
			httpPost.setEntity(new StringEntity(postContent, "UTF-8"));
			LOG.debug("post url: " + url);
			LOG.debug("params: " + postContent);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
			result.setCode(response.getStatusLine().getStatusCode());
			result.setMsg(msg);
			LOG.debug("return code: " + result.getCode());
			LOG.debug("return content: " + msg);
			response.close();
		} catch (Exception ex) {
			LOG.error("POST请求发生错误！", ex);
			result.setCode(HttpReturnStatus.ERROR_CODE);
			result.setMsg(ex.toString());
		}
		return result;
	}
	
	/**
	 * 模拟post请求
	 * @param url  请求链接
	 * @param postContent post的内容参数
	 * @return
	 */
	public HttpReturnStatus doPost(CloseableHttpClient httpClient,
			String url, Map<String, Object> params) {
		HttpReturnStatus result = new HttpReturnStatus();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig reqcfg=HttpClientUtils.getRequestConfig(10000);
		httpPost.setConfig(reqcfg);
		try {
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Language", "zh-CN");
			//添加Post参数
			String debugMsg = "";
			List<NameValuePair> nvParams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				debugMsg += key + "=" + params.get(key).toString() + ", ";
				nvParams.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvParams, Charsets.UTF_8));
			LOG.debug("post url: " + url);
			LOG.debug("params: " + debugMsg);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
			result.setCode(response.getStatusLine().getStatusCode());
			result.setMsg(msg);
			LOG.debug("return code: " + result.getCode());
			LOG.debug("return content: " + msg);
			response.close();
		} catch (Exception ex) {
			LOG.error("POST请求发生错误！", ex);
			result.setCode(HttpReturnStatus.ERROR_CODE);
			result.setMsg(ex.toString());
		}
		return result;
	}
}
