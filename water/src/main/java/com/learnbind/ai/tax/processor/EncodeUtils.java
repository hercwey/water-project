package com.learnbind.ai.tax.processor;

import java.io.UnsupportedEncodingException;

public class EncodeUtils {
	
	/**
	 * @Title: utf2gbk
	 * @Description: UTF-8--->GBK
	 * @param parms  需要转换的字符串(UTF-8编码,JAVA的默认编码)
	 * @return 
	 * 		gbk编码的字符串
	 */
	public static String utf2gbk(String parms) {		
		String plain="";
		try {
			byte[] bytes = parms.getBytes("utf-8");
			byte[] bytes2 = new String(bytes, "utf-8").getBytes("gbk");		
			plain=new String(bytes2, "gbk");
			return plain;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plain;
	}
	
	/**
	 * @Title: gbk2utf
	 * @Description: GBK--->UTF-8
	 * @param parms
	 * @return 
	 */
	public static String gbk2utf(String parms) {		
		String plain="";
		try {
			byte[] bytes = parms.getBytes("gbk");
			byte[] bytes2 = new String(bytes, "gbk").getBytes("utf-8");		
			plain=new String(bytes2, "utf-8");
			return plain;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plain;
	}

}
