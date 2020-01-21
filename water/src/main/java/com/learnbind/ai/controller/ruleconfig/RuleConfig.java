package com.learnbind.ai.controller.ruleconfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.jsengine.PartitionRuleUtil;

/**
 * 
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.ruleconfig
 *
 * @Title: RuleConfig.java
 * @Description: 规则配置(分水量规则配置)
 *
 * @author lenovo
 * @date 2019年9月28日 上午8:13:17
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/rule")
public class RuleConfig {
	private static Log log = LogFactory.getLog(RuleConfig.class);
	private static final String TEMPLATE_PATH = "rule_config/"; // 页面目录
	
	@RequestMapping(value = "/starter")	
	public String ruleStarter(Model model) {
		return TEMPLATE_PATH + "rule_starter";
	}
	
	/**
	 * 
	 * @Title: ruleValidate
	 * @Description: 对rule进行校验
	 * @param ruleStr rule字符串
	 * @param model
	 * @return  如果验证通过则返回
	 * 			success,否则返回fail			
	 */
	@RequestMapping(value = "/validate", produces = "application/json")
	@ResponseBody	
	public Object ruleValidate(String ruleStr,Model model) {
		//
		PartitionRuleUtil ruleUtil=new PartitionRuleUtil();
		boolean validResult=ruleUtil.validateRule(ruleStr);
		if(validResult)
			return RequestResultUtil.getResultSuccess("true");
		else
			return RequestResultUtil.getResultFail("false"); 
	}
	
	
	
}
