package com.learnbind.ai.controller.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.IAuthService;
import com.learnbind.ai.service.iot.ISubscribeService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    IAuthService authService;
    @Autowired
    ISubscribeService subscribeService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login() {
        JsonResult jsonResult = authService.login();
        return ResponseEntity.ok(jsonResult.toString());
    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public ResponseEntity<String> refreshToken() {
        JsonResult jsonResult = authService.refreshToken();
        return ResponseEntity.ok(jsonResult.toString());
    }
}
