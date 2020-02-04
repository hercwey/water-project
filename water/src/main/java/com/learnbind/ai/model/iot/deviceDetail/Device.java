package com.learnbind.ai.model.iot.deviceDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Device {
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("gatewayId")
    private String gatewayId;
    @JsonProperty("nodeType")
    private String nodeType;
    @JsonProperty("createTime")
    private String createTime;
    @JsonProperty("lastModifiedTime")
    private String lastModifiedTime;
    @JsonProperty("deviceInfo")
    private DeviceInfo deviceInfo;
    @JsonProperty("services")
    private List<Services> services;
    @JsonProperty("connectionInfo")
    private ConnectionInfo connectionInfo;
    @JsonProperty("location")
    private Location_ location;
    @JsonProperty("productName")
    private String productName;

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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public Location_ getLocation() {
        return location;
    }

    public void setLocation(Location_ location) {
        this.location = location;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
