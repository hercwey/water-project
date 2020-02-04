package com.learnbind.ai.model.iot.deviceDetail;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceData {
    @JsonProperty("JRprotocolXY")
    private String JRprotocolXY;

    public String getJRprotocolXY() {
        return JRprotocolXY;
    }

    public void setJRprotocolXY(String JRprotocolXY) {
        this.JRprotocolXY = JRprotocolXY;
    }
}