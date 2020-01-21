package com.learnbind.ai.controller.customers;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersController.java
 * @Description: 客户档案
 *
 * @author Thinkpad
 * @date 2019年5月16日 上午11:42:14
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-detail")
public class CustomerDetailController {
	private static Log log = LogFactory.getLog(CustomerDetailController.class);
	private static final String TEMPLATE_PATH = "customers/detail/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private DiscountService discountService;  //政策减免
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private LocationCustomerService locationCustomerService;
	@Autowired
	private LocationService locationService;
	

	@RequestMapping(value = "/tabs")
	public String tabs(Model model) {
		return TEMPLATE_PATH + "detail_tabs";
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
		return TEMPLATE_PATH + "detail_main";
	}

	
	
	@RequestMapping(value = "/table")
	public String detail(Model model, Long id) {
		
		Map<String, Object> customerMap = null;
		if(id!=null && id>0) {
			Customers customer = customersService.selectByPrimaryKey(id);
			
			customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("settleTimeStr", customer.getSettleTimeStr());//立户时间
			customerMap.put("idTypeStr", customer.getIdTypeStr());//证件类型
			customerMap.put("customerTypeStr", customer.getCustomerTypeStr());//客户类型
			customerMap.put("waterStatusStr", customer.getWaterStatusStr());//用水状态
			customerMap.put("notifyModeStr", customer.getNotifyModeStr());//通知方式
			customerMap.put("deductTypeStr", customer.getDeductTypeStr());//扣费方式
			
			customerMap.put("industryValue", this.getDataDictValue(EnumDataDictType.CUSTOMER_TRADE_TYPE.getCode(), customer.getIndustry()));//行业性质
			customerMap.put("discountName", this.getDiscountName(customer.getDiscountType()));//政策减免名称
			customerMap.put("waterUseValue", this.getDataDictValue(EnumDataDictType.WATER_USE.getCode(), customer.getWaterUse()));//水价名称
			
			
		}
		
		model.addAttribute("customer", customerMap);
		
		return TEMPLATE_PATH + "detail_table";
	}
	
	/**
	 * @Title: loadCustomerTable
	 * @Description: 加载客户详情
	 * @param model
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-customer-detail-table")
	public String loadCustomerTable(Model model, Long customerId) {
		
		Map<String, Object> customerMap = null;
		Customers customer = customersService.selectByPrimaryKey(customerId);
		
		customerMap = EntityUtils.entityToMap(customer);
		customerMap.put("settleTimeStr", customer.getSettleTimeStr());//立户时间
		customerMap.put("idTypeStr", customer.getIdTypeStr());//证件类型
		customerMap.put("customerTypeStr", customer.getCustomerTypeStr());//客户类型
		customerMap.put("waterStatusStr", customer.getWaterStatusStr());//用水状态
		customerMap.put("notifyModeStr", customer.getNotifyModeStr());//通知方式
		customerMap.put("deductTypeStr", customer.getDeductTypeStr());//扣费方式
		customerMap.put("isPartWaterStr", customer.getIsPartWaterStr());//是否分水量
		
		customerMap.put("industryValue", this.getDataDictValue(EnumDataDictType.CUSTOMER_TRADE_TYPE.getCode(), customer.getIndustry()));//行业性质
		customerMap.put("discountName", this.getDiscountName(customer.getDiscountType()));//政策减免名称
		customerMap.put("waterUseValue", this.getDataDictValue(EnumDataDictType.WATER_USE.getCode(), customer.getWaterUse()));//水价分类
		String waterPriceName = "";
		if(customer.getPriceCode() != null) {
			SysWaterPrice waterPrice = waterPriceService.getWaterPriceByPriceCode(customer.getPriceCode());
			waterPriceName = waterPrice.getLadderName();
		}
		
		customerMap.put("waterPriceName", waterPriceName);//水价
		
		Example exampleLocation = new Example(LocationCustomer.class);
		exampleLocation.createCriteria().andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> locationList = locationCustomerService.selectByExample(exampleLocation);
		String locationName = "";
		if(locationList.size() > 0) {//判断客户是否绑定地理位置
			for(LocationCustomer temp : locationList) {
				String place = locationService.getPlace(temp.getTraceIds());//查询地理位置全称
				locationName = place+"；"+locationName;
			}
		}
		
		model.addAttribute("locationName",locationName);
		model.addAttribute("customer", customerMap);
			
		//判断客户类型是个人客户还是单位客户
		if(customer.getCustomerType()==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {
			return TEMPLATE_PATH + "edit_person_customer_table";
		}
		
		return TEMPLATE_PATH + "edit_company_customer_table";
		
	}
	
	/**
	 * @Title: getDiscountName
	 * @Description: 获取政策减免名称
	 * @param discountId
	 * @return 
	 */
	private String getPriceTypeName(String priceTypeCode) {
		String priceTypeName = waterPriceService.getPriceTypeName(priceTypeCode);
		if(priceTypeName!=null) {
			return priceTypeName;
		}
		return null;
	}
	
	/**
	 * @Title: getDiscountName
	 * @Description: 获取水价类型
	 * @param discountId
	 * @return 
	 */
	private String getDiscountName(Long discountId) {
		SysDiscount record = discountService.selectByPrimaryKey(discountId);
		if(record!=null) {
			return record.getName();
		}
		return null;
	}

	/**
	 * @Title: getDataDictValue
	 * @Description: 根据数据字典类型编码和字典编码查询
	 * @param typeCode
	 * @param key
	 * @return 
	 */
	private String getDataDictValue(String typeCode, String key) {
		
		if(StringUtils.isBlank(typeCode) && StringUtils.isBlank(key)) {
			return null;
		}
		
		DataDict dict = new DataDict();
		if(StringUtils.isNotBlank(typeCode)) {
			dict.setTypeCode(typeCode);
		}
		dict.setKey(key);
		List<DataDict> dictList = dataDictService.select(dict);
		if(dictList!=null && dictList.size()>0) {
			dict = dictList.get(0);
		}
		
		if(dict!=null) {
			return dict.getValue();
		}
		return null;
	}


}