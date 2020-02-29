package com.learnbind.ai.controller.meters;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.iot.EnumCommandType;
import com.learnbind.ai.iot.protocol.Protocol;
import com.learnbind.ai.iot.protocol.bean.MeterValveControlCmd;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.MeterConfigBean;
import com.learnbind.ai.model.iot.WmCommand;
import com.learnbind.ai.model.iotbean.command.BaseCommandRequest;
import com.learnbind.ai.model.iotbean.command.ConfigParamsRequest;
import com.learnbind.ai.model.iotbean.command.ConfigThresholdRequest;
import com.learnbind.ai.model.iotbean.command.ControlValveRequest;
import com.learnbind.ai.model.iotbean.command.QueryMonthDataRequest;
import com.learnbind.ai.model.iotbean.common.ControlValveType;
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
import com.learnbind.ai.service.iot.WmCommandService;
import com.learnbind.ai.service.meters.MetersService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterDeviceController.java
 * @Description: 水表设备IOT接口调用前端控制器
 *
 * @author SRD
 * @date 2020年3月1日 上午12:59:23
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-device")
public class MeterDeviceController {
	private static Log log = LogFactory.getLog(MeterDeviceController.class);
	private static final String TEMPLATE_PATH = "meters/meter_device/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小
	private static final String SERVICE_ID = "JRprotocol"; // 页大小
	private static final String METHOD = "JRprotocolYX"; // 页大小

	@Autowired
	private MetersService metersService; // 表计档案
	@Autowired
	private DeviceRegisterProducer deviceRegisterProducer; // 表计注册接口
	@Autowired
	private QueryMonthDataProducer queryMonthDataProducer;// 月冻结数据命令
	@Autowired
	private QueryParmsProducer queryParmsProducer;// 查询读表配置参数数据命令
	@Autowired
	private ConfigThresholdProducer configThresholdProducer;// 设置阈值返回数据
	@Autowired
	private ControlValveProducer controlValveProducer;// 下发控制设备（开关阀控制）命令
	@Autowired
	private ConfigParmsProducer configParmsProducer;// 配置水表参数
	@Autowired
	private WmCommandService wmCommandService;// 下发命令记录

	// ----------------加载水表配置对话框部分----------------------------------------------------------------------------------
	@RequestMapping(value = "/load-meter-config-detail-dialog")
	public String loadMeterConfigDetailDialog(Model model, Long itemId) {

		Meters device = metersService.selectByPrimaryKey(itemId);
		MeterConfigBean meterConfigBean = null;
		if (device != null) {

			String meterConfig = device.getMeterConfig();// 表配置信息
			// String meterFreeze = device.getMeterFreeze();//表月冻结数据
			meterConfigBean = MeterConfigBean.fromJson(meterConfig);
		}
		model.addAttribute("meterConfigBean", meterConfigBean);

		return TEMPLATE_PATH + "meter_config_detail_dialog";
	}

