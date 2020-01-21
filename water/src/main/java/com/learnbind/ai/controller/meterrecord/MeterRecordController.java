package com.learnbind.ai.controller.meterrecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterRecordStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.constant.MeterFunctionModuleConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.MeterRecordPhoto;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterRecordController.java
 * @Description: 抄表记录
 *
 * @author Thinkpad
 * @date 2019年5月31日 下午7:04:08
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-record")
public class MeterRecordController {
	private static Log log = LogFactory.getLog(MeterRecordController.class);
	private static final String TEMPLATE_PATH = "meter_record/"; // 页面目录角色
	private static final String TEMPLATE_PATH_METER = "meter_record/insert_record/"; // 增加抄表记录
	private static final int PAGE_SIZE = 15; // 页大小

	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private MeterRecordService meterRecordService;  //抄表记录服务
	@Autowired
	private MeterRecordPhotoService meterRecordPhotoService;//抄表记录照片服务
	@Autowired
	private WaterPriceService waterPriceService;//水价
	@Autowired
	private MetersService metersService;  //表计服务
	@Autowired
	private CustomersService customersService;  //客户服务
	@Autowired
	private CustomerMeterService customerMeterService;  //客户服务
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private MeterPartWaterRuleTraceService meterPartWaterRuleTraceService;//分水量规则配置日志表
	@Autowired
	private PartitionRuleUtil partitionRuleUtil;//分水量规则工具类
	
