package com.learnbind.ai.mq.north;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.mq.MQConstant;
import com.learnbind.ai.mq.north.service.AutoReportDataProcessService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: AutoReportConsumer.java
 * @Description: 消费者-设备自动上报数据
 *
 * @author SRD
 * @date 2020年2月22日 上午10:57:23
 * @version V1.0
 *
 */
@Component
public class AutoReportConsumer extends BaseConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AutoReportConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;
	@Autowired
	private AutoReportDataProcessService autoReportDataProcessService;

	/**
	 * 通过构造函数 实例化对象
	 */
	public AutoReportConsumer() {
		
	}
	
	@Override
	public void initParams() {
		this.consumerGroup = MQConstant.C_G_NORTH_AUTO_REPORT;// 消费者分组
		this.topicName = rocketTopicConfig.getTopicName();// 主题名称
		this.tag = rocketTopicConfig.getTagAutoReport();// tag
	}
	@Override
	public void processMessage(String msg) {
		AutoReport reportData = AutoReport.fromJson(msg);//把接收到的数据转成对象
		autoReportDataProcessService.processAutoReportData(reportData);//处理接收到的数据
	}
	
	public static AutoReport getTest() {
		String test = "{\r\n" + 
				" \"checksum\": -97,\r\n" + 
				" \"ctrlCode\": \"81\",\r\n" + 
				" \"data\": \"{\\\"batteryVoltage\\\":3219,\\\"meterNumber\\\":\\\"434045073936\\\",\\\"meterStatus\\\":{\\\"batteryLow\\\":0,\\\"magneticAlarmOn\\\":0,\\\"magneticOn\\\":0,\\\"maxReportOn\\\":0,\\\"periodOn\\\":1,\\\"sampleLineCut\\\":0,\\\"valveAbnormal\\\":0,\\\"valveOpen\\\":1},\\\"meterTime\\\":1581374157000,\\\"pressure\\\":\\\"0.0\\\",\\\"sampleUnit\\\":\\\"0.1\\\",\\\"signal\\\":\\\"13\\\",\\\"totalVolume\\\":34}\",\r\n" + 
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
				"  \"totalVolume\": 34\r\n" + 
				" },\r\n" + 
				" \"sequence\": 176,\r\n" + 
				" \"serviceId\": \"JRprotocol\",\r\n" + 
				" \"serviceType\": \"JRprotocol\"\r\n" + 
				"}";
		AutoReport report = AutoReport.fromJson(test);
		System.out.println(report.toString());
		System.out.println(report.getReportData().toString());
		return report;
	}
	public static void main(String[] args) {
		//test();
	}

}
