package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumOperationType;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.meters.MetersService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: WaterStatusController.java
 * @Description: 修改用水状态
 *
 * @author Thinkpad
 * @date 2019年5月25日 上午10:51:57
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/water-status")
public class WaterStatusController {
	private static Log log = LogFactory.getLog(WaterStatusController.class);
	private static final String TEMPLATE_PATH = "customers/water_status/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	private MetersService metersService;  //表计档案
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计绑定表
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private DiscountService discountService;  //政策减免
	@Autowired
	private CustomersTraceService traceService;//客户档案维护日志
	

	/** 
	*	@Title: roleStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String roleStarter(Model model) {
		return TEMPLATE_PATH + "water_starter";
	}

	/** 
	*	@Title: customersSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String customersSearch(Model model) {
		//加载数据字典-用水性质、行业性质
		List<DataDict> waterUseList = dataDictService.getListByTypeCode(DataDictType.WATER_USE);
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
						
		model.addAttribute("waterUseList", waterUseList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		return TEMPLATE_PATH + "water_main";
	}

	/** 
	*	@Title: customersTable 
	*	@Description: 加载用户列表(列表页面)
	*	@param @param model ModelView中传递数据的对象
	*	@param @param pageNum 页号
	*	@param @param pageSize 页大小
	*	@param @param searchCond 查询条件
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String customersTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String customerStatus, Integer customerType) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		//获取客户状态集合
		List<Integer> statusList = new ArrayList<>();
		if(StringUtils.isNotBlank(customerStatus)) {
			String[] statusArr = customerStatus.split(",");
			for(String status : statusArr) {
				if(StringUtils.isNotBlank(status)) {
					statusList.add(Integer.valueOf(status));
				}
			}
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Customers> customerList = customersService.searchCustomers(searchCond, statusList, customerType);
		PageInfo<List<Customers>> pageInfo = new PageInfo(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "water_table";
	}

	


	/** 
	*	@Title: loadmodiwaterdialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodiwaterdialog")
	public String loadModiCustomerDialog(Long itemId, Model model) {
		//加载数据字典-用水性质
		List<DataDict> waterUseList = dataDictService.getListByTypeCode(DataDictType.WATER_USE);
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
					
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
										
		model.addAttribute("waterUseList", waterUseList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		//读取需要编辑的条目
		Customers currItem=customersService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		return TEMPLATE_PATH + "water_dialog_edit";
	}
	
	/**
	 * @Title: updateStatus
	 * @Description: 修改用水状态
	 * @param id
	 * @param enabled
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update-status", produces = "application/json")
	@ResponseBody
	public Object updateStatus(Long customerId ,Integer waterStatus) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			Customers beforeCustomer = customersService.selectByPrimaryKey(customerId);
			
			int rows = customersService.updateWaterStatus(customerId, waterStatus);
			if(rows>0) {
				//增加客户档案维护日志
				String customerName = customersService.getCustomerNameById(customerId);
				Customers cus = customersService.selectByPrimaryKey(customerId);
				CustomersTrace trace = new CustomersTrace();
				trace.setCustomerId(customerId);
				trace.setCustomerName(customerName);
				trace.setOperationTime(new Date());
				trace.setOperatorId(userBean.getId());
				if(waterStatus==1) {
					trace.setOperationType(EnumOperationType.WATER_NO.getValue());//停水
				} else {
					trace.setOperationType(EnumOperationType.WATER_YES.getValue());//复水
				}
				//HTML字符编码
				String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(beforeCustomer));
				String changeAfter = StringEscapeUtils.escapeHtml(JSON.toJSONString(cus));
				
				trace.setChangeBefore(changeBefore);
				trace.setChangeAfter(changeAfter);
				trace.setOperatorName(userBean.getRealname());
				traceService.insertSelective(trace);
				
				return RequestResultUtil.getResultUpdateSuccess();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultUpdateWarn();

	}

	
	

}