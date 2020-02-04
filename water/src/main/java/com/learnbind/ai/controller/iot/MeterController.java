package com.learnbind.ai.controller.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.service.iot.IMeterService;

@RestController
@RequestMapping("/meter")
public class MeterController {

    @Autowired
    IMeterService meterService;

    @RequestMapping(value = "/uploadDeviceData", method = RequestMethod.POST)
    public ResponseEntity<String> uploadDeviceData(@RequestBody String data) {
        System.out.println("收到设备消息:"+data);
        //TODO 水表数据解析
        MeterBean meterBean = MeterBean.fromUploadDataJson(data);
        //TODO 水表数据保存
        JsonResult jsonResult = meterService.save(meterBean);
        jsonResult.setData(data);
        return ResponseEntity.ok(jsonResult.toString());
    }
}
