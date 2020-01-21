package com.learnbind.ai.service.wechat.pay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.service.wechat.cache.CacheManager;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechat.service.WechatService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.pay
 *
 * @Title: WxPayQrcodeService.java
 * @Description: 微信二维码支付
 *
 * @author Administrator
 * @date 2019年8月6日 上午1:08:57
 * @version V1.0 
 *
 */
@Service
public class WxPayQrcodeService {
	
	private Log log = LogFactory.getLog(WxPayQrcodeService.class);
	
	public static final String KEY_CODE_URL = "code_url";//trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。注意：code_url的值并非固定，使用时按照URL格式转成二维码即可
	public static final String KEY_CREATE_TIME = "create_time";//创建时间，统一下单的日期（秒）
	
	@Autowired
	private WxUnifiedOrderService wxUnifiedOrderService;
	@Autowired
	private WechatService wechatService;
	
	/**
	 * @Title: getWxPayCodeUrl
	 * @Description: 获取微信支付二维码链接
	 * @param wechat
	 * @param accountItem
	 * @return 
	 */
	public String getWxPayCodeUrl(Wechat wechat, CustomerAccountItem accountItem){
		if(accountItem==null){
			log.error("订单为空");
			return null;
		}
		
		//trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。注意：code_url的值并非固定，使用时按照URL格式转成二维码即可
		String codeUrl = this.getCodeUrl(accountItem.getId());
		if(codeUrl==null){
			
			//暂时屏幕以下语句
			//commented by hz   2019/08/13
			//codeUrl = wxUnifiedOrderService.doUnifiedOrder(wechat, accountItem, null, WxPayTradeType.NATIVE.getIndex());//微信公众号支付			
			
			if(codeUrl==null){
				log.error("统一下单获取codeUrl为空");
				return null;
			}
			//trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。注意：code_url的值并非固定，使用时按照URL格式转成二维码即可，该值有效期为2小时
			JSONObject obj = new JSONObject();
			obj.put(KEY_CODE_URL, codeUrl);
			obj.put(KEY_CREATE_TIME, System.currentTimeMillis()/1000);
			CacheManager.set(accountItem.getId().toString(), obj);//缓存codeUrl，有效期为2小时
		}
		return codeUrl;
	}
	
	/**
	 * @Title: getPrepayId
	 * @Description: 获取微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
	 * @param accountItemId
	 * @return 
	 */
	private String getCodeUrl(Long accountItemId) {
		
		JSONObject jsonObj = CacheManager.get(accountItemId.toString());
		if(jsonObj!=null) {
			Long createTime = jsonObj.getLong(KEY_CREATE_TIME);
			if(this.isValid(createTime)) {
				String prepayId = jsonObj.getString(KEY_CODE_URL);//微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
				return prepayId;
			}
		}
		return null;
	}
	
	/**
	 * 验证codeUrl有效期（有效期为2小时）
	 * 		真正有效期为7200秒，此处验证为7000秒
	 * @return
	 */
	private Boolean isValid(Long createTime){
		Long currentTime = System.currentTimeMillis()/1000;
		if(createTime-currentTime>200){
			return true;
		}
		return false;
	}

}
