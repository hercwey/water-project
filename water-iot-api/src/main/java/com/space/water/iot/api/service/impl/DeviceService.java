package com.space.water.iot.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.config.Constants;
import com.space.water.iot.api.model.device.RegisterDeviceRequest;
import com.space.water.iot.api.model.device.RegisterDeviceResponse;
import com.space.water.iot.api.model.device.UpdateDeviceRequest;
import com.space.water.iot.api.service.IDeviceService;
import com.space.water.iot.api.util.IoTRequestUtil;

@Service
public class DeviceService implements IDeviceService {

	@Override
	public JsonResult registerDevice(RegisterDeviceRequest registerReq) {
		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");
		if (registerReq == null) {
			return jsonResult;
		}
		String urlReg = Constants.REGISTER_DEVICE;

		Map<String, Object> paramReg = new HashMap<>();
		paramReg = RegisterDeviceRequest.toRegisterParamsMap(registerReq);

		try {
			jsonResult = IoTRequestUtil.doPostJsonGetStatusLine(urlReg, paramReg);
			// {"deviceId":"5616575e-4ff0-4e0e-a5c1-ee37a7163c91","verifyCode":"012345678910","timeout":0,"psk":"2b7d8054905de276c28152fec697ad45"}
			String registerResponse = jsonResult.getData();

	        if (StringUtils.isNotBlank(registerResponse) && !registerResponse.contains("error_code")) {
	        	RegisterDeviceResponse registerRsp = RegisterDeviceResponse.fromJson(registerResponse);
	            UpdateDeviceRequest modifyReq = new UpdateDeviceRequest();
	            modifyReq.setAppId(registerReq.getAppId());
	            modifyReq.setDeviceId(registerRsp.getDeviceId());
	            modifyReq.setDeviceType(registerReq.getDeviceType());
	            modifyReq.setManufacturerId(registerReq.getManufacturerId());
	            modifyReq.setManufacturerName(registerReq.getManufacturerName());
	            modifyReq.setModel(registerReq.getModel());
	            modifyReq.setProtocolType(registerReq.getProtocolType());
	            
	            JsonResult modifyResult = modifyDevice(modifyReq);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Override
	public JsonResult modifyDevice(UpdateDeviceRequest modifyReq) {
		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");
		if (modifyReq == null) {
			return jsonResult;
		}
		String urlModifyDeviceInfo = Constants.MODIFY_DEVICE_INFO + "/" + modifyReq.getDeviceId();

		Map<String, Object> paramModifyDeviceInfo = new HashMap<>();
		paramModifyDeviceInfo = UpdateDeviceRequest.toMap(modifyReq);

		try {
			jsonResult = IoTRequestUtil.doPutJsonGetStatusLine(urlModifyDeviceInfo, paramModifyDeviceInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Override
	public JsonResult queryDevices(String page, String size) {
		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");
		String urlQueryDevices = Constants.QUERY_DEVICES;
		Map<String, String> paramQueryDevices = new HashMap<>();
		paramQueryDevices.put("pageNo", page);
		paramQueryDevices.put("pageSize", size);

		try {
			jsonResult = IoTRequestUtil.doGetWithParasGetStatusLine(urlQueryDevices, paramQueryDevices);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Override
	public JsonResult deleteFromIoT(String deviceId) {
		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");

		String urlDelete = Constants.DELETE_DEVICE + "/" + deviceId;

		try {
			jsonResult = IoTRequestUtil.doDeleteGetStatusLine(urlDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
}
