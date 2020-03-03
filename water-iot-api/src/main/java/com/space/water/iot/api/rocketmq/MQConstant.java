package com.space.water.iot.api.rocketmq;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq
 *
 * @Title: MQConstant.java
 * @Description: RocketMQ常量类
 *
 * @author SRD
 * @date 2020年2月21日 下午11:58:05
 * @version V1.0 
 *
 */
public class MQConstant {
	
	//---------------------------消费者分组常量-------------------------------------------------------------
	/**
	 * @Fields C_G_NORTH_AUTO_REPORT：消费者分组-设备自动上报数据
	 */
	public static final String C_G_NORTH_AUTO_REPORT = "NORTH_AUTO_REPORT";
	/**
	 * @Fields C_G_NORTH_CONFIG_PARMS：消费者分组-配置设备参数返回数据
	 */
	public static final String C_G_NORTH_CONFIG_PARMS = "NORTH_CONFIG_PARMS";
	/**
	 * @Fields C_G_NORTH_CONFIG_THRESHOLD：消费者分组-设置阈值返回数据
	 */
	public static final String C_G_NORTH_CONFIG_THRESHOLD = "NORTH_CONFIG_THRESHOLD";
	/**
	 * @Fields C_G_NORTH_CONTROL_VALVE：消费者分组-控制设备（开关阀控制）返回数据
	 */
	public static final String C_G_NORTH_CONTROL_VALVE = "NORTH_CONTROL_VALVE";
	/**
	 * @Fields C_G_NORTH_ORDER_STATUS：消费者分组-命令执行状态返回数据
	 */
	public static final String C_G_NORTH_ORDER_STATUS = "NORTH_ORDER_STATUS";
	/**
	 * @Fields C_G_NORTH_QUERY_MONTH_DATA：消费者分组-查询月冻结返回数据
	 */
	public static final String C_G_NORTH_QUERY_MONTH_DATA = "NORTH_QUERY_MONTH_DATA";
	/**
	 * @Fields C_G_NORTH_QUERY_PARMS：消费者分组-查询参数返回数据
	 */
	public static final String C_G_NORTH_QUERY_PARMS = "NORTH_QUERY_PARMS";
	/**
	 * @Fields C_G_NORTH_DEVICE_REGISTER：消费者分组-注册设备
	 */
	public static final String C_G_NORTH_DEVICE_REGISTER = "NORTH_DEVICE_REGISTER";
	/**
	 * @Fields C_G_NORTH_DEVICE_UPDATE：消费者分组-修改设备
	 */
	public static final String C_G_NORTH_DEVICE_UPDATE = "NORTH_DEVICE_UPDATE";
	/**
	 * @Fields C_G_NORTH_DEVICE_DELETE：消费者分组-删除设备
	 */
	public static final String C_G_NORTH_DEVICE_DELETE = "NORTH_DEVICE_DELETE";
	/**
	 * @Fields C_G_NORTH_DEVICE_QUERY：消费者分组-查询设备
	 */
	public static final String C_G_NORTH_DEVICE_QUERY = "NORTH_DEVICE_QUERY";
	
	//---------------------------生产者分组常量-------------------------------------------------------------
	/**
	 * @Fields P_G_SORTH_CONFIG_PARMS：生产者分组-下发配置设备参数命令
	 */
	public static final String P_G_SORTH_CONFIG_PARMS = "SORTH_CONFIG_PARMS";
	/**
	 * @Fields P_G_SORTH_CONFIG_THRESHOLD：生产者分组-下发设置阈值命令
	 */
	public static final String P_G_SORTH_CONFIG_THRESHOLD = "SORTH_CONFIG_THRESHOLD";
	/**
	 * @Fields P_G_SORTH_CONTROL_VALVE：生产者分组-下发控制设备（开关阀控制）指令
	 */
	public static final String P_G_SORTH_CONTROL_VALVE = "SORTH_CONTROL_VALVE";
	/**
	 * @Fields P_G_SORTH_QUERY_MONTH_DATA：生产者分组-下发查询月冻结数据命令
	 */
	public static final String P_G_SORTH_QUERY_MONTH_DATA = "SORTH_QUERY_MONTH_DATA";
	/**
	 * @Fields P_G_SORTH_QUERY_PARMS：生产者分组-下发查询设备参数命令
	 */
	public static final String P_G_SORTH_QUERY_PARMS = "SORTH_QUERY_PARMS";
	/**
	 * @Fields P_G_SORTH_DEVICE_REGISTER：生产者分组-调用IOT电信平台注册设备接口
	 */
	public static final String P_G_SORTH_DEVICE_REGISTER = "SORTH_DEVICE_REGISTER";
	/**
	 * @Fields P_G_SORTH_DEVICE_UPDATE：生产者分组-调用IOT电信平台修改设备接口
	 */
	public static final String P_G_SORTH_DEVICE_UPDATE = "SORTH_DEVICE_UPDATE";
	/**
	 * @Fields P_G_SORTH_DEVICE_DELETE：生产者分组-调用IOT电信平台删除设备接口
	 */
	public static final String P_G_SORTH_DEVICE_DELETE = "SORTH_DEVICE_DELETE";
	/**
	 * @Fields P_G_SORTH_DEVICE_QUERY：生产者分组-调用IOT电信平台查询设备接口
	 */
	public static final String P_G_SORTH_DEVICE_QUERY = "SORTH_DEVICE_QUERY";
	/**
	 * @Fields P_G_SORTH_ACCOUNT_STATUS_READ：生产者分组-下发读取开户状态指令
	 */
	public static final String P_G_SORTH_ACCOUNT_STATUS_READ = "SORTH_ACCOUNT_STATUS_READ";
	/**
	 * @Fields P_G_SORTH_ACCOUNT_STATUS_WRITE：生产者分组-下发修改开户状态指令
	 */
	public static final String P_G_SORTH_ACCOUNT_STATUS_WRITE = "SORTH_ACCOUNT_STATUS_WRITE";
	
	/**
	 * @Fields G_AUTO_REPORT_CACHE:设备上报数据缓存
	 */
	public static final String G_AUTO_REPORT_CACHE = "AUTO_REPORT_CACHE";
	
	//---------------------------字符集常量-------------------------------------------------------------
	/**
	 * @Fields CHARSET_NAME：字符集
	 */
	public static final String CHARSET_NAME = "UTF-8";
	
	//---------------------------Name Server地址常量-------------------------------------------------------------
    /**
     * Name Server地址，如果是集群部署 所以有多个用 分号 隔开
     */
    //public static final String NAME_SERVER = "127.12.15.6:9876;127.12.15.6:9877";
	//public static final String NAME_SERVER = "127.0.0.1:9876";
	public static final String NAME_SERVER = "39.107.230.168:9876";
    

}
