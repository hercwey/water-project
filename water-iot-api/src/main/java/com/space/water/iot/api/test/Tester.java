package com.space.water.iot.api.test;

import static com.space.water.iot.api.protocol.PacketCodec.encodeData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.rocketmq.common.message.Message;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.space.water.iot.api.config.MQTags;
import com.space.water.iot.api.model.command.BaseCommandResponse;
import com.space.water.iot.api.model.command.ConfigParamsRequest;
import com.space.water.iot.api.model.command.ConfigParamsResponse;
import com.space.water.iot.api.model.command.ConfigThresholdRequest;
import com.space.water.iot.api.model.command.ConfigThresholdResponse;
import com.space.water.iot.api.model.command.ControlValveRequest;
import com.space.water.iot.api.model.command.QueryMonthDataRequest;
import com.space.water.iot.api.model.command.QueryMonthDataResponse;
import com.space.water.iot.api.model.command.QueryParamsRequest;
import com.space.water.iot.api.model.command.QueryParamsResponse;
import com.space.water.iot.api.model.common.DeviceParams;
import com.space.water.iot.api.model.device.DeleteDeviceRequest;
import com.space.water.iot.api.model.device.RegisterDeviceRequest;
import com.space.water.iot.api.model.device.UpdateDeviceRequest;
import com.space.water.iot.api.model.report.AutoReport;
import com.space.water.iot.api.model.report.BaseReportData;
import com.space.water.iot.api.model.report.MeterReportBean;
import com.space.water.iot.api.protocol.Protocol;
import com.space.water.iot.api.protocol.bean.MeterConfig;
import com.space.water.iot.api.protocol.bean.MeterConfigReadResp;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteResp;
import com.space.water.iot.api.protocol.bean.MeterReadWaterResp;
import com.space.water.iot.api.protocol.bean.MeterValveControlResp;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdResp;
import com.space.water.iot.api.rocketmq.RocketTopicConfig;

public class Tester {
	
	public static String autoReport() {
		String iotReportData = "{\"notifyType\":\"deviceDatasChanged\",\"requestId\":null,\"deviceId\":\"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\",\"gatewayId\":\"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\",\"services\":[{\"serviceId\":\"JRprotocol\",\"serviceType\":\"JRprotocol\",\"data\":{\"JRprotocolXY\":\"681036390745404358811d1f90b0363907454043573522100102200e0000000019320900130000009f16\"},\"eventTime\":\"20200210T223604Z\"}]}";
		// TODO 数据解析
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(iotReportData);
		AutoReport report = AutoReport.fromJson(BaseReportData.toJsonString(meterBean));
		report.setReportData(MeterReportBean.fromJson(meterBean.getData()));
		String jsonData = AutoReport.toJsonString(report);
		// 调用数据上报接口
		System.out.println("---------------------------");
		System.out.println("| 模拟自动上报数据：" + jsonData);
		System.out.println("---------------------------");
		return jsonData;
	}

	public static String configParamsSouth() {
		String jsonData = "";

		ConfigParamsRequest request = new ConfigParamsRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		request.setMeterAddress("4045073936");
		request.setMeterFactoryCode("5843");
		request.setMeterType((byte) 16);
		request.setMethod("JRprotocolYX");
		request.setSequence((byte) 123);
		request.setServiceId("JRprotocol");

		DeviceParams deviceParams = DeviceParams.fromJson(
				"{\"configFlag\":{\"emergTime\":true,\"magneticAlarmClear\":false,\"maxReport\":true,\"meterNum\":true,\"meterStatus\":false,\"meterTime\":false,\"period\":true,\"periodUnit\":true,\"sampleUnit\":true,\"serverIp\":false,\"serverPort\":false,\"valveMaintainTime\":true,\"valveTime\":true,\"waterVolume\":true},\"meterBasicValue\":0,\"reportPeriod\":0,\"reportPeriodUnit\":0,\"reportRation\":0,\"sampleUnit\":0.0,\"serverPort\":0,\"temporaryTime\":0,\"valveMaintainPeriod\":0,\"valveRunTime\":0}");
		request.setDeviceParams(deviceParams);

		jsonData = ConfigParamsRequest.toJsonString(request);
		// 调用数据上报接口
		System.out.println("---------------------------");
		System.out.println("| 模拟配置设备指令数据：" + jsonData);
		System.out.println("---------------------------");
		return jsonData;
	}

