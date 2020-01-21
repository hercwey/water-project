package com.learnbind.ai.controller.cmbc;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCmbcBatchStatus;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.cmbc.CmbcBatchWithholdRecordService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.cmbc
 *
 * @Title: CmbcWithHoldFileHandelController.java
 * @Description: CMBC代扣文件处理
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午5:16:36
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/cmbc-holdfile-handel")
public class CmbcWithHoldFileHandelController {
	
	private static final Logger logger = LoggerFactory.getLogger(CmbcWithHoldFileHandelController.class);
	
	private static final String TEMPLATE_PATH = "cmbc/holdfile_handel/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CmbcBatchWithholdRecordService cmbcBatchWithholdRecordService;//中国民生银行批量代扣记录服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private CmbcBatchWithholdRecordService CmbcBatchWithholdRecordService;//批量代扣文件
	
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
		
		return TEMPLATE_PATH + "hold_file_table";
	}
	
	
	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/import-file-dialog")
	public String loadImportFileDialog(Model model) {
		return TEMPLATE_PATH + "import_file_dialog";
	}
	
	
	/**
	 * @Title: importReurnFile
	 * @Description: 导入回盘文件
	 * @param request
	 * @param model
	 * @param fileType
	 * @param inputName
	 * @param holdFileId
	 * @return 
	 */
	@RequestMapping(value = "/import-return-file")
	@ResponseBody
	public Object importReurnFile(HttpServletRequest request, Model model, String fileType, String inputName, Long holdFileId) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date date = new Date();
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			//String prefix = this.getPathPrefix(path);
			
			//上传协议文件
			List<String> uploadPathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			List<String> filePathList = new ArrayList<>();
			if(uploadPathList!=null && uploadPathList.size()>0) {
				
				for(String uploadPath : uploadPathList) {
					String filePath = StringUtils.replace(uploadPath, "/", File.separator);
					filePathList.add(filePath);
				}
				
			}else {
				return JSON.toJSONString(RequestResultUtil.getResultFail("没有上传文件，请选择上传文件！"));
			}
			
			String filePathJson = JSON.toJSONString(filePathList);//把多个文件路径转为JSON保存
			
			CmbcBatchWithholdRecord record = cmbcBatchWithholdRecordService.selectByPrimaryKey(holdFileId);
			record.setSummaryFile(filePathJson);
			record.setCmbcVchrFile(filePathJson);//上传文件名称
			//保存回盘文件路径到CMBC代扣文件
			int rows = cmbcBatchWithholdRecordService.updateByPrimaryKeySelective(record);
			if(rows>0) {
				//修改代扣文件状态为已导入回盘
				record.setStatus(EnumCmbcBatchStatus.IMPORT_RETURN_FILE.getValue());
				cmbcBatchWithholdRecordService.updateByPrimaryKeySelective(record);
				return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
	}
	
	//------------------------------	上传文件通用方法	------------------------------
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getUploadFolder();
		System.out.println("----------"+path);
		return path;
	}
	
	/**
	 * @Title: getPathPrefix
	 * @Description: 拼接盘符
	 * @param path
	 * @return 
	 */
	public String getPathPrefix(String path) {
		return path.substring(0, path.indexOf("upload")-1);
	}
	
public static void main(String[] args) {
	String a = "d:/upload/cmbc_txt/20190825/2019082520391720184098.txt";
	//String b = a.replaceAll("/", "\\\\");
	a = StringUtils.replace(a, "/", File.separator);
	System.out.println(a);
}
	
}
