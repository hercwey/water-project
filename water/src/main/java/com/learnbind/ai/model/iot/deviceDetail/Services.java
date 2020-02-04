package com.learnbind.ai.model.iot.deviceDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Services {

    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("serviceType")
    private String serviceType;
    @JsonProperty("data")
    private DeviceData data;
    @JsonProperty("eventTime")
    private String eventTime;
    @JsonProperty("serviceInfo")
    private String serviceInfo;

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

    public DeviceData getData() {
        return data;
    }

    public void setData(DeviceData data) {
        this.data = data;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }
}