package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.device.DeleteDeviceRequest;
import com.space.water.iot.api.rocketmq.MQConstant;
import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.service.IDeviceService;

@Component
public class DeviceQueryConsumer extends BaseConsumer {
	@Autowired
	Producer producer;
	@Autowired
	IDeviceService deviceService;

	public DeviceQueryConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			JsonResult result = deviceService.queryDevices("0", "10000");
			producer.sendNorth(result.getData(), topicConfig.getTagDeviceQueryNorth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_DEVICE_QUERY;
		tag = topicConfig.getTagDeviceQuerySouth();
	}
}
