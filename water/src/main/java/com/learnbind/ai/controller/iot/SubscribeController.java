package com.learnbind.ai.controller.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.ISubscribeService;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Autowired
    ISubscribeService subscribeService;

    @RequestMapping(value = "/deviceData", method = RequestMethod.POST)
    public ResponseEntity<String> subscribeDeviceData() {
        JsonResult jsonResult = subscribeService.subscribeDeviceData();
        System.out.println("设备数据变动消息订阅成功");
        return ResponseEntity.ok(jsonResult.toString());
    }
}
