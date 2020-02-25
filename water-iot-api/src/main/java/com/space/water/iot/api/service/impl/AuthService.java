package com.space.water.iot.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.config.Constants;
import com.space.water.iot.api.model.iot.auth.LoginResp;
import com.space.water.iot.api.service.IAuthService;
import com.space.water.iot.api.service.ISubscribeService;
import com.space.water.iot.api.util.IoTRequestUtil;
import com.space.water.iot.api.util.JsonUtil;

@Service
public class AuthService implements IAuthService {

	@Override
	public JsonResult login() {
		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");
		try {
			String appId = Constants.APP_ID;
			String secret = Constants.SECRET;
			String urlLogin = Constants.APP_AUTH;

			Map<String, String> param = new HashMap<>();
			param.put("appId", appId);
			param.put("secret", secret);

			jsonResult = IoTRequestUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, param);

			LoginResp response = LoginResp.fromJson(jsonResult.getData());
			Constants.accessToken = response.getAccessToken();

			System.out.println("accessToken:" + Constants.accessToken);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Override
	public JsonResult refreshToken() {
		String accessToken = null;
		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");
		try {
			String appId = Constants.APP_ID;
			String secret = Constants.SECRET;
			String urlLogin = Constants.APP_AUTH;

			Map<String, String> paramLogin = new HashMap<>();
			paramLogin.put("appId", appId);
			paramLogin.put("secret", secret);

			jsonResult = IoTRequestUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);

			LoginResp loginResp = LoginResp.fromJson(jsonResult.getData());
			accessToken = loginResp.getRefreshToken();

			// FIXME G11 待整理
			String urlRefreshToken = Constants.REFRESH_TOKEN;

			Map<String, Object> param_reg = new HashMap<>();
			param_reg.put("appId", appId);
			param_reg.put("secret", secret);
			param_reg.put("refreshToken", accessToken);

			String response = IoTRequestUtil.doPostJsonGetStatusLine(urlRefreshToken, param_reg).getData();
			System.out.println("============" + response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Constants.accessToken = accessToken;
		return jsonResult;
	}
}
