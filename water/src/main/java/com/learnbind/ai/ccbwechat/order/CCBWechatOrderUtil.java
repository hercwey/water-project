package com.learnbind.ai.ccbwechat.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.ccbwechat.entity.CCBResponseOrderParam;
import com.learnbind.ai.ccbwechat.entity.CCBRquestOrderParam;
import com.learnbind.ai.ccbwechat.entity.CCBWechatContants;
import com.learnbind.ai.ccbwechat.entity.PayParams;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.util.wx.HttpUtils;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.ccbwechat.order
 *
 * @Title: CCBWechatUtil.java
 * @Description: 处理CCB下单请求
 *
 * @author lenovo
 * @date 2019年12月27日 下午3:16:15
 * @version V1.0 
 *
 */
public class CCBWechatOrderUtil {	
	private static final Logger log = LoggerFactory.getLogger(CCBWechatOrderUtil.class); 
	
	//向CCB下单
	
	/**
	 * @Title: postOrder
	 * @Description: 向CCB下单并获取支付参数.
	 * @param busOrderNo
	 * @param orderAmount
	 * @param appId
	 * @param openId
	 * @return 
	 */
	public static PayParams postOrder2CCB(String busOrderNo,BigDecimal orderAmount,String appId,String openId) {
		//生成待mac字符串
		String preMACStr=genPreMACStr( busOrderNo, orderAmount, appId, openId);	
		//String preStr=genPreMACStr1( busOrderNo, orderAmount, appId, openId);
		
		//(1)采用md5计算MAC参数;
		String mac=calcMAC(preMACStr);		
		//(2)发送请求;
		String response=sendOrder2CCB(preMACStr,mac);  //JSON格式
		//对返回的参数进行处理
		PayParams payParams=null;
		CCBResponseOrderParam respParam=JSON.parseObject(response, CCBResponseOrderParam.class);
		log.debug("第一次请求返回的结果是:"+response);
		if(respParam.getSUCCESS().equalsIgnoreCase("true")) {			
			String  payParamsStr=getPayParamsFromCCB(respParam.getPAYURL());  //继续向CCB服务器发送请求,获取支付参数
			log.debug("第二次请求返回结果:"+payParamsStr);
			payParams=JSON.parseObject(payParamsStr, PayParams.class);
		}
		
		return payParams;
		
	}
	
	//生成预校验的字符串
	private static String genPreMACStr(String busOrderNo,BigDecimal orderAmount,String appId,String openId) {
		//final String PRODUCT_NAME="水费";
		final String PRODUCT_NAME="shuifei";
		
		//(0)确定需要待生成mac的字符串
		CCBRquestOrderParam requestParam=new CCBRquestOrderParam();
		requestParam.setORDERID(busOrderNo);  //订单号
		orderAmount=orderAmount.setScale(2);  //确保小数点两位
		requestParam.setPAYMENT(orderAmount.toPlainString());  //订单金额
		requestParam.setSUB_APPID(appId);  //appId微信公众号ID
		requestParam.setSUB_OPENID(openId);  //客户id
		
		//String escStr=escapeStr(PRODUCT_NAME);  //转码后的商品名称
		//requestParam.setPROINFO(escStr);
		
		requestParam.setPROINFO(PRODUCT_NAME);

		//转换后需要保持原来的顺序
		List<Map<String,Object>> list=EntityUtils.entityToListMap(requestParam);		
		String tempStr="";
		for(Map<String,Object> mapx:list) {
			String key=(String)mapx.keySet().iterator().next();
			String value=(String)mapx.get(key);
			tempStr=tempStr+key+"="+value+"&";
		}		
		tempStr=tempStr.substring(0, tempStr.length()-1);
		
		return tempStr;
	}
	
	//将字符串采用Escape进行转码处理
	//不再使用汉字进行Escape转码
	private static String escapeStr(String str) {
		ScriptEngineManager sem = new ScriptEngineManager();
        
        ScriptEngine engine = sem.getEngineByExtension("js");  
        try{  
            //直接解析
            Object res = engine.eval(" escape('"+ str  +"')");  
            //System.out.println(res);
            return (String)res;
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
		return "";
	}
	
	//计算mac值
	private static String calcMAC(String str) {
		String mac=DigestUtils.md5Hex(str);
		return mac;
	}
	
	//各CCB发送请求(下单-支付订单) 
	//mac校验值
	private static String sendOrder2CCB(String preMACStr,String mac) {
		String  url=CCBWechatContants.CCB_WECHAT_URL;
		url=url+"&"+preMACStr+"&MAC="+mac;		
		String resp=HttpUtils.doPost(url, null);
		log.debug("建行聚合支付第一次请求结果如下:"+resp);
		log.debug(resp);
		return resp;
	}
	
	//向CCB获取支付参数
	private static String getPayParamsFromCCB(String url) {
		String resp=HttpUtils.doPost(url, null);
		return resp;
	}
	
	public static final void main(String[] args) {
		//test1
//		String strx=CCBWechatOrderUtil.escapeStr("张三");
//		System.out.println(strx);
		
		//test2
		String preMacStr=CCBWechatOrderUtil.genPreMACStr("orderno001",new BigDecimal("2.54"),"appid001","openid001");
		System.out.println(preMacStr);
		
	}

}
