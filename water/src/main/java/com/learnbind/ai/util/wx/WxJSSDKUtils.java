package com.learnbind.ai.util.wx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信支付
 * jssdk
 * @author srd
 *
 */
public class WxJSSDKUtils {
	
	private static final Logger log = LoggerFactory.getLogger(WxJSSDKUtils.class);
	
	private static String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	
	/**
	 * 获取jsapi_ticket
	 * @param accessToken
	 * @return
	 */
	public static String getTicket(String accessToken){
		JSONObject json = getTicketJson(accessToken);
		if(WxErrcodeUtils.isNotErrcode(json)){
			return json.getString("ticket");
		}
		log.error(json.getIntValue("errcode")+":"+json.getString("errmsg"));
		return null;
	}
	
	/**
	 * 获取jsapi_ticket JSON对象
	 * @param accessToken
	 * @return 
	 */
	public static JSONObject getTicketJson(String accessToken){
		String url = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		return HttpUtils.doGetJson(url);
	}
	
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
	 * @param jsapi_ticket
	 * @param url
	 * @return Map<String, String>
	 * 		"jsNonceStr", nonce_str 随机字符串
	 * 		"jsTimestamp", timestamp 时间戳
	 * 		"jsSignature", signature 签名
	 * 		appId 公众号的唯一标识，返回的Map中没有此值，需要手动添加
	 */
	public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = createNonceStr();
        String timestamp = createTimestamp();
        String str;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        if(log.isDebugEnabled()){
        	log.debug(str);
        }
        
        signature = DigestUtils.sha1Hex(str);

        ret.put("jsNonceStr", nonce_str);
        ret.put("jsTimestamp", timestamp);
        ret.put("jsSignature", signature);

        return ret;
    }
	
	/**
	 * 创建随机字符串
	 * @return
	 */
	private static String createNonceStr() {
		return UUID.randomUUID().toString().replaceAll("-", "");
    }
	
	/**
	 * 创建时间戳（秒）
	 * @return
	 */
	private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
	
	public static void main(String[] args) {
		System.out.println("随机字符串 UUID:"+createNonceStr());
	}

}
