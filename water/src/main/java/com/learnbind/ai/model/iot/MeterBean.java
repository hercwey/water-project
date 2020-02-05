package com.learnbind.ai.model.iot;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnbind.ai.iot.protocol.PacketCodec;
import com.learnbind.ai.iot.protocol.PacketFrame;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;

public class MeterBean {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("gatewayId")
    private String gatewayId;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("serviceType")
    private String serviceType;
    @JsonProperty("eventTime")
    private String eventTime;

    @JsonProperty("meterType")
    private Integer meterType;
    @JsonProperty("meterAddr")
    private String meterAddr;
    @JsonProperty("factoryCode")
    private String factoryCode;
    @JsonProperty("ctrlCode")
    private Integer ctrlCode;
    @JsonProperty("dataDI")
    private Short dataDI;
    @JsonProperty("sequence")
    private Integer sequence;
    @JsonProperty("data")
    private String data;
    @JsonProperty("checksum")
    private Integer checksum;
    @JsonProperty("jsonData")
    private String jsonData;
    @JsonProperty("createtime")
    private Date createtime;
    @JsonProperty("updatetime")
    private Date updatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
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

    public Integer getCtrlCode() {
        return ctrlCode;
    }

    public void setCtrlCode(Integer ctrlCode) {
        this.ctrlCode = ctrlCode;
    }

    public Short getDataDI() {
        return dataDI;
    }

    public void setDataDI(Short dataDI) {
        this.dataDI = dataDI;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public static MeterBean fromUploadDataJson(String data) {
        MeterBean meterBean = new MeterBean();
        UploadMessageBean uploadMessageBean = UploadMessageBean.parseJson(data);

        meterBean.setDeviceId(uploadMessageBean.getDeviceId());
        meterBean.setGatewayId(uploadMessageBean.getGatewayId());
        meterBean.setRequestId(uploadMessageBean.getRequestId());
        meterBean.setJsonData(data);

        ServiceBean serviceBean = uploadMessageBean.getService();
        if (serviceBean == null) {
            JSONArray services= uploadMessageBean.getServices();
            if (services.size() > 0) {
                serviceBean = ServiceBean.parseJson(services.getJSONObject(0).toJSONString());
            }
        }
        if (serviceBean != null) {
            PacketFrame packetFrame = PacketCodec.decodeFrame(HexStringUtils.hexStringToBytes(serviceBean.getData().getJRprotocolXY()));
            meterBean.setServiceId(serviceBean.getServiceId());
            meterBean.setServiceType(serviceBean.getServiceType());
            meterBean.setEventTime(serviceBean.getEventTime());

            meterBean.setMeterType(packetFrame.getMeterType());
            meterBean.setMeterAddr(packetFrame.getMeterAddr());
            meterBean.setFactoryCode(packetFrame.getFactoryCode());
            meterBean.setCtrlCode(packetFrame.getCtrlCode());
            meterBean.setDataDI((short)packetFrame.getDataDI());
            meterBean.setSequence(packetFrame.getSequence());
            meterBean.setData(HexStringUtils.bytesToHexString(packetFrame.getData()));
            meterBean.setChecksum(Integer.valueOf(packetFrame.getChecksum()));
        }

        return meterBean;
    }
}
