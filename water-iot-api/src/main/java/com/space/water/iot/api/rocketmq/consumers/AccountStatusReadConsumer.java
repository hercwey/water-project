package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.model.command.AccountStatusReadRequest;
import com.space.water.iot.api.model.command.AccountStatusReadResponse;
import com.space.water.iot.api.model.command.ControlValveRequest;
import com.space.water.iot.api.protocol.CommandGenerator;
import com.space.water.iot.api.protocol.bean.MeterAccountReadCmd;
import com.space.water.iot.api.protocol.bean.MeterValveControlCmd;
import com.space.water.iot.api.rocketmq.MQConstant;

@Component
public class AccountStatusReadConsumer extends BaseConsumer {

	public AccountStatusReadConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			AccountStatusReadRequest request = AccountStatusReadRequest.fromJson(requestJson);
			// TODO G11 生成HEX设备指令
			MeterAccountReadCmd cmd = new MeterAccountReadCmd();

			String cmdHexStr = CommandGenerator.generateCmd(request.getMeterType(), request.getMeterAddress(),
					request.getMeterFactoryCode(), request.getSequence(), cmd);

			cacheCommand(request, cmdHexStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_ACCOUNT_STATUS_READ;
		tag = topicConfig.getTagAccountStatusReadSouth();
	}
}
