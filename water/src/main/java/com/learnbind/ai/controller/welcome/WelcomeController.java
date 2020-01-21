package com.learnbind.ai.controller.welcome;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.rights.RightsService;
import com.learnbind.ai.service.role.RolesService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.welcome
 *
 * @Title: WelcomeController.java
 * @Description: 系统欢迎页控制器
 *
 * @author Administrator
 * @date 2019年8月12日 下午3:51:12
 * @version V1.0 
 *
 */
@Controller
public class WelcomeController {
	private static Log log = LogFactory.getLog(WelcomeController.class);
	private static final String TEMPLATE_PATH = ""; // 页面目录批次

	/**
	 * @Title: welcome
	 * @Description: 系统欢迎页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/welcome")
	public String welcome(Model model) {
		log.info("----------	欢迎进入智慧营收系统	----------");
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("realname", userBean.getRealname());
		return TEMPLATE_PATH + "welcome";
	}

}