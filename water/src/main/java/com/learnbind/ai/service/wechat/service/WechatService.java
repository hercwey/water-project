package com.learnbind.ai.service.wechat.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.wechat.cache.CacheManager;
import com.learnbind.ai.service.wechat.entity.AccessToken;
import com.learnbind.ai.service.wechat.entity.JsapiTicket;
import com.learnbind.ai.service.wechat.entity.OAuthAccessToken;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.util.wx.WxAccessTokenUtils;
import com.learnbind.ai.util.wx.WxJSSDKUtils;
import com.learnbind.ai.util.wx.WxOauth2Utils;
import com.learnbind.ai.util.wx.entity.Signature;

/**
 * 微信请求处理
 * @author srd
 */
@Service
public class WechatService {

	private static final Logger log = LoggerFactory.getLogger(WechatService.class);

	public static final Integer PAY_PARAMS_TYPE_WECHAT = 0;//微信支付
	
	/**
	 * @Fields interfaceConfigService：营业系统接口配置-服务    (此处只使用微信配置)
	 */
	@Autowired
	private InterfaceConfigService interfaceConfigService;
	/**
	 * @Fields wxJSSDKService：
	 */
	@Autowired
	private WxJSSDKService wxJSSDKService;

	/**
	 * 验证请求来自微信
	 * 
	 * @param id 公众号id
	 * @param signature  微信加密签名，signature结合了开发者填写的token和请求中的timestamp、nonce
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @param echostr 随机字符串
	 * @return
	 */
	public Boolean wechatValid(Long id, Signature signature) throws Exception {

		//TODO 根据项目中的微信用户实体自行修改
//		Wechat wechat = getWechatById(id);
//		if (log.isDebugEnabled()) {
//			log.debug(wechat);
//		}
//		if (wechat != null) {
//			if (SignUtil.checkSignature(wechat.getToken(), signature)) {
//				return true;
//			}
//		}
		return false;
	}

	/**
	 * 根据主键查询微信公众号信息
	 * @param id
	 * @return
	 */
//	public Wechat getWechatById(Long wechatId) {
//		if(wechatId==null||wechatId==0){
//			return null;
//		}
//		return wechatMapper.selectByPrimaryKey(wechatId);
//	}
	
	/**
	 * 根据appID查询微信公众号信息
	 * @param appId
	 * @return
	 */
//	public Wechat getWechatByAppId(String appId){
//		if(StringUtils.isBlank(appId)){
//			return null;
//		}
//		Example example = new Example(Wechat.class);
//		example.createCriteria().andEqualTo("appid", appId);
//		List<Wechat> wechatList = wechatMapper.selectByExample(example);
//		if(!wechatList.isEmpty() && wechatList.size()>0){
//			return wechatList.get(0);
//		}
//		return null;
//	}
	
	/*public void getUserInfo(String code){
		OAuthAccessToken token = this.getOAuthAccessToken(code);
		JSONObject jsonObj = WxOauth2Utils.getMemberInfo(token.getAccess_token(), token.getOpenid());
		log.info("userInfo:"+jsonObj);
	}*/
	
	/**
	 * 获取授权OAuthAccessToken
	 * 		获取access_token请求返回的参数实体类
	 * @param code
	 * @return
	 */
	public OAuthAccessToken getOAuthAccessToken(String code){
		
		try {
			JSONObject wechatJson = CacheManager.get(CacheManager.KEY_WECHAT);
			if(wechatJson==null){
				Wechat wechat = this.getWechat();
				if(wechat!=null){
					wechatJson = JSON.parseObject(JSON.toJSONString(wechat));
					CacheManager.set(CacheManager.KEY_WECHAT, wechatJson);
				}else{
					log.error("微信公众号信息为空");
					return null;
				}
			}
			Wechat wechat = JSON.toJavaObject(wechatJson, Wechat.class);
			if(wechat==null){
				log.error("微信公众号信息为空");
				return null;
			}
			if(log.isDebugEnabled()){
				log.debug(wechat.toString());
			}
			
			JSONObject jsonObj = null;
			
			JSONObject obj = CacheManager.get(CacheManager.KEY_OAUTH_ACCESS_TOKEN);
			if(obj!=null){
				OAuthAccessToken cacheToken = JSON.toJavaObject(obj, OAuthAccessToken.class);
				if(cacheToken==null){
					log.error("JSONObject转Class为异常或JSONObject为空");
				}
				if(cacheToken.isValid()){//本地缓存accesstoken
					return cacheToken;
				}else{
					jsonObj = WxOauth2Utils.getRefreshAccessToken(wechat.getAppid(), cacheToken.getRefresh_token());
					if(jsonObj==null){
						log.error("重新获取access_token异常");
					}
				}
			}else{
				log.info("缓存中oauth_access_token为空，第一次获取oauth_access_token信息");
				jsonObj = WxOauth2Utils.getAccessToken(wechat.getAppid(), wechat.getSecret(), code);
				if(jsonObj==null){
					log.error("第一次获取oauth_access_token异常");
				}
			}
			
			OAuthAccessToken token = JSON.toJavaObject(jsonObj, OAuthAccessToken.class);
			if(token!=null){
				token.setCreateTime(System.currentTimeMillis()/1000);
				CacheManager.set(CacheManager.KEY_OAUTH_ACCESS_TOKEN, JSON.parseObject(JSON.toJSONString(token)));//重新获取token后加入缓存
			}
			return token;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取access_token异常");
		}
		
		return null;
		
	}
	
