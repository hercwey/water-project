package com.learnbind.ai.service.iot.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.iot.Constants;
import com.learnbind.ai.iot.util.IoTRequestUtil;
import com.learnbind.ai.iot.util.JsonUtil;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.IAuthService;
import com.learnbind.ai.service.iot.ISubscribeService;

@Service
public class AuthService implements IAuthService {
	
	@Autowired
	private ISubscribeService iSubscribeService;
	
    @Override
    public JsonResult login() {
        String accessToken = null;
        JsonResult jsonResult = JsonResult.fail(0,"Unknown Error");
        try {
            String appId = Constants.APP_ID;
            String secret = Constants.SECRET;
            String urlLogin = Constants.APP_AUTH;

            Map<String, String> param = new HashMap<>();
            param.put("appId", appId);
            param.put("secret", secret);

            jsonResult = IoTRequestUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, param);

            Map<String, String> dataMap = new HashMap<>();
            dataMap = JsonUtil.jsonString2SimpleObj(jsonResult.getData(), dataMap.getClass());
            accessToken = dataMap.get("accessToken");
            System.out.println("accessToken:" + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Constants.accessToken = accessToken;
        //TODO G11 登录成功后自动订阅设备消息
        if (accessToken != null) {
//            ISubscribeService subscribeService = new SubscribeService();
//            subscribeService.subscribeDeviceData();
            iSubscribeService.subscribeDeviceData();
        }
        return jsonResult;
    }

    @Override
    public JsonResult refreshToken() {
        String accessToken = null;
        JsonResult jsonResult = JsonResult.fail(0,"Unknown Error");
        try {
            String appId = Constants.APP_ID;
            String secret = Constants.SECRET;
            String urlLogin = Constants.APP_AUTH;

            Map<String, String> paramLogin = new HashMap<>();
            paramLogin.put("appId", appId);
            paramLogin.put("secret", secret);

            jsonResult = IoTRequestUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

            Map<String, String> data = new HashMap<>();
            data = JsonUtil.jsonString2SimpleObj(jsonResult.getData(), data.getClass());
            accessToken = data.get("refreshToken");

            String urlRefreshToken = Constants.REFRESH_TOKEN;

            Map<String, Object> param_reg = new HashMap<>();
            param_reg.put("appId", appId);
            param_reg.put("secret", secret);
            param_reg.put("refreshToken", accessToken);

            String response = IoTRequestUtil.doPostJsonGetStatusLine(urlRefreshToken, param_reg).getData();
            System.out.println("============"+response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Constants.accessToken = accessToken;
        //TODO G11 刷新token成功后自动订阅设备消息
        if (accessToken != null) {
//            ISubscribeService subscribeService = new SubscribeService();
//            subscribeService.subscribeDeviceData();
        	iSubscribeService.subscribeDeviceData();
        }
        return jsonResult;
    }
}
