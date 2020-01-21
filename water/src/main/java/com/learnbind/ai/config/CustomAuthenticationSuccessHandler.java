package com.learnbind.ai.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumLoginStatus;
import com.learnbind.ai.model.LoginLog;
import com.learnbind.ai.service.loginlog.LoginLogService;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		LoginLog loginLog = new LoginLog();
		loginLog.setUserId(userBean.getId());
		loginLog.setUsername(userBean.getRealname());
		loginLog.setLoginDate(new Date());
		loginLog.setStatus(EnumLoginStatus.LOGIN_IN.getValue());
		loginLogService.insertSelective(loginLog);
		
		log.debug("====================登录成功");

		// 这里可以根据实际情况，来确定是跳转到页面或者json格式。
		// 如果是返回json格式，那么我们这么写
		/**
		 * Map<String,String> map=new HashMap<>(); map.put("code", "200");
		 * map.put("msg", "登录成功");
		 * response.setContentType("application/json;charset=UTF-8");
		 * response.getWriter().write(objectMapper.writeValueAsString(map));
		 **/

		// 如果是要跳转到某个页面的
		new DefaultRedirectStrategy().sendRedirect(request, response, "/main-index/starter");
		// response.sendRedirect("/main-index/starter");

		// 什么都不做的话，那就直接调用父类的方法
		// super.onAuthenticationSuccess(request, response, authentication);

	}
}
