package com.learnbind.ai.controller.customers;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumOperationType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAgreement;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerAgreementService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerAccountController.java
 * @Description: 客户-账户前端控制器
 *
 * @author Administrator
 * @date 2019年5月29日 下午4:36:58
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-account")
public class CustomerAccountController {
	
	private static Log log = LogFactory.getLog(CustomerAccountController.class);
	private static final String TEMPLATE_PATH = "customers/account/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户服务
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计服务
	@Autowired
	private CustomerAccountService customerAccountService;//客户-账户服务
	@Autowired
	private CustomerAgreementService customerAgreementService;//客户-协议服务
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private CustomersTraceService traceService;//客户档案维护日志
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//客户账目
	@Autowired
	private MeterRecordService meterRecordService;  //抄表记录

	/**
	 * @Title: main
	 * @Description: 加载客户银行信息
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model, String functionModule, Long customerId) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		return TEMPLATE_PATH + "account_main";
	}
	
	/*------------------------------	客户-账户	------------------------------*/
	/**
	 * @Title: loadAccountInfo
	 * @Description: 加载账户基本信息
	 * @param model
	 * @param functionModule
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-account-info")
	public String loadAccountInfo(Model model, String functionModule, Long customerId) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		CustomerAccount record = new CustomerAccount();
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerAccount> accountList = customerAccountService.select(record);
		if(accountList!=null && accountList.size()>0) {
			record = accountList.get(0);
		}
		model.addAttribute("account", record);
		
		return TEMPLATE_PATH + "account_info_table";
	}
	
	/**
	 * @Title: loadCustomerAccountDialog
	 * @Description: 加载客户-账户对话框
	 * @param model
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-account-dialog")
	public String loadCustomerAccountDialog(Model model, Long customerId) {
		
		CustomerAccount record = new CustomerAccount();
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerAccount> accountList = customerAccountService.select(record);
		if(accountList!=null && accountList.size()>0) {
			record = accountList.get(0);
		}else {
			//如果该客户没有账户信息，则从客户信息中获取对应信息作为默认值
			Customers customer = customersService.selectByPrimaryKey(customerId);
			record = new CustomerAccount();
			record.setCustomerId(customerId);
			record.setAccountName(customer.getCustomerName());//客户名称
			record.setIdNo(customer.getIdNo());//证件号
			record.setTel(customer.getCustomerTel());//客户电话
			record.setMobile(customer.getCustomerMobile());//客户手机
			record.setStatus(EnumCustomerStatus.ACCOUNT_NO.getValue());//立户状态
		}
		model.addAttribute("currItem", record);
		
		return TEMPLATE_PATH + "account_dialog_edit";
	}
	
	/**
	 * @Title: insertAccount
	 * @Description: 增加账户信息（立户）
	 * @param model
	 * @param account
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insertAccount(Model model, CustomerAccount account) throws Exception {
		
		try {
			
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(userBean==null) {
				return RequestResultUtil.getResultAddWarn("请重新登录！");
			}
			
			account.setCreateTime(new Date());
			account.setOperatorId(userBean.getId());
			account.setOperatorName(userBean.getRealname());
			account.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			account.setStatus(EnumCustomerStatus.ACCOUNT_OPEN.getValue());
			int rows = customerAccountService.insertSelective(account);
			
			//判断该客户是否绑定水表，如果绑定添加抄表记录
			Example example = new Example(CustomerMeter.class);
			Long customerId = account.getCustomerId();
			example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("defaultCustomer", EnumDefaultStatus.DEFAULT_YES.getValue());
			List<CustomerMeter> customerMeterList = customerMeterService.selectByExample(example);
			if(customerMeterList.size()>0) {
				for(CustomerMeter cm : customerMeterList) {
					Long meterId = cm.getMeterId();
					rows = meterRecordService.insertInitMeterRecord(meterId, customerId, userBean.getId(), userBean.getRealname(), null);
					}
				
			}
			
			if(rows>0) {
				
				//立户后修改客户状态
				if(customerId!=null) {
					this.updateCustomerStatus(customerId, EnumCustomerStatus.ACCOUNT_OPEN.getValue());
				}
				
				//增加客户档案维护日志
				CustomersTrace trace = new CustomersTrace();
				trace.setCustomerId(account.getCustomerId());
				trace.setCustomerName(account.getAccountName());
				trace.setOperationTime(new Date());
				trace.setOperatorId(userBean.getId());
				trace.setOperationType(EnumOperationType.SETTLE_ACCOUNT.getValue());//立户
				trace.setOperatorName(userBean.getRealname());
				Customers cus = customersService.selectByPrimaryKey(account.getCustomerId());
				
				//HTML字符编码
				String changeAfter = StringEscapeUtils.escapeHtml(JSON.toJSONString(account));
				String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(cus));
				
				trace.setChangeBefore(changeBefore);
				trace.setChangeAfter(changeAfter);
				traceService.insertSelective(trace);
				return RequestResultUtil.getResultAddSuccess();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultAddWarn();

	}
	
	/**
	 * @Title: updateAccount
	 * @Description: 修改客户-账户信息
	 * @param model
	 * @param account
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateAccount(Model model, CustomerAccount account) throws Exception {
		
		try {
			
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(userBean==null) {
				return RequestResultUtil.getResultUpdateWarn("请重新登录！");
			}
			
			account.setOperatorId(userBean.getId());
			account.setOperatorName(userBean.getRealname());
			int rows = customerAccountService.updateByPrimaryKeySelective(account);
			if(rows>0) {
				return RequestResultUtil.getResultUpdateSuccess();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultUpdateWarn();

	}
	
	/**
	 * @Title: updateStatus
	 * @Description: 修改客户-账户状态
	 * @param model
	 * @param customerId
	 * @param accountStatus
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update-status", produces = "application/json")
	@ResponseBody
	public Object updateStatus(Model model, Long customerId, Integer accountStatus) throws Exception {
		
		try {
			
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(userBean==null) {
				return RequestResultUtil.getResultAddWarn("请重新登录！");
			}
			//如果是销户操作
			if(accountStatus==EnumCustomerStatus.ACCOUNT_CLOSE.getValue()) {
				//判断客户欠费金额是否大于0，大于0表示有欠费，否则表示无欠费
				BigDecimal zero = new BigDecimal("0");
				BigDecimal owedAmount = customerAccountItemService.getCurrBillOwedAmount(customerId);
				if(BigDecimalUtils.greaterThan(owedAmount, zero)) {
					return RequestResultUtil.getResultFail("该客户欠费金额为【"+owedAmount+"】，请结清所有欠款后再继续！");
				}
			}
			
			CustomerAccount account = new CustomerAccount();
			account.setCustomerId(customerId);
			List<CustomerAccount> caList = customerAccountService.select(account);
			if(caList!=null && caList.size()>0) {
				account = caList.get(0);
			}
			
			account.setStatus(accountStatus);
			account.setCreateTime(new Date());
			account.setOperatorId(userBean.getId());
			account.setOperatorName(userBean.getRealname());
			
			int rows = customerAccountService.updateByPrimaryKeySelective(account);
			if(rows>0) {

				this.updateCustomerStatus(customerId, accountStatus);//修改客户状态
				
				//增加客户档案维护日志
				CustomersTrace trace = new CustomersTrace();
				trace.setCustomerId(account.getCustomerId());
				trace.setCustomerName(account.getAccountName());
				trace.setOperationTime(new Date());
				trace.setOperatorId(userBean.getId());
				trace.setOperatorName(userBean.getRealname());
				if(accountStatus==EnumCustomerStatus.ACCOUNT_CLOSE.getValue()) {
					trace.setOperationType(EnumOperationType.CANCEL_ACCOUNT.getValue());//销户
					trace.setChangeBefore(EnumCustomerStatus.ACCOUNT_OPEN.getName());
					trace.setChangeAfter(EnumCustomerStatus.ACCOUNT_CLOSE.getName());
				} else {
					trace.setOperationType(EnumOperationType.RECOVER_ACCOUNT.getValue());//销户恢复
					trace.setChangeBefore(EnumCustomerStatus.ACCOUNT_CLOSE.getName());
					trace.setChangeAfter(EnumCustomerStatus.ACCOUNT_OPEN.getName());
				}
				traceService.insertSelective(trace);
				return RequestResultUtil.getResultSaveSuccess();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultSaveWarn();

	}
	
	/**
	 * @Title: setCustomerStatus
	 * @Description: 修改客户状态
	 * @param customerId
	 * @param customerStatus 
	 */
	private void updateCustomerStatus(Long customerId, Integer customerStatus) {
		Customers customer = new Customers();
		customer.setId(customerId);
		customer.setStatus(customerStatus);
		customersService.updateByPrimaryKeySelective(customer);
	}
	
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
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			
			//上传协议文件
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			
			List<CustomerAgreement> agreementList = new ArrayList<>();//协议集合
			
			if(filePathList!=null && filePathList.size()>0) {
				for(String filePath : filePathList) {
					//String filePath = filePathList.get(0);
					System.out.println("上传图片路径："+filePath);
					filePath = FileUploadUtil.subImgPath(filePathList.get(0));
					System.out.println("截取后上传图片路径："+filePath);
					agreement.setPath(filePath);
					agreement.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
					agreementList.add(agreement);
				}
				
			}
			
			//保存客户-协议到数据库
			int rows = customerAgreementService.insert(agreementList);
			if(rows>0) {
				return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
			}
			
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
	 * 		如：/upload/img/20190701/abc.jpg
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