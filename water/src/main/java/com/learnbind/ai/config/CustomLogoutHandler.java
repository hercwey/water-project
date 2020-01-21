package com.learnbind.ai.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * 登出实现类
 * @author Administrator
 *
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

	private final Log log = LogFactory.getLog(getClass());

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		log.debug("====================登出");
	}

}
