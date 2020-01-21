package com.learnbind.ai.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) 2018 by Hz
 * 
 * @ClassName: Propertiesconfiguration.java
 * @Description: 自application.properties属性文件中读取系统配置
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年9月1日 下午6:11:36
 */
public class Propertiesconfiguration {

	/**
	 * 加载资源文件中系统属性配置文件
	 */
	private static final String APP_PROPERTIES_FILE = "/application.properties";

	/**
	 * 加载日志文件
	 */
	private static final Log logger = LogFactory.getLog(Propertiesconfiguration.class);

	/**
	 * 定义Properties对象
	 */
	private static Properties p = null;

	static {
		init();
	}

	/**
	 * 默认构造函数
	 */
	public Propertiesconfiguration() {
	}

	/**
	 * 初始化加载系统配置文件
	 */
	protected static void init() {
		InputStream in = null;

		try {
			// 读取系统配置文件文件流
			in = Propertiesconfiguration.class.getResourceAsStream(APP_PROPERTIES_FILE);

			if (in != null) {
				if (p == null) {
					p = new Properties();
				}
				p.load(in);
			}
		} catch (IOException e) {
			logger.error("load " + APP_PROPERTIES_FILE + " into Constants error!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("close " + APP_PROPERTIES_FILE + " error!");
				}
			}
		}
	}

	
	/** 
	*	@Title: getStringProperty 
	*	@Description: 根据传入的key值，获取相应的Value 
	*	@param @param key 传入的key
	*	@param @return     
	*	@return String    返回类型 相应的Value 
	*/
	public static String getStringProperty(String key) {
		if (null != p) {
			return p.getProperty(key);
		}
		return "";
	}
}
