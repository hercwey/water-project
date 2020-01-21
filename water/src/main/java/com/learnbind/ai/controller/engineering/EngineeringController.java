package com.learnbind.ai.controller.engineering;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.InstallEngineering;
import com.learnbind.ai.model.LocationEngineering;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.engineering.EngineeringService;
import com.learnbind.ai.service.engineering.LocationEngineeringService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.engineering
 *
 * @Title: EngineeringController.java
 * @Description: 安装工程控制器
 *
 * @author Administrator
 * @date 2019年8月4日 上午9:16:46
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/engineering")
public class EngineeringController {
	private static Log log = LogFactory.getLog(EngineeringController.class);
	private static final String TEMPLATE_PATH = "engineering/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	private EngineeringService engineeringService;//安装工程
	@Autowired
	private LocationEngineeringService locationEngineeringService;//地理位置-工程
	

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "engineering_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "engineering_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		Long operatorId = this.getOperatorId();//操作员ID
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Example example = new Example(InstallEngineering.class);
		Criteria criteria = example.createCriteria();
		if(operatorId!=null) {
			criteria.andEqualTo("operatorId", operatorId);
		}
		if(StringUtils.isNotBlank(searchCond)) {
			criteria.andLike("address", "%"+searchCond+"%");
		}
		List<InstallEngineering> engineeringList = engineeringService.selectByExample(example);
		PageInfo<InstallEngineering> pageInfo = new PageInfo<>(engineeringList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("engineeringList", engineeringList);//列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "engineering_table";
	}

	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long itemId) {
		
		InstallEngineering engineering = null;
		if(itemId!=null) {
			engineering = engineeringService.selectByPrimaryKey(itemId);
		}
		model.addAttribute("currItem", engineering);
		
		return TEMPLATE_PATH + "engineering_dialog_edit";
	}
	
	/** 
	*	@Title: addPrinter 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(InstallEngineering engineering, Long locationId, String traceIds) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		engineering.setOperatorId(userBean.getId());
		engineering.setOperatorName(userBean.getRealname());
		engineering.setCreateTime(new Date());
		
		int row = engineeringService.insertSelective(engineering);
		if (row > 0) {
			//增加地理位置-工程，增加前删除原工程的绑定关系
			this.insertLocationEngineering(engineering.getId(), locationId, traceIds);
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: insertLocationEngineering
	 * @Description: 增加地理位置-工程，增加前删除原工程的绑定关系
	 * @param engineeringId
	 * @param locationId
	 * @param traceIds 
	 */
	private void insertLocationEngineering(Long engineeringId, Long locationId, String traceIds) {
		
		if(locationId!=null && locationId>0) {
			LocationEngineering record = new LocationEngineering();
			record.setEngineeringId(engineeringId);
			locationEngineeringService.delete(record);
			
			LocationEngineering le = new LocationEngineering();
			le.setEngineeringId(engineeringId);
			le.setLocationId(locationId);
			le.setLocationTraceIds(traceIds);
			locationEngineeringService.insertSelective(le);
		}
		
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param engineering
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(InstallEngineering engineering, Long locationId, String traceIds) throws Exception {
		
		//修改安装工程
		engineeringService.updateByPrimaryKeySelective(engineering);
		//增加地理位置-工程，增加前删除原工程的绑定关系
		this.insertLocationEngineering(engineering.getId(), locationId, traceIds);
		
		return RequestResultUtil.getResultUpdateSuccess();
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				engineeringService.deleteByPrimaryKey(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 
	 * 		为null时是管理员角色，查询所有；不为null时是工程公司角色，只查询此工程公司创建的安装工程
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;//操作员ID
		if (userBean!=null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for(SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}
			
			//indexOf() 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
			if(roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_ADMIN)==-1) {
				operatorId = userBean.getId();//操作员ID
			}
			
		}
		return operatorId;
	}

}