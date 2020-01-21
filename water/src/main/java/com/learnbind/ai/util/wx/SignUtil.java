package com.learnbind.ai.util.wx;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.learnbind.ai.util.wx.entity.Signature;

/**
 * 微信验证url
 * @author srd
 *
 */
public class SignUtil {
	
	/**
	 * 验证
	 * @param token 用户在微信页面设置的token
	 * @param signature 微信消息
	 * @return
	 */
	public static Boolean checkSignature(String token,Signature signature){
		
		String[] strArray = new String[]{token, signature.getTimestamp(), signature.getNonce()};
		Arrays.sort(strArray);//按字典排序
		StringBuffer stringBuffer = new StringBuffer();
		for(String str:strArray){
			stringBuffer.append(str);
		}
		return signature.getSignature().equalsIgnoreCase(DigestUtils.sha1Hex(stringBuffer.toString()));
		
	}
	
}