	/**
	 * @Title: register
	 * @Description: 水表注册
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/register", produces = "application/json")
	@ResponseBody
	public Object register(String dataJson) throws Exception {
		// 接收前台传递参数
		JSONObject jsonObj = JSON.parseObject(dataJson);
		String verifyCode = jsonObj.getString("verifyCode");
		String manufacturerId = jsonObj.getString("manufacturerId");
		String manufacturerName = jsonObj.getString("manufacturerName");
		String model = jsonObj.getString("model");
		String protocolType = jsonObj.getString("protocolType");
		String deviceType = jsonObj.getString("deviceType");
		// 将数据封装
		RegisterDeviceRequest request = new RegisterDeviceRequest();
		request.setVerifyCode(verifyCode);
		request.setNodeId(verifyCode);
		request.setTimeout(0);

		// 调用注册服务接口
		String oJson = JSON.toJSONString(request);
		SendResult sendResult = deviceRegisterProducer.sendMsg(null, oJson);
		if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
			return RequestResultUtil.getResultSuccess("设备注册请求已发送，刷新列表查看注册结果！");
		}
		// return sendResult;
		return RequestResultUtil.getResultFail("设备注册请求发送失败，请重新操作！");

	}

	// ----------------加载发送指令对话框部分----------------------------------------------------------------------------------
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

		String htmlPath = "meters/meter_device/command_meter_config_dialog";// 默认为表配置对话框
		System.out.println("----------加载水量阀值指令对话框");
		htmlPath = "meters/meter_device/command_water_amount_dialog";// 默认为表配置对话框

		return htmlPath;
	}

	// ----------------加载发送指令对话框部分----------------------------------------------------------------------------------
	@RequestMapping(value = "/load-send-cmd-open-close-dialog")
	public String loadSendCmdOpenCloseDialog(Model model, Long itemId, Integer cmdType) {

		String htmlPath = "meters/meter_device/command_meter_config_dialog";// 默认为表配置对话框
		System.out.println("----------加载开/关阀指令对话框");
		htmlPath = "meters/meter_device/command_openclose_dialog";// 默认为表配置对话框
		return htmlPath;
	}

	// ----------------加载发送指令对话框部分----------------------------------------------------------------------------------
	@RequestMapping(value = "/load-send-cmd-meter-config-dialog")
	public String loadSendCmdMeterConfigDialog(Model model, Long itemId, Integer cmdType) {

		String htmlPath = "meters/meter_device/command_meter_config_dialog";// 默认为表配置对话框
		// cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
		System.out.println("----------加载水表配置指令对话框");
		htmlPath = "meters/meter_device/command_meter_config_dialog";// 默认为表配置对话框

		// WmDevice device = wmDeviceService.selectByPrimaryKey(itemId);
		Meters device = metersService.selectByPrimaryKey(itemId);
		MeterConfigBean meterConfigBean = null;
		if (device != null) {

			String meterConfig = device.getMeterConfig();// 表配置信息
			meterConfigBean = MeterConfigBean.fromJson(meterConfig);
		}
		model.addAttribute("meterConfigBean", meterConfigBean);

		return htmlPath;
	}

	// ----------------生成指令部分----------------------------------------------------------------------------------
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
	public Object cmdFreezeGenerator(Long id, Integer cmdType, String cmdAction) throws Exception {
		try {
			// Long id = Long.valueOf(deviceId);
			// WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Meters device = metersService.selectByPrimaryKey(id);

			Integer meterType = Integer.valueOf(device.getMeterUseType());// 表类型
			String meterAddress = device.getMeterAddress();// 表地址
			String meterFactoryCode = device.getMeterFactoryCode();// 表厂商
			Integer sequence = device.getMeterSequence();// 序号

			// TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
			if (sequence < 255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
			device.setMeterSequence(sequence);
			metersService.updateByPrimaryKeySelective(device);
			// wmDeviceService.updateByPrimaryKeySelective(device);

			// ----------测试时使用，正常使用时需要注释----------
//	    	if(StringUtils.isBlank(meterAddress)) {
//	    		meterAddress = "1505900569";
//	    	}
//	    	if(StringUtils.isBlank(meterFactoryCode)) {
//	    		meterFactoryCode = "5843";
//	    	}
			// ----------

			// if(StringUtils.isBlank(cmdAction) || meterType==null ||
			// StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) ||
			// sequence==null) {
			if (meterType == null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode)
					|| sequence == null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
			// 如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
			// 或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
			if ((StringUtils.isBlank(cmdAction))) {
				log.debug("----------允许生成指令类型：" + cmdType + "，指令动作：" + cmdAction);
			} else {
				return RequestResultUtil.getResultFail("参数错误！");
			}

			log.debug("----------继续生成指令类型：" + cmdType + "，指令动作：" + cmdAction);

//		    	<option value="10">冷水表</option>
//				<option value="11">生活热水表</option>
//				<option value="12">直饮水水表</option>
//				<option value="13">中水水表</option>
			byte meterTypeB = 0x00;
			if (meterType == 10) {
				meterTypeB = Protocol.METER_TYPE_10H;
			} else if (meterType == 11) {
				meterTypeB = Protocol.METER_TYPE_11H;
			} else if (meterType == 12) {
				meterTypeB = Protocol.METER_TYPE_12H;
			} else if (meterType == 13) {
				meterTypeB = Protocol.METER_TYPE_13H;
			}

			byte sequenceB = 0x00;

			// 4=读月冻结指令；
			System.out.println("----------生成读月冻结指令");
			// 生成读月冻结指令
			// String command = this.generatorReadMonthFreezeCommand(meterTypeB,
			// meterAddress, meterFactoryCode, sequence.byteValue(), device.getDeviceId());
			// 获取指令对象
			WmCommand wmCommand = wmCommandService.getWmCommand(device, EnumCommandType.TYPE_READ_MONTH_FREEZE.getKey(),
					null);
			int rows = wmCommandService.insertSelective(wmCommand);// 增加命令记录
			if (rows > 0) {

				QueryMonthDataRequest queryMonthData = new QueryMonthDataRequest();
				queryMonthData.setDeviceId(device.getDeviceId());
				queryMonthData.setId(wmCommand.getId());
				queryMonthData.setMeterAddress(meterAddress);
				queryMonthData.setMeterFactoryCode(meterFactoryCode);
				queryMonthData.setMeterType(meterType.byteValue());
				queryMonthData.setMethod(METHOD);
				queryMonthData.setSequence(sequence.byteValue());
				queryMonthData.setServiceId(SERVICE_ID);

				SendResult sendResult = queryMonthDataProducer.sendMsg(null,
						queryMonthData.toJsonString(queryMonthData));
				if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
					// 发送成功
					return RequestResultUtil.getResultSuccess("发送指令成功，请到下发指令记录列表中查看结果！");
				}
			}
			// 发送失败
			return RequestResultUtil.getResultFail("发送指令失败，请重新操作！");
//	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
//	    	resultMap.put("command", command);
//	    	return resultMap;
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
	public Object cmdReadGenerator(Long id, Integer cmdType, String cmdAction) {
		try {
			// WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Meters device = metersService.selectByPrimaryKey(id);

			Integer meterType = Integer.valueOf(device.getMeterUseType());// 表类型
			String meterAddress = device.getMeterAddress();// 表地址
			String meterFactoryCode = device.getMeterFactoryCode();// 表厂商
			Integer sequence = device.getMeterSequence();// 序号

			// TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
			if (sequence < 255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
			device.setMeterSequence(sequence);
			metersService.updateByPrimaryKeySelective(device);
			// wmDeviceService.updateByPrimaryKeySelective(device);

			// ----------测试时使用，正常使用时需要注释----------
//	    	if(StringUtils.isBlank(meterAddress)) {
//	    		meterAddress = "1505900569";
//	    	}
//	    	if(StringUtils.isBlank(meterFactoryCode)) {
//	    		meterFactoryCode = "5843";
//	    	}
			// ----------

			// if(StringUtils.isBlank(cmdAction) || meterType==null ||
			// StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) ||
			// sequence==null) {
			if (meterType == null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode)
					|| sequence == null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
			// 如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
			// 或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
			if ((StringUtils.isBlank(cmdAction))) {
				log.debug("----------允许生成指令类型：" + cmdType + "，指令动作：" + cmdAction);
			} else {
				return RequestResultUtil.getResultFail("参数错误！");
			}

			log.debug("----------继续生成指令类型：" + cmdType + "，指令动作：" + cmdAction);

//		    	<option value="10">冷水表</option>
//				<option value="11">生活热水表</option>
//				<option value="12">直饮水水表</option>
//				<option value="13">中水水表</option>
			byte meterTypeB = 0x00;
			if (meterType == 10) {
				meterTypeB = Protocol.METER_TYPE_10H;
			} else if (meterType == 11) {
				meterTypeB = Protocol.METER_TYPE_11H;
			} else if (meterType == 12) {
				meterTypeB = Protocol.METER_TYPE_12H;
			} else if (meterType == 13) {
				meterTypeB = Protocol.METER_TYPE_13H;
			}

			byte sequenceB = 0x00;

			// 5=读表配置指令
			System.out.println("----------生成读表配置指令");
			// 读表配置指令
			// String command = this.generatorReadMeterConfigCommand(meterTypeB,
			// meterAddress, meterFactoryCode, sequence.byteValue(), device.getDeviceId());
			// 获取指令对象
			WmCommand wmCommand = wmCommandService.getWmCommand(device, EnumCommandType.TYPE_READ_METER_CONFIG.getKey(),
					null);
			int rows = wmCommandService.insertSelective(wmCommand);// 增加命令记录
			if (rows > 0) {

				BaseCommandRequest request = new BaseCommandRequest();
				request.setDeviceId(device.getDeviceId());
				request.setMeterAddress(meterAddress);
				request.setMeterFactoryCode(meterFactoryCode);
				request.setMeterType(meterType.byteValue());
				request.setMethod(METHOD);
				request.setSequence(sequence.byteValue());
				request.setServiceId(SERVICE_ID);

				SendResult sendResult = queryParmsProducer.sendMsg(null, request.toJsonString(request));
				if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
					// 发送成功
					return RequestResultUtil.getResultSuccess("发送读表配置指令成功，请到下发指令记录列表中查看结果！");
				}
			}
			// 发送失败
			return RequestResultUtil.getResultFail("发送读表配置指令失败，请重新操作！");
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
	public Object cmdWaterAmountGenerator(Long id, Integer cmdType, String cmdAction) {
		try {
			// Long id = Long.valueOf(deviceId);
			// WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Meters device = metersService.selectByPrimaryKey(id);
			Integer meterType = Integer.valueOf(device.getMeterUseType());// 表类型
			String meterAddress = device.getMeterAddress();// 表地址
			String meterFactoryCode = device.getMeterFactoryCode();// 表厂商
			Integer sequence = device.getMeterSequence();// 序号

			// TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
			if (sequence < 255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
			device.setMeterSequence(sequence);
			metersService.updateByPrimaryKeySelective(device);
			// wmDeviceService.updateByPrimaryKeySelective(device);

			// ----------测试时使用，正常使用时需要注释----------
//	    	if(StringUtils.isBlank(meterAddress)) {
//	    		meterAddress = "1505900569";
//	    	}
//	    	if(StringUtils.isBlank(meterFactoryCode)) {
//	    		meterFactoryCode = "5843";
//	    	}
			// ----------

			// if(StringUtils.isBlank(cmdAction) || meterType==null ||
			// StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) ||
			// sequence==null) {
			if (meterType == null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode)
					|| sequence == null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
			// 如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
			// 或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
//	    	if((StringUtils.isNotBlank(cmdAction))) {
//	    		log.debug("----------允许生成指令类型："+cmdType+"，指令动作："+cmdAction);
//	    	}else {
//	    		return RequestResultUtil.getResultFail("参数错误！");
//	    	}

			log.debug("----------继续生成指令类型：" + cmdType + "，指令动作：" + cmdAction);

//	    	<option value="10">冷水表</option>
//			<option value="11">生活热水表</option>
//			<option value="12">直饮水水表</option>
//			<option value="13">中水水表</option>
			byte meterTypeB = 0x00;
			if (meterType == 10) {
				meterTypeB = Protocol.METER_TYPE_10H;
			} else if (meterType == 11) {
				meterTypeB = Protocol.METER_TYPE_11H;
			} else if (meterType == 12) {
				meterTypeB = Protocol.METER_TYPE_12H;
			} else if (meterType == 13) {
				meterTypeB = Protocol.METER_TYPE_13H;
			}

			byte sequenceB = 0x00;
			// 5=水量阀值指令
			System.out.println("----------生成水量阀值指令");
			// 水量阀值指令
			// String command = this.generatorWaterAmountCommand(meterTypeB, meterAddress,
			// meterFactoryCode, sequence.byteValue(),Integer.valueOf(cmdAction),
			// device.getDeviceId());
			// 获取指令对象
			WmCommand wmCommand = wmCommandService.getWmCommand(device, EnumCommandType.TYPE_WATER_AMOUNT.getKey(),
					null);
			int rows = wmCommandService.insertSelective(wmCommand);// 增加命令记录
			if (rows > 0) {

				ConfigThresholdRequest request = new ConfigThresholdRequest();
				request.setThreshold((Integer.valueOf(cmdAction)).shortValue());
				request.setDeviceId(device.getDeviceId());
				request.setMeterAddress(meterAddress);
				request.setMeterFactoryCode(meterFactoryCode);
				request.setMeterType(meterType.byteValue());
				request.setMethod(METHOD);
				request.setSequence(sequence.byteValue());
				request.setServiceId(SERVICE_ID);

				SendResult sendResult = configThresholdProducer.sendMsg(null, request.toJsonString(request));
				if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
					// 发送成功
					return RequestResultUtil.getResultSuccess("发送水量阀值指令成功，请到下发指令记录列表中查看结果！");
				}
			}
			// 发送失败
			return RequestResultUtil.getResultFail("发送水量阀值指令失败，请重新操作！");

//			//生成开/关阀指令
//			String command = null;
//			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
//			System.out.println("----------生成水量阀值指令");
//			//生成水量阀值指令
//			command = this.generatorWaterAmountCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), Integer.valueOf(cmdAction), deviceId);
//	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
//	    	resultMap.put("command", command);
//	    	return resultMap;
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
	public Object cmdOpenCloseGenerator(Long id, Integer cmdType, String cmdAction) {
		try {
			// WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Meters device = metersService.selectByPrimaryKey(id);
			Integer meterType = Integer.valueOf(device.getMeterUseType());// 表类型
			String meterAddress = device.getMeterAddress();// 表地址
			String meterFactoryCode = device.getMeterFactoryCode();// 表厂商
			Integer sequence = device.getMeterSequence();// 序号

			// TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
			if (sequence < 255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
			device.setMeterSequence(sequence);
			metersService.updateByPrimaryKeySelective(device);
			// wmDeviceService.updateByPrimaryKeySelective(device);

			// ----------测试时使用，正常使用时需要注释----------
//	    	if(StringUtils.isBlank(meterAddress)) {
//	    		meterAddress = "1505900569";
//	    	}
//	    	if(StringUtils.isBlank(meterFactoryCode)) {
//	    		meterFactoryCode = "5843";
//	    	}
			// ----------

			// if(StringUtils.isBlank(cmdAction) || meterType==null ||
			// StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) ||
			// sequence==null) {
			if (meterType == null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode)
					|| sequence == null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
			// 如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
			// 或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
			if ((StringUtils.isNotBlank(cmdAction))) {
				log.debug("----------允许生成指令类型：" + cmdType + "，指令动作：" + cmdAction);
			} else {
				return RequestResultUtil.getResultFail("参数错误！");
			}

			log.debug("----------继续生成指令类型：" + cmdType + "，指令动作：" + cmdAction);

//	    	<option value="10">冷水表</option>
//			<option value="11">生活热水表</option>
//			<option value="12">直饮水水表</option>
//			<option value="13">中水水表</option>
			byte meterTypeB = 0x00;
			if (meterType == 10) {
				meterTypeB = Protocol.METER_TYPE_10H;
			} else if (meterType == 11) {
				meterTypeB = Protocol.METER_TYPE_11H;
			} else if (meterType == 12) {
				meterTypeB = Protocol.METER_TYPE_12H;
			} else if (meterType == 13) {
				meterTypeB = Protocol.METER_TYPE_13H;
			}

			byte sequenceB = 0x00;
			// 5=开/关阀指令
			System.out.println("----------生成开/关阀指令");
			// 开/关阀指令
			// String command = this.generatorOpenCloseCommand(meterTypeB, meterAddress,
			// meterFactoryCode, sequence.byteValue(),Integer.valueOf(cmdAction),
			// device.getDeviceId());
			// 获取指令对象
			WmCommand wmCommand = wmCommandService.getWmCommand(device, EnumCommandType.TYPE_OPEN_CLOSE.getKey(), null);
			int rows = wmCommandService.insertSelective(wmCommand);// 增加命令记录
			if (rows > 0) {

				// 获取开关阀指令动作 对应ControlValveType类中的值
				byte action = this.getOpenCloseAction(Integer.valueOf(cmdAction));

				ControlValveRequest request = new ControlValveRequest();
				request.setAction(action);
				request.setDeviceId(device.getDeviceId());
				request.setMeterAddress(meterAddress);
				request.setMeterFactoryCode(meterFactoryCode);
				request.setMeterType(meterType.byteValue());
				request.setMethod(METHOD);
				request.setSequence(sequence.byteValue());
				request.setServiceId(SERVICE_ID);

				SendResult sendResult = controlValveProducer.sendMsg(null, request.toJsonString(request));
				if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
					// 发送成功
					return RequestResultUtil.getResultSuccess("发送开/关阀指令成功，请到下发指令记录列表中查看结果！");
				}
			}
			// 发送失败
			return RequestResultUtil.getResultFail("发送水开/关阀指令失败，请重新操作！");

//			//生成开/关阀指令
//			String command = null;
//			//cmdType 指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
//			System.out.println("----------生成开/关阀指令");
//			//生成水量阀值指令
//			command = this.generatorOpenCloseCommand(meterTypeB, meterAddress, meterFactoryCode, sequence.byteValue(), Integer.valueOf(cmdAction), deviceId);
//	    	Map<String, Object> resultMap = RequestResultUtil.getResultSuccess("生成指令成功！");
//	    	resultMap.put("command", command);
//	    	return resultMap;
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
	public Object cmdMeterConfigGenerator(Long id, Integer cmdType, String cmdAction) {
		try {
			// WmDevice device = wmDeviceService.selectByPrimaryKey(id);
			Meters device = metersService.selectByPrimaryKey(id);
			Integer meterType = Integer.valueOf(device.getMeterUseType());// 表类型
			String meterAddress = device.getMeterAddress();// 表地址
			String meterFactoryCode = device.getMeterFactoryCode();// 表厂商
			Integer sequence = device.getMeterSequence();// 序号

			// TODO G11 需要每次+1，范围1到255，循环，sequence+1然后保存到数据库中
			if (sequence < 255) {
				sequence = sequence + 1;
			} else {
				sequence = 1;
			}
			device.setMeterSequence(sequence);
			metersService.updateByPrimaryKeySelective(device);
			// wmDeviceService.updateByPrimaryKeySelective(device);

			// ----------测试时使用，正常使用时需要注释----------
//	    	if(StringUtils.isBlank(meterAddress)) {
//	    		meterAddress = "1505900569";
//	    	}
//	    	if(StringUtils.isBlank(meterFactoryCode)) {
//	    		meterFactoryCode = "5843";
//	    	}
			// ----------

			// if(StringUtils.isBlank(cmdAction) || meterType==null ||
			// StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode) ||
			// sequence==null) {
			if (meterType == null || StringUtils.isBlank(meterAddress) || StringUtils.isBlank(meterFactoryCode)
					|| sequence == null) {
				return RequestResultUtil.getResultFail("参数错误！");
			}
			// 如果命令动作为空且指令类型为4=读月冻结指令或5=读表配置指令时可以继续生成指令
			// 或如果命令动作不为空且指令类型不为4=读月冻结指令，且不为5=读表配置指令时可以继续生成指令
			if ((StringUtils.isNotBlank(cmdAction))) {
				log.debug("----------允许生成指令类型：" + cmdType + "，指令动作：" + cmdAction);
			} else {
				return RequestResultUtil.getResultFail("参数错误！");
			}

			log.debug("----------继续生成指令类型：" + cmdType + "，指令动作：" + cmdAction);

//	    	<option value="10">冷水表</option>
//			<option value="11">生活热水表</option>
//			<option value="12">直饮水水表</option>
//			<option value="13">中水水表</option>
			byte meterTypeB = 0x00;
			if (meterType == 10) {
				meterTypeB = Protocol.METER_TYPE_10H;
			} else if (meterType == 11) {
				meterTypeB = Protocol.METER_TYPE_11H;
			} else if (meterType == 12) {
				meterTypeB = Protocol.METER_TYPE_12H;
			} else if (meterType == 13) {
				meterTypeB = Protocol.METER_TYPE_13H;
			}

			byte sequenceB = 0x00;
			// 水表配置指令
			System.out.println("----------生成水表配置指令");
			// 水表配置指令
			// String command = this.generatorMeterConfigCommand(meterTypeB, meterAddress,
			// meterFactoryCode, sequence.byteValue(),cmdAction, device.getDeviceId());

			// 获取指令对象
			WmCommand wmCommand = wmCommandService.getWmCommand(device, EnumCommandType.TYPE_METER_CONFIG.getKey(),
					null);
			int rows = wmCommandService.insertSelective(wmCommand);// 增加命令记录
			if (rows > 0) {

				ConfigParamsRequest request = this.getConfigParamsRequest(device, wmCommand.getId(), cmdAction);

				SendResult sendResult = configParmsProducer.sendMsg(null, request.toJsonString(request));
				if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
					// 发送成功
					return RequestResultUtil.getResultSuccess("发送水表配置指令成功，请到下发指令记录列表中查看结果！");
				}
			}
			// 发送失败
			return RequestResultUtil.getResultFail("发送水水表配置指令失败，请重新操作！");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成指令异常！");

	}

	/**
	 * @Title: getConfigParamsRequest
	 * @Description: //获取下发表配置指令请求对象
	 * @param device      水表设备
	 * @param wmCommandId 已保存的指令ID
	 * @param cmdAction   下发指令的动作
	 * @return
	 */
	private ConfigParamsRequest getConfigParamsRequest(Meters device, Long wmCommandId, String cmdAction) {

		// JSONObject action = JSON.parseObject(cmdAction);
		// String meterNumber = action.getString("meterNumber");//表号

		// QueryMonthDataRequest queryMonthData = new QueryMonthDataRequest();
		Integer meterType = Integer.valueOf(device.getMeterUseType());// 表类型
		String meterAddress = device.getMeterAddress();// 表地址
		String meterFactoryCode = device.getMeterFactoryCode();// 表厂商
		Integer sequence = device.getMeterSequence();// 序号

		// 获取下发表配置指令对象-表参数对象
		DeviceParams deviceParams = this.getDeviceParams(cmdAction);

		ConfigParamsRequest request = new ConfigParamsRequest();
		request.setDeviceId(device.getDeviceId());
		request.setId(wmCommandId);
		request.setMeterAddress(meterAddress);
		request.setMeterFactoryCode(meterFactoryCode);
		request.setMeterType(meterType.byteValue());
		request.setMethod(METHOD);
		request.setSequence(sequence.byteValue());
		request.setServiceId(SERVICE_ID);

		request.setDeviceParams(deviceParams);

		return request;
	}