	/**
	 * 获取普通的access_token
	 * @return
	 */
	public String getAccessToken(){
		JSONObject obj = CacheManager.get(CacheManager.KEY_ACCESS_TOKEN);
		if(obj!=null){
			AccessToken token = JSON.toJavaObject(obj, AccessToken.class);
			if(token==null){
				log.error("JSONObject转Class为异常或JSONObject为空");
			}
			if(token.isValid()){//本地缓存accesstoken
				return token.getAccess_token();
			}
		}
		
		JSONObject wechatJson = CacheManager.get(CacheManager.KEY_WECHAT);
		if(obj==null){
			Wechat wechat = this.getWechat();
			if(wechat!=null){
				wechatJson = JSON.parseObject(JSON.toJSONString(wechat));
				CacheManager.set(CacheManager.KEY_WECHAT, wechatJson);
			}else{
				log.error("微信公众号信息为空");
				return null;
			}
		}
		Wechat wechat = JSON.toJavaObject(wechatJson, Wechat.class);
		if(wechat==null){
			log.error("JSONObject转Class为异常或JSONObject为空");
		}
		if(log.isDebugEnabled()){
			log.debug(wechat.toString());
		}
		return getAccessToken(wechat);
	}
	
	/**
	 * 重新请求获取普通的access_token
	 * @param wechat
	 * @return
	 */
	public String getAccessToken(Wechat wechat){
		try{
			JSONObject jsonObj = WxAccessTokenUtils.getTokenJson(wechat.getAppid(), wechat.getSecret());
			if(jsonObj==null){
				log.error("获取access_token为空");
				return null;
			}
			if(WxAccessTokenUtils.isAccessToken(jsonObj)){
				
				AccessToken token = JSON.toJavaObject(jsonObj, AccessToken.class);
				if(token==null){
					log.error("JSONObject转Class为异常或JSONObject为空");
				}
				token.setCreateTime(System.currentTimeMillis()/1000);
				CacheManager.set(CacheManager.KEY_ACCESS_TOKEN, JSON.parseObject(JSON.toJSONString(token)));//重新获取token后加入缓存
				
				return token.getAccess_token();
			}
		} catch (Exception e){
			e.printStackTrace();
			log.error("获取access_token异常", e);
		}
		return null;
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
	 * @param wechat
	 * @param url
	 * @return Map<String, String>
	 * 		"jsNonceStr", nonce_str 随机字符串
	 * 		"jsTimestamp", timestamp 时间戳
	 * 		"jsSignature", signature 签名
	 * 		"jsAppId", appId 公众号的唯一标识
	 */
	public Map<String, String> getJsConfig(String appId, String url){
		
		//自Cache中获取ticket
		JsapiTicket ticket = JSON.toJavaObject(CacheManager.get(CacheManager.KEY_JSAPI_TICKET), JsapiTicket.class);
		
		String jsapiTicket = null;
		if(ticket!=null && ticket.isValid()){
			jsapiTicket = ticket.getTicket();
		}else{
			try {
				jsapiTicket = WxJSSDKUtils.getTicket(getAccessToken());
				if(jsapiTicket==null){
					log.error("重新获取jsapi_ticket返回为空");
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("重新获取jsapiTicket异常");
			}
		}
		
		return wxJSSDKService.getJsConfig(jsapiTicket, appId, url);// 页面调用jssdk
	}
	
	/**
	 * @Title: getWechat
	 * @Description: 获取公众号信息
	 * @return 
	 */
	public Wechat getWechat() {
		
		//(1)查询微信公众号配置列表
		SysInterfaceConfig config = new SysInterfaceConfig();
		config.setInterfaceType(InterfaceConstant.INTERFACE_TYPE_WECHAT);		
		List<SysInterfaceConfig> configList = interfaceConfigService.select(config);

		//设置
		Wechat wechat = new Wechat();
		for(SysInterfaceConfig configTemp : configList) {
			String key = configTemp.getKey();
			String value=configTemp.getValue();
			
			switch(key.toUpperCase()) {
			case InterfaceConstant.WECHAT_APPID:
				wechat.setAppid(value);
				break;
			case InterfaceConstant.WECHAT_APPSECRET:
				wechat.setSecret(value);
				break;
			case InterfaceConstant.WECHAT_MCH_ID:
				wechat.setMchid(value);
				break;	
			case InterfaceConstant.WECHAT_PAY_KEY:
				wechat.setPaykey(value);
				break;
			case InterfaceConstant.WECHAT_PAY_NOTIFY_URL:
				wechat.setPayNotifyUrl(value);
				break;
			}
		}	
		
		return wechat;
	}
	
}
