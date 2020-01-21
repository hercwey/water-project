package com.learnbind.ai.service.usersroles.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.dao.SysUsersRolesMapper;
import com.learnbind.ai.model.SysUsersRoles;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.usersroles.UsersRolesService;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     UserRefRoleServiceImpl.java
	* @Description:   用户角色 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class UsersRolesServiceImpl extends AbstractBaseService<SysUsersRoles, Long> implements  UsersRolesService {
	
	@Autowired
	public SysUsersRolesMapper sysUsersRolesMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public UsersRolesServiceImpl(SysUsersRolesMapper mapper) {
		this.sysUsersRolesMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertList
	 * @Description: 批量增加
	 * @param userId
	 * @param roleIds
	 * @return 
	 */
	@Override
	@Transactional
	public int insertList(Long userId, String  roleIds) {
		
		SysUsersRoles userRole = new SysUsersRoles();
		userRole.setUserId(userId);
		sysUsersRolesMapper.delete(userRole);//删除原来的用户-角色-关系
		
		String[] roleIdArr = roleIds.split(",");
		
		int rows = 0;
		for(String roleId : roleIdArr) {
			if(StringUtils.isNotBlank(roleId)) {
				userRole = new SysUsersRoles();
				userRole.setUserId(userId);
				userRole.setRoleId(Long.valueOf(roleId));
				rows = sysUsersRolesMapper.insertSelective(userRole);//增加新的角色授权
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
		}

		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getRolesByUserId
	 * @Description: 根据用户id获取角色
	 * @param userId
	 * @return 
	 * @see com.learnbind.ai.service.usersroles.UsersRolesService#getRolesByUserId(java.lang.Long)
	 */
	@Override
	public String getRolesByUserId(Long userId) {
		return sysUsersRolesMapper.getRolesByUserId(userId);
	}
}
