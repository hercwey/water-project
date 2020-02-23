package com.learnbind.ai.model.iotbean.common;

public class ReportDataType {	
	/**
	 * @Fields METER_DATA_TYPE_UNKNOWN：未知类型数据
	 */
	public static final int METER_DATA_TYPE_UNKNOWN = 0;//未知类型数据
	/**
	 * @Fields METER_DATA_TYPE_REPORT：设备主动上报数据
	 */
	public static final int METER_DATA_TYPE_REPORT = 1;//设备主动上报数据
	/**
	 * @Fields METER_DATA_TYPE_RSP_READ_CONFIG：设备配置信息数据
	 */
	public static final int METER_DATA_TYPE_RSP_READ_CONFIG = 2;//设备配置信息数据
	/**
	 * @Fields METER_DATA_TYPE_MONTH_FREEZE：设备月冻结数据
	 */
	public static final int METER_DATA_TYPE_MONTH_FREEZE = 3;//设备月冻结数据
	/**
	 * @Fields METER_DATA_TYPE_START_CONNECT：设备开始与电信平台建立连接
	 */
	public static final int METER_DATA_TYPE_START_CONNECT = 4;//设备开始与电信平台建立连接
	/**
	 * @Fields METER_DATA_TYPE_START_DISCONNECT：设备即将断开连接
	 */
	public static final int METER_DATA_TYPE_START_DISCONNECT = 5;//设备即将断开连接
	/**
	 * @Fields METER_DATA_TYPE_RSP_WRITE_CONFIG：写配置指令返回信息
	 */
	public static final int METER_DATA_TYPE_RSP_WRITE_CONFIG = 6;//写配置指令返回信息
	/**
	 * @Fields METER_DATA_TYPE_RSP_SWITCH_VALVE：开关阀门指令返回信息
	 */
	public static final int METER_DATA_TYPE_RSP_SWITCH_VALVE = 7;//开关阀门指令返回信息
	/**
	 * @Fields METER_DATA_TYPE_RSP_SET_THRESHOLD：设置阈值指令返回信息
	 */
	public static final int METER_DATA_TYPE_RSP_SET_THRESHOLD = 8;//设置阈值指令返回信息
}
