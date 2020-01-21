package com.learnbind.ai.controller.customers;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumOperationType;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAgreement;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerAgreementService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerAccountController.java
 * @Description: 客户-协议前端控制器
 *
 * @author Administrator
 * @date 2019年5月29日 下午4:36:58
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-agreement")
public class CustomerAgreementController {
	
	private static Log log = LogFactory.getLog(CustomerAgreementController.class);
	private static final String TEMPLATE_PATH = "customers/account/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户服务
	@Autowired
	private CustomerAccountService customerAccountService;//客户-账户服务
	@Autowired
	private CustomerAgreementService customerAgreementService;//客户-协议服务
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private CustomersTraceService traceService;//客户档案维护日志

	
	/*------------------------------	客户-协议	------------------------------*/
	
	/**
	 * @Title: main
	 * @Description: 加载客户银行信息
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/agreement-main")
	public String agreementMain(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		
		return TEMPLATE_PATH + "agreement_main";
	}
	/**
	 * @Title: loadAgreementItem
	 * @Description: 加载客户-协议
	 * @param model
	 * @param functionModule
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-agreement-item")
	public String loadAgreementItem(Model model, String functionModule, Long customerId) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		CustomerAgreement record = new CustomerAgreement();
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerAgreement> agreementList = customerAgreementService.select(record);
		
		model.addAttribute("agreementList", agreementList);
		
		return TEMPLATE_PATH + "agreement_item_table";
	}
	
	/**
	 * @Title: loadAgreementDialog
	 * @Description: 加载协议上传对话框
	 * @param model
	 * @param functionModule
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-agreement-dialog")
	public String loadAgreementDialog(Model model, String functionModule, Long customerId) {
		
		return TEMPLATE_PATH + "agreement_dialog_edit";
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
	@RequestMapping(value = "/agreement-upload")
	@ResponseBody
	public Object upload(HttpServletRequest request, Model model, String fileType, String inputName, CustomerAgreement agreement) {
		int rows = 0;
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			
			//上传协议文件
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			if(filePathList!=null && filePathList.size()>0) {
				for(String filePath : filePathList) {
					System.out.println("上传图片路径："+filePath);
					filePath = FileUploadUtil.subImgPath(filePath);
					System.out.println("截取后上传图片路径："+filePath);
					//设置主键为空
					agreement.setId(null);
					agreement.setPath(filePath);
					//保存客户-协议到数据库
					agreement.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
					//agreement.setPath(filePathList.toString());
					rows = customerAgreementService.insertSelective(agreement);
				}
				
			}
			
			if(rows>0) {
				return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
			}
			
			//Map<String, Object> respMap = RequestResultUtil.getResultUploadSuccess();
			//respMap.put("filePathList", filePathList);
			//return JSON.toJSONString(respMap);
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
	@RequestMapping(value = "/delete-agreement")
	@ResponseBody
	public Object deleteAgreement(HttpServletRequest request, Model model, Long id, String path) {
		
		try {
			//删除协议文件
			//TODO
			
			//删除客户-协议
			CustomerAgreement agreement = new CustomerAgreement();
			agreement.setId(id);
			agreement.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			int rows = customerAgreementService.updateByPrimaryKeySelective(agreement);
			if(rows>0) {
				return RequestResultUtil.getResultDeleteSuccess();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultDeleteWarn();
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
	
	/**
	 * @Title: subImgPath
	 * @Description: 截取上传图片路径
	 * @param path
	 * 		如：windows=d:/upload/img/20190701/abc.jpg
	 * 		如：linux=/home/upload/img/20190701/abc.jpg
	 * @return 
	 * 		如：upload/img/20190701/abc.jpg
	 */
	private String subImgPath(String path) {
		path = path.substring(path.indexOf("upload"));
		path = File.separator+path;
		return path;
	}
	
	public static void main(String[] args) {
		String path = "d:/upload/img/20190701/abc.jpg";
		//String path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("windows:"+path);
		path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("linux:"+path);
	}
	
}