package com.learnbind.ai.service.iot;

import java.util.List;

import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.model.iot.WmMeter;

public interface IMeterService {

    public JsonResult save(MeterBean meterBean);
    
    public List<WmMeter> searchList(Integer searchDataType, String searchCond);
    
}
