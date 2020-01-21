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
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.abnormalcfg
 *
 * @Title: TestIndexController.java
 * @Description: 首页统计图
 *
 * @author Thinkpad
 * @date 2020年1月12日 下午4:06:06
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/test-index")
public class TestIndexController {
	
	private static Log log = LogFactory.getLog(TestIndexController.class);
	
	private static final String TEMPLATE_PATH = "abnormal_cfg/test_index/"; // 页面目录角色
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
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "main";
	}
	
	
	@RequestMapping(value = "/table")
	public String table(Model model) {
		
		return TEMPLATE_PATH + "table";
	}
	
	
}