	/**
	 * @Title: getDeviceParams
	 * @Description: 获取下发表配置指令对象-表参数对象
	 * @param cmdAction
	 * @return
	 */
	private DeviceParams getDeviceParams(String cmdAction) {
		JSONObject action = JSON.parseObject(cmdAction);
		String meterNumber = action.getString("meterNumber");// 表号
		Integer reportPeriod = action.getInteger("reportPeriod");// 定时上传周期
		Integer reportPeriodUnit = action.getInteger("reportPeriodUnit");// 定时上传周期单位
		Integer reportRation = action.getInteger("reportRation");// 定量上传值
		Integer temporaryTime = action.getInteger("temporaryTime");// 用户临时开阀用水限定时间
		Integer valveRunTime = action.getInteger("valveRunTime");// 阀门行程时间
		Integer valveMaintainPeriod = action.getInteger("valveMaintainPeriod");// 阀门维护周期
		Integer basicValue = action.getInteger("meterBasicValue");// 表底数
		Float sampleUnit = action.getFloat("sampleUnit");// 采样参数单位
		Date meterTime = action.getDate("meterTime");// 表当前时间 格式：yyyy-MM-dd HH:mm:ss
		Integer magneticAlarmOn = action.getInteger("magneticAlarmOn");// 磁干扰告警消除 TODO

		Integer meterStatusPeriod = action.getInteger("meterStatusPeriod");// 表状态字-定时上传功能开关 (1开 / 0关)
		Integer meterStatusMaxReport = action.getInteger("meterStatusMaxReport");// 表状态字-定量上传功能开关 (1开 / 0关)
		Integer meterStatusMagnetic = action.getInteger("meterStatusMagnetic");// 表状态字-磁干扰关阀功能开关 (1开 / 0关)

		// 获取下发表配置指令对象-参数修改标识对象
		DeviceParamsFlags configFlag = this.getDeviceParamsFlags(cmdAction);
		// 获取下发表配置指令对象-表状态字对象
		MeterStatusBean meterStatus = this.getMeterStatusBean(meterStatusPeriod, meterStatusMaxReport,
				meterStatusMagnetic);
		DeviceParams params = new DeviceParams();
		params.setConfigFlag(configFlag); // 参数修改标识
		params.setMeterBasicValue(basicValue); // 表底数: 4字节, 整型
		params.setMeterNumber(meterNumber); // 表号：12字节数字字符串(6字节水表资产编号，BCD格式；)
		params.setMeterStatus(meterStatus); // 表状态字：由meterStatusFlag转换
		params.setMeterTime(meterTime); // 表当前时间：14字节数字字符串格式为yyyymmddHHMMSS (7字节，年、月、星期、日、时、分、秒，BCD格式；)
		params.setReportPeriod(reportPeriod.shortValue()); // 定时上传周期：2字节(0-65535)十进制，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台；
		params.setReportPeriodUnit(reportPeriodUnit.byteValue()); // 定时上传周期单位：1字节(0-255)十进制，0为分钟，1为小时，2为天；
		params.setReportRation(reportRation.shortValue()); // 定量上传值：2字节(0-65535)十进制，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台；
		params.setSampleUnit(sampleUnit); // 采样参数：1字节(0-255)十进制，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样；
		params.setServerIp(null); // 服务器IP：AAA.BBB.CCC.DDD格式
		params.setServerPort((short) 0); // 端口号：2字节(0-65535)十进制
		params.setTemporaryTime(temporaryTime.byteValue()); // 用户临时开阀用水限定时间：1字节(0-255)十进制，单位为小时，用户可通过磁吸装置实现临时用水；
		params.setValveMaintainPeriod(valveMaintainPeriod.shortValue()); // 阀门维护周期：2字节(0-65535)十进制，单位为小时，水表以该周期值进行阀门维护操作；
		params.setValveRunTime(valveRunTime.byteValue()); // 阀门行程时间：1字节(0-255)十进制，单位为秒，正常情况下阀门单行程的最大时间值；
		return params;
	}

