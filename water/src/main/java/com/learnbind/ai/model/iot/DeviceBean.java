package com.learnbind.ai.model.iot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import java.io.IOException;

import javax.persistence.Column;

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
    
    @JsonProperty("meterType")
    private Integer meterType=0;
    @JsonProperty("meterAddress")
    private String meterAddress="";
    @JsonProperty("meterFactoryCode")
    private String meterFactoryCode="";
    @JsonProperty("meterSequence")
    private Integer meterSequence=1;
    
    @JsonProperty("meterConfig")
    private String meterConfig="";
    @JsonProperty("meterFreeze")
    private String meterFreeze="";

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

    public Integer getMeterType() {
		return meterType;
	}

	public void setMeterType(Integer meterType) {
		this.meterType = meterType;
	}

	public String getMeterAddress() {
		return meterAddress;
	}

	public void setMeterAddress(String meterAddress) {
		this.meterAddress = meterAddress;
	}

	public String getMeterFactoryCode() {
		return meterFactoryCode;
	}

	public void setMeterFactoryCode(String meterFactoryCode) {
		this.meterFactoryCode = meterFactoryCode;
	}

	public Integer getMeterSequence() {
		return meterSequence;
	}

	public void setMeterSequence(Integer meterSequence) {
		this.meterSequence = meterSequence;
	}

	public String getMeterConfig() {
		return meterConfig;
	}

	public void setMeterConfig(String meterConfig) {
		this.meterConfig = meterConfig;
	}

	public String getMeterFreeze() {
		return meterFreeze;
	}

	public void setMeterFreeze(String meterFreeze) {
		this.meterFreeze = meterFreeze;
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
	
	public static DeviceBean fromWmDevice(WmDevice wmDevice) {
		DeviceBean deviceBean = new DeviceBean();
		deviceBean.setId(wmDevice.getId());
		deviceBean.setDeviceId(wmDevice.getDeviceId());
		deviceBean.setVerifyCode(wmDevice.getVerifyCode());
		deviceBean.setManufacturerId(wmDevice.getManufacturerId());
		deviceBean.setManufacturerName(wmDevice.getManufacturerName());
		deviceBean.setDeviceType(wmDevice.getDeviceType());
		deviceBean.setModel(wmDevice.getModel());
		deviceBean.setProtocolType(wmDevice.getProtocolType());
		deviceBean.setCreateTime(wmDevice.getCreateTime().toString());
		deviceBean.setUpdateTime(wmDevice.getUpateTime().toString());
		deviceBean.setMeterType(wmDevice.getMeterType());
		deviceBean.setMeterAddress(wmDevice.getMeterAddress());
		deviceBean.setMeterFactoryCode(wmDevice.getMeterFactoryCode());
		deviceBean.setMeterSequence(wmDevice.getMeterSequence());
		deviceBean.setMeterConfig(wmDevice.getMeterConfig());
		deviceBean.setMeterFreeze(wmDevice.getMeterFreeze());
		return deviceBean;
	}
}
