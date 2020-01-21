package com.learnbind.ai.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.learnbind.ai.ccb.des.DesCBC;

/**
 * HTTP请求工具类
 * 
 * @author srd
 *
 */
public class HttpClientUtil {

	private static Log log = LogFactory.getLog(HttpClientUtil.class);
	
	/**
	 * HTTP GET请求
	 * 
	 * @param url
	 * @param param
	 * 		Map<String, String> param
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	/**
	 * HTTP GET请求
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * HTTP POST请求
	 * @param url
	 * @param param
	 * 		Map<String, String> param
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param) {
		
		log.info("HTTP请求："+url);
		//log.info("HTTP请求参数："+param.toString());
		System.out.println(new Date() + " HTTP请求："+url);
		//System.out.println(new Date() + " HTTP请求参数："+param.toString());
		
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("ContentType", ContentType.APPLICATION_FORM_URLENCODED.toString());
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					System.out.println(param.get(key));
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"UTF-8");  //一定要加上编码,否则有中文时会出现乱码
				entity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.toString());
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			System.out.println("HTTP请求返回状态码："+response.getStatusLine().getStatusCode());
			log.info("HTTP请求返回状态码："+response.getStatusLine().getStatusCode());			
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			log.info("HTTP请求返回结果："+resultString);
			System.out.println(new Date() + " 请求结果：" + resultString);
			return resultString;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HTTP请求异常", e);
			System.out.println(new Date() + " HTTP请求异常");
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	/**
	 * added by HZ 2019/07/09
	 * 发送HTTP请求返回byte[]
	 * @param url		请求地址
	 * @param param		请求参数
	 * @return	byte[]类型数据
	 */
	public static byte[] doPostRespBytes(String url, Map<String, String> param) {
		
		log.info(" HTTP请求："+url);
		//log.info(" HTTP请求参数："+param.toString());
		
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			//(1)创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("ContentType", ContentType.APPLICATION_FORM_URLENCODED.toString());
			//(2)创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					//System.out.println(param.get(key));
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"UTF-8");  //一定要加上编码,否则有中文时会出现乱码
				entity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.toString());
				httpPost.setEntity(entity);
			}
			//(3)执行http请求
			response = httpClient.execute(httpPost);
			
			//返回结果处理.
			log.info("HTTP请求返回状态码："+response.getStatusLine().getStatusCode());
			if (response != null && response.getStatusLine().getStatusCode()==200) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null && resEntity.getContent()!=null) {
					log.info("------Begin Receive Data------");
					byte[] allData = EntityUtils.toByteArray(resEntity);  //转换成字节数组
					String respDataStr1 = new String(allData, "UTF-8");
					//log.info("----------new String HTTP请求响应的原始数据:"+respDataStr1);
					String respDataStr = DesCBC.parseByte2HexStr(allData);
					//log.info("----------DesCBC.parseByte2HexStr HTTP请求响应的原始数据:"+respDataStr);
					return allData;
				}
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HTTP请求异常", e);
			System.out.println(new Date() + " HTTP请求异常");
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	

	/**
	 * HTTP POST请求
	 * @param url
	 * @param param
	 * 		String param
	 * @return
	 */
	public static String doPost(String url, String param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("ContentType",  ContentType.APPLICATION_FORM_URLENCODED.toString());
			// 创建参数列表
			if (StringUtils.isNotEmpty(param)) {
				StringEntity se = new StringEntity(param, ContentType.APPLICATION_FORM_URLENCODED);
				httpPost.setEntity(se);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
			//System.out.println("请求结果：" + resultString);
			log.info("请求结果：" + resultString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
	
	/**
	 * @Description: 以POST方式发送请求,参数格式为JSON
	 * @param
	 *     @param url	
	 *     @param param
	 *     @return   
	 * @return 
	 *     String  
	 * @throws 
	 * @author Administrator
	 * @date 2018年1月29日-下午9:43:25
	 */
	public static String doPostJson(String url, String param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("ContentType", ContentType.APPLICATION_JSON.toString());
			// 创建参数列表
			if (StringUtils.isNotEmpty(param)) {
				StringEntity se = new StringEntity(param, ContentType.APPLICATION_JSON);
				httpPost.setEntity(se);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			//System.out.println("请求结果：" + resultString);
			log.info("请求结果：" + resultString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		return resultString;
	}
	
	
}
