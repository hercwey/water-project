package com.learnbind.ai.controller.notify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumGroupIncludeFlag;
import com.learnbind.ai.common.enumclass.EnumNotifyUseLocation;
import com.learnbind.ai.common.enumclass.EnumWaterNotifyGroupType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.NotifyGroup;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.notify.NotifyGroupMeterService;
import com.learnbind.ai.service.notify.NotifyGroupService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.notify
 *
 * @Title: NotifyGroupController.java
 * @Description: 通知单规则
 *
 * @author Administrator
 * @date 2019年12月9日 上午10:16:17
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/water-notify-group")
public class NotifyGroupController {

	private static Log log = LogFactory.getLog(NotifyGroupController.class);
	private static final String TEMPLATE_PATH = "notify/group/"; // 页面目录
	private static final String TEMPLATE_INSERT_PATH = "notify/group/insert/";//页面目录
	private static final String TEMPLATE_GROUP_METER_PATH = "notify/group/groupmeter/";//页面目录
	private static final String TEMPLATE_SELECTED_METER_PATH = "notify/group/groupmeter/selectedmeter/";//页面目录
	private static final String TEMPLATE_UNSELECTED_METER_PATH = "notify/group/groupmeter/unselectedmeter/";//页面目录
	
	private static final int PAGE_SIZE = 8; // 页大小


	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	public CustomerMeterService customerMeterService;//客户-表计关系服务
	@Autowired
	private CustomersService customersService;// 客户
	@Autowired
	private NotifyGroupService notifyGroupService;//分组
	@Autowired
	private NotifyGroupMeterService notifyGroupMeterService;//分组-表计

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "notify_group_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		EnumGroupIncludeFlag[] groupIncludeFlagList = EnumGroupIncludeFlag.values();//查询分组标志
		model.addAttribute("groupIncludeFlagList", groupIncludeFlagList);
		
