package com.learnbind.ai.controller.login;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.common.util.RsaUtil;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.service.user.UsersService;


/**
 * Copyright (c) 2018 by Hz
 * 
 * @ClassName: LoginController.java
 * @Description: 前端控制器.登录
 * 
 * @author: lenovo
 * @version: V1.0
 * @Date: 2018年8月1日 下午2:59:41
 */
@Controller
@RequestMapping(value = "/user-login")
public class LoginController {
	
	private static Log log = LogFactory.getLog(LoginController.class);

	private static final String TEMPLATE_PATH = ""; // 页面目录批次

	/** 
	*	@Title: batchStarter 
	*	@Description: 批次起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	public String batchStarter(HttpServletRequest request, Model model) {
		
		//在此处生成私钥与公钥并置于session中
		List<String> keyList= RsaUtil.createKeyPairs();
		HttpSession session=request.getSession();
		log.debug("keyList:"+keyList);
		session.setAttribute(SessionConstant.KEY_LIST, keyList);
		//将公钥发送到前台页面
		model.addAttribute(SessionConstant.PUBLIC_KEY, keyList.get(RsaUtil.PUBLIC_KEY_INDEX));
		
		log.debug("==========	go login");
		return TEMPLATE_PATH + "login_starter";
	}
	
	/**
	 * @Title: error
	 * @Description: 登录失败
	 * @param request
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/error")
	public String error(HttpServletRequest request, Model model) {
		
		//在此处生成私钥与公钥并置于session中
		List<String> keyList= RsaUtil.createKeyPairs();
		HttpSession session=request.getSession();
		log.debug("keyList:"+keyList);
		session.setAttribute(SessionConstant.KEY_LIST, keyList);
		//将公钥发送到前台页面
		model.addAttribute(SessionConstant.PUBLIC_KEY, keyList.get(RsaUtil.PUBLIC_KEY_INDEX));
		
		model.addAttribute("error_msg", "用户名或密码错误！");
		log.debug("==========	error");
		return TEMPLATE_PATH + "login_starter";
	}

}