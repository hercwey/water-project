package com.learnbind.ai.service.iot;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.WmCommand;

public interface ICommandService {

    public JsonResult postCommand(CommandBean commandBean);
    public void postAsynCommand(CommandBean commandBean);
    public JsonResult save(CommandBean commandBean);
    public JsonResult update(CommandBean commandBean);
    public JsonResult updateByDeviceCommand(CommandBean commandBean);
    
    public CommandBean getCommandBeanByCommandId(CommandBean commandBean);
    
    public List<Map<String, Object>> searchList(Integer searchCommandType, String searchCond);
    public List<Map<String, Object>> searchByDeviceId(String deviceId);
}
