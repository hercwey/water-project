package com.learnbind.ai.controller.meters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.constant.MeterFunctionModuleConstant;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.MeterERecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysCheckMeterResult;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.checkmeter.CheckMeterResultService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MeterERecordService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterRepairController.java
 * @Description: 库存管理-检修
 *
 * @author Thinkpad
 * @date 2019年10月23日 下午1:21:58
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-repair")
public class MeterRepairController {
	private static Log log = LogFactory.getLog(MeterRepairController.class);
	private static final String TEMPLATE_PATH = "meter_stock/meter_repair/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MetersService metersService;  //客户档案
	@Autowired
	private LocationMeterService locationMeterService;  //地理位置-表计关系表
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计关联表
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private DiscountService discountService;  //政策减免
	@Autowired
	private WaterPriceService waterPriceService;  //水价
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//客户账目
	@Autowired
	private CheckMeterResultService checkMeterResultService;  //检测结果
	@Autowired
	private MeterERecordService meterERecordService;//表计-电子档案服务
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "meters_starter";
	}

	/**
	 * @Title: tabs
	 * @Description: 加载选项卡页面
	 * @param model
	 * @param functionModule
	 * @return 
	 */
	@RequestMapping(value = "/tabs")
	public String tabs(Model model) {
		
		return TEMPLATE_PATH + "tabs";
	}
	
	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		model.addAttribute("meter", null);//初始化表计档案详情信息
		
		return TEMPLATE_PATH + "meters_main";
	}
	
	/**
	 * @Title: locationTable
	 * @Description: 加载地理位置
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/location-table")
	public String locationTable(Model model) {

		return TEMPLATE_PATH + "location_table";
	}
	
	/**
	 * @Title: table
	 * @Description: 加载地理位置列表
	 * @param model
	 * @param functionModule
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String caliber) {

		
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = new ArrayList<>();
		//获取表计状态
		List<Integer> cycleStatusList = this.getCycleStatus();
		if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			meterList = metersService.getMeterListByCycleStatus(searchCond, traceIds, cycleStatusList);
		} else {
			meterList = metersService.searchMetersByCycleStatus(searchCond, cycleStatusList);
		}
		PageInfo<Meters> pageInfo = new PageInfo<Meters>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(Meters meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("cycleStatusStr", meter.getCycleStatusStr());//表计生命周期状态
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMap.put("readModeValue", this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meter.getReadMode()));//抄表方式
			meterMapList.add(meterMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "meters_table";
	}
	
	/**
	 * @Title: getCycleStatus
	 * @Description: 获取查询表计生命周期状态
	 * @return 
	 */
	private List<Integer> getCycleStatus(){
		List<Integer> cycleStatusList = new ArrayList<>();
		//待检修
		Integer inactiveStatus = EnumMeterCycleStatus.NO_MAINTENANCE.getValue();
		cycleStatusList.add(inactiveStatus);
		//检修中
		Integer notEnableStatus = EnumMeterCycleStatus.IN_MAINTENANCE.getValue();
		cycleStatusList.add(notEnableStatus);
		
		return cycleStatusList;
	}
	
	
	/**
	 * @Title: loadMeterReceiveDialog
	 * @Description: 打开领用水表对话框
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-meter-repair-dialog")
	public String loadMeterRepairDialog(Model model, Long meterId) {
		model.addAttribute("meterId", meterId);
		return TEMPLATE_PATH + "meter_repair_dialog";
	}
	
	/**
	 * @Title: insert
	 * @Description: 检测水表
	 * @param request
	 * @param model
	 * @param fileDir
	 * @param inputName
	 * @param erecord
	 * @return 
	 */
	@RequestMapping(value = "/insert")
	@ResponseBody
	public String insert(HttpServletRequest request, Model model, String fileDir, String inputName, MeterERecord erecord) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			
			//上传协议文件
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileDir, inputName);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("上传图片路径："+filePath);
				filePath = FileUploadUtil.subImgPath(filePathList.get(0));
				System.out.println("截取后上传图片路径："+filePath);
				erecord.setErecordPath(filePath);
			}
			
			int rows = meterERecordService.insertSelective(erecord);
			if(rows>0) {
				//修改水表的生命周期状态
				Meters meter = metersService.selectByPrimaryKey(erecord.getMeterId());
				meter.setCycleStatus(EnumMeterCycleStatus.IN_MAINTENANCE.getValue());
				
				metersService.updateByPrimaryKeySelective(meter);
				return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
	}
	
	

	
	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long meterId) {
		Meters currItem=metersService.selectByPrimaryKey(meterId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "meters_dialog_edit";
	}
	
	/**
	 * @Title: updateMeters
	 * @Description: 修改
	 * @param meter
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateMeters(Long meterId, Integer cycleStatus) throws Exception {
		Meters meter = metersService.selectByPrimaryKey(meterId);
		meter.setCycleStatus(cycleStatus);
		int rows = metersService.updateByPrimaryKeySelective(meter);
		if(rows>0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	


	/**
	 * @Title: detail
	 * @Description: 加载表计档案详细信息
	 * @param model
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/detail-table")
	public String detail(Model model, Long meterId) {
		
		Map<String, Object> meterMap = null;
		if(meterId!=null && meterId>0) {
			Meters meter = metersService.selectByPrimaryKey(meterId);
			
			meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("checkDateStr", meter.getCheckDateStr());//水表检定日期
			meterMap.put("effectiveDateStr", meter.getEffectiveDateStr());//水表有效日期
			meterMap.put("meterStatusStr", meter.getMeterStatusStr());//水表状态
			meterMap.put("virtualRealStr", meter.getVirtualRealStr());//虚实表
			meterMap.put("cycleStatusStr", meter.getCycleStatusStr());//水表生命周期状态
			
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMap.put("caliberValue", this.getDataDictValue(EnumDataDictType.METER_WATER_CALIBER.getCode(), meter.getCaliber()));//水表口径
			meterMap.put("meterModelValue", this.getDataDictValue(EnumDataDictType.METER_MODEL.getCode(), meter.getMeterModel()));//水表型号
			meterMap.put("meterTypeValue", this.getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meter.getMeterType()));//水表类型
			meterMap.put("meterUseValue", this.getDataDictValue(EnumDataDictType.METER_USE.getCode(), meter.getMeterUse()));//水表用途
			meterMap.put("readModeValue", this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meter.getReadMode()));//抄表方式
		}
		
		model.addAttribute("meter", meterMap);
		
		return TEMPLATE_PATH + "meters_detail_table";
	}
	//-------------------------检测结果选项卡------------
	/**
	 * @Title: resultMain
	 * @Description: 检测结果
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/check-result-main")
	public String resultMain(Model model, Long meterId) {
		model.addAttribute("meterId", meterId);
		return TEMPLATE_PATH + "result_main";
	}
	
	@RequestMapping(value = "/result-table")
	public String resultTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long meterId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//只查询检测结果
		Integer checkType =2;
		List<SysCheckMeterResult> checkMeterresultList = checkMeterResultService.getList(searchCond, meterId, checkType);
		PageInfo<List<SysCheckMeterResult>> pageInfo = new PageInfo(checkMeterresultList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(SysCheckMeterResult scr : checkMeterresultList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(scr);
			String checkResult = StringEscapeUtils.unescapeHtml(scr.getCheckResult());
			customerMap.put("checkTimeStr", scr.getCheckTimeStr());
			customerMap.put("checkResult", checkResult);
			customerMap.put("checkResultTypeStr", scr.getCheckResultTypeStr());
			
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("checkMeterresultList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "result_table";
	}
	
	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadadddialog")
	public String loadAddDialog(Model model, Long meterId) {
		Meters meter = metersService.selectByPrimaryKey(meterId);
		String title="增加";
		model.addAttribute("title",title);
		model.addAttribute("meterId",meterId);
		model.addAttribute("cycleStatus",meter.getCycleStatus());
		return TEMPLATE_PATH + "result_dialog_edit";
	}
	
	
	
	/** 
	*	@Title: add
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert-result", produces = "application/json")
	@ResponseBody
	public Object addResult(SysCheckMeterResult sysCheckMeterResult, Integer cycleStatus) {
		//HTML字符编码
		String result = StringEscapeUtils.escapeHtml(sysCheckMeterResult.getCheckResult());
		sysCheckMeterResult.setCheckResult(result);
		Meters meter = metersService.selectByPrimaryKey(sysCheckMeterResult.getMeterId());
		meter.setCycleStatus(cycleStatus);
		int row = checkMeterResultService.insertSelective(sysCheckMeterResult);
		if (row > 0) {
			metersService.updateByPrimaryKeySelective(meter);
			return RequestResultUtil.getResultAddSuccess();
		}else {
			return RequestResultUtil.getResultAddWarn();
		}
	}
	
	/** 
	*	@Title: loadModiDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiDialog(Long itemId, Model model) {
		//读取需要编辑的条目
		SysCheckMeterResult currItem=checkMeterResultService.selectByPrimaryKey(itemId);
		//HTML转义解码
		String result = StringEscapeUtils.unescapeHtml(currItem.getCheckResult());
		currItem.setCheckResult(result);
		model.addAttribute("currItem",currItem);
		String title="编辑";
		model.addAttribute("title",title);
		Meters meter = metersService.selectByPrimaryKey(currItem.getMeterId());
		model.addAttribute("cycleStatus",meter.getCycleStatus());
		return TEMPLATE_PATH + "result_dialog_edit";
	}

	/** 
	*	@Title: update
	*	@Description: 修改水价 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update-result", produces = "application/json")
	@ResponseBody
	public Object updateResult(SysCheckMeterResult checkMeter, Integer cycleStatus) throws Exception {
		//HTML字符编码
		String result = StringEscapeUtils.escapeHtml(checkMeter.getCheckResult());
		checkMeter.setCheckResult(result);
		Meters meter = metersService.selectByPrimaryKey(checkMeter.getMeterId());
		meter.setCycleStatus(cycleStatus);
		int rows = checkMeterResultService.updateByPrimaryKeySelective(checkMeter);
		if(rows > 0) {
			metersService.updateByPrimaryKeySelective(meter);
			return RequestResultUtil.getResultUpdateSuccess();
		} 
			return RequestResultUtil.getResultUpdateWarn();
		
	}
	
	//------------------------------	内部方法	------------------------------
	/**
	 * @Title: getDataDictValue
	 * @Description: 根据数据字典类型编码和字典编码查询
	 * @param typeCode
	 * @param key
	 * @return 
	 */
	private String getDataDictValue(String typeCode, String key) {
		
		if(StringUtils.isBlank(typeCode) && StringUtils.isBlank(key)) {
			return null;
		}
		
		DataDict dict = new DataDict();
		if(StringUtils.isNotBlank(typeCode)) {
			dict.setTypeCode(typeCode);
		}
		dict.setKey(key);
		List<DataDict> dictList = dataDictService.select(dict);
		if(dictList!=null && dictList.size()>0) {
			dict = dictList.get(0);
		}
		
		if(dict!=null) {
			return dict.getValue();
		}
		return null;
	}
	
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getLinuxUploadFolder();
		if(this.isWindowsOS()) {
			path = uploadFileConfig.getWindowsUploadFolder();
		}
		System.out.println("----------"+path);
		return path;
	}
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	private boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}
	

	
	
	
	
}