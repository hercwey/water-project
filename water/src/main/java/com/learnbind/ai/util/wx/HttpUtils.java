package com.learnbind.ai.util.wx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * http请求工具类
 * 
 * @author srd
 * 
 */
public class HttpUtils {

	private static Log log = LogFactory.getLog(HttpUtils.class);

	private static PoolingHttpClientConnectionManager poolConnManager;

	public static final String CHARSET = "UTF-8";

	static {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext, hostnameVerifier);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http",
							PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslsf).build();
			poolConnManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			poolConnManager.setMaxTotal(50);// 最大链接数
			poolConnManager.setDefaultMaxPerRoute(20); // 最大路由数
			SocketConfig socketConfig = SocketConfig.custom()
					.setSoTimeout(3000).build();
			poolConnManager.setDefaultSocketConfig(socketConfig);
		} catch (Exception e) {
			log.error("InterfacePhpUtilManager init Exception" + e.toString());
		}
	}

	public static CloseableHttpClient getHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(5000).setConnectTimeout(5000)
				.setSocketTimeout(20000).build();// 链接请求超时时间；链接超时；返回超时
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(poolConnManager)
				.setDefaultRequestConfig(requestConfig).build();
		if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
			log.info("now client pool "
					+ poolConnManager.getTotalStats().toString());
		}
		return httpClient;
	}
	
	/**
	 * GET请求返回String
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, null, CHARSET);
	}
	
	/**
	 * GET请求返回json
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject doGetJson(String url) {
		return JSON.parseObject(doGet(url));
	}

	/**
	 * GET请求返回String
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, CHARSET);
	}
	/**
	 * GET请求返回Json
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject doGetJson(String url, Map<String, String> params) {
		return JSON.parseObject(doGet(url, params, CHARSET));
	}

	/**
	 * POST请求返回json
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject doPostJson(String url, String params) {
		return JSON.parseObject(doPostString(url, params, CHARSET));
	}

	/**
	 * POST请求上传文件
	 * 
	 * @param url
	 * @param path
	 * @return json对象
	 */
	public static JSONObject doPostUploadFileJson(String url, String paramsName,
			String path) {
		return JSON.parseObject(doPostUploadFile(url, paramsName, path));
	}

	/**
	 * POST请求上传文件
	 * @param url
	 * @param paramsName
	 * @param in 上传文件流
	 * @return json对象
	 */
	public static JSONObject doPostUploadFileJson(String url, String paramsName,
			InputStream in) {
		return JSON.parseObject(doPostUploadFile(url, paramsName, in));
	}

	/**
	 * POST请求返回String
	 * 
	 * @param url
	 * @return
	 */
	public static String doPost(String url, String params) {
		return doPostString(url, params, CHARSET);
	}
	
	/**
	 * POST请求下载文件
	 * @param url
	 * @param params
	 * @param fileSaveDir
	 * @return
	 */
	public static String doPostDownFile(String url, String params, String fileSaveDir){
		return doPostDownFile(url, params, CHARSET, fileSaveDir);
	}
	
	
	/**
	 * GET请求下载文件
	 * @param url
	 * @param fileSaveDir 文件保存目录
	 * @return 文件名
	 */
	public static String doGetDownFile(String url,String fileSaveDir) {

		if (StringUtils.isBlank(url)) {
			return null;
		}
		try {
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = getHttpClient().execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			String filename = getFileName(response);
			if (entity != null) {
				File file = new File(fileSaveDir+File.separator+filename);
				if(!file.getParentFile().exists()){
					if(!file.getParentFile().mkdirs()){
						log.error("目录创建异常："+fileSaveDir);
						return null;
					}
				}
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(EntityUtils.toByteArray(entity));
				fileOutputStream.close();
			}
			EntityUtils.consume(entity);
			response.close();
			return filename;
		} catch (Exception e) {
			log.error("err", e);
		}
		return null;
		
	}

	/**
	 * GET请求
	 * @param url
	 * @param params
	 * @param charset
	 * @return 字符串
	 */
	public static String doGet(String url, Map<String, String> params,
			String charset) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		if(log.isDebugEnabled()){
			log.debug(params);
		}
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(
						params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?"
						+ EntityUtils.toString(new UrlEncodedFormEntity(pairs,
								charset));
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = getHttpClient().execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Post请求下载文件
	 * @param url
	 * @param fileSaveDir 文件保存目录
	 * @return 文件名
	 */
	public static String doPostDownFile(String url,String params, String charset, String fileSaveDir) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		if(log.isDebugEnabled()){
			log.debug(params);
		}
		try {
			HttpPost httpPost = new HttpPost(url);
			if (StringUtils.isNotEmpty(params)) {
				StringEntity se = new StringEntity(params, charset);
				httpPost.setEntity(se);
			}
			CloseableHttpResponse response = getHttpClient().execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			String filename = getFileName(response);
			if (entity != null) {
				File file = new File(fileSaveDir+filename);
				if(!file.getParentFile().mkdirs()){
					log.error("目录创建异常："+fileSaveDir);
					return null;
				}
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(EntityUtils.toByteArray(entity));
				fileOutputStream.close();
			}
			EntityUtils.consume(entity);
			response.close();
			return filename;
		} catch (Exception e) {
			log.error("err", e);
		}
		return null;
		
	}

	/**
	 * Post访问url
	 * @param url
	 * @param params
	 * @param charset
	 * @return 字符串
	 */
	public static String doPostString(String url, String params, String charset) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		if(log.isDebugEnabled()){
			log.debug(params);
		}
		HttpPost httpPost = new HttpPost(url);
		if (StringUtils.isNotEmpty(params)) {
			StringEntity se = new StringEntity(params, charset);
			httpPost.setEntity(se);
		}
		return doPost(httpPost);
	}

	/**
	 * Post上传文件
	 * @param url
	 * @param paramsName
	 * @param filePath
	 * @return
	 */
	public static String doPostUploadFile(String url, String paramsName, String filePath) {
		if(log.isDebugEnabled()){
			log.debug(paramsName+":"+filePath);
		}
		File file = new File(filePath);
		HttpPost httpPost = new HttpPost(url);
		FileBody bin = new FileBody(file);
		HttpEntity builder = MultipartEntityBuilder.create()
				.addPart(paramsName, bin).build();
		httpPost.setEntity(builder);
		return doPost(httpPost);
	}
	
	/**
	 * Post上传文件
	 * @param url
	 * @param paramsName
	 * @param in 上传文件流
	 * @return
	 */
	public static String doPostUploadFile(String url, String paramsName, InputStream in) {
		HttpPost httpPost = new HttpPost(url);
		HttpEntity builder = MultipartEntityBuilder.create()
				.addBinaryBody(paramsName, in).build();
		httpPost.setEntity(builder);
		return doPost(httpPost);
	}

	/**
	 * Post上传文件
	 * @param url
	 * @param paramsName
	 * @param filePath 媒体文件地址
	 * @param type 永久素材媒体文件类型
	 * @return
	 */
	public static JSONObject doPostUploadFile(String url, String paramsName, String filePath, String type) {
		if(log.isDebugEnabled()){
			log.debug(paramsName+":"+filePath);
		}
		File file = new File(filePath);
		HttpPost httpPost = new HttpPost(url);
		FileBody bin = new FileBody(file);
		HttpEntity builder = MultipartEntityBuilder.create()
				.addPart(paramsName, bin).addTextBody("type", type).build();
		httpPost.setEntity(builder);
		return JSON.parseObject(doPost(httpPost));
	}

	/**
	 * Post上传永久视频素材
	 * @param url
	 * @param paramsName
	 * @param filePath
	 * @param type
	 * @param description 素材的描述信息
	 * @return
	 */
	public static JSONObject doPostUploadFile(String url, String paramsName, String filePath, String type, String description) {
		if(log.isDebugEnabled()){
			log.debug(paramsName+":"+filePath);
		}
		File file = new File(filePath);
		HttpPost httpPost = new HttpPost(url);
		FileBody bin = new FileBody(file);
		HttpEntity builder = MultipartEntityBuilder.create()
				.addPart(paramsName, bin).addTextBody("type", type).addTextBody("description", description).build();
		httpPost.setEntity(builder);
		return JSON.parseObject(doPost(httpPost));
	}

	public static String doPost(HttpPost httpPost) {
		String result = null;
		try {
			CloseableHttpResponse response = getHttpClient().execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Exception e) {
			httpPost.abort();
			log.error(" Exception" + e.toString() + " httpPost="
					+ httpPost.toString());
		}
		return result;
	}
	
	/**
	 * 获取文件名
	 * @param response
	 * @return
	 */
	private static String getFileName(CloseableHttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if(contentHeader!=null){
			HeaderElement[] elements = contentHeader.getElements();
			if(elements.length==1){
				NameValuePair valuePair = elements[0].getParameterByName("filename");
				if(valuePair!=null){
					filename = valuePair.getValue();
				}
			}
		}
		return filename;
	}

}