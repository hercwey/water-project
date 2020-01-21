package com.learnbind.ai.dao;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysUsersRoles;
import tk.mybatis.mapper.common.Mapper;

public interface SysUsersRolesMapper extends Mapper<SysUsersRoles> {
	
	
	/**
	 * @Title: getRolesByUserId
	 * @Description: 根据用户id获取角色
	 * @param userId
	 * @return 
	 */
	public String getRolesByUserId(@Param("userId")Long userId);
}