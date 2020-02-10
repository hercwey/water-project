package com.learnbind.ai.model.iot;

import java.util.Date;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.security.crypto.encrypt.BytesEncryptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.learnbind.ai.iot.protocol.bean.MeterReport;
import com.learnbind.ai.iot.util.StringUtil;

public class MeterReportBean {

    private static final long serialVersionUID = 1L;

    @JsonProperty("meterNumber")
    private String meterNumber;    // 表号: 6字节数字型字符串
    @JsonProperty("meterTime")
    private Date meterTime;      // 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
    @JsonProperty("totalVolume")
    private int totalVolume;       // 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
    @JsonProperty("sampleUnit")
    private String sampleUnit;      // 采样参数：单位M3
    @JsonProperty("batteryVoltage")
    private int batteryVoltage;    // 电池电压：单位V
    @JsonProperty("meterStatus")
    private MeterStatusBean meterStatus;       // 表状态字：由meterStatusFlag转换
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
	public Date getMeterTime() {
		return meterTime;
	}
	public void setMeterTime(Date meterTime) {
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
	public MeterStatusBean getMeterStatus() {
		return meterStatus;
	}
	public void setMeterStatus(MeterStatusBean meterStatus) {
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
    
	public static String toJsonString(MeterReportBean meterDataBean) {
		return JSON.toJSONString(meterDataBean);
	}
	
	public static MeterReportBean fromJson(String json) {
		return JSON.parseObject(json, MeterReportBean.class);
	}
	
	public static MeterReportBean fromMeterReport(MeterReport meterReport) {
        MeterReportBean meterReportBean = new MeterReportBean();
		if (meterReport != null) {
			meterReportBean.setMeterNumber(meterReport.getMeterNumber());
			meterReportBean.setMeterTime(StringUtil.meterTimeTrans(meterReport.getMeterTime()));
			meterReportBean.setTotalVolume(meterReport.getTotalVolume());
			meterReportBean.setSampleUnit(meterReport.getSampleUnit()+"");
			meterReportBean.setBatteryVoltage(meterReport.getBatteryVoltage());
			meterReportBean.setMeterStatus(MeterStatusBean.fromStatusFlag(meterReport.getMeterStatus()));
			meterReportBean.setSignal(meterReport.getSignal());
			meterReportBean.setPressure(meterReport.getPressure()+"");
		}
		return meterReportBean;
	}
	
	public static MeterReportBean fromHexData(String data) {
		byte[] bytes = HexUtils.fromHexString(data);
		
		MeterReport meterReport = new MeterReport(bytes);
		return fromMeterReport(meterReport);
	}
}
