package com.learnbind.ai.model.iot;

import java.util.Date;
import javax.persistence.*;

@Table(name = "WM_COMMAND")
public class WmCommand {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "COMMAND_ID")
    private String commandId;

    @Column(name = "APP_ID")
    private String appId;

    @Column(name = "DEVICE_ID")
    private Long deviceId;

    @Column(name = "SERVICE_ID")
    private Long serviceId;

    @Column(name = "METHOD_ID")
    private Long methodId;

    @Column(name = "METHOD_PARAMS")
    private String methodParams;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "EXPIRE_TIME")
    private Long expireTime;

    @Column(name = "PLATFORM_ISSUED_TIME")
    private String platformIssuedTime;

    @Column(name = "ISSUED_TIMES")
    private Integer issuedTimes;

    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    @Column(name = "COMMAND_TYPE")
    private Integer commandType;

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
     * @return COMMAND_ID
     */
    public String getCommandId() {
        return commandId;
    }

    /**
     * @param commandId
     */
    public void setCommandId(String commandId) {
        this.commandId = commandId == null ? null : commandId.trim();
    }

    /**
     * @return APP_ID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return DEVICE_ID
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return SERVICE_ID
     */
    public Long getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     */
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return METHOD_ID
     */
    public Long getMethodId() {
        return methodId;
    }

    /**
     * @param methodId
     */
    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    /**
     * @return METHOD_PARAMS
     */
    public String getMethodParams() {
        return methodParams;
    }

    /**
     * @param methodParams
     */
    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams == null ? null : methodParams.trim();
    }

    /**
     * @return OPERATOR_ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return STATUS
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return EXPIRE_TIME
     */
    public Long getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime
     */
    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * @return PLATFORM_ISSUED_TIME
     */
    public String getPlatformIssuedTime() {
        return platformIssuedTime;
    }

    /**
     * @param platformIssuedTime
     */
    public void setPlatformIssuedTime(String platformIssuedTime) {
        this.platformIssuedTime = platformIssuedTime == null ? null : platformIssuedTime.trim();
    }

    /**
     * @return ISSUED_TIMES
     */
    public Integer getIssuedTimes() {
        return issuedTimes;
    }

    /**
     * @param issuedTimes
     */
    public void setIssuedTimes(Integer issuedTimes) {
        this.issuedTimes = issuedTimes;
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

    public Integer getCommandType() {
		return commandType;
	}

	public void setCommandType(Integer commandType) {
		this.commandType = commandType;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", commandId=").append(commandId);
        sb.append(", appId=").append(appId);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", serviceId=").append(serviceId);
        sb.append(", methodId=").append(methodId);
        sb.append(", methodParams=").append(methodParams);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", platformIssuedTime=").append(platformIssuedTime);
        sb.append(", issuedTimes=").append(issuedTimes);
        sb.append(", createTime=").append(createTime);
        sb.append(", commandType=").append(commandType);
        sb.append("]");
        return sb.toString();
    }
}