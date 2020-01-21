package com.learnbind.ai.controller.abnormalcfg;

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
import com.learnbind.ai.model.AbnormalFee;
import com.learnbind.ai.service.abnormalcfg.AbnormalFeeService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.abnormalcfg
 *
 * @Title: AbnormalFeeController.java
 * @Description: 异常水费报警配置控制器
 *
 * @author Administrator
 * @date 2019年5月16日 上午11:11:39
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/abnormal-fee")
public class AbnormalFeeController {
	
	private static Log log = LogFactory.getLog(AbnormalFeeController.class);
	
	private static final String TEMPLATE_PATH = "abnormal_cfg/abnormal_fee/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private AbnormalFeeService abnormalFeeServiceService;//异常水费报警配置服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "abnormal_fee_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		List<AbnormalFee> abnormalFeeList = abnormalFeeServiceService.selectAll();
		
		AbnormalFee abnormalFee = null;
		if(abnormalFeeList!=null && abnormalFeeList.size()>0) {
			abnormalFee = abnormalFeeList.get(0); 
		}
		model.addAttribute("currItem", abnormalFee);
		
		return TEMPLATE_PATH + "abnormal_fee_main";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param abnormalFee
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(AbnormalFee abnormalFee) {
		int rows = abnormalFeeServiceService.insertSelective(abnormalFee);
		if(rows>0) {
			Long abnormalFeeId = 0l;
			List<AbnormalFee> abnormalFeeList = abnormalFeeServiceService.selectAll();
			if(abnormalFeeList!=null && abnormalFeeList.size()>0) {
				AbnormalFee record = abnormalFeeList.get(0);
				abnormalFeeId = record.getId();
			}
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("abnormalFeeId", abnormalFeeId);
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param abnormalFee
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(AbnormalFee abnormalFee) {
		int rows = abnormalFeeServiceService.updateByPrimaryKeySelective(abnormalFee);
		if(rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("abnormalFeeId", abnormalFee.getId());
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}

}