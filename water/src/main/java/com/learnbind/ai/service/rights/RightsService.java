package com.learnbind.ai.service.rights;

import java.util.List;

import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by Hz
 * 
 * @ClassName: RightsService.java
 * @Description: 菜单权限-service layer
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:49:37
 */
public interface RightsService extends IBaseService<SysRights, Long> {

	/**
	 * 	根据ID修改菜单权限
	 * @param id
	 * @return
	 */
	public int updateById(SysRights privilege);
	
	/**
	 * 	根据ID删除菜单权限
	 * @param id
	 * @return
	 */
	public int deleteById(Long id);
	
	/**
	 * 根据角色ID集合查询菜单权限
	 * @param roleIds
	 * @return
	 */
	public List<SysRights> getListByRoleIds(List<Long> roleIds);
	
}
