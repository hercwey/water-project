package com.learnbind.ai.model.iot;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterDataBaseBean {
	
	public static final int METER_DATA_TYPE_UNKNOWN = 0;//未知类型数据
	public static final int METER_DATA_TYPE_REPORT = 1;//设备上报数据
	public static final int METER_DATA_TYPE_CONFIG = 2;//设备配置信息数据
	public static final int METER_DATA_TYPE_MONTH_FREEZE = 3;//设备月冻结数据
	
	@JsonProperty("type")
	private int type = METER_DATA_TYPE_UNKNOWN;
	@JsonProperty("data")
	private String data = "";
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
	
	public static String toJsonString(MeterDataBaseBean bean) {
		return JSON.toJSONString(bean);
	}
	
	public static MeterDataBaseBean fromJson(String json) {
		return JSON.parseObject(json, MeterDataBaseBean.class);
	}
}
