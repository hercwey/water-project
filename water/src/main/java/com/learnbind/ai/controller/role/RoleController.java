package com.learnbind.ai.controller.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.service.rights.RightsService;
import com.learnbind.ai.service.role.RolesService;
import com.learnbind.ai.service.rolesrights.RolesRightsService;


/**
 * Copyright (c) 2018 by srd
 * 
 * @ClassName: RoleController.java
 * @Description: 前端控制器.角色
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:59:41
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController {
	private static Log log = LogFactory.getLog(RoleController.class);
	private static final String TEMPLATE_PATH = "role/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private RolesService rolesService;  //角色
	@Autowired
	private RightsService rightsService;  //菜单权限
	@Autowired
	private RolesRightsService rolesRightsService;  //角色-菜单权限-关系

	/** 
	*	@Title: roleStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String roleStarter(Model model) {
		return TEMPLATE_PATH + "role_starter";
	}

	/** 
	*	@Title: roleSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String roleSearch(Model model) {
		List<SysRights> rightList = rightsService.selectAll();
		
		List<SysRights> resultList = new ArrayList<>();//保存菜单排序结果
		resultList.clear();
		for(int i=0; i<rightList.size(); i++){
			SysRights right = rightList.get(i);
			if(right.getPid()==0){
				//System.out.println(privilege.getType()+","+privilege.getName());
				resultList.add(right);
				sortList(resultList, rightList,right.getId());
			}
		}
		model.addAttribute("rightList", resultList);
		return TEMPLATE_PATH + "role_main";
	}

	/** 
	*	@Title: roleTable 
	*	@Description: 加载用户列表(列表页面)
	*	@param @param model ModelView中传递数据的对象
	*	@param @param pageNum 页号
	*	@param @param pageSize 页大小
	*	@param @param searchCond 查询条件
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String roleTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysRoles> roleList = rolesService.searchRole(searchCond);
		PageInfo<List<SysRoles>> pageInfo = new PageInfo(roleList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("roleList", roleList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "role_table";
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
		return TEMPLATE_PATH + "role_dialog_edit";
	}
	
	/** 
	*	@Title: addRole 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addRole(SysRoles role) {
		role.setCreateTime(new Date());
		int row = rolesService.insertRole(role);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteRole 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteRole(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				rolesService.deleteRole(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/** 
	*	@Title: loadModiRoleDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiRoleDialog(Long itemId, Model model) {
		//读取需要编辑的条目
		SysRoles currItem=rolesService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "role_dialog_edit";
	}

	/** 
	*	@Title: updateRole 
	*	@Description: 修改分类 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateRole(SysRoles role) throws Exception {
		rolesService.updateByPrimaryKeySelective(role);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	/**
	 * 加载菜单权限对话框
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-privilege-dialog")
	public String loadPrivilegeDialog(HttpServletRequest request, Model model, Long roleId) {
		
		List<SysRights> rightList = rightsService.selectAll();
		
		List<SysRights> resultList = new ArrayList<>();//保存菜单排序结果
		resultList.clear();
		for(int i=0; i<rightList.size(); i++){
			SysRights privilege = rightList.get(i);
			if(privilege.getPid()==0){
				//System.out.println(privilege.getType()+","+privilege.getName());
				resultList.add(privilege);
				sortList(resultList, rightList,privilege.getId());
			}
		}
		model.addAttribute("rightList", resultList);
		
		model.addAttribute("roleId", roleId);
		
		return TEMPLATE_PATH + "role_privilege_dialog";
	}
	
	/**
	 * List排序
	 * @param categoryList
	 * @param cid
	 */
	private void sortList(List<SysRights> resultList, List<SysRights> rightList,Long rightId) {
		
		for(int i=0; i<rightList.size(); i++){
			SysRights right = rightList.get(i);
        	if(right.getPid().equals(rightId)){
        		//System.out.println(privilege.getType()+","+privilege.getName());
                resultList.add(right);
                sortList(resultList, rightList,right.getId());
            }
        }
    }
	
	/**
	 * 查询角色菜单权限关系
	 * @param request
	 * @param model
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/select-role-privilege", produces = "application/json")
	@ResponseBody
	public Object selectRolePrivilege(HttpServletRequest request, Model model, Long roleId) throws Exception {
		
		try {
			SysRolesRights record = new SysRolesRights();
			record.setRoleId(roleId);
			List<SysRolesRights> roleRightList = rolesRightsService.select(record);
			
			Map<String, Object> resp = RequestResultUtil.getResultSelectSuccess();
			resp.put("roleRightList", roleRightList);
			resp.put("roleId", roleId);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultSelectSuccess();
	}
	
	/**
	 * 增加角色-菜单权限-关系
	 * @param request
	 * @param model
	 * @param roleId
	 * @param privilegeIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insert-role-privilege", produces = "application/json")
	@ResponseBody
	public Object insertRolePrivilege(HttpServletRequest request, Model model, Long roleId, String privilegeIds) throws Exception {
		
		try {
			if(roleId!=null && StringUtils.isNotBlank(privilegeIds)) {
				int rows = rolesRightsService.insertRolesRights(roleId, privilegeIds);
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