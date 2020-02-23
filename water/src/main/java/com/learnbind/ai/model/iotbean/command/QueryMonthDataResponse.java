package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.common.MonthData;

public class QueryMonthDataResponse extends BaseCommandResponse{
	MonthData monthData;//月冻结水量

	public MonthData getMonthData() {
		return monthData;
	}

	public void setMonthData(MonthData monthData) {
		this.monthData = monthData;
	}
	public static String toJsonString(QueryMonthDataResponse configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static QueryMonthDataResponse fromJson(String json) {
		return JSON.parseObject(json, QueryMonthDataResponse.class);
	}
}
