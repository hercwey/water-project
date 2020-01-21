package com.learnbind.ai.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.model.SysUsersRoles;
import com.learnbind.ai.service.role.RolesService;
import com.learnbind.ai.service.user.UsersService;
import com.learnbind.ai.service.usersroles.UsersRolesService;


/**
 * Copyright (c) 2018 by srd
 * 
 * @ClassName: UserController.java
 * @Description: 前端控制器.用户
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:59:41
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
	private static Log log = LogFactory.getLog(UserController.class);
	private static final String TEMPLATE_PATH = "user/"; // 页面目录用户
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	private UsersService usersService;  //用户
	@Autowired
	private RolesService rolesService;  //角色
	@Autowired
	private UsersRolesService usersRolesService;  //用户角色

	/** 
	*	@Title: userStarter 
	*	@Description: 用户起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String userStarter(Model model) {
		return TEMPLATE_PATH + "user_starter";
	}

	/** 
	*	@Title: userSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String userSearch(Model model) {
		
		List<SysRoles> roleList = rolesService.selectAll();
		model.addAttribute("roleList", roleList);
		
		return TEMPLATE_PATH + "user_main";
	}
	
	/**
	 * @Title: sysUserTab
	 * @Description: 加载系统用户列表
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/sysuser-tab")
	/* @ResponseBody */
	public String sysUserTab(Model model) {
		
		List<SysRoles> roleList = rolesService.selectAll();
		model.addAttribute("roleList", roleList);
		
		return TEMPLATE_PATH + "sysuser_tab";
	}
	
	/** 
	*	@Title: userTable 
	*	@Description: 加载系统用户列表(列表页面)
	*	@param @param model ModelView中传递数据的对象
	*	@param @param pageNum 页号
	*	@param @param pageSize 页大小
	*	@param @param searchCond 查询条件
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/sysuser-table")
	/* @ResponseBody */
	public String userTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysUsers> userList = usersService.searchUser(searchCond);
		PageInfo<List<SysUsers>> pageInfo = new PageInfo(userList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("userList", userList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "sysuser_table";
	}
	
	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadadddialog")
	public String loadAddDialog(Model model) {
		return TEMPLATE_PATH + "user_dialog_edit";
	}
	
	/** 
	*	@Title: addUser 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addUser(SysUsers user) {
		String password = DigestUtils.md5Hex("123456").toUpperCase();//默认密码123456
		user.setPassword(password);
		user.setCreateTime(new Date());
		int row = usersService.addUser(user);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteUser 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteUser(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				//userService.deleteByPrimaryKey(id);
				SysUsers user = new SysUsers();
				user.setId(id);
				user.setDeleted(1);
				usersService.updateByPrimaryKeySelective(user);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}		
		return RequestResultUtil.getResultDeleteSuccess();

	}

	/** 
	*	@Title: loadModiUserDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiDialogxxx(Long userId, Model model) {
		//读取需要编辑的条目
		SysUsers currItem=usersService.selectByPrimaryKey(userId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "user_dialog_edit";
	}

	/** 
	*	@Title: updateUser 
	*	@Description: 修改分类 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateUser(SysUsers user) throws Exception {
		usersService.updateByPrimaryKeySelective(user);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	/**
	 * @Title: selectRoleAuth
	 * @Description: 获取角色授权
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/select-role-auth", produces = "application/json")
	@ResponseBody
	public Object selectRoleAuth(Long userId) {
		
		try {
			SysUsersRoles userRole = new SysUsersRoles();
			userRole.setUserId(userId);
			List<SysUsersRoles> userRefRoleList = usersRolesService.select(userRole);
			
			Map<String, Object> resp = RequestResultUtil.getResultSelectSuccess();
			resp.put("userRefRoleList", userRefRoleList);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.RESULT_SELECT_WARN;
	}
	
	/**
	 * 	增加新的角色授权
	 * @param userId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	/**
	 * @Title: insertRoleAuth
	 * @Description: 增加角色授权（用户角色关系）
	 * @param userId
	 * @param roleIds
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/insert-role-auth", produces = "application/json")
	@ResponseBody
	public Object insertRoleAuth(Long userId, String  roleIds) throws Exception {
		
		try {
			if(userId!=null && StringUtils.isNotBlank(roleIds)) {
				int rows = usersRolesService.insertList(userId, roleIds);
				if(rows>0) {
					return RequestResultUtil.getResultSaveSuccess();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultSaveWarn();
	}

	@RequestMapping(value = "/testdialog")
	public String testdialog(Model model) {
		return "/testdialog";
	}

}