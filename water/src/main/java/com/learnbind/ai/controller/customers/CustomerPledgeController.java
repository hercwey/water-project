package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerPledge;
import com.learnbind.ai.service.customers.PledgeService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerPledgeController.java
 * @Description: 客户押金
 *
 * @author Thinkpad
 * @date 2019年5月22日 上午10:39:20
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-pledge")
public class CustomerPledgeController {
	private static Log log = LogFactory.getLog(CustomerPledgeController.class);
	private static final String TEMPLATE_PATH = "customers/pledge/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private PledgeService pledgeService;  //客户-押金信息
	
	/**
	 * @Title: main
	 * @Description: 加载客户押金信息
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		return TEMPLATE_PATH + "pledge_main";
	}

	/**
	 * @Title: table
	 * @Description: 押金信息table
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param pageNum
	 * @param pageSize
	 * @param customerId
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String functionModule, Integer pageNum, Integer pageSize, Long customerId, String searchCond) {

		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerPledge> pledgeList = pledgeService.searchCustomerPledge(customerId, searchCond);
		PageInfo<List<CustomerPledge>> pageInfo = new PageInfo(pledgeList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("pledgeList", pledgeList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "pledge_table";
	}
	
	/**
	 * @Title: loadAddPledgeDialog
	 * @Description: 加载增加客户押金信息对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadaddpledgedialog")
	public String loadAddPledgeDialog(Model model) {
		
		return TEMPLATE_PATH + "pledge_dialog_edit";
	}
	
	/**
	 * @Title: addPledge
	 * @Description: 增加客户押金信息
	 * @param bank
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addPledge(CustomerPledge pledge) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		
		pledge.setOperatorName(userBean.getRealname());
		pledge.setOperatorId(userBean.getId());
		pledge.setOperationTime(new Date());
		pledge.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		int row = pledgeService.insertCustomerPledge(pledge);
		if (row > 0) {
			return RequestResultUtil.getResultAddSuccess();
		} else {
			return RequestResultUtil.getResultAddWarn();
		}
			
			
	}
	
	/**
	 * @Title: deletepledge
	 * @Description: 删除客户开票信息
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deletePledge(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				pledgeService.deleteCustomerPledge(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	
	/**
	 * @Title: loadModipledgeDialog
	 * @Description: 修改客户银行信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodipledgedialog")
	public String loadModiPledgeDialog(Long itemId, Model model) {
		
		//读取需要编辑的条目
		CustomerPledge currItem=pledgeService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "pledge_dialog_edit";
	}
	
	

	/**
	 * @Title: updatepledge
	 * @Description: 编辑客户银行信息
	 * @param bank
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updatePledge(CustomerPledge pledge) throws Exception {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		
		pledge.setOperatorName(userBean.getRealname());
		pledge.setOperatorId(userBean.getId());
		pledge.setOperationTime(new Date());
		
		pledgeService.updateByPrimaryKeySelective(pledge);
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	


}