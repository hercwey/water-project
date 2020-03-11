package com.learnbind.ai.model.iotbean.common;

import java.util.Date;

import org.apache.tomcat.util.buf.HexUtils;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.iot.protocol.PacketCodec;
import com.learnbind.ai.iot.protocol.PacketFrame;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.iot.util.StringUtil;
import com.learnbind.ai.model.iotbean.report.MeterStatusBean;

public class DeviceParams {
	private int reportPeriod; // 定时上传周期：2字节(0-65535)十进制，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台；
	private byte reportPeriodUnit; // 定时上传周期单位：1字节(0-255)十进制，0为分钟，1为小时，2为天；
	private int reportRation; // 定量上传值：2字节(0-65535)十进制，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台；
	private byte temporaryTime; // 用户临时开阀用水限定时间：1字节(0-255)十进制，单位为小时，用户可通过磁吸装置实现临时用水；
	private byte valveRunTime; // 阀门行程时间：1字节(0-255)十进制，单位为秒，正常情况下阀门单行程的最大时间值；
	private int valveMaintainPeriod; // 阀门维护周期：2字节(0-65535)十进制，单位为小时，水表以该周期值进行阀门维护操作；
	private int meterBasicValue; // 表底数: 4字节, 整型
	private float sampleUnit; // 采样参数：1字节(0-255)十进制，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样；
	private String meterNumber; // 表号：12字节数字字符串(6字节水表资产编号，BCD格式；)
	private Date meterTime = new Date(0L); // 表当前时间：14字节数字字符串格式为yyyymmddHHMMSS (7字节，年、月、星期、日、时、分、秒，BCD格式；)
	private MeterStatusBean meterStatus; // 表状态字：由meterStatusFlag转换
	private String serverIp; // 服务器IP：AAA.BBB.CCC.DDD格式
	private int serverPort; // 端口号：2字节(0-65535)十进制
	private DeviceParamsFlags configFlag;//参数修改标识

	public int getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(int reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public byte getReportPeriodUnit() {
		return reportPeriodUnit;
	}

	public void setReportPeriodUnit(byte reportPeriodUnit) {
		this.reportPeriodUnit = reportPeriodUnit;
	}

	public int getReportRation() {
		return reportRation;
	}

	public void setReportRation(int reportRation) {
		this.reportRation = reportRation;
	}

	public byte getTemporaryTime() {
		return temporaryTime;
	}

	public void setTemporaryTime(byte temporaryTime) {
		this.temporaryTime = temporaryTime;
	}

	public byte getValveRunTime() {
		return valveRunTime;
	}

	public void setValveRunTime(byte valveRunTime) {
		this.valveRunTime = valveRunTime;
	}

	public int getValveMaintainPeriod() {
		return valveMaintainPeriod;
	}

	public void setValveMaintainPeriod(int valveMaintainPeriod) {
		this.valveMaintainPeriod = valveMaintainPeriod;
	}

	public int getMeterBasicValue() {
		return meterBasicValue;
	}

	public void setMeterBasicValue(int meterBasicValue) {
		this.meterBasicValue = meterBasicValue;
	}

	public float getSampleUnit() {
		return sampleUnit;
	}

	public void setSampleUnit(float sampleUnit) {
		this.sampleUnit = sampleUnit;
	}

	public String getMeterNumber() {
		return meterNumber;
	}

	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}

	public Date getMeterTime() {
		return meterTime;
	}