	public static String configParamsNorth() {
		String jsonData = "";

		// TODO 生成设备原始指令
		MeterConfigWriteResp cmdResp = new MeterConfigWriteResp();
		cmdResp.setReportPeriod((short) 60);// 定时上传周期
		cmdResp.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
		cmdResp.setReportRation((short) 100);// 定量上传值
		cmdResp.setTemporaryTime((byte) 2);// 用户临时开阀用水限定时间
		cmdResp.setValveRunTime((byte) 3);// 阀门行程时间
		cmdResp.setValveMaintainPeriod((short) 30);// 阀门维护周期
		cmdResp.setMeterBasicValue(3000);// 表底数 TODO 数据类型
		cmdResp.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位
		cmdResp.setMeterNumber("123456123456"); // 表号
		cmdResp.setMeterTime("20200101235959"); // 表当前时间

		// 表状态字
		cmdResp.setMeterStatusFlag((short) (Protocol.METER_STATUS_VALVE_OPEN | Protocol.METER_STATUS_PERIOD_ON
				| Protocol.METER_STATUS_MAX_REPORT_ON | Protocol.METER_STATUS_MAGNETIC_ON));
		cmdResp.setServerIp("10.88.192.11");
		cmdResp.setServerPort((short) 6538);

		// [从站] 封装为字节消息
		byte[] respBytes = encodeData(Protocol.METER_TYPE_10H, "150590056911", "7833", (byte) 0x01, cmdResp);
		String originHexData = HexUtils.toHexString(respBytes);

		String iotData = packIotResponse(originHexData);

		// TODO 解析电信平台数据
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(iotData);

		// TODO 封装north消息
		ConfigParamsResponse response = ConfigParamsResponse.fromJson(BaseReportData.toJsonString(meterBean));

		jsonData = ConfigParamsResponse.toJsonString(response);
		return jsonData;
	}

	public static String configThresholdSouth() {
		String jsonData = "";
		ConfigThresholdRequest request = new ConfigThresholdRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		request.setMeterAddress("4045073936");
		request.setMeterFactoryCode("5843");
		request.setMeterType((byte) 16);
		request.setMethod("JRprotocolYX");
		request.setSequence((byte) 123);
		request.setServiceId("JRprotocol");

		request.setThreshold((short) 50);
		jsonData = JSON.toJSONString(request);
		return jsonData;
	}
	
	public static String configThresholdNorth() {
		String jsonData = "";

		// TODO 生成设备原始指令
		MeterVolumeThresholdResp cmdResp = new MeterVolumeThresholdResp();

		// [从站] 封装为字节消息
		byte[] respBytes = encodeData(Protocol.METER_TYPE_10H, "150590056911", "7833", (byte) 0x01, cmdResp);
		String originHexData = HexUtils.toHexString(respBytes);

		String iotData = packIotResponse(originHexData);

		// TODO 解析电信平台数据
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(iotData);

		// TODO 封装north消息
		ConfigThresholdResponse response = ConfigThresholdResponse.fromJson(BaseReportData.toJsonString(meterBean));

		jsonData = ConfigParamsResponse.toJsonString(response);
		return jsonData;
	}

	public static String queryParamsSouth() {
		String jsonData = "";
		QueryParamsRequest request = new QueryParamsRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		request.setMeterAddress("4045073936");
		request.setMeterFactoryCode("5843");
		request.setMeterType((byte) 16);
		request.setMethod("JRprotocolYX");
		request.setSequence((byte) 123);
		request.setServiceId("JRprotocol");

		jsonData = JSON.toJSONString(request);
		return jsonData;
	}

