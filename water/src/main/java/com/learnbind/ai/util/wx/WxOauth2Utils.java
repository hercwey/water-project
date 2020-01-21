package com.learnbind.ai.util.wx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 网页授权获取用户基本信息
 * 用户在微信客户端中访问第三方网页，公众号可以通过微信网页授权机制，来获取用户基本信息
 * @author srd
 *
 */
public class WxOauth2Utils {
	
	private static final Logger log = LoggerFactory.getLogger(WxOauth2Utils.class);
	
	private static final String OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

	//获取认证access_token和openid
	private static final String OAUTH2_ACCESS_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	//刷新认证access_token
	private static final String OAUTH2_REFRESHACCESS_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	//获取用户详细信息(需scope为 snsapi_userinfo)
	private static final String OAUTH2_GETINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 获得认证页面url
	 * （服务号获得高级接口后，默认拥有scope参数中的snsapi_base和snsapi_userinfo）
	 * @param appID
	 * @param redirectUri
	 * @param scope
	 * @param state
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getAuthorizeUrl(String appID,String redirectUri,String scope,String state) throws UnsupportedEncodingException{
		redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		String url = OAUTH2_AUTHORIZE_URL.replace("APPID", appID).replace("REDIRECT_URI", redirectUri).replace("SCOPE",scope).replace("STATE", state);
		if(log.isDebugEnabled()){
			log.debug(url);
		}
		return url;
	}
	
	/**
	 * 获取认证access_token和openid
	 * 如果网页授权的作用域为snsapi_base
	 * 则本步骤中获取到网页授权access_token的同时，也获取到了openid
	 * snsapi_base式的网页授权流程即到此为止。
	 * @param appID
	 * @param secret
	 * @param code
	 * @return
	 */
	public static JSONObject getAccessToken(String appID,String secret,String code){
		String url=OAUTH2_ACCESS_URL.replace("APPID", appID).replace("SECRET",secret).replace("CODE",code);
		if(log.isDebugEnabled()){
			log.debug(url);
		}
		return HttpUtils.doGetJson(url);
	}
	
	/**
	 * 刷新认证access_token
	 * @param appID
	 * @param refreshToken
	 * @return
	 */
	public static JSONObject getRefreshAccessToken(String appID,String refreshToken){
		String url=OAUTH2_REFRESHACCESS_URL.replace("APPID", appID).replace("REFRESH_TOKEN",refreshToken);
		if(log.isDebugEnabled()){
			log.debug(url);
		}
		return HttpUtils.doGetJson(url);
	}

	/**
	 * 	注:自微信服务器获取微信用户的信息.
	 * 	获取用户详细信息(需scope为 snsapi_userinfo)  
	 * 
	 * @param accessToken
	 * @param openID
	 * @return
	 */
	public static JSONObject getMemberInfo(String accessToken,String openID){
		String url=OAUTH2_GETINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID",openID);
		if(log.isDebugEnabled()){
			log.debug(url);
		}
		return HttpUtils.doGetJson(url);
	}
	
}
