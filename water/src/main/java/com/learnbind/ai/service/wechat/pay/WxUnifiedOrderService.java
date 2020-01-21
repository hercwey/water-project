package com.learnbind.ai.service.wechat.pay;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.util.wx.HttpUtils;
import com.learnbind.ai.util.wx.WxPayOrderUtils;
import com.learnbind.ai.util.wx.pay.UnifiedOrder;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.pay
 *
 * @Title: WxUnifiedOrderService.java
 * @Description: 微信统一下单
 *
 * @author Administrator
 * @date 2019年8月5日 下午11:09:29
 * @version V1.0 
 *
 */
@Service
public class WxUnifiedOrderService {
	
	private Log log = LogFactory.getLog(WxUnifiedOrderService.class);

	//TODO 修改 此URL  换成外网服务器的IP地址.
	//TODO 是否加入到数据字典的配置中,这样可以更好变动. 已经处理.
	/**
	 * @Fields WX_PAY_NOTIFY_URL：微信支付后回调链接
	 */
	//private final String WX_PAY_NOTIFY_URL = "http://127.0.0.1/wechat/pay/notify";//微信支付后回调链接
	
	/**
	 * @Title: doUnifiedOrder
	 * @Description: 统一下单
	 * @param wechat
	 * @param accountItem
	 * @param wechatUser
	 * @param tradeTypeIndex	交易类型 WxPayTradeType
	 * @return 统一下单
	 */
	public String doUnifiedOrder(Wechat wechat, 
								WechatUser wechatUser,
								int tradeTypeIndex,
								String busOrderNo,
								int orderAmount,
								String billCreateIp	){
		UnifiedOrder unifiedOrder = createUnifiedOrder(wechat, 
													   wechatUser, 
													   WxPayTradeType.getValue(tradeTypeIndex),
													   busOrderNo,
													   orderAmount,
													   billCreateIp
													   );
		Map<String, String> respMap = getRespMap(unifiedOrder, wechat.getPaykey());
		return valiWxReturnStr(respMap, wechat.getPaykey());
	}
	
	/**
	 * @Title: createUnifiedOrder
	 * @Description: 创建微信统一下单的订单
	 * @param wechat
	 * @param accountItem
	 * @param wechatUser
	 * @param tradeType
	 * @return 
	 */
	private UnifiedOrder createUnifiedOrder(Wechat wechat,										     
										    WechatUser wechatUser, 
										    WxPayTradeType tradeType,
										    String busOrderNo,
											int orderAmount,
											String billCreateIp   ){
		if(tradeType==null){
			log.error("交易类型异常,WxPayTradeType==null");
			return null;
		}
		UnifiedOrder unifiedOrder = new UnifiedOrder();
		unifiedOrder.setAppid(wechat.getAppid());//微信支付分配的公众账号ID（企业号corpid即为此appId）
		//unifiedOrder.setBody(accountItem.getCreditSubject());//商品简单描述
		unifiedOrder.setBody("查缴水费");//商品简单描述
		unifiedOrder.setMch_id(wechat.getMchid());//微信支付分配的商户号
		unifiedOrder.setNotify_url(wechat.getPayNotifyUrl());//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
		
		unifiedOrder.setOut_trade_no(busOrderNo);//系统中账单的唯一ID，商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。		
		unifiedOrder.setSpbill_create_ip(billCreateIp);//下单终端的IP，支持IPV4和IPV6两种格式的IP地址。用户的客户端IP		
		unifiedOrder.setTotal_fee(orderAmount);//账单需要支付的金额，订单总金额，单位为分；交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。外币交易的支付金额精确到币种的最小单位，参数值不能带小数点。
		
		//JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付，不同trade_type决定了调起支付的方式，请根据支付产品正确上传
		//MICROPAY--付款码支付，付款码支付有单独的支付接口，所以接口不需要上传，该字段在对账单中会出现
		unifiedOrder.setTrade_type(tradeType.getValue());
		
		if(wechatUser!=null){
			unifiedOrder.setOpenid(wechatUser.getOpenid());//trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
			//unifiedOrder.setAttach(String.valueOf(wechatUser.getId()));//附加数据，可以不设置，在查询API和支付通知中原样返回，可作为自定义参数使用。
		}
		
		return unifiedOrder;
	}
	
	/**
	 * @Title: getRespMap
	 * @Description: 执行微信统一下单功能，并解析返回数据为Map
	 * @param unifiedOrder
	 * @param key
	 * @return 
	 */
	private Map<String, String> getRespMap(UnifiedOrder unifiedOrder, String key){
		String params = createReqXmlStr(unifiedOrder, key);//获取微信统一下单参数XML字符串
		//向支付服务器发送请求---统一下单
		String respXmlStr = HttpUtils.doPost(WxPayOrderUtils.PAY_UNIFIEDORDER_URL, params);		
		try {
			return WxPayOrderUtils.parseXML(respXmlStr);
		} catch (UnsupportedEncodingException | DocumentException e) {
			log.error("Xml转换Map异常", e);
		}
		
		return null;
	}
	
	/**
	 * @Title: getReqXmlStr
	 * @Description: 生成微信统一下单请求参数
	 * @param unifiedOrder 统一下单对象
	 * @param key 支付key(申请支付接口时由Tencent提供,需要加入业务系统配置中)
	 * @return  统一下单参数(XML格式)
	 */
	private String createReqXmlStr(UnifiedOrder unifiedOrder, String key){
		unifiedOrder.setNonce_str(WxPayOrderUtils.createNonceStr());
		Map<String, String> map = null;
		try {
			map = WxPayOrderUtils.getObjectMap(unifiedOrder, UnifiedOrder.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("请求对象转换Map异常", e);
		}
		
		String sign = WxPayOrderUtils.getSign(map, key);
		map.put("sign", sign);
		String params = WxPayOrderUtils.getXmlStr(map);
		
		return params;
	}
	
	/**
	 * 验证微信返回数据
	 * @param map
	 * @param key
	 * @return prepay_id或code_url
	 */
	private String valiWxReturnStr(Map<String, String> map, String key){
		String result = null;
		String returnCode = map.get("return_code");
		if("SUCCESS".equals(returnCode)){
			if(WxPayOrderUtils.isWxNotify(map, key)){
				String resultCode = map.get("result_code");
				if("SUCCESS".equals(resultCode)){
					result = getResultValue(map);
				}else{
					log.error(map.get("err_code")+"="+map.get("err_code_des"));
				}
			}else{
				log.error("签名异常:"+map);
			}
		}else{
			log.error(map.get("return_msg"));
		}
		
		return result;
	}
	
	/**
	 * @Title: getResultValue
	 * @Description: 在统一下单返回的结果中获取预支付会话标识，用于支付（有效期为2小时）
	 * @param map
	 * @return 
	 */
	private String getResultValue(Map<String, String> map){
		String tradeType = map.get("trade_type");
		if(tradeType.equalsIgnoreCase(WxPayTradeType.NATIVE.getValue())){
			return map.get("code_url");//trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。注意：code_url的值并非固定，使用时按照URL格式转成二维码即可
		}
		return map.get("prepay_id");//微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
	}
	
}
