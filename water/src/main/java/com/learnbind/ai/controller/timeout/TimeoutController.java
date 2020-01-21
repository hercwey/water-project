package com.learnbind.ai.controller.timeout;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Copyright (c) 2018 by srd
 * 
 * @ClassName: TimeoutController.java
 * @Description: 前端控制器.session超时
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:59:41
 */
@Controller
//@RequestMapping(value = "/user-logout")
public class TimeoutController {
	private static Log log = LogFactory.getLog(TimeoutController.class);
	
	private static final String TEMPLATE_PATH = ""; // 页面目录

	@RequestMapping(value = "/session-timeout/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String sessionTimeout(HttpServletRequest request, Model model) {
		model.addAttribute("error_msg", "登录超时，请重新登录！");
		return TEMPLATE_PATH + "timeout";
	}
	
	@RequestMapping(value = "/relogin/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String relogin(HttpServletRequest request, Model model) {
		model.addAttribute("error_msg", "账号已在其他地方登录，如需继续操作请重新登录！");
		return TEMPLATE_PATH + "timeout";
	}

}