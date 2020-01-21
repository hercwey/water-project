package com.learnbind.ai.sms;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: SMSConstants.java
 * @Description: 短信系统常量定义
 *
 * @author lenovo
 * @date 2019年8月20日 上午10:59:34
 * @version V1.0
 *  
 * 	重构建议:此处的常量如需要在运行时变更时,则将常量移至数据字典中即可
 */
public class SMSConstants {
	
		//-------------------------APP部分------------------------
		/**
		 * @Fields SDK_APPID：在Tencent Cloud申请的app标识
		 */
		public static final String SDK_APPID="1400244012";
		/**
		 * @Fields APP_KEY：在Tencent Cloud申请的app所对应的KEY
		 */
		public static final String APP_KEY="9718f932c0618478eeaa0a56c2b459b4";
		
		/**
		 * @Fields SMS_SENDER_SING：短信发送者签名(需在Tencent申请审核.用于标识短信的发送者)
		 */
		public static final String SMS_SENDER_SIGN="石家庄高新区供水排水公司";
		
		//-------------------请求地址部分-------------------------		
		/**
		 * @Fields SNED_SINGLE_SMS_URL：单发短信的请求地址(tencent提供,可参见开发文档)
		 */
		public static final String SNED_SINGLE_SMS_URL="https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=xxxxx&random=xxxx";
		
		/**
		 * @Fields SEND_MULT_SMS_URL：群发短的请求地址(Tencent提供,,可参见开发文档)
		 */
		public static final String SEND_MULT_SMS_URL="https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2?sdkappid=xxxxx&random=xxxx";
		
		//-------------------短信模板定义----------------------
		/**
		 * @Fields SMS_TEMPLATE_ID_FEE：短信模板ID,此模板需要在Tencent配置,而后申请审核,通过后方可使用
		 * 此模板的参数有:  有三个参数,分别为: 
		 * 				1-用户姓名 
		 * 				2-期间 
		 * 				3-水费金额.
		 */
		//public static final long SMS_TEMPLATE_ID_FEE=400261;
		public static final long SMS_TEMPLATE_ID_FEE=401009;
		
		//---------------------回调函数定义-------------------------
		/**
		 * @Fields SMS_CALLBACK_FUNCTION：短信下发状态通知
		 * 	注:其中的IP地址可以更改为域名或是服务器地址.
		 */
		public static final String SMS_CALLBACK_FUNCTION="http:/192.168.3.181/sms/callback";
		
		//-----------------电话号码-国家号码----------------------
		/**
		 * @Fields MOBILE_NATION_CODE_CN：电话号码-国家号码-中国
		 */
		public static final String MOBILE_NATION_CODE_CN="86";
}
