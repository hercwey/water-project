package com.learnbind.ai.controller.privilege;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.service.rights.RightsService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by srd
 * 
 * @ClassName: RightsController.java
 * @Description: 前端控制器.菜单权限
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月27日 下午2:59:41
 */
@Controller
@RequestMapping(value = "/privilege")
public class RightsController {
	private static Log log = LogFactory.getLog(RightsController.class);
	private static final String TEMPLATE_PATH = "privilege/"; // 页面目录菜单权限
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private RightsService rightsService;  //菜单权限

	/** 
	*	@Description: 菜单权限起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String privilegeStarter(Model model) {
		return TEMPLATE_PATH + "privilege_starter";
	}

	/** 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String main(Model model) {
		
		Example example = new Example(SysRights.class);
		example.setOrderByClause("right_level ASC,sort ASC,pid ASC,id ASC");
		List<SysRights> privilegeList = rightsService.selectByExample(example);
		
		//String privilegeListJson = JSON.toJSONString(privilegeList);
		model.addAttribute("privilegeListJson", privilegeList);
		
		List<SysRights> resultList = new ArrayList<>();//保存菜单排序结果
		resultList.clear();
		for(int i=0; i<privilegeList.size(); i++){
			SysRights privilege = privilegeList.get(i);
			if(privilege.getPid()==0){
				//System.out.println(privilege.getType()+","+privilege.getName());
				resultList.add(privilege);
				sortList(resultList, privilegeList,privilege.getId());
			}
		}
		model.addAttribute("privilegeList", resultList);
		
		return TEMPLATE_PATH + "privilege_main";
	}

	/** 
	*	@Title: loadEditDialog 
	*	@Description: 加载编辑对话框 
	*			未用
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model) {
		
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
		model.addAttribute("privilegeList", resultList);
		
		return TEMPLATE_PATH + "privilege_dialog_edit";
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
	 * 	增加
	 * @param request
	 * @param right
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(HttpServletRequest request, SysRights right){
		right.setCreateTime(new Date());
		int row = rightsService.insertSelective(right);
		if (row>0){
			return RequestResultUtil.getResultAddSuccess();
		}else{
			return RequestResultUtil.getResultAddWarn();
		}
	}
	
	/**
	 * 	修改
	 * @param request
	 * @param right
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(HttpServletRequest request, SysRights right){
		int row = rightsService.updateById(right);
		if (row>0){
			return RequestResultUtil.getResultUpdateSuccess();
		}else{
			return RequestResultUtil.getResultUpdateWarn();
		}
	}
	
	/**
	 * 	删除
	 * @param request
	 * @param privilege
	 * @return
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(HttpServletRequest request, Long id){
		int row = rightsService.deleteById(id);
		if (row>0){
			return RequestResultUtil.getResultDeleteSuccess();
		}else{
			return RequestResultUtil.getResultDeleteWarn();
		}
	}

	/** 
	*	@Title: deleteBatch 
	*	@Description: 删除分类 
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	/*@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteBatch(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				batchService.deleteByPrimaryKey(id);
				
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}*/

}