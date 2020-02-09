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

import tk.mybatis.mapper.entity.Example;

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
            //deviceBean.setDeviceId(registerRspBean.getDeviceId());
            
            //根据verifyCode(水表编号)更新IOT平台设备ID
            Example example = new Example(WmDevice.class);
            example.createCriteria().andEqualTo("verifyCode", deviceBean.getVerifyCode());
            WmDevice record = new WmDevice();
            record.setDeviceId(registerRspBean.getDeviceId());
            wmDeviceMapper.updateByExampleSelective(record, example);
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

	@Override
	public JsonResult deleteFromIoT(String deviceId) {
        JsonResult jsonResult=JsonResult.fail(0,"Unknown Error");
        String urlDelete = Constants.DELETE_DEVICE + "/" +deviceId;

        try {
            jsonResult = IoTRequestUtil.doDeleteGetStatusLine(urlDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //FIXME G11 本地数据库操作，暂定清空deviceID
        if (jsonResult.getCode() == JsonResult.SUCCESS) {
        	DeviceBean deviceBean = new DeviceBean();
        	deviceBean.setDeviceId(deviceId);
			WmDevice device = wmDeviceMapper.getDeviceByDeviceId(deviceBean);
			if (device != null) {
				deviceBean = DeviceBean.fromWmDevice(device);
				deviceBean.setDeviceId("");
				wmDeviceMapper.updateById(deviceBean);
			}
		}
        
        return jsonResult;
	}
}
