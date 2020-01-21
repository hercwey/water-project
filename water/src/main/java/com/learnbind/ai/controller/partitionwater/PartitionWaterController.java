package com.learnbind.ai.controller.partitionwater;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: GeneratorBillController.java
 * @Description: 开账
 *
 * @author Administrator
 * @date 2019年8月16日 上午11:11:38
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/partition-water")
public class PartitionWaterController {
	private static Log log = LogFactory.getLog(PartitionWaterController.class);
	private static final String TEMPLATE_PATH = "partition_water/"; // 页面目录

	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private MeterRecordService meterRecordService;  //抄表记录服务
	@Autowired
	private MeterRecordPhotoService meterRecordPhotoService;//抄表记录照片服务
	@Autowired
	private MetersService metersService;  //表计服务
	@Autowired
	private CustomersService customersService;  //客户服务
	@Autowired
	private CustomerMeterService customerMeterService;  //客户服务
	@Autowired
	private PartitionWaterService partitionWaterService;  //分水量服务
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private AddSubWaterService addSubWaterService;//追加减免水量
	@Autowired
	private CustomerAccountService customerAccountService;//客户-账户
	@Autowired
	private CustomerAccountItemService customerAccountItemService;  //客户账单信息
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户
	@Autowired
	private LocationMeterService locationMeterService;//地理位置-表计
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
		return TEMPLATE_PATH + "partition_water_starter";
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
		
