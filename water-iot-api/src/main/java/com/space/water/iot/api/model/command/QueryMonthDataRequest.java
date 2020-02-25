package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;

public class QueryMonthDataRequest extends BaseCommandRequest{

	public static String toJsonString(QueryMonthDataRequest configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static QueryMonthDataRequest fromJson(String json) {
		return JSON.parseObject(json, QueryMonthDataRequest.class);
	}
}