package com.learnbind.ai.service.iot.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WmDeviceMapper;
import com.learnbind.ai.iot.Constants;
import com.learnbind.ai.iot.util.HttpsUtil;
import com.learnbind.ai.iot.util.IoTRequestUtil;
import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.DeviceRegisterRspBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.service.iot.IDeviceService;

@Service
public class DeviceService implements IDeviceService {

    @Autowired
    private WmDeviceMapper wmDeviceMapper;

    @Override
    public JsonResult registerDevice(DeviceBean deviceBean) {
        JsonResult jsonResult=JsonResult.fail(0,"Unknown Error");
        String urlReg = Constants.REGISTER_DEVICE;

        Map<String, Object> paramReg = new HashMap<>();
        paramReg.put("verifyCode", deviceBean.getVerifyCode());
        paramReg.put("nodeId", deviceBean.getVerifyCode());
        paramReg.put("timeout", 0);
        paramReg.put("manufacturerId",deviceBean.getManufacturerId());
        paramReg.put("manufacturerName",deviceBean.getManufacturerName());
        paramReg.put("deviceType",deviceBean.getDeviceType());
        paramReg.put("model",deviceBean.getModel());
        paramReg.put("protocolType",deviceBean.getProtocolType());

        try {
            jsonResult = IoTRequestUtil.doPostJsonGetStatusLine(urlReg, paramReg);
            //{"deviceId":"5616575e-4ff0-4e0e-a5c1-ee37a7163c91","verifyCode":"012345678910","timeout":0,"psk":"2b7d8054905de276c28152fec697ad45"}
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonResult != null && jsonResult.getData().contains("deviceId")) {
            DeviceRegisterRspBean registerRspBean = DeviceRegisterRspBean.parseJson(jsonResult.getData());
            deviceBean.setDeviceId(registerRspBean.getDeviceId());

            wmDeviceMapper.save(deviceBean);
        }
        return jsonResult;
    }

    @Override
    public JsonResult modifyDevice(DeviceBean deviceBean) {
        JsonResult jsonResult=JsonResult.fail(0,"Unknown Error");
        String urlModifyDeviceInfo = Constants.MODIFY_DEVICE_INFO + "/" + deviceBean.getDeviceId();

        Map<String, Object> paramModifyDeviceInfo = new HashMap<>();
        paramModifyDeviceInfo.put("manufacturerId", deviceBean.getManufacturerId());
        paramModifyDeviceInfo.put("manufacturerName", deviceBean.getManufacturerName());
        paramModifyDeviceInfo.put("deviceType", deviceBean.getDeviceType());
        paramModifyDeviceInfo.put("model", deviceBean.getModel());
        paramModifyDeviceInfo.put("protocolType", deviceBean.getProtocolType());

        try {
            HttpsUtil httpsUtil = new HttpsUtil();
            httpsUtil.initSSLConfigForTwoWay();
            jsonResult = IoTRequestUtil.doPutJsonGetStatusLine(urlModifyDeviceInfo, paramModifyDeviceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wmDeviceMapper.updateByDeviceId(deviceBean);
        return jsonResult;
    }

    @Override
    public JsonResult queryDevices(String page, String size) {
        JsonResult jsonResult=JsonResult.fail(0,"Unknown Error");
        String urlQueryDevices = Constants.QUERY_DEVICES;
        Map<String, String> paramQueryDevices = new HashMap<>();
        paramQueryDevices.put("pageNo", page);
        paramQueryDevices.put("pageSize", size);

        try {
            HttpsUtil httpsUtil = new HttpsUtil();
            httpsUtil.initSSLConfigForTwoWay();
            jsonResult = IoTRequestUtil.doGetWithParasGetStatusLine(urlQueryDevices, paramQueryDevices);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO G11 是否同步本地数据？
        return jsonResult;
    }

	@Override
	public WmDevice getDeviceByDeviceId(DeviceBean deviceBean) {
		return wmDeviceMapper.getDeviceByDeviceId(deviceBean);
	}


}
