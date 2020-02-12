package com.learnbind.ai.controller.iot;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.iot.protocol.util.ByteUtil;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.model.iot.MeterConfigBean;
import com.learnbind.ai.model.iot.MeterDataBaseBean;
import com.learnbind.ai.model.iot.MeterReportBean;
import com.learnbind.ai.model.iot.MeterStatusBean;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.service.iot.ICommandService;
import com.learnbind.ai.service.iot.IDeviceService;
import com.learnbind.ai.service.iot.IMeterService;
import com.learnbind.ai.service.iot.WmDeviceService;

@Controller
@RequestMapping("/meter")
@EnableAsync
public class MeterController {

    @Autowired
    IMeterService meterService;
    @Autowired
    IDeviceService deviceService;
    @Autowired
    private WmDeviceService wmDeviceService;
    @Autowired
    ICommandService commandService;

    @RequestMapping(value = "/uploadDeviceData", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadDeviceData(@RequestBody String data) {
        System.out.println("收到设备消息:"+data);
        //TODO 水表数据解析
        MeterBean meterBean = MeterBean.fromUploadDataJson(data);
        //TODO 水表数据保存
        JsonResult jsonResult = meterService.save(meterBean);//JsonResult.success(JsonResult.SUCCESS, data);
        
        MeterDataBaseBean meterDataBaseBean = new MeterDataBaseBean();
        try {
			meterDataBaseBean = new MeterDataBaseBean();
			meterDataBaseBean.setType(meterBean.getDataType());
			meterDataBaseBean.setData(meterBean.getData());
			meterDataBaseBean.setDataBasic(meterBean.getDataBasic());
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
	        if (reportBean != null) {
	        	MeterConfigBean configBean = null;
	        	if (deviceBean != null && deviceBean.getMeterConfig() != null) {
	        		configBean = MeterConfigBean.fromJson(deviceBean.getMeterConfig());
	        	}
		        if (configBean == null) {
					configBean = new MeterConfigBean();
				}
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
		}
        
        int type = meterDataBaseBean.getType();
        if (type == MeterDataBaseBean.METER_DATA_TYPE_REPORT || type == MeterDataBaseBean.METER_DATA_TYPE_CONFIG || type == MeterDataBaseBean.METER_DATA_TYPE_MONTH_FREEZE) {
        	//TODO G11 查询待下发指令逻辑
            List<CommandBean> unSentList = checkUnSentCommands(meterBean.getDeviceId());
            //TODO G11 逐条发送指令（异步发送）,并将状态设置为PENDING
            for (CommandBean commandBean : unSentList) {
            	//发送指令
            	commandService.postAsynCommand(commandBean);
            	//更新状态位PENDING
            	commandBean.setDatabaseStatus(1);
            	commandService.update(commandBean);
			}
            //TODO 根据请求结果，更新指令状态，不管什么状态，不再重新发送
            
		}
        
        jsonResult.setData(data);
        return ResponseEntity.ok(jsonResult.toString());
    }
    
    /**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return "iot/wm_meter/meter_main";
	}
    
    /**
     * @Title: searchMeters
     * @Description: 查询上报数据列表
     * @param model
     * @param pageNum
     * @param pageSize
     * @return 
     */
    @RequestMapping(value = "/search-meters")
    public String searchMeters(Model model, Integer pageNum, Integer pageSize, Integer searchDataType, String searchCond) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = 5;//PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<WmMeter> meterList = meterService.searchList(searchDataType, searchCond);
		PageInfo<WmMeter> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)

		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(WmMeter meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);//实体转MAP
			
			Long deviceId = meter.getDeviceId();//设备（水表）表主键ID
			
			WmDevice device = wmDeviceService.selectByPrimaryKey(deviceId);//查询水表信息
			meterMap.put("verifyCode", device.getVerifyCode());
			
			String meterData = meter.getMeterData();
			MeterReportBean meterReportBean = null;
			Integer dataType = null;

			MeterDataBaseBean meterDataBaseBean = new MeterDataBaseBean();//MeterDataBaseBean.fromJson(meterData);
			try {
				meterDataBaseBean = new MeterDataBaseBean();
				meterDataBaseBean.setType(meter.getMeterDataType());
				dataType = meterDataBaseBean.getType();//赋值表计数据类型：0=未知类型数据；1=设备上报数据；2=设备配置信息数据；3=设备月冻结数据；
				meterDataBaseBean.setData(meter.getMeterData());
				meterDataBaseBean.setDataBasic(meter.getMeterDataBasic());
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(StringUtils.isNotBlank(meterData)) {
				if(meterData.startsWith("{") && meterData.endsWith("}")) {
					//TODO G11 meter_data字段,增加多种类型数据，根据type判断数据类型
					if (meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_REPORT || meterDataBaseBean.getType() == MeterDataBaseBean.METER_DATA_TYPE_CONFIG) {
						meterReportBean = MeterReportBean.fromJson(meterDataBaseBean.getData());
					}
				} else {
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
			BigDecimal totalVolumeBd = null;//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			String sampleUnit = "";//采样参数：单位M3
			Integer batteryVoltage = null;//电池电压：单位V
			MeterStatusBean meterStatus = null;//表状态字：2字节
			String signal = "";//信号强度
			String pressure = "";//压力值：xx.yyyy
			if(meterReportBean!=null) {
				meterNumber = meterReportBean.getMeterNumber();//表号: 6字节数字型字符串
				Date meterTimeD = meterReportBean.getMeterTime();//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
				int totalVolume = meterReportBean.getTotalVolume();//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
				meterTime = sdf.format(meterTimeD);//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
				totalVolume = meterReportBean.getTotalVolume();//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
				sampleUnit = meterReportBean.getSampleUnit();//采样参数：单位M3
				batteryVoltage = meterReportBean.getBatteryVoltage();//电池电压：单位V
				//meterStatus = MeterStatusBean.toJsonString(meterReportBean.getMeterStatus());//表状态字：2字节
				meterStatus = meterReportBean.getMeterStatus();//表状态字：2字节
				signal = meterReportBean.getSignal();//信号强度
				pressure = meterReportBean.getPressure();//压力值：xx.yyyy
				totalVolumeBd = BigDecimalUtils.multiply(new BigDecimal(totalVolume), new BigDecimal(sampleUnit));//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			}
			
			meterMap.put("dataType", dataType);
			meterMap.put("meterNumber", meterNumber);//表号: 6字节数字型字符串
			meterMap.put("meterTime", meterTime);//表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
			meterMap.put("totalVolumeBd", totalVolumeBd);//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			meterMap.put("sampleUnit", sampleUnit);//采样参数：单位M3
			meterMap.put("batteryVoltage", batteryVoltage);//电池电压：单位V
			meterMap.put("meterStatus", meterStatus);//表状态字：2字节
			meterMap.put("signal", signal);//信号强度
			meterMap.put("pressure", pressure);//压力值：xx.yyyy
			
			//TODO G11  数据标识由short转为hexString，方便显示
			try {
				String dataDiHex = HexStringUtils.bytesToHexString(ByteUtil.getBytes(Short.parseShort(meter.getDataDi())));
				meterMap.put("dataDi",dataDiHex);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			meterMapList.add(meterMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return "iot/wm_meter/meter_table";
    	
    }
    public List<CommandBean> checkUnSentCommands(String deviceId) {
    	//1、查询根据deviceId查询数据库，查询该设备未下发指令，即status=0的指令，然后逐条下发到电信平台，如果电信平台有回复，不管成功与否，记录结果，不再下发，如果未下发成功，下次再次下发
        //2、status0,未下发，负数为发送失败，1，正在发送到电信平台，2已发送到电信平台，>=3电信平台执行结果，（另需计时并记录是否超时）
        //3、
//    	List<WmCommand> commandList = commandService.searchByDeviceId(deviceId);
    	List<Map<String, Object>> commandMapList = commandService.searchByDeviceId(deviceId);
    	List<CommandBean> unSentList = new ArrayList<>();
    	System.out.println("---------------------------");
    	System.out.println("| 待下发指令      设备ID:" + deviceId);
    	if (commandMapList != null) {
			for (Map<String, Object> wmCommand : commandMapList) {
				CommandBean commandBean = new CommandBean();
				commandBean.setId(((BigDecimal) wmCommand.get("ID")).longValue());
				commandBean.setDeviceId((String) wmCommand.get("DEVICE_IDS"));
				commandBean.setServiceId((String) wmCommand.get("SERVICE_NAME"));
				commandBean.setMethod("JRprotocolYX");
				commandBean.setParas(JSON.parseObject((String)wmCommand.get("METHOD_PARAMS")));
                commandBean.setMethodParams(commandBean.getParas().toJSONString());
                commandBean.setDatabaseStatus(((BigDecimal) wmCommand.get("STATUS")).intValue());
				
				if (commandBean.getDatabaseStatus() == 0) {
					unSentList.add(commandBean);
					System.out.println("| "+commandBean.getCommandId());
				}
			}
		}

    	System.out.println("| 总计："+unSentList.size());
    	System.out.println("---------------------------");
        return unSentList;
    }
}
