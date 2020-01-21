package com.learnbind.ai.util.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取微信AccessToken
 * @author srd
 *
 */
public class WxAccessTokenUtils {

	private static final Logger log = LoggerFactory.getLogger(WxAccessTokenUtils.class);
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	public static final String ACCESS_TOKEN = "access_token";

	public static final String EXPIRES_IN = "expires_in";
	
	/**
	 * 判断返回值是否异常
	 * @param appid
	 * @param appsecret
	 * @return
	 * @throws Exception
	 */
	public static Boolean isAccessToken(JSONObject json) throws Exception{
		if(json!=null){
			if(WxErrcodeUtils.isNotErrcode(json)){
				return true;
			}
			log.info(WxErrcodeUtils.getErrcodeMessage(json.getString("errcode")));
		}else{
			log.error("json为null");
		}
		return false;
	}
	
	/**
	 * 获取普通access_token
	 * @param appid
	 * @param appsecret
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getTokenJson(String appid, String appsecret) throws Exception{
		String url = getUrl(appid, appsecret);
		if (log.isDebugEnabled()) {
			log.debug(url);
		}
		return HttpUtils.doGetJson(url);
	}

	/**
	 * 获取access_token请求的rul
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	private static String getUrl(String appid, String appsecret) {
		return ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET",
				appsecret);
	}
	
}
