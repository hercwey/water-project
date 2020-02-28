package com.space.water.iot.api.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.config.Constants;

public class IoTRequestUtil {

    public static JsonResult doPostJson(String url, Map<String, Object> data) throws Exception{

        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        String accessToken = Constants.accessToken;
        String appId = Constants.APP_ID;

        Map<String, String> header = new HashMap<>();
        header.put(Constants.HEADER_APP_KEY, appId);
        header.put(Constants.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        String jsonRequest = JsonUtil.jsonObj2String(data);
        HttpResponse response = httpsUtil.doPostJson(url, header, jsonRequest);

        JsonResult jsonResult = getJsonResult(response,httpsUtil);
        return jsonResult;
    }

    public static JsonResult doPutJsonGetStatusLine(String url,Map<String, Object> data) throws Exception{

        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        String accessToken = Constants.accessToken;
        String appId = Constants.APP_ID;

        Map<String, String> header = new HashMap<>();
        header.put(Constants.HEADER_APP_KEY, appId);
        header.put(Constants.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        String jsonRequest = JsonUtil.jsonObj2String(data);
        StreamClosedHttpResponse response = httpsUtil.doPutJsonGetStatusLine(url, header, jsonRequest);

        JsonResult jsonResult = getJsonResult(response,httpsUtil);
        return jsonResult;
    }

    public static JsonResult doPostJsonGetStatusLine(String url,Map<String, Object> data) throws Exception{

        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        String accessToken = Constants.accessToken;
        String appId = Constants.APP_ID;

        Map<String, String> header = new HashMap<>();
        header.put(Constants.HEADER_APP_KEY, appId);
        header.put(Constants.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        String jsonRequest = JsonUtil.jsonObj2String(data);
        StreamClosedHttpResponse response = httpsUtil.doPostJsonGetStatusLine(url, header, jsonRequest);
        JsonResult jsonResult = getJsonResult(response,httpsUtil);
        return jsonResult;
    }

    public static JsonResult doPostFormUrlEncodedGetStatusLine(String url, Map<String, String> data) throws Exception{
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        StreamClosedHttpResponse response = httpsUtil.doPostFormUrlEncodedGetStatusLine(url, data);
        JsonResult jsonResult = getJsonResult(response,httpsUtil);
        return jsonResult;
    }

    public static JsonResult doGetWithParasGetStatusLine(String url,Map<String, String> queryParams) throws Exception {
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        String accessToken = Constants.accessToken;
        String appId = Constants.APP_ID;

        Map<String, String> header = new HashMap<>();
        header.put(Constants.HEADER_APP_KEY, appId);
        header.put(Constants.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        HttpResponse response = httpsUtil.doGetWithParas(url, queryParams, header);
        if (null == response) {
            LogUtil.debug("The response body is null.");
        }
        JsonResult jsonResult = getJsonResult(response,httpsUtil);
        return jsonResult;
    }
    public static JsonResult doDeleteGetStatusLine(String url) throws Exception {
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();

        String accessToken = Constants.accessToken;
        String appId = Constants.APP_ID;

        Map<String, String> header = new HashMap<>();
        header.put(Constants.HEADER_APP_KEY, appId);
        header.put(Constants.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        HttpResponse response = httpsUtil.doDelete(url, header);
        if (null == response) {
            LogUtil.debug("The response body is null.");
        }
        JsonResult jsonResult = getJsonResult(response,httpsUtil);
        return jsonResult;
    }

    private static JsonResult getJsonResult(HttpResponse response,HttpsUtil httpsUtil) {
        JsonResult jsonResult = JsonResult.fail(0,"Unknown Error");
        /**
         * 1**请求未成功
         * 2**请求成功、表示成功处理了请求的状态代码。
         * 3**请求被重定向、表示要完成请求，需要进一步操作。 通常，这些状态代码用来重定向。
         * 4** 请求错误这些状态代码表示请求可能出错，妨碍了服务器的处理。
         * 5**（服务器错误）这些状态代码表示服务器在尝试处理请求时发生内部错误。 这些错误可能是服务器本身的错误，而不是请求出错。
         */
        LogUtil.debug("----------------------------------------");
        LogUtil.debug("| IoT Platform Request, response content:");
        try {
            if (response != null) {

                LogUtil.debug("| Status   Code:" + response.getStatusLine());

                int statusCode = response.getStatusLine().getStatusCode();
                int statusType = statusCode/100;
                String responseBody = "";
                if (response instanceof StreamClosedHttpResponse) {
                    responseBody = ((StreamClosedHttpResponse) response).getContent();
                } else {
                    responseBody = httpsUtil.getHttpResponseBody(response);
                }

                LogUtil.debug("| Response Body:"+responseBody);

                /**
                 * 请求失败消息，需要重新登录
                 * {
                 *     "resultcode":"1010005",
                 *     "resultmsg":"Invalid access token or appId."
                 * }
                 */
                if (responseBody == null) {
                    responseBody = "";
                }

                if (responseBody.contains("resultcode") && responseBody.contains("1010005")) {
                    //FIXME G11 重新登录获取token后，重新执行订阅操作（需要整理成统一方法来处理）
                }

                switch (statusType) {
                    case 2: {
                        jsonResult = JsonResult.success(statusCode,responseBody);
                        break;
                    }
                    case 1: {
                    }
                    case 3: {
                    }
                    case 4: {
                    }
                    case 5: {
                    }
                    default: {
                        jsonResult = JsonResult.fail(statusCode,responseBody);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = JsonResult.fail(0,"Error:" + e.getMessage());
        }

        LogUtil.debug("----------------------------------------\n");
        return jsonResult;
    }

}