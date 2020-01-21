package com.learnbind.ai.service.wechat.service;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.learnbind.ai.util.wx.WxJSSDKUtils;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.service.wechat.service
 *
 * @Title: WxJSSDKService.java
 * @Description: jssdk
 *
 * @author lenovo
 * @date 2019年8月15日 上午8:40:18
 * @version V1.0 
 *
 */
@Service
public class WxJSSDKService {
	
	private Log log = LogFactory.getLog(WxJSSDKService.class);
	
	/**
	 * 获取jssdk的config信息
	 * wx.config({
		    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: '', // 必填，公众号的唯一标识
		    timestamp: , // 必填，生成签名的时间戳
		    nonceStr: '', // 必填，生成签名的随机串
		    signature: '',// 必填，签名，见附录1
		    jsApiList: [] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
	 * @param jsapiTicket
	 * @param wechat
	 * @param url
	 * @return Map<String, String>
	 * 		"jsNonceStr", nonce_str 随机字符串
	 * 		"jsTimestamp", timestamp 时间戳
	 * 		"jsSignature", signature 签名
	 * 		"jsAppId", appId 公众号的唯一标识
	 */
	public Map<String, String> getJsConfig(String jsapiTicket, String appId, String url){
		
		Map<String, String> map = WxJSSDKUtils.sign(jsapiTicket, url);
		map.put("jsAppId", appId);
		
		if(log.isDebugEnabled()){
			log.debug(map);
		}
		
		return map;
		
	}

}
