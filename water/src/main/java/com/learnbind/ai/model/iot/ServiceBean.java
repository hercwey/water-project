package com.learnbind.ai.model.iot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceBean {
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("serviceType")
    private String serviceType;
    @JsonProperty("data")
    private UploadDataBean data;
    @JsonProperty("eventTime")
    private String eventTime;

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

    public UploadDataBean getData() {
        return data;
    }

    public void setData(UploadDataBean data) {
        this.data = data;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
