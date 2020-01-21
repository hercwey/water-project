package com.learnbind.ai.controller.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: SelectCustomerBillController.java
 * @Description: 选择客户账单
 *
 * @author Administrator
 * @date 2019年12月1日 下午4:29:17
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/select-customer-bill")
public class SelectCustomerBillController {
	
	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(SelectCustomerBillController.class);
	
	/**
	 * @Fields TEMPLATE_PATH：页面目录
	 */
	private static final String TEMPLATE_PATH = "customers/invoice/bill/"; // 页面目录
	/**
	 * @Fields PAGE_SIZE：页大小
	 */
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	private CustomerAccountItemService customerAccountItemService;  //客户信息
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量
	@Autowired
	private WaterPriceService waterPriceService;//水价
	
	/**
	 * @Title: table
	 * @Description: 银行客户信息table
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
	public String table(Model model, String parmsJSON) {
		
		JSONObject parmsObj = JSON.parseObject(parmsJSON);
		String pageNumStr = parmsObj.getString("pageNum");
		String pageSizeStr = parmsObj.getString("pageSize");
		String periodStart = parmsObj.getString("periodStart");
		String periodEnd = parmsObj.getString("periodEnd");
		Boolean searchWaterFeeBill = parmsObj.getBoolean("searchWaterFeeBill");
		Boolean searchStoredBill = parmsObj.getBoolean("searchStoredBill");
		String dialogId = parmsObj.getString("dialogId");
		String customerIdStr = parmsObj.getString("customerId");
		//Integer settlementStatus = parmsObj.getInteger("settlementStatus");
		
		int pageNum = 0;
		int pageSize = PAGE_SIZE;
		if(StringUtils.isNotBlank(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		if(StringUtils.isNotBlank(pageSizeStr)) {
			pageSize = Integer.valueOf(pageSizeStr);
		}
		// 判定页码有效性
		if (pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		Long customerId = 0l;
		if(StringUtils.isNotBlank(customerIdStr)) {
			customerId = Long.valueOf(customerIdStr);
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<CustomerAccountItem> billList = customerAccountItemService.getCustomerAccountItemList(null, customerId, null);
		List<CustomerAccountItem> billList = customerAccountItemService.getTaxInvoiceBillList(periodStart, periodEnd, customerId, null, searchWaterFeeBill, searchStoredBill);
		PageInfo<CustomerAccountItem> pageInfo = new PageInfo<>(billList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> billMapList = new ArrayList<>();
		for(CustomerAccountItem bill : billList) {
			
			Map<String, Object> billMap = EntityUtils.entityToMap(bill);
			
			if(bill.getDebitCredit().equalsIgnoreCase(EnumAiDebitCreditStatus.CREDIT.getKey())) {//水费账单
				PartitionWater pw = partitionWaterService.getPartitionWater(bill.getId());//获取分水量信息
				if(pw!=null) {
					SysWaterPrice waterPrice = waterPriceService.getWaterPriceByPriceCode(pw.getWaterUse());//获取水价信息
					
					billMap.put("waterUse", waterPrice.getLadderName());
					billMap.put("basePrice", waterPrice.getBasePrice());
					billMap.put("sewagePrice", waterPrice.getTreatmentFee());
					billMap.put("totalPrice", waterPrice.getTotalPrice());
					billMap.put("waterAmount", pw.getRealWaterAmount());
					billMap.put("totalWaterFee", pw.getWaterFee());
				}else {
					
					billMap.put("waterUse", null);
					billMap.put("basePrice", 0);
					billMap.put("sewagePrice", 0);
					billMap.put("totalPrice", 0);
					billMap.put("waterAmount", 0);
					billMap.put("totalWaterFee", 0);
				}
			}else {//充值账单
				billMap.put("waterUse", null);
				billMap.put("basePrice", 0);
				billMap.put("sewagePrice", 0);
				billMap.put("totalPrice", 0);
				billMap.put("waterAmount", 0);
				billMap.put("totalWaterFee", bill.getDebitAmount());
			}
			billMapList.add(billMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("billList", billMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("dialogId", dialogId);
		
		return TEMPLATE_PATH + "customer_bill_table";
	}
	
}