package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.model.command.QueryParamsRequest;
import com.space.water.iot.api.protocol.CommandGenerator;
import com.space.water.iot.api.protocol.bean.MeterConfigReadCmd;
import com.space.water.iot.api.rocketmq.MQConstant;

@Component
public class QueryParamsConsumer extends BaseConsumer {

	public QueryParamsConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			QueryParamsRequest request = QueryParamsRequest.fromJson(requestJson);
			// TODO G11 生成HEX设备指令
			MeterConfigReadCmd cmd = new MeterConfigReadCmd();

			String cmdHexStr = CommandGenerator.generateCmd(request.getMeterType(), request.getMeterAddress(),
					request.getMeterFactoryCode(), request.getSequence(), cmd);

			cacheCommand(request, cmdHexStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_QUERY_PARMS;
		tag = topicConfig.getTagQueryParmsSouth();
	}
}
