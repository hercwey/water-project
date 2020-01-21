package com.learnbind.ai.controller.notify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.notify.NotifyGroupService;
import com.learnbind.ai.service.notify.WaterNotifyService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.notify
 *
 * @Title: CreateWaterNotifyController.java
 * @Description: 生成水费通知单
 *
 * @author Thinkpad
 * @date 2019年12月8日 下午4:46:52
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/create-water-notify")
public class CreateWaterNotifyController {

	private static Log log = LogFactory.getLog(CreateWaterNotifyController.class);
	private static final String TEMPLATE_PATH = "notify/create_notify/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小


	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系表
	@Autowired
	private CustomersService customersService;// 客户
	@Autowired
	private NotifyGroupService notifyGroupService;// 水费通知单分组
	@Autowired
	private CustomerAccountItemService customerAccountItemService;// 客户账单
	@Autowired
	private WaterNotifyService waterNotifyService;// 水费通知单实体

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "notify_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "notify_main";
	}
	

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond,String period) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
		List<Customers> customersList = customersService.getNotifyCustomerList(traceIds, searchCond, customerType, period);
		
		PageInfo<List<Customers>> pageInfo = new PageInfo(customersList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Customers customer : customersList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			String traceId = locationCustomerService.getTraceIds(customer.getId());
			String customerPlace = locationService.getPlace(traceId);
			
			// 水费欠费金额
			BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
			// 查询往期欠费
			BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
			// 查询客户余额
			BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
			// 查询本月水费欠费金额（本期欠费+往期欠费）
			BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
			
			// 本期欠费
			customerMap.put("oweAmount", oweAmount);
			// 往期欠费
			customerMap.put("pastOweAmount", pastOweAmount);
			// 应缴水费
			customerMap.put("waterFeeOweAmount", waterFeeOweAmount);
			customerMap.put("customerPlace", customerPlace);
			customerMap.put("period", period);
			customerMapList.add(customerMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("customerMapList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "notify_table";
	}
	
	
	/**
	 * @Title: generateNotifySingle
	 * @Description: 单个生成水费通知单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	@RequestMapping(value = "/generateNotifySingle", produces = "application/json")
	@ResponseBody
	public Object generateNotifySingle(Long customerId, String period) {
		try {
			int rows = waterNotifyService.generateNotifySingle(customerId, period);
			if(rows>0) {
				return RequestResultUtil.getResultSuccess("生成通知单成功！");
			}else if(rows==-1) {
				return RequestResultUtil.getResultSuccess("通知单已生成！");
			}
			else if(rows==-3) {
				return RequestResultUtil.getResultSuccess("未生成账单无法生成通知单！");
			}
			return RequestResultUtil.getResultFail("生成通知单失败！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("生成通知单异常！");
	}
	
	/**
	 * @Title: generateNotifyBatch
	 * @Description: 批量生成通知单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param customerIdStr
	 * @return 
	 */
	@RequestMapping(value = "/generateNotifyBatch", produces = "application/json")
	@ResponseBody
	public Object generateNotifyBatch(String searchCond, String traceIds,  String period,String customerIdStr) {
		int rows = waterNotifyService.generateNotifyBatch(searchCond, traceIds, period, customerIdStr);
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess("生成通知单成功！");
		}
		return RequestResultUtil.getResultUpdateWarn("生成通知单失败！");
	}
	
}