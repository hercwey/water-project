package com.learnbind.ai.service.iot;

import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.WmDevice;

public interface IDeviceService {
    public JsonResult registerDevice(DeviceBean deviceBean);
    public JsonResult modifyDevice(DeviceBean deviceBean);
    public JsonResult deleteFromIoT(String deviceId);

    public JsonResult queryDevices(String page, String size);
    public WmDevice getDeviceByDeviceId(DeviceBean deviceBean);
    
}
