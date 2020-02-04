package com.learnbind.ai.service.iot;

import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;

public interface IMeterService {

    public JsonResult save(MeterBean meterBean);
}
