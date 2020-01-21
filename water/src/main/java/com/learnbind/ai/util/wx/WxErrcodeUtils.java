package com.learnbind.ai.util.wx;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信错误代码处理
 * @author srd
 *
 */
public class WxErrcodeUtils {
	
	private static final Logger log = LoggerFactory.getLogger(WxErrcodeUtils.class);
	
	private static Properties properties;
	
	static{
		properties = new Properties();
		InputStream in = null;
		try {
			//需要判定errorcode配置文件是否可以真正读取到
			//commented at 2019/08/05
			in = WxErrcodeUtils.class.getClassLoader().getResourceAsStream("config/wx-errcode.properties");
			properties.load(in);
		} catch (Exception e) {
			log.error("加载wx-errcode.properties异常！", e);
		}finally{
			if (in != null) {
	            try {
	                in.close();
	            } catch(InterruptedIOException ignore) {
	                Thread.currentThread().interrupt();
	            } catch(IOException ignore) {
	            } catch(RuntimeException ignore) {
	            }
	        }
		}
	}
	
	/**
	 * 判断不是错误信息
	 * @param json
	 * @return true 不包含 false包含
	 */
	public static Boolean isNotErrcode(JSONObject json){
		if(json!=null){
			int errcode = json.getIntValue("errcode");
			if(errcode==0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取错误信息
	 * @return 错误信息
	 */
	public static String getErrcodeMessage(String errcode){
		
		if(StringUtils.isEmpty(errcode)){
			return "errcode为null！";
		}
		
		String value = properties.getProperty(errcode);
		if(value!=null){
			return value;
		}
		
		return "未知的异常:"+errcode;
	}
	
	/**
	 * 获取错误信息
	 * @param json
	 * @return 错误信息
	 */
	public static String getErrcodeMessage(JSONObject json){
		
		if(json==null){
			return null;
		}
		
		String errcode = json.getString("errcode");
		return getErrcodeMessage(errcode);
		
	}
	
	public static void doErrcode(String errcode){
		
	}
	
	
}
