package com.learnbind.ai.controller.overduefineconfig;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.SysOverdueFine;
import com.learnbind.ai.service.overduefine.OverdueFineService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.overduefine
 *
 * @Title: OverdueFineController.java
 * @Description: 每日违约金比例配置
 *
 * @author Administrator
 * @date 2019年5月15日 下午4:25:06
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/overdue-fine")
public class OverdueFineController {
	
	private static Log log = LogFactory.getLog(OverdueFineController.class);
	
	private static final String TEMPLATE_PATH = "overdue_fine/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private OverdueFineService overdueFineService;//每日违约金比例配置服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "overdue_fine_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		List<SysOverdueFine> overdueFineList = overdueFineService.selectAll();
		
		SysOverdueFine overdueFine = null;
		if(overdueFineList!=null && overdueFineList.size()>0) {
			overdueFine = overdueFineList.get(0); 
		}
		model.addAttribute("currItem", overdueFine);
		
		return TEMPLATE_PATH + "overdue_fine_main";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param overdueFine
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(SysOverdueFine overdueFine) {
		int rows = overdueFineService.insertSelective(overdueFine);
		if(rows>0) {
			Long overdueFineId = 0l;
			List<SysOverdueFine> overdueFineList = overdueFineService.selectAll();
			if(overdueFineList!=null && overdueFineList.size()>0) {
				SysOverdueFine record = overdueFineList.get(0);
				overdueFineId = record.getId();
			}
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("overdueFineId", overdueFineId);
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param overdueFine
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(SysOverdueFine overdueFine) {
		int rows = overdueFineService.updateByPrimaryKeySelective(overdueFine);
		if(rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("overdueFineId", overdueFine.getId());
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}

}