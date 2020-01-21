package com.learnbind.ai.service.wechat.cache;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.service.wechat.entity.OAuthAccessToken;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.service.wechat.cache
 *
 * @Title: CacheManager.java
 * @Description:
 * 					Wechat缓存					微信对象
 * 					OAuthAccessToken缓存			登录微信时
 * 					JsapiTicket缓存 				微信支付时
 *
 * @author lenovo
 * @date 2019年8月15日 上午8:23:55
 * @version V1.0 
 *
 */
public class CacheManager {
	
	private static final int PERIOID_VALUE=7200; //缓存中数据保留时长,单位为秒
	public static final String KEY_CREATE_TIME = "createTime"; //创建时间,单位:秒

	// key:accessToken value: AccessToken
	private static ConcurrentHashMap<String, JSONObject> tokenCache = new ConcurrentHashMap<String, JSONObject>();

	public static final String KEY_WECHAT = "wechat";// 微信公众号信息key
	public static final String KEY_OAUTH_ACCESS_TOKEN = "oauth_access_token";// oauth认证的access_token，用于网页授权
	public static final String KEY_ACCESS_TOKEN = "access_token";// 普通的access_token，用于调用微信JS接口
	public static final String KEY_JSAPI_TICKET = "jsapi_ticket";// jsapi_ticket，用于调用微信JS接口的临时票据（获取签名时用）	

	/**
	 * 在缓存中添加数据
	 * 
	 * @param key
	 * @param accessToken
	 */
	public static void set(String key, JSONObject jsonObj) {
		synchronized (tokenCache) {
			tokenCache.put(key, jsonObj);
		}

	}

	/**
	 * @Title: get
	 * @Description: 自缓存中获取
	 * @param key
	 * @return 
	 */
	public static JSONObject get(String key) {
		return tokenCache.get(key);
	}
	
	/**
	 * @Title: clearCache
	 * @Description: 清除已经过期的Cache
	 * 				  在定时任务中调用,定时任务每10分钟执行一次. 
	 */
	public static void clearCache() {
		synchronized (tokenCache) {
			// 扫描cache中的内容
			// 将建立时间至当前时间止已经超过2个小时的对象清除掉
			for (String key : tokenCache.keySet()) {
				JSONObject jsonObj = get(key);
				if(jsonObj!=null) {
					Long createTime = jsonObj.getLong(CacheManager.KEY_CREATE_TIME);  //获取创建时间,有一些缓存的对象并没有createTime字段.					
					if(createTime!=null) {
						Long currentTime = System.currentTimeMillis() / 1000;
						Long period=currentTime - createTime;
						if(period>=PERIOID_VALUE){
							remove(key);
						}
					}
				}
			}
		}  //end synchronized
	}
	
	/**
	 * @Title: remove
	 * @Description: 清除指定key的数据
	 * @param key  键值 
	 */
	public static void remove(String key) {
		tokenCache.remove(key);
	}

	/**
	 * @Title: clear
	 * @Description: 清除Cache中所有的内容 
	 */
	public static void clear() {
		tokenCache.clear();
	}

	/**
	 * @Title: getSize
	 * @Description: 返回缓存中的数据个数
	 * @return 
	 */
	public static Integer getSize() {
		return tokenCache.size();
	}

	public static void main(String[] args) {
		OAuthAccessToken token = new OAuthAccessToken();
		token.setExpires_in(123456);
		String tokenStr = UUID.randomUUID().toString();
		System.out.println(tokenStr);
		token.setAccess_token(tokenStr);
		CacheManager.set("accessToken", JSON.parseObject(JSON.toJSONString(token)));
		System.out.println(CacheManager.getSize());
		System.out.println(JSON.toJavaObject(CacheManager.get("accessToken"), OAuthAccessToken.class).getAccess_token());
		System.out.println(JSON.toJavaObject(CacheManager.get("accessToken"), OAuthAccessToken.class).getExpires_in());

		OAuthAccessToken token1 = new OAuthAccessToken();
		token1.setExpires_in(234567);
		String tokenStr1 = UUID.randomUUID().toString();
		System.out.println(tokenStr1);
		token1.setAccess_token(tokenStr1);
		CacheManager.set("accessToken", JSON.parseObject(JSON.toJSONString(token1)));
		System.out.println(CacheManager.getSize());
		System.out.println(JSON.toJavaObject(CacheManager.get("accessToken"), OAuthAccessToken.class).getAccess_token());
		System.out.println(JSON.toJavaObject(CacheManager.get("accessToken"), OAuthAccessToken.class).getExpires_in());

	}

}
