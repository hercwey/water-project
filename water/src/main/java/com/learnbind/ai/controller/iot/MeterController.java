package com.learnbind.ai.controller.iot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.model.iot.MeterDataBean;
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

		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(WmMeter meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);//实体转MAP
			
			String meterData = meter.getMeterData();
			MeterDataBean meterDataBean = null;
			if(StringUtils.isNotBlank(meterData)) {
				if(meterData.startsWith("{") && meterData.endsWith("}")) {
					meterDataBean = MeterDataBean.fromJson(meterData);
				}else {
					try {
						meterDataBean = MeterDataBean.fromHexData(meterData);
					} catch (Exception e) {
						e.printStackTrace();
						meterDataBean = null;
						System.out.println("----------解析16进制数据异常（MeterDataBean.fromHexData），原16进制数据："+meterData);
					}
				}
			}
			
			String meterNumber = "";//表号: 6字节数字型字符串
			String meterTime = "";//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
			Integer totalVolume = null;//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			String sampleUnit = "";//采样参数：单位M3
			Integer batteryVoltage = null;//电池电压：单位V
			String meterStatus = "";//表状态字：2字节
			String signal = "";//信号强度
			String pressure = "";//压力值：xx.yyyy
			if(meterDataBean!=null) {
				meterNumber = meterDataBean.getMeterNumber();//表号: 6字节数字型字符串
				meterTime = meterDataBean.getMeterTime();//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
				totalVolume = meterDataBean.getTotalVolume();//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
				sampleUnit = meterDataBean.getSampleUnit();//采样参数：单位M3
				batteryVoltage = meterDataBean.getBatteryVoltage();//电池电压：单位V
				meterStatus = meterDataBean.getMeterStatus();//表状态字：2字节
				signal = meterDataBean.getSignal();//信号强度
				pressure = meterDataBean.getPressure();//压力值：xx.yyyy
			}
			
			meterMap.put("meterNumber", meterNumber);//表号: 6字节数字型字符串
			meterMap.put("meterTime", meterTime);//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
			meterMap.put("totalVolume", totalVolume);//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			meterMap.put("sampleUnit", sampleUnit);//采样参数：单位M3
			meterMap.put("batteryVoltage", batteryVoltage);//电池电压：单位V
			meterMap.put("meterStatus", meterStatus);//表状态字：2字节
			meterMap.put("signal", signal);//信号强度
			meterMap.put("pressure", pressure);//压力值：xx.yyyy
			
			meterMapList.add(meterMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return "iot/meter_table";
    	
    }
}
