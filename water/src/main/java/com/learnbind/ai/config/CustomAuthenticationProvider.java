package com.learnbind.ai.config;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.learnbind.ai.bean.UserBean;

/**
 * 自定义验证
 * 
 * @author Administrator
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final Log log = LogFactory.getLog(getClass());//日志
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;//系统用户
	@Autowired
	private CustomPasswordEncoder customPasswordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
		log.debug("----------"+details.toString());
		
		//String username = authentication.getName();
		//String password = (String) authentication.getCredentials();
		String username = details.getUsername();//authentication.getName();
		String password = details.getPassword();//(String) authentication.getCredentials();
		
		log.debug("username:"+username);
		log.debug("password:"+password);
		
		
		UserBean user = (UserBean) customUserDetailsService.loadUserByUsername(username);
		if (user == null) {
			throw new BadCredentialsException("Username not found.");
		}

		String encoderPassword = customPasswordEncoder.encode(password);

		// 加密过程在这里体现
		if (!encoderPassword.equals(user.getPassword())) {
			throw new BadCredentialsException("Wrong password.");
		}
		
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		return new UsernamePasswordAuthenticationToken(user, password, authorities);
		
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
