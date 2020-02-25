package com.space.water.iot.api.service;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.model.report.BaseReportData;

public interface IReportService {

    public JsonResult process(BaseReportData meterBean);
    
}
