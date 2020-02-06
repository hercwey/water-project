package com.learnbind.ai.controller.iot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.service.iot.IDeviceService;
import com.learnbind.ai.service.iot.IMeterService;

@Controller
@RequestMapping("/meter")
public class MeterController {

    @Autowired
    IMeterService meterService;
    @Autowired
    IDeviceService deviceService;

    @RequestMapping(value = "/uploadDeviceData", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadDeviceData(@RequestBody String data) {
        System.out.println("收到设备消息:"+data);
        //TODO 水表数据解析
        MeterBean meterBean = MeterBean.fromUploadDataJson(data);
        //TODO 水表数据保存
        JsonResult jsonResult = meterService.save(meterBean);
        //TODO 水表数据封装信息保存（表类型、表地址、表厂商）
        DeviceBean deviceBean = new DeviceBean();
        deviceBean.setDeviceId(meterBean.getDeviceId());
        WmDevice wmDevice = deviceService.getDeviceByDeviceId(deviceBean);
        
        if (wmDevice != null) {
        	deviceBean = DeviceBean.fromWmDevice(wmDevice);
            deviceBean.setMeterType(meterBean.getMeterType());
            deviceBean.setMeterAddress(meterBean.getMeterAddr());
            deviceBean.setMeterFactoryCode(meterBean.getFactoryCode());
            
            deviceService.modifyDevice(deviceBean);
            
		}
        
        jsonResult.setData(data);
        return ResponseEntity.ok(jsonResult.toString());
    }
    
    @RequestMapping(value = "/search-meters")
    public String searchMeters(Model model, Integer pageNum, Integer pageSize) {
    	
    	// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<WmMeter> meterList = meterService.searchList();
		PageInfo<WmMeter> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return "iot/meter_table";
    	
    }
}
