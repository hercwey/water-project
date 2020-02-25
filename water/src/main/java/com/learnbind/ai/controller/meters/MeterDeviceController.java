package com.learnbind.ai.controller.meters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.iot.protocol.CommandGenerator;
import com.learnbind.ai.iot.protocol.Protocol;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.bean.MeterConfigReadCmd;
import com.learnbind.ai.iot.protocol.bean.MeterConfigWriteCmd;
import com.learnbind.ai.iot.protocol.bean.MeterReadWaterCmd;
import com.learnbind.ai.iot.protocol.bean.MeterValveControlCmd;
import com.learnbind.ai.iot.protocol.bean.MeterVolumeThresholdCmd;
import com.learnbind.ai.model.iot.MeterConfigBean;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.model.iotbean.command.BaseCommandRequest;
import com.learnbind.ai.model.iotbean.command.ConfigParamsRequest;
import com.learnbind.ai.model.iotbean.command.ConfigThresholdRequest;
import com.learnbind.ai.model.iotbean.command.ControlValveRequest;
import com.learnbind.ai.model.iotbean.command.QueryMonthDataRequest;
import com.learnbind.ai.model.iotbean.common.DeviceParams;
import com.learnbind.ai.model.iotbean.common.DeviceParamsFlags;
import com.learnbind.ai.model.iotbean.device.RegisterDeviceRequest;
import com.learnbind.ai.model.iotbean.report.MeterStatusBean;
import com.learnbind.ai.mq.south.ConfigParmsProducer;
import com.learnbind.ai.mq.south.ConfigThresholdProducer;
import com.learnbind.ai.mq.south.ControlValveProducer;
import com.learnbind.ai.mq.south.DeviceRegisterProducer;
import com.learnbind.ai.mq.south.QueryMonthDataProducer;
import com.learnbind.ai.mq.south.QueryParmsProducer;
import com.learnbind.ai.service.iot.WmDeviceService;
import com.learnbind.ai.service.meters.MetersService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterDocController.java
 * @Description: 表计-单据管理
 *
 * @author Thinkpad
 * @date 2019年10月22日 上午11:58:44
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-device")
public class MeterDeviceController {
	private static Log log = LogFactory.getLog(MeterDeviceController.class);
	private static final String TEMPLATE_PATH = "meters/meter_device/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小
	private static final String SERVICE_ID = "SERVICE_ID"; // 页大小
	private static final String METHOD = "METHOD"; // 页大小

	
	@Autowired
	private MetersService metersService;  //表计档案
	@Autowired
	private DeviceRegisterProducer deviceRegisterProducer;  //表计注册接口
	@Autowired
	private WmDeviceService wmDeviceService;//智慧水务平台测试水表服务
	@Autowired
	private QueryMonthDataProducer queryMonthDataProducer;//月冻结数据命令
	@Autowired
	private QueryParmsProducer queryParmsProducer;//查询读表配置参数数据命令
	@Autowired
	private ConfigThresholdProducer configThresholdProducer;//设置阈值返回数据
	@Autowired
	private ControlValveProducer controlValveProducer;//下发控制设备（开关阀控制）命令
	@Autowired
	private ConfigParmsProducer configParmsProducer;//配置水表参数
	


	/**
	 * @Title: register
	 * @Description: 水表注册
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/register", produces = "application/json")
	public Object register(String dataJson) throws Exception {
		//接收前台传递参数
		JSONObject jsonObj=JSON.parseObject(dataJson);
		String verifyCode=jsonObj.getString("verifyCode");
		String manufacturerId=jsonObj.getString("manufacturerId");
		String manufacturerName=jsonObj.getString("manufacturerName");
		String model=jsonObj.getString("model");
		String protocolType=jsonObj.getString("protocolType");
		String deviceType=jsonObj.getString("deviceType");
		//将数据封装
		RegisterDeviceRequest request = new RegisterDeviceRequest();
		request.setVerifyCode(verifyCode);
		request.setNodeId(verifyCode);
		request.setTimeout(0);
		
		//调用注册服务接口
		String oJson = JSON.toJSONString(request);
		SendResult sendResult = deviceRegisterProducer.sendMsg(null, oJson);
		return sendResult;
		//return RequestResultUtil.getResultSaveWarn("注册成功！");

	}
	
	//----------------加载发送指令对话框部分----------------------------------------------------------------------------------
	/**
	 * @Title: loadSendCmdWaterAmountDialog
	 * @Description: 水量阀值指令对话框
	 * @param model
	 * @param itemId
	 * @param cmdType
	 * @return 
	 */
	@RequestMapping(value = "/load-send-cmd-water-amount-dialog")
	public String loadSendCmdWaterAmountDialog(Model model, Long itemId, Integer cmdType) {
		
		String htmlPath = "meters/meter_device/command_meter_config_dialog";//默认为表配置对话框
		System.out.println("----------加载水量阀值指令对话框");
		htmlPath = "meters/meter_device/command_water_amount_dialog";//默认为表配置对话框
		
		return htmlPath;
	}
	
