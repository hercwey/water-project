package com.space.water.iot.api.model.common;

public class ReportDataType {	
	public static final int UNKNOWN = 0;//未知类型数据
	public static final int REPORT = 1;//设备上报数据
	public static final int RSP_READ_CONFIG = 2;//设备配置信息数据
	public static final int MONTH_FREEZE = 3;//设备月冻结数据
	public static final int START_CONNECT = 4;//设备开始与电信平台建立连接
	public static final int START_DISCONNECT = 5;//设备即将断开连接

	public static final int RSP_WRITE_CONFIG = 6;//写配置指令返回信息
	public static final int RSP_SWITCH_VALVE = 7;//开关阀门指令返回信息
	public static final int RSP_SET_THRESHOLD = 8;//设置阈值指令返回信息
	public static final int RSP_ACCOUNT_STATUS_READ = 9;//读取开户状态
	public static final int RSP_ACCOUNT_STATUS_WRITE = 10;//设置开户状态
}
