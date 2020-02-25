package com.space.water.iot.api.rocketmq;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.space.water.iot.api.command.CommandCache;
import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.command.BaseCommandRequest;
import com.space.water.iot.api.model.command.ConfigParamsRequest;
import com.space.water.iot.api.model.command.ConfigThresholdRequest;
import com.space.water.iot.api.model.command.ControlValveRequest;
import com.space.water.iot.api.model.command.QueryMonthDataRequest;
import com.space.water.iot.api.model.command.QueryParamsRequest;
import com.space.water.iot.api.model.common.DeviceParams;
import com.space.water.iot.api.model.common.DeviceParamsFlags;
import com.space.water.iot.api.model.device.DeleteDeviceRequest;
import com.space.water.iot.api.model.device.RegisterDeviceRequest;
import com.space.water.iot.api.model.device.UpdateDeviceRequest;
import com.space.water.iot.api.model.iot.command.CommandBean;
import com.space.water.iot.api.protocol.CommandGenerator;
import com.space.water.iot.api.protocol.bean.MeterConfigReadCmd;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteCmd;
import com.space.water.iot.api.protocol.bean.MeterReadWaterCmd;
import com.space.water.iot.api.protocol.bean.MeterValveControlCmd;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdCmd;
import com.space.water.iot.api.service.impl.DeviceService;

@Component
public class Consumer {

	@Autowired
	Producer producer;
	@Autowired
	RocketTopicConfig topicConfig;
	/**
	 * 消费者组
	 */
	public static final String CONSUMER_GROUP = "example_group_name";// "test_mq_consumer";

	/**
	 * 通过构造函数 实例化对象
	 */
	public Consumer() throws MQClientException {
		
	}
	
	public void initConsumer() {
		try {
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
			consumer.setNamesrvAddr(MQConstant.NAME_SERVER);
			// 消费模式:一个新的订阅组第一次启动从队列的最后位置开始消费 后续再启动接着上次消费的进度开始消费
			// consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			// set to broadcast mode
			consumer.setMessageModel(MessageModel.BROADCASTING);
			// 订阅主题和 标签（ * 代表所有标签)下信息
			consumer.subscribe("TOPIC", "*");
			// //注册消费的监听 并在此监听中消费信息，并返回消费的状态信息
			consumer.registerMessageListener(new MessageListenerConcurrently() {

				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					// msgs中只收集同一个topic，同一个tag，并且key相同的message
					// 会把不同的消息分别放置到不同的队列中
					try {
						for (Message msg : msgs) {
							String body = new String(msg.getBody(), "utf-8");
							System.out.println("-------------------------------------");
							System.out.println("| Consumer获取消息");
							System.out.println("| Time :" + new Date(System.currentTimeMillis()).toGMTString());
							System.out.println("| Topic:" + msg.getTopic());
							System.out.println("| Tags :" + msg.getTags());
							System.out.println("| Data :" + body);
							// TODO G11 根据tags中的值调用对应接口
							processMessage(msg);
							System.out.println("-------------------------------------");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

				}
			});

			consumer.start();
			System.out.println("-------------------------------------");
			System.out.println("| IoT Api Consumer 启动成功=======");
			System.out.println("-------------------------------------");
		} catch (MQClientException e) {
			e.printStackTrace();
		}

	}

	private void processMessage(Message msg) {
		String msgTag = msg.getTags();
		if (msgTag.equals(topicConfig.getTagConfigParmsSouth())) {
			// TODO G11 设置设备参数
			configParamsSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagConfigThresholdSouth())) {
			// TODO G11 设置阈值参数
			configThresholdSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagQueryParmsSouth())) {
			// TODO G11 读取设备参数
			queryParamsSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagQueryMonthDataSouth())) {
			// TODO G11 读取月冻结数据
			queryMonthDataSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagControlValveSouth())) {
			// TODO G11 控制阀门开关
			controlValveSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagDeviceRegisterSouth())) {
			// TODO G11 注册设备
			deviceRegisterSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagDeviceUpdateSouth())) {
			// TODO G11 修改设备
			deviceUpdateSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagDeviceDeleteSouth())) {
			// TODO G11 删除设备
			deviceDeleteSouth(msg);
		}
		else if (msgTag.equals(topicConfig.getTagDeviceQuerySouth())) {
			// TODO G11 查询设备
			deviceQuerySouth(msg);
		} else {
			System.out.println("| 未知位置消息类型，不处理");
		}
	}

	private void configParamsSouth(Message msg) {
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

	private void configThresholdSouth(Message msg) {
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

	private void queryParamsSouth(Message msg) {
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

	private void queryMonthDataSouth(Message msg) {
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

	private void controlValveSouth(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			ControlValveRequest request = ControlValveRequest.fromJson(requestJson);
			// TODO G11 生成HEX设备指令
			MeterValveControlCmd cmd = new MeterValveControlCmd();
			cmd.setAction(request.getAction());

			String cmdHexStr = CommandGenerator.generateCmd(request.getMeterType(), request.getMeterAddress(),
					request.getMeterFactoryCode(), request.getSequence(), cmd);

			cacheCommand(request, cmdHexStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deviceRegisterSouth(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			RegisterDeviceRequest request = RegisterDeviceRequest.fromJson(requestJson);
			DeviceService deviceService = new DeviceService();
			JsonResult result = deviceService.registerDevice(request);
			producer.sendNorth(result.getData(), topicConfig.getTagDeviceRegisterNorth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deviceUpdateSouth(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			UpdateDeviceRequest request = UpdateDeviceRequest.fromJson(requestJson);
			DeviceService deviceService = new DeviceService();
			JsonResult result = deviceService.modifyDevice(request);
			producer.sendNorth(result.getData(), topicConfig.getTagDeviceUpdateNorth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deviceDeleteSouth(Message msg) {
		try {
			String requestJson = new String(msg.getBody(), "utf-8");
			DeleteDeviceRequest request = DeleteDeviceRequest.fromJson(requestJson);
			DeviceService deviceService = new DeviceService();
			JsonResult result = deviceService.deleteFromIoT(request.getDeviceId());
			producer.sendNorth(result.getData(), topicConfig.getTagDeviceDeleteNorth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deviceQuerySouth(Message msg) {
		DeviceService deviceService = new DeviceService();
		JsonResult result = deviceService.queryDevices("0", "10000");
		producer.sendNorth(result.getData(), topicConfig.getTagDeviceQueryNorth());
	}

	private void cacheCommand(BaseCommandRequest request, String cmdHexStr) {
		// TODO G11 生成CommandBean
		CommandBean commandBean = new CommandBean();
		commandBean.setDeviceId(request.getDeviceId());
		commandBean.setServiceId(request.getServiceId());
		commandBean.setMethod(request.getMethod());
		JSONObject parasObject = new JSONObject();
		parasObject.put("value", cmdHexStr);
		commandBean.setParas(parasObject);

		CommandCache.getInstance().addCommand(request.getDeviceId(), JSON.toJSONString(commandBean));
	}
}
