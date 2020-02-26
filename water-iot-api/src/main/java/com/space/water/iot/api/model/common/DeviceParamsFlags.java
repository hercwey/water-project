package com.space.water.iot.api.model.common;

import com.space.water.iot.api.protocol.bean.MeterConfig;

public class DeviceParamsFlags {
	/* 配置项 */
//    public static final short FLAG_PERIOD             = 0x0001;  
//    public static final short FLAG_PERIOD_UNIT        = 0x0002;  
//    public static final short FLAG_MAX_REPORT         = 0x0004;  
//    public static final short FLAG_EMERG_TIME         = 0x0008;  
//    public static final short FLAG_VALVE_TIME          = 0x0010;  
//    public static final short FLAG_VALVE_MAINTAIN_TIME = 0x0020;  
//    public static final short FLAG_WATER_VLOUME       = 0x0040;  
//    public static final short FLAG_SAMPLE_UNIT        = 0x0080;  
//    public static final short FLAG_METER_NUM          = 0x0100;  
//    public static final short FLAG_METER_TIME         = 0x0200;  
//    public static final short FLAG_METER_STATUS       = 0x0400;  
//    public static final short FLAG_SERVER_IP          = 0x0800;  
//    public static final short FLAG_SERVER_PORT        = 0x1000;  
//    public static final short FLAG_MAGNETIC_ALARM_CLEAR = 0x2000;  

	private Boolean period = false;// 定时上传周期
	private Boolean periodUnit = false;// 定时上传周期单位
	private Boolean maxReport = false;// 定量上传值
	private Boolean emergTime = false;// 用户临时开阀用水限定时间
	private Boolean valveTime = false;// 阀门行程时间
	private Boolean valveMaintainTime = false;// 阀门维护周期
	private Boolean waterVolume = false;// 累计使用量
	private Boolean sampleUnit = false;// 采样参数
	private Boolean meterNum = false;// 表号
	private Boolean meterTime = false;// 表时间
	private Boolean meterStatus = false;// 表状态字
	private Boolean serverIp = false;// 服务器IP
	private Boolean serverPort = false;// 端口号
	private Boolean magneticAlarmClear = false;// 磁干扰告警清除

	public Boolean getPeriod() {
		return period;
	}

	public void setPeriod(Boolean period) {
		this.period = period;
	}

	public Boolean getPeriodUnit() {
		return periodUnit;
	}

	public void setPeriodUnit(Boolean periodUnit) {
		this.periodUnit = periodUnit;
	}

	public Boolean getMaxReport() {
		return maxReport;
	}

	public void setMaxReport(Boolean maxReport) {
		this.maxReport = maxReport;
	}

	public Boolean getEmergTime() {
		return emergTime;
	}

	public void setEmergTime(Boolean emergTime) {
		this.emergTime = emergTime;
	}

	public Boolean getValveTime() {
		return valveTime;
	}

	public void setValveTime(Boolean valveTime) {
		this.valveTime = valveTime;
	}

	public Boolean getValveMaintainTime() {
		return valveMaintainTime;
	}

	public void setValveMaintainTime(Boolean valveMaintainTime) {
		this.valveMaintainTime = valveMaintainTime;
	}

	public Boolean getWaterVolume() {
		return waterVolume;
	}

	public void setWaterVolume(Boolean waterVolume) {
		this.waterVolume = waterVolume;
	}

	public Boolean getSampleUnit() {
		return sampleUnit;
	}

	public void setSampleUnit(Boolean sampleUnit) {
		this.sampleUnit = sampleUnit;
	}

	public Boolean getMeterNum() {
		return meterNum;
	}

	public void setMeterNum(Boolean meterNum) {
		this.meterNum = meterNum;
	}

	public Boolean getMeterTime() {
		return meterTime;
	}

	public void setMeterTime(Boolean meterTime) {
		this.meterTime = meterTime;
	}

	public Boolean getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(Boolean meterStatus) {
		this.meterStatus = meterStatus;
	}

	public Boolean getServerIp() {
		return serverIp;
	}

	public void setServerIp(Boolean serverIp) {
		this.serverIp = serverIp;
	}

	public Boolean getServerPort() {
		return serverPort;
	}

	public void setServerPort(Boolean serverPort) {
		this.serverPort = serverPort;
	}

	public Boolean getMagneticAlarmClear() {
		return magneticAlarmClear;
	}

