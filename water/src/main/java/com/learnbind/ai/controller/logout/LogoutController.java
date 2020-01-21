package com.learnbind.ai.controller.logout;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Copyright (c) 2018 by srd
 * 
 * @ClassName: LogoutController.java
 * @Description: 前端控制器.登出
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:59:41
 */
@Controller
//@RequestMapping(value = "/user-logout")
public class LogoutController {
	private static Log log = LogFactory.getLog(LogoutController.class);
	private static final String TEMPLATE_PATH = ""; // 页面目录批次
	private static final int PAGE_SIZE = 8; // 页大小

	@RequestMapping(value = "/user-logout")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String userLogout(HttpServletRequest request, Model model) {
		model.addAttribute("error_msg", "登出！");
		return TEMPLATE_PATH + "login_starter";
	}

}