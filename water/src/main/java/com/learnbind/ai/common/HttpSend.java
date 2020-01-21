package com.learnbind.ai.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;


public class HttpSend {
    public static String doGet(String url, String queryString) {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {
            if (StringUtils.isNotBlank(queryString))
                method.setQueryString(URIUtil.encodeQuery(queryString));
            client.executeMethod(method);
            method.getStatusCode();


            String str = method.getResponseBodyAsString();

            return str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }
}
