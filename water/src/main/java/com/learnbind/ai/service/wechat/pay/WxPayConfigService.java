package com.learnbind.ai.service.wechat.pay;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.wechat.cache.CacheManager;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechat.service.WechatService;
import com.learnbind.ai.util.wx.WxPayOrderUtils;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.pay
 *
 * @Title: WxPayConfigService.java
 * @Description: 微信支付参数
 *
 * @author Administrator
 * @date 2019年8月6日 上午1:13:26
 * @version V1.0 
 *
 */
@Service
public class WxPayConfigService {

	private Log log = LogFactory.getLog(WxPayConfigService.class);
	
	public static final String KEY_PREPAY_ID = "prepay_id";	 //微信支付服务器生成的预支付会话标识
	
	
	@Resource
	private WechatService wechatService;
	/**
	 * @Fields wxUnifiedOrderService：统一支付-服务
	 */
	@Resource
	private WxUnifiedOrderService wxUnifiedOrderService;
	
	/**
	 * @Title: getWxPayConfig
	 * @Description: 获取微信支付页面参数
	 * @param wechat
	 * @param accountItem
	 * @param wechatUser
	 * @return 
	 */
	public Map<String, String> getWxPayConfig(Wechat wechat, 
												WechatUser wechatUser,
												String busOrderNo,
												int payAmount,
												String billCreateIp
												){
		
		
		String prepayId = this.getPrepayId(busOrderNo);//获取微信生成的预支付会话标识，并验证有效期，用于后续接口调用中使用，该值有效期为2小时
		if(prepayId==null){
			prepayId = wxUnifiedOrderService.doUnifiedOrder(wechat,
															wechatUser, 
															WxPayTradeType.JSAPI.getIndex(),
															busOrderNo,
															payAmount,
															billCreateIp
															);//微信公众号支付
			if(prepayId==null){				
				return null;
			}
			
			//把微信生成的预支付会话标识保存到cache，
			//用于后续接口调用中使用，该值有效期为2小时
			addPerpayIdInCache(prepayId,busOrderNo);
			
		}
		return getBrandWCPayRequest(wechat, prepayId);
	}
	
	
	/**
	 * @Title: addPerpayIdInCache
	 * @Description: 将prepayId置于Cache中
	 * @param prepayId 预支付id
	 * @param busOrderNo 业务订单编号(在此处为营业收费系统所生成的支付订单编号)
	 */
	private void addPerpayIdInCache(String prepayId,String busOrderNo) {
		JSONObject obj = new JSONObject();
		obj.put(KEY_PREPAY_ID, prepayId);
		obj.put(CacheManager.KEY_CREATE_TIME, System.currentTimeMillis()/1000);
		
		CacheManager.set(busOrderNo, obj);//缓存prepayId，有效期为2小时
	}
	
	/**
	 * @Title: getBrandWCPayRequest
	 * @Description: 获取微信支付页面参数
	 * @param wechat
	 * @param prepayId
	 * @return 
	 */
	private Map<String, String> getBrandWCPayRequest(Wechat wechat, String prepayId){
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("appId", wechat.getAppid());
		map.put("timeStamp", String.valueOf(DateUtils.getDateTimeLong()));
		map.put("nonceStr", WxPayOrderUtils.createNonceStr());
		map.put("package", "prepay_id="+prepayId);
		map.put("signType", "MD5");
		String paySign = WxPayOrderUtils.getSign(map, wechat.getPaykey());
		map.put("paySign", paySign);
		map.put("prepayId", "prepay_id="+prepayId);
		
		if(log.isDebugEnabled()){
			log.debug(map);
		}
		return map;
	}
	
	/**
	 * @Title: getPrepayId
	 * @Description: 获取微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
	 * @param accountItemId
	 * @return 
	 */
	private String getPrepayId(String busOrderNo) {
		
		JSONObject jsonObj = CacheManager.get(busOrderNo);
		if(jsonObj!=null) {
			Long createTime = jsonObj.getLong(CacheManager.KEY_CREATE_TIME);
			if(this.isValid(createTime)) {
				String prepayId = jsonObj.getString(KEY_PREPAY_ID);//微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
				return prepayId;
			}
		}
		return null;
	}
	
	/**
	 * 验证prepayId有效期（有效期为2小时）
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
