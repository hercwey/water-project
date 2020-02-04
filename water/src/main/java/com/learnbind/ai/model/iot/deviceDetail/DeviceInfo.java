package com.learnbind.ai.model.iot.deviceDetail;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceInfo {

    @JsonProperty("nodeId")
    private String nodeId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("manufacturerId")
    private String manufacturerId;
    @JsonProperty("manufacturerName")
    private String manufacturerName;
    @JsonProperty("mac")
    private String mac;
    @JsonProperty("location")
    private String location;
    @JsonProperty("deviceType")
    private String deviceType;
    @JsonProperty("model")
    private String model;
    @JsonProperty("swVersion")
    private String swVersion;
    @JsonProperty("fwVersion")
    private String fwVersion;
    @JsonProperty("hwVersion")
    private String hwVersion;
    @JsonProperty("protocolType")
    private String protocolType;
    @JsonProperty("bridgeId")
    private String bridgeId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("statusDetail")
    private String statusDetail;
    @JsonProperty("mute")
    private String mute;
    @JsonProperty("supportedSecurity")
    private String supportedSecurity;
    @JsonProperty("isSecurity")
    private String isSecurity;
    @JsonProperty("signalStrength")
    private String signalStrength;
    @JsonProperty("sigVersion")
    private String sigVersion;
    @JsonProperty("serialNumber")
    private String serialNumber;
    @JsonProperty("batteryLevel")
    private String batteryLevel;
    @JsonProperty("isHD")
    private String isHD;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public String getFwVersion() {
        return fwVersion;
    }

    public void setFwVersion(String fwVersion) {
        this.fwVersion = fwVersion;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getBridgeId() {
        return bridgeId;
    }

    public void setBridgeId(String bridgeId) {
        this.bridgeId = bridgeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public String getSupportedSecurity() {
        return supportedSecurity;
    }

    public void setSupportedSecurity(String supportedSecurity) {
        this.supportedSecurity = supportedSecurity;
    }

    public String getIsSecurity() {
        return isSecurity;
    }

    public void setIsSecurity(String isSecurity) {
        this.isSecurity = isSecurity;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getSigVersion() {
        return sigVersion;
    }

    public void setSigVersion(String sigVersion) {
        this.sigVersion = sigVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getIsHD() {
        return isHD;
    }

    public void setIsHD(String isHD) {
        this.isHD = isHD;
    }
}