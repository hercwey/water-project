package com.learnbind.ai.controller.peopleadjust;

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
import com.learnbind.ai.model.SysPeopleAdjust;
import com.learnbind.ai.service.peopleadjust.PeopleAdjustService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.peopleadjust
 *
 * @Title: PeopleAdjustController.java
 * @Description: 多人口调整配置控制器
 *
 * @author Administrator
 * @date 2019年5月15日 上午11:30:05
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/people-adjust")
public class PeopleAdjustController {
	
	private static Log log = LogFactory.getLog(PeopleAdjustController.class);
	
	private static final String TEMPLATE_PATH = "people_adjust/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private PeopleAdjustService peopleAdjustService;//多人口调整配置服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "people_adjust_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		List<SysPeopleAdjust> peopleAdjustList = peopleAdjustService.selectAll();
		
		SysPeopleAdjust peopleAdjust = null;
		if(peopleAdjustList!=null && peopleAdjustList.size()>0) {
			peopleAdjust = peopleAdjustList.get(0); 
		}
		model.addAttribute("currItem", peopleAdjust);
		
		return TEMPLATE_PATH + "people_adjust_main";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param peopleAdjust
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(SysPeopleAdjust peopleAdjust) {
		int rows = peopleAdjustService.insertSelective(peopleAdjust);
		if(rows>0) {
			Long peopleAdjustId = 0l;
			List<SysPeopleAdjust> peopleAdjustList = peopleAdjustService.selectAll();
			if(peopleAdjustList!=null && peopleAdjustList.size()>0) {
				SysPeopleAdjust record = peopleAdjustList.get(0);
				peopleAdjustId = record.getId();
			}
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("peopleAdjustId", peopleAdjustId);
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param peopleAdjust
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(SysPeopleAdjust peopleAdjust) {
		int rows = peopleAdjustService.updateByPrimaryKeySelective(peopleAdjust);
		if(rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("peopleAdjustId", peopleAdjust.getId());
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}

}