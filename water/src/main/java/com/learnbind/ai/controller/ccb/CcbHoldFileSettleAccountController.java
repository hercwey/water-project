package com.learnbind.ai.controller.ccb;

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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCcbSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CcbBatchCustomerAccount;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.ccb.CcbBatchCustomerAccountService;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.ccb
 *
 * @Title: CcbHoldFileSettleAccountController.java
 * @Description: 代扣文件销账
 *
 * @author Thinkpad
 * @date 2019年8月21日 上午11:31:55
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/holdfile-settle-account")
public class CcbHoldFileSettleAccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbHoldFileSettleAccountController.class);
	
	private static final String TEMPLATE_PATH = "ccb/settle_account/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//中国建设银行批量代扣记录服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置客户
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private CcbBatchCustomerAccountService ccbBatchCustomerAccountService;//代扣文件账单关系表
	@Autowired
	private DataDictService dataDictService;  //数据字典
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "settle_account_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "settle_account_main";
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period, Integer holdStatus) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize =PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CcbBatchWithholdRecord> accountItemList = ccbBatchWithholdRecordService.searchCcbHoldFileList(searchCond, traceIds, period, holdStatus);
		PageInfo<CcbBatchWithholdRecord> pageInfo = new PageInfo<>(accountItemList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		for(CcbBatchWithholdRecord ccbBatchWithholdRecord : accountItemList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(ccbBatchWithholdRecord);
			String place = locationService.getPlace(ccbBatchWithholdRecord.getTraceId());
			String withholdFile = ccbBatchWithholdRecord.getWithholdFile();
			withholdFile = withholdFile.substring(withholdFile.lastIndexOf(File.separator)+1);
			
			String ccbVchrFile = "";
			if(ccbBatchWithholdRecord.getCcbVchrFile() != null) {
				ccbVchrFile = ccbBatchWithholdRecord.getCcbVchrFile();;//返回文件名称截取
				ccbVchrFile = ccbVchrFile.substring(ccbVchrFile.lastIndexOf(File.separator)+1);
			}
			
			customerMap.put("place", place);
			customerMap.put("subWithholdFile", withholdFile);
			customerMap.put("subCcbVchrFile", ccbVchrFile);
			accountItemMapList.add(customerMap);
		}
	

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "settle_account_table";
	}
	
	//************************************销账结果选项卡***********************
	/**
	 * @Title: settleAccountResultMain
	 * @Description: 销账
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/settle-account-result-main")
	public String settleAccountResultMain(Model model) {
		
		return TEMPLATE_PATH + "settle_account_result_main";
	}
	
	/**
	 * @Title: loadGlobalLocationPage
	 * @Description: 加载通用地理位置页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-global-location-settle-page")
	public String loadGlobalLocationPage(Model model) {

		return TEMPLATE_PATH + "global_location_settle_page";
	}
	
	/**
	 * @Title: settleAccountResultTable
	 * @Description:销账结果
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	@RequestMapping(value = "/settle-account-result-table")
	public String settleAccountResultTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer settleAccountStatus, Long withRecordId, String period) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize =PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//查询账单代扣结果
		
		List<CcbBatchCustomerAccount> accountItemList = new ArrayList<>();
		if(withRecordId != 0) {//点击单条代扣文件的销账结果
			accountItemList = ccbBatchCustomerAccountService.selectAccountByRecordId(withRecordId, period,settleAccountStatus, searchCond);
		} else {//点击选项卡的销账结果
			accountItemList = ccbBatchCustomerAccountService.searchAllAccount(searchCond, settleAccountStatus, period);
		}
		
		PageInfo<CcbBatchCustomerAccount> pageInfo = new PageInfo<>(accountItemList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		
		BigDecimal totalAmount = new BigDecimal("0");//总金额
		BigDecimal successAmount = new BigDecimal("0");//CCB处理成功总金额
		int successNum = 0;//CCB处理成功总笔数
		BigDecimal failAmount = new BigDecimal("0");//CCB处理失败总金额
		int failNum = 0;//CCB处理失败总笔数
		List<Map<String, Object>> statisticMap = ccbBatchCustomerAccountService.getAccountStatistic(withRecordId, period,settleAccountStatus, searchCond);
		for(Map<String, Object> map : statisticMap) {
			Integer settleStatus = Integer.parseInt(map.get("SETTLE_ACCOUNT_STATUS").toString());
			if(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_SUCESS.getValue() == settleStatus) {
				successNum = Integer.parseInt(map.get("NUM").toString());
				successAmount = new BigDecimal(map.get("ACCOUNT_AMOUNT").toString());
			} else {
				failNum = failNum + Integer.parseInt(map.get("NUM").toString());
				failAmount = BigDecimalUtils.add(failAmount, new BigDecimal(map.get("ACCOUNT_AMOUNT").toString()));
			}
		}
		totalAmount = BigDecimalUtils.add(successAmount, failAmount);
		for(CcbBatchCustomerAccount ccbBatchCustomerAccount : accountItemList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(ccbBatchCustomerAccount);
			
//			CcbBatchWithholdRecord batchRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(ccbBatchCustomerAccount.getBathWithholdingId());
//			customerMap.put("period", batchRecord.getPeriod());//加入代扣文件时间
			customerMap.put("settleTimeStr", ccbBatchCustomerAccount.getSettleTimeStr());//销账事件
			accountItemMapList.add(customerMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		//CCB处理统计数据
		model.addAttribute("totalAmount", totalAmount);//代扣处理总金额
		model.addAttribute("successAmount", successAmount);//CCB处理成功总金额
		model.addAttribute("successNum", successNum);//CCB处理成功总笔数
		model.addAttribute("failAmount", failAmount);//CCB处理失败总金额
		model.addAttribute("failNum", failNum);//CCB处理失败总笔数
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "settle_account_result_table";
	}
	
	
	/**
	 * @Title: settleHoldFile
	 * @Description: 自动销账
	 * @param holdFileIdStr
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/settle-handel-holding-file", produces = "application/json")
	@ResponseBody
	public Object settleHoldFile(String holdFileIdStr, String searchCond, String traceIds, String searchPeriod, Integer holdStatus) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int rows = 1;
		if(StringUtils.isNotBlank(holdFileIdStr)) {//勾选代扣文件进行销账
			String[] holdFileIdArr = holdFileIdStr.split(",");
			for(String holdFileId : holdFileIdArr) {
				CcbBatchWithholdRecord record = ccbBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
				if(record.getStatus() == EnumCcbBatchStatus.HANDLE_RETURN.getValue()) {//如果代扣文件状态不是已预处理回盘，则不进行销账操作
					rows = ccbBatchCustomerAccountService.settleAccount(Long.valueOf(holdFileId), userBean.getId());
					if(rows>0) {//修改代扣文件状态
						this.updateWithHoldRecordStatus(Long.valueOf(holdFileId));
					}
				}
				
			}
			
		} else {//未勾选代扣文件，将查询出来的所有代扣文件进行销账
			List<CcbBatchWithholdRecord> accountItemList = ccbBatchWithholdRecordService.searchCcbHoldFileList(searchCond, traceIds, searchPeriod, holdStatus);
			for(CcbBatchWithholdRecord withHoldRecord : accountItemList) {
				
				if(withHoldRecord.getStatus() == EnumCcbBatchStatus.HANDLE_RETURN.getValue()) {//如果代扣文件状态不是已预处理回盘，则不进行销账操作
					rows = ccbBatchCustomerAccountService.settleAccount(withHoldRecord.getId(), userBean.getId());
					if(rows>0) {//修改代扣文件状态
						this.updateWithHoldRecordStatus(withHoldRecord.getId());
					}
				}
				
			}
			
		}
		if(rows<0) {
			return RequestResultUtil.getResultAddWarn("代扣文件销账失败");
		}
		
		
		return RequestResultUtil.getResultAddSuccess("代扣文件销账成功");
	}
	
	/**
	 * @Title: updateWithHoldRecordStatus
	 * @Description: 销账操作后修改代扣文件状态为已销账
	 * @param withHoldRecordId
	 * @return 
	 */
	private int updateWithHoldRecordStatus(Long withHoldRecordId) {
		CcbBatchWithholdRecord ccbBatchWithholdRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(withHoldRecordId);
		ccbBatchWithholdRecord.setStatus(EnumCcbBatchStatus.SALE_ACCOUNT.getValue());
		ccbBatchWithholdRecordService.updateByPrimaryKeySelective(ccbBatchWithholdRecord);
		
		return 0;
	}
	
	/**
	 * @Title: selectSettleResult
	 * @Description: 单独查看销账结果
	 * @param withRecordId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/get-settle-account-result", produces = "application/json")
	@ResponseBody
	public Object selectSettleResult(Long withRecordId) throws Exception {
		
		
		
		return RequestResultUtil.getResultAddSuccess("代扣文件销账成功");
	}
	
	/**
	 * @Title: advanceHandelFile
	 * @Description: 预处理批量代扣文件
	 * @param holdFileIdStr
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/advance-handel-holding-file", produces = "application/json")
	@ResponseBody
	public Object advanceHandelFile(String holdFileIdStr, String searchCond, String traceIds, String searchPeriod, Integer holdStatus) throws Exception {
		int rows = 1;
		if(StringUtils.isNotBlank(holdFileIdStr)) {//勾选代扣文件进行销账
			String[] holdFileIdArr = holdFileIdStr.split(",");
			for(String holdFileId : holdFileIdArr) {
				CcbBatchWithholdRecord record = ccbBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
				if(record.getStatus() == EnumCcbBatchStatus.DOWNLOAD_RETURN.getValue()) {//如果代扣文件状态不是已下载回盘，则不预处理文件
					rows = ccbBatchCustomerAccountService.advanceWithHold(Long.valueOf(holdFileId));
					if(rows > 0) {//修改代扣文件状态为已处理回盘文件
						CcbBatchWithholdRecord ccbBatchWithholdRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
						ccbBatchWithholdRecord.setStatus(EnumCcbBatchStatus.HANDLE_RETURN.getValue());
						ccbBatchWithholdRecordService.updateByPrimaryKeySelective(ccbBatchWithholdRecord);
					}
				}
			}
			
		} else {//未勾选代扣文件，将查询出来的所有代扣文件进行销账
			List<CcbBatchWithholdRecord> accountItemList = ccbBatchWithholdRecordService.searchCcbHoldFileList(searchCond, traceIds, searchPeriod, holdStatus);
			for(CcbBatchWithholdRecord withHoldRecord : accountItemList) {
				if(withHoldRecord.getStatus() == EnumCcbBatchStatus.DOWNLOAD_RETURN.getValue()) {//如果代扣文件状态不是已下载回盘，则不预处理文件
					rows = ccbBatchCustomerAccountService.advanceWithHold(withHoldRecord.getId());
					if(rows > 0) {//修改代扣文件状态为已处理回盘文件
						withHoldRecord.setStatus(EnumCcbBatchStatus.HANDLE_RETURN.getValue());
						ccbBatchWithholdRecordService.updateByPrimaryKeySelective(withHoldRecord);
					}
				}
				
			}
			
		}
		
		return RequestResultUtil.getResultAddSuccess("代扣文件预处理成功");
	}
	
	
	//************************************代扣文件详情选项卡***********************
	/**
	 * @Title: holdFileDetailMain
	 * @Description: 代扣文件详情
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/with-hold-file-detail-main")
	public String holdFileDetailMain(Model model) {
		//代扣失败原因数据字典
		List<DataDict> failReasonList = dataDictService.getListByTypeCode(DataDictType.WITHHOLD_FAIL_REASON);
		model.addAttribute("failReasonList", failReasonList);
		return TEMPLATE_PATH + "hold_file_detail_main";
	}
	
	
	/**
	 * @Title: settleAccountResultTable
	 * @Description:代扣结果
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	@RequestMapping(value = "/hold-file-detail-table")
	public String holdFileDeatilTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer withholdingStatus, Long withRecordId, String period, String withholdFailReason) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		
		//查询批量代扣文件记录
		CcbBatchWithholdRecord withholdRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(withRecordId);

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//查询账单代扣结果
		List<Map<String, Object>> withholdDetailList = ccbBatchCustomerAccountService.selectHoldFileDetail(withRecordId, searchCond, withholdingStatus, withholdFailReason);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(withholdDetailList);// (使用了拦截器或是AOP进行查询的再次处理)
		
//		for(Map<String, Object> withholdDetail : withholdDetailList) {
//			//Map<String, Object> customerMap = EntityUtils.entityToMap(ccbBatchCustomerAccount);
//			//CcbBatchWithholdRecord batchRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(ccbBatchCustomerAccount.getBathWithholdingId());
////			customerMap.put("period", batchRecord.getPeriod());
////			customerMap.put("joinTimeStr", ccbBatchCustomerAccount.getJoinTimeStr());////加入代扣文件时间
////			customerMap.put("settleTimeStr", ccbBatchCustomerAccount.getSettleTimeStr());//销账时间
////			accountItemMapList.add(customerMap);
//			
//			String billIdStr = (String)withholdDetail.get("ONE_ACCOUNT_ITEM_ID");//账单ID
//			Date joinTime = (Date)withholdDetail.get("JOIN_TIME");//加入代扣文件时间
//			BigDecimal currWithholdingStatusBd = (BigDecimal)withholdDetail.get("WITHHOLDING_STATUS");//代扣状态
//			String customerName = (String)withholdDetail.get("CUSTOMER_NAME");//户名
//			String customerCardNo = (String)withholdDetail.get("CUSTOMER_CARD_NO");//卡号
//			BigDecimal accountAmount = (BigDecimal)withholdDetail.get("ACCOUNT_AMOUNT");//金额
//			String currWithholdFailReason = (String)withholdDetail.get("WITHHOLD_FAIL_REASON");//代扣失败原因
//			int currWithholdingStatus = 0;
//			if(currWithholdingStatusBd!=null) {
//				currWithholdingStatus = currWithholdingStatusBd.intValue();
//			}
//			
//		}
		
		// 传递如下数据至前台页面
		model.addAttribute("withholdDetailList", withholdDetailList); // 列表数据
		model.addAttribute("withholdRecord", withholdRecord);
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "hold_file_detail_table";
	}
	
	//------------------------------	导出代扣结果Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-hold-file-detail-excel")
	public void exportCustomersExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Long withRecordId, Integer withholdingStatus, String withholdFailReason) {
		
		//String period = DateUtils.getYearMonth();//期间默认本月
		//excel标题
		String[] titles = {"地理位置","期间","客户名称", "电话", "客户卡号", "账单金额", "代扣状态", "加入代扣文件时间", "失败原因"};
		//excel列名
		String[] columnNames = {"place", "period","customerName", "customerMobile", "customerCardNo", "accountAmount", "withholdingStatus", "joinTime", "withholdFailReason"};
		//sheet名
		String sheetName = "客户代扣结果";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportHoldFileExcelData(searchCond, withRecordId, withholdingStatus, withholdFailReason);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//查询批量代扣文件记录
		CcbBatchWithholdRecord withholdRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(withRecordId);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(withholdRecord.getPeriod(), withholdRecord.getTraceId());
		
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
	private List<Map<String, Object>> getExportHoldFileExcelData(String searchCond, Long withRecordId, Integer withholdingStatus, String withholdFailReason){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		List<Map<String, Object>> withholdDetailList = ccbBatchCustomerAccountService.selectHoldFileDetail(withRecordId, searchCond, withholdingStatus, withholdFailReason);
		List<Map<String, Object>> accountMapList = new ArrayList<>();//返回map
		//查询批量代扣文件记录
		CcbBatchWithholdRecord withholdRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(withRecordId);
		for(Map<String, Object> withholdDetail : withholdDetailList) {
			
			BigDecimal billIdBd = (BigDecimal)withholdDetail.get("ONE_ACCOUNT_ITEM_ID");//账单ID
			Date joinTime = (Date)withholdDetail.get("JOIN_TIME");//加入代扣文件时间
			BigDecimal currWithholdingStatusBd = (BigDecimal)withholdDetail.get("WITHHOLDING_STATUS");//代扣状态
			String customerName = (String)withholdDetail.get("CUSTOMER_NAME");//户名
			String customerCardNo = (String)withholdDetail.get("CUSTOMER_CARD_NO");//卡号
			BigDecimal accountAmount = (BigDecimal)withholdDetail.get("ACCOUNT_AMOUNT");//金额
			String currWithholdFailReason = (String)withholdDetail.get("WITHHOLD_FAIL_REASON");//代扣失败原因
			
			int currWithholdingStatus = 0;//代扣状态
			if(currWithholdingStatusBd!=null) {
				currWithholdingStatus = currWithholdingStatusBd.intValue();
			}
			long billId = 0;
			if(billIdBd!=null) {
				billId = billIdBd.longValue();
			}
			
			String customerMobile = "";//客户电话
			CustomerAccountItem bill = customerAccountItemService.selectByPrimaryKey(billId);
			if(billIdBd!=null && bill!=null) {
				Customers customer = customersService.selectByPrimaryKey(bill.getCustomerId());
				String mobile = customer.getCustomerMobile();
				String tel = customer.getCustomerTel();
				if(StringUtils.isNotBlank(mobile)) {
					customerMobile = mobile;//客户电话
				}
				if(StringUtils.isNotBlank(tel)) {
					if(StringUtils.isNotBlank(customerMobile)) {
						customerMobile = customerMobile+" "+tel;//客户电话
					}else {
						customerMobile = tel;//客户电话
					}
				}
			}
			
			String place = locationCustomerService.getPlace(bill.getCustomerId());
			String joinTimeStr = sdf.format(joinTime);
			
			Map<String, Object> accountMap = EntityUtils.entityToMap(withholdDetail);
			//地理位置
			accountMap.put("place", place);
			accountMap.put("period", withholdRecord.getPeriod());
			accountMap.put("customerName", customerName);
			accountMap.put("customerMobile", customerMobile);
			accountMap.put("customerCardNo", customerCardNo);
			accountMap.put("accountAmount", accountAmount);
			accountMap.put("withholdingStatus", EnumCcbWhHoldStatus.getName(currWithholdingStatus));
			accountMap.put("joinTime", joinTimeStr);
			accountMap.put("withholdFailReason", currWithholdFailReason);
			accountMapList.add(accountMap);
		}
		
		return accountMapList;
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
			fileName = period+"-"+place;
		}
		
		fileName = fileName+"-"+times+"-"+"代扣结果"+".xls";
		
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
