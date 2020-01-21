package com.learnbind.ai.service.rolesrights.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.dao.SysRolesRightsMapper;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.rolesrights.RolesRightsService;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     RolesRightsServiceImpl.java
	* @Description:   用户角色 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class RolesRightsServiceImpl extends AbstractBaseService<SysRolesRights, Long> implements  RolesRightsService {
	
	@Autowired
	public SysRolesRightsMapper sysRolesRightsMapper;
	@Autowired
	private RolesRightsService rolesRightsService;  //角色-菜单权限-关系
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public RolesRightsServiceImpl(SysRolesRightsMapper mapper) {
		this.sysRolesRightsMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int insertRolesRights(Long roleId, String rightIds) {
		
		SysRolesRights record = new SysRolesRights();
		record.setRoleId(roleId);
		sysRolesRightsMapper.delete(record);//删除原来的角色-菜单权限-关系
		
		if(StringUtils.isNotBlank(rightIds)){
			int rows = this.saveRolesRights(roleId, rightIds);
			if(rows>0){
				return rows;
			}
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	@Transactional
	private int saveRolesRights(Long roleId, String rightIds){
		int rows = 0;
		String[] ids = rightIds.split(",");
		int len = ids.length;
		if(len<=0){
			return 1;
		}
		for(int i=0; i<len; i++){
			String rightId = ids[i];//获取权限ID，此处的权限为菜单权限，用的是菜单表
			if(StringUtils.isNotBlank(rightId)){
				SysRolesRights record = new SysRolesRights();
				record.setRoleId(roleId);
				record.setRightId(Long.valueOf(rightId));
				record.setRemark("123");
				rows = rolesRightsService.insertSelective(record);
				if(rows>0){
					continue;
				}else{
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
		}
		if(rows>0){
			return rows;
		}
		return 0;
	}
}
