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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumLoginStatus;
import com.learnbind.ai.model.LoginLog;
import com.learnbind.ai.service.loginlog.LoginLogService;

/**
 * 登出成功实现类
 * @author Administrator
 *
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	private final Log log = LogFactory.getLog(getClass());//日志
	
	@Autowired
    private LoginLogService loginLogService;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.debug("====================登出成功");
		try {
            UserBean user = (UserBean) authentication.getPrincipal();
      		
      	  	LoginLog loginlog = new LoginLog();
      	  	loginlog.setUserId(user.getId());
      	  	loginlog.setUsername(user.getRealname());
      	  	loginlog.setLoginDate(new Date());
      	  	loginlog.setStatus(EnumLoginStatus.LOGIN_OUT.getValue());
      	  	loginLogService.insertSelective(loginlog);
      	  	
            log.info("USER : " + user.getUsername() + " LOGOUT SUCCESS !  ");
            log.debug("===================="+"USER : " + user.getUsername() + " LOGOUT SUCCESS !  ");
            
        } catch (Exception e) {
            log.info("LOGOUT EXCEPTION , e : " + e.getMessage());
        }
		
		response.sendRedirect("/user-login/starter");
	}

}
