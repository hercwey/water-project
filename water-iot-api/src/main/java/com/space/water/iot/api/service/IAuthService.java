package com.space.water.iot.api.service;

import com.space.water.iot.api.common.JsonResult;

public interface IAuthService {
	public JsonResult login();

	public JsonResult refreshToken();
}
