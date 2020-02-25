package com.space.water.iot.api.model.report;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.ReportDataType;

public class MeterDataBaseBean {
	
	private int type = ReportDataType.METER_DATA_TYPE_UNKNOWN;
	private String data = "";
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
