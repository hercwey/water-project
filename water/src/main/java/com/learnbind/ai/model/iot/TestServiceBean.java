package com.learnbind.ai.model.iot;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestServiceBean {
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("serviceType")
    private String serviceType;
    @JsonProperty("data")
    private TestUploadDataBean data;
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

    public TestUploadDataBean getData() {
        return data;
    }

    public void setData(TestUploadDataBean data) {
        this.data = data;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    
    public static TestServiceBean parseJson(String data) {
        TestServiceBean bean = new TestServiceBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            bean = mapper.readValue(data, TestServiceBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bean;
    }
}
