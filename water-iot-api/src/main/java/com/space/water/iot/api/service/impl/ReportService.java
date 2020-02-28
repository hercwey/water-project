package com.space.water.iot.api.service.impl;

import java.util.ArrayList;

import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.space.water.iot.api.command.CommandCache;
import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.command.ConfigParamsResponse;
import com.space.water.iot.api.model.command.ConfigThresholdResponse;
import com.space.water.iot.api.model.command.ControlValveResponse;
import com.space.water.iot.api.model.command.QueryMonthDataResponse;
import com.space.water.iot.api.model.command.QueryParamsResponse;
import com.space.water.iot.api.model.common.DeviceParams;
import com.space.water.iot.api.model.common.MonthData;
import com.space.water.iot.api.model.common.ReportDataType;
import com.space.water.iot.api.model.iot.command.CommandBean;
import com.space.water.iot.api.model.report.AutoReport;
import com.space.water.iot.api.model.report.BaseReportData;
import com.space.water.iot.api.model.report.MeterReportBean;
import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.rocketmq.RocketTopicConfig;
import com.space.water.iot.api.service.ICommandService;
import com.space.water.iot.api.service.IReportService;

@Service
public class ReportService implements IReportService {

	@Autowired
	ICommandService commandService;
	@Autowired
	Producer producer;
	@Autowired
	RocketTopicConfig topicConfig;
	
	@Override
	public JsonResult process(BaseReportData meterBean) {		
		//TODO G11 将消息发送给《营收子系统》
		SendResult sendResult = sendNorth(meterBean);

		int type = meterBean.getDataType();
		if (type == ReportDataType.METER_DATA_TYPE_REPORT
				|| type == ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG
				|| type == ReportDataType.METER_DATA_TYPE_MONTH_FREEZE
				|| type == ReportDataType.METER_DATA_TYPE_RSP_WRITE_CONFIG
				|| type == ReportDataType.METER_DATA_TYPE_RSP_SWITCH_VALVE
				|| type == ReportDataType.METER_DATA_TYPE_RSP_SET_THRESHOLD) {

			//TODO G11 检测该设备是否有待下发指令，如果有，逐条下发
			ArrayList<String> commandList = CommandCache.getInstance().getCommandList(meterBean.getDeviceId());
			if (commandList != null && commandList.size() > 0) {
				//TODO 逐条下发指令，并清除指令列表
				for (int i = 0; i < commandList.size(); i++) {
					// TODO G11 向电信平台发送指令，并向“营收子系统”发布命令执行状态改变消息
					commandService.postAsynCommand(CommandBean.parseJson(commandList.get(i)));

					/**
					LogUtil.debug("-------------------------------------");
					LogUtil.debug("| 下发设备指令");
					LogUtil.debug("| DeviceID：" + meterBean.getDeviceId());
					LogUtil.debug("| Command ：" + commandList.get(i));
					LogUtil.debug("-------------------------------------");
					*/
					
				}
				/**
				LogUtil.debug("-------------------------------------");
				LogUtil.debug("| 下发设备指令列表完成");
				LogUtil.debug("| DeviceID：" + meterBean.getDeviceId());
				LogUtil.debug("-------------------------------------");
				*/
			}

			//TODO G11 下发消息后，从消息列表中删除消息
			if (commandList != null) {
				commandList.clear();
			}
		}
		
		return JsonResult.success(JsonResult.SUCCESS, BaseReportData.toJsonString(meterBean));
	}
	
	public SendResult sendNorth(BaseReportData meterBean) {

		String response = "";
		String tag = "";
		switch (meterBean.getDataType()) {
		case ReportDataType.METER_DATA_TYPE_REPORT:
			tag =  topicConfig.getTagAutoReport();
			AutoReport report = AutoReport.fromJson(BaseReportData.toJsonString(meterBean));
			report.setReportData(MeterReportBean.fromJson(meterBean.getData()));
			response = AutoReport.toJsonString(report);
			break;
		case ReportDataType.METER_DATA_TYPE_MONTH_FREEZE:
			tag = topicConfig.getTagQueryMonthdataNorth();
			QueryMonthDataResponse monthDataResp = QueryMonthDataResponse.fromJson(BaseReportData.toJsonString(meterBean));
			monthDataResp.setMonthData(MonthData.fromJson(meterBean.getData()));
			response = QueryMonthDataResponse.toJsonString(monthDataResp);
			break;
		case ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG:
			tag = topicConfig.getTagQueryParmsNorth();
			QueryParamsResponse paramsResponse = QueryParamsResponse.fromJson(BaseReportData.toJsonString(meterBean));
			paramsResponse.setDeviceParams(DeviceParams.fromJson(meterBean.getData()));
			response = AutoReport.toJsonString(paramsResponse);
			break;
		case ReportDataType.METER_DATA_TYPE_RSP_SET_THRESHOLD:
			tag = topicConfig.getTagConfigThresholdNorth();
			ConfigThresholdResponse thresholdResp = ConfigThresholdResponse.fromJson(BaseReportData.toJsonString(meterBean));
			response = AutoReport.toJsonString(thresholdResp);
			break;
		case ReportDataType.METER_DATA_TYPE_RSP_SWITCH_VALVE:
			tag = topicConfig.getTagControlValveNorth();
			ControlValveResponse cvResp = ControlValveResponse.fromJson(BaseReportData.toJsonString(meterBean));
			response = AutoReport.toJsonString(cvResp);
			break;
		case ReportDataType.METER_DATA_TYPE_RSP_WRITE_CONFIG:
			tag = topicConfig.getTagConfigParmsNorth();
			ConfigParamsResponse configParamsResponse = ConfigParamsResponse.fromJson(BaseReportData.toJsonString(meterBean));
			configParamsResponse.setDeviceParams(DeviceParams.fromJson(meterBean.getData()));
			response = AutoReport.toJsonString(configParamsResponse);
			break;
		case ReportDataType.METER_DATA_TYPE_START_CONNECT:
			tag =  topicConfig.getTagAutoReport();
			AutoReport reportConnect = AutoReport.fromJson(BaseReportData.toJsonString(meterBean));
			response = AutoReport.toJsonString(reportConnect);
			break;
		case ReportDataType.METER_DATA_TYPE_START_DISCONNECT:
			tag =  topicConfig.getTagAutoReport();
			AutoReport reportDisonnect = AutoReport.fromJson(BaseReportData.toJsonString(meterBean));
			response = AutoReport.toJsonString(reportDisonnect);
			break;
		default:
			break;
		}
		
		return producer.sendNorth(response, tag);//new SendResult();//
	}
}
