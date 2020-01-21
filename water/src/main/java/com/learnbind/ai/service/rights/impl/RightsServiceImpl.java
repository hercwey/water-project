package com.learnbind.ai.service.rights.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.dao.SysRightsMapper;
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.rights.RightsService;
import com.learnbind.ai.service.rolesrights.RolesRightsService;

/**
*	Copyright (c) 2018 by Hz
*	@ClassName:     PrivilegeServiceImpl.java
*	@Description:   菜单权限实现类
* 
*	@author:        lenovo
*	@version:       V1.0  
*	@Date:          2018年8月17日 下午5:51:34 
*/
@Service
public class RightsServiceImpl extends AbstractBaseService<SysRights, Long> implements RightsService {

	public SysRightsMapper sysRightsMapper;
	
	@Autowired
	public RolesRightsService rolesRightsService;

	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public RightsServiceImpl(SysRightsMapper mapper) {
		this.sysRightsMapper = mapper;
		this.setMapper(mapper);
	}

	/**
	 * @see com.learnbind.ai.service.rights.PrivilegeService#updateById(com.learnbind.ai.model.SysPrivilege)
	 */
	@Override
	public int updateById(SysRights privilege) {
		int rows = sysRightsMapper.updateByPrimaryKeySelective(privilege);
		return rows;
	}

	/** 
	 * @see com.ecp.service.ICategoryService#deleteById(java.lang.Long)
	 * 	根据主键删除（如果删除节点中有子节点一起删除）（逻辑删除）
	 */
	@Override
	@Transactional
	public int deleteById(Long id) {
		try {
			SysRights right = sysRightsMapper.selectByPrimaryKey(id);
			
			if(getLeafNodeSize(right.getId())>0){//如果有子节点
				return deleteNodes(id);
			}else {//如果没有子节点直接删除当前节点
//				SysRights record = new SysRights();
//				record.setId(id);
//				int rows = sysRightsMapper.delete(record);
				int rows = sysRightsMapper.deleteByPrimaryKey(id);
				if(rows>0) {
					//删除 角色-菜单权限关系表
					SysRolesRights temp = new SysRolesRights();
					temp.setRightId(id);
					rolesRightsService.delete(temp);
					return rows;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * 根据角色ID集合查询菜单权限
	 * @see com.learnbind.ai.service.rights.PrivilegeService#getListByRoleIds(java.util.List)
	 */
	@Override
	public List<SysRights> getListByRoleIds(List<Long> roleIds) {
		return sysRightsMapper.getByRoleIds(roleIds);
	}

	/**
	 * 	获取叶子节点个数
	 * @param id
	 * @return
	 */
	private int getLeafNodeSize(Long id) {
		SysRights record = new SysRights();
		record.setPid(id);
		List<SysRights> rightList = sysRightsMapper.select(record);
		if(rightList!=null) {
			return rightList.size();
		}
		return 0;
	}
	
	/**
	 * 	循环删除子节点
	 * @param id
	 * @return
	 */
	@Transactional
	private int deleteNodes(Long pid) throws Exception{
		//System.out.println("开始查询并删除子节点 主节点ID："+pid);
		SysRights record = new SysRights();
		record.setPid(pid);
		List<SysRights> privilegeList = sysRightsMapper.select(record);
		int rows = 0;
		for(int i=0; i<privilegeList.size(); i++){
			SysRights privilege = privilegeList.get(i);
			//System.out.println("for循环 节点："+privilege.toString());
			if(getLeafNodeSize(privilege.getId())<=0){//如果没有子节点
//				SysRights temp = new SysRights();
//				//temp.setParentId(pid);
//				temp.setId(privilege.getId());
//				rows = sysRightsMapper.delete(temp);
				rows = sysRightsMapper.deleteByPrimaryKey(privilege.getId());
				//System.out.println("for循环 删除节点："+privilege.toString());
				if(rows>0) {
					//删除 角色-菜单权限关系表
					SysRolesRights rolesRights = new SysRolesRights();
					rolesRights.setRightId(privilege.getId());
					rolesRightsService.delete(rolesRights);
				}
			}else {
				rows = deleteNodes(privilege.getId());
			}
		}
		if(rows>0) {
			SysRights temp = new SysRights();
			//temp.setParentId(pid);
			temp.setId(pid);
			rows = sysRightsMapper.delete(temp);
			//System.out.println("删除 主节点ID："+pid);
			if(rows>0) {
				//删除 角色-菜单权限关系表
				SysRolesRights rolesRights = new SysRolesRights();
				rolesRights.setRightId(temp.getId());
				rolesRightsService.delete(rolesRights);
			}
			return rows;
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

}