	/** 
	*	@Title: RecordStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String RecordStarter(Model model, Integer readType) {
		model.addAttribute("readType", readType);
		return TEMPLATE_PATH + "record_starter";
	}

	/** 
	*	@Title: meterRecordSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String meterRecordSearch(Model model, Integer readType) {
		
		model.addAttribute("readType", readType);
		
		//加载数据字典-抄表方式
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		model.addAttribute("readModeList", readModeList);
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "record_main";
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
	 * @Title: meterRecordTable
	 * @Description: 加载用户列表(列表页面)
	 * @param model	ModelView中传递数据的对象
	 * @param pageNum	页号
	 * @param pageSize	页大小
	 * @param searchCond	查询条件
	 * @param readType
	 * @param locationCode	地理位置-小区编码
	 * @return 
	 */
	@RequestMapping(value = "/table-old")
	/* @ResponseBody */
	public String meterRecordTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer readType, String traceIds, String period, Integer isAddSubWater, Integer isPartWater, String startDate, String endDate) {

		//判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> customerList = meterRecordService.getListGroupByCustomerAndPeriod(null, period, searchCond, readType, operatorId, traceIds, isAddSubWater, isPartWater, startDate, endDate);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		for(Map<String, Object> customer : customerList) {
			
			String recordIds = customer.get("RECORD_IDS").toString();//抄表记录ID，多个抄表记录ID用逗号分隔
			String[] recordIdArr = recordIds.split(",");
			//String meterIds = customer.get("METER_IDS").toString();//表计ID，多个表计ID用逗号分隔
			//String[] meterIdArr = meterIds.split(",");
			customer.put("METER_SIZE", recordIdArr.length);
			
			String currTraceIds = customer.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			String customerId = customer.get("CUSTOMER_ID").toString();//客户ID
			
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			customer.put("place", place);
			
			String currPeriod = customer.get("PERIOD").toString();//期间
			
			if(StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(currPeriod)) {
				List<Map<String, Object>> recordMapList = new ArrayList<>();
				//List<MeterRecord> recordList = meterRecordService.getListByCustomerIdAndPeriod(Long.valueOf(customerId), currPeriod);
				List<MeterRecord> recordList = meterRecordService.getListByRecordIdList(recordIdArr);
				for(MeterRecord record : recordList) {
					Map<String, Object> recordMap = EntityUtils.entityToMap(record);
					recordMap.put("readTypeStr", record.getReadTypeStr());
					recordMap.put("preDateStr", record.getPreDateStr());
					recordMap.put("currDateStr", record.getCurrDateStr());
					recordMap.put("preDateStr", record.getPreDateStr());
					
					//获取抄表记录的照片
					//String imagePath = this.getMeterRecordPhoto(record.getCustomerId(), record.getMeterId(), record.getPeriod());
					String imagePath = this.getMeterRecordPhoto(record.getId());
					recordMap.put("imagePath", imagePath);
					
					recordMapList.add(recordMap);
				}
				customer.put("recordList", recordMapList);
			}else {
				customer.put("recordList", new ArrayList<>());
			}
			
			//是否已创建账单
			//boolean isCreateBill = this.getIsCreateBill(Long.valueOf(customerId), period);
			//customer.put("IS_CREATER_BILL", isCreateBill);
		}
		
		
		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "record_table";
	}
	
	/**
	 * @Title: table
	 * @Description: 加载table类型
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @param isAddSubWater
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer readType, String traceIds, String period, Integer isAddSubWater, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount) {

		//判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		//Long operatorId = this.getOperatorId();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		if(StringUtils.isNotBlank(searchCond)) {
			searchCond = searchCond.trim();
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> customerList = meterRecordService.getMeterRecordList(null, searchCond, readType, null, traceIds, period,isPartWater, startDate, endDate,meterRecordStatus, currAmount);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		//获取统计的水量
		String totalWaterAmount = "";//总水量
		Map<String, Object> statisticMap = meterRecordService.getMeterRecorStatisticMap(null, searchCond, readType, null, traceIds, period,isPartWater, startDate, endDate,meterRecordStatus, currAmount);
		if(statisticMap != null) {
			totalWaterAmount = statisticMap.get("TOTAL_WATER_AMOUNT").toString();
		}
		for(Map<String, Object> customer : customerList) {
			
//					String meterIds = customer.get("METER_IDS").toString();//表计ID，多个表计ID用逗号分隔
//					String[] meterIdArr = meterIds.split(",");
//					customer.put("METER_SIZE", meterIdArr.length);
			
			String recordId = customer.get("ID").toString();//抄表记录ID
			String currTraceIds = customer.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			//String customerId = customer.get("CUSTOMER_ID").toString();//客户ID
			String meterId = customer.get("METER_ID").toString();//表计ID
			
			//Integer customerType = Integer.valueOf(customer.get("CUSTOMER_TYPE").toString());
			String customerName = (String)customer.get("CUSTOMER_NAME");
			
			//if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue()) {//如果是单位用户
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterId));
				String des = meter.getDescription();//表计描述
				if(StringUtils.isNotBlank(des)) {
					customerName = customerName+"（"+meter.getDescription()+"）";
				}
			//}
			customer.put("meterPlace", customerName);
			
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			customer.put("place", place);
			
			String curReadType = null;//customer.get("READ_TYPE").toString();//抄表类型
			if(customer.get("READ_TYPE")!=null) {
				curReadType = customer.get("READ_TYPE").toString();//抄表类型
			}
			System.out.println("----------抄表类型："+curReadType);
			Date curPreDate = (Date)customer.get("PRE_DATE");//上期抄表日期
			Date curCurrDate = (Date)customer.get("CURR_DATE");//本期抄表日期
			
			if(StringUtils.isNotBlank(curReadType)) {
				customer.put("readTypeStr", EnumReadType.getName(Integer.valueOf(curReadType)));
			}else {
				customer.put("readTypeStr", "");
			}
			
			String curPreDateStr = null;
			if(curPreDate!=null) {
				curPreDateStr = DateUtils.formatDate(curPreDate, "yyyy-MM-dd");
			}
			customer.put("preDateStr", curPreDateStr);
			
			String currDateStr = null;
			if(curCurrDate!=null) {
				currDateStr = DateUtils.formatDate(curCurrDate, "yyyy-MM-dd");
			}
			customer.put("currDateStr", currDateStr);
			
			//获取抄表记录状态
			String status = customer.get("STATUS").toString();
			customer.put("meterRecordStatusStr", EnumMeterRecordStatus.getName(Integer.valueOf(status)));
			//获取抄表记录的照片
			String imagePath = this.getMeterRecordPhoto(Long.valueOf(recordId));
			customer.put("imagePath", imagePath);
			
			//获取分水量规则
			String partWaterRule = this.getPartWaterRule(Long.valueOf(recordId));
			customer.put("partWaterRule", partWaterRule);
		}
		
		
		// 传递如下数据至前台页面
		model.addAttribute("totalWaterAmount", totalWaterAmount);  //列表数据
		model.addAttribute("customerList", customerList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "record_table";
	}
	
	/**
	 * @Title: getPartWaterRule
	 * @Description: 根据抄表记录ID获取分水量规则
	 * @param meterRecordId
	 * @return 
	 */
	private String getPartWaterRule(Long meterRecordId) {
		//查询抄表记录使用的分水量规则
		List<MeterPartWaterRuleTrace> ruleTraceList = meterPartWaterRuleTraceService.getPWaterRuleTraceByMeterRecordId(meterRecordId);
		String partWaterRule = "";
		if(ruleTraceList.size() <= 0) {
			return partWaterRule; 
		}
		for(MeterPartWaterRuleTrace trace : ruleTraceList) {
			String currRule = trace.getRuleReal();
			if(StringUtils.isBlank(currRule)) {
				continue;
			}
			log.debug("----------抄表记录ID:"+meterRecordId+"，表计规则:"+currRule);
			currRule = partitionRuleUtil.getRealExpression(currRule);//去规则中的分隔符号，返回表达式
			List<String> formalParamList = partitionRuleUtil.getRuleFormalParams(currRule);//获取表达式中的形参
			Map<String, String> actualParamMap = this.getActualParamMap(formalParamList);//获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
			String showExpression = partitionRuleUtil.setRuleActualParams(actualParamMap, currRule);//实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
			if(StringUtils.isNotBlank(showExpression)) {
				//String html = "<p>"+currRule+"</p>";
				SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(trace.getWaterPriceId());//水价
				
				String html = "("+showExpression+")"+"*"+waterPrice.getLadderName()+"("+waterPrice.getTotalPrice()+")"+"<br>";
				partWaterRule = partWaterRule+html;
			}
			
		}
		return partWaterRule; 
	}
	/**
	 * @Title: getActualParamMap
	 * @Description: 获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
	 * @param formalParamList
	 * @return 
	 */
	private Map<String, String> getActualParamMap(List<String> formalParamList){
		Map<String, String> actualParamMap = new HashMap<>();
		for(String meterParam : formalParamList) {
			String meterId = partitionRuleUtil.getMeterIndentify(meterParam);
			if(StringUtils.isNotBlank(meterId)) {
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterId));
				//String meterNo = meter.getMeterNo();//表计编号
				//默认显示表计描述，如果表计描述为空时显示表计安装位置，如果表计安装位置也为空时显示表计编号
				String meterDesc = meter.getDescription();//表计描述
				if(StringUtils.isBlank(meterDesc)) {
					String place = meter.getPlace();//表计安装位置
					if(StringUtils.isNotBlank(place)) {
						meterDesc = place;
					}else {
						meterDesc = meter.getMeterNo();//表计编号
					}
				}
				actualParamMap.put(meterParam, meterDesc);
			}
		}
		return actualParamMap;
	}
	
	/**
	 * @Title: getMeterRecordPhoto
	 * @Description: 获取抄表记录的照片
	 * @param customerId
	 * @param meterId
	 * @param period
	 * @return 
	 */
	//private String getMeterRecordPhoto(Long customerId, Long meterId, String period) {
	private String getMeterRecordPhoto(Long meterRecordId) {
		MeterRecordPhoto photo = new MeterRecordPhoto();
//		photo.setCustomerId(customerId);
//		photo.setMeterId(meterId);
//		photo.setPeriod(period);
		photo.setRecordId(meterRecordId);
		List<MeterRecordPhoto> photoList = meterRecordPhotoService.select(photo);
		if(photoList!=null && photoList.size()>0) {
			photo = photoList.get(0);
			return photo.getImagePath();
		}
		return null;
	}
	
	/**
	 * @Title: getIsCreateBill
	 * @Description: 获取是否已创建账单状态
	 * @param customerId
	 * @param period
	 * @return 
	 */
