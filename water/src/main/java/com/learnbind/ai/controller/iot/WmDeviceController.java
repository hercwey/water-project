package com.learnbind.ai.controller.iot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.iot.protocol.CommandGenerator;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.bean.MeterConfigWriteCmd;
import com.learnbind.ai.iot.protocol.bean.MeterValveControlCmd;
import com.learnbind.ai.iot.protocol.bean.MeterVolumeThresholdCmd;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.service.iot.WmDeviceService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.controller.iot
 *
 * @Title: WmDeviceController.java
 * @Description: 智慧水务平台水表设备前端控制器
 *
 * @author SRD
 * @date 2020年2月6日 上午11:39:36
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/wm-device")
public class WmDeviceController {
	
	private static Log log = LogFactory.getLog(WmDeviceController.class);
	private static final String TEMPLATE_PATH = "iot/wm_device/"; // 页面目录
	private static final int PAGE_SIZE = 5;//每页显示记录条数
	
	@Autowired
	private WmDeviceService wmDeviceService;//智慧水务平台测试水表服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "device_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "device_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		Example example = new Example(WmDevice.class);
		Criteria criteria = example.createCriteria();
		if(StringUtils.isNotBlank(searchCond)) {
			criteria.andLike("verifyCode", "%"+searchCond+"%");
		}
		example.setOrderByClause(" ID DESC");
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<WmDevice> deviceList = wmDeviceService.selectByExample(example);
		PageInfo<WmDevice> pageInfo = new PageInfo<>(deviceList);//(使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("deviceList", deviceList);//列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "device_table";
	}

	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long itemId) {
		
		WmDevice device = wmDeviceService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem", device);
		
		return TEMPLATE_PATH + "device_dialog_edit";
	}

	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param device
	 * @return 
	 */
	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(WmDevice device) {
		device.setCreateTime(new Date());//设置增加时间
		int row = wmDeviceService.insertSelective(device);
		if (row > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: update
	 * @Description: 修改
	 * @param device
	 * @return 
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(WmDevice device) {
		int rows = wmDeviceService.updateByPrimaryKeySelective(device);
		if (rows>0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	
	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				wmDeviceService.deleteByPrimaryKey(id);
			}
		} catch (Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	/**
	 * @Title: cmdGenerator
	 * @Description: 生成指令
	 * @param id
	 * @param cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；
	 * @param cmdAction	
	 * @return
	 */
	@RequestMapping(value = "/cmd-generator")
	@ResponseBody
	public Object cmdGenerator(Long id, Integer cmdType, String cmdAction) {
		try {
			WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Integer meterType = device.getMeterType();//表类型
			String meterAddress = device.getMeterAddress();//表地址
			String meterFactoryCode = device.getMeterFactoryCode();//表厂商
			Integer sequence = device.getMeterSequence();//序号
			
			//----------测试时使用，正常使用时需要注释----------
	    	if(StringUtils.isBlank(meterAddress)) {
	    		meterAddress = "1505900569";
	    	}
	    	if(StringUtils.isBlank(meterFactoryCode)) {
	    		meterFactoryCode = "7833";
	    	}
	    	//----------
			
			if(StringUtils.isBlank(cmdAction) || meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
			
			log.debug("----------生成指令动作："+cmdAction);
			
			//生成开/关阀指令
			String command = null;
			//指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；
			if(cmdType==1) {//1=水表配置指令；
				//生成水表配置指令
				command = this.generatorMeterConfigCommand(meterType.byteValue(), meterAddress, meterFactoryCode, sequence.byteValue(), cmdAction);
			}else if(cmdType==2) {//2=开/关阀指令；
				//生成开/关阀指令
				command = this.generatorOpenCloseCommand(meterType.byteValue(), meterAddress, meterFactoryCode, sequence.byteValue(), Integer.valueOf(cmdAction));
			}else if(cmdType==3) {//3=水量阀值指令；
				//生成水量阀值指令
				command = this.generatorWaterAmountCommand(meterType.byteValue(), meterAddress, meterFactoryCode, sequence.byteValue(), Integer.valueOf(cmdAction));
			}
			
	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
	    	resultMap.put("command", command);
	    	return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成指令异常！");

	}
	
	/**
	 * @Title: generatorMeterConfigCommand
	 * @Description: 生成水表配置指令
	 * @param meterType
	 * @param meterAddress
	 * @param meterFactoryCode
	 * @param sequence
	 * @param cmdAction
	 * @return 
	 */
	private String generatorMeterConfigCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, String cmdAction) {
		
		JSONObject action = JSON.parseObject(cmdAction);
		short reportPeriod = action.getInteger("reportPeriod").shortValue();//定时上传周期
		byte reportPeriodUnit = action.getInteger("reportPeriodUnit").byteValue();//定时上传周期单位
		short reportRation = action.getInteger("reportRation").shortValue();//定量上传值
		byte temporaryTime = action.getInteger("temporaryTime").byteValue();//用户临时开阀用水限定时间
		byte valveRunTime = action.getInteger("valveRunTime").byteValue();//阀门行程时间
		short valveMaintainPeriod = action.getInteger("valveMaintainPeriod").shortValue();//阀门维护周期
		Integer basicValue = action.getInteger("meterBasicValue");//表底数
		Float sampleUnit = action.getFloat("sampleUnit");//采样参数单位
		String meterNumber = action.getString("meterNumber");//表号
		String meterTime = action.getString("meterTime");//表当前时间
		
		MeterConfig meterConfig = new MeterConfig();
        meterConfig.setReportPeriod(reportPeriod);//定时上传周期
        meterConfig.setReportPeriodUnit(reportPeriodUnit);//定时上传周期单位
        meterConfig.setReportRation(reportRation);//定量上传值
        meterConfig.setTemporaryTime(temporaryTime);//用户临时开阀用水限定时间
        meterConfig.setValveRunTime(valveRunTime);//阀门行程时间
        meterConfig.setValveMaintainPeriod(valveMaintainPeriod);//阀门维护周期
        meterConfig.setMeterBasicValue(basicValue);// 表底数 TODO 数据类型
        meterConfig.setSampleUnit(sampleUnit);//采样参数单位
        meterConfig.setMeterNumber(meterNumber);//表号
        meterConfig.setMeterTime(meterTime);//表当前时间 格式：20200101235959
        meterConfig.setServerIp("10.88.192.11"); // 服务器IP地址
        meterConfig.setServerPort((short) 6538); // 服务器端口

        MeterConfigWriteCmd cmd = new MeterConfigWriteCmd();
        cmd.setConfigFlag((short)(MeterConfig.FLAG_PERIOD |
                MeterConfig.FLAG_PERIOD |
                MeterConfig.FLAG_PERIOD_UNIT |
                MeterConfig.FLAG_MAX_REPORT |
                MeterConfig.FLAG_EMERG_TIME |
                MeterConfig.FLAG_VALVE_TIME |
                MeterConfig.FLAG_VALVE_MAINTAIN_TIME |
                MeterConfig.FLAG_WATER_VLOUME |
                MeterConfig.FLAG_SAMPLE_UNIT |
                MeterConfig.FLAG_METER_NUM |
                MeterConfig.FLAG_METER_TIME |
                MeterConfig.FLAG_SERVER_IP |
                MeterConfig.FLAG_SERVER_PORT));
        cmd.setConfig(meterConfig);

//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}
	
	/**
	 * @Title: generatorOpenCloseCommand
	 * @Description: 生成开关阀指令
	 * @param meterType
	 * @param meterAddress
	 * @param meterFactoryCode
	 * @param sequence
	 * @param cmdAction
	 * @return 
	 */
	private String generatorOpenCloseCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, Integer cmdAction) {
		byte action = MeterValveControlCmd.VALVE_CLOSE;//关阀
		//cmdAction 1=关阀;2=开阀;3=打开50%;4=打开25%;5=打开12.5%;
		if(cmdAction==1) {
			action = MeterValveControlCmd.VALVE_CLOSE;//关阀
		}else if(cmdAction==2) {
			action = MeterValveControlCmd.VALVE_OPEN;//开阀
		}else if(cmdAction==3) {
			action = MeterValveControlCmd.VALVE_OPEN_PER_50;//打开50%
		}else if(cmdAction==4) {
			action = MeterValveControlCmd.VALVE_OPEN_PER_25;//打开25%
		}else if(cmdAction==5) {
			action = MeterValveControlCmd.VALVE_OPEN_PER_12_5;//打开12.5%
		}
		
		MeterValveControlCmd cmd = new MeterValveControlCmd();
    	cmd.setAction(action);

//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}
	
	/**
	 * @Title: generatorWaterAmountCommand
	 * @Description: 生成水量阀值指令
	 * @param meterType
	 * @param meterAddress
	 * @param meterFactoryCode
	 * @param sequence
	 * @param cmdAction
	 * @return 
	 */
	private String generatorWaterAmountCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, Integer cmdAction) {
		
		MeterVolumeThresholdCmd cmd = new MeterVolumeThresholdCmd();
    	cmd.setThreshold(cmdAction.shortValue());

//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}

}