	public void setMagneticAlarmClear(Boolean magneticAlarmClear) {
		this.magneticAlarmClear = magneticAlarmClear;
	}

	public static DeviceParamsFlags fromShort(short flags) {
		DeviceParamsFlags deviceParamsFlags = new DeviceParamsFlags();

		deviceParamsFlags.setPeriod((flags & MeterConfig.FLAG_PERIOD) > 0);
		deviceParamsFlags.setPeriodUnit((flags & MeterConfig.FLAG_PERIOD_UNIT) > 0);
		deviceParamsFlags.setMaxReport((flags & MeterConfig.FLAG_MAX_REPORT) > 0);
		deviceParamsFlags.setEmergTime((flags & MeterConfig.FLAG_EMERG_TIME) > 0);
		deviceParamsFlags.setValveTime((flags & MeterConfig.FLAG_VALVE_TIME) > 0);
		deviceParamsFlags.setValveMaintainTime((flags & MeterConfig.FLAG_VALVE_MAINTAIN_TIME) > 0);
		deviceParamsFlags.setWaterVolume((flags & MeterConfig.FLAG_WATER_VLOUME) > 0);
		deviceParamsFlags.setSampleUnit((flags & MeterConfig.FLAG_SAMPLE_UNIT) > 0);
		deviceParamsFlags.setMeterNum((flags & MeterConfig.FLAG_METER_NUM) > 0);
		deviceParamsFlags.setMeterTime((flags & MeterConfig.FLAG_METER_TIME) > 0);
		deviceParamsFlags.setMeterStatus((flags & MeterConfig.FLAG_METER_STATUS) > 0);
		deviceParamsFlags.setServerIp((flags & MeterConfig.FLAG_SERVER_IP) > 0);
		deviceParamsFlags.setServerPort((flags & MeterConfig.FLAG_SERVER_PORT) > 0);
		deviceParamsFlags.setMagneticAlarmClear((flags & MeterConfig.FLAG_MAGNETIC_ALARM_CLEAR) > 0);

		return deviceParamsFlags;

	}

	public static short toShort(DeviceParamsFlags deviceParamsFlags) {
		short flags = 0x0000;
		if (deviceParamsFlags.getPeriod()) {
			flags = (short) (flags | MeterConfig.FLAG_PERIOD);
		}
		if (deviceParamsFlags.getPeriodUnit()) {
			flags = (short) (flags | MeterConfig.FLAG_PERIOD_UNIT);
		}
		if (deviceParamsFlags.getMaxReport()) {
			flags = (short) (flags | MeterConfig.FLAG_MAX_REPORT);
		}
		if (deviceParamsFlags.getEmergTime()) {
			flags = (short) (flags | MeterConfig.FLAG_EMERG_TIME);
		}
		if (deviceParamsFlags.getValveTime()) {
			flags = (short) (flags | MeterConfig.FLAG_VALVE_TIME);
		}
		if (deviceParamsFlags.getValveMaintainTime()) {
			flags = (short) (flags | MeterConfig.FLAG_VALVE_MAINTAIN_TIME);
		}
		if (deviceParamsFlags.getWaterVolume()) {
			flags = (short) (flags | MeterConfig.FLAG_WATER_VLOUME);
		}
		if (deviceParamsFlags.getSampleUnit()) {
			flags = (short) (flags | MeterConfig.FLAG_SAMPLE_UNIT);
		}
		if (deviceParamsFlags.getMeterNum()) {
			flags = (short) (flags | MeterConfig.FLAG_METER_NUM);
		}
		if (deviceParamsFlags.getMeterTime()) {
			flags = (short) (flags | MeterConfig.FLAG_METER_TIME);
		}
		if (deviceParamsFlags.getMeterStatus()) {
			flags = (short) (flags | MeterConfig.FLAG_METER_STATUS);
		}
		if (deviceParamsFlags.getServerIp()) {
			flags = (short) (flags | MeterConfig.FLAG_SERVER_IP);
		}
		if (deviceParamsFlags.getServerPort()) {
			flags = (short) (flags | MeterConfig.FLAG_SERVER_PORT);
		}
		if (deviceParamsFlags.getMagneticAlarmClear()) {
			flags = (short) (flags | MeterConfig.FLAG_MAGNETIC_ALARM_CLEAR);
		}

		return flags;
	}
}
