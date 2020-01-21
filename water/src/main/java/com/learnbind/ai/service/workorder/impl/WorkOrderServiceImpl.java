package com.learnbind.ai.service.workorder.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.dao.WorkOrderMapper;
import com.learnbind.ai.model.WorkOrder;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.usersroles.UsersRolesService;
import com.learnbind.ai.service.workorder.WorkOrderService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.workorder.impl
 *
 * @Title: WorkOrderServiceImpl.java
 * @Description: 工单
 *
 * @author Thinkpad
 * @date 2019年7月20日 下午3:36:09
 * @version V1.0 
 *
 */
@Service
public class WorkOrderServiceImpl extends AbstractBaseService<WorkOrder, Long> implements  WorkOrderService {
	
	@Autowired
	public WorkOrderMapper workOrderMapper;
	@Autowired
	public UsersRolesService usersRolesService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WorkOrderServiceImpl(WorkOrderMapper  mapper) {
		this.workOrderMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<WorkOrder> searchWorkOrder(String searchCond, Integer status) {
//		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Long userId = userBean.getId();
//		String operator = userBean.getRealname();
//		String roleName = usersRolesService.getRolesByUserId(userId);
//		
//		if(StringUtils.equals("售后人员", roleName)) {
//			return workOrderMapper.searchWorkOrderSale(searchCond, userId, status);
//		} else {
			return workOrderMapper.searchWorkOrder(searchCond, status);
//		}
		
	}
	
	@Override
	public List<WorkOrder> searchWorkOrderSale(String searchCond, Long userId, Integer status) {
			return workOrderMapper.searchWorkOrderSale(searchCond, userId, status);
//		}
		
	}
	
	@Override
	public List<Map<String, Object>> getListAll() {
		return workOrderMapper.getListAll();
	}
	

}
