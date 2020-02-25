package com.space.water.iot.api.model.report;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.command.BaseCommandResponse;

public class AutoReport extends BaseCommandResponse{
	MeterReportBean reportData;//设备上报数据

	public MeterReportBean getReportData() {
		return reportData;
	}

	public void setReportData(MeterReportBean reportData) {
		this.reportData = reportData;
	}
	public static String toJsonString(AutoReport bean) {
		return JSON.toJSONString(bean);
	}
	
	public static AutoReport fromJson(String json) {
		return JSON.parseObject(json, AutoReport.class);
	}
}
