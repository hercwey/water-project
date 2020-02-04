package com.learnbind.ai.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity // 注解开启Spring Security的功能
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final Log log = LogFactory.getLog(getClass());//日志
	
    /*@Bean
    public PasswordEncoder passwordEncoder(){
    	return new BCryptPasswordEncoder();
    }*/
	
	//@Autowired
	//private CustomPasswordEncoder customPasswordEncoder;
	@Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;  //注入我们自己的AuthenticationProvider
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	@Autowired
	private CustomAuthenticationFailHander customAuthenticationFailHander;
	@Autowired
	private CustomLogoutHandler customLogoutHandler;
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	@Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
		
		log.debug("---------- com.learnbind.pivas.config.WebSecurityConfig");
		log.debug("---------- configure WebSecurity");
		//解决静态资源被拦截的问题
		//web.ignoring().antMatchers("/static/**");
		web.ignoring().antMatchers("/bootstrap/**");
		web.ignoring().antMatchers("/calendar/**");
		web.ignoring().antMatchers("/color-picker/**");
		web.ignoring().antMatchers("/font-awesome/**");
		web.ignoring().antMatchers("/index/**");
		web.ignoring().antMatchers("/jquery/**");
		web.ignoring().antMatchers("/jquery-ui/**");
		web.ignoring().antMatchers("/login/**");		
		web.ignoring().antMatchers("/ztree/**");
		web.ignoring().antMatchers("/jsencrypt/**");
		web.ignoring().antMatchers("/viewer/**");//图片查看器
		web.ignoring().antMatchers("/tools/**");//工具
		web.ignoring().antMatchers("/pivas/**");//工具
		web.ignoring().antMatchers("/images/**");//工具
		web.ignoring().antMatchers("/js/**");//
		web.ignoring().antMatchers("/favicon.ico");//ico
		web.ignoring().antMatchers("/MP_verify_7Q8vmV0IbBm49kw9.txt");//微信验证文件
		
		web.ignoring().antMatchers("/weui/**");//工具
		web.ignoring().antMatchers("/bootstrap-switcher/**");//工具
		web.ignoring().antMatchers("/css/**");//工具
		web.ignoring().antMatchers("/assets/**");//登录页和首页静态资源
		
		web.ignoring().antMatchers("/upload/**");//上传文件目录
		web.ignoring().antMatchers("/download/**");//下载文件
		
		
		//不需要拦截的URL
		web.ignoring().antMatchers("/session-timeout/starter");
		web.ignoring().antMatchers("/relogin/starter");
		web.ignoring().antMatchers("/user-login/starter");
		web.ignoring().antMatchers("/api/**");   //restful api放行.
		//web.ignoring().antMatchers("/test/**");   //restful test放行.
		web.ignoring().antMatchers("/ccb-test/**");   //restful test放行.
		
		//测试时使用,生产环境屏蔽即可.
		web.ignoring().antMatchers("/wechat/**");
		web.ignoring().antMatchers("/commons/**");
		
		//IOT接入平台测试
		web.ignoring().antMatchers("/iot/**");
		web.ignoring().antMatchers("/auth/**");
		web.ignoring().antMatchers("/device/**");
		web.ignoring().antMatchers("/subscribe/**");
		web.ignoring().antMatchers("/meter/**");
		web.ignoring().antMatchers("/cmd/**");
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.debug("---------- com.learnbind.pivas.config.WebSecurityConfig");
		log.debug("---------- configure HttpSecurity");
		//解决非thymeleaf的form表单提交被拦截问题
        http.csrf().disable();
		http
			.formLogin()
			.loginPage("/user-login/starter")//指定登录页是"/login/starter"
			.loginProcessingUrl("/user-login/submit")//登录处理url
			.successHandler(customAuthenticationSuccessHandler)
			.failureHandler(customAuthenticationFailHander)
			//.defaultSuccessUrl("/main-index/starter")//登录成功后默认跳转到"/index/starter"
			//.successForwardUrl("/main-index/starter")//登录成功转发
			.failureUrl("/user-login/error")
			.authenticationDetailsSource(authenticationDetailsSource)
			.permitAll()
			.and()
			.authorizeRequests()//定义哪些url需要保护，哪些url不需要保护
			//.antMatchers("/user-login/starter").permitAll()//允许所有用户访问"/"和"/home"
			.anyRequest().authenticated()//其他地址的访问均需验证权限
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			//.logoutUrl("user-logout")
			.logoutSuccessUrl("/user-login/starter")//退出登录后的默认url是"/login/starter"
			//.addLogoutHandler(new CustomLogoutHandler())
			.addLogoutHandler(customLogoutHandler)
			.logoutSuccessHandler(customLogoutSuccessHandler)		
			.invalidateHttpSession(true)//指定是否在注销时让HttpSession无效
			.clearAuthentication(true)
			.permitAll();
		
		//session管理
        //session失效后跳转
        //http.sessionManagement().invalidSessionUrl("/session-timeout/starter");
		http.sessionManagement().invalidSessionUrl("/user-login/starter");
        //只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
        http.sessionManagement().maximumSessions(1).expiredUrl("/user-login/starter");
        //http.sessionManagement().maximumSessions(1).expiredUrl("/relogin/starter");
		
		http.headers().frameOptions().sameOrigin();//设置可以在iframe中加载
        
	    //解决中文乱码问题
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter,CsrfFilter.class);
        
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService());
		log.debug("---------- com.learnbind.pivas.config.WebSecurityConfig");
		log.debug("---------- configureGlobal");
		auth.authenticationProvider(customAuthenticationProvider);
		//auth.userDetailsService(new UsersService()).passwordEncoder(passwordEncoder());
        //也可以将用户名密码写在内存，不推荐
        //auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
		//在内存中创建了一个用户，该用户的名称为user，密码为password，用户角色为USER
	}
	
}
