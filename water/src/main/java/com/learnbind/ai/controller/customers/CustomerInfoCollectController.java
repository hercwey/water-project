package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.learnbind.ai.common.enumclass.EnumBindStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumReplyStatus;
import com.learnbind.ai.common.enumclass.EnumSatisfiedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerInfoCollect;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.service.customers.CustomerInfoCollectService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerInfoCollectController.java
 * @Description: 客户信息汇总
 *
 * @author Thinkpad
 * @date 2019年8月2日 上午10:18:06
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-info-collect")
public class CustomerInfoCollectController {
	private static Log log = LogFactory.getLog(CustomerInfoCollectController.class);
	private static final String TEMPLATE_PATH = "customers/info_collect/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomerInfoCollectService customerInfoCollectService;  //客户信息汇总
	

	/**
	 * @Title: starter
	 * @Description: 起始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "info_starter";
	}
	
	/**
	 * @Title: main
	 * @Description: main页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "info_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表信息
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerInfoCollect> infoList = customerInfoCollectService.searchCond(searchCond);
		PageInfo<List<CustomerInfoCollect>> pageInfo = new PageInfo(infoList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(CustomerInfoCollect customerInfoCollect : infoList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customerInfoCollect);
			String question = StringEscapeUtils.unescapeHtml(customerInfoCollect.getQuestion());
			String solution = StringEscapeUtils.unescapeHtml(customerInfoCollect.getSolution());
			
			customerMap.put("operationTimeStr", customerInfoCollect.getOperationTimeStr());
			customerMap.put("replyStatusStr", customerInfoCollect.getReplyStatusStr());
			customerMap.put("satisfiedStatusStr", customerInfoCollect.getSatisfiedStatusStr());
			customerMap.put("question", question);
			customerMap.put("solution", solution);
			
			
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("infoList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "info_table";
	}
	
	
	/**
	 * @Title: loadAddDialog
	 * @Description: 新增对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadadddialog")
	public String loadAddDialog(Model model) {
		String title = "增加";
		model.addAttribute("title", title);
		return TEMPLATE_PATH + "info_edit_dialog";
	}
	
	/**
	 * @Title: addInfo
	 * @Description: 新增
	 * @param customerInfoCollect
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addInfo(CustomerInfoCollect customerInfoCollect) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		customerInfoCollect.setQuestion(StringEscapeUtils.escapeHtml(customerInfoCollect.getQuestion()));
		customerInfoCollect.setSolution(StringEscapeUtils.escapeHtml(customerInfoCollect.getSolution()));
		customerInfoCollect.setOperationTime(new Date());
		customerInfoCollect.setOperator(userBean.getRealname());
		customerInfoCollect.setReplyStatus(EnumReplyStatus.REPLY_NO.getValue());//默认是未回复的状态
		customerInfoCollect.setSatisfiedStatus(EnumSatisfiedStatus.SATISFIED_YES.getValue());//默认是满意的状态
		int row = customerInfoCollectService.insertSelective(customerInfoCollect);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: deleteInfo
	 * @Description: 删除信息
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteInfo(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				customerInfoCollectService.deleteByPrimaryKey(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	
	/**
	 * @Title: loadModiInfoDialog
	 * @Description: 修改信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiInfoDialog(Long itemId, Model model) {
		
		//读取需要编辑的条目
		CustomerInfoCollect currItem = customerInfoCollectService.selectByPrimaryKey(itemId);
		currItem.setQuestion(StringEscapeUtils.unescapeHtml(currItem.getQuestion()));
		currItem.setSolution(StringEscapeUtils.unescapeHtml(currItem.getSolution()));
		model.addAttribute("currItem",currItem);
		
		String title = "编辑";
		model.addAttribute("title", title);
		
		return TEMPLATE_PATH + "info_edit_dialog";
	}
	

	/**
	 * @Title: updateInfo
	 * @Description: 编辑信息
	 * @param bank
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateInfo(CustomerInfoCollect customerInfoCollect) throws Exception {
		customerInfoCollect.setQuestion(StringEscapeUtils.escapeHtml(customerInfoCollect.getQuestion()));
		customerInfoCollect.setSolution(StringEscapeUtils.escapeHtml(customerInfoCollect.getSolution()));
		int rows = customerInfoCollectService.updateByPrimaryKeySelective(customerInfoCollect);
		if (rows > 0)
			return RequestResultUtil.getResultUpdateSuccess();
		else
			return RequestResultUtil.getResultUpdateWarn();
	}
	
	


}