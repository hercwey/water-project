package com.learnbind.ai.model.iot;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnbind.ai.iot.protocol.Protocol;
import com.learnbind.ai.iot.protocol.bean.MeterReadWaterResp;

public class MeterMonthFreezeBean {
	@JsonProperty("meterNumber")
	private String meterNumber;             // 表号， 6字节数字字符串

	@JsonProperty("sampleUnit")
    private float sampleUnit;               // 数据采样单位M3

	@JsonProperty("freezeList")
    private List<Integer> freezeList;    // 月冻结水量数据:0,上一月；1，上2月；....；11，上12月

	public String getMeterNumber() {
		return meterNumber;
	}

	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}

	public float getSampleUnit() {
		return sampleUnit;
	}

	public void setSampleUnit(float sampleUnit) {
		this.sampleUnit = sampleUnit;
	}

	public List<Integer> getFreezeList() {
		return freezeList;
	}

	public void setFreezeList(List<Integer> freezeList) {
		this.freezeList = freezeList;
	}
	
	public static String toJsonString(MeterMonthFreezeBean bean) {
		return JSON.toJSONString(bean);
	}
	
	public static MeterMonthFreezeBean fromJson(String json) {
		return JSON.parseObject(json, MeterMonthFreezeBean.class);
	}
	
	public static MeterMonthFreezeBean fromMeterTeadWaterResp(MeterReadWaterResp resp) {
		MeterMonthFreezeBean bean = new MeterMonthFreezeBean();
		
		bean.setMeterNumber(resp.getMeterNumber());
		bean.setSampleUnit(resp.getSampleUnit());
		bean.setFreezeList(new ArrayList<Integer>());
		if (resp.getListWater() != null) {
			for (int i = 0; i < resp.getListWater().size(); i++) {
				bean.getFreezeList().add(i, resp.getListWater().get(i).getVolume());
			}
		}
		
    	return bean;
    }
}
