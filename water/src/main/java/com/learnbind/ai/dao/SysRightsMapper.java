package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysRights;

import tk.mybatis.mapper.common.Mapper;

public interface SysRightsMapper extends Mapper<SysRights> {
	
	/**
	 * 根据角色ID集合查询菜单权限
	 * @param roleIds
	 * @return
	 */
	public List<SysRights> getByRoleIds(@Param("roleIds") List<Long> roleIds);
	
}