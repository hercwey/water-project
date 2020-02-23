package com.learnbind.ai.model.iot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestUploadDataBean {
    @JsonProperty("JRprotocolXY")
    private String JRprotocolXY;

    public String getJRprotocolXY() {
        return JRprotocolXY;
    }

    public void setJRprotocolXY(String JRprotocolXY) {
        this.JRprotocolXY = JRprotocolXY;
    }
}
