package com.learnbind.ai.model.iot;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnbind.ai.iot.ErrorCodeEnum;

public class JsonResult {

    public static final int SUCCESS = 1;
    public static final int FAILED = 0;

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    public static final String STATUS_ERROR = "error";

    @JsonProperty("code")
    private int code = FAILED;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message = "";
    @JsonProperty("data")
    private String data = "";

    public JsonResult(int code, String msg) {
        this.code = code;
        this.status = code+"";
        this.message = msg;
    }

    public boolean isSuccess() {
        return this.getCode() == SUCCESS;
    }

    public static JsonResult success(int statusCode,String data) {
        JsonResult ret = new JsonResult(SUCCESS, "success");
        ret.setStatus(statusCode+"");
        ret.setData(data);
        return ret;
    }

    public static JsonResult fail(int code, String msg) {
        return new JsonResult(code, msg);
    }


    public static JsonResult fail(ErrorCodeEnum codeEnum) {
        return new JsonResult(codeEnum.getCode(), codeEnum.getMsg());
    }


    public static JsonResult fail(Map<String, Object> map) {
        JsonResult jsonResult = new JsonResult(ErrorCodeEnum.PARAM_ERROR.getCode(), ErrorCodeEnum.PARAM_ERROR.getMsg());

        JSONObject dataJson = new JSONObject(map);
        jsonResult.setData(dataJson.toJSONString());
        return jsonResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        //FIXME G11 临时方案，需要优化
        String result = "{}";
        result = "{\"code\":\"" + code + "\",\"status\":\"" + status + "\",\"message\":\"" + message + "\",\"data\":\"" + data.replace("\"","\\\"") + "\"}";
        return result;
    }
}
