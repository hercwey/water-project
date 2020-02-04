package com.learnbind.ai.model.iot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class DeviceBean {

    /**
     * {
     *      "deviceId":"f36d7c58-9ded-4d96-af97-0eff0c132151",
     *      "gatewayId":"f36d7c58-9ded-4d96-af97-0eff0c132151",
     *      "nodeType":"GATEWAY",
     *      "createTime":"20200112T033001Z",
     *      "lastModifiedTime":"20200115T032730Z",
     *      "deviceInfo":{
     *          "nodeId":"866733040228123",
     *          "name":"4418",
     *          "description":null,
     *          "manufacturerId":"JR0912X",
     *          "manufacturerName":"JRIWA",
     *          "mac":null,
     *          "location":"",
     *          "deviceType":"JRNBWaterMeter",
     *          "model":"JR0912Y",
     *          "swVersion":null,
     *          "fwVersion":null,
     *          "hwVersion":null,
     *          "protocolType":"CoAP",
     *          "bridgeId":null,
     *          "status":"ONLINE",
     *          "statusDetail":"NONE",
     *          "mute":null,
     *          "supportedSecurity":null,
     *          "isSecurity":null,
     *          "signalStrength":null,
     *          "sigVersion":null,
     *          "serialNumber":null,
     *          "batteryLevel":null,
     *          "isHD":null
     *      },
     *      "services":[
     *          {
     *              "serviceId":"JRprotocol",
     *              "serviceType":"JRprotocol",
     *              "data":{
     *                  "JRprotocolXY":"681083042809191100811d1f90008304280919110000000404010005000000023236595d18000000cf16"
     *              },
     *              "eventTime":"20200115T032729Z",
     *              "serviceInfo":null
     *          }
     *      ],
     *      "connectionInfo":{
     *      },
     *      "location":{
     *          "latitude":null,
     *          "longitude":null,
     *          "accuracy":null,
     *          "time":null,
     *          "description":"",
     *          "region":"China",
     *          "heading":null,
     *          "vehicleSpeed":null,
     *          "crashInformation":null,
     *          "numberOfPassengers":null,
     *          "language":null
     *      },
     *      "productName":"JRNbWater"
     * }
     */


    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("verifyCode")
    private String verifyCode;
    @JsonProperty("manufacturerId")
    private String manufacturerId;
    @JsonProperty("manufacturerName")
    private String manufacturerName;
    @JsonProperty("deviceType")
    private String deviceType;
    @JsonProperty("model")
    private String model;
    @JsonProperty("protocolType")
    private String protocolType;
    @JsonProperty("createTime")
    private String createTime;
    @JsonProperty("updateTime")
    private String updateTime;

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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
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

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static DeviceBean parseJson(String data) {
        DeviceBean deviceBean = new DeviceBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            deviceBean = mapper.readValue(data, DeviceBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deviceBean;
    }
}