	public void setMeterTime(Date meterTime) {
		this.meterTime = meterTime;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public MeterStatusBean getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(MeterStatusBean meterStatus) {
		this.meterStatus = meterStatus;
	}

	public static String toJsonString(DeviceParams configBean) {
		return JSON.toJSONString(configBean);
	}

	public static DeviceParams fromJson(String json) {
		return JSON.parseObject(json, DeviceParams.class);
	}

	public DeviceParamsFlags getConfigFlag() {
		return configFlag;
	}

	public void setConfigFlag(DeviceParamsFlags configFlag) {
		this.configFlag = configFlag;
	}

	public static DeviceParams fromMeterConfig(MeterConfig meterConfig) {
		DeviceParams configBean = new DeviceParams();
		if (meterConfig != null) {
			configBean.setReportPeriod(meterConfig.getReportPeriod());
			configBean.setReportPeriodUnit(meterConfig.getReportPeriodUnit());
			configBean.setReportRation(meterConfig.getReportRation());
			configBean.setTemporaryTime(meterConfig.getTemporaryTime());
			configBean.setValveMaintainPeriod(meterConfig.getValveMaintainPeriod());
			configBean.setMeterBasicValue(meterConfig.getMeterBasicValue());
			configBean.setSampleUnit(meterConfig.getSampleUnit());
			configBean.setMeterNumber(meterConfig.getMeterNumber());
			configBean.setMeterTime(StringUtil.meterTimeTrans(meterConfig.getMeterTime()));
			configBean.setMeterStatus(MeterStatusBean.fromStatusFlag(meterConfig.getMeterStatusFlag()));
			configBean.setServerIp(meterConfig.getServerIp());
			configBean.setServerPort(meterConfig.getServerPort());
		}
		return configBean;
	}

	public static MeterConfig toMeterConfig(DeviceParams params) {
		MeterConfig meterConfig = new MeterConfig();
		DeviceParamsFlags configFlag = params.getConfigFlag();
		if (configFlag == null) {
			configFlag = new DeviceParamsFlags();
		}
		
		if (configFlag.getPeriod()) {// 定时上传周期
			meterConfig.setReportPeriod((short) params.getReportPeriod());
		} else {
			meterConfig.setReportPeriod((short) 0);
		}
		if (configFlag.getPeriodUnit()) {// 定时上传周期单位
			meterConfig.setReportPeriodUnit(params.getReportPeriodUnit());
		} else {
			meterConfig.setReportPeriodUnit((byte) 0);
		}
		if (configFlag.getMaxReport()) {// 定量上传值
			meterConfig.setReportRation((short) params.getReportRation());
		} else {
			meterConfig.setReportRation((short) 0);
		}
		if (configFlag.getEmergTime()) {// 用户临时开阀用水限定时间
			meterConfig.setTemporaryTime(params.getTemporaryTime());
		} else {
			meterConfig.setTemporaryTime((byte) 0);
		}
		if (configFlag.getValveTime()) {// 阀门行程时间
			meterConfig.setValveRunTime(params.getValveRunTime());
		} else {
			meterConfig.setValveRunTime((byte) 0);
		}
		if (configFlag.getValveMaintainTime()) {// 阀门维护周期
			meterConfig.setValveMaintainPeriod((short) params.getValveMaintainPeriod());
		} else {
			meterConfig.setValveMaintainPeriod((short) 0);
		}
		if (configFlag.getWaterVolume()) {// 累计使用量
			meterConfig.setMeterBasicValue(params.getMeterBasicValue());
		} else {
			meterConfig.setMeterBasicValue(0);
		}
		if (configFlag.getSampleUnit()) {// 采样参数
			meterConfig.setSampleUnit(params.getSampleUnit());
		} else {
			meterConfig.setSampleUnit((float) 0.0);
		}
		if (configFlag.getMeterNum()) {// 表号
			meterConfig.setMeterNumber(params.getMeterNumber());
		} else {
			meterConfig.setMeterNumber("000000000000");
		}
		if (configFlag.getMeterTime()) {// 表时间
			meterConfig.setMeterTime(StringUtil.toMeterTime(params.getMeterTime()));
		} else {
			meterConfig.setMeterTime(StringUtil.toMeterTime(null));
		}
		if (configFlag.getMeterStatus()) {// 表状态字
			meterConfig.setMeterStatusFlag(MeterStatusBean.toStatusFlag(params.getMeterStatus()));
		} else {
			meterConfig.setMeterStatusFlag((short) 0);
		}
		if (configFlag.getServerIp()) {// 服务器IP
			meterConfig.setServerIp(params.getServerIp());
		} else {
			meterConfig.setServerIp("0.0.0.0");
		}
		if (configFlag.getServerPort()) {// 端口号
			meterConfig.setServerPort((short) params.getServerPort());
		} else {
			meterConfig.setServerPort((short) 0);
		}
		if (configFlag.getMagneticAlarmClear()) {// 磁干扰告警清除
			
		}
		
		return meterConfig;
	}

	public static DeviceParams fromHexData(String data) {
		byte[] bytes = HexUtils.fromHexString(data);

		MeterConfig meterConfig = new MeterConfig(bytes);
		return fromMeterConfig(meterConfig);
	}

	public static void main(String[] args) {
		String tempString = "681033400745404358812621812902000164000c07ffefc800000002334007454043004813080103200900899d3c753316a816";

		PacketFrame packetFrame = PacketCodec.decodeFrame(HexStringUtils.hexStringToBytes(tempString));
		MeterConfig meterConfig = (MeterConfig) PacketCodec.decodeData(packetFrame);
		DeviceParams meterConfigBean = DeviceParams.fromMeterConfig(meterConfig);
		System.out.println(DeviceParams.toJsonString(meterConfigBean));

		DeviceParams meterConfig2 = DeviceParams.fromJson(DeviceParams.toJsonString(meterConfigBean));

		System.out.println(DeviceParams.toJsonString(meterConfig2));
		//short s=-4097;
		
	}
}
