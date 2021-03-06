package com.space.water.iot.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.service.IAuthService;
import com.space.water.iot.api.service.ISubscribeService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    IAuthService authService;
    @Autowired
    ISubscribeService subscribeService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login() {

    	System.out.println("-----------------------------");
    	System.out.println("| 电信平台登录接口");
    	System.out.println("-----------------------------");
    	
        JsonResult jsonResult = authService.login();
        return ResponseEntity.ok(jsonResult.toString());
    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public ResponseEntity<String> refreshToken() {
    	System.out.println("-----------------------------");
    	System.out.println("| 电信平台刷新token接口");
    	System.out.println("-----------------------------");
        //FIXME G11 调用此接口后token失效（暂时替换成调用登录接口）
//      JsonResult jsonResult = authService.refreshToken();
    	JsonResult jsonResult = authService.login();
        return ResponseEntity.ok(jsonResult.toString());
    }
}
