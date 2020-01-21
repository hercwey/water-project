package com.learnbind.ai.service.role.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.SysRolesMapper;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.model.SysUsersRoles;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.role.RolesService;
import com.learnbind.ai.service.rolesrights.RolesRightsService;
import com.learnbind.ai.service.usersroles.UsersRolesService;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     RoleServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class RolesServiceImpl extends AbstractBaseService<SysRoles, Long> implements  RolesService {
	
	@Autowired
	public SysRolesMapper sysRolesMapper;
	@Autowired
	public UsersRolesService usersRolesService;
	@Autowired
	public RolesRightsService rolesRightsService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public RolesServiceImpl(SysRolesMapper mapper) {
		this.sysRolesMapper=mapper;
		this.setMapper(mapper);
	}

	/**
	 * @see com.learnbind.ai.service.role.RoleService#searchRole(java.lang.String)
	 * 根据条件查询
	 */
	@Override
	public List<SysRoles> searchRole(String searchCond) {
		return sysRolesMapper.searchRole(searchCond);
	}

	/**
	 * 增加
	 * @see com.learnbind.ai.service.role.RoleService#insertRole(com.learnbind.ai.model.SysRole)
	 */
	@Override
	public int insertRole(SysRoles role) {
		return sysRolesMapper.insertSelective(role);
	}

	/**
	 * 修改
	 * @see com.learnbind.ai.service.role.RoleService#updateRole(com.learnbind.ai.model.SysRole)
	 */
	@Override
	public int updateRole(SysRoles role) {
		return sysRolesMapper.updateByPrimaryKeySelective(role);
	}

	/**
	 * 删除
	 * @see com.learnbind.ai.service.role.RoleService#deleteRole(long)
	 */
	@Override
	@Transactional
	public int deleteRole(long roleId) {
		try {
			int rows = sysRolesMapper.deleteByPrimaryKey(roleId);
			if(rows>0) {
				SysUsersRoles userRole = new SysUsersRoles();
				userRole.setRoleId(roleId);
				usersRolesService.delete(userRole);//删除用户-角色关系表
				SysRolesRights roleRight = new SysRolesRights();
				roleRight.setRoleId(roleId);
				rolesRightsService.delete(roleRight);//删除角色-菜单权限关系表
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<SysRoles> findAllRole(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<SysRoles> roleList = sysRolesMapper.selectAll();
		PageInfo result = new PageInfo(roleList);
		return result;
	}

	/**
	 * @see com.learnbind.ai.service.role.RoleService#getRoleListByUserId(java.lang.Long)
	 */
	@Override
	public List<SysRoles> getRoleListByUserId(Long userId) {
		return sysRolesMapper.getListByUserId(userId);
	}
}
