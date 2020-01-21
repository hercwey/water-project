package com.learnbind.ai.controller.cmbc;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCmbcBatchStatus;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.cmbc.CmbcBatchWithholdRecordService;
import com.learnbind.ai.service.cmbc.CmbcBusinessBatchWithholdService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.cmbc
 *
 * @Title: CmbcCustomerAccountItemController.java
 * @Description: 中国民生银行批量代扣业务流程处理控制器
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午4:28:23
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/cmbc-cus-account")
public class CmbcCustomerAccountItemController {
	
	private static final Logger logger = LoggerFactory.getLogger(CmbcCustomerAccountItemController.class);
	
	private static final String TEMPLATE_PATH = "cmbc/account_item/";//页面目录
	private static final String TEMPLATE_HOLD_PATH = "cmbc/account_item/hold_file/";//代扣文件
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private CmbcBatchWithholdRecordService cmbcBatchWithholdRecordService;//中国民生银行批量代扣记录服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CmbcBusinessBatchWithholdService cmbcBusinessBatchWithholdService;//CMBC批量代扣业务处理
	@Autowired
	private AddSubWaterService addSubWaterService;//追加减免水量
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "cmbc_account_starter";
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
		
		return TEMPLATE_PATH + "cmbc_account_main";
	}
	
	/**
	 * @Title: table
	 * @Description: 导出批量代扣table页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCcbCustomerAccountItem(searchCond, traceIds, period, deductMode, startDate, endDate);
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.getListGroupByCustomerId(searchCond, traceIds, period, deductMode, startDate, endDate,null);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

//		I.CUSTOMER_ID,	客户ID
//		LISTAGG ( I.ID, ',' ) WITHIN GROUP ( ORDER BY I.ID ) AS BILL_IDS,	账单ID,多条记录用逗号","分隔
//		I.PERIOD,	期间
//		-- I.*,
//		SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,	账单金额
//		SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,	已充值金额
//		( SUM(I.CREDIT_AMOUNT) - SUM(I.DEBIT_AMOUNT) ) AS OWE_AMOUNT	欠费金额
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
//			String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
			String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			
			String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置
			
			//计算违约金总金额
			//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			//BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationService.getPlace(currTraceIds);
			accountItemMap.put("place", place);//地理位置
			
			//accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			//accountItemMap.put("overdueValue", overdueValue);//违约金总金额
			
			//查询往期水费欠费金额
			BigDecimal pastWaterFeeOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customerId, period);
			//查询往期违约金欠费金额
			//BigDecimal pastOverdueOweAmount = customerAccountItemService.getPastOverdueOweAmount(customerId, period);
			//往期欠费总金额=往期水费欠费金额+往期违约金欠费金额
			BigDecimal pastOweTotalAmount = pastWaterFeeOweAmount;//BigDecimalUtils.add(pastWaterFeeOweAmount, pastOverdueOweAmount);
			accountItemMap.put("pastOweTotalAmount", pastOweTotalAmount);
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，本期欠费金额+往期欠费金额
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweTotalAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			
			//获取贷方摘要
//			String creditSubject = "";
//			if(accountItemMap.get("CREDIT_SUBJECT") != null) {
//				creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());
//
//			}
//			accountItemMap.put("creditSubject", creditSubject);
			//是否追加减免水量
			boolean isAddSubWater = false;
			AddSubWater log = addSubWaterService.getAddSubLog(Long.valueOf(customerId), periodTemp, null);
			if(log!=null) {
				isAddSubWater = true;
			}
			accountItemMap.put("isAddSubWater", isAddSubWater);

		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "cmbc_account_table";
	}
	
	/**
	 * @Title: loadGenerateFileDialog
	 * @Description:生成批量代扣文件对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-generate-file-dialog")
	public String loadGenerateFileDialog(Model model) {
		return TEMPLATE_PATH + "generate_file_dialog";
	}
	
	
	/**
	 * @Title: generateWithHoldingFile
	 * @Description: 生成批量代扣文件
	 * @param accountItemIdStr
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @param deductMode
	 * @param period
	 * @param comment
	 * @param pinYinCode
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/generate-holding-file", produces = "application/json")
	@ResponseBody
	public Object generateWithHoldingFile(String customerIds, String searchCond, String traceIds, String searchPeriod, Integer deductMode, 
				String period, String remark, String pinYinCode) throws Exception {
		
		if(StringUtils.isNotBlank(customerIds)) {//如果用户选择客户账单导出时
			return this.generatorWithholdFileByAiid(customerIds, period, traceIds, remark, pinYinCode);
		}else {
			
			if(StringUtils.isNotBlank(traceIds)) {//如果用户选择地理位置条件导出时
				return this.generatorWithholdFileByTraceIds(searchCond, traceIds, period, deductMode, remark, pinYinCode);
			}else {//如果用户未选择地理位置时
				return RequestResultUtil.getResultFail("请选择地理位置！");
			}
			
		}
		
	}
	
	/**
	 * @Title: generatorWithholdFileByAiid
	 * @Description: 根据用户选择客户ID查询并导出excel文件
	 * @param customerIds
	 * @param period
	 * @param remark
	 * @param pyCode
	 * @return 
	 */
	private Map<String, Object> generatorWithholdFileByAiid(String customerIds, String period, String traceIds, String remark, String pyCode){
		
		List<Long> customerIdList = new ArrayList<>();
		String[] customerIdArr = customerIds.split(",");
		for(String customerIdStr : customerIdArr) {
			if(StringUtils.isNotBlank(customerIdStr)) {
				Long customerId = Long.valueOf(customerIdStr);
				customerIdList.add(customerId);
			}
		}
		int rows = cmbcBusinessBatchWithholdService.exportCmbcAccountItem(customerIdList, period, traceIds, remark, pyCode);
		if(rows>0) {
			return RequestResultUtil.getResultSuccess("民生银行欠费账单生成批量代扣文件成功！");
		}else if(rows==-1) {
			return RequestResultUtil.getResultFail("没有需要民生银行代扣的欠费账单！");
		}else {
			return RequestResultUtil.getResultFail("生成批量代扣文件失败，请联系开发人员检查相关日志！");
		}
	}
	
	/**
	 * @Title: generatorWithholdFileByTraceIds
	 * @Description: 根据用户选择的地理位置查询并导出excel文件
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param remark
	 * @param pyCode
	 * @return 
	 */
	private Map<String, Object> generatorWithholdFileByTraceIds(String searchCond, String traceIds, String period, Integer deductMode, String remark, String pyCode){
		//如果地理位置traceIds不为空时，根据条件查询需要民生银行代扣，且结算状态!=1且贷方金额-借方金额>0的水费账单
		List<Map<String, Object>> itemList = customerAccountItemService.getListGroupByCustomerId(searchCond, traceIds, period, deductMode, null, null,null);
		if(itemList==null || itemList.size()<=0) {//如果查询账单为空，则直接继续下次循环
			return RequestResultUtil.getResultFail("没有需要民生银行代扣的欠费账单！");
		}
		
		List<Long> customerIdList = new ArrayList<>();
		for (Map<String, Object> accountItemMap : itemList) {
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			if(StringUtils.isNotBlank(customerIdStr)) {
				Long customerId = Long.valueOf(customerIdStr);//客户ID
				customerIdList.add(customerId);
			}
			
		}	
		
		//处理数据生成代扣文件
		int rows = cmbcBusinessBatchWithholdService.exportCmbcAccountItem(customerIdList, period, traceIds, remark, pyCode);;
		if(rows>0) {
			return RequestResultUtil.getResultSuccess("民生银行欠费账单生成批量代扣文件成功！");
		}else if(rows==-1) {
			return RequestResultUtil.getResultFail("没有需要民生银行代扣的欠费账单！");
		}else {
			return RequestResultUtil.getResultFail("生成批量代扣文件失败，请联系开发人员检查相关日志！");
		}
	}
	
	//************************************批量代扣文件选项卡***********************
	/**
	 * @Title: holdFileMain
	 * @Description: 批量代扣文件main
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-holding-file-main")
	public String holdFileMain(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_HOLD_PATH + "with_holding_file_main";
	}
	
	/**
	 * @Title: loadGlobalLocationPage
	 * @Description: 加载通用地理位置页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-global-location-file-page")
	public String loadGlobalLocationPage(Model model) {

		return TEMPLATE_HOLD_PATH + "global_location_file_page";
	}
	
	/**
	 * @Title: holdFileTable
	 * @Description: 批量代扣文件table
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	@RequestMapping(value = "/hold-file-table")
	public String holdFileTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period, Integer holdStatus) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = cmbcBatchWithholdRecordService.searchCmbcHoldFileItem(searchCond, traceIds, period, holdStatus);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		for(Map<String, Object> accountMap : accountItemMapList) {
			String withholdFile = accountMap.get("WITHHOLD_FILE").toString();//文件名称截取
			withholdFile = withholdFile.substring(withholdFile.lastIndexOf(File.separator)+1);
			
			Integer cmbcVchrFileSize = null;
			if(accountMap.get("CMBC_VCHR_FILE") != null) {
				String cmbcVchrFile = accountMap.get("CMBC_VCHR_FILE").toString();//返回文件名称截取
				List<String> filePathList = JSON.parseArray(cmbcVchrFile, String.class);
				cmbcVchrFileSize = filePathList.size();
			}
			
			String place = "";
			if(accountMap.get("TRACE_ID") != null) {
				String currTraceIds = accountMap.get("TRACE_ID").toString();//返回文件名称截取
				place = locationService.getPlace(currTraceIds);//地理位置
			}
			
			accountMap.put("subWithholdFile", withholdFile);
			accountMap.put("subCmbcVchrFileSize", cmbcVchrFileSize);
			accountMap.put("place", place);
		}
	

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_HOLD_PATH + "with_holding_file_table";
	}
	
	/**
	 * @Title: downloadWithholdFile
	 * @Description: 下载批量代扣文件
	 * @param response
	 * @param id
	 * @throws Exception 
	 */
	@RequestMapping(value = "/download-withhold-file")
	public void downloadWithholdFile(HttpServletResponse response, Long id) throws Exception {
		
		//查询批量代扣记录
		CmbcBatchWithholdRecord record = cmbcBatchWithholdRecordService.selectByPrimaryKey(id);
		
		String filePath = record.getWithholdFile();//批量代扣文件路径
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator));//获取文件名
		
		try {
			boolean flag = DownLoadFileUtil.downLoad(fileName, "xls", filePath, response);
			if(flag) {
				CmbcBatchWithholdRecord recordTemp = new CmbcBatchWithholdRecord();
				recordTemp.setId(record.getId());
				recordTemp.setStatus(EnumCmbcBatchStatus.DOWNLOAD.getValue());
				cmbcBatchWithholdRecordService.updateByPrimaryKeySelective(recordTemp);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * @Title: exportHoldFile
	 * @Description: 导出批量代扣文件（Excel）
	 * @param holdFileIdStr
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @return
	 * @throws Exception 
	 */
//	@RequestMapping(value = "/upload-holding-file", produces = "application/json")
//	@ResponseBody
//	public Object uploadHoldFile(String holdFileIdStr, String searchCond, String traceIds, String searchPeriod) throws Exception {
//		int rows = 1;
//		String[] holdFileIdArr = holdFileIdStr.split(",");
//		
//		
//		
//		return RequestResultUtil.getResultAddSuccess("上传代扣文件成功");
//	}
	
	
}
