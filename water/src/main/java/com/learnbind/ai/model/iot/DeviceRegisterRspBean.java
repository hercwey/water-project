package com.learnbind.ai.model.iot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class DeviceRegisterRspBean {

    /**
     *{deviceId":"9f1b9555-4320-419a-863b-9d577d5e8dcc","verifyCode":"106f0a4d-4ba9-408a-b5a1-40bb4320f903","timeout":0,"psk":"09215f58576f4403dc44265080d843b9"}
     */

    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("verifyCode")
    private String verifyCode;
    @JsonProperty("timeout")
    private String timeout;
    @JsonProperty("psk")
    private String psk;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_desc")
    private String errorDesc;

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

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getPsk() {
        return psk;
    }

    public void setPsk(String psk) {
        this.psk = psk;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public static DeviceRegisterRspBean parseJson(String data) {
        DeviceRegisterRspBean deviceBean = new DeviceRegisterRspBean();

        try {
            ObjectMapper mapper = new ObjectMapper();
            deviceBean = mapper.readValue(data, DeviceRegisterRspBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deviceBean;
    }
}
