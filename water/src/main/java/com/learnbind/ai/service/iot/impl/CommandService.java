package com.learnbind.ai.service.iot.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learnbind.ai.dao.WmCommandMapper;
import com.learnbind.ai.iot.Constants;
import com.learnbind.ai.iot.util.IoTRequestUtil;
import com.learnbind.ai.iot.util.JsonUtil;
import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.CommandCallbackBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.WmCommand;
import com.learnbind.ai.service.iot.ICommandService;

@Service
public class CommandService implements ICommandService {

    @Autowired
    private WmCommandMapper wmCommandMapper;

    @Override
    public JsonResult postCommand(CommandBean commandBean) {

        JsonResult jsonResult = JsonResult.fail(0,"Unknown Error");

        System.out.println("| postCommand 下发控制指令："+commandBean.getDeviceId()+"=="+commandBean.getCommandId());
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
            paramPostAsyncCmd.put("expireTime", 0);

            jsonResult = IoTRequestUtil.doPostJson(urlPostAsyncCmd,paramPostAsyncCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Async
	@Override
	public void postAsynCommand(CommandBean commandBean) {
    	JsonResult jsonResult = postCommand(commandBean);

        String result = jsonResult.getData();
        System.out.println("---------------------------");
        System.out.println("| 控制指令异步发送到IoT平台："+commandBean.getDeviceId()+"=="+commandBean.getCommandId());

        //TODO 补充完整数据库中指令信息，status设置为1（指令已下发）
        CommandBean resultBean = CommandBean.parseJson(result);
        commandBean.setCommandId(resultBean.getCommandId());
        commandBean.setAppId(resultBean.getAppId());
        commandBean.setExpireTime(resultBean.getExpireTime());
        commandBean.setIssuedTimes(resultBean.getIssuedTimes());
        commandBean.setPlatformIssuedTime(resultBean.getPlatformIssuedTime());
        commandBean.setStatus(resultBean.getStatus());
      //FIXME G11 判断指令执行结果，逻辑待优化
        if (result.contains(CommandCallbackBean.COMMAND_CALLBACK_STATUS_DELIVERED) || result.contains(CommandCallbackBean.COMMAND_CALLBACK_STATUS_SENT) || result.contains(CommandCallbackBean.COMMAND_CALLBACK_STATUS_SUCCESS)) {
        	commandBean.setDatabaseStatus(2);
		} else {
            commandBean.setDatabaseStatus(1);
		}

        if (resultBean.getParas() != null) {
            commandBean.setMethodParams(resultBean.getParas().toJSONString());
        }
        update(commandBean);

        System.out.println("| 控制指令异步发送完成："+ result);
        System.out.println("---------------------------");
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
	public List<Map<String, Object>> searchList(Integer searchCommandType, String searchCond) {
		return wmCommandMapper.searchList(searchCommandType, searchCond);
	}

	@Override
	public CommandBean getCommandBeanByCommandId(CommandBean commandBean) {
		// TODO Auto-generated method stub
		return wmCommandMapper.selectByCommandId(commandBean);
	}
	@Override
	public List<Map<String, Object>> searchByDeviceId(String deviceId) {
		return wmCommandMapper.searchByDeviceId(deviceId);
	}
    
}
