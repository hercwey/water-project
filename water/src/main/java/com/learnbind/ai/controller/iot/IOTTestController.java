package com.learnbind.ai.controller.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.service.iot.IAuthService;
import com.learnbind.ai.service.iot.ISubscribeService;

@Controller
@RequestMapping("/iot")
public class IOTTestController {
    @Autowired
    IAuthService authService;
    @Autowired
    ISubscribeService subscribeService;

    @RequestMapping(value = "/starter")
    public String starter() {
        return "iot/starter";
    }
    
    @RequestMapping(value = "/test")
    public String test() {
        return "iot/test";
    }

}
