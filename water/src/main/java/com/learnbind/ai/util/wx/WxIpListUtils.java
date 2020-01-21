package com.learnbind.ai.util.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取微信服务器列表
 * @author srd
 *
 */
public class WxIpListUtils {
	
	private static final Logger log = LoggerFactory.getLogger(WxIpListUtils.class);
	
	private static final String IP_LIST_URL="https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
	
	private static String getUrl(String token){
		String url = IP_LIST_URL.replace("ACCESS_TOKEN", token);
		if (log.isDebugEnabled()){
			log.debug(url);
		}
		return url;
	}
	
	public static JSONObject getIpListJson(String token) throws Exception{
		return HttpUtils.doGetJson(getUrl(token));
	}

}
