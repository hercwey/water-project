package com.space.water.iot.api.controller;

import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.rocketmq.RocketTopicConfig;
import com.space.water.iot.api.service.ICommandService;
import com.space.water.iot.api.service.IDeviceService;
import com.space.water.iot.api.service.IReportService;

@Controller
@RequestMapping("/report")
@EnableAsync
public class ReportController {

	@Autowired
	IReportService reportService;
	@Autowired
	IDeviceService deviceService;
	@Autowired
	ICommandService commandService;
	@Autowired
	Producer producer;
    @Autowired
    RocketTopicConfig topicConfig;

	@RequestMapping(value = "/uploadDeviceData", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> uploadDeviceData(@RequestBody String data) {
		// 创建生产信息
		SendResult sendResult = producer.sendNorthAsync(data, topicConfig.getTagAutoReportCache());
		/**
		Message message = new Message("Topic1", "reportTest", "keys" , data.getBytes());
		try {
    		SendResult sendResult = producer.getProducer().send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		/**
		// TODO 数据解析
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(data);
		// TODO 数据处理（逻辑待优化）
		JsonResult jsonResult = reportService.process(meterBean);//JsonResult.success(JsonResult.SUCCESS, data);//

		jsonResult.setData(data);
		*/
		return ResponseEntity.ok(data);
	}
}
