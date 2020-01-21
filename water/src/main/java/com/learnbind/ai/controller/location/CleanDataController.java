package com.learnbind.ai.controller.location;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.statistic.CleanDataTempService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.login
 *
 * @Title: CleanDataController.java
 * @Description: 数据清理
 *
 * @author Thinkpad
 * @date 2019年10月1日 上午10:45:57
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/clean-data")
public class CleanDataController {
	private static Log log = LogFactory.getLog(CleanDataController.class);
	private static final String TEMPLATE_PATH = "location/clean_data/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MetersService metersService;  //客户档案
	@Autowired
	private LocationMeterService locationMeterService;  //地理位置-表计关系表
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计关联表
	
	@Autowired
	private CleanDataTempService cleanDataTempService;  //清理数据
	
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "clean_data_starter";
	}
	
	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "clean_data_main";
	}
	
	
	/**
	 * @Title: batchCleanData
	 * @Description: 批量清理数据
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/batch-clean-data", produces = "application/json")
	@ResponseBody
	public Object batchCleanData(String traceIds) {

		try {
			//查询该地理位置节点下的所有客户信息；
			int result = 0;
			List<Customers> customersList = customersService.getCustomerData(traceIds);
			List<Meters> metersList = metersService.getMeterData(traceIds);
			int rows = cleanDataTempService.batchCleanCustomerData(customersList);
			result = cleanDataTempService.batchCleanMeterData(metersList, traceIds);
			
			log.debug("成功清除客户数据 "+rows);
			log.debug("成功清除表计数据 "+result);
			return RequestResultUtil.getResultFail("成功清除客户数据 "+rows+"；"+"成功清除表计数据 "+result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("操作异常！");
	}
	
	
}