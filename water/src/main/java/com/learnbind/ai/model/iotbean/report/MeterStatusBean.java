package com.learnbind.ai.model.iotbean.report;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.iot.protocol.Protocol;

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
    public static final short METER_STATUS_MAGNETIC_ALARM_ON    = 0x0080;    // 磁干扰报警 (1有效 / 0无效)
    */
	
    private int valveOpen;//阀门状态
    private int valveAbnormal;//阀门故障
    private int batteryLow;//电池电压低
    private int periodOn;//定时上传功能开关
    private int maxReportOn;//定量上传功能开关
    private int magneticOn;//磁干扰关阀功能开关
    private int sampleLineCut;//采样线断线报警状态
    private int magneticAlarmOn;//磁干扰报警
    
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
	
	public int getMagneticAlarmOn() {
		return magneticAlarmOn;
	}

	public void setMagneticAlarmOn(int magneticAlarmOn) {
		this.magneticAlarmOn = magneticAlarmOn;
	}

	public static String toJsonString(MeterStatusBean meterStatusBean) {
		return JSON.toJSONString(meterStatusBean);
	}
	
	public static MeterStatusBean fromJson(String json) {
		return JSON.parseObject(json, MeterStatusBean.class);
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
    	statusBean.setMagneticAlarmOn((Protocol.METER_STATUS_MAGNETIC_ALARM_ON&flag)>>7);
    	
    	return statusBean;
    }
	
	public static short toStatusFlag(MeterStatusBean statusBean) {
		
		short status = 0;
		if (statusBean != null) {
			if (statusBean.getValveOpen() == 1) {
				status = (short) (status + Protocol.METER_STATUS_VALVE_OPEN);
			}
			if (statusBean.getValveAbnormal() == 1) {
				status = (short) (status + Protocol.METER_STATUS_VALVE_ABNORMAL);
			}
			if (statusBean.getBatteryLow() == 1) {
				status = (short) (status + Protocol.METER_STATUS_BATTERY_LOW);
			}
			if (statusBean.getPeriodOn() == 1) {
				status = (short) (status + Protocol.METER_STATUS_PERIOD_ON);
			}
			if (statusBean.getMaxReportOn() == 1) {
				status = (short) (status + Protocol.METER_STATUS_MAX_REPORT_ON);
			}
			if (statusBean.getMagneticOn() == 1) {
				status = (short) (status + Protocol.METER_STATUS_MAGNETIC_ON);
			}
			if (statusBean.getSampleLineCut() == 1) {
				status = (short) (status + Protocol.METER_STATUS_SMAPLE_LINE_CUT);
			}
			if (statusBean.getMagneticAlarmOn() == 1) {
				status = (short) (status + Protocol.METER_STATUS_MAGNETIC_ALARM_ON);
			}
		}
		
		return status;
	}

	@Override
	public String toString() {
		return "MeterStatusBean [valveOpen=" + valveOpen + ", valveAbnormal=" + valveAbnormal + ", batteryLow="
				+ batteryLow + ", periodOn=" + periodOn + ", maxReportOn=" + maxReportOn + ", magneticOn=" + magneticOn
				+ ", sampleLineCut=" + sampleLineCut + ", magneticAlarmOn=" + magneticAlarmOn + "]";
	}
	
}
