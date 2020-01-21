package com.learnbind.ai.controller.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.service.rights.RightsService;
import com.learnbind.ai.service.role.RolesService;
import com.learnbind.ai.service.user.UsersService;


/**
 * Copyright (c) 2018 by Hz
 * 
 * @ClassName: IndexController.java
 * @Description: 前端控制器.首页
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:59:41
 */
@Controller
@RequestMapping(value = "/main-index")
public class IndexController {
	private static Log log = LogFactory.getLog(IndexController.class);
	private static final String TEMPLATE_PATH = ""; // 页面目录批次
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private RolesService rolesService;
	@Autowired
	private RightsService rightsService;
	@Autowired
	private UsersService usersService;

	/** 
	*	@Title: batchStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String starter(Model model) {
		
		
		//获取当前登录用户信息
		//UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//System.out.println("当前登录用户[UserDetails]："+userDetails);
		try {
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//System.out.println("当前登录用户[UserBean]："+userBean);
			if(userBean!=null) {
				List<SysRoles> roleList = this.getRoleListByUserId(userBean.getId());
				//System.out.println("登录用户角色："+roleList);
				userBean.setRoleList(roleList);
				
				List<SysRights> rightList = this.getRightListByRoleIds(roleList);
				//System.out.println("登录用户菜单权限："+rightList);
				userBean.setRightList(rightList);
				//List<SysRights> rightList = userBean.getRightList();
				model.addAttribute("privilegeList", rightList);
			}
			model.addAttribute("userName", userBean.getNickname());
			model.addAttribute("user", userBean);
			return TEMPLATE_PATH + "index_starter";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return TEMPLATE_PATH + "index_starter";
	}
	
	/**
	 * @Title: loadEditPasswordDialog
	 * @Description: 打开修改密码对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/edit-password-dialog")
	public String loadEditPasswordDialog(Model model) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("user", userBean);
		return TEMPLATE_PATH + "edit_password_dialog";
	}
	
	/**
	 * @Title: checkPassword
	 * @Description: 验证旧密码是否正确
	 * @param userId
	 * @param oldPassword
	 * @return 
	 */
	@RequestMapping(value = "/check-password")
	@ResponseBody
	private boolean checkPassword(Long userId, String oldPassword) {
		SysUsers user = usersService.selectByPrimaryKey(userId);
		String password = DigestUtils.md5Hex(oldPassword).toUpperCase();//加密密码
		if(user.getPassword().equals(password)) {//旧密码输入正确
			return true;
		} else {
			return false;
		}
		
	}
	
	
	@RequestMapping(value = "/edit-password", produces = "application/json")
	@ResponseBody
	public Object editPassword(Long userId, String newPassword) {
		SysUsers user = usersService.selectByPrimaryKey(userId);
		String password = DigestUtils.md5Hex(newPassword).toUpperCase();//加密密码
		user.setPassword(password);
		int row = usersService.updateByPrimaryKeySelective(user);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
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