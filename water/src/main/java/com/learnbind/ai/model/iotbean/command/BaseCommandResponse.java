package com.learnbind.ai.model.iotbean.command;

import java.util.Date;

import com.learnbind.ai.model.iotbean.common.BaseResponse;

public class BaseCommandResponse extends BaseResponse{
	private String deviceId;//设备ID（电信平台）
    private String gatewayId;//网关ID，默认与设备ID相同（电信平台）
    private String requestId;//请求ID（电信平台）
    private String serviceId;//服务ID（电信平台）
    private String serviceType;//服务类型ID（电信平台）
    private Date eventTime;//事件时间（电信平台）

    private Integer meterType;// 表类型
    private String meterAddr;// 表地址
    private String factoryCode;// 表厂商代码
    private String ctrlCode;// 控制码
    private short dataDI;// 数据标识
    private Integer sequence;// 序列号
    private Integer dataType;//数据类型（对应ReportDataType）
    private String data;//解析后的json数据
    private String dataBasic;//原始HEX数据
    private Integer checksum;// 校验和
    private String jsonData;//接收电信平台的完整json数据
    
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public Integer getMeterType() {
		return meterType;
	}
	public void setMeterType(Integer meterType) {
		this.meterType = meterType;
	}
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getCtrlCode() {
		return ctrlCode;
	}
	public void setCtrlCode(String ctrlCode) {
		this.ctrlCode = ctrlCode;
	}
	public short getDataDI() {
		return dataDI;
	}
	public void setDataDI(short dataDI) {
		this.dataDI = dataDI;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataBasic() {
		return dataBasic;
	}
	public void setDataBasic(String dataBasic) {
		this.dataBasic = dataBasic;
	}
	public Integer getChecksum() {
		return checksum;
	}
	public void setChecksum(Integer checksum) {
		this.checksum = checksum;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
    
    
}
