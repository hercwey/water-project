package com.space.water.iot.api.model.common;

import java.util.Date;

import org.apache.tomcat.util.buf.HexUtils;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.report.MeterStatusBean;
import com.space.water.iot.api.protocol.PacketCodec;
import com.space.water.iot.api.protocol.PacketFrame;
import com.space.water.iot.api.protocol.bean.MeterConfig;
import com.space.water.iot.api.protocol.util.HexStringUtils;
import com.space.water.iot.api.util.StringUtil;

public class DeviceParams {
	private short reportPeriod; // 定时上传周期：2字节(0-65535)十进制，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台；
	private byte reportPeriodUnit; // 定时上传周期单位：1字节(0-255)十进制，0为分钟，1为小时，2为天；
	private short reportRation; // 定量上传值：2字节(0-65535)十进制，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台；
	private byte temporaryTime; // 用户临时开阀用水限定时间：1字节(0-255)十进制，单位为小时，用户可通过磁吸装置实现临时用水；
	private byte valveRunTime; // 阀门行程时间：1字节(0-255)十进制，单位为秒，正常情况下阀门单行程的最大时间值；
	private short valveMaintainPeriod; // 阀门维护周期：2字节(0-65535)十进制，单位为小时，水表以该周期值进行阀门维护操作；
	private int meterBasicValue; // 表底数: 4字节, 整型
	private float sampleUnit; // 采样参数：1字节(0-255)十进制，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样；
	private String meterNumber; // 表号：12字节数字字符串(6字节水表资产编号，BCD格式；)
	private Date meterTime; // 表当前时间：14字节数字字符串格式为yyyymmddHHMMSS (7字节，年、月、星期、日、时、分、秒，BCD格式；)
	private MeterStatusBean meterStatus; // 表状态字：由meterStatusFlag转换
	private String serverIp; // 服务器IP：AAA.BBB.CCC.DDD格式
	private short serverPort; // 端口号：2字节(0-65535)十进制
	private DeviceParamsFlags configFlag;//参数修改标识

	public short getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(short reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public byte getReportPeriodUnit() {
		return reportPeriodUnit;
	}

	public void setReportPeriodUnit(byte reportPeriodUnit) {
		this.reportPeriodUnit = reportPeriodUnit;
	}

	public short getReportRation() {
		return reportRation;
	}

	public void setReportRation(short reportRation) {
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

	public short getValveMaintainPeriod() {
		return valveMaintainPeriod;
	}

	public void setValveMaintainPeriod(short valveMaintainPeriod) {
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

	public short getServerPort() {
		return serverPort;
	}

	public void setServerPort(short serverPort) {
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
		meterConfig.setMeterBasicValue(params.getMeterBasicValue());
		meterConfig.setMeterNumber(params.getMeterNumber());
		meterConfig.setMeterStatusFlag(MeterStatusBean.toStatusFlag(params.getMeterStatus()));
		meterConfig.setMeterTime(params.getMeterTime() != null ? params.getMeterTime().toString():"");
		meterConfig.setReportPeriod(params.getReportPeriod());
		meterConfig.setReportPeriodUnit(params.getReportPeriodUnit());
		meterConfig.setReportRation(params.getReportRation());
		meterConfig.setSampleUnit(params.getSampleUnit());
		meterConfig.setServerIp(params.getServerIp());
		meterConfig.setServerPort(params.getServerPort());
		meterConfig.setTemporaryTime(params.getTemporaryTime());
		meterConfig.setValveMaintainPeriod(params.getValveMaintainPeriod());
		meterConfig.setValveRunTime(params.getValveRunTime());
		return meterConfig;
	}

	public static DeviceParams fromHexData(String data) {
		byte[] bytes = HexUtils.fromHexString(data);

		MeterConfig meterConfig = new MeterConfig(bytes);
		return fromMeterConfig(meterConfig);
	}

	public static void main(String[] args) {
		String tempString = "681054360745404358812621810101000105000c070100aaaaaaaa0154360745404307080506010220290089633c753316dc16";

		PacketFrame packetFrame = PacketCodec.decodeFrame(HexStringUtils.hexStringToBytes(tempString));
		MeterConfig meterConfig = (MeterConfig) PacketCodec.decodeData(packetFrame);
		DeviceParams meterConfigBean = DeviceParams.fromMeterConfig(meterConfig);
		System.out.println(DeviceParams.toJsonString(meterConfigBean));

		DeviceParams meterConfig2 = DeviceParams.fromJson(DeviceParams.toJsonString(meterConfigBean));

		System.out.println(DeviceParams.toJsonString(meterConfig2));
	}
}