	public static String queryParamsNorth() {
		String jsonData = "";
		// TODO 生成设备原始指令
		MeterConfigReadResp cmdResp = new MeterConfigReadResp();
		cmdResp.setReportPeriod((short) 60);// 定时上传周期
		cmdResp.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
		cmdResp.setReportRation((short) 100);// 定量上传值
		cmdResp.setTemporaryTime((byte) 2);// 用户临时开阀用水限定时间
		cmdResp.setValveRunTime((byte) 3);// 阀门行程时间
		cmdResp.setValveMaintainPeriod((short) 30);// 阀门维护周期
		cmdResp.setMeterBasicValue(3000);// 表底数 TODO 数据类型
		cmdResp.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位
		cmdResp.setMeterNumber("123456123456"); // 表号
		cmdResp.setMeterTime("20200101235959"); // 表当前时间

		// 表状态字
		cmdResp.setMeterStatusFlag((short) (Protocol.METER_STATUS_VALVE_OPEN | Protocol.METER_STATUS_PERIOD_ON
				| Protocol.METER_STATUS_MAX_REPORT_ON | Protocol.METER_STATUS_MAGNETIC_ON));
		cmdResp.setServerIp("10.88.192.11");
		cmdResp.setServerPort((short) 6538);

		// [从站] 封装为字节消息
		byte[] respBytes = encodeData(Protocol.METER_TYPE_10H, "150590056911", "7833", (byte) 0x01, cmdResp);
		String originHexData = HexUtils.toHexString(respBytes);

		String iotData = packIotResponse(originHexData);

		// TODO 解析电信平台数据
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(iotData);

		// TODO 封装north消息
		QueryParamsResponse response = QueryParamsResponse.fromJson(BaseReportData.toJsonString(meterBean));

		jsonData = QueryParamsResponse.toJsonString(response);
		return jsonData;
	}

	public static String queryMonthDataSouth() {
		String jsonData = "";
		QueryMonthDataRequest request = new QueryMonthDataRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		request.setMeterAddress("4045073936");
		request.setMeterFactoryCode("5843");
		request.setMeterType((byte) 16);
		request.setMethod("JRprotocolYX");
		request.setSequence((byte) 123);
		request.setServiceId("JRprotocol");

		jsonData = JSON.toJSONString(request);
		return jsonData;
	}

	public static String queryMonthDataNorth() {
		String jsonData = "";

		// TODO 生成设备原始指令
		MeterReadWaterResp cmdResp = new MeterReadWaterResp();
        cmdResp.setMeterNumber("123456123456"); // 采样单位
        cmdResp.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位

        List<MeterReadWaterResp.WaterVolume> listVolume = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            MeterReadWaterResp.WaterVolume volume = cmdResp.new WaterVolume();
            int m = i+1;
            if (m < 10) {
                volume.date = "20200" + m; //TODO 年月
            } else {
                volume.date = "2020" + m;
            }
            volume.volume = 100 + i;

            listVolume.add(volume);
        }
        cmdResp.setListWater(listVolume);

		// [从站] 封装为字节消息
		byte[] respBytes = encodeData(Protocol.METER_TYPE_10H, "150590056911", "7833", (byte) 0x01, cmdResp);
		String originHexData = HexUtils.toHexString(respBytes);

		String iotData = packIotResponse(originHexData);

		// TODO 解析电信平台数据
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(iotData);

		// TODO 封装north消息
		QueryMonthDataResponse response = QueryMonthDataResponse.fromJson(BaseReportData.toJsonString(meterBean));

