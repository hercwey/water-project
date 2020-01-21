package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysRoles;

import tk.mybatis.mapper.common.Mapper;

public interface SysRolesMapper extends Mapper<SysRoles> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysRoles> searchRole(@Param("searchCond") String searchCond);
	
    /**
     * @Title: getRoleListByUserId
     * @Description: 根据用户ID查询用户角色
     * @param userId
     * @return 
     */
    public List<SysRoles> getListByUserId(@Param("userId") Long userId); 
    
}