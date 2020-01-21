package com.learnbind.ai.sms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.util.HttpClientUtil;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: SMSService.java
 * @Description: SMS业务接口层
 *
 * @author lenovo
 * @date 2019年8月20日 下午3:41:07
 * @version V1.0 
 *
 */
@Service
public class SMSService {
	
	/**
	 * @Title: sendSingleSMS
	 * @Description: 发送单条短信
	 * @param mobileNo  接收短信的电话号码
	 * @param tpl_id    模板id
	 * @param tpl_parms 指定模板所对应的参数值.
	 * @return  JSON格式
	 * 
	  	{
    		"result": 0,
    		"errmsg": "OK",
    		"ext": "",
    		"fee": 1,
    		"sid": "xxxxxxx"
		}

	 * 	
	 */
	public String sendSingleSMS(String mobileNo,long tpl_id,List<String> tpl_parms) {
		//(1)生成请求单条发送SM时请求的URL
		String randomStr=SMSUtils.genRandomStr();
		String url=  SMSUtils.genSendSingleSMSUrl(SMSConstants.SDK_APPID,randomStr);
		//(2)生成请求参数
		SendSingleSMRequestParams requestParams=new SendSingleSMRequestParams();		
		
		requestParams.setTpl_id(tpl_id);  //(2.1)设置发送的模板值
		requestParams.setParams(tpl_parms);  //(2.2)短信内容中的参数项(短信模板参数值)				
		//(2.3)接收短信的电话号码
		TelephoneNo telNo=new TelephoneNo();
		telNo.setMobile(mobileNo);
		telNo.setNationcode(SMSConstants.MOBILE_NATION_CODE_CN);   //mobile nation code
		requestParams.setTel(telNo);		
		//(2.4)时间戳
	    String strTime=SMSUtils.getTimestamp();
		requestParams.setTime(Long.parseLong(strTime));		
		//(2.5)发送者签名
		requestParams.setSign(SMSConstants.SMS_SENDER_SIGN);		
		//(2.6)内容签名(注:此处使用的随机字符串同生成url时相同)
		String sig=SMSUtils.genSignSHA256(SMSConstants.APP_KEY,randomStr,strTime, mobileNo);		
		requestParams.setSig(sig);
		
		String jsonParms=JSON.toJSONString(requestParams);
		String response=HttpClientUtil.doPostJson(url,jsonParms);
		return response;
	}
	
	/**
	 * @Title: sendMultSMS
	 * @Description: 群发短信
	 * @param mobileNo
	 * @param tpl_id
	 * @param tpl_parms
	 * @return 
	 */
	public String sendMultSMS(List<String> mobileNoList,long tpl_id,List<String> tpl_parms) {
		//(1)生成请求单条发送SM时请求的URL
		String randomStr=SMSUtils.genRandomStr();
		String url=  SMSUtils.genSendMultSMSUrl(SMSConstants.SDK_APPID,randomStr);
		
		//(2)生成请求参数
		SendMultSMRequestParams requestParams=new SendMultSMRequestParams();		
		
		requestParams.setTpl_id(tpl_id);  //(2.1)设置发送的模板值
		requestParams.setParams(tpl_parms);  //(2.2)短信内容中的参数项(短信模板参数值)				
		//(2.3)接收短信的电话号码(多个)
		List<TelephoneNo> teleNoList=new ArrayList<>();
		for(String mobileNo:mobileNoList) {
			TelephoneNo telNo=new TelephoneNo();
			telNo.setMobile(mobileNo);
			telNo.setNationcode(SMSConstants.MOBILE_NATION_CODE_CN);   //mobile nation code
			//requestParams.setTel(telNo);
			teleNoList.add(telNo);
		}
		requestParams.setTel(teleNoList);
		
		//(2.4)时间戳
	    String strTime=SMSUtils.getTimestamp();
		requestParams.setTime(Long.parseLong(strTime));		
		//(2.5)发送者签名
		requestParams.setSign(SMSConstants.SMS_SENDER_SIGN);		
		//(2.6)内容签名(注:此处使用的随机字符串同生成url时相同)
		String mobileNoStr=listToString(mobileNoList,',');
		String sig=SMSUtils.genSignSHA256(SMSConstants.APP_KEY,randomStr,strTime, mobileNoStr);		
		requestParams.setSig(sig);
		
		String jsonParms=JSON.toJSONString(requestParams);
		String response=HttpClientUtil.doPostJson(url,jsonParms);
		return response;
	}
	
	/**
	 * @Title: listToString
	 * @Description: list转为字符串,其中的各个元素以指定的分隔符进行分隔
	 * @param list   列表
	 * @param separator  分隔符  
	 * @return   1,2,3,4
	 */
	public String listToString(List<?> list, char separator) {
        return org.apache.commons.lang.StringUtils.join(list.toArray(), separator);
    }	
	
	

}