	/**
	 * @Title: getDeviceParamsFlags
	 * @Description: 获取下发表配置指令对象-参数修改标识对象
	 * @param cmdAction
	 * @return
	 */
	private DeviceParamsFlags getDeviceParamsFlags(String cmdAction) {
		JSONObject action = JSON.parseObject(cmdAction);

		Boolean checkboxReportPeriod = action.getBoolean("checkboxReportPeriod");// 是否配置定时上传周期
		Boolean checkboxReportPeriodUnit = action.getBoolean("checkboxReportPeriodUnit");// 是否配置定时上传周期单位
		Boolean checkboxReportRation = action.getBoolean("checkboxReportRation");// 是否配置定量上传值
		Boolean checkboxTemporaryTime = action.getBoolean("checkboxTemporaryTime");// 是否配置用户临时开阀用水限定时间
		Boolean checkboxValveRunTime = action.getBoolean("checkboxValveRunTime");// 是否配置阀门行程时间
		Boolean checkboxValveMaintainPeriod = action.getBoolean("checkboxValveMaintainPeriod");// 是否配置阀门维护周期
		Boolean checkboxMeterBasicValue = action.getBoolean("checkboxMeterBasicValue");// 是否配置表底数
		Boolean checkboxSampleUnit = action.getBoolean("checkboxSampleUnit");// 是否配置采样参数单位
		Boolean checkboxMeterTime = action.getBoolean("checkboxMeterTime");// 是否配置表当前时间
		Boolean checkboxMagneticAlarmOn = action.getBoolean("checkboxMagneticAlarmOn");// 是否配置磁干扰告警消除
		Boolean checkboxMeterStatus = action.getBoolean("checkboxMeterStatus");// 是否配置表状态字

		DeviceParamsFlags paramsFlags = new DeviceParamsFlags();
		paramsFlags.setEmergTime(checkboxTemporaryTime);// 用户临时开阀用水限定时间
		paramsFlags.setMagneticAlarmClear(checkboxMagneticAlarmOn);// 磁干扰告警清除
		paramsFlags.setMaxReport(checkboxReportRation);// 定量上传值
		paramsFlags.setMeterNum(false);// 表号
		paramsFlags.setMeterStatus(checkboxMeterStatus);// 表状态字
		paramsFlags.setMeterTime(checkboxMeterTime);// 表时间
		paramsFlags.setPeriod(checkboxReportPeriod);// 定时上传周期
		paramsFlags.setPeriodUnit(checkboxReportPeriodUnit);// 定时上传周期单位
		paramsFlags.setSampleUnit(checkboxSampleUnit);// 采样参数
		paramsFlags.setServerIp(false);// 服务器IP
		paramsFlags.setServerPort(false);// 端口号
		paramsFlags.setValveMaintainTime(checkboxValveMaintainPeriod);// 阀门维护周期
		paramsFlags.setValveTime(checkboxValveRunTime);// 阀门行程时间
		paramsFlags.setWaterVolume(checkboxMeterBasicValue);// 累计使用量
		return paramsFlags;
	}

