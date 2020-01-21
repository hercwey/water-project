package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysUsers;

import tk.mybatis.mapper.common.Mapper;

public interface SysUsersMapper extends Mapper<SysUsers> {
	
	/**
	 * @Title: searchUser
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<SysUsers> searchUser(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getUserListByRoleCode
	 * @Description: 根据角色编码或条件查询用户
	 * @param roleCode
	 * @param searchCond
	 * @return 
	 */
	public List<SysUsers> getUserListByRoleCode(@Param("roleCode") String roleCode, @Param("searchCond") String searchCond);
	
}