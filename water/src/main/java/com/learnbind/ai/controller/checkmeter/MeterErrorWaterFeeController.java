package com.learnbind.ai.controller.checkmeter;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.DiscountWaterFeeTraceService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.sms.SMSService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.checkmeter
 *
 * @Title: MeterErrorWaterFeeController.java
 * @Description: 异常水费报警
 *
 * @author Thinkpad
 * @date 2019年8月5日 下午5:56:08
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/error-water-fee")
public class MeterErrorWaterFeeController {
	private static Log log = LogFactory.getLog(MeterErrorWaterFeeController.class);
	private static final String TEMPLATE_PATH = "check_meter/error_water_fee/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService; // 客户信息
	@Autowired
	private CustomerAccountService customerAccountService;// 客户-账户服务
	@Autowired
	private CustomerOverdueFineService customerOverdueFineService;// 客户-违约金
	@Autowired
	private DiscountFineTraceService discountFineTraceService;// 客户-违约金减免日志
	@Autowired
	private DiscountWaterFeeTraceService discountWaterFeeTraceService;// 客户-水费减免日志
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private BankService bankService;//客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计绑定关系表
	@Autowired
	private SMSService smsService;//发送短信
	@Autowired
	private MeterPartWaterRuleTraceService meterPartWaterRuleTraceService;//分水量规则配置日志表
	@Autowired
	private PartitionRuleUtil partitionRuleUtil;//分水量规则工具类
	@Autowired
	private PartitionWaterService partitionWaterService;  //分水量服务
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private MetersService metersService;  //表计服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {

		return TEMPLATE_PATH + "account_starter";
	}

	/**
	 * @Title: main
	 * @Description: 加载客户账单信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {

		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "account_main";
	}

	/**
	 * @Title: table
	 * @Description: 客户账单信息table
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param period
	 * @param locationCode
	 * @param settlementStatus
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
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
		List<Map<String, Object>> partitionWaterMapList = partitionWaterService.searchCustomerAccountItemErrorFee(null, searchCond, traceIds, isMakeBill, isPartWater, startDate, endDate, period);
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
			
			Meters meter = metersService.selectByPrimaryKey(Long.valueOf(partitionWaterMap.get("METER_ID").toString()));
			String des = meter.getDescription();//表计描述
			if(StringUtils.isNotBlank(des)) {
				customerName = customerName+"（"+meter.getDescription()+"）";
			}
			partitionWaterMap.put("meterPlace", customerName);
			
			partitionWaterMap.put("waterUseValue", this.getLadderName(priceCode));//水价名称
			//获取分水量规则
			String partWaterRule = this.getPartWaterRule(Long.valueOf(partitionWaterMap.get("ID").toString()));
			partitionWaterMap.put("partWaterRule", partWaterRule);
			
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
		
		return TEMPLATE_PATH + "account_table";
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

	
	


	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;// 操作员ID
		if (userBean != null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for (SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}

			if (roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_METER_READER) != -1) {
				operatorId = userBean.getId();// 操作员ID
			}

		}
		return operatorId;
	}
	
	//------------------------------	导出异常水量Excel	------------------------------
		/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-data-excel")
	public void exportMeterRecordExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer isMakeBill, String traceIds, String period) {
		
		//String period = DateUtils.getYearMonth();//期间默认本月
		//Long operatorId = this.getOperatorId();
		//excel标题
		String[] titles = { "地理位置", "客户姓名", "期间", "用水性质", "基础水价", "污水处理费", "总水价", "水量", "水费", "备注"};
		//excel列名
		String[] columnNames = { "place", "customerName", "period", "waterUse", "basePrice", "treatmentFee", "waterPrice", "realWaterAmount", "waterFee", "remark"};
		//sheet名
		String sheetName = period+"抄表记录";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, isMakeBill, traceIds, period);
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
	private List<Map<String, Object>> getExportExcelData(String searchCond, Integer isMakeBill, String traceIds, String period){
		List<Map<String, Object>> recordMapList = partitionWaterService.searchCustomerAccountItemErrorFee(null, searchCond, traceIds, isMakeBill, null, null, null, period);
		//TODO 导出数据进行排序
		//this.sort(recordMapList);
		
		for(Map<String, Object> recordMap : recordMapList) {
			String place = "";//ROOM
			
			String currTraceIds = recordMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			place = locationService.getPlace(currTraceIds);
			String customerName = recordMap.get("CUSTOMER_NAME").toString();//客户姓名
			
			//String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			recordMap.put("place", place);
			recordMap.put("customerName", customerName);
			recordMap.put("period", period);
			String waterUse = "";
			if(recordMap.get("WATER_USE") != null) {
				String priceCode = (String)recordMap.get("WATER_USE");
				recordMap.put("preDateStr", this.getLadderName(priceCode));
			}
			if(recordMap.get("BASE_PRICE") != null) {
				recordMap.put("basePrice", recordMap.get("BASE_PRICE").toString());
			}
			if(recordMap.get("TREATMENT_FEE") != null) {
				recordMap.put("treatmentFee",recordMap.get("TREATMENT_FEE").toString());
			}
			if(recordMap.get("WATER_PRICE") != null) {
				recordMap.put("waterPrice",recordMap.get("WATER_PRICE").toString());
			}
			if(recordMap.get("REAL_WATER_AMOUNT") != null) {
				recordMap.put("realWaterAmount",recordMap.get("REAL_WATER_AMOUNT").toString());
			}
			if(recordMap.get("WATER_FEE") != null) {
				recordMap.put("waterFee",EnumReadMode.getName(recordMap.get("WATER_FEE").toString()));
			}
			if(recordMap.get("REMARK") != null) {
				recordMap.put("remark",recordMap.get("REMARK").toString());
			}
			
		}
		
		return recordMapList;
	}
	
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
	
	


}