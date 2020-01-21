package com.learnbind.ai.controller.component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.WaterNotify;
import com.learnbind.ai.model.WaterNotifyDetail;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.notify.WaterNotifyDetailService;
import com.learnbind.ai.service.notify.WaterNotifyService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: SelectCustomerBillController.java
 * @Description: 选择客户通知单
 *
 * @author Administrator
 * @date 2019年12月1日 下午4:29:17
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/select-customer-notify")
public class SelectCustomerNotifyController {
	
	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(SelectCustomerNotifyController.class);
	
	/**
	 * @Fields TEMPLATE_PATH：页面目录
	 */
	private static final String TEMPLATE_PATH = "customers/invoice/notify/"; // 页面目录
	private static final String NOTIFY_DETAIL_TEMPLATE_PATH = "notify/print_template/";//打印时用到的模版路径
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
	
	@Autowired
	private WaterNotifyService waterNotifyService;//通知单服务
	@Autowired
	private WaterNotifyDetailService waterNotifyDetailService;//通知单明细
	
	/**
	 * @Title: table
	 * @Description: 客户通知单列表
	 * @param model
	 * @param parmsJSON
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String parmsJSON) {
		
		JSONObject parmsObj = JSON.parseObject(parmsJSON);
		String pageNumStr = parmsObj.getString("pageNum");
		String pageSizeStr = parmsObj.getString("pageSize");
		String period = parmsObj.getString("period");
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
		List<WaterNotify> notifyList = waterNotifyService.getList(customerId, period, null);
		//PageInfo<WaterNotify> pageInfo = new PageInfo<>(notifyList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> notifyMapList = new ArrayList<>();
		for(WaterNotify notify : notifyList) {
			
			Map<String, Object> notifyMap = EntityUtils.entityToMap(notify);
			
			WaterNotifyDetail detail = new WaterNotifyDetail();
			detail.setWaterNotifyId(notify.getId());
			List<WaterNotifyDetail> detailList = waterNotifyDetailService.select(detail);
			String detailStr = "";
			List<Map<String, Object>> detailMapList = new ArrayList<>();
			List<Long> partWaterIdList = new ArrayList<>();
			for(WaterNotifyDetail detailTemp : detailList) {
				String partWaterIds = detailTemp.getPartWaterId();
				List<Long> pwIdList = JSON.parseArray(partWaterIds, Long.class);
				for(Long pwId : pwIdList) {
					partWaterIdList.add(pwId);
				}
			}
			
			//获取通知单账单明细
			List<Map<String, Object>> invoiceBillMapList = new ArrayList<>();//开票账单集合
			if(partWaterIdList!=null && partWaterIdList.size()>0) {
				Example example = new Example(PartitionWater.class);
				example.createCriteria().andIn("id", partWaterIdList);
				List<PartitionWater> pwList = partitionWaterService.selectByExample(example);
				for(PartitionWater pw : pwList) {
					Long accountItemId = pw.getAccountItemId();//账单ID
					if(accountItemId==null) {
						accountItemId = 0l;
					}
					BigDecimal basePrice = pw.getBasePrice();//基础水价
					BigDecimal sewagePrice = pw.getTreatmentFee();//污水处理费单价
					BigDecimal totalPrice = pw.getWaterPrice();//综合水价
					BigDecimal realWaterAmount = pw.getRealWaterAmount();//水量
					BigDecimal baseWaterFee = BigDecimalUtils.multiply(basePrice, realWaterAmount);//基础水费
					BigDecimal sewageWaterFee = BigDecimalUtils.multiply(sewagePrice, realWaterAmount);//污水处理费水费
					BigDecimal totalWaterFee = pw.getWaterFee();//合计水费
					
					Map<String, Object> invoiceBillMap = new HashMap<>();
					invoiceBillMap.put("accountItemId", accountItemId);
					invoiceBillMap.put("basePrice", basePrice);
					invoiceBillMap.put("sewagePrice", sewagePrice);
					invoiceBillMap.put("totalPrice", totalPrice);
					invoiceBillMap.put("realWaterAmount", realWaterAmount);
					invoiceBillMap.put("baseWaterFee", baseWaterFee);
					invoiceBillMap.put("sewageWaterFee", sewageWaterFee);
					invoiceBillMap.put("totalWaterFee", totalWaterFee);
					invoiceBillMapList.add(invoiceBillMap);
					
					detailStr += basePrice+":"+sewagePrice+":"+totalPrice+":"+realWaterAmount+":"+baseWaterFee+":"+sewageWaterFee+":"+totalWaterFee+"<br/>";
				}
			}
			
			//Map<String, Object> notifyMap = new HashMap<>();//通知单
			notifyMap.put("detail", detailStr);
			
			//通知单结果对象，用于前端用户选择
			Map<String, Object> notifyResMap = new HashMap<>();
			notifyResMap.put("notifyId", notify.getId());
			notifyResMap.put("notifyBillList", invoiceBillMapList);
			//notifyResMap.put("notifyBillListJson", JSON.toJSONString(detailMapList));
			
			notifyMap.put("notifyResMap", JSON.toJSONString(notifyResMap));
			
			notifyMapList.add(notifyMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("notifyList", notifyMapList);  //列表数据
		
		//分页数据
		//model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("dialogId", dialogId);
		
		return TEMPLATE_PATH + "customer_notify_table";
	}
	
	@RequestMapping(value = "/detail")
	public String detail(Model model, Long notifyId) {
		
		Map<String, Object> notifyMap = new HashMap<>();
		try {
			notifyMap = waterNotifyService.getWaterNotifyMap(notifyId, null, null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//查询条件回传
		model.addAttribute("textData", notifyMap);
		
		return TEMPLATE_PATH+"A4Template";
	}
	
}