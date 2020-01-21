package com.learnbind.ai.controller.cmbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCmbcBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcWhHoldStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CmbcBatchCustomerAccount;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.cmbc.CmbcBatchCustomerAccountService;
import com.learnbind.ai.service.cmbc.CmbcBatchWithholdRecordService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.cmbc
 *
 * @Title: CmbcHoldFileSettleAccountController.java
 * @Description: 民生银行代扣销账
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午4:24:16
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/holdfile-settle-account-cmbc")
public class CmbcHoldFileSettleAccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(CmbcHoldFileSettleAccountController.class);
	
	private static final String TEMPLATE_PATH = "cmbc/settle_account/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CmbcBatchWithholdRecordService cmbcBatchWithholdRecordService;//中国建设银行批量代扣记录服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置客户
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private CmbcBatchCustomerAccountService cmbcBatchCustomerAccountService;//代扣文件账单关系表
	
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
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CmbcBatchWithholdRecord> accountItemList = cmbcBatchWithholdRecordService.searchCmbcHoldFileList(searchCond, traceIds, period, holdStatus);
		PageInfo<CmbcBatchWithholdRecord> pageInfo = new PageInfo<>(accountItemList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		for(CmbcBatchWithholdRecord cmbcBatchWithholdRecord : accountItemList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(cmbcBatchWithholdRecord);
			
			String place = locationService.getPlace(cmbcBatchWithholdRecord.getTraceId());
			String withholdFile = cmbcBatchWithholdRecord.getWithholdFile();
			withholdFile = withholdFile.substring(withholdFile.lastIndexOf(File.separator)+1);
			
			Integer cmbcVchrFileSize = null;
			if(cmbcBatchWithholdRecord.getCmbcVchrFile() != null) {
				String cmbcVchrFile = cmbcBatchWithholdRecord.getCmbcVchrFile();;//返回文件名称截取
				List<String> filePathList = JSON.parseArray(cmbcVchrFile, String.class);
				cmbcVchrFileSize = filePathList.size();
			}
			
			customerMap.put("place", place);
			customerMap.put("subWithholdFile", withholdFile);
			customerMap.put("subCmbcVchrFileSize", cmbcVchrFileSize);
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
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//查询账单代扣结果
		
		List<CmbcBatchCustomerAccount> withholdDetailList = new ArrayList<>();
		if(withRecordId!=0) {//点击单条代扣文件的销账结果
			withholdDetailList = cmbcBatchCustomerAccountService.selectAccountByRecordId(withRecordId, period, searchCond, settleAccountStatus);
		} else {//点击选项卡的销账结果
			withholdDetailList = cmbcBatchCustomerAccountService.searchAllAccount(searchCond, settleAccountStatus, period);
		}
		
		PageInfo<CmbcBatchCustomerAccount> pageInfo = new PageInfo<>(withholdDetailList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		
		BigDecimal totalAmount = new BigDecimal("0");//总金额
		BigDecimal successAmount = new BigDecimal("0");//CMBC处理成功总金额
		int successNum = 0;//CMBC处理成功总笔数
		BigDecimal failAmount = new BigDecimal("0");//CMBC处理失败总金额
		int failNum = 0;//CMBC处理失败总笔数
		
		List<CmbcBatchCustomerAccount> statWithholdDetailList = cmbcBatchCustomerAccountService.selectAccountByRecordId(withRecordId, period, null, null);
		for(CmbcBatchCustomerAccount cmbcBatchCustomerAccount : statWithholdDetailList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(cmbcBatchCustomerAccount);
			
			
			customerMap.put("joinTimeStr", cmbcBatchCustomerAccount.getJoinTimeStr());//加入代扣文件时间
			customerMap.put("settleTimeStr", cmbcBatchCustomerAccount.getSettleTimeStr());//销账事件
			accountItemMapList.add(customerMap);
			if(cmbcBatchCustomerAccount.getSettleAccountStatus() == EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue()) {//销账成功
				successNum = successNum+1;//处理成功笔数+1
				successAmount = BigDecimalUtils.add(successAmount, cmbcBatchCustomerAccount.getAccountAmount());// 处理成功金额累计
			} else if(cmbcBatchCustomerAccount.getSettleAccountStatus() == EnumSettlementStatus.SETTLEMENT_FAIL.getValue()) {//销账失败
				failNum = failNum + 1;//处理失败笔数+1
				failAmount = BigDecimalUtils.add(failAmount, cmbcBatchCustomerAccount.getAccountAmount());// 处理失败金额累计
			}
		}
		
		totalAmount = BigDecimalUtils.add(successAmount, failAmount);// 代扣处理总金额

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		//CMBC处理统计数据
		model.addAttribute("totalAmount", totalAmount);//代扣处理总金额
		model.addAttribute("successAmount", successAmount);//CMBC处理成功总金额
		model.addAttribute("successNum", successNum);//CMBC处理成功总笔数
		model.addAttribute("failAmount", failAmount);//CMBC处理失败总金额
		model.addAttribute("failNum", failNum);//CMBC处理失败总笔数
		
		
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
				CmbcBatchWithholdRecord record = cmbcBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
				if(record.getStatus() == EnumCmbcBatchStatus.HANDLE_RETURN.getValue()) {//如果代扣文件状态不是已预处理，则不进行销账操作
					rows = cmbcBatchCustomerAccountService.settleAccount(Long.valueOf(holdFileId), userBean.getId());
					if(rows>0) {//修改代扣文件状态
						this.updateWithHoldRecordStatus(Long.valueOf(holdFileId));
					}
				}
				
			}
			
		} else {//未勾选代扣文件，将查询出来的所有代扣文件进行销账
			List<CmbcBatchWithholdRecord> accountItemList = cmbcBatchWithholdRecordService.searchCmbcHoldFileList(searchCond, traceIds, searchPeriod, holdStatus);
			for(CmbcBatchWithholdRecord withHoldRecord : accountItemList) {
				if(withHoldRecord.getStatus() == EnumCmbcBatchStatus.HANDLE_RETURN.getValue()) {//如果代扣文件状态不是已预处理，则不进行销账操作
					rows = cmbcBatchCustomerAccountService.settleAccount(withHoldRecord.getId(), userBean.getId());
					if(rows>0) {//修改代扣文件状态
						this.updateWithHoldRecordStatus(withHoldRecord.getId());
					}
				}
				
			}
			
		}
		if(rows<0) {
			return RequestResultUtil.getResultAddWarn("已预处理状态的代扣文件销账失败！");
		}
		
		
		return RequestResultUtil.getResultAddSuccess("已预处理状态的代扣文件销账成功！");
	}
	
	/**
	 * @Title: updateWithHoldRecordStatus
	 * @Description: 销账操作后修改代扣文件状态为已销账
	 * @param withHoldRecordId
	 * @return 
	 */
	private int updateWithHoldRecordStatus(Long withHoldRecordId) {
		CmbcBatchWithholdRecord cmbcBatchWithholdRecord = cmbcBatchWithholdRecordService.selectByPrimaryKey(withHoldRecordId);
		cmbcBatchWithholdRecord.setStatus(EnumCmbcBatchStatus.SALE_ACCOUNT.getValue());
		cmbcBatchWithholdRecordService.updateByPrimaryKeySelective(cmbcBatchWithholdRecord);
		
		return 0;
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
				CmbcBatchWithholdRecord record = cmbcBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
				if(record.getStatus() == EnumCmbcBatchStatus.IMPORT_RETURN_FILE.getValue()) {//如果代扣文件状态不是已导入回盘，则不进行预处理
					rows = cmbcBatchCustomerAccountService.advanceWithHold(Long.valueOf(holdFileId));
					if(rows > 0) {//修改代扣文件状态为已处理回盘文件
						CmbcBatchWithholdRecord cmbcBatchWithholdRecord = cmbcBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
						cmbcBatchWithholdRecord.setStatus(EnumCmbcBatchStatus.HANDLE_RETURN.getValue());
						cmbcBatchWithholdRecordService.updateByPrimaryKeySelective(cmbcBatchWithholdRecord);
					}else if(rows==-1) {
						logger.debug("==========读取批量代扣文件【id="+holdFileId+"】为空");
					}
				}
				
			}
			
		} else {//未勾选代扣文件，将查询出来的所有代扣文件进行销账
			List<CmbcBatchWithholdRecord> accountItemList = cmbcBatchWithholdRecordService.searchCmbcHoldFileList(searchCond, traceIds, searchPeriod, holdStatus);
			for(CmbcBatchWithholdRecord withHoldRecord : accountItemList) {
				if(withHoldRecord.getStatus() == EnumCmbcBatchStatus.IMPORT_RETURN_FILE.getValue()) {//如果代扣文件状态不是已导入回盘，则不进行预处理
					rows = cmbcBatchCustomerAccountService.advanceWithHold(withHoldRecord.getId());
					if(rows > 0) {//修改代扣文件状态为已处理回盘文件
						withHoldRecord.setStatus(EnumCmbcBatchStatus.HANDLE_RETURN.getValue());
						cmbcBatchWithholdRecordService.updateByPrimaryKeySelective(withHoldRecord);
					}else if(rows==-1) {
						logger.debug("==========读取批量代扣文件【id="+withHoldRecord.getId()+"，sn="+withHoldRecord.getFileSn()+"】为空");
					}
				}
				
			}
			
		}
		
		
		return RequestResultUtil.getResultAddSuccess("已导入回盘状态的代扣文件预处理成功！");
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
		
		return TEMPLATE_PATH + "hold_file_detail_main";
	}
	
	
	/**
	 * @Title: holdFileDeatilTable
	 * @Description: 代扣结果
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param withholdingStatus
	 * @param withholdFailReason
	 * @param withRecordId
	 * @return 
	 */
	@RequestMapping(value = "/hold-file-detail-table")
	public String holdFileDeatilTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer withholdingStatus, String withholdFailReason, Long withRecordId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//查询账单代扣结果
		List<Map<String, Object>> withholdDetailList = cmbcBatchCustomerAccountService.selectHoldFileDetail(withRecordId, searchCond, withholdingStatus, withholdFailReason);
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(withholdDetailList);// (使用了拦截器或是AOP进行查询的再次处理)
		//List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		
		BigDecimal totalAmount = new BigDecimal("0");//代扣总金额
		BigDecimal successAmount = new BigDecimal("0");//代扣成功总金额
		int successNum = 0;//代扣成功总笔数
		BigDecimal failAmount = new BigDecimal("0");//代扣失败总金额
		int failNum = 0;//代扣失败总笔数
		
		List<Map<String, Object>> statWithholdDetailList = cmbcBatchCustomerAccountService.selectHoldFileDetail(withRecordId, null, null, null);
		for(Map<String, Object> withholdDetail : statWithholdDetailList) {
			
//			String billIdStr = (String)withholdDetail.get("ONE_ACCOUNT_ITEM_ID");//账单ID
//			Date joinTime = (Date)withholdDetail.get("JOIN_TIME");//加入代扣文件时间
			BigDecimal currWithholdingStatusBd = (BigDecimal)withholdDetail.get("WITHHOLDING_STATUS");//代扣状态
//			String customerName = (String)withholdDetail.get("CUSTOMER_NAME");//户名
//			String customerCardNo = (String)withholdDetail.get("CUSTOMER_CARD_NO");//卡号
			BigDecimal accountAmount = (BigDecimal)withholdDetail.get("ACCOUNT_AMOUNT");//金额
//			String currWithholdFailReason = (String)withholdDetail.get("WITHHOLD_FAIL_REASON");//代扣失败原因
			int currWithholdingStatus = 0;
			if(currWithholdingStatusBd!=null) {
				currWithholdingStatus = currWithholdingStatusBd.intValue();
			}
			
			totalAmount = BigDecimalUtils.add(totalAmount, accountAmount);
			if(currWithholdingStatus==EnumCmbcWhHoldStatus.WITHHOLDING_SUCESS.getValue()) {
				successAmount = BigDecimalUtils.add(successAmount, accountAmount);
				successNum = successNum+1;
			}else if(currWithholdingStatus==EnumCmbcWhHoldStatus.WITHHOLDING_FAIL.getValue()) {
				failAmount = BigDecimalUtils.add(failAmount, accountAmount);
				failNum = failNum+1;
			}
			
		}

		// 传递如下数据至前台页面
		model.addAttribute("withholdDetailList", withholdDetailList); // 列表数据
		//代扣结果统计数据
		model.addAttribute("totalAmount", totalAmount);//代扣总金额
		model.addAttribute("successAmount", successAmount);//代扣成功总金额
		model.addAttribute("successNum", successNum);//代扣成功总笔数
		model.addAttribute("failAmount", failAmount);//代扣失败总金额
		model.addAttribute("failNum", failNum);//代扣失败总笔数
		
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
	public void exportCustomersExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer withholdingStatus, String withholdFailReason, Long withRecordId) {
		
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
		CmbcBatchWithholdRecord withholdRecord = cmbcBatchWithholdRecordService.selectByPrimaryKey(withRecordId);
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
		
		//查询账单代扣结果
		List<Map<String, Object>> withholdDetailList = cmbcBatchCustomerAccountService.selectHoldFileDetail(withRecordId, searchCond, withholdingStatus, withholdFailReason);
		List<Map<String, Object>> accountMapList = new ArrayList<>();
		//查询批量代扣文件记录
		CmbcBatchWithholdRecord withholdRecord = cmbcBatchWithholdRecordService.selectByPrimaryKey(withRecordId);
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
			
			String place = locationCustomerService.getPlace(bill.getCustomerId());//地理位置
        	String joinTimeStr = sdf.format(joinTime);
			
			Map<String, Object> accountMap = EntityUtils.entityToMap(withholdDetail);
			accountMap.put("place", place);
			accountMap.put("period", withholdRecord.getPeriod());
			accountMap.put("customerName", customerName);
			accountMap.put("customerMobile", customerMobile);
			accountMap.put("customerCardNo", customerCardNo);
			accountMap.put("accountAmount", accountAmount);
			accountMap.put("withholdingStatus", EnumCmbcWhHoldStatus.getName(currWithholdingStatus));
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
