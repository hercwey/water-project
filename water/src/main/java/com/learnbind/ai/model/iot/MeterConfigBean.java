package com.learnbind.ai.model.iot;

import java.util.Date;

import org.apache.tomcat.util.buf.HexUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnbind.ai.iot.protocol.PacketCodec;
import com.learnbind.ai.iot.protocol.PacketFrame;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.util.ByteUtil;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.iot.util.StringUtil;

public class MeterConfigBean {

    private static final long serialVersionUID = 1L;

    @JsonProperty("reportPeriod")
    private short reportPeriod;            // 定时上传周期：2字节(0-65535)十进制，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台；

    @JsonProperty("reportPeriodUnit")
    private byte reportPeriodUnit;         // 定时上传周期单位：1字节(0-255)十进制，0为分钟，1为小时，2为天；

    @JsonProperty("reportRation")
    private short reportRation;         // 定量上传值：2字节(0-65535)十进制，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台；

    @JsonProperty("temporaryTime")
    private byte temporaryTime;      // 用户临时开阀用水限定时间：1字节(0-255)十进制，单位为小时，用户可通过磁吸装置实现临时用水；

    @JsonProperty("valveRunTime")
    private byte valveRunTime;          // 阀门行程时间：1字节(0-255)十进制，单位为秒，正常情况下阀门单行程的最大时间值；

    @JsonProperty("valveMaintainPeriod")
    private short valveMaintainPeriod; // 阀门维护周期：2字节(0-65535)十进制，单位为小时，水表以该周期值进行阀门维护操作；

    @JsonProperty("meterBasicValue")
    private int meterBasicValue;     // 表底数: 4字节, 整型

    @JsonProperty("sampleUnit")
    private float sampleUnit;        // 采样参数：1字节(0-255)十进制，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样；

    @JsonProperty("meterNumber")
    private String meterNumber;      // 表号：12字节数字字符串(6字节水表资产编号，BCD格式；)

    @JsonProperty("meterTime")
    private Date meterTime;        // 表当前时间：14字节数字字符串格式为yyyymmddHHMMSS (7字节，年、月、星期、日、时、分、秒，BCD格式；)

    @JsonProperty("meterStatus")
    private TestMeterStatusBean meterStatus;       // 表状态字：由meterStatusFlag转换

    @JsonProperty("serverIp")
    private String serverIp;         // 服务器IP：AAA.BBB.CCC.DDD格式

    @JsonProperty("serverPort")
    private short serverPort;        // 端口号：2字节(0-65535)十进制
    
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

	public TestMeterStatusBean getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(TestMeterStatusBean meterStatus) {
		this.meterStatus = meterStatus;
	}

	public static String toJsonString(MeterConfigBean meterConfigBean) {
		return JSON.toJSONString(meterConfigBean);
	}
	
	public static MeterConfigBean fromJson(String json) {
		return JSON.parseObject(json, MeterConfigBean.class);
	}
	public static MeterConfigBean fromMeterConfig(MeterConfig meterConfig) {
        MeterConfigBean meterConfigBean = new MeterConfigBean();
		if (meterConfig != null) {
			meterConfigBean.setReportPeriod(meterConfig.getReportPeriod());
			meterConfigBean.setReportPeriodUnit(meterConfig.getReportPeriodUnit());
			meterConfigBean.setReportRation(meterConfig.getReportRation());
			meterConfigBean.setTemporaryTime(meterConfig.getTemporaryTime());
			meterConfigBean.setValveMaintainPeriod(meterConfig.getValveMaintainPeriod());
			meterConfigBean.setMeterBasicValue(meterConfig.getMeterBasicValue());
			meterConfigBean.setSampleUnit(meterConfig.getSampleUnit());
			meterConfigBean.setMeterNumber(meterConfig.getMeterNumber());
			meterConfigBean.setMeterTime(StringUtil.meterTimeTrans(meterConfig.getMeterTime()));
			meterConfigBean.setMeterStatus(TestMeterStatusBean.fromStatusFlag(meterConfig.getMeterStatusFlag()));
			meterConfigBean.setServerIp(meterConfig.getServerIp());
			meterConfigBean.setServerPort(meterConfig.getServerPort());
			
		}
		return meterConfigBean;
	}
	
	public static MeterConfigBean fromHexData(String data) {
		byte[] bytes = HexUtils.fromHexString(data);
		
		MeterConfig meterConfig = new MeterConfig(bytes);
		return fromMeterConfig(meterConfig);
	}
	
	public static void main(String[] args) {
		String tempString = "681054360745404358812621810101000105000c070100aaaaaaaa0154360745404307080506010220290089633c753316dc16";
		
		PacketFrame packetFrame = PacketCodec.decodeFrame(HexStringUtils.hexStringToBytes(tempString));
		MeterConfig meterConfig = (MeterConfig)PacketCodec.decodeData(packetFrame);
        MeterConfigBean meterConfigBean = MeterConfigBean.fromMeterConfig(meterConfig);
        System.out.println(MeterConfigBean.toJsonString(meterConfigBean));
        
        MeterConfigBean meterConfig2 = MeterConfigBean.fromJson(MeterConfigBean.toJsonString(meterConfigBean));

        System.out.println(MeterConfigBean.toJsonString(meterConfig2));
	}
}
