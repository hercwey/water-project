package com.learnbind.ai.config.upload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.config.upload
 *
 * @Title: UploadFilePathConfig.java
 * @Description: 配置上传文件目录可访问
 *
 * @author Administrator
 * @date 2019年5月27日 下午3:06:22
 * @version V1.0 
 *
 */
@Component
public class UploadFilePathConfig implements WebMvcConfigurer {

	private final Log log = LogFactory.getLog(getClass());//日志
	
	@Autowired
	private UploadFileConfig uploadFileConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    	String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		log.info("==============================操作系统："+os);
    		registry.addResourceHandler(uploadFileConfig.getStaticAccessPath()).addResourceLocations("file:" + uploadFileConfig.getWindowsUploadFolder());
    	}else {
    		log.info("==============================操作系统："+os);
    		registry.addResourceHandler(uploadFileConfig.getStaticAccessPath()).addResourceLocations("file:" + uploadFileConfig.getLinuxUploadFolder());
    	}
    	
    }
}
