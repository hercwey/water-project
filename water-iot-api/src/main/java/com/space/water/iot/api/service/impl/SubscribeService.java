package com.space.water.iot.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.config.Constants;
import com.space.water.iot.api.service.ISubscribeService;
import com.space.water.iot.api.util.IoTRequestUtil;

@Service
public class SubscribeService implements ISubscribeService {
    @Override
    public JsonResult subscribeDeviceData() {

        JsonResult result = JsonResult.fail(0,"Unknown Error");
        try {
            String urlSubscribe = Constants.SUBSCRIBE_NOTIFICATION;
            String callbackurl_deviceDataChanged = Constants.DEVICE_DATA_CHANGED_CALLBACK_URL;
            String notifyType_deviceDataChanged = Constants.DEVICE_DATA_CHANGED;

            Map<String, Object> paramSubscribe_deviceDataChanged = new HashMap<>();
            paramSubscribe_deviceDataChanged.put("notifyType", notifyType_deviceDataChanged);
            paramSubscribe_deviceDataChanged.put("callbackurl", callbackurl_deviceDataChanged);

            result = IoTRequestUtil.doPostJson(urlSubscribe,paramSubscribe_deviceDataChanged);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
