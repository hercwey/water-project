package com.learnbind.ai.controller.location;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.location
 *
 * @Title: LocationCustomerController.java
 * @Description: 地理位置-客户档案关系
 *
 * @author Thinkpad
 * @date 2019年5月23日 下午6:29:59
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/location-customer")
public class LocationCustomerController {
	
	private static Log log = LogFactory.getLog(LocationCustomerController.class);
	
	private static final String TEMPLATE_PATH = "location/location_customer/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private DataDictService dataDictService;//数据字典服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	private DiscountService discountService;  //政策减免
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置与客户关系服务

	/**
	 * @Title: starter
	 * @Description: 起始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "location_customer_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("locaNodeType", dictList);
		//加载数据字典-用水性质、行业性质
		List<DataDict> waterUseList = dataDictService.getListByTypeCode(DataDictType.WATER_USE);
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
						
		model.addAttribute("waterUseList", waterUseList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
//		Example example = new Example(Location.class);
//		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//		example.setOrderByClause("location_level ASC,sort_value ASC,pid ASC,id ASC");
//		List<Location> locationList = locationService.selectByExample(example);
//		
//		//String locationListJson = JSON.toJSONString(locationList);
//		model.addAttribute("locationListJson", locationList);
		
		return TEMPLATE_PATH + "location_customer_main";
	}
	
	/**
	 * @Title: locationTable
	 * @Description: 加载地理位置
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/location-table")
	public String locationTable(Model model) {

//		//查询地理位置节点类型数据字典
//		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
//		model.addAttribute("dictList", dictList);
//		
//		Example example = new Example(Location.class);
//		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//		example.setOrderByClause(" LOCATION_LEVEL ASC,SORT_VALUE ASC,PID ASC,CODE ASC, NAME ASC, ID ASC");
//		List<Location> locationList = locationService.selectByExample(example);
//		
//		//String locationListJson = JSON.toJSONString(locationList);
//		model.addAttribute("locationListJson", locationList);
//		
//		List<Location> resultList = new ArrayList<>();//保存排序结果
//		resultList.clear();
//		for(int i=0; i<locationList.size(); i++){
//			Location location = locationList.get(i);
//			if(location.getPid()==0){
//				resultList.add(location);
//				locationListSort(resultList, locationList,location.getId());
//			}
//		}
//		model.addAttribute("locationList", resultList);
		
		return TEMPLATE_PATH + "location_table";
	}
	
	/**
	 * @Title: metersMain
	 * @Description: 表计主页
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/customer-main")
	public String metersMain(Model model) {
		
		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("locaNodeType", dictList);
		//加载数据字典-用水性质、行业性质
		List<DataDict> waterUseList = dataDictService.getListByTypeCode(DataDictType.WATER_USE);
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
								
		model.addAttribute("waterUseList", waterUseList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
		return TEMPLATE_PATH + "customer_main";
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
	@RequestMapping(value = "/customer-table")
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
		List<Customers> customersList = customersService.searchCustomers(searchCond, statusList, customerType);
		PageInfo<List<Customers>> pageInfo = new PageInfo(customersList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("customersList", customersList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "customer_table";
	}
	
	/**
	 * @Title: loadLocationDetailDialog
	 * @Description: 加载地理位置详情对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-location-detail-dialog")
	public String loadLocationDetailDialog(Model model) {
		
		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("dictList", dictList);
		
		Example example = new Example(Location.class);
		example.setOrderByClause("location_level ASC,sort_value ASC,pid ASC,id ASC");
		List<Location> locationList = locationService.selectByExample(example);
		
		//String locationListJson = JSON.toJSONString(locationList);
		//model.addAttribute("locationListJson", locationList);
		
		List<Location> resultList = new ArrayList<>();//保存排序结果
		resultList.clear();
		for(int i=0; i<locationList.size(); i++){
			Location location = locationList.get(i);
			if(location.getPid()==0){
				resultList.add(location);
				locationListSort(resultList, locationList,location.getId());
			}
		}
		model.addAttribute("locationList", resultList);
		
		return TEMPLATE_PATH + "location_dialog_edit";
	}
	
	/**
	 * @Title: loadCustomerDetailDialog
	 * @Description: 加载客户详情对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-customer-detail-dialog")
	public String loadDetailDialog(Long itemId, Model model) {
		//读取需要编辑的条目
		Customers currItem=customersService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "customer_dialog_edit";
	}

	/**
	 * @Title: locationListSort
	 * @Description: 对地理位置集合排序
	 * @param resultList
	 * @param locationList
	 * @param id 
	 */
	private void locationListSort(List<Location> resultList, List<Location> locationList,Long id) {
		
		for(int i=0; i<locationList.size(); i++){
			Location right = locationList.get(i);
        	if(right.getPid().equals(id)){
                resultList.add(right);
                locationListSort(resultList, locationList,right.getId());
            }
        }
    }
	
	
	/**
	 * @Title: bind
	 * @Description: 建立地理位置与客户的绑定关系
	 * @param request
	 * @param locationId
	 * @param customerIds
	 * @return 
	 */
	@RequestMapping(value = "/bind", produces = "application/json")
	@ResponseBody
	public Object bind(HttpServletRequest request, Long locationId, String customerIds){
		
		Location location = locationService.selectByPrimaryKey(locationId);
		
		String customerMsg = "";
		boolean isBinding = false;
		
		String[] customerIdArr = customerIds.split(",");
		customerMsg = this.isBinding(customerIdArr);
		if(StringUtils.isNotBlank(customerMsg)) {
			isBinding = true;
		}
		
		if(isBinding) {
			//return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+customerMsg+"】已绑定！");
			return RequestResultUtil.getResultSaveWarn("【"+customerMsg+"】已绑定！");
		}

		customerMsg = this.getCustomerMsg(customerIdArr);
		
		int row = locationCustomerService.TransactionCheckBind(locationId, location.getTraceIds(), customerIdArr);
		
		if (row>0){
			return RequestResultUtil.getResultSaveSuccess("【"+location.getName()+"】与【"+customerMsg+"】绑定成功！");
		}else{
			return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+customerMsg+"】绑定失败！请重新操作！");
		}
	}
	
	/**
	 * @Title: isBinding
	 * @Description: 验证客户与地理位置是否绑定
	 * @param customerMsg
	 * @param isBinding
	 * @param customerIdArr 
	 */
	private String isBinding(Long locationId, String[] customerIdArr) {
		String customerMsg = "";
		for(String custIdStr : customerIdArr) {
			
			if(StringUtils.isNotBlank(custIdStr)) {
				Long customerId = Long.valueOf(custIdStr);
				Customers customer = customersService.selectByPrimaryKey(customerId);

				boolean flag = locationCustomerService.isBinding(locationId, customerId);
				if(flag) {
					customerMsg = customerMsg+" "+customer.getCustomerCode();
				}
			}
		}
		return customerMsg;
	}
	
	/**
	 * @Title: isBinding
	 * @Description: 验证客户是否绑定
	 * @param customerMsg
	 * @param isBinding
	 * @param customerIdArr 
	 */
	private String isBinding(String[] customerIdArr) {
		String customerMsg = "";
		for(String custIdStr : customerIdArr) {
			
			if(StringUtils.isNotBlank(custIdStr)) {
				Long customerId = Long.valueOf(custIdStr);
				Customers customer = customersService.selectByPrimaryKey(customerId);

				boolean flag = locationCustomerService.isBinding(customerId);
				if(flag) {
					customerMsg = customerMsg+" "+customer.getCustomerCode();
				}
			}
		}
		return customerMsg;
	}
	
	/**
	 * @Title: getCustomerMsg
	 * @Description: 获取客户提示信息
	 * @param customerIdArr
	 * @return 
	 */
	private String getCustomerMsg(String[] customerIdArr) {
		String customerMsg = "";
		for(String custIdStr : customerIdArr) {
			
			if(StringUtils.isNotBlank(custIdStr)) {
				Long customerId = Long.valueOf(custIdStr);
				Customers customer = customersService.selectByPrimaryKey(customerId);
				customerMsg = customerMsg+" "+customer.getCustomerCode();
			}
		}
		return customerMsg;
	}
	
	/**
	 * @Title: unbind
	 * @Description: 解除地理位置与客户的绑定关系
	 * @param request
	 * @param locationId
	 * @param customerIds
	 * @return 
	 */
	@RequestMapping(value = "/unbind", produces = "application/json")
	@ResponseBody
	public Object unbind(HttpServletRequest request, Long locationId, String customerIds){
		
		Location location = locationService.selectByPrimaryKey(locationId);
		 
		String customerMsg = "";
		boolean isBinding = true;
		
		String[] customerIdArr = customerIds.split(",");
		customerMsg = this.isBinding(locationId, customerIdArr);
		if(StringUtils.isNotBlank(customerMsg)) {
			isBinding = false;
		}
		
		if(isBinding) {
			return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+customerMsg+"】未绑定！不能解除绑定");
		}
		
		
		customerMsg = this.getCustomerMsg(customerIdArr);
		
		int row = locationCustomerService.TransactionCheckUnBind(locationId, customerIdArr);
		
		if (row>0){
			return RequestResultUtil.getResultSaveSuccess("【"+location.getName()+"】与【"+customerMsg+"】解绑成功！");
		}else{
			return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+customerMsg+"】解绑失败！请重新操作！");
		}
	}

}