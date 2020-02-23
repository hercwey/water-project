package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;

public class QueryParamsRequest extends BaseCommandRequest{

	public static String toJsonString(QueryParamsRequest configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static QueryParamsRequest fromJson(String json) {
		return JSON.parseObject(json, QueryParamsRequest.class);
	}
}
