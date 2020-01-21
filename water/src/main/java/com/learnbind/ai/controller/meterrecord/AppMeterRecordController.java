package com.learnbind.ai.controller.meterrecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAppReadResult;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterRecordTemp;
import com.learnbind.ai.model.MeterRecordTempPhoto;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;



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
@RequestMapping(value = "/app-meter-record")
public class AppMeterRecordController {
	//private static Log log = LogFactory.getLog(AppMeterRecordController.class);
	private static final String TEMPLATE_PATH = "meter_record/app_meter_record/"; // 页面目录角色
	private static final int PAGE_SIZE = 15; // 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private MeterRecordTempService meterRecordTempService;  //app抄表记录服务
	@Autowired
	private MeterRecordService meterRecordService;  //抄表记录服务
	@Autowired
	private MetersService metersService;  //表计服务
	@Autowired
	private CustomersService customersService;  //客户服务
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private MeterRecordTempPhotoService meterRecordTempPhotoService;//抄表记录临时照片表服务
	@Autowired
	private WaterPriceService WaterPriceService;  //水价
	@Autowired
	private BankService bankService;//客户银行信息

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
	public String RecordStarter(Model model) {
		return TEMPLATE_PATH + "app_record_starter";
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
	public String meterRecordSearch(Model model) {
		//加载数据字典-抄表方式
		//List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		//抄表方式
		EnumReadMode[] readModeList = EnumReadMode.values();
		//APP抄表结果状态
		EnumAppReadResult[] appReadResult = EnumAppReadResult.values();
		
		model.addAttribute("readModeList", readModeList);
		model.addAttribute("appReadResult", appReadResult);
		
		return TEMPLATE_PATH + "app_record_main";
	}

	/**
	 * @Title: appMeterRecordTable
	 * @Description: app抄表记录
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String appMeterRecordTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer readResult, String readMode,  String traceIds, String startDate, String endDate, Integer removeRepeat) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//加载数据字典-抄表方式
		//List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		
		//判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = userBean.getId();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		if(operatorId == 1) {
			operatorId = null;
		}
		if(StringUtils.isNotBlank(searchCond)) {
			searchCond = searchCond.trim();
		}
		List<Map<String, Object>> customerList = new ArrayList<>();
		if(removeRepeat == 1) {//只查询重复数据
			customerList = meterRecordTempService.getRepeatListGroupByCustomer(null, searchCond, readResult, readMode, null, traceIds, startDate, endDate);
		} else {
			customerList = meterRecordTempService.getListGroupByCustomer(null, searchCond, readResult, readMode, null, traceIds, startDate, endDate);
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		for(Map<String, Object> customer : customerList) {
			
//			String meterIds = customer.get("METER_IDS").toString();//表计ID，多个表计ID用逗号分隔
//			String[] meterIdArr = meterIds.split(",");
//			customer.put("METER_SIZE", meterIdArr.length);
			
			String currTraceIds = (String)customer.get("TRACE_IDS");//地理位置痕迹ID（用英文减号分隔）
			String customerId = customer.get("CUSTOMER_ID").toString();//客户ID
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
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			customer.put("place", place);
			
			
			customer.put("meterPlace", customerName);
			//String curReadType = customer.get("READ_TYPE").toString();//抄表类型
			Date curPreDate = (Date)customer.get("PRE_DATE");//上期抄表日期
			Date curCurrDate = (Date)customer.get("CURR_DATE");//本期抄表日期
			String curReadResult = customer.get("READ_RESULT").toString();//抄表类型
//			if(StringUtils.isNotBlank(curReadType)) {
//				customer.put("readTypeStr", EnumReadType.getName(Integer.valueOf(curReadType)));
//			}else {
//				customer.put("readTypeStr", "");
//			}
			if(StringUtils.isNotBlank(curReadResult)) {
				customer.put("readResultStr", EnumAppReadResult.getName(Integer.valueOf(curReadResult)));
			}else {
				customer.put("readResultStr", "");
			}
			customer.put("preDateStr", DateUtils.formatDate(curPreDate, "yyyy-MM-dd HH:mm:ss"));
			
			String currDateStr = null;
			if(curCurrDate!=null) {
				currDateStr = DateUtils.formatDate(curCurrDate, "yyyy-MM-dd HH:mm:ss");
			}
			customer.put("currDateStr", currDateStr);
			
			//获取抄表记录的照片
			String imagePath = this.getAppMeterRecordPhoto(Long.valueOf(customerId), Long.valueOf(meterId));
			customer.put("imagePath", imagePath);
			
			
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "app_record_table";
	}
	
	
	/**
	 * @Title: getAppMeterRecordPhoto
	 * @Description: 获取APP抄表记录的照片
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	private String getAppMeterRecordPhoto(Long customerId, Long meterId) {
		MeterRecordTempPhoto tempPhoto = new MeterRecordTempPhoto();
		tempPhoto.setCustomerId(customerId);
		tempPhoto.setMeterId(meterId);
		List<MeterRecordTempPhoto> tempPhotoList = meterRecordTempPhotoService.select(tempPhoto);
		if(tempPhotoList!=null && tempPhotoList.size()>0) {
			tempPhoto = tempPhotoList.get(0);
			return tempPhoto.getImagePath();
		}
		return null;
	}
	/**
	 * @Title: getReadModeStr
	 * @Description: 获取抄表方式字符串
	 * @param readModeList
	 * @param readMode
	 * @return 
	 */
//	private String getReadModeStr(List<DataDict> readModeList, String readMode) {
//		String readModeStr = null;
//		for(DataDict dict : readModeList) {
//			String key = dict.getKey();
//			if(key.equals(readMode)) {
//				readModeStr = dict.getValue();
//				break;
//			}
//		}
//		return readModeStr;
//	}
	
	/**
	 * @Title: loadModiAppRecordDialog
	 * @Description: 加载编辑对话框
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiAppRecordDialog(Long itemId, Model model) {
		//读取需要编辑的条目
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		
		model.addAttribute("readModeList", readModeList);
		MeterRecordTemp currItem=meterRecordTempService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "app_record_dialog_edit";
	}

	/** 
	*	@Title: updat 
	*	@Description: 修改
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(Long appRecordId, String preRead, String preDate, String currRead, String currDate) throws Exception {
//		MeterRecordTemp meterRecordTemp = meterRecordTempService.selectByPrimaryKey(appRecordId);
//		meterRecordTemp.setCurrRead(currRead);
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
//		Date currDateTemp = format.parse(currDate);
//		meterRecordTemp.setCurrDate(currDateTemp);
//		BigDecimal currReadTemp = new BigDecimal(currRead);
//		BigDecimal preRead = new BigDecimal("0");
//		if(StringUtils.isNotBlank(meterRecordTemp.getPreRead())) {
//			preRead = new BigDecimal(meterRecordTemp.getPreRead());
//		}
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		
		MeterRecordTemp meterRecordTemp = new MeterRecordTemp();
		meterRecordTemp.setId(appRecordId);
		
		meterRecordTemp.setPreRead(preRead);
		Date preDateTemp = format.parse(preDate);
		meterRecordTemp.setPreDate(preDateTemp);
		
		meterRecordTemp.setCurrRead(currRead);
		Date currDateTemp = format.parse(currDate);
		meterRecordTemp.setCurrDate(currDateTemp);
		
		BigDecimal currAmount= BigDecimalUtils.subtract(new BigDecimal(currRead), new BigDecimal(preRead)); 
		meterRecordTemp.setCurrAmount(currAmount);
		meterRecordTemp.setReadResult(EnumAppReadResult.RESULT_MANUAL_EDIT.getValue());
		meterRecordTempService.updateByPrimaryKeySelective(meterRecordTemp);
		return RequestResultUtil.getResultUpdateSuccess();
	}
//	public Object update(Long appRecordId, String currRead, String currDate) throws Exception {
//		MeterRecordTemp meterRecordTemp = meterRecordTempService.selectByPrimaryKey(appRecordId);
//		meterRecordTemp.setCurrRead(currRead);
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
//		Date currDateTemp = format.parse(currDate);
//		meterRecordTemp.setCurrDate(currDateTemp);
//		BigDecimal currReadTemp = new BigDecimal(currRead);
//		BigDecimal preRead = new BigDecimal("0");
//		if(StringUtils.isNotBlank(meterRecordTemp.getPreRead())) {
//			preRead = new BigDecimal(meterRecordTemp.getPreRead());
//		}
//		BigDecimal currAmount= BigDecimalUtils.subtract(currReadTemp, preRead); 
//		meterRecordTemp.setCurrAmount(currAmount);
//		meterRecordTemp.setReadResult(EnumAppReadResult.RESULT_MANUAL_EDIT.getValue());
//		meterRecordTempService.updateByPrimaryKeySelective(meterRecordTemp);
//		return RequestResultUtil.getResultUpdateSuccess();
//	}

	
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
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			
			for (Long id : ids) {
				//System.out.println(id);
				meterRecordTempService.deleteAppMeterRecord(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
			
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	/**
	 * @Title: loadConfirmRecordDialog
	 * @Description: 确认抄表记录
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadconfirmrecorddialog")
	public String loadConfirmRecordDialog(Model model, String searchCond, String traceIds, String idJSON) {
		String maxPeriod ="";
		//判断是否勾选了抄表记录进行确认
		if(StringUtils.isNotBlank(idJSON)) {
			String[] s1 = idJSON.split(",");
			for(String appRecordId : s1) {
				MeterRecordTemp recordTemp = meterRecordTempService.selectByPrimaryKey(Long.valueOf(appRecordId));
				maxPeriod = meterRecordService.getMeterRecordMaxPeriod(searchCond, traceIds, recordTemp.getMeterId());
			} 
		} else {
			maxPeriod = meterRecordService.getMeterRecordMaxPeriod(searchCond, traceIds, null);
		}
		
		String defaultConfirmPeriod = maxPeriod;//默认确认期间
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//注意月份是MM
			Date date = sdf.parse(maxPeriod);
			Calendar c=Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MONTH, 1);
			String period = sdf.format(c.getTime());
			defaultConfirmPeriod = period;
		} catch (ParseException e) {
			e.printStackTrace();
			defaultConfirmPeriod = maxPeriod;
		}
		model.addAttribute("maxPeriod",maxPeriod);
		model.addAttribute("defaultConfirmPeriod",defaultConfirmPeriod);
		return TEMPLATE_PATH + "confirm_record_dialog";
	}
	
	/**
	 * @Title: confirmMeterRecord
	 * @Description: 确认抄表记录
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/confirm-record", produces = "application/json")
	@ResponseBody
	public Object confirmMeterRecord(Model model,String searchCond, Integer readResult, String readMode, String traceIds, String appRecordJSON,  String period) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = userBean.getId();
		if(operatorId == 1) {
			operatorId = null;
		}
		List<MeterRecordTemp> recordTempList = new ArrayList<>();
		if(StringUtils.isNoneBlank(appRecordJSON)) {
			List<Long> appRecordList = JSON.parseArray(appRecordJSON, Long.class);
			for(Long appRecordId : appRecordList) {
				MeterRecordTemp recordTemp = meterRecordTempService.selectByPrimaryKey(appRecordId);
				recordTempList.add(recordTemp);
			}
			
		} else {
			recordTempList = meterRecordTempService.getConfirmAppMeterRecord(null, searchCond, readResult, readMode, null, traceIds);
			
		}
		
		int rows = meterRecordTempService.confirmMeterRecord(userBean.getId(), userBean.getRealname(), recordTempList, period);
		if(rows>0) {
			return RequestResultUtil.getResultSuccess("已确认抄表正常的记录!");
		}else if(rows==0) {
			return RequestResultUtil.getResultFail("确认抄表记录异常，请检查后再次确认!");
		}else {
			return RequestResultUtil.getResultFail("没有需要确认的抄表记录!");
		}

	}
	
	//------------------------------	导出抄表记录Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-meter-record-excel")
	public void exportMeterRecordExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer readResult, String traceIds, Integer readMode) {
		
		String period = DateUtils.getYearMonth();//期间默认本月
		
		//excel标题
		String[] titles = { "房间号", "客户姓名", "表计位置", "上期抄表日期", "上期抄表读数", "本期抄表日期", "本期抄表读数",  "本期水量", "表计水价", "客户水价", "抄表方式", "抄表结果", "抄表员", "备注", "代扣方式", "账户名称", "银行卡号"};
		//excel列名
		String[] columnNames = { "room", "customerName", "meterPlace", "preDateStr", "preRead", "currDateStr", "currRead", "currAmount", "meterPriceCode", "customerPriceCode", "readModeStr", "readResultStr", "operatorName", "remark", "deductType", "accountName", "bankNo"};
		//sheet名
		String sheetName = period+"抄表记录";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, readResult, traceIds, readMode);
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
	private List<Map<String, Object>> getExportExcelData( String searchCond, Integer readResult, String traceIds, Integer readMode){
		List<Map<String, Object>> recordTempMapList = meterRecordTempService.getExportMeterRecordTempData(searchCond, readResult, traceIds, readMode);
		for(Map<String, Object> recordMap : recordTempMapList) {
			String currTraceIds = recordMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			String customerId = recordMap.get("CUSTOMER_ID").toString();//客户ID
			String meterPlace = recordMap.get("PLACE").toString();//表计位置
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			recordMap.put("room", place);
			recordMap.put("customerName", customer.getCustomerName());
			if(recordMap.get("PRE_DATE") != null) {
				recordMap.put("preDateStr", recordMap.get("PRE_DATE").toString());
			}
			recordMap.put("meterPlace", meterPlace);//表计位置
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
			
			
			if(recordMap.get("METER_PRICE_CODE") != null) {
				SysWaterPrice price = WaterPriceService.getWaterPriceByPriceCode(recordMap.get("METER_PRICE_CODE").toString());
				recordMap.put("meterPriceCode", price.getLadderName());
			}
			if(recordMap.get("CUSTOMER_PRICE_CODE") != null) {
				SysWaterPrice price = WaterPriceService.getWaterPriceByPriceCode(recordMap.get("CUSTOMER_PRICE_CODE").toString());
				recordMap.put("customerPriceCode", price.getLadderName());
			}
			
			
			
			if(recordMap.get("READ_MODE") != null) {
				recordMap.put("readModeStr",EnumReadMode.getName(recordMap.get("READ_MODE").toString()));
			}
			if(recordMap.get("READ_RESULT") != null) {
				recordMap.put("readResultStr",EnumAppReadResult.getName(Integer.valueOf(recordMap.get("READ_RESULT").toString())));
			}
			if(recordMap.get("OPERATOR_NAME") != null) {
				recordMap.put("operatorName",recordMap.get("OPERATOR_NAME").toString());
			}
			
			if(recordMap.get("REMARK") != null) {
				recordMap.put("remark",recordMap.get("REMARK").toString());
			}
			//客户银行卡信息
			CustomerBanks bank = bankService.getCustomerBank(customer.getId());
			String deductType = "";
			if(customer.getDeductType() != null) {
				deductType = EnumDeductType.getName(customer.getDeductType());
			}
			recordMap.put("deductTypeValue", deductType);
			String accountName = "";
			String bankNo = "";
			if(bank != null) {
				accountName = bank.getAccountName();
				bankNo = bank.getCardNo();
			}
			recordMap.put("accountName", accountName);
			recordMap.put("bankNo", bankNo);
		}
		return recordTempMapList;
	}
	
	
	//------------------------------	一户多表导出抄表记录Excel	------------------------------
		/**
		 * @Title: exportMeterRecordExcel
		 * @Description: 导出抄表记录Excel
		 * @param request
		 * @param response 
		 */
		@RequestMapping("/export-more-meter-record-excel")
		public void exportMoreMeterRecordExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer readResult, String traceIds, Integer readMode) {
			
			String period = DateUtils.getYearMonth();//期间默认本月
			//获取导出EXCEL的数据
			List<Map<String, Object>> excelDataList = this.getExportMoreExcelData(searchCond, readResult, traceIds, readMode);
			Map<String, Object> recordMap = excelDataList.get(0);
			String desc1 =recordMap.get("desc1").toString();
			String desc2 =recordMap.get("desc2").toString();
			String desc3 = "";
			if(recordMap.get("desc3").toString() != null) {
				desc3 =recordMap.get("desc3").toString();
			}
			String desc4 = "";
			if(recordMap.get("desc4").toString() != null) {
				desc4 =recordMap.get("desc4").toString();
			}
			
			
			//excel标题
			String[] titles = { "房间号", "客户姓名",desc1, desc2,desc3,desc4,desc1, desc2,desc3,desc4,"合计水量","代扣方式", "账户名称", "银行卡号"};
			//excel列名
			String[] columnNames = { "room", "customerName", "preRead1","preRead2", "preRead3", "preRead4","currRead1","currRead2", "currRead3","currRead4","totalAmount","deductType", "accountName", "bankNo"};
			//sheet名
			String sheetName = period+"抄表记录";
			
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
		private List<Map<String, Object>> getExportMoreExcelData( String searchCond, Integer readResult, String traceIds, Integer readMode){
			//获取一表多户的抄表记录
			List<Map<String, Object>> recordTempMapList = meterRecordTempService.getExportMoreMeterRecordTempData(searchCond, readResult, traceIds, readMode);
			for(Map<String, Object> recordMap : recordTempMapList) {
				String place = locationService.getPlace(recordMap.get("TRACE_IDS").toString());//获取地理位置信息（小区 楼号-单元-门牌号）
				Long customerId = Long.valueOf(recordMap.get("CUSTOMER_ID").toString());
				String perReadStr = recordMap.get("COLUMN2").toString();//上期读数
				String currReadStr = recordMap.get("COLUMN3").toString();//本期读数
				String descStr = recordMap.get("COLUMN4").toString();//表计描述
				String currAmountStr = recordMap.get("COLUMN5").toString();//本期水量
				String[] preReadArr = perReadStr.split(",");
				String[] currReadArr = currReadStr.split(",");
				String[] descArr = descStr.split(",");
				String[] currAmountArr = currAmountStr.split(",");
				BigDecimal totalAmount = new BigDecimal("0"); 
				for(String amount : currAmountArr) {//获取水量
					totalAmount = BigDecimalUtils.add(totalAmount, new BigDecimal(amount));
				}
				
				recordMap.put("room", place);
				Customers customer = customersService.selectByPrimaryKey(customerId);
				recordMap.put("customerName", customer.getCustomerName());
				if(preReadArr.length > 0) {
					recordMap.put("desc1", descArr[0]);
					recordMap.put("preRead1", preReadArr[0]);
					recordMap.put("currRead1", currReadArr[0]);
				}
				if(preReadArr.length > 1) {
					recordMap.put("desc2", descArr[1]);
					recordMap.put("preRead2", preReadArr[1]);
					recordMap.put("currRead2", currReadArr[1]);
				}
				String desc3 = "";
				if(preReadArr.length > 2) {
					desc3 = descArr[2];
					recordMap.put("preRead3", preReadArr[2]);
					recordMap.put("currRead3", currReadArr[2]);
				}
				recordMap.put("desc3", descArr[2]);
				String desc4 = "";
				if(preReadArr.length > 3) {
					desc4 = descArr[3];
					recordMap.put("preRead4", preReadArr[3]);
					recordMap.put("currRead4", currReadArr[3]);
				}
				recordMap.put("desc4", desc4 );
				recordMap.put("totalAmount", totalAmount);
				//客户银行卡信息
				CustomerBanks bank = bankService.getCustomerBank(customer.getId());
				String deductType = "";
				if(customer.getDeductType() != null) {
					deductType = EnumDeductType.getName(customer.getDeductType());
				}
				recordMap.put("deductTypeValue", deductType);
				String accountName = "";
				String bankNo = "";
				if(bank != null) {
					accountName = bank.getAccountName();
					bankNo = bank.getCardNo();
				}
				recordMap.put("accountName", accountName);
				recordMap.put("bankNo", bankNo);
				
			}
			return recordTempMapList;
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
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(new Date());
		String place = locationService.getPlace(traceIds);
		// excel文件名
		String fileName = period;
		if(StringUtils.isNotBlank(traceIds)) {
			fileName = fileName+"-"+place;
		}
		fileName = fileName+"-"+times+".xls";
		
		return fileName;
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