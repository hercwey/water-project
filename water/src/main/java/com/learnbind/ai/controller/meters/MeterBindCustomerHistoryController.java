package com.learnbind.ai.controller.meters;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterBindCustomerHistoryController.java
 * @Description: 表计档案-客户历史
 *
 * @author Thinkpad
 * @date 2019年10月17日 下午1:37:12
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-customer-history")
public class MeterBindCustomerHistoryController {
	private static Log log = LogFactory.getLog(MeterBindCustomerHistoryController.class);
	private static final String TEMPLATE_PATH = "meters/customer_history/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户信息
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计信息
	@Autowired
	private DataDictService dataDictService;//数据字典
	@Autowired
	private WaterPriceService waterPriceService;  //水价
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关联表
	@Autowired
	private DiscountService discountService;//政策减免
	/**
	 * @Title: main
	 * @Description: 客户绑定表计历史
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/meter-customer-main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "customer_history_main";
	}

	/**
	 * @Title: table
	 * @Description: 客户绑定的表计历史
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param customerId
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model,Integer pageNum, Integer pageSize, Long meterId, String searchCond) {
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerMeter> customerList = customerMeterService.getCustomerHistoryMessage(meterId, searchCond);
		PageInfo<List<CustomerMeter>> pageInfo = new PageInfo(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(CustomerMeter temp : customerList) {
			Customers customer = customersService.selectByPrimaryKey(temp.getCustomerId());
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("customerTypeStr", customer.getCustomerTypeStr());//客户类型
			customerMap.put("settleTimeStr", customer.getSettleTimeStr());//立户时间
			customerMap.put("bindTimeStr", temp.getBindTimeStr());//绑定时间
			customerMap.put("unBindTimeStr", temp.getUnBindTimeStr());//解绑时间
			customerMap.put("statusStr", customer.getStatusStr());//客户状态
			customerMap.put("customerId", temp.getCustomerId());
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "customer_history_table";
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
	
	
	/**
	 * @Title: loadModiBankDialog
	 * @Description: 修改客户银行信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-customer-history-dialog")
	public String loadModiBankDialog(Long itemId, Model model) {
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//查询用水性质
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);

		//读取需要编辑的条目
		Customers currItem=customersService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("priceTypeCode", currItem.getWaterUse()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
		
		Location location = this.getLocationByCustomerId(itemId);//查询地理位置
		String place = "";
		if(location != null) {
			place = locationService.getPlace(location.getTraceIds());//查询地理位置全称
		}
		model.addAttribute("location",location);
		model.addAttribute("waterPriceList",waterPriceList);
		
		model.addAttribute("place",place);
		
		return TEMPLATE_PATH + "customer_history_dialog";
	}
	
	/**
	 * @Title: getLocationByMeterId
	 * @Description: 根据表计ID查询绑定的地理位置
	 * @param customerId
	 * @return 
	 */
	private Location getLocationByCustomerId(Long customerId) {
		List<Long> locationIdList = locationCustomerService.getLocationIdListByCustId(customerId);
		if(locationIdList!=null && locationIdList.size()>0) {
			Long locationId = locationIdList.get(0);
			Location location = locationService.selectByPrimaryKey(locationId);
			return location;
		}
		return null;
	}

}