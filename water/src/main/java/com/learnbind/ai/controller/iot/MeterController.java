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
import com.learnbind.ai.model.iot.MeterConfigBean;
import com.learnbind.ai.model.iot.MeterDataBaseBean;
import com.learnbind.ai.model.iot.MeterReportBean;
import com.learnbind.ai.model.iot.MeterStatusBean;
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
        
        MeterDataBaseBean meterDataBaseBean = new MeterDataBaseBean();
        try {
			meterDataBaseBean = MeterDataBaseBean.fromJson(meterBean.getData());
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        if (meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_CONFIG) {
            //水表配置信息，更新数据库device表meter_config内容
        	DeviceBean deviceBean = new DeviceBean();
        	deviceBean.setDeviceId(meterBean.getDeviceId());
        	deviceBean = DeviceBean.fromWmDevice(deviceService.getDeviceByDeviceId(deviceBean));
        	
        	deviceBean.setMeterConfig(meterDataBaseBean.getData());
        	deviceService.modifyDevice(deviceBean);
		} else if (meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_MONTH_FREEZE) {
            //水表月冻结信息，更新数据库device表meter_freeze内容
			DeviceBean deviceBean = new DeviceBean();
        	deviceBean.setDeviceId(meterBean.getDeviceId());
        	deviceBean = DeviceBean.fromWmDevice(deviceService.getDeviceByDeviceId(deviceBean));
        	
        	deviceBean.setMeterFreeze(meterDataBaseBean.getData());
        	deviceService.modifyDevice(deviceBean);
		} else if (meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_REPORT) {
			//TODO 水表数据封装信息保存（更新MeterConfig信息）
	        DeviceBean deviceBean = new DeviceBean();
	        deviceBean.setDeviceId(meterBean.getDeviceId());
	        WmDevice wmDevice = deviceService.getDeviceByDeviceId(deviceBean);
	        if (wmDevice == null) {
				wmDevice = new WmDevice();
			}
	        deviceBean = DeviceBean.fromWmDevice(wmDevice);
	        MeterReportBean reportBean= MeterReportBean.fromJson(meterDataBaseBean.getData());
	        	
	        MeterConfigBean configBean = MeterConfigBean.fromJson(deviceBean.getMeterConfig());
	        configBean.setMeterNumber(reportBean.getMeterNumber());
	        configBean.setMeterTime(reportBean.getMeterTime());
	        configBean.setMeterStatus(reportBean.getMeterStatus());
	        try {
		        configBean.setSampleUnit(Float.valueOf(reportBean.getSampleUnit()));
			} catch (Exception e) {
				configBean.setSampleUnit(0);
			} finally {
				deviceBean.setMeterConfig(MeterConfigBean.toJsonString(configBean));
		        deviceService.modifyDevice(deviceBean);
			}
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
			MeterReportBean meterReportBean = null;
			Integer dataType = null;
			if(StringUtils.isNotBlank(meterData)) {
				if(meterData.startsWith("{") && meterData.endsWith("}")) {
					//TODO G11 meter_data字段,增加多种类型数据，根据type判断数据类型
					MeterDataBaseBean meterDataBaseBean = MeterDataBaseBean.fromJson(meterData);
					dataType = meterDataBaseBean.getType();//赋值表计数据类型：0=未知类型数据；1=设备上报数据；2=设备配置信息数据；3=设备月冻结数据；
					if (meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_REPORT || meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_CONFIG) {
						meterReportBean = MeterReportBean.fromJson(meterDataBaseBean.getData());
					}
				}else {
					try {
						meterReportBean = MeterReportBean.fromHexData(meterData);
					} catch (Exception e) {
						e.printStackTrace();
						meterReportBean = null;
						System.out.println("----------解析16进制数据异常（MeterDataBean.fromHexData），原16进制数据："+meterData);
					}
				}
			}
			
			String meterNumber = "";//表号: 6字节数字型字符串
			String meterTime = "";//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
			Integer totalVolume = null;//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			String sampleUnit = "";//采样参数：单位M3
			Integer batteryVoltage = null;//电池电压：单位V
			MeterStatusBean meterStatus = null;//表状态字：2字节
			String signal = "";//信号强度
			String pressure = "";//压力值：xx.yyyy
			if(meterReportBean!=null) {
				meterNumber = meterReportBean.getMeterNumber();//表号: 6字节数字型字符串
				meterTime = meterReportBean.getMeterTime();//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
				totalVolume = meterReportBean.getTotalVolume();//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
				sampleUnit = meterReportBean.getSampleUnit();//采样参数：单位M3
				batteryVoltage = meterReportBean.getBatteryVoltage();//电池电压：单位V
				//meterStatus = MeterStatusBean.toJsonString(meterReportBean.getMeterStatus());//表状态字：2字节
				meterStatus = meterReportBean.getMeterStatus();//表状态字：2字节
				signal = meterReportBean.getSignal();//信号强度
				pressure = meterReportBean.getPressure();//压力值：xx.yyyy
			}
			
			meterMap.put("dataType", dataType);
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
