package com.learnbind.ai.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.dao.SysUsersMapper;
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.rights.RightsService;
import com.learnbind.ai.service.role.RolesService;
import com.learnbind.ai.service.user.UsersService;


/**
	* Copyright (c) 2018 by Hz
	* @ClassName:     UsersServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class UsersServiceImpl extends AbstractBaseService<SysUsers, Long> implements  UsersService {
	
	@Autowired
	public SysUsersMapper sysUsersMapper;
	
	@Autowired
	public RolesService rolesService;
	@Autowired
	public RightsService rightsService;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public UsersServiceImpl(SysUsersMapper mapper) {
		this.sysUsersMapper=mapper;
		this.setMapper(mapper);
	}

	/**
	 * 根据条件查询
	 */
	@Override
	public List<SysUsers> searchUser(String searchCond) {
		return sysUsersMapper.searchUser(searchCond);
	}
	
	@Override
	public List<SysUsers> getUserListByRoleCode(String roleCode, String searchCond) {
		return sysUsersMapper.getUserListByRoleCode(roleCode, searchCond);
	}

	/**
	 * 增加用户
	 */
	@Override
	public int addUser(SysUsers user) {
		return sysUsersMapper.insertSelective(user);
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<SysUsers> findAllUser(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<SysUsers> userDomains = sysUsersMapper.selectAll();
		PageInfo<SysUsers> result = new PageInfo<>(userDomains);
		return result;
	}

	@Override
	public UserBean selectUserByUsername(String username) {
		SysUsers record = new SysUsers();
		record.setUsername(username);
		List<SysUsers> userList = sysUsersMapper.select(record);
		if(userList!=null && userList.size()==1) {
			SysUsers sysUser = userList.get(0);
			UserBean userBean = new UserBean();
			userBean.setId(sysUser.getId());
			userBean.setUsername(sysUser.getUsername());
			userBean.setPassword(sysUser.getPassword());
			userBean.setNickname(sysUser.getNickname());
			userBean.setRealname(sysUser.getRealname());
			userBean.setTel(sysUser.getTel());
			userBean.setMobile(sysUser.getMobile());
			userBean.setEmail(sysUser.getEmail());
			userBean.setRemark(sysUser.getRemark());
			
			userBean.setIconPath(sysUser.getIconPath());
			userBean.setMarried(sysUser.getMarried());			
			userBean.setEnabled(sysUser.getEnabled());
			userBean.setDeleted(sysUser.getDeleted());
			userBean.setCreateTime(sysUser.getCreateTime());
			
			//List<SysRoles> roleList = this.getRoleListByUserId(sysUser.getId());
			//System.out.println("登录用户角色："+roleList);
			//userBean.setRoleList(roleList);
			
			//List<SysRights> rightList = this.getRightListByRoleIds(roleList);
			//System.out.println("登录用户菜单权限："+rightList);
			//userBean.setRightList(rightList);
			
			return userBean;
		}
		return null;
	}
	
	/**
	 * 根据用户ID查询角色
	 * @param userId
	 * @return
	 */
	private List<SysRoles> getRoleListByUserId(Long userId){
		return rolesService.getRoleListByUserId(userId);
	}
	
	/**
	 * 根据角色查询菜单权限
	 * @param roleList
	 * @return
	 */
	private List<SysRights> getRightListByRoleIds(List<SysRoles> roleList){
		
		List<Long> roleIds = new ArrayList<>();
		for(SysRoles role : roleList) {
			roleIds.add(role.getId());
		}
		
		if(roleIds==null || roleIds.size()<=0) {
			return null;
		}
		
		return rightsService.getListByRoleIds(roleIds);
	}

}
