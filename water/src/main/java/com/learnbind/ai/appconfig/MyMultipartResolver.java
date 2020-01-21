package com.learnbind.ai.appconfig;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
 
/**
 * @author lenovo
 * 用于兼容spring boot 及  ServletFielUpload 兼容性
 */
@Configuration
public class MyMultipartResolver extends CommonsMultipartResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(MyMultipartResolver.class);	
 
    /**
     * 这里是处理Multipart http的方法。
     * 	如果这个返回值为true,那么Multipart http body就会MyMultipartResolver 消耗掉.
     *  如果这里返回false 那么就会交给后面的自己写的处理函数处理例如刚才ServletFileUpload 所在的函数
     * @see org.springframework.web.multipart.commons.CommonsMultipartResolver#isMultipart(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        // 过滤保存的接口  兼容MultipartResolver 或者 ServletFileUpload
		// 如果是ueditor上传的路径则采用
    	 final String ueditUploadUri="/ueditor/action";  //此处为UEditorController的请求URL
    	 
		/* System.out.println("=================="+request.getRequestURL()); */
    	 
    	
        if (request.getRequestURI().contains(ueditUploadUri)) {
        	//System.out.println("MyMultipartResolver  user ServletUploadFile----- is:  true");
            return false;
        }
        return super.isMultipart(request);
    }
 
}