//	private boolean getIsCreateBill(Long customerId, String period){
//		CustomerAccountItem record = new CustomerAccountItem();
//		record.setCustomerId(customerId);
//		record.setPeriod(period);
//		List<CustomerAccountItem> itemList = customerAccountItemService.select(record);
//		if(itemList!=null && itemList.size()>0) {
//			return true;
//		}
//		return false;
//	}

	/** 
	*	@Title: loadaddrecorddialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	
	@RequestMapping(value = "/load-insert-meter-record-item")
	public String loadInsertMeterRecordItem(Model model) {
		
		//加载数据字典-抄表方式
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		model.addAttribute("readModeList", readModeList);
		
		
		return TEMPLATE_PATH + "insert_meter_record";
	}
	
	
	/**
	 * @Title: meterItemMain
	 * @Description: 加载换表时表计对话框列表
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/meter-item-main")
	public String meterItemMain(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = MeterFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(MeterFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		model.addAttribute("meter", null);//初始化表计档案详情信息
		
		return TEMPLATE_PATH_METER + "meter_item_main";
	}
	
	/**
	 * @Title: meterItemTable
	 * @Description: 加载换表对话框中表计列表
	 * @param model
	 * @param functionModule
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/meter-item-table")
	public String meterItemTable(Model model, String functionModule, Integer pageNum, Integer pageSize, String searchCond, String traceIds, Integer searchType) {

		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> meterList = metersService.getMeterBindCustomerList(searchCond, traceIds,searchType);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		for(Map<String, Object> meter : meterList) {
			meter.put("meterStatusStr", EnumMeterStatus.getName(Integer.valueOf(meter.get("METER_STATUS").toString())));//停/复水状态
			String place = locationService.getPlace(meter.get("TRACE_IDS").toString());
			meter.put("place", place);
			
			Integer customerType = Integer.valueOf(meter.get("CUSTOMER_TYPE").toString());
			String customerName = "";
			if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue()) {//如果是单位用户
				customerName = meter.get("PLACE").toString();
			} else {
				customerName = meter.get("CUSTOMER_NAME").toString();
			}
			meter.put("CUSTOMER_NAME", customerName);
			
			String readModeValue = "";
			if(meter.get("READ_MODE") != null) {
				readModeValue = this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meter.get("READ_MODE").toString());
			}
			meter.put("readModeValue", readModeValue);//抄表方式
			String factoryValue = ""; 
			if(meter.get("FACTORY") != null) {
				factoryValue = this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.get("FACTORY").toString());
			}
			meter.put("factoryValue", factoryValue);//水表生产厂家
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH_METER + "meter_item_table";
	}
	
	
	/** 
	*	@Title: getMeterMessage
	*	@Description: 根据水表id查询绑定的客户信息
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/get-customer-message")
	@ResponseBody
	public Object getMeterMessage(Model model, Long meterId, String place) {
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(meterId);
		Customers customer = customersService.selectByPrimaryKey(cm.getCustomerId());
		return customer;
	}
	
	
	/**
	 * @Title: getNodes
	 * @Description: ztree异步请求获取表计节点
	 * @param response
	 * @param model
	 * @param meterId 
	 */
	@RequestMapping(value = "/get-meter-nodes", produces = "application/json")
	public void getNodes(HttpServletResponse response, Model model, Long meterId) {
		
		try {
			
			List<Map<String, Object>> meterMapList = new ArrayList<>();
			if(meterId==null){
				meterId = 0l;
				Example example = new Example(Meters.class);
				example.createCriteria().andEqualTo("pid", meterId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
				example.setOrderByClause("pid ASC,id ASC");
				List<Meters> meterList = metersService.selectByExample(example);
				
				for(Meters meter : meterList) {
					Map<String, Object> meterMap = new HashMap<>();
					meterMap.put("METER_ID", meter.getId());
					meterMap.put("METER_PID", meter.getPid());
					meterMap.put("STEEL_SEAL_NO", meter.getSteelSealNo());
					meterMap.put("PLACE", meter.getPlace());
					meterMap.put("CUSTOMER_ID", null);
					meterMap.put("PROP_NAME", null);
					meterMap.put("isParent", true);
					meterMapList.add(meterMap);
				}
			}else {
				meterMapList = metersService.getBindMeterList(meterId);
				for(Map<String, Object> meterMap : meterMapList) {
					String meterIdStr = meterMap.get("METER_ID").toString();
					if(StringUtils.isNotBlank(meterIdStr)) {
						meterMap.put("isParent", this.isParent(Long.valueOf(meterIdStr)));
					}else {
						meterMap.put("isParent", false);
					}
				}
			}
			
			String meterMapListJson = JSON.toJSONString(meterMapList);
			
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(meterMapListJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @Title: isParent
	 * @Description: 判断是否是父节点
	 * @param id
	 * @return 
	 */
	private boolean isParent(Long id) {
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("pid", id).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		example.setOrderByClause("pid ASC,id ASC");
		List<Meters> meterList = metersService.selectByExample(example);
		if(meterList!=null && meterList.size()>0) {
			return true;
		}
		return false;
	}
	
	/** 
	*	@Title: addMeterRecord 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(MeterRecord record, Long locationId) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
	
		int row = meterRecordService.insertMeterRecord(record, userBean.getId(), userBean.getRealname());
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: generatorPartWater
	 * @Description: 生成分水量
	 * @param record
	 * @param meterRecrodIds
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @param isAddSubWater
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	@RequestMapping(value = "/generator-part-water", produces = "application/json")
	@ResponseBody
	public Object generatorPartWater(String recordIds, String searchCond, Integer readType, String traceIds, String period, Integer isAddSubWater, Integer isPartWater, String startDate, String endDate) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
	
		try {
			
			List<MeterRecord> recordList = new ArrayList<>();
			
			if(StringUtils.isNotBlank(recordIds)) {
				String[] recordIdArr = recordIds.split(",");
				for(String recordId : recordIdArr) {
					if(StringUtils.isNotBlank(recordId)) {
						MeterRecord record = meterRecordService.selectByPrimaryKey(Long.valueOf(recordId));
						recordList.add(record);
					}
					
				}
			}else {
				recordList = meterRecordService.searchMeterRecord(period, traceIds, readType, searchCond);
			}
			
			int result = meterRecordService.generatorPartWater(recordList);
			if(result==-1) {
				return RequestResultUtil.getResultSuccess("未查询到需要生成分水量的记录！");
			}else {
				return RequestResultUtil.getResultSuccess("抄表记录生成分水量成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("抄表记录生成分水量异常！");

	}
	
	/**
	 * @Title: batchGeneratorPartWater
	 * @Description: 批量生成分水量记录
	 * @param recordIds
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	@RequestMapping(value = "/batch-generator-part-water", produces = "application/json")
	@ResponseBody
	public Object batchGeneratorPartWater(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate, String recordIds) {
		try {
			List<MeterRecord> recordList = new ArrayList<>();
			
			if(StringUtils.isNotBlank(recordIds)) {
				String[] recordIdArr = recordIds.split(",");
				for(String recordId : recordIdArr) {
					if(StringUtils.isNotBlank(recordId)) {
						MeterRecord record = meterRecordService.selectByPrimaryKey(Long.valueOf(recordId));
						recordList.add(record);
					}
					
				}
			}else {
				recordList = meterRecordService.getMeterRecordData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate);;
			}
			
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("未查询到需要生成分水量的记录！");
			}
			
			//查询抄表记录
			//List<MeterRecord> recordList = meterRecordService.getMeterRecordData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate);
 			int result = meterRecordService.batchGeneratorPartWater(recordList);
			if(result>0) {
				return RequestResultUtil.getResultSuccess("抄表记录生成分水量成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("抄表记录生成分水量异常！");

	}
	
	/** 
	*	@Title: deletemeterRecord 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(String recordIdsJSON) throws Exception {
		try {
			
			if(StringUtils.isBlank(recordIdsJSON)) {
				return RequestResultUtil.getResultFail("未选择需要删除的抄表记录！");
			}
			
			int rows = meterRecordService.deleteBatch(recordIdsJSON);
			if(rows>0) {
				return RequestResultUtil.getResultSuccess("已删除抄表记录！");
			}else if(rows==-1) {
				return RequestResultUtil.getResultFail("未查询到需要删除的抄表记录，请确认对应的账单是未结算状态！");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("删除异常，请重新操作！");
	}

	/** 
	*	@Title: loadModiRecordDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiRecordDialog(Long itemId, Model model) {
		//加载数据字典-抄表方式
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
						
		model.addAttribute("readModeList", readModeList);
		
//		List<Map<String, Object>> meterMapList = metersService.getBindMeterList();
//		model.addAttribute("meterMapList", meterMapList);
		//读取需要编辑的条目
		MeterRecord currItem=meterRecordService.selectByPrimaryKey(itemId);
		Map<String, Object> recordMap = EntityUtils.entityToMap(currItem);
		Date preDate = currItem.getPreDate();
		Date currDate = currItem.getCurrDate();
		String preDateStr = null;
		if(preDate!=null) {
			preDateStr = DateUtils.formatDate(preDate, "yyyy-MM-dd HH:mm:ss");
		}
		String currDateStr = null;
		if(currDate!=null) {
			currDateStr = DateUtils.formatDate(currDate, "yyyy-MM-dd HH:mm:ss");
		}
		recordMap.put("preDateStr", preDateStr);
		recordMap.put("currDateStr", currDateStr);
		model.addAttribute("currItem",recordMap);
		Customers customer = customersService.selectByPrimaryKey(currItem.getCustomerId());
		model.addAttribute("customer", customer);
		
		return TEMPLATE_PATH + "record_dialog_edit";
	}

	/** 
	*	@Title: updatMeterRecord
	*	@Description: 修改水价 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updatMeterRecord(MeterRecord record) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
		
		record.setOperatorId(userBean.getId());
		record.setOperatorName(userBean.getRealname());
		record.setOperationTime(new Date());
		
		String preRead = record.getPreRead();
		String currRead = record.getCurrRead();
		if(StringUtils.isNotBlank(preRead) && StringUtils.isNotBlank(currRead)) {
			BigDecimal preReadBd = new BigDecimal(preRead);
			BigDecimal currReadBd = new BigDecimal(currRead);
			BigDecimal currAmount = BigDecimalUtils.subtract(currReadBd, preReadBd);
			record.setCurrAmount(currAmount);
		}
		//设置抄表记录状态为手工修改
		record.setStatus(EnumMeterRecordStatus.MANUAL_EDIT.getValue());
		meterRecordService.updateByPrimaryKeySelective(record);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	/**
	 * @Title: initMeterRecord
	 * @Description: 初始化抄表记录
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/init-record", produces = "application/json")
	@ResponseBody
	public Object initMeterRecord(Model model) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
		
		int row = meterRecordService.initMeterRecord(userBean.getId(), userBean.getRealname());
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}
	
	//******************确认虚表记录*************************************
	/**
	 * @Title: loadConfirmVirtualRecordDialog
	 * @Description: 加载确认虚表对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-confirm-virtual-record-dialog")
	public String loadConfirmVirtualRecordDialog(Model model) {
		
		return TEMPLATE_PATH + "confirm_virtual_record_dialog";
	}
	
	/**
	 * @Title: confirmVirtualMeterRecord
	 * @Description: 确认虚表记录
	 * @param model
	 * @param traceIds
	 * @param period
	 * @return 
	 */
	@RequestMapping(value = "/confirm-virtual-record", produces = "application/json")
	@ResponseBody
	public Object confirmVirtualMeterRecord(Model model,String traceIds, String period) {
		//List<MeterRecord> recordList = meterRecordService.getVirtualMeterRecord(traceIds, period);
		
		try {
			int rows = meterRecordService.confirmVirtualMeter(period, traceIds);
			if(rows>0) {
				return RequestResultUtil.getResultSuccess("已确认本期未确认过的虚表记录!");
			}else if(rows==-1) {
				return RequestResultUtil.getResultSuccess("未查询到本期未确认过的虚表记录!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("确认虚表记录异常!");
		
	}
	
	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 
	 * 		为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;//操作员ID
		if (userBean!=null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for(SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}
			
			if(roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_METER_READER)!=-1) {
				operatorId = userBean.getId();//操作员ID
			}
			
		}
		return operatorId;
	}
	
	
	//------------------------上传照片-------------------	
	/**
	 * @Title: loadRecordPhotoItem
	 * @Description: 加载抄表记录照片
	 * @param model
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-meter-record-photo-item")
	public String loadRecordPhotoItem(Model model, Long customerId, String meterIds, String recordIds, String period) {
		
		//加载抄表记录照片
		//MeterRecordPhoto record = new MeterRecordPhoto();
		//record.setCustomerId(customerId);
		//record.setMeterId(meterId);
		//record.setPeriod(period);
		
		//抄表记录ID集合
		List<Long> recordIdList = new ArrayList<>();
		
		//把抄表记录ID字符串通过逗号分隔行到数组，再把数组中的每个值add到抄表记录ID集合中
		String[] recordIdArr = recordIds.split(",");
		for(String recordId : recordIdArr) {
			if(StringUtils.isNotBlank(recordId)) {
				recordIdList.add(Long.valueOf(recordId));
			}
		}
		
		Example example = new Example(MeterRecordPhoto.class);
		example.createCriteria().andIn("recordId", recordIdList);
		List<MeterRecordPhoto> recordPhotoList = meterRecordPhotoService.selectByExample(example);
		
		model.addAttribute("recordPhotoList", recordPhotoList);
		
		return TEMPLATE_PATH + "record_photo_table";
	}
	
	/**
	 * @Title: uploadPhotoDialog
	 * @Description: 加载上传照片
	 * @param model
	 * @param meterRecordId
	 * @return 
	 */
	@RequestMapping(value = "/loadphotodialog")
	public String uploadPhotoDialog(Model model, Long meterRecordId) {
		
		return TEMPLATE_PATH + "photo_dialog_edit";
	}
	
	/**
	 * @Title: upload
	 * @Description: 协议上传
	 * @param request
	 * @param model
	 * @param fileType
	 * @param inputName
	 * @param agreement
	 * @return 
	 */
	@RequestMapping(value = "/photo-upload")
	@ResponseBody
	public Object upload(HttpServletRequest request, Model model, String fileType, String inputName, String meterIds, String recordIds, Long customerId, String period) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date date = new Date();
		try {
			
			String[] meterIdArr = meterIds.split(",");//表计
			String[] recordIdArr = recordIds.split(",");//抄表记录
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			
			//上传抄表照片文件
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			MeterRecordPhoto meterRecordPhoto = new MeterRecordPhoto();
			if(filePathList!=null && filePathList.size()>0) {
				
				String filePath = filePathList.get(0);
				System.out.println("上传图片路径："+filePath);
				filePath = FileUploadUtil.subImgPath(filePathList.get(0));
				System.out.println("截取后上传图片路径："+filePath);
				meterRecordPhoto.setImagePath(filePath);
				meterRecordPhoto.setCustomerId(customerId);
				meterRecordPhoto.setPeriod(period);
				meterRecordPhoto.setRecordId(Long.valueOf(recordIdArr[0]));
				meterRecordPhoto.setMeterId(Long.valueOf(meterIdArr[0]));
				meterRecordPhoto.setOperationTime(date);
				meterRecordPhoto.setOperatorId(userBean.getId());
			}
			
			//保存抄表记录-照片到数据库
			int rows = meterRecordPhotoService.insertSelective(meterRecordPhoto);
			if(rows>0) {
				return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
	}
	
	/**
	 * @Title: deleteAgreement
	 * @Description: 删除协议
	 * @param request
	 * @param model
	 * @param id
	 * @param path
	 * @return 
	 */
	@RequestMapping(value = "/delete-photo")
	@ResponseBody
	public Object deleteAgreement(HttpServletRequest request, Model model, Long id, String path) {
		
		try {
			
			//删除抄表记录照片
			MeterRecordPhoto meterRecordPhoto = meterRecordPhotoService.selectByPrimaryKey(id);
			int rows = meterRecordPhotoService.delete(meterRecordPhoto);
			if(rows>0) {
				return RequestResultUtil.getResultDeleteSuccess();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultDeleteWarn();
	}
	
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
	
	
	//------------------------------	导出抄表记录Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-meter-record-excel")
	public void exportMeterRecordExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer readType, String traceIds, String period, Integer isAddSubWater, Integer isPartWater, String startDate, String endDate, Integer currAmount) {
		
		//String period = DateUtils.getYearMonth();//期间默认本月
		//Long operatorId = this.getOperatorId();
		//excel标题
		String[] titles = { "房间号", "客户姓名", "期间", "上期抄表日期", "上期抄表读数", "本期抄表日期", "本期抄表读数", "本期水量", "抄表方式", "备注"};
		//excel列名
		String[] columnNames = { "room", "customerName", "period", "preDateStr", "preRead", "currDateStr", "currRead", "currAmount", "readModeStr", "remark"};
		//sheet名
		String sheetName = period+"抄表记录";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate, currAmount);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(period, traceIds);
		
		File file = new File(realPath+fileName);
		
		//文件输出流
	    FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    try {
			wb.write(outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    System.out.println("导出文件成功！文件导出路径：--"+file);
	    
	    try {
			DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * @Title: getExportExcelData
	 * @Description: 获取导出excel
	 * @return 
	 */
	private List<Map<String, Object>> getExportExcelData(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer currAmount){
		List<Map<String, Object>> recordMapList = meterRecordService.getExportMeterRecordData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate, currAmount);
		
		//TODO 导出数据进行排序
		//this.sort(recordMapList);
		
		for(Map<String, Object> recordMap : recordMapList) {
			String room = "";//ROOM
			if(recordMap.get("ROOM") != null) {
				room = recordMap.get("ROOM").toString();
			}
			
			//String currTraceIds = recordMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			String customerId = recordMap.get("CUSTOMER_ID").toString();//客户ID
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			
			//String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			recordMap.put("room", room);
			recordMap.put("customerName", customer.getCustomerName());
			recordMap.put("period", period);
			if(recordMap.get("PRE_DATE") != null) {
				recordMap.put("preDateStr", recordMap.get("PRE_DATE").toString());
			}
			if(recordMap.get("PRE_READ") != null) {
				recordMap.put("preRead", recordMap.get("PRE_READ").toString());
			}
			if(recordMap.get("CURR_DATE") != null) {
				recordMap.put("currDateStr",recordMap.get("CURR_DATE").toString());
			}
			if(recordMap.get("CURR_READ") != null) {
				recordMap.put("currRead",recordMap.get("CURR_READ").toString());
			}
			if(recordMap.get("CURR_AMOUNT") != null) {
				recordMap.put("currAmount",recordMap.get("CURR_AMOUNT").toString());
			}
			if(recordMap.get("READ_MODE") != null) {
				recordMap.put("readModeStr",EnumReadMode.getName(recordMap.get("READ_MODE").toString()));
			}
			
			if(recordMap.get("REMARK") != null) {
				recordMap.put("remark",recordMap.get("REMARK").toString());
			}
			
		}
		
		return recordMapList;
	}
	
	/**
	 * @Title: sort
	 * @Description: List排序 TODO 
	 * @param recordMapList 
	 */
//	private void sort(List<Map<String, Object>> recordMapList) {
//		Collections.sort(recordMapList, new Comparator<Map<String, Object>>() {
//
//			@Override
//			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//				int diff = o1.get("comparte").toString() - o2.get("comparte").toString();
//					if (diff > 0) {
//						return 1;
//					}else if (diff < 0) {
//						return -1;
//					}
//					return 0; //相等为0
//			}
//		});
//	}
	
	/**
	 * 获取文件路径
	 * @param request
	 * @return
	 */
	private String getRealPath(HttpServletRequest request){
		String realPath = this.getPath();
		realPath = realPath+File.separator+"export excel"+File.separator;
		
		File temp = new File(realPath);
		//如果文件路径不存在，则创建
		if(!temp.exists()){
			temp.mkdirs();
		}
		return realPath;
	}
	/**
	 * 获取导出EXCEL文件名
	 * @return
	 */
	private String getFileName(String period, String traceIds){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(new Date());
		String place = locationService.getPlace(traceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
		// excel文件名
		String fileName = period;
		
		if(StringUtils.isNotBlank(place)) {
			fileName = fileName+"-"+place;
		}
		
		fileName = fileName+"-"+times+"-"+"抄表记录"+".xls";
		
		return fileName;
	}
	
	//------------------------------	上传文件通用方法	------------------------------
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
	
	public static void main(String[] args) {
		String path = "d:/upload/img/20190701/abc.jpg";
		//String path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("windows:"+path);
		path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("linux:"+path);
		
		String[] ids = new String[3];
		ids[0]="1";
		ids[1]="2";
		ids[2]="3";
		System.out.println("数组toString:"+ids.toString());
	}
	

}