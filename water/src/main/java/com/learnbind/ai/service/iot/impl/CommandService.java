package com.learnbind.ai.service.iot.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learnbind.ai.dao.WmCommandMapper;
import com.learnbind.ai.iot.Constants;
import com.learnbind.ai.iot.util.IoTRequestUtil;
import com.learnbind.ai.iot.util.JsonUtil;
import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.ICommandService;

@Service
public class CommandService implements ICommandService {

    @Autowired
    private WmCommandMapper wmCommandMapper;

    @Override
    public JsonResult postAsynCommand(CommandBean commandBean) {

        JsonResult jsonResult = JsonResult.fail(0,"Unknown Error");
        try {
            String urlPostAsyncCmd = Constants.POST_ASYNC_CMD;
            String callbackUrl = Constants.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;

            String deviceId = commandBean.getDeviceId();
            String serviceId = commandBean.getServiceId();
            String method = commandBean.getMethod();

            ObjectNode paras = JsonUtil.convertObject2ObjectNode(commandBean.getParas().toJSONString());

            Map<String, Object> paramCommand = new HashMap<>();
            paramCommand.put("serviceId", serviceId);
            paramCommand.put("method", method);
            paramCommand.put("paras", paras);

            Map<String, Object> paramPostAsyncCmd = new HashMap<>();
            paramPostAsyncCmd.put("deviceId", deviceId);
            paramPostAsyncCmd.put("command", paramCommand);
            paramPostAsyncCmd.put("callbackUrl", callbackUrl);

            jsonResult = IoTRequestUtil.doPostJson(urlPostAsyncCmd,paramPostAsyncCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    public JsonResult save(CommandBean commandBean) {
        int result = wmCommandMapper.save(commandBean);
        return JsonResult.success(result,result+"");
    }

    @Override
    public JsonResult update(CommandBean commandBean) {
        int result = wmCommandMapper.modify(commandBean);
        return JsonResult.success(result,result+"");
    }

    @Override
    public JsonResult updateByDeviceCommand(CommandBean commandBean) {
        int result = wmCommandMapper.updateByDeviceCommand(commandBean);
        return JsonResult.success(result,result+"");
    }

	@Override
	public List<Map<String, Object>> searchList() {
		return wmCommandMapper.searchList();
	}
    
}
