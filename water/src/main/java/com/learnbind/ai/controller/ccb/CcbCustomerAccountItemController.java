package com.learnbind.ai.controller.ccb;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.ccb.CcbBatchCustomerAccountService;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.ccb.CcbBusinessBatchWithholdService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.ccb
 *
 * @Title: CcbBusinessProcessController.java
 * @Description: 中国建设银行批量代扣业务流程处理控制器
 *
 * @author Administrator
 * @date 2019年7月14日 下午3:43:51
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/ccb-cus-account")
public class CcbCustomerAccountItemController {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbCustomerAccountItemController.class);
	
	private static final String TEMPLATE_PATH = "ccb/account_item/";//页面目录
	private static final String TEMPLATE_HOLD_PATH = "ccb/account_item/hold_file/";//代扣文件
	private static final String TEMPLATE_DETAIL_PATH = "ccb/account_item/hold_file_detail/";//代扣文件详情
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
	private LocationCustomerService locationCustomerService;//地理位置-客户
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private CcbBatchCustomerAccountService ccbBatchCustomerAccountService;//批量代扣文件-账单关系表
	@Autowired
	private CcbBusinessBatchWithholdService CcbBusinessBatchWithholdService;//CCB业务处理批量代扣服务
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
		return TEMPLATE_PATH + "ccb_account_starter";
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
		
		return TEMPLATE_PATH + "ccb_account_main";
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
	@RequestMapping(value = "/table-old")
	public String tableOld(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCcbCustomerAccountItem(searchCond, traceIds, period, deductMode, startDate, endDate);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {
			String accountItemIdStr = accountItemMap.get("ID").toString();//账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);//账单ID
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			//Long customerId = Long.valueOf(customerIdStr);//客户ID
			//String currPeriod = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置traceIds
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额-贷方金额（账单金额）-借方金额（充值金额）
			
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationService.getPlace(currTraceIds);
			accountItemMap.put("place", place);//地理位置
			
			accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			accountItemMap.put("overdueValue", overdueValue);//违约金总金额
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，欠费金额+违约金欠款
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			
			//获取贷方摘要
			String creditSubject = "";
			if(accountItemMap.get("CREDIT_SUBJECT") != null) {
				creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());

			}
			accountItemMap.put("creditSubject", creditSubject);

		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "ccb_account_table";
	}
	
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper

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
			String currPeriod = accountItemMap.get("PERIOD").toString();//期间
			
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
			BigDecimal pastWaterFeeOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customerId, currPeriod);
			//查询往期违约金欠费金额
			//BigDecimal pastOverdueOweAmount = customerAccountItemService.getPastOverdueOweAmount(customerId, currPeriod);
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
			AddSubWater log = addSubWaterService.getAddSubLog(Long.valueOf(customerId), currPeriod, null);
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
		
		return TEMPLATE_PATH + "ccb_account_table";
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
	 * @param period		用户输入的期间
	 * @param comment		用户输入的备注
	 * @param pinYinCode	用户输入的前缀
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/generate-holding-file", produces = "application/json")
	@ResponseBody
	public Object generateWithHoldingFile(String customerIds, String searchCond, String traceIds, String searchPeriod, Integer deductMode, 
				String period, String remark, String pinYinCode) throws Exception {
		
		try {
			//生成批量代扣文件业务处理
			int rows = CcbBusinessBatchWithholdService.generateBatchWithholdRecord(customerIds, searchCond, traceIds, searchPeriod, deductMode, period, remark, pinYinCode);
			if(rows>0) {
				return RequestResultUtil.getResultAddSuccess("生成代扣文件成功");
			}else if(rows==-1) {
				return RequestResultUtil.getResultFail("没有建设银行批量代扣的账单！");
			}
			return RequestResultUtil.getResultFail("生成代扣文件失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成代扣文件异常");
//		int rows = 1;
//		String[] accountItemIdArr = accountItemIdStr.split(",");
//		if(accountItemIdArr.length<1) {//生成代扣文件是没有勾选账单
//			//根据查询条件，将所有查询出的数据（客户的账单数据）生成批量代扣文件
//			List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCcbCustomerAccountItem(searchCond, traceIds, searchPeriod, deductMode);
//			
//			//生成批量代扣文建
//			rows = ccbBatchWithholdRecordService.generateBatchHoldFile(accountItemMapList, period, remark, pinYinCode, traceIds);
//			if(rows>0) {
//				return RequestResultUtil.getResultAddSuccess("生成代扣文件成功");
//			}
//			return RequestResultUtil.getResultAddSuccess("生成代扣文件失败");
//		} else {//生成代扣文件是勾选账单
//			for(String accountItemId : accountItemIdArr) {
//				int index = 1;
//				String withHoldNo = String.valueOf(index);
//				Long customerAccountItemId = Long.valueOf(accountItemId);
//				
//				rows = ccbBatchWithholdRecordService.singleGenerateBatchHoldFile(customerAccountItemId, period, remark, pinYinCode, traceIds, withHoldNo);
//				index++;
//			}
//			return RequestResultUtil.getResultAddSuccess("生成代扣文件成功");
//		}
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
		List<Map<String, Object>> accountItemMapList = ccbBatchWithholdRecordService.searchCcbHoldFileItem(searchCond, traceIds, period, holdStatus);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		for(Map<String, Object> accountMap : accountItemMapList) {
			String withholdFile = accountMap.get("WITHHOLD_FILE").toString();//文件名称截取
			withholdFile = withholdFile.substring(withholdFile.lastIndexOf(File.separator)+1);
			
			String ccbVchrFile = "";
			if(accountMap.get("CCB_VCHR_FILE") != null) {
				ccbVchrFile = accountMap.get("CCB_VCHR_FILE").toString();//返回文件名称截取
				ccbVchrFile = ccbVchrFile.substring(ccbVchrFile.lastIndexOf(File.separator)+1);
			}
			String place = "";
			if(accountMap.get("TRACE_ID") != null) {
				String currTraceIds = accountMap.get("TRACE_ID").toString();//返回文件名称截取
				place = locationService.getPlace(currTraceIds);//地理位置
			}
			
			accountMap.put("subWithholdFile", withholdFile);
			accountMap.put("subCcbVchrFile", ccbVchrFile);
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
	 * @Title: uploadHoldFile
	 * @Description: 上传批量代扣文件
	 * @param holdFileIdStr
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/upload-holding-file", produces = "application/json")
	@ResponseBody
	public Object uploadHoldFile(String holdFileIdStr, String searchCond, String traceIds, String searchPeriod) throws Exception {
		
		try {
			List<CcbBatchWithholdRecord> recordList = new ArrayList<>();//需要上传到CCB的批量代扣文件集合		
			if(StringUtils.isNotBlank(holdFileIdStr)) {//用户选择代扣文件上传
				String[] holdFileIdArr = holdFileIdStr.split(",");
				
				//根据用户选择的需要上传的批量代扣文件ID，查询批量代扣文件集合，并add到list集合
				for(String holdFileId : holdFileIdArr) {
					if(StringUtils.isNotBlank(holdFileId)) {
						//查询批量代扣文件记录信息
						CcbBatchWithholdRecord record = ccbBatchWithholdRecordService.selectByPrimaryKey(Long.valueOf(holdFileId));
						recordList.add(record);
					}
					
				}
				
			}else {//用户未选择代扣文件，则按条件查询
				Integer holdStatus = EnumCcbBatchStatus.GENERATE.getValue();//状态为已生成
				recordList = ccbBatchWithholdRecordService.searchCcbHoldFileList(searchCond, traceIds, searchPeriod, holdStatus);
			}
			
			if(recordList!=null && recordList.size()>0) {
				//批量上传批量代扣文件到CCB
				return ccbBatchWithholdRecordService.uploadWithholdFileToCcb(recordList);
			}else {
				return RequestResultUtil.getResultFail("没有需要上传的代扣文件！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("上传的代扣文件异常，请检查日志！");
		
	}
	
	
	/**
	 * @Title: deleteHoldFile
	 * @Description: 删除批量代扣文件
	 * @param itemId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete-hold-file", produces = "application/json")
	@ResponseBody
	public Object deleteHoldFile(Long itemId) throws Exception {
		int rows = 1;
		rows = ccbBatchWithholdRecordService.deleteHoldFile(itemId);
		if(rows > 0) {
			
			return RequestResultUtil.getResultDeleteSuccess();
		}
		return RequestResultUtil.getResultFail("代扣文件中存在已代扣成功的账单，无法删除");
	}
	
	

	//---------------------------------代扣文件详情选项卡----------------------------------
	
	@RequestMapping(value = "/load-with-hold-file-detail-main")
	public String loadMeterBookMain(Model model) {
		
		return TEMPLATE_DETAIL_PATH + "with_hold_file_detail_main";
	}
	
	/**
	 * @Title: withHoldFileTable
	 * @Description: 代扣文件详情
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param withHoldFileId
	 * @param withHoldStatus
	 * @param settleAccountStatus
	 * @return 
	 */
	@RequestMapping(value = "/with-hold-file-detail-table")
	public String withHoldFileTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long withHoldFileId, Integer withHoldStatus, Integer settleAccountStatus) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> withholdDetailList = ccbBatchCustomerAccountService.selectHoldFileDetail(withHoldFileId, searchCond, withHoldStatus, null);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(withholdDetailList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> withHoldFileMapList = new ArrayList<>();
		for(Map<String, Object> withholdDetail : withholdDetailList) {
			Date joinTime = (Date)withholdDetail.get("JOIN_TIME");//加入代扣文件时间
			String joinTimeStr = sdf.format(joinTime);
			withholdDetail.put("joinTimeStr", joinTimeStr);//获取加入代扣文件时间字符串

		}

		// 传递如下数据至前台页面
		model.addAttribute("withholdDetailList", withholdDetailList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_DETAIL_PATH + "with_hold_file_detail_table";
	}
	
}
