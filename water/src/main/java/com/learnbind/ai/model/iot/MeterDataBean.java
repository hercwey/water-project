package com.learnbind.ai.model.iot;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterDataBean {

    private static final long serialVersionUID = 1L;

    @JsonProperty("meterNumber")
    private String meterNumber;    // 表号: 6字节数字型字符串
    @JsonProperty("meterTime")
    private String meterTime;      // 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
    @JsonProperty("totalVolume")
    private int totalVolume;       // 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
    @JsonProperty("sampleUnit")
    private String sampleUnit;      // 采样参数：单位M3
    @JsonProperty("batteryVoltage")
    private int batteryVoltage;    // 电池电压：单位V
    @JsonProperty("meterStatus")
    private String meterStatus;     // 表状态字：2字节
    @JsonProperty("signal")
    private String signal;         // 信号强度
    @JsonProperty("pressure")
    private String pressure;        // 压力值：xx.yyyy
    
	public String getMeterNumber() {
		return meterNumber;
	}
	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}
	public String getMeterTime() {
		return meterTime;
	}
	public void setMeterTime(String meterTime) {
		this.meterTime = meterTime;
	}
	public int getTotalVolume() {
		return totalVolume;
	}
	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}
	public String getSampleUnit() {
		return sampleUnit;
	}
	public void setSampleUnit(String sampleUnit) {
		this.sampleUnit = sampleUnit;
	}
	public int getBatteryVoltage() {
		return batteryVoltage;
	}
	public void setBatteryVoltage(int batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}
	public String getMeterStatus() {
		return meterStatus;
	}
	public void setMeterStatus(String meterStatus) {
		this.meterStatus = meterStatus;
	}
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
    
	public static String toJsonString(MeterDataBean meterDataBean) {
		return JSON.toJSONString(meterDataBean);
	}
	
	public static MeterDataBean fromJson(String json) {
		return JSON.parseObject(json, MeterDataBean.class);
	}
}
