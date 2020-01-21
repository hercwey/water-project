package com.learnbind.ai.ccb.httpclient;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpHelper {  
    CloseableHttpClient m_HttpClient;  
  
    public HttpHelper() {  
        m_HttpClient = HttpClients.createDefault();  
    }  
  
      
    /** 
     * send bytes and recv bytes
     * @param url
     * @param bytes
     * @param contentType
     * 
     * @return
     * @throws IOException
     */
    public byte[] post(String url, byte[] bytes, String contentType) throws IOException {  
        HttpPost httpPost = new HttpPost(url);  
        httpPost.setEntity(new ByteArrayEntity(bytes));  
        if (contentType != null)  
            httpPost.setHeader("Content-type", contentType);  
        CloseableHttpResponse httpResponse = m_HttpClient.execute(httpPost);  
        try {  
            HttpEntity entityResponse = httpResponse.getEntity();  
            int contentLength = (int) entityResponse.getContentLength();  
            if (contentLength <= 0)  
                throw new IOException("No response");  
            byte[] respBuffer = new byte[contentLength];  
            if (entityResponse.getContent().read(respBuffer) != respBuffer.length)  
                throw new IOException("Read response buffer error");  
            return respBuffer;  
        } finally {  
            httpResponse.close();  
        }  
    }  
  
    public byte[] post(String url, byte[] bytes) throws IOException {  
        return post(url, bytes, null);  
    }  
  
    public String postXml(String url, String str) throws IOException {  
        byte[] reqBuffer = str.getBytes(Charset.forName("UTF-8"));  
        byte[] respBuffer = post(url, reqBuffer, "application/xml; charset=UTF-8");  
        String resp = new String(respBuffer, Charset.forName("UTF-8"));  
        return resp;  
    }  
}  