		return TEMPLATE_PATH + "partition_water_main";
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
	 * @Description: 加载列表页面
	 * @param model			ModelView中传递数据的对象
	 * @param pageNum		页号
	 * @param pageSize		页大小
	 * @param searchCond	用户输入的查询条件
	 * @param traceIds		地理位置-traceIds
	 * @param isMakeBill	开账状态，是否已开账：0=未开账（默认值）；1=已开账
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String meterRecordTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, Integer isMakeBill, Integer isPartWater, String startDate, String endDate, String period) {

		//判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> partitionWaterMapList = partitionWaterService.getPartitionWaterMapList(null, searchCond, traceIds, isMakeBill, isPartWater, startDate, endDate, period);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(partitionWaterMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		String totalWaterAmount = "";//总水量
		String totalWaterTee = "";//总金额
		//获取统计的水费及水量
		Map<String, Object> statisticMap = partitionWaterService.getPartitionWaterStatisticMap(null, searchCond, traceIds, isMakeBill, isPartWater, startDate, endDate, period);
		if(statisticMap != null) {
			totalWaterAmount = statisticMap.get("TOTAL_WATER_AMOUNT").toString();
			totalWaterTee = statisticMap.get("TOTAL_WATER_FEE").toString();
		}
		
		for(Map<String, Object> partitionWaterMap : partitionWaterMapList) {
			String priceCode = (String)partitionWaterMap.get("WATER_USE");
			String customerId = partitionWaterMap.get("CUSTOMER_ID").toString();//客户ID
			String currTraceIds = partitionWaterMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			partitionWaterMap.put("place", place);
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			partitionWaterMap.put("customer", customer);
			
			String customerName = partitionWaterMap.get("CUSTOMER_NAME").toString();
			String currperiod = partitionWaterMap.get("PERIOD").toString();
			
			Meters meter = metersService.selectByPrimaryKey(Long.valueOf(partitionWaterMap.get("METER_ID").toString()));
			//Integer customerType = Integer.valueOf(partitionWaterMap.get("CUSTOMER_TYPE").toString());
			//String customerName = "";
//			if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue()) {//如果是单位用户
//				customerName = meter.getPlace();
//			} else {
//				customerName = partitionWaterMap.get("CUSTOMER_NAME").toString();
//			}
			//if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue()) {//如果是单位用户
				//Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterId));
				String des = meter.getDescription();//表计描述
				if(StringUtils.isNotBlank(des)) {
					customerName = customerName+"（"+meter.getDescription()+"）";
				}
			//}
			partitionWaterMap.put("meterPlace", customerName);
			
			//partitionWaterMap.put("waterUseValue", this.getPriceTypeName(customer.getWaterUse()));//用水性质名称
			partitionWaterMap.put("waterUseValue", this.getLadderName(priceCode));//水价名称
			//获取分水量规则
			String partWaterRule = this.getPartWaterRule(Long.valueOf(partitionWaterMap.get("ID").toString()));
			partitionWaterMap.put("partWaterRule", partWaterRule);
			
			//String waterAmount = partitionWaterMap.get("WATER_AMOUNT").toString();//水量
			//String waterFee = partitionWaterMap.get("WATER_FEE").toString();//水费
			//totalWaterAmount = BigDecimalUtils.add(totalWaterAmount, new BigDecimal(waterAmount));//统计总水量，用于列表统计
			//totalWaterTee = BigDecimalUtils.add(totalWaterTee, new BigDecimal(waterFee));//水量
			
			//是否追加减免水量
			boolean isAddSubWater = false;
			AddSubWater log = addSubWaterService.getAddSubLog(Long.valueOf(customerId), currperiod, Long.valueOf(partitionWaterMap.get("ID").toString()));
			if(log!=null) {
				isAddSubWater = true;
			}
			partitionWaterMap.put("isAddSubWater", isAddSubWater);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("partitionWaterMapList", partitionWaterMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		//回传总水量和总费用
		model.addAttribute("totalWaterAmount", totalWaterAmount);
		model.addAttribute("totalWaterFee", totalWaterTee);
		
		return TEMPLATE_PATH + "partition_water_table";
	}
	
	/**
	 * @Title: getPartWaterRule
	 * @Description: 根据分水量ID获取分水量规则
	 * @param partWaterId
	 * @return 
	 */
	private String getPartWaterRule(Long partWaterId) {
		//查询抄表记录使用的分水量规则
		List<MeterPartWaterRuleTrace> ruleTraceList = meterPartWaterRuleTraceService.getPWaterRuleTraceByPartWaterId(partWaterId);
		String partWaterRule = "";
		if(ruleTraceList.size() <= 0) {
			return partWaterRule; 
		}
		for(MeterPartWaterRuleTrace trace : ruleTraceList) {
			String currRule = trace.getRuleReal();
			if(StringUtils.isBlank(currRule)) {
				continue;
			}
			currRule = partitionRuleUtil.getRealExpression(currRule);//去规则中的分隔符号，返回表达式
			List<String> formalParamList = partitionRuleUtil.getRuleFormalParams(currRule);//获取表达式中的形参
			Map<String, String> actualParamMap = this.getActualParamMap(formalParamList);//获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
			String showExpression = partitionRuleUtil.setRuleActualParams(actualParamMap, currRule);//实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
			if(StringUtils.isNotBlank(showExpression)) {
//				//String html = "<p>"+currRule+"</p>";
//				String html = showExpression+"<br>";
//				partWaterRule = html+partWaterRule;
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
	 * @Title: getPriceTypeName
	 * @Description: 获取用水性质名称
	 * @param priceTypeCode
	 * @return 
	 */
	private String getPriceTypeName(String priceTypeCode) {
		String priceTypeName = waterPriceService.getPriceTypeName(priceTypeCode);
		if(priceTypeName!=null) {
			return priceTypeName;
		}
		return null;
	}
	
	/**
	 * @Title: getLadderName
	 * @Description: 获取水价名称
	 * @param waterUse
	 * @param priceCode
	 * @return 
	 */
	private String getLadderName(String priceCode) {
		
//		String ladder = "阶梯水价";
//		if(waterUse.equalsIgnoreCase(WaterPriceConstant.JMSHYS)) {
//			return ladder;
//		}
		if(StringUtils.isBlank(priceCode)) {
			return null;
		}
		SysWaterPrice wp = new SysWaterPrice();
		wp.setPriceCode(priceCode);
		List<SysWaterPrice> wpList = waterPriceService.select(wp);
		if(wpList!=null && wpList.size()>0) {
			wp = wpList.get(0);
			return wp.getLadderName();
		}
		return null;
	}
	
	//------------------------------	处理账单业务	------------------------------
	//------------------------------	处理分水量业务	------------------------------
	
	/** 
	*	@Title: loadPartitionDialog 
	*	@Description: 加载分水量对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadpartitiondialog")
	public String loadPartitionDialog(Model model, Long partWaterId, Long customerId, String period, String recordIds) {
		
		//调用水价配置接口获取用水性质
		List<SysWaterPrice> waterPriceList = waterPriceService.getNotJmshysPriceList();
		model.addAttribute("waterPriceList", waterPriceList);
		
//		PartitionWater pw = new PartitionWater();
//		pw.setCustomerId(customerId);
//		pw.setPeriod(period);
//		pw.setRecordId(recordIds);
//		pw.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
//		pw.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//		List<PartitionWater> pwList = partitionWaterService.select(pw);
//		model.addAttribute("pwList", pwList);
		
		
		//List<MeterPartWaterRuleTrace> ruleTraceList = partitionWaterService.getMeterPartWaterRuleTraceList(customerId, period, recordIds);
		List<MeterPartWaterRuleTrace> ruleTraceList = meterPartWaterRuleTraceService.getPWaterRuleTraceByPartWaterId(partWaterId);
		model.addAttribute("ruleList", ruleTraceList);
		
		model.addAttribute("partWaterId", partWaterId);//传参分水量ID到页面
		
		//return TEMPLATE_PATH + "partition_water_dialog";
		return TEMPLATE_PATH + "part_water_rule_dialog";
	}
	
	/**
	 * @Title: savePartitionWater
	 * @Description: 保存分水量
	 * @param model
	 * @param recordIds
	 * @param meterIds
	 * @param customerId
	 * @param period
	 * @param partitionWaterJSON
	 * @return 
	 */
//	@RequestMapping(value = "/save-partition-water", produces = "application/json")
//	@ResponseBody
//	public Object savePartitionWater(Model model, Long customerId, String period, String recordIds, String meterIds, String partitionWaterJSON) {
//		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if (userBean==null) {
//			return RequestResultUtil.getResultAddWarn("请重新登录!");
//		}
//		
//		//List<PartitionWater> pwList = JSON.parseArray(partitionWaterJSON, PartitionWater.class);
//		//int rows = partitionWaterService.saveList(recordIds, meterIds, customerId, period, pwList);
////			obj.waterPriceId = waterPriceId;
////			obj.waterAmount = waterAmount;
////			obj.waterPrice = waterPrice;
////			obj.waterUse = waterUse;
//		//json array 格式：[{waterPriceId:waterPriceId, waterAmount:waterAmount, waterPrice:waterPrice, waterUse:waterUse}]
//		JSONArray arr = JSON.parseArray(partitionWaterJSON);
//		
//		int rows = partitionWaterService.saveList(recordIds, meterIds, customerId, period, arr);
//		if (rows>0) {
//			return RequestResultUtil.getResultSaveSuccess("保存分水量数据成功！");			
//		}
//		return RequestResultUtil.getResultSaveWarn("保存分水量数据异常，请重新尝试！");
//		
//	}
	
	/**
	 * @Title: savePartWaterRule
	 * @Description: 保存分水量
	 * @param model
	 * @param meterId
	 * @param meterPartWaterJson
	 * 				var obj = new Object();
					//obj.meterId = meterId;//表计ID
					obj.ruleReal = ruleRealStr;//实际使用的规则
					obj.waterPriceId = waterPriceId;//水价ID
					obj.isDefault = 0;//是否默认，0=是；1=否；
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save-partition-water", produces = "application/json")
	@ResponseBody
	//public Object savePartWaterRule(Model model, Long partWaterId, Long customerId, String period, String recordIds, String meterIds, String meterPartWaterJson) throws Exception {
	public Object savePartWaterRule(Model model, Long partWaterId, String meterPartWaterJson) throws Exception {
		try {
			
			List<MeterPartWaterRuleTrace> ruleTraceList = JSON.parseArray(meterPartWaterJson, MeterPartWaterRuleTrace.class);
			
			int rows = partitionWaterService.savePartitionWater(partWaterId, ruleTraceList);
			
			if (rows>0) {
				return RequestResultUtil.getResultSaveSuccess("保存成功！");
			}
			return RequestResultUtil.getResultSaveWarn("保存异常，请重新操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultSaveWarn("保存异常，请重新操作！");

	}
	
	//------------------------增加减免水量选项卡-------------------	
	/**
	 * @Title: loadAddsubWaterDialog
	 * @Description: 加载追加减免水量对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadaddsubwaterdialog")
	public String loadAddsubWaterDialog(Model model, Long partWaterId) {
		
		//查询分水量数据
		PartitionWater partWater = partitionWaterService.selectByPrimaryKey(partWaterId);
		
		//当前总水量
		BigDecimal totalWaterAmount = new BigDecimal(0.00);
		if(partWater!=null) {
			totalWaterAmount = partWater.getRealWaterAmount();
		}
		model.addAttribute("totalWaterAmount", totalWaterAmount);
		return TEMPLATE_PATH + "addsub_water_dialog";
	}
	
	
	/**
	 * @Title: saveAddsubWater
	 * @Description: 保存追加减免水量
	 * @param model
	 * @param customerId
	 * @param meterId
	 * @param addsubWaterJSON
	 * @return 
	 */
	@RequestMapping(value = "/save-addsub-water", produces = "application/json")
	@ResponseBody
	public Object saveAddsubWater(Model model, Long partWaterId, String addsubWaterJSON) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
		//List<AddSubWater> awList = JSON.parseArray(addsubWaterJSON, AddSubWater.class);
//			obj.compensation = compensation;
//			obj.waterBefore = waterBefore;
//			obj.partitionWaterId = partitionWaterId;
		//json array 格式：[{compensation:compensation, waterBefore:waterBefore, partitionWaterId:partitionWaterId}]
		JSONObject addsubWaterObj = JSON.parseObject(addsubWaterJSON);
		//int rows = addSubWaterService.saveList(customerId, period, meterIds, awList, recordIds);
		int rows = addSubWaterService.saveAddSubWater(partWaterId, addsubWaterObj);
		if (rows>0) {
			return RequestResultUtil.getResultSaveSuccess("追加/减免水量成功！");			
		}else if(rows==-1) {
			return RequestResultUtil.getResultSaveWarn("追加/减免水量数据错误，请检查追加减免水量值和客户档案和表计档案的状态！");
		}
		return RequestResultUtil.getResultSaveWarn("追加/减免水量数据异常，请重新尝试！");
		
	}
	
	//----------------------------其他辅助性函数------------------------------------------------------------------------------
	/**
	 * @Title: getAccountId
	 * @Description: 获取账户ID
	 * @param customerId
	 * @return 
	 */
	private Long getAccountId(Long customerId) {
		//客户-账户信息
		CustomerAccount ca = new CustomerAccount();
		ca.setCustomerId(customerId);
		ca =  customerAccountService.selectOne(ca);
		return ca.getId();
	}
	
	/**
	 * @Title: deleteBill
	 * @Description: 删除账单
	 * @param recordId 
	 */
	private void deleteBill(Long customerId, Long accountId, String period) {
		CustomerAccountItem item = new CustomerAccountItem();
		item.setCustomerId(customerId);
		item.setAccountId(accountId);
		item.setPeriod(period);
		customerAccountItemService.delete(item);
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
	
	/**
	 * @Title: delete
	 * @Description: 删除分水量
	 * @param model
	 * @param partWaterIdJSON
	 * @return 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(Model model, String partWaterIdJSON) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}

		if(StringUtils.isBlank(partWaterIdJSON)) {
			return RequestResultUtil.getResultSaveWarn("没有查询到未开账的分水量记录！");
		}
		
		List<Long> partWaterIdList = JSON.parseArray(partWaterIdJSON, Long.class);
		int rows = partitionWaterService.delete(partWaterIdList);
		if (rows>0) {
			return RequestResultUtil.getResultSaveSuccess("已删除未开账的分水量记录！");			
		}else if(rows==-1) {
			return RequestResultUtil.getResultSaveWarn("没有查询到未开账的分水量记录！");
		}
		return RequestResultUtil.getResultSaveWarn("删除异常，请重新尝试！");
		
	}
	
}