package com.learnbind.ai.controller.login;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.LoginLog;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.loginlog.LoginLogService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.login
 *
 * @Title: LoginLogController.java
 * @Description: 登陆登出日志
 *
 * @author Thinkpad
 * @date 2019年7月6日 下午6:08:55
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/login-out-log")
public class LoginOutLogController {
	
	private static Log log = LogFactory.getLog(LoginOutLogController.class);
	
	private static final String TEMPLATE_PATH = "log_manage/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private LoginLogService loingLogService;//登陆登出服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "loginout_log_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "loginout_log_main";
	}
	
	/**
	 * @Title: loginoutLogTable
	 * @Description: 登陆登出日志
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String loginoutLogTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<LoginLog> logList = loingLogService.search(searchCond);
		PageInfo<List<LoginLog>> pageInfo = new PageInfo(logList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("logList", logList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "loginout_log_table";
	}


}