		return TEMPLATE_PATH + "notify_group_main";
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer includeFlag) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<NotifyGroup> groupList = notifyGroupService.searchCond(searchCond, includeFlag);
		PageInfo<List<NotifyGroup>> pageInfo = new PageInfo(groupList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> groupMapList = new ArrayList<>();
		for(NotifyGroup group : groupList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(group);
			Customers customer = customersService.selectByPrimaryKey(group.getCustomerId());
			if(customer!=null && StringUtils.isNotBlank(customer.getCustomerName())) {
				customerMap.put("customerName", customer.getCustomerName());
			}else {
				customerMap.put("customerName", null);
			}
			
			customerMap.put("includeFlagStr", EnumGroupIncludeFlag.getName(group.getIncludeFlag()));
			
			groupMapList.add(customerMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("groupMapList", groupMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "notify_group_table";
	}
	
	/**
	 * @Title: loadInsertNotifyGroupItem
	 * @Description: 加载增加/编辑分组页面
	 * @param model
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/load-insert-notify-group-item")
	public String loadInsertNotifyGroupItem(Model model, Long id) {
		
		EnumGroupIncludeFlag[] groupIncludeFlagList = EnumGroupIncludeFlag.values();//查询分组标志
		model.addAttribute("groupIncludeFlagList", groupIncludeFlagList);
		
		EnumNotifyUseLocation[] useLocationList = EnumNotifyUseLocation.values();//使用地理位置
		model.addAttribute("useLocationList", useLocationList);
		
		if(id!=null) {
			//读取需要编辑的条目
			NotifyGroup currItem = notifyGroupService.selectByPrimaryKey(id);
			Customers customer = customersService.selectByPrimaryKey(currItem.getCustomerId());
			model.addAttribute("customer", customer);
			model.addAttribute("currItem", currItem);
		}else {
			model.addAttribute("customer", null);
			model.addAttribute("currItem", null);
		}
		
		return TEMPLATE_INSERT_PATH + "insert_notify_group";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param meterBook
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(NotifyGroup group) {
		
		int rows = 0;
		group.setType(EnumWaterNotifyGroupType.BASE_CUSTOMER.getValue());
		rows = notifyGroupService.insertSelective(group);
		
		if(rows > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn("添加失败");
	}

	/**
	 * @Title: updae
	 * @Description: 修改
	 * @param group
	 * @return 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updae(NotifyGroup group) {
		
		group.setType(EnumWaterNotifyGroupType.BASE_CUSTOMER.getValue());
		int rows = notifyGroupService.updateByPrimaryKeySelective(group);
		
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		
		return RequestResultUtil.getResultUpdateWarn();
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				notifyGroupService.delete(id);
			}
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	//------------------------------------------------选择表计部分------------------------------------------------------------------------------------------
	
	
	@RequestMapping(value = "/load-group-meter-main")
	public String loadGroupMeterMain(Model model, Long groupId) {
		return TEMPLATE_GROUP_METER_PATH + "group_meter_main";
	}
	
	//-----------------------------已选择的表计-------------------------------------------------------------------------------------
	@RequestMapping(value = "/load-selected-meter-main")
	public String loadSelectedMeterMain(Model model, Long groupId) {
		
		NotifyGroup group = notifyGroupService.selectByPrimaryKey(groupId);
		String groupName = "";
		if(group!=null && StringUtils.isNotBlank(group.getName())) {
			groupName = group.getName();
		}
		model.addAttribute("groupName", groupName);
		
		return TEMPLATE_SELECTED_METER_PATH + "selected_meter_main";
	}
	
	/**
	 * @Title: loadSelectedMeterTable
	 * @Description: 加载已选择的表计列表
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param groupId
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/load-selected-meter-table")
	public String loadSelectedMeterTable(Model model, Integer pageNum, Integer pageSize, Long groupId, String searchCond) {
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = notifyGroupMeterService.getSelectedMeterList(groupId, searchCond);
		PageInfo<List<NotifyGroup>> pageInfo = new PageInfo(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		// 传递如下数据至前台页面
		model.addAttribute("selectedMeterList", meterList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_SELECTED_METER_PATH + "selected_meter_table";
	}
	
	
	/**
	 * @Title: unselectedMeter
	 * @Description: 移除已选择的表计（把表计从当前分组移除）
	 * @param groupId
	 * @param meterIds
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/unselected-meter", produces = "application/json")
	@ResponseBody
	public Object unselectedMeter(Long groupId, String meterIds) throws Exception {
		try {
			
			if(StringUtils.isBlank(meterIds)) {
				return RequestResultUtil.getResultFail("请选择表计！");
			}
			
			List<Long> meterIdList = JSON.parseArray(meterIds, Long.class);
			int rows = notifyGroupMeterService.unselectedMeter(groupId, meterIdList);
			if(rows>0) {
				return RequestResultUtil.getResultSuccess("移除成功！");
			}
			return RequestResultUtil.getResultSuccess("移除失败，请重新操作！");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("移除异常！");

	}
	
	//-----------------------------未选择的表计-------------------------------------------------------------------------------------
	
	@RequestMapping(value = "/load-unselected-meter-main")
	public String loadUnselectedMeterMain(Model model, Long groupId) {
		return TEMPLATE_UNSELECTED_METER_PATH + "unselected_meter_main";
	}
	@RequestMapping(value = "/load-unselected-meter-table")
	public String loadUnselectedMeterTable(Model model, Integer pageNum, Integer pageSize, Long customerId, Integer includeFlag, String searchCond) {
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = notifyGroupMeterService.getUnselectedMeterList(customerId, includeFlag, searchCond);
		PageInfo<List<NotifyGroup>> pageInfo = new PageInfo(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		// 传递如下数据至前台页面
		model.addAttribute("unselectedMeterList", meterList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_UNSELECTED_METER_PATH + "unselected_meter_table";
	}
	
	/**
	 * @Title: selectedMeter
	 * @Description: 添加已选择的表计（把表计从当前分组添加到已选择表计列表）
	 * @param groupId
	 * @param meterIds
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/selected-meter", produces = "application/json")
	@ResponseBody
	public Object selectedMeter(Long groupId, String meterIds) throws Exception {
		try {
			
			if(StringUtils.isBlank(meterIds)) {
				return RequestResultUtil.getResultFail("请选择表计！");
			}
			
			List<Long> meterIdList = JSON.parseArray(meterIds, Long.class);
			int rows = notifyGroupMeterService.selectedMeter(groupId, meterIdList);
			if(rows>0) {
				return RequestResultUtil.getResultSuccess("添加成功！");
			}
			return RequestResultUtil.getResultSuccess("添加失败，请重新操作！");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("添加异常！");

	}
	

}