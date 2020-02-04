package com.learnbind.ai.model.iot;

import java.util.Date;
import javax.persistence.*;

@Table(name = "WM_DEVICE")
public class WmDevice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "DEVICE_ID")
    private String deviceId;

    @Column(name = "VERIFY_CODE")
    private String verifyCode;

    @Column(name = "MANUFACTURER_ID")
    private String manufacturerId;

    @Column(name = "MANUFACTURER_NAME")
    private String manufacturerName;

    @Column(name = "DEVICE_TYPE")
    private String deviceType;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "PROTOCOL_TYPE")
    private String protocolType;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPATE_TIME")
    private Date upateTime;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return DEVICE_ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * @return VERIFY_CODE
     */
    public String getVerifyCode() {
        return verifyCode;
    }

    /**
     * @param verifyCode
     */
    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode == null ? null : verifyCode.trim();
    }

    /**
     * @return MANUFACTURER_ID
     */
    public String getManufacturerId() {
        return manufacturerId;
    }

    /**
     * @param manufacturerId
     */
    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId == null ? null : manufacturerId.trim();
    }

    /**
     * @return MANUFACTURER_NAME
     */
    public String getManufacturerName() {
        return manufacturerName;
    }

    /**
     * @param manufacturerName
     */
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName == null ? null : manufacturerName.trim();
    }

    /**
     * @return DEVICE_TYPE
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    /**
     * @return MODEL
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * @return PROTOCOL_TYPE
     */
    public String getProtocolType() {
        return protocolType;
    }

    /**
     * @param protocolType
     */
    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType == null ? null : protocolType.trim();
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return UPATE_TIME
     */
    public Date getUpateTime() {
        return upateTime;
    }

    /**
     * @param upateTime
     */
    public void setUpateTime(Date upateTime) {
        this.upateTime = upateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", verifyCode=").append(verifyCode);
        sb.append(", manufacturerId=").append(manufacturerId);
        sb.append(", manufacturerName=").append(manufacturerName);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", model=").append(model);
        sb.append(", protocolType=").append(protocolType);
        sb.append(", createTime=").append(createTime);
        sb.append(", upateTime=").append(upateTime);
        sb.append("]");
        return sb.toString();
    }
}