	//----------------加载发送指令对话框部分----------------------------------------------------------------------------------
	@RequestMapping(value = "/load-send-cmd-open-close-dialog")
	public String loadSendCmdOpenCloseDialog(Model model, Long itemId, Integer cmdType) {
		
		String htmlPath = "meters/meter_device/command_meter_config_dialog";//默认为表配置对话框
		System.out.println("----------加载开/关阀指令对话框");
		htmlPath = "meters/meter_device/command_openclose_dialog";//默认为表配置对话框
		return htmlPath;
	}
	
	//----------------加载发送指令对话框部分----------------------------------------------------------------------------------
	@RequestMapping(value = "/load-send-cmd-meter-config-dialog")
	public String loadSendCmdMeterConfigDialog(Model model, Long itemId, Integer cmdType) {
		
		String htmlPath = "meters/meter_device/command_meter_config_dialog";//默认为表配置对话框
		//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
		System.out.println("----------加载水表配置指令对话框");
		htmlPath = "meters/meter_device/command_meter_config_dialog";//默认为表配置对话框
		
		WmDevice device = wmDeviceService.selectByPrimaryKey(itemId);
		MeterConfigBean meterConfigBean = null;
		if(device!=null) {
			
			String meterConfig = device.getMeterConfig();//表配置信息
			meterConfigBean = MeterConfigBean.fromJson(meterConfig);
		}
		model.addAttribute("meterConfigBean", meterConfigBean);
			
		return htmlPath;
	}
	
