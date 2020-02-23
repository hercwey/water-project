package com.learnbind.ai.model.iotbean.report;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.command.BaseCommandResponse;

public class AutoReport extends BaseCommandResponse{
	MeterReportBean reportData;//设备上报数据

	public MeterReportBean getMonthData() {
		return reportData;
	}

	public void setMonthData(MeterReportBean reportData) {
		this.reportData = reportData;
	}
	public static String toJsonString(AutoReport bean) {
		return JSON.toJSONString(bean);
	}
	
	public static AutoReport fromJson(String json) {
		return JSON.parseObject(json, AutoReport.class);
	}
}
