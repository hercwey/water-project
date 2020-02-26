package com.learnbind.ai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.mq.north.service.AutoReportDataProcessService;

@Controller
@RequestMapping(value = "/test/process")
public class Test {

	
	private static final Logger log = LoggerFactory.getLogger(Test.class);

	
	@Autowired
	private AutoReportDataProcessService autoReportDataProcessService;
	
	static Integer totalVolume=1000;
	
	@RequestMapping(value = "/starter")
	@ResponseBody
	public Object process() {
		
		totalVolume = totalVolume+1;
		
		String test = "{\r\n" + 
				" \"checksum\": -97,\r\n" + 
				" \"ctrlCode\": \"81\",\r\n" + 
				" \"data\": \"{\\\"batteryVoltage\\\":3219,\\\"meterNumber\\\":\\\"434045073936\\\",\\\"meterStatus\\\":{\\\"batteryLow\\\":0,\\\"magneticAlarmOn\\\":0,\\\"magneticOn\\\":0,\\\"maxReportOn\\\":0,\\\"periodOn\\\":1,\\\"sampleLineCut\\\":0,\\\"valveAbnormal\\\":0,\\\"valveOpen\\\":1},\\\"meterTime\\\":1581374157000,\\\"pressure\\\":\\\"0.0\\\",\\\"sampleUnit\\\":\\\"0.1\\\",\\\"signal\\\":\\\"13\\\",\\\"totalVolume\\\":264}\",\r\n" + 
				" \"dataBasic\": \"681036390745404358811d1f90b0363907454043573522100102200e0000000019320900130000009f16\",\r\n" + 
				" \"dataDI\": -28641,\r\n" + 
				" \"dataType\": 1,\r\n" + 
				" \"deviceId\": \"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\",\r\n" + 
				" \"eventTime\": 1581374164000,\r\n" + 
				" \"factoryCode\": \"5843\",\r\n" + 
				" \"gatewayId\": \"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\",\r\n" + 
				" \"jsonData\": \"{\\\"notifyType\\\":\\\"deviceDatasChanged\\\",\\\"requestId\\\":null,\\\"deviceId\\\":\\\"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\\\",\\\"gatewayId\\\":\\\"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\\\",\\\"services\\\":[{\\\"serviceId\\\":\\\"JRprotocol\\\",\\\"serviceType\\\":\\\"JRprotocol\\\",\\\"data\\\":{\\\"JRprotocolXY\\\":\\\"681036390745404358811d1f90b0363907454043573522100102200e0000000019320900130000009f16\\\"},\\\"eventTime\\\":\\\"20200210T223604Z\\\"}]}\",\r\n" + 
				" \"meterAddr\": \"4045073936\",\r\n" + 
				" \"meterType\": 16,\r\n" + 
				" \"reportData\": {\r\n" + 
				"  \"batteryVoltage\": 3219,\r\n" + 
				"  \"meterNumber\": \"434045073936\",\r\n" + 
				"  \"meterStatus\": {\r\n" + 
				"   \"batteryLow\": 0,\r\n" + 
				"   \"magneticAlarmOn\": 0,\r\n" + 
				"   \"magneticOn\": 0,\r\n" + 
				"   \"maxReportOn\": 0,\r\n" + 
				"   \"periodOn\": 1,\r\n" + 
				"   \"sampleLineCut\": 0,\r\n" + 
				"   \"valveAbnormal\": 0,\r\n" + 
				"   \"valveOpen\": 1\r\n" + 
				"  },\r\n" + 
				"  \"meterTime\": 1581374157000,\r\n" + 
				"  \"pressure\": \"0.0\",\r\n" + 
				"  \"sampleUnit\": \"0.1\",\r\n" + 
				"  \"signal\": \"13\",\r\n" + 
				"  \"totalVolume\": "+totalVolume+"\r\n" + 
				" },\r\n" + 
				" \"sequence\": 176,\r\n" + 
				" \"serviceId\": \"JRprotocol\",\r\n" + 
				" \"serviceType\": \"JRprotocol\"\r\n" + 
				"}";
		
		log.info("----------JSON字符串转对象");
		Long date1 = System.currentTimeMillis();
		AutoReport report = AutoReport.fromJson(test);
		Long date2 = System.currentTimeMillis();
		log.info("----------JSON字符串转对象用时："+(date2-date1));
		System.out.println(report.toString());
		System.out.println(report.getReportData().toString());
		
		//autoReportDataProcessService.processAutoReportData(report);//处理接收到的数据
		
		return RequestResultUtil.getResultSuccess("成功");
		
	}
	
}
