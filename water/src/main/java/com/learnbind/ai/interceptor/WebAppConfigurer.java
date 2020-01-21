package com.learnbind.ai.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.learnbind.ai.util.SpringUtil;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
	
	private WxLoginInterceptor wechatInterceptor;
	
    /** 
     * (非 Javadoc)
     * 
     * @Title: addInterceptors
     * @Description: 将需要绑定后方可执行功能导航到绑定页
     * @param registry 
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	//interceptor 1:
    	//生成一个新的拦截器对象
    	//WxLoginInterceptor wechatInterceptor=new WxLoginInterceptor();
    	//自spring factory中获取指定的Bean
    	wechatInterceptor=SpringUtil.getBean(WxLoginInterceptor.class);
    	
    	//设置放行的URL		
		List<String> allowUrls=new ArrayList<>(); 
		allowUrls.add("/wechat/valid");			//Tencent验证水司服务器
		allowUrls.add("/wechat/articleList");	//文章列表
		allowUrls.add("/wechat/article");		//文章详情
		
		//allowUrls.add("/wechat/customer");		//用户绑定主页
		allowUrls.add("/wechat/bindcustomer");  //加载用户绑定页
		allowUrls.add("/wechat/bind");          //绑定请求		
		allowUrls.add("/wechat/searchcustomerno");  //加载查询用户编号页
		allowUrls.add("/wechat/customernolist");  //查询用户编号请求
		
		
		allowUrls.add("/wechat/meterrepair");   			//水表报修		                       
		allowUrls.add("/wechat/commitmeterrepair");   		//提交水表报修
		allowUrls.add("/wechat/dissentrequest");   			//争议申请
		allowUrls.add("/wechat/commitdissentrequest");   	//争议申请提交
		
		//allowUrls.add("/wechat/waterrepairbusinesspage"); //水表报修业务页
		
		allowUrls.add("/wechat/pay/notify");   			//支付成功CCB服务器发送异步请求地址.
		
		
		//allowUrls.add("/wechat/bill");  //账单列晴  此处不可以放行.
		
		
		allowUrls.add("/wechat/popper.min.js.map");	     //放行的URL
		allowUrls.add("/wechat/bootstrap.min.js.map");	 //放行的URL
		
		wechatInterceptor.setAllowUrls(allowUrls);		
    	
    	//需要过拦截器的URL pattern
        registry.addInterceptor(wechatInterceptor).addPathPatterns("/wechat/**");        
        
    }

}
