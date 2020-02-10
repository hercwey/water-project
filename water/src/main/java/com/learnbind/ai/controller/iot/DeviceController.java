package com.learnbind.ai.controller.iot;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.DeviceRegisterRspBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.IDeviceService;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    IDeviceService deviceService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody String data) {
        //TODO 注册设备
        DeviceBean deviceBean = DeviceBean.parseJson(data);

        //TODO 调用IoT平台，注册设备接口
        JsonResult registerResult = deviceService.registerDevice(deviceBean);
        String registerResponse = registerResult.getData();

        if (StringUtils.isNotBlank(registerResponse) && !registerResult.getData().contains("error_code")) {
        	DeviceRegisterRspBean registerRspBean = DeviceRegisterRspBean.parseJson(registerResponse);
            deviceBean.setDeviceId(registerRspBean.getDeviceId());
            JsonResult modifyResult = deviceService.modifyDevice(deviceBean);
        }

        return ResponseEntity.ok(registerResult.toString());
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseEntity<String> modify(@RequestBody String data) {
        //TODO 修改设备
        DeviceBean deviceBean = DeviceBean.parseJson(data);
        //TODO 调用IoT平台，修改设备接口
        JsonResult jsonResult = deviceService.modifyDevice(deviceBean);
        return ResponseEntity.ok(jsonResult.toString());
    }
    
    @RequestMapping(value = "/delete/{deviceId}", method = RequestMethod.GET)
    public ResponseEntity<String> delete(@PathVariable String deviceId) {
        //TODO 调用IoT平台，删除设备接口
        JsonResult jsonResult = deviceService.deleteFromIoT(deviceId);
        return ResponseEntity.ok(jsonResult.toString());
    }

    @RequestMapping(value = "/queryDevices/{page}/{size}",method = RequestMethod.GET)
    public ResponseEntity<String> queryDevices(@PathVariable String page,@PathVariable String size) {
        JsonResult jsonResult = deviceService.queryDevices(page,size);
        return ResponseEntity.ok(jsonResult.toString());
    }
}