		jsonData = QueryMonthDataResponse.toJsonString(response);
		return jsonData;
	}

	public static String controlValveSouth() {
		String jsonData = "";
		ControlValveRequest request = new ControlValveRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		request.setMeterAddress("4045073936");
		request.setMeterFactoryCode("5843");
		request.setMeterType((byte) 16);
		request.setMethod("JRprotocolYX");
		request.setSequence((byte) 123);
		request.setServiceId("JRprotocol");
		request.setAction((byte) 1);

		jsonData = JSON.toJSONString(request);
		return jsonData;
	}

	public static String controlValveNorth() {
		String jsonData = "";

		// TODO 生成设备原始指令
		MeterValveControlResp cmdResp = new MeterValveControlResp();

		// [从站] 封装为字节消息
		byte[] respBytes = encodeData(Protocol.METER_TYPE_10H, "150590056911", "7833", (byte) 0x01, cmdResp);
		String originHexData = HexUtils.toHexString(respBytes);

		String iotData = packIotResponse(originHexData);

		// TODO 解析电信平台数据
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(iotData);

		// TODO 封装north消息
		QueryMonthDataResponse response = QueryMonthDataResponse.fromJson(BaseReportData.toJsonString(meterBean));

		jsonData = QueryMonthDataResponse.toJsonString(response);
		return jsonData;
	}
	
	public static String deviceRegisterSouth() {
		String jsonData = "";
		RegisterDeviceRequest request = new RegisterDeviceRequest();
		request.setDeviceType("JRNBWaterMeter");
		request.setManufacturerId("JR0912X");
		request.setManufacturerName("JRIWA");
		request.setModel("JR0912Y");
		request.setNodeId("12345677777");
		request.setProtocolType("CoAP");
		request.setTimeout(0);
		request.setVerifyCode("12345677777");
		jsonData = JSON.toJSONString(request);

		return jsonData;
	}

	public static String deviceUpdateSouth() {
		String jsonData = "";
		UpdateDeviceRequest request = new UpdateDeviceRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		request.setDeviceType("JRNBWaterMeter");
		request.setManufacturerId("JR0912X");
		request.setManufacturerName("JRIWA");
		request.setModel("JR0912Y");
		request.setProtocolType("CoAP");
		jsonData = JSON.toJSONString(request);

		return jsonData;
	}

	public static String deviceDeleteSouth() {
		String jsonData = "";
		DeleteDeviceRequest request = new DeleteDeviceRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		jsonData = JSON.toJSONString(request);

		return jsonData;
	}

	public static String deviceQuery() {
		String jsonData = "";
		DeleteDeviceRequest request = new DeleteDeviceRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		jsonData = JSON.toJSONString(request);

		return jsonData;
	}

	public static Message packMQMessage(String tag, String data) {
		Message message = null;
		try {
			message = new Message("Topic1", tag, data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public static void initResponseBase(BaseCommandResponse response) {
		response.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		response.setGatewayId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		response.setServiceId("JRprotocol");
		response.setServiceType("JRprotocol");
		response.setMeterAddr("");
		response.setMeterType(16);
		response.setFactoryCode("5843");
		response.setCtrlCode("81");
		response.setSequence(176);
		response.setEventTime(new Date(1581374164000L));
		response.setChecksum(-97);
		response.setData("{}");
		response.setDataDI((short) -28641);
		response.setDataType(1);
		response.setDataBasic("");
		response.setJsonData("{}");
	}

	public static String packIotResponse(String commandHex) {
		// TODO 封装电信平台返回数据
		JSONObject iotDataObject = new JSONObject();
		iotDataObject.put("notifyType", "deviceDatasChanged");
		iotDataObject.put("requestId", null);
		iotDataObject.put("deviceId", "b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		iotDataObject.put("gatewayId", "b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		JSONArray services = new JSONArray();
		JSONObject service = new JSONObject();
		service.put("serviceId", "JRprotocol");
		service.put("serviceType", "JRprotocol");
		JSONObject data = new JSONObject();
		data.put("JRprotocolXY", commandHex);
		service.put("data", data);
		service.put("eventTime", "20200210T223604Z");

		services.add(service);
		iotDataObject.put("services", services);

		String iotData = iotDataObject.toJSONString();
		return iotData;
	}

}
