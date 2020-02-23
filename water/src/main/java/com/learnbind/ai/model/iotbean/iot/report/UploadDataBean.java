package com.learnbind.ai.model.iotbean.iot.report;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadDataBean {
    @JsonProperty("JRprotocolXY")
    private String JRprotocolXY;

    public String getJRprotocolXY() {
        return JRprotocolXY;
    }

    public void setJRprotocolXY(String JRprotocolXY) {
        this.JRprotocolXY = JRprotocolXY;
    }
}
