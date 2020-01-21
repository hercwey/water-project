package com.learnbind.ai.util.wx;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.learnbind.ai.util.wx.pay.WxOrder;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.util.wx
 *
 * @Title: WxPayOrderUtils.java
 * @Description: 微信支付
 *
 * @author Administrator
 * @date 2019年8月5日 下午6:52:20
 * @version V1.0 
 *
 */
public class WxPayOrderUtils {
	
	/**
	 * @Fields PAY_UNIFIEDORDER_URL：微信统一下单
	 */
	public static final String PAY_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public static final String PAY_ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	public static final String PAY_CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	
	public static final String PAY_REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

	/**
	 * 
	 * @param wxOrder
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String getReqXmlStr(WxOrder wxOrder, String key) throws IllegalArgumentException, IllegalAccessException{
		wxOrder.setNonce_str(createNonceStr());
		Map<String, String> map = WxPayOrderUtils.getObjectMap(wxOrder, wxOrder.getClass());
		String sign = WxPayOrderUtils.getSign(map, key);
		map.put("sign", sign);
		String params = WxPayOrderUtils.getXmlStr(map);
		return params;
	}
	
	/**
	 * 请求Map转换Xml字符串
	 * @param map
	 * @return 
	 */
	public static String getXmlStr(Map<String, String> map){
		if(map==null){
			return null;
		}
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		Set<String> set = map.keySet();
		for(String key:set){
			root.addElement(key).setText(map.get(key));
		}
		return document.asXML();
	}
	
	/**
	 * 请求对象转换为Map
	 * @param object
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Map<String, String> getObjectMap(Object object, Class<?> clazz) throws IllegalArgumentException, IllegalAccessException{
		
		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = clazz.getDeclaredFields();
		
		for(int i=0;i<fields.length;i++){
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			String typeName = fields[i].getType().getName();
			
			if(java.lang.String.class.getName().equals(typeName)){
				String value = (String) fields[i].get(object);
				
				if(value!=null){
					map.put(fieldName, value);
				}
				
			}else if(Integer.class.getName().equals(typeName)||"int".equals(typeName)){
				String value = String.valueOf(fields[i].get(object));
				map.put(fieldName, value);
			}
		}
		
		return map;
	}
	
	/**
	 * 获取签名
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getSign(Map<String, String> map, String key) {
		if(map==null){
			return null;
		}
		
		Set<String> set = map.keySet();
		Object[] strArray = set.toArray();
		Arrays.sort(strArray);//字典排序
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<strArray.length;i++){
			sb.append(strArray[i]).append("=").append(map.get(strArray[i]));
			if(i!=strArray.length-1){
				sb.append("&");
			}
		}
		
		sb.append("&key=").append(key);
		String sign = DigestUtils.md5Hex(sb.toString());
		
        return sign.toUpperCase();
    }
	
	/**
	 * 解析返回xml
	 * @param xmlStr
	 * @return
	 * @throws DocumentException 
	 * @throws UnsupportedEncodingException 
	 */
	public static Map<String, String> parseXML(String xmlStr) throws UnsupportedEncodingException, DocumentException{

		if(StringUtils.isBlank(xmlStr)){
			return null;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		Document document = null;
		document = reader.read(new ByteArrayInputStream(xmlStr.getBytes("utf-8")));
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();

		for (Element e : elementList) {
			if(StringUtils.isBlank(e.getText())){
				continue;
			}
			map.put(e.getName(), e.getText());
		}
		return map;

	}
	
	/**
	 * 验证返回信息签名
	 * @param respMap
	 * @param key
	 * @return
	 */
	public static boolean isWxNotify(Map<String, String> respMap, String key){
		String sign = respMap.get("sign");
		respMap.remove("sign");
		if(sign.equals(WxPayOrderUtils.getSign(respMap, key))){
			return true;
		}
		return false;
	}
	
	/**
	 * 创建随机字符串
	 * @return
	 */
	public static String createNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
	
}
