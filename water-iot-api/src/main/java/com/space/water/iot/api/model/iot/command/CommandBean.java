package com.space.water.iot.api.model.iot.command;

import java.io.IOException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.water.iot.api.util.LogUtil;

public class CommandBean {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("commandId")
    private String commandId;
    @JsonProperty("appId")
    private String appId;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("method")
    private String method;
    @JsonProperty("paras")
    private JSONObject paras;
    @JsonProperty("methodParams")
    private String methodParams;
    @JsonProperty("operatorId")
    private String operatorId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("databaseStatus")
    private Integer databaseStatus;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("expireTime")
    private Long expireTime;
    @JsonProperty("platformIssuedTime")
    private String platformIssuedTime;
    @JsonProperty("issuedTimes")
    private int issuedTimes;
    @JsonProperty("createTime")
    private Date createTime;

    @JsonProperty("callbackUrl")
    private String callbackUrl;
    @JsonProperty("command")
    private JSONObject command;
    @JsonProperty("creationTime")
    private String creationTime;
    
    @JsonProperty("commandType")
    private Integer commandType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getParas() {
        return paras;
    }

    public void setParas(JSONObject paras) {
        this.paras = paras;
    }

    public String getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDatabaseStatus() {
        return databaseStatus;
    }

    public void setDatabaseStatus(Integer databaseStatus) {
        this.databaseStatus = databaseStatus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getPlatformIssuedTime() {
        return platformIssuedTime;
    }

    public void setPlatformIssuedTime(String platformIssuedTime) {
        this.platformIssuedTime = platformIssuedTime;
    }

    public int getIssuedTimes() {
        return issuedTimes;
    }

    public void setIssuedTimes(int issuedTimes) {
        this.issuedTimes = issuedTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public JSONObject getCommand() {
        return command;
    }

    public void setCommand(JSONObject command) {
        this.command = command;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getCommandType() {
		return commandType;
	}

	public void setCommandType(Integer commandType) {
		this.commandType = commandType;
	}

	public static CommandBean parseJson(String data) {
        CommandBean commandBean = new CommandBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            commandBean = mapper.readValue(data, CommandBean.class);
            if (commandBean.getParas() != null) {
                commandBean.setMethodParams(commandBean.getParas().toJSONString());
            }
        } catch (IOException e) {
            LogUtil.error("\n---------------------------");
            LogUtil.error("| CommandBean数据解析失败："+e.getMessage());
            LogUtil.error("---------------------------\n");
        }

        return commandBean;
    }
}
