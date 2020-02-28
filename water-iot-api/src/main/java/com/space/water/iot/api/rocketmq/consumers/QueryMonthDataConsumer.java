package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.model.command.QueryMonthDataRequest;
import com.space.water.iot.api.model.command.QueryParamsRequest;
import com.space.water.iot.api.protocol.CommandGenerator;
import com.space.water.iot.api.protocol.bean.MeterConfigReadCmd;
import com.space.water.iot.api.protocol.bean.MeterReadWaterCmd;
import com.space.water.iot.api.rocketmq.MQConstant;

@Component
public class QueryMonthDataConsumer extends BaseConsumer {

	public QueryMonthDataConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			QueryMonthDataRequest request = QueryMonthDataRequest.fromJson(requestJson);
			// TODO G11 生成HEX设备指令
			MeterReadWaterCmd cmd = new MeterReadWaterCmd();

			String cmdHexStr = CommandGenerator.generateCmd(request.getMeterType(), request.getMeterAddress(),
					request.getMeterFactoryCode(), request.getSequence(), cmd);

			cacheCommand(request, cmdHexStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_QUERY_MONTH_DATA;
		tag = topicConfig.getTagQueryMonthDataSouth();
	}
}
