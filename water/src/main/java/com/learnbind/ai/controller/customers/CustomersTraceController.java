package com.learnbind.ai.controller.customers;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.service.customers.CustomersTraceService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersTraceController.java
 * @Description: 客户档案维护日志
 *
 * @author Thinkpad
 * @date 2019年7月7日 上午9:57:29
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customers-trace")
public class CustomersTraceController {
	
	private static Log log = LogFactory.getLog(CustomersTraceController.class);
	
	private static final String TEMPLATE_PATH = "customers/customers_trace/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersTraceService customersTraceService;//客户档案维护服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "customers_trace_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "customers_trace_main";
	}
	
	/**
	 * @Title: loginoutLogTable
	 * @Description: 登陆登出日志
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String loginoutLogTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomersTrace> traceList = customersTraceService.search(searchCond);
		PageInfo<List<CustomersTrace>> pageInfo = new PageInfo(traceList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("traceList", traceList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "customers_trace_table";
	}
	
	@RequestMapping(value = "/detail")
	public String loadModiDialog(Long itemId, Model model) {
		
		Map<String, Object> traceMap = null;
		if(itemId!=null && itemId>0) {
			CustomersTrace trace = customersTraceService.selectByPrimaryKey(itemId);
			
			//HTML转义解码
			String changeBefore = StringEscapeUtils.unescapeHtml(trace.getChangeBefore());
			//HTML转义解码
			String changeAfter = StringEscapeUtils.unescapeHtml(trace.getChangeAfter());
			traceMap = EntityUtils.entityToMap(trace);
			traceMap.put("operationTimeStr", trace.getOperationTimeStr());//操作时间
			traceMap.put("operationTypeStr", trace.getOperationTypeStr());//操作类型
			traceMap.put("changeBefore", changeBefore);//变更前
			traceMap.put("changeAfter", changeAfter);//变更后
			
		}
		
		model.addAttribute("customer", traceMap);
		
		return TEMPLATE_PATH + "customers_trace_detail";
	}

}