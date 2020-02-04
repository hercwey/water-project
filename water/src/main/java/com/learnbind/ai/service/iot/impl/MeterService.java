package com.learnbind.ai.service.iot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WmMeterMapper;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.service.iot.IMeterService;


@Service
public class MeterService implements IMeterService {

    @Autowired
    private WmMeterMapper wmMeterMapper;

    @Override
    public JsonResult save(MeterBean meterBean) {
        int result = wmMeterMapper.save(meterBean);
        return JsonResult.success(result,result+"");
    }
}
