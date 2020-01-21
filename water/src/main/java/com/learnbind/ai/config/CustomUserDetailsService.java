package com.learnbind.ai.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.service.user.UsersService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private final Log log = LogFactory.getLog(getClass());// 日志

	@Autowired
	private CustomPasswordEncoder customPasswordEncoder;

	@Autowired
	private UsersService usersService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 根据用户名从数据库查询对应记录
		UserBean user = usersService.selectUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("username is not exists");
		}
		log.debug("username is : " + user.getUsername() + ", password is :" + user.getPassword());
		// user.setPassword(customPasswordEncoder.encode(user.getPassword()));
		// System.out.println("username is : " + user.getAccount() + ", password is :" +
		// user.getPassword());

		// 封装用户信息，并返回。参数分别是：用户名，密码，用户权限
		// User userTemp = new User(account, user.getPassword(),
		// AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
		// System.out.println("username is : " + userTemp.getUsername() + ", password is
		// :" + userTemp.getPassword());

		return user;
	}
}
