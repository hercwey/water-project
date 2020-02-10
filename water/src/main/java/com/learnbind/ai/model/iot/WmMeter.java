package com.learnbind.ai.model.iot;

import java.util.Date;
import javax.persistence.*;

@Table(name = "WM_METER")
public class WmMeter {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "DEVICE_ID")
    private Long deviceId;

    @Column(name = "GATEWAY_ID")
    private String gatewayId;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "EVENT_TIME")
    private Date eventTime;

    @Column(name = "METER_TYPE")
    private Long meterType;

    @Column(name = "METER_ADDR")
    private String meterAddr;

    @Column(name = "FACTORY_CODE")
    private String factoryCode;

    @Column(name = "CTRL_CODE")
    private String ctrlCode;

    @Column(name = "DATA_DI")
    private Long dataDi;

    @Column(name = "METER_SEQUENCE")
    private Long meterSequence;

    @Column(name = "METER_DATA_TYPE")
    private String meterDataType;
    
    @Column(name = "METER_DATA")
    private String meterData;
    
    @Column(name = "METER_DATA_BASIC")
    private String meterDataBasic;

    @Column(name = "METER_CHECKSUM")
    private Long meterChecksum;

    @Column(name = "JSON_DATA")
    private String jsonData;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return DEVICE_ID
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return GATEWAY_ID
     */
    public String getGatewayId() {
        return gatewayId;
    }

    /**
     * @param gatewayId
     */
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId == null ? null : gatewayId.trim();
    }

    /**
     * @return REQUEST_ID
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @param requestId
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId == null ? null : requestId.trim();
    }

    /**
     * @return SERVICE_ID
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    /**
     * @return SERVICE_TYPE
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    /**
     * @return EVENT_TIME
     */
    public Date getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime
     */
    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return METER_TYPE
     */
    public Long getMeterType() {
        return meterType;
    }

    /**
     * @param meterType
     */
    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    /**
     * @return METER_ADDR
     */
    public String getMeterAddr() {
        return meterAddr;
    }

    /**
     * @param meterAddr
     */
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }

    /**
     * @return FACTORY_CODE
     */
    public String getFactoryCode() {
        return factoryCode;
    }

    /**
     * @param factoryCode
     */
    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode == null ? null : factoryCode.trim();
    }

    /**
     * @return CTRL_CODE
     */
    public String getCtrlCode() {
        return ctrlCode;
    }

    /**
     * @param ctrlCode
     */
    public void setCtrlCode(String ctrlCode) {
        this.ctrlCode = ctrlCode == null ? null : ctrlCode.trim();
    }

    /**
     * @return DATA_DI
     */
    public Long getDataDi() {
        return dataDi;
    }

    /**
     * @param dataDi
     */
    public void setDataDi(Long dataDi) {
        this.dataDi = dataDi;
    }

    /**
     * @return METER_SEQUENCE
     */
    public Long getMeterSequence() {
        return meterSequence;
    }

    /**
     * @param meterSequence
     */
    public void setMeterSequence(Long meterSequence) {
        this.meterSequence = meterSequence;
    }

    /**
     * @return METER_DATA
     */
    public String getMeterData() {
        return meterData;
    }

    /**
     * @param meterData
     */
    public void setMeterData(String meterData) {
        this.meterData = meterData == null ? null : meterData.trim();
    }

    /**
     * @return METER_CHECKSUM
     */
    public Long getMeterChecksum() {
        return meterChecksum;
    }

    /**
     * @param meterChecksum
     */
    public void setMeterChecksum(Long meterChecksum) {
        this.meterChecksum = meterChecksum;
    }

    /**
     * @return JSON_DATA
     */
    public String getJsonData() {
        return jsonData;
    }

    /**
     * @param jsonData
     */
    public void setJsonData(String jsonData) {
        this.jsonData = jsonData == null ? null : jsonData.trim();
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return UPDATE_TIME
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMeterDataType() {
		return meterDataType;
	}

	public void setMeterDataType(String meterDataType) {
		this.meterDataType = meterDataType;
	}

	public String getMeterDataBasic() {
		return meterDataBasic;
	}

	public void setMeterDataBasic(String meterDataBasic) {
		this.meterDataBasic = meterDataBasic;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", gatewayId=").append(gatewayId);
        sb.append(", requestId=").append(requestId);
        sb.append(", serviceId=").append(serviceId);
        sb.append(", serviceType=").append(serviceType);
        sb.append(", eventTime=").append(eventTime.getTime());
        sb.append(", meterType=").append(meterType);
        sb.append(", meterAddr=").append(meterAddr);
        sb.append(", factoryCode=").append(factoryCode);
        sb.append(", ctrlCode=").append(ctrlCode);
        sb.append(", dataDi=").append(dataDi);
        sb.append(", meterSequence=").append(meterSequence);
        sb.append(", meterDataType=").append(meterDataType);
        sb.append(", meterData=").append(meterData);
        sb.append(", meterDataBasic=").append(meterDataBasic);
        sb.append(", meterChecksum=").append(meterChecksum);
        sb.append(", jsonData=").append(jsonData);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}