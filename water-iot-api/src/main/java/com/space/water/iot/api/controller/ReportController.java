package com.space.water.iot.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.report.BaseReportData;
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

	@RequestMapping(value = "/uploadDeviceData", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> uploadDeviceData(@RequestBody String data) {
    	System.out.println("-----------------------------");
    	System.out.println("| 设备数据上报接口");
    	System.out.println("| data:" + data);
    	System.out.println("-----------------------------");
		// TODO 数据解析
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(data);
		// TODO 数据处理（逻辑待优化）
		JsonResult jsonResult = reportService.process(meterBean);// JsonResult.success(JsonResult.SUCCESS, data);

		jsonResult.setData(data);
		return ResponseEntity.ok(jsonResult.toString());
	}
}
