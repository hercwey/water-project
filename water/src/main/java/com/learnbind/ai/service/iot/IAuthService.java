package com.learnbind.ai.service.iot;

import com.learnbind.ai.model.iot.JsonResult;

public interface IAuthService {
    public JsonResult login();
    public JsonResult refreshToken();
    
    public JsonResult taskRefreshToken();
    
}
