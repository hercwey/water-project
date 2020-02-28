package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.device.UpdateDeviceRequest;
import com.space.water.iot.api.rocketmq.MQConstant;
import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.service.IDeviceService;

@Component
public class DeviceUpdateConsumer extends BaseConsumer {
	@Autowired
	Producer producer;
	@Autowired
	IDeviceService deviceService;

	public DeviceUpdateConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			UpdateDeviceRequest request = UpdateDeviceRequest.fromJson(requestJson);
			JsonResult result = deviceService.modifyDevice(request);
			producer.sendNorth(result.getData(), topicConfig.getTagDeviceUpdateNorth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_DEVICE_UPDATE;
		tag = topicConfig.getTagDeviceUpdateSouth();
	}
}
