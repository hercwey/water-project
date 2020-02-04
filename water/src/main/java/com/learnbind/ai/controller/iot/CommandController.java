package com.learnbind.ai.controller.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.CommandResultBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.ICommandService;

@RestController
@RequestMapping("/cmd")
public class CommandController {

    @Autowired
    ICommandService commandService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<String> send(@RequestBody String data) {
        //TODO 下发指令
        CommandBean commandBean = CommandBean.parseJson(data);

        //TODO 将指令写入数据库，status设置为0（指令已创建，未下发成功）
        commandBean.setDatabaseStatus(0);

        commandService.save(commandBean);
        JsonResult jsonResult = commandService.postAsynCommand(commandBean);

        String result = jsonResult.getData();
        System.out.println("控制指令发送到IoT平台："+commandBean.getDeviceId()+"=="+commandBean.getServiceId()+"=="+commandBean.getMethod());

        //TODO 补充完整数据库中指令信息，status设置为1（指令已下发）
        CommandBean resultBean = CommandBean.parseJson(result);
        commandBean.setCommandId(resultBean.getCommandId());
        commandBean.setAppId(resultBean.getAppId());
        commandBean.setExpireTime(resultBean.getExpireTime());
        commandBean.setIssuedTimes(resultBean.getIssuedTimes());
        commandBean.setPlatformIssuedTime(resultBean.getPlatformIssuedTime());
        commandBean.setStatus(resultBean.getStatus());
        if (commandBean.getStatus() != null && commandBean.getStatus().equalsIgnoreCase("sent")) {
            commandBean.setDatabaseStatus(1);
        }

        if (resultBean.getParas() != null) {
            commandBean.setMethodParams(resultBean.getParas().toJSONString());
        }
        commandService.update(commandBean);

        return ResponseEntity.ok(jsonResult.toString());
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public ResponseEntity<String> callback(@RequestBody String data) {
        //TODO 指令执行结果
        System.out.println("控制指令执行结果：" + data);

        //TODO 更改status为-1（设备执行失败）或2（设备执行成功）
        CommandResultBean commandResultBean = CommandResultBean.parseJson(data);

        CommandBean commandBean = new CommandBean();
        commandBean.setDeviceId(commandResultBean.getDeviceId());
        commandBean.setCommandId(commandResultBean.getCommandId());
        if (commandResultBean.getResultCode()!=null && commandResultBean.getResultCode().equalsIgnoreCase("FAILED")) {
            commandBean.setDatabaseStatus(-1);
        } else {
            commandBean.setDatabaseStatus(2);
        }
        commandBean.setDesc(commandResultBean.getReason());
        commandService.updateByDeviceCommand(commandBean);

        return ResponseEntity.ok(JsonResult.success(JsonResult.SUCCESS,data).toString());
    }

}
