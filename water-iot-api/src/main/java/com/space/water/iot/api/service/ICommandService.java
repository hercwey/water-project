package com.space.water.iot.api.service;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.iot.command.CommandBean;

public interface ICommandService {

    public JsonResult postCommand(CommandBean commandBean);
    public void postAsynCommand(CommandBean commandBean);
}
