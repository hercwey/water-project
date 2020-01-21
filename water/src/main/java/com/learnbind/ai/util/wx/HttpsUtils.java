package com.learnbind.ai.util.wx;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * 微信https请求工具类
 * @author srd
 *
 */
public class HttpsUtils {
	
	private static final Log log = LogFactory.getLog(HttpsUtils.class);
	
	public static String doPost(String url, String params,String mchid,String dir){
		HttpPost httpPost = getHttpPost(url, params);
		return doPost(httpPost,mchid,dir);
	}
	
	public static String doPost(HttpPost httpPost,String mchid,String dir) {
		String result = null;
		try {
			CloseableHttpClient httpClient = getHttpClient(mchid,dir);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			httpPost.abort();
			log.error(" Exception" + e.toString() + " httpPost=" + httpPost.toString());
		}
		if(log.isDebugEnabled()){
			log.debug("HTTPS返回值：" + result);
		}
		return result;
	}
	
	private static HttpPost getHttpPost(String url, String params){
		if(log.isDebugEnabled()){
			log.debug("HTTPS请求参数：" + params);
		}
		HttpPost httpPost = new HttpPost(url);
		if (StringUtils.isNotEmpty(params)) {
			StringEntity se = new StringEntity(params, "UTF-8");
			httpPost.setEntity(se);
		}
		return httpPost;
	}
    
    
	private static CloseableHttpClient getHttpClient(String mchid,String dir) throws Exception {
    	
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        
        InputStream instream = new FileInputStream(new File(dir));
        
        try {
            keyStore.load(instream, mchid.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mchid.toCharArray())
                .build();
        // Allow TLSv1 protocol only
		HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                hostnameVerifier);
        
        return  HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

}
