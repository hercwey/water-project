package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.model.command.ConfigParamsRequest;
import com.space.water.iot.api.model.common.DeviceParams;
import com.space.water.iot.api.model.common.DeviceParamsFlags;
import com.space.water.iot.api.protocol.CommandGenerator;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteCmd;
import com.space.water.iot.api.rocketmq.MQConstant;

@Component
public class ConfigParamsConsumer extends BaseConsumer {

	public ConfigParamsConsumer() throws MQClientException {
		super();
	}

	@Override
	void processMessage(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			ConfigParamsRequest request = ConfigParamsRequest.fromJson(requestJson);
			// TODO G11 生成HEX设备指令
			MeterConfigWriteCmd cmd = new MeterConfigWriteCmd();
			cmd.setConfigFlag(DeviceParamsFlags.toShort(request.getDeviceParams().getConfigFlag()));
			cmd.setConfig(DeviceParams.toMeterConfig(request.getDeviceParams()));
			String cmdHexStr = CommandGenerator.generateCmd(request.getMeterType(), request.getMeterAddress(),
					request.getMeterFactoryCode(), request.getSequence(), cmd);

			cacheCommand(request, cmdHexStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initParams() {
		group = MQConstant.P_G_SORTH_CONFIG_PARMS;
		tag = topicConfig.getTagConfigParmsSouth();
	}
}
