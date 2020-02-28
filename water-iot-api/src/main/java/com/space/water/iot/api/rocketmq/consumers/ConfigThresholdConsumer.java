package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.model.command.ConfigThresholdRequest;
import com.space.water.iot.api.protocol.CommandGenerator;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdCmd;
import com.space.water.iot.api.rocketmq.MQConstant;

@Component
public class ConfigThresholdConsumer extends BaseConsumer {

	public ConfigThresholdConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			ConfigThresholdRequest request = ConfigThresholdRequest.fromJson(requestJson);
			// TODO G11 生成HEX设备指令
			MeterVolumeThresholdCmd cmd = new MeterVolumeThresholdCmd();
			cmd.setThreshold(request.getThreshold());

			String cmdHexStr = CommandGenerator.generateCmd(request.getMeterType(), request.getMeterAddress(),
					request.getMeterFactoryCode(), request.getSequence(), cmd);

			cacheCommand(request, cmdHexStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_CONFIG_THRESHOLD;
		tag = topicConfig.getTagConfigThresholdSouth();
	}
}
