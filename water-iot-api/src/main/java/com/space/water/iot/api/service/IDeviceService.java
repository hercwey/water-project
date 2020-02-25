package com.space.water.iot.api.service;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.device.DeleteDeviceRequest;
import com.space.water.iot.api.model.device.ModifyRequest;
import com.space.water.iot.api.model.device.RegisterDeviceRequest;
import com.space.water.iot.api.model.device.UpdateDeviceRequest;

public interface IDeviceService {
	public JsonResult registerDevice(RegisterDeviceRequest registerReq);

	public JsonResult modifyDevice(UpdateDeviceRequest modifyReq);

	public JsonResult deleteFromIoT(String deviceId);

	public JsonResult queryDevices(String page, String size);

}
