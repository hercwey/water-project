package com.learnbind.ai.model.iot;

import org.apache.tools.ant.Main;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnbind.ai.iot.protocol.Protocol;
import com.learnbind.ai.iot.protocol.util.ByteUtil;

public class MeterStatusBean {
	/* 表状态字 */
	/**
    public static final short METER_STATUS_VALVE_OPEN       = 0x0001;    // 阀门状态开 (开1 / 关0)
    public static final short METER_STATUS_VALVE_ABNORMAL   = 0x0002;    // 阀门故障 (1有效 / 0无效)
    public static final short METER_STATUS_BATTERY_LOW      = 0x0004;    // 电池电压低 (1有效 / 0无效)
    public static final short METER_STATUS_PERIOD_ON        = 0x0008;    // 定时上传功能开关 (1开 / 0关)
    public static final short METER_STATUS_MAX_REPORT_ON    = 0x0010;    // 定量上传功能开关 (1开 / 0关)
    public static final short METER_STATUS_MAGNETIC_ON      = 0x0020;    // 磁干扰关阀功能开关 (1开 / 0关)
    public static final short METER_STATUS_SMAPLE_LINE_CUT  = 0x0040;    // 采样线断线报警状态 (1有效 / 0无效)
    */
	
    @JsonProperty("valveOpen")
    private int valveOpen;
    @JsonProperty("valveAbnormal")
    private int valveAbnormal;
    @JsonProperty("batteryLow")
    private int batteryLow;
    @JsonProperty("periodOn")
    private int periodOn;
    @JsonProperty("maxReportOn")
    private int maxReportOn;
    @JsonProperty("magneticOn")
    private int magneticOn;
    @JsonProperty("sampleLineCut")
    private int sampleLineCut;
    
    public int getValveOpen() {
		return valveOpen;
	}

	public void setValveOpen(int valveOpen) {
		this.valveOpen = valveOpen;
	}

	public int getValveAbnormal() {
		return valveAbnormal;
	}

	public void setValveAbnormal(int valveAbnormal) {
		this.valveAbnormal = valveAbnormal;
	}

	public int getBatteryLow() {
		return batteryLow;
	}

	public void setBatteryLow(int batteryLow) {
		this.batteryLow = batteryLow;
	}

	public int getPeriodOn() {
		return periodOn;
	}

	public void setPeriodOn(int periodOn) {
		this.periodOn = periodOn;
	}

	public int getMaxReportOn() {
		return maxReportOn;
	}

	public void setMaxReportOn(int maxReportOn) {
		this.maxReportOn = maxReportOn;
	}

	public int getMagneticOn() {
		return magneticOn;
	}

	public void setMagneticOn(int magneticOn) {
		this.magneticOn = magneticOn;
	}

	public int getSampleLineCut() {
		return sampleLineCut;
	}

	public void setSampleLineCut(int sampleLineCut) {
		this.sampleLineCut = sampleLineCut;
	}

	public static MeterStatusBean fromStatusFlag(short flag) {
    	MeterStatusBean statusBean = new MeterStatusBean();
    	
    	statusBean.setValveOpen((Protocol.METER_STATUS_VALVE_OPEN&flag)>>0);
    	statusBean.setValveAbnormal((Protocol.METER_STATUS_VALVE_ABNORMAL&flag)>>1);
    	statusBean.setBatteryLow((Protocol.METER_STATUS_BATTERY_LOW&flag)>>2);
    	statusBean.setPeriodOn((Protocol.METER_STATUS_PERIOD_ON&flag)>>3);
    	statusBean.setMaxReportOn((Protocol.METER_STATUS_MAX_REPORT_ON&flag)>>4);
    	statusBean.setMagneticOn((Protocol.METER_STATUS_MAGNETIC_ON&flag)>>5);
    	statusBean.setSampleLineCut((Protocol.METER_STATUS_SMAPLE_LINE_CUT&flag)>>6);
    	
    	return statusBean;
    }
	
	public static void main(String[] args) {
		short flag = (short)(Protocol.METER_STATUS_VALVE_OPEN |
				Protocol.METER_STATUS_VALVE_ABNORMAL |
				Protocol.METER_STATUS_BATTERY_LOW |
                Protocol.METER_STATUS_PERIOD_ON |
                Protocol.METER_STATUS_MAX_REPORT_ON |
                Protocol.METER_STATUS_MAGNETIC_ON |
                Protocol.METER_STATUS_SMAPLE_LINE_CUT);
		
		MeterStatusBean statusBean = MeterStatusBean.fromStatusFlag(flag);
		System.out.println(JSON.toJSONString(statusBean));
	}
}
