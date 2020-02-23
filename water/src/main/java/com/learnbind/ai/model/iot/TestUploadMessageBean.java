package com.learnbind.ai.model.iot;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class TestUploadMessageBean {
    @JsonProperty("notifyType")
    private String notifyType;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("gatewayId")
    private String gatewayId;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("service")
    private TestServiceBean service;
    @JsonProperty("services")
    private JSONArray services;

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
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

    public TestServiceBean getService() {
        return service;
    }

    public void setService(TestServiceBean service) {
        this.service = service;
    }

    public JSONArray getServices() {
        return services;
    }

    public void setServices(JSONArray services) {
        this.services = services;
    }

    public static TestUploadMessageBean parseJson(String data) {
        TestUploadMessageBean uploadMessageBean = new TestUploadMessageBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            uploadMessageBean = mapper.readValue(data, TestUploadMessageBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadMessageBean;
    }
}
