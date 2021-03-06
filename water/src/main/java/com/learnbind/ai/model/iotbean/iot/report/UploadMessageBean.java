package com.learnbind.ai.model.iotbean.iot.report;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UploadMessageBean {
    @JsonProperty("notifyType")
    private String notifyType;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("gatewayId")
    private String gatewayId;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("service")
    private ServiceBean service;
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

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public JSONArray getServices() {
        return services;
    }

    public void setServices(JSONArray services) {
        this.services = services;
    }

    public static UploadMessageBean parseJson(String data) {
        UploadMessageBean uploadMessageBean = new UploadMessageBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            uploadMessageBean = mapper.readValue(data, UploadMessageBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadMessageBean;
    }
}
