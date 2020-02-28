package com.space.water.iot.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.service.ISubscribeService;
import com.space.water.iot.api.util.LogUtil;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Autowired
    ISubscribeService subscribeService;

    @RequestMapping(value = "/deviceData", method = RequestMethod.POST)
    public ResponseEntity<String> subscribeDeviceData() {
        JsonResult jsonResult = subscribeService.subscribeDeviceData();
    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 设备数据上报地址订阅接口");
    	LogUtil.info("-----------------------------");
        return ResponseEntity.ok(jsonResult.toString());
    }
}