	//----------------生成指令部分----------------------------------------------------------------------------------
	/**
	 * @Title: cmdFreezeGenerator
	 * @Description: 读月冻结指令
	 * @param id
	 * @param cmdType
	 * @param cmdAction
	 * @return 
	 */
	@RequestMapping(value = "/cmd-freeze-generator")
	@ResponseBody
	public Object cmdFreezeGenerator(String deviceId, Integer cmdType, String cmdAction) throws Exception{
		try {
			Long id = Long.valueOf(deviceId);
			WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			
			Integer meterType = device.getMeterType();//表类型
			String meterAddress = device.getMeterAddress();//表地址
			String meterFactoryCode = device.getMeterFactoryCode();//表厂商
			Integer sequence = device.getMeterSequence();//序号
			
			//TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
	        if (sequence<255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
	        device.setMeterSequence(sequence);
	        wmDeviceService.updateByPrimaryKeySelective(device);
			
			//----------测试时使用，正常使用时需要注释----------
	    	if(StringUtils.isBlank(meterAddress)) {
	    		meterAddress = "1505900569";
	    	}
	    	if(StringUtils.isBlank(meterFactoryCode)) {
	    		meterFactoryCode = "5843";
	    	}
	    	//----------
			
			//if(StringUtils.isBlank(cmdAction) || meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
	    	if(meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
	    	//如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
	    	//或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
	    	if((StringUtils.isBlank(cmdAction) )) {
	    		log.debug("----------允许生成指令类型："+cmdType+"，指令动作："+cmdAction);
	    	}else {
	    		return RequestResultUtil.getResultFail("参数错误！");
	    	}
			
	    	log.debug("----------继续生成指令类型："+cmdType+"，指令动作："+cmdAction);
			
//		    	<option value="10">冷水表</option>
//				<option value="11">生活热水表</option>
//				<option value="12">直饮水水表</option>
//				<option value="13">中水水表</option>
	    	byte meterTypeB = 0x00;
	    	if(meterType==10) {
	    		meterTypeB = Protocol.METER_TYPE_10H;
	    	}else if(meterType==11) {
	    		meterTypeB = Protocol.METER_TYPE_11H;
	    	}else if(meterType==12) {
	    		meterTypeB = Protocol.METER_TYPE_12H;
	    	}else if(meterType==13) {
	    		meterTypeB = Protocol.METER_TYPE_13H;
	    	}
	    	
	    	byte sequenceB = 0x00;
	    	
			//生成开/关阀指令
			String command = null;
			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
			//4=读月冻结指令；
			System.out.println("----------生成读月冻结指令");
			//生成读月冻结指令
			command = this.generatorReadMonthFreezeCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), deviceId);
			
	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
	    	resultMap.put("command", command);
	    	return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成指令异常！");

	}
	
	/**
	 * @Title: cmdReadGenerator
	 * @Description: 生成读表配置指令
	 * @param deviceId
	 * @param cmdType
	 * @param cmdAction
	 * @return 
	 */
	@RequestMapping(value = "/cmd-read-generator")
	@ResponseBody
	public Object cmdReadGenerator(String deviceId, Integer cmdType, String cmdAction) {
		try {
			Long id = Long.valueOf(deviceId);
			WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			
			Integer meterType = device.getMeterType();//表类型
			String meterAddress = device.getMeterAddress();//表地址
			String meterFactoryCode = device.getMeterFactoryCode();//表厂商
			Integer sequence = device.getMeterSequence();//序号
			
			//TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
	        if (sequence<255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
	        device.setMeterSequence(sequence);
	        wmDeviceService.updateByPrimaryKeySelective(device);
			
			//----------测试时使用，正常使用时需要注释----------
	    	if(StringUtils.isBlank(meterAddress)) {
	    		meterAddress = "1505900569";
	    	}
	    	if(StringUtils.isBlank(meterFactoryCode)) {
	    		meterFactoryCode = "5843";
	    	}
	    	//----------
			
			//if(StringUtils.isBlank(cmdAction) || meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
	    	if(meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
	    	//如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
	    	//或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
	    	if((StringUtils.isBlank(cmdAction) )) {
	    		log.debug("----------允许生成指令类型："+cmdType+"，指令动作："+cmdAction);
	    	}else {
	    		return RequestResultUtil.getResultFail("参数错误！");
	    	}
			
	    	log.debug("----------继续生成指令类型："+cmdType+"，指令动作："+cmdAction);
			
//		    	<option value="10">冷水表</option>
//				<option value="11">生活热水表</option>
//				<option value="12">直饮水水表</option>
//				<option value="13">中水水表</option>
	    	byte meterTypeB = 0x00;
	    	if(meterType==10) {
	    		meterTypeB = Protocol.METER_TYPE_10H;
	    	}else if(meterType==11) {
	    		meterTypeB = Protocol.METER_TYPE_11H;
	    	}else if(meterType==12) {
	    		meterTypeB = Protocol.METER_TYPE_12H;
	    	}else if(meterType==13) {
	    		meterTypeB = Protocol.METER_TYPE_13H;
	    	}
	    	
	    	byte sequenceB = 0x00;
	    	
			//生成开/关阀指令
			String command = null;
			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
			System.out.println("----------生成读表配置指令");
			//读表配置指令
			command = this.generatorReadMeterConfigCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), deviceId);
			
	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
	    	resultMap.put("command", command);
	    	return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成指令异常！");

	}
	
	/**
	 * @Title: cmdWaterAmountGenerator
	 * @Description: 生成水量阀值指令
	 * @param deviceId
	 * @param cmdType
	 * @param cmdAction
	 * @return 
	 */
	@RequestMapping(value = "/cmd-water-amount-generator")
	@ResponseBody
	public Object cmdWaterAmountGenerator(String deviceId, Integer cmdType, String cmdAction) {
		try {
			Long id = Long.valueOf(deviceId);
			WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Integer meterType = device.getMeterType();//表类型
			String meterAddress = device.getMeterAddress();//表地址
			String meterFactoryCode = device.getMeterFactoryCode();//表厂商
			Integer sequence = device.getMeterSequence();//序号
			
			//TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
	        if (sequence<255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
	        device.setMeterSequence(sequence);
	        wmDeviceService.updateByPrimaryKeySelective(device);
			
			//----------测试时使用，正常使用时需要注释----------
	    	if(StringUtils.isBlank(meterAddress)) {
	    		meterAddress = "1505900569";
	    	}
	    	if(StringUtils.isBlank(meterFactoryCode)) {
	    		meterFactoryCode = "5843";
	    	}
	    	//----------
			
			//if(StringUtils.isBlank(cmdAction) || meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
	    	if(meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
	    	//如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
	    	//或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
//	    	if((StringUtils.isNotBlank(cmdAction))) {
//	    		log.debug("----------允许生成指令类型："+cmdType+"，指令动作："+cmdAction);
//	    	}else {
//	    		return RequestResultUtil.getResultFail("参数错误！");
//	    	}
			
	    	log.debug("----------继续生成指令类型："+cmdType+"，指令动作："+cmdAction);
			
//	    	<option value="10">冷水表</option>
//			<option value="11">生活热水表</option>
//			<option value="12">直饮水水表</option>
//			<option value="13">中水水表</option>
	    	byte meterTypeB = 0x00;
	    	if(meterType==10) {
	    		meterTypeB = Protocol.METER_TYPE_10H;
	    	}else if(meterType==11) {
	    		meterTypeB = Protocol.METER_TYPE_11H;
	    	}else if(meterType==12) {
	    		meterTypeB = Protocol.METER_TYPE_12H;
	    	}else if(meterType==13) {
	    		meterTypeB = Protocol.METER_TYPE_13H;
	    	}
	    	
	    	byte sequenceB = 0x00;
	    	
			//生成开/关阀指令
			String command = null;
			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
			System.out.println("----------生成水量阀值指令");
			//生成水量阀值指令
			command = this.generatorWaterAmountCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), Integer.valueOf(cmdAction), deviceId);
	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
	    	resultMap.put("command", command);
	    	return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成指令异常！");

	}
	
	/**
	 * @Title: cmdOpenCloseGenerator
	 * @Description: 生成开/关阀指令
	 * @param deviceId
	 * @param cmdType
	 * @param cmdAction
	 * @return 
	 */
	@RequestMapping(value = "/cmd-open-close-generator")
	@ResponseBody
	public Object cmdOpenCloseGenerator(String deviceId, Integer cmdType, String cmdAction) {
		try {
			Long id = Long.valueOf(deviceId);
			WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Integer meterType = device.getMeterType();//表类型
			String meterAddress = device.getMeterAddress();//表地址
			String meterFactoryCode = device.getMeterFactoryCode();//表厂商
			Integer sequence = device.getMeterSequence();//序号
			
			//TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
	        if (sequence<255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
	        device.setMeterSequence(sequence);
	        wmDeviceService.updateByPrimaryKeySelective(device);
			
			//----------测试时使用，正常使用时需要注释----------
	    	if(StringUtils.isBlank(meterAddress)) {
	    		meterAddress = "1505900569";
	    	}
	    	if(StringUtils.isBlank(meterFactoryCode)) {
	    		meterFactoryCode = "5843";
	    	}
	    	//----------
			
			//if(StringUtils.isBlank(cmdAction) || meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
	    	if(meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
	    	//如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
	    	//或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
	    	if((StringUtils.isNotBlank(cmdAction))) {
	    		log.debug("----------允许生成指令类型："+cmdType+"，指令动作："+cmdAction);
	    	}else {
	    		return RequestResultUtil.getResultFail("参数错误！");
	    	}
			
	    	log.debug("----------继续生成指令类型："+cmdType+"，指令动作："+cmdAction);
			
//	    	<option value="10">冷水表</option>
//			<option value="11">生活热水表</option>
//			<option value="12">直饮水水表</option>
//			<option value="13">中水水表</option>
	    	byte meterTypeB = 0x00;
	    	if(meterType==10) {
	    		meterTypeB = Protocol.METER_TYPE_10H;
	    	}else if(meterType==11) {
	    		meterTypeB = Protocol.METER_TYPE_11H;
	    	}else if(meterType==12) {
	    		meterTypeB = Protocol.METER_TYPE_12H;
	    	}else if(meterType==13) {
	    		meterTypeB = Protocol.METER_TYPE_13H;
	    	}
	    	
	    	byte sequenceB = 0x00;
	    	
			//生成开/关阀指令
			String command = null;
			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
			System.out.println("----------生成开/关阀指令");
			//生成水量阀值指令
			command = this.generatorOpenCloseCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), Integer.valueOf(cmdAction), deviceId);
	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
	    	resultMap.put("command", command);
	    	return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成指令异常！");

	}
	
	/**
	 * @Title: cmdMeterConfigGenerator
	 * @Description: 成水表配置指令
	 * @param deviceId
	 * @param cmdType
	 * @param cmdAction
	 * @return 
	 */
	@RequestMapping(value = "/cmd-meter-config-generator")
	@ResponseBody
	public Object cmdMeterConfigGenerator(String deviceId, Integer cmdType, String cmdAction) {
		try {
			Long id = Long.valueOf(deviceId);
			WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Integer meterType = device.getMeterType();//表类型
			String meterAddress = device.getMeterAddress();//表地址
			String meterFactoryCode = device.getMeterFactoryCode();//表厂商
			Integer sequence = device.getMeterSequence();//序号
			
			//TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
	        if (sequence<255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
	        device.setMeterSequence(sequence);
	        wmDeviceService.updateByPrimaryKeySelective(device);
			
			//----------测试时使用，正常使用时需要注释----------
	    	if(StringUtils.isBlank(meterAddress)) {
	    		meterAddress = "1505900569";
	    	}
	    	if(StringUtils.isBlank(meterFactoryCode)) {
	    		meterFactoryCode = "5843";
	    	}
	    	//----------
			
			//if(StringUtils.isBlank(cmdAction) || meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
	    	if(meterType==null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) || sequence==null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
	    	//如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
	    	//或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
	    	if((StringUtils.isNotBlank(cmdAction))) {
	    		log.debug("----------允许生成指令类型："+cmdType+"，指令动作："+cmdAction);
	    	}else {
	    		return RequestResultUtil.getResultFail("参数错误！");
	    	}
			
	    	log.debug("----------继续生成指令类型："+cmdType+"，指令动作："+cmdAction);
			
//	    	<option value="10">冷水表</option>
//			<option value="11">生活热水表</option>
//			<option value="12">直饮水水表</option>
//			<option value="13">中水水表</option>
	    	byte meterTypeB = 0x00;
	    	if(meterType==10) {
	    		meterTypeB = Protocol.METER_TYPE_10H;
	    	}else if(meterType==11) {
	    		meterTypeB = Protocol.METER_TYPE_11H;
	    	}else if(meterType==12) {
	    		meterTypeB = Protocol.METER_TYPE_12H;
	    	}else if(meterType==13) {
	    		meterTypeB = Protocol.METER_TYPE_13H;
	    	}
	    	
	    	byte sequenceB = 0x00;
	    	
			//生成开/关阀指令
			String command = null;
			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
			System.out.println("----------生成水表配置指令");
			//生成水表配置指令
			command = this.generatorMeterConfigCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), cmdAction, deviceId);
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
	private String generatorMeterConfigCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, String cmdAction, String deviceId) {
		
		JSONObject action = JSON.parseObject(cmdAction);
		String meterNumber = action.getString("meterNumber");//表号
		
		Boolean checkboxReportPeriod = action.getBoolean("checkboxReportPeriod");//是否配置定时上传周期
		Integer reportPeriod = action.getInteger("reportPeriod");//定时上传周期
		
		Boolean checkboxReportPeriodUnit = action.getBoolean("checkboxReportPeriodUnit");//是否配置定时上传周期单位
		Integer reportPeriodUnit = action.getInteger("reportPeriodUnit");//定时上传周期单位
		
		Boolean checkboxReportRation = action.getBoolean("checkboxReportRation");//是否配置定量上传值
		Integer reportRation = action.getInteger("reportRation");//定量上传值
		 
		Boolean checkboxTemporaryTime = action.getBoolean("checkboxTemporaryTime");//是否配置用户临时开阀用水限定时间
		Integer temporaryTime = action.getInteger("temporaryTime");//用户临时开阀用水限定时间
		
		Boolean checkboxValveRunTime = action.getBoolean("checkboxValveRunTime");//是否配置阀门行程时间
		Integer valveRunTime = action.getInteger("valveRunTime");//阀门行程时间
		
		Boolean checkboxValveMaintainPeriod = action.getBoolean("checkboxValveMaintainPeriod");//是否配置阀门维护周期
		Integer valveMaintainPeriod = action.getInteger("valveMaintainPeriod");//阀门维护周期
		
		Boolean checkboxMeterBasicValue = action.getBoolean("checkboxMeterBasicValue");//是否配置表底数
		Integer basicValue = action.getInteger("meterBasicValue");//表底数
		
		Boolean checkboxSampleUnit = action.getBoolean("checkboxSampleUnit");//是否配置采样参数单位
		Float sampleUnit = action.getFloat("sampleUnit");//采样参数单位
		
		Boolean checkboxMeterTime = action.getBoolean("checkboxMeterTime");//是否配置表当前时间
		Date meterTime = action.getDate("meterTime");//表当前时间 格式：yyyy-MM-dd HH:mm:ss
		
		Boolean checkboxMagneticAlarmOn = action.getBoolean("checkboxMagneticAlarmOn");//是否配置磁干扰告警消除
		Integer magneticAlarmOn = action.getInteger("magneticAlarmOn");//磁干扰告警消除
		
		Boolean checkboxMeterStatus = action.getBoolean("checkboxMeterStatus");//是否配置表状态字
		Integer meterStatusPeriod = action.getInteger("meterStatusPeriod");//表状态字-定时上传功能开关 (1开 / 0关)
		Integer meterStatusMaxReport = action.getInteger("meterStatusMaxReport");//表状态字-定量上传功能开关 (1开 / 0关)
		Integer meterStatusMagnetic = action.getInteger("meterStatusMagnetic");//表状态字-磁干扰关阀功能开关 (1开 / 0关)
		
		short meterStatusFlog = this.getMeterStatus(meterStatusPeriod, meterStatusMaxReport, meterStatusMagnetic);//获取表状态字
		
		MeterConfig meterConfig = new MeterConfig();
		
		
		short FLAG_DEFAULT = 0x0000;//默认值
		//参数修改标识
		short configFlag = (short)(FLAG_DEFAULT |
                MeterConfig.FLAG_METER_NUM |
                MeterConfig.FLAG_SERVER_IP |
                MeterConfig.FLAG_SERVER_PORT);
		meterConfig.setMeterNumber(meterNumber);//表号
		
		if(checkboxReportPeriod) {//是否配置定时上传周期
			meterConfig.setReportPeriod(reportPeriod.shortValue());//定时上传周期
			configFlag = (short)(configFlag | MeterConfig.FLAG_PERIOD);
		}
		if(checkboxReportPeriodUnit) {//是否配置定时上传周期单位
			meterConfig.setReportPeriodUnit(reportPeriodUnit.byteValue());//定时上传周期单位
			configFlag = (short)(configFlag | MeterConfig.FLAG_PERIOD_UNIT);
		}
		if(checkboxReportRation) {//是否配置定量上传值
			meterConfig.setReportRation(reportRation.shortValue());//定量上传值
			configFlag = (short)(configFlag | MeterConfig.FLAG_MAX_REPORT);
		}
		if(checkboxTemporaryTime) {//是否配置用户临时开阀用水限定时间
			meterConfig.setTemporaryTime(temporaryTime.byteValue());//用户临时开阀用水限定时间
			configFlag = (short)(configFlag | MeterConfig.FLAG_EMERG_TIME);
		}
		if(checkboxValveRunTime) {//是否配置阀门行程时间
			meterConfig.setValveRunTime(valveRunTime.byteValue());//阀门行程时间
			configFlag = (short)(configFlag | MeterConfig.FLAG_VALVE_TIME);
		}
		if(checkboxValveMaintainPeriod) {//是否配置阀门维护周期
			meterConfig.setValveMaintainPeriod(valveMaintainPeriod.shortValue());//阀门维护周期
			configFlag = (short)(configFlag | MeterConfig.FLAG_VALVE_MAINTAIN_TIME);
		}
		if(checkboxMeterBasicValue) {//是否配置表底数
			meterConfig.setMeterBasicValue(basicValue);// 表底数 TODO 数据类型
			configFlag = (short)(configFlag | MeterConfig.FLAG_WATER_VLOUME);
		}
		if(checkboxSampleUnit) {//是否配置采样参数单位
			meterConfig.setSampleUnit(sampleUnit);//采样参数单位
			configFlag = (short)(configFlag | MeterConfig.FLAG_SAMPLE_UNIT);
		}
		if(checkboxMeterTime) {//是否配置表当前时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//时间格式化
			String meterTimeStr = sdf.format(meterTime);
			meterConfig.setMeterTime(meterTimeStr);//表当前时间 格式：20200101235959
			configFlag = (short)(configFlag | MeterConfig.FLAG_METER_TIME);
		}
		if(checkboxMagneticAlarmOn) {//是否配置磁干扰告警消除
			if(magneticAlarmOn==1) {//如果magneticAlarmOn=1时表示磁干扰告警消除有效；magneticAlarmOn=0时表示磁干扰告警消除无效；有效时配置参数修改标识
				configFlag = (short)(configFlag | MeterConfig.FLAG_MAGNETIC_ALARM_CLEAR);
			}
		}
		if(checkboxMeterStatus) {//是否配置表状态字
			meterConfig.setMeterStatusFlag(meterStatusFlog);//表状态字：2字节，HEX格式
			configFlag = (short)(configFlag | MeterConfig.FLAG_METER_STATUS);
		}
        meterConfig.setServerIp("10.88.192.11"); // 服务器IP地址
        meterConfig.setServerPort((short) 6538); // 服务器端口

        MeterConfigWriteCmd cmd = new MeterConfigWriteCmd();
        //参数修改标识
//        cmd.setConfigFlag((short)(MeterConfig.FLAG_PERIOD |
//                //MeterConfig.FLAG_PERIOD |
//                MeterConfig.FLAG_PERIOD_UNIT |
//                MeterConfig.FLAG_MAX_REPORT |
//                MeterConfig.FLAG_EMERG_TIME |
//                MeterConfig.FLAG_VALVE_TIME |
//                MeterConfig.FLAG_VALVE_MAINTAIN_TIME |
//                MeterConfig.FLAG_WATER_VLOUME |
//                MeterConfig.FLAG_SAMPLE_UNIT |
//                MeterConfig.FLAG_METER_NUM |
//                MeterConfig.FLAG_METER_TIME |
//                MeterConfig.FLAG_METER_STATUS |
//                MeterConfig.FLAG_SERVER_IP |
//                MeterConfig.FLAG_SERVER_PORT));
        cmd.setConfigFlag(configFlag);
        cmd.setConfig(meterConfig);

        ConfigParamsRequest request = new ConfigParamsRequest();
        DeviceParamsFlags flag = new DeviceParamsFlags();
        flag.setPeriod(checkboxValveMaintainPeriod);
        flag.setPeriodUnit(checkboxReportPeriodUnit);
        flag.setMaxReport(checkboxReportRation);
        flag.setEmergTime(checkboxTemporaryTime);
        flag.setValveTime(checkboxValveRunTime);
        flag.setValveMaintainTime(checkboxValveRunTime);
        //flag.setWaterVolume(waterVolume);
        flag.setSampleUnit(checkboxSampleUnit);
        //flag.setMeterNum(meterNumber);
        flag.setMeterTime(checkboxMeterTime);
        flag.setMeterStatus(checkboxMeterStatus);
        //flag.setServerIp("10.88.192.11");
        //flag.setServerPort(serverPort);
        flag.setMagneticAlarmClear(checkboxMagneticAlarmOn);
        
        DeviceParams parms = new DeviceParams();
        parms.setReportPeriod(reportPeriod.shortValue());
        parms.setReportPeriodUnit(reportPeriodUnit.byteValue());
        parms.setReportRation(reportRation.shortValue());
        parms.setTemporaryTime(temporaryTime.byteValue());
        parms.setValveRunTime(valveRunTime.byteValue());
        parms.setValveMaintainPeriod(valveMaintainPeriod.shortValue());
        parms.setMeterBasicValue(basicValue);
        parms.setSampleUnit(sampleUnit);
        parms.setMeterNumber(meterNumber);
        parms.setMeterTime(meterTime);
        MeterStatusBean bean = new MeterStatusBean();
        bean.setBatteryLow(0);
        bean.setMagneticAlarmOn(0);
        bean.setMagneticOn(0);
        bean.setMaxReportOn(0);
        bean.setPeriodOn(0);
        bean.setSampleLineCut(0);
        bean.setValveAbnormal(0);
        bean.setValveOpen(0);
        parms.setMeterStatus(bean);
        parms.setServerIp("10.88.192.11");
        parms.setServerPort((short) 6538);
        parms.setConfigFlag(flag);
        request.setDeviceParams(parms);
        
        String requestJson = JSON.toJSONString(request);
        try {
        	configParmsProducer.sendMsg(null, requestJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}
	
	/**
	 * @Title: getMeterStatus
	 * @Description: 获取表状态字
	 * @param meterStatusPeriod
	 * @param meterStatusMaxReport
	 * @param meterStatusMagnetic
	 * @return 
	 */
	private short getMeterStatus(Integer meterStatusPeriod, Integer meterStatusMaxReport, Integer meterStatusMagnetic) {
		final int ON = 1;
		final int OFF = 0;
		short meterStatusFlag = 0;
		if(meterStatusPeriod==ON && meterStatusMaxReport==OFF) {
			meterStatusFlag = Protocol.METER_STATUS_PERIOD_ON;
		}else if(meterStatusPeriod==OFF && meterStatusMaxReport==ON) {
			meterStatusFlag = Protocol.METER_STATUS_MAX_REPORT_ON;
		}
		
		if(meterStatusMagnetic==1) {
			meterStatusFlag = (short)(meterStatusFlag | Protocol.METER_STATUS_MAGNETIC_ON);
		}
		
//		if(meterStatusPeriod==ON && meterStatusMaxReport==ON && meterStatusMagnetic==ON) {//表状态字-定时上传功能开,定量上传功能开,磁干扰关阀功能开
//			meterStatusFlag = (short)(Protocol.METER_STATUS_PERIOD_ON | Protocol.METER_STATUS_MAX_REPORT_ON | Protocol.METER_STATUS_MAGNETIC_ON);
//		}else if(meterStatusPeriod==ON && meterStatusMaxReport==OFF && meterStatusMagnetic==OFF) {//表状态字-定时上传功能开,定量上传功能关,磁干扰关阀功能关
//			meterStatusFlag = (short)(Protocol.METER_STATUS_PERIOD_ON | ~Protocol.METER_STATUS_MAX_REPORT_ON | ~Protocol.METER_STATUS_MAGNETIC_ON);
//		}else if(meterStatusPeriod==ON && meterStatusMaxReport==ON && meterStatusMagnetic==OFF) {//表状态字-定时上传功能开,定量上传功能开,磁干扰关阀功能关
//			meterStatusFlag = (short)(Protocol.METER_STATUS_PERIOD_ON | Protocol.METER_STATUS_MAX_REPORT_ON | ~Protocol.METER_STATUS_MAGNETIC_ON);
//		}else if(meterStatusPeriod==OFF && meterStatusMaxReport==ON && meterStatusMagnetic==ON) {//表状态字-定时上传功能关,定量上传功能开,磁干扰关阀功能开
//			meterStatusFlag = (short)(~Protocol.METER_STATUS_PERIOD_ON | Protocol.METER_STATUS_MAX_REPORT_ON | Protocol.METER_STATUS_MAGNETIC_ON);
//		}else if(meterStatusPeriod==OFF && meterStatusMaxReport==OFF && meterStatusMagnetic==ON) {//表状态字-定时上传功能关,定量上传功能关,磁干扰关阀功能开
//			meterStatusFlag = (short)(~Protocol.METER_STATUS_PERIOD_ON | ~Protocol.METER_STATUS_MAX_REPORT_ON | Protocol.METER_STATUS_MAGNETIC_ON);
//		}
		return meterStatusFlag;
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
	private String generatorOpenCloseCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, Integer cmdAction, String deviceId){
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
    	ControlValveRequest request = new ControlValveRequest();
    	request.setAction(action);;
    	request.setDeviceId(deviceId);
		request.setMeterAddress(meterAddress);
		request.setMeterFactoryCode(meterFactoryCode);
		request.setMeterType(meterType);
		request.setMethod(METHOD);
		request.setSequence(sequence);
		request.setServiceId(SERVICE_ID);
		String requestJson = JSON.toJSONString(request);
		try {
			controlValveProducer.sendMsg(null, requestJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	private String generatorWaterAmountCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, Integer cmdAction, String deviceId){
		
		MeterVolumeThresholdCmd cmd = new MeterVolumeThresholdCmd();
    	cmd.setThreshold(cmdAction.shortValue());
    	ConfigThresholdRequest request = new ConfigThresholdRequest();
    	request.setThreshold(cmdAction.shortValue());
    	request.setDeviceId(deviceId);
		request.setMeterAddress(meterAddress);
		request.setMeterFactoryCode(meterFactoryCode);
		request.setMeterType(meterType);
		request.setMethod(METHOD);
		request.setSequence(sequence);
		request.setServiceId(SERVICE_ID);
		String requestJson = JSON.toJSONString(request);
		try {
			configThresholdProducer.sendMsg(null, requestJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}
	
	/**
	 * @Title: generatorReadMonthFreezeCommand
	 * @Description: 生成读月冻结指令
	 * @param meterType
	 * @param meterAddress
	 * @param meterFactoryCode
	 * @param sequence
	 * @return 
	 * @throws Exception 
	 */
	private String generatorReadMonthFreezeCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, String deviceId){
		
		MeterReadWaterCmd cmd = new MeterReadWaterCmd();
		QueryMonthDataRequest request = new QueryMonthDataRequest();
		request.setDeviceId(deviceId);
		request.setMeterAddress(meterAddress);
		request.setMeterFactoryCode(meterFactoryCode);
		request.setMeterType(meterType);
		request.setMethod(METHOD);
		request.setSequence(sequence);
		request.setServiceId(SERVICE_ID);
		String requestJson = JSON.toJSONString(request);
		try {
			queryMonthDataProducer.sendMsg(null, requestJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}
	
	/**
	 * @Title: generatorReadMeterConfigCommand
	 * @Description: 生成读表配置指令
	 * @param meterType
	 * @param meterAddress
	 * @param meterFactoryCode
	 * @param sequence
	 * @return 
	 */
	private String generatorReadMeterConfigCommand(byte meterType, String meterAddress, String meterFactoryCode, byte sequence, String deviceId) throws Exception{
		
		MeterConfigReadCmd cmd = new MeterConfigReadCmd();
		BaseCommandRequest request = new BaseCommandRequest();
		request.setDeviceId(deviceId);
		request.setMeterAddress(meterAddress);
		request.setMeterFactoryCode(meterFactoryCode);
		request.setMeterType(meterType);
		request.setMethod(METHOD);
		request.setSequence(sequence);
		request.setServiceId(SERVICE_ID);
		String requestJson = JSON.toJSONString(request);
		queryParmsProducer.sendMsg(null, requestJson);
		
//    	byte meterType = meterTypeI.byteValue();
//    	byte sequence = sequenceI.byteValue();
    	
    	String command = CommandGenerator.generateCmd(meterType, meterAddress, meterFactoryCode, sequence, cmd);
    	return command;
	}

	
	
	
}