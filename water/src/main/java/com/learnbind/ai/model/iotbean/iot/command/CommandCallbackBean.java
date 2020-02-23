package com.learnbind.ai.model.iotbean.iot.command;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandCallbackBean {
    /**
     * {
     *  "deviceId":"106f0a4d-4ba9-408a-b5a1-40bb4320f903",
     *  "commandId":"9c6a9097064a4d7b8a4ba816e22913cd",
     *  "result":{
     *      "resultCode":"FAILED",
     *      "resultDetail":{
     *          "reason":"send command failed"
     *          }
     *      }
     *  }
     */

    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("commandId")
    private String commandId;
    @JsonProperty("result")
    private JSONObject result;
    @JsonProperty("resultCode")
    private String resultCode;
    @JsonProperty("resultDetail")
    private JSONObject resultDetail;
    @JsonProperty("reason")
    private String reason;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public JSONObject getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(JSONObject resultDetail) {
        this.resultDetail = resultDetail;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static CommandCallbackBean parseJson(String data) {
        CommandCallbackBean commandResultBean = new CommandCallbackBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            commandResultBean = mapper.readValue(data, CommandCallbackBean.class);
            if (commandResultBean.getResult() != null && commandResultBean.getResult().containsKey("resultCode")) {
                commandResultBean.setResultCode(commandResultBean.getResult().getString("resultCode"));
                commandResultBean.setResultDetail(commandResultBean.getResult().getJSONObject("resultDetail"));
                if (commandResultBean.getResultDetail() != null) {
                    commandResultBean.setReason(commandResultBean.getResultDetail().getString("reason"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commandResultBean;
    }
}