	/**
	 * @Title: getMeterStatusBean
	 * @Description: 获取下发表配置指令对象-表状态字对象
	 * @param meterStatusPeriod    //表状态字-定时上传功能开关 (1开 / 0关)
	 * @param meterStatusMaxReport //表状态字-定量上传功能开关 (1开 / 0关)
	 * @param meterStatusMagnetic  //表状态字-磁干扰关阀功能开关 (1开 / 0关)
	 * @return
	 */
	private MeterStatusBean getMeterStatusBean(Integer meterStatusPeriod, Integer meterStatusMaxReport,
			Integer meterStatusMagnetic) {

		int zero = 0;

		MeterStatusBean meterStatus = new MeterStatusBean();
		meterStatus.setBatteryLow(zero);// 电池电压低
		meterStatus.setMagneticAlarmOn(zero);// 磁干扰报警 TODO
		meterStatus.setMagneticOn(meterStatusMagnetic);// 磁干扰关阀功能开关
		meterStatus.setMaxReportOn(meterStatusMaxReport);// 定量上传功能开关
		meterStatus.setPeriodOn(meterStatusPeriod);// 定时上传功能开关
		meterStatus.setSampleLineCut(zero);// 采样线断线报警状态
		meterStatus.setValveAbnormal(zero);// 阀门故障
		meterStatus.setValveOpen(zero);// 阀门状态
		return meterStatus;
	}

	/**
	 * @Title: getOpenCloseAction
	 * @Description: 获取开关阀指令动作
	 * @param cmdAction
	 * @return
	 */
	private byte getOpenCloseAction(Integer cmdAction) {
		byte action = MeterValveControlCmd.VALVE_CLOSE;// 关阀
		// cmdAction 1=关阀;2=开阀;3=打开50%;4=打开25%;5=打开12.5%;
		if (cmdAction == 1) {
			action = ControlValveType.VALVE_CLOSE;// 关阀
		} else if (cmdAction == 2) {
			action = ControlValveType.VALVE_OPEN;// 开阀
		} else if (cmdAction == 3) {
			action = ControlValveType.VALVE_OPEN_PER_50;// 打开50%
		} else if (cmdAction == 4) {
			action = ControlValveType.VALVE_OPEN_PER_25;// 打开25%
		} else if (cmdAction == 5) {
			action = ControlValveType.VALVE_OPEN_PER_12_5;// 打开12.5%
		}
		return action;
	}

}