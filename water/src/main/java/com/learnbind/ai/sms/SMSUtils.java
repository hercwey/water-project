package com.learnbind.ai.sms;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class SMSUtils {
	
	/**
	 * @Title: createRandomStr
	 * @Description: 创建随机字符串
	 * @return 
	 */
	public static String genRandomStr() {
		return UUID.randomUUID().toString().replaceAll("-", "");
    }
	
	/**
	 *  创建时间戳（秒） UNIX 时间戳
	 * @return  当前时间的时间戳
	 */
	public static String getTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
		//return Long.toString(new Date().getTime());
    }
	
	/**
	 * @Title: getSendSingleSMSUrl
	 * @Description: 生成发送单条SM的URL地址
	 * @param appid	    自Tencent Cloud申请的appid
	 * @param randomStr	 随机字符串
	 * @return   发送单条短信时的URL地址
	 */
	public static String genSendSingleSMSUrl(String appid,String randomStr) {
		String url="https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid="+appid+"&random="+randomStr;
		return url;
	}
	
	/**
	 * @Title: genSendMultSMSUrl
	 * @Description: 生成群发短信的URL地址
	 * @param appid   自Tencent Cloud申请的appid
	 * @param randomStr  随机字符串
	 * @return 群发短信时的URL地址
	 */
	public static String genSendMultSMSUrl(String appid,String randomStr) {
		//public static final String SEND_MULT_SMS_URL="https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2?sdkappid=xxxxx&random=xxxx";
		//          https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2?sdkappid=xxxxx&random=xxxx
		String url="https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2?sdkappid="+appid+"&random="+randomStr;
		return url;
	}
	
	/**
	 * @Title: sign
	 * @Description: 生成请求的签名.
	 * 4.	sig字段根据公式 sha256（appkey=$appkey&random=$random&time=$time&mobile=$mobile）生成，其伪代码如下：
			o	
			5.	string strMobile = "13788888888"; //tel 的 mobile 字段的内容
			6.	string strAppKey = "5f03a35d00ee52a21327ab048186a2c4"; //sdkappid 对应的 appkey，需要业务方高度保密
			7.	string strRand = "7226249334"; //URL 中的 random 字段的值
			8.	string strTime = "1457336869"; //UNIX 时间戳
			9.	string sig = sha256(appkey=5f03a35d00ee52a21327ab048186a2c4&random=7226249334&time=1457336869&mobile=13788888888)
			        = ecab4881ee80ad3d76bb1da68387428ca752eb885e52621a3129dcf4d9bc4fd4;

	 
	 * @param strAppKey
	 * @param strRand
	 * @param strTime
	 * @param strMobile
	 * @return 
	 */
	public static String genSignSHA256(String strAppKey,String strRand,String strTime,String strMobile) {
		//String strTime=getTimestamp();  //生成UNIX时间戳
		String strx="appkey="+strAppKey+
					"&random="+strRand+
					"&time="+strTime+
					"&mobile="+strMobile;
        String signature = DigestUtils.sha256Hex(strx);
        return signature;
    }
}
