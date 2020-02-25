package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.MonthData;

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
