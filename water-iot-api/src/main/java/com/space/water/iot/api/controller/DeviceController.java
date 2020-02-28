package com.space.water.iot.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.device.RegisterDeviceRequest;
import com.space.water.iot.api.model.device.UpdateDeviceRequest;
import com.space.water.iot.api.service.IDeviceService;
import com.space.water.iot.api.util.LogUtil;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    IDeviceService deviceService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody String data) {

    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 设备注册接口");
    	LogUtil.info("| data:" + data);
    	LogUtil.info("-----------------------------");
    	
        //TODO 注册设备
        RegisterDeviceRequest registerReq = RegisterDeviceRequest.fromJson(data);

        //TODO 调用IoT平台，注册设备接口
        JsonResult registerResult = deviceService.registerDevice(registerReq);

        return ResponseEntity.ok(registerResult.toString());
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseEntity<String> modify(@RequestBody String data) {
    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 修改设备信息接口");
    	LogUtil.info("| data:" + data);
    	LogUtil.info("-----------------------------");
        //TODO 修改设备
    	UpdateDeviceRequest modifyReq = UpdateDeviceRequest.fromJson(data);
        //TODO 调用IoT平台，修改设备接口
        JsonResult jsonResult = deviceService.modifyDevice(modifyReq);
        return ResponseEntity.ok(jsonResult.toString());
    }
    
    @RequestMapping(value = "/delete/{deviceId}", method = RequestMethod.GET)
    public ResponseEntity<String> delete(@PathVariable String deviceId) {
    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 删除设备接口");
    	LogUtil.info("| data:" + deviceId);
    	LogUtil.info("-----------------------------");
        //TODO 调用IoT平台，删除设备接口
        JsonResult jsonResult = deviceService.deleteFromIoT(deviceId);
        return ResponseEntity.ok(jsonResult.toString());
    }

    @RequestMapping(value = "/queryDevices/{page}/{size}",method = RequestMethod.GET)
    public ResponseEntity<String> queryDevices(@PathVariable String page,@PathVariable String size) {
    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 获取设备列表接口");
    	LogUtil.info("| data:" + page + "   " + size);
    	LogUtil.info("-----------------------------");
        JsonResult jsonResult = deviceService.queryDevices(page,size);
        return ResponseEntity.ok(jsonResult.toString());
    }
}
