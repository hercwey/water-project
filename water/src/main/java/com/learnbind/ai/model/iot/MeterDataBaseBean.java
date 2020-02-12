package com.learnbind.ai.model.iot;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterDataBaseBean {
	
	public static final int METER_DATA_TYPE_UNKNOWN = 0;//未知类型数据
	public static final int METER_DATA_TYPE_REPORT = 1;//设备上报数据
	public static final int METER_DATA_TYPE_RSP_READ_CONFIG = 2;//设备配置信息数据
	public static final int METER_DATA_TYPE_MONTH_FREEZE = 3;//设备月冻结数据
	public static final int METER_DATA_TYPE_START_CONNECT = 4;//设备开始与电信平台建立连接
	public static final int METER_DATA_TYPE_START_DISCONNECT = 5;//设备即将断开连接

	public static final int METER_DATA_TYPE_RSP_WRITE_CONFIG = 6;//写配置指令返回信息
	public static final int METER_DATA_TYPE_RSP_SWITCH_VALVE = 7;//开关阀门指令返回信息
	public static final int METER_DATA_TYPE_RSP_SET_THRESHOLD = 8;//设置阈值指令返回信息
	
	@JsonProperty("type")
	private int type = METER_DATA_TYPE_UNKNOWN;//表计数据类型：0=未知类型数据；1=设备上报数据；2=设备配置信息数据；3=设备月冻结数据；
	@JsonProperty("data")
	private String data = "";
	@JsonProperty("dataBasic")
	private String dataBasic = "";
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public String getDataBasic() {
		return dataBasic;
	}
	public void setDataBasic(String dataBasic) {
		this.dataBasic = dataBasic;
	}
	public static String toJsonString(MeterDataBaseBean bean) {
		return JSON.toJSONString(bean);
	}
	
	public static MeterDataBaseBean fromJson(String json) {
		return JSON.parseObject(json, MeterDataBaseBean.class);
	}
}
