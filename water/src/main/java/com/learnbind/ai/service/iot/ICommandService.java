package com.learnbind.ai.service.iot;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.iot.TestCommandBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.WmCommand;

public interface ICommandService {

    public JsonResult postCommand(TestCommandBean commandBean);
    public void postAsynCommand(TestCommandBean commandBean);
    public JsonResult save(TestCommandBean commandBean);
    public JsonResult update(TestCommandBean commandBean);
    public JsonResult updateByDeviceCommand(TestCommandBean commandBean);
    
    public TestCommandBean getCommandBeanByCommandId(TestCommandBean commandBean);
    
    public List<Map<String, Object>> searchList(Integer searchCommandType, String searchCond);
    public List<Map<String, Object>> searchByDeviceId(String deviceId);
}
