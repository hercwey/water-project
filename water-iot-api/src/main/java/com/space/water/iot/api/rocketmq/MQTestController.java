package com.space.water.iot.api.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.space.water.iot.api.config.MQTags;
import com.space.water.iot.api.test.Tester;

@RestController
public class MQTestController {

	@Autowired
	private Producer producer;
	/**
	 * 初始化消息
	 */
	public MQTestController() {

	}

	@RequestMapping("/test/rocketmq")
	public Object callback(@RequestBody String data) throws Exception {
		JSONObject jsonObject = (JSONObject) JSONObject.parse(data);
		/**
		String topic = jsonObject.getString("topic");
		String tag = jsonObject.getString("tag");
		String key = jsonObject.getString("key");
		String dataStr = jsonObject.getString("data");

		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| tag  :" + tag);
		System.out.println("| key  :" + key);
		System.out.println("| data :" + dataStr);
		System.out.println("--------------------------------------------------------------------------------------");

		// 创建生产信息
		Message message = new Message(topic, tag, key, dataStr.getBytes());
		*/
		//自动生成模拟数据
		String tag = jsonObject.getString("tag");
		String messageData = "";
		switch (tag) {
		case MQTags.AUTO_REPORT:
			messageData = Tester.autoReport();
			break;
		case MQTags.CONFIG_PARAMS_SOUTH:
			messageData = Tester.configParamsSouth();
			break;
		case MQTags.CONFIG_PARAMS_NORTH:
			messageData = Tester.configParamsNorth();
			break;
		case MQTags.CONFIG_THRESHOLD_SOUTH:
			messageData = Tester.configThresholdSouth();
			break;
		case MQTags.CONFIG_THRESHOLD_NORTH:
			messageData = Tester.configThresholdNorth();
			break;
		case MQTags.CONTROL_VALVE_SOUTH:
			messageData = Tester.controlValveSouth();
			break;
		case MQTags.CONTROL_VALVE_NORTH:
			messageData = Tester.controlValveNorth();
			break;
		case MQTags.DEVICE_REGISTER_SOUTH:
			messageData = Tester.deviceRegisterSouth();
			break;
		case MQTags.DEVICE_UPDATE_SOUTH:
			messageData = Tester.deviceUpdateSouth();
			break;
		case MQTags.DEVICE_DELETE_SOUTH:
			messageData = Tester.deviceDeleteSouth();
			break;
		case MQTags.DEVICE_QUERY_SOUTH:
			messageData = Tester.deviceQuery();
			break;
		case MQTags.QUERY_MONTH_DATA_SOUTH:
			messageData = Tester.queryMonthDataSouth();
			break;
		case MQTags.QUERY_MONTH_DATA_NORTH:
			messageData = Tester.queryMonthDataNorth();
			break;
		case MQTags.QUERY_PARAMS_SOUTH:
			messageData = Tester.queryParamsSouth();
			break;
		case MQTags.QUERY_PARAMS_NORTH:
			messageData = Tester.queryParamsNorth();
			break;
		default:
			break;
		}
		
		// 发送
		SendResult sendResult = producer.getProducer().send(Tester.packMQMessage(tag, messageData));
		System.out.println("输出生产者信息={}" + sendResult);
		return "成功";
	}
}
