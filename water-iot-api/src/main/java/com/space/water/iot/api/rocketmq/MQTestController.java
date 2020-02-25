package com.space.water.iot.api.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

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
		// 发送
		SendResult sendResult = producer.getProducer().send(message);
		System.out.println("输出生产者信息={}" + sendResult);
		return "成功";
	}
}
