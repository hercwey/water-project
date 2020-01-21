package com.learnbind.ai.controller.ccb;

import java.io.File;
import java.util.ArrayList;
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
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.ccb
 *
 * @Title: CcbWithHoldFileHandelController.java
 * @Description: CCB代扣文件处理
 *
 * @author Thinkpad
 * @date 2019年8月21日 上午10:31:41
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/ccb-holdfile-handel")
public class CcbWithHoldFileHandelController {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbWithHoldFileHandelController.class);
	
	private static final String TEMPLATE_PATH = "ccb/holdfile_handel/";//页面目录
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
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private CcbBatchWithholdRecordService CcbBatchWithholdRecordService;//批量代扣文件
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "hold_file_starter";
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
		
		return TEMPLATE_PATH + "hold_file_main";
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
			pageSize =PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = CcbBatchWithholdRecordService.searchCcbHoldFileItem(searchCond, traceIds, period, holdStatus);
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
		
		return TEMPLATE_PATH + "hold_file_table";
	}
	
	//***************************批量代扣文件处理任务情况**************
	
	@RequestMapping(value = "/handel-task-main")
	public String handelTaskain(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "handel_task_main";
	}
	
	
	/**
	 * @Title: loadGlobalLocationPage
	 * @Description: 加载通用地理位置页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-global-location-handel-page")
	public String loadGlobalLocationPage(Model model) {

		return TEMPLATE_PATH + "global_location_handel_page";
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
	@RequestMapping(value = "/handel-task-table")
	public String handelTaskTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period, Integer holdStatus) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize =PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = CcbBatchWithholdRecordService.searchCcbHoldFileItem(searchCond, traceIds, period, holdStatus);
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
		
		return TEMPLATE_PATH + "handel_task_table";
	}
	
	/**
	 * @Title: handelHoldFile
	 * @Description: 申请处理批量代扣文件
	 * @param holdFileIdStr
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/ask-handel-holding-file", produces = "application/json")
	@ResponseBody
	public Object handelHoldFile(String holdFileIdStr, String searchCond, String traceIds, String searchPeriod) throws Exception {
		
		try {
			List<CcbBatchWithholdRecord> recordList = new ArrayList<>();//需要上传到CCB的批量代扣文件集合		
			if(StringUtils.isNotBlank(holdFileIdStr)) {//用户选择代扣文件上传
				String[] holdFileIdArr = holdFileIdStr.split(",");
				
				//根据用户选择的需要申请处理的批量代扣文件ID，查询批量代扣文件集合，并add到list集合
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
				//申请批量处理代扣文件
				return ccbBatchWithholdRecordService.applyProcessWithholdFile(recordList);
			}else {
				return RequestResultUtil.getResultFail("没有需要申请处理的代扣文件！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("申请处理代扣文件异常，请检查日志！");
		
	}
	
}
