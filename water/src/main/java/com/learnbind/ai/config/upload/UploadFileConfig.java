package com.learnbind.ai.config.upload;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.config.upload
 *
 * @Title: UploadFileConfig.java
 * @Description: 读取配置文件
 *
 * @author Administrator
 * @date 2019年5月27日 下午3:07:30
 * @version V1.0 
 *
 */
@Configuration
@PropertySource(value = {"classpath:config/application-upload.properties" })
public class UploadFileConfig {

	private final Log log = LogFactory.getLog(getClass());//日志
	
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;//静态资源对外暴露的访问路径
    @Value("${file.linuxUploadFolder}")
    private String linuxUploadFolder;//文件上传目录（Linux）
    @Value("${file.windowsUploadFolder}")
    private String windowsUploadFolder;//文件上传目录（Windows）
    
    
	public String getStaticAccessPath() {
		return staticAccessPath;
	}
	public void setStaticAccessPath(String staticAccessPath) {
		this.staticAccessPath = staticAccessPath;
	}
	public String getLinuxUploadFolder() {
		return linuxUploadFolder;
	}
	public void setLinuxUploadFolder(String linuxUploadFolder) {
		this.linuxUploadFolder = linuxUploadFolder;
	}
	public String getWindowsUploadFolder() {
		return windowsUploadFolder;
	}
	public void setWindowsUploadFolder(String windowsUploadFolder) {
		this.windowsUploadFolder = windowsUploadFolder;
	}
	
	/**
	 * 根据不同的操作系统返回相应的上传路径.
	 * @return
	 */
	public String getUploadFolder() {
		String uploadFolder="";
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows
		log.info("==============================操作系统："+os);
    	if(os.toUpperCase().startsWith("WIN")){  
    		uploadFolder=windowsUploadFolder;
    	}else {
    		uploadFolder=linuxUploadFolder;
    	}
    	uploadFolder = StringUtils.replace(uploadFolder, "//", "/");
    	uploadFolder = StringUtils.replace(uploadFolder, "/", File.separator);
    	return uploadFolder;
	}
	
	@Override
	public String toString() {
		return "UploadFileConfig [staticAccessPath=" + staticAccessPath + ", linuxUploadFolder=" + linuxUploadFolder
				+ ", windowsUploadFolder=" + windowsUploadFolder + "]";
	}
    
	/** @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(uploadFolder);
        //文件最大
        factory.setMaxFileSize("10MB");
        // 设置总上传数据总大小
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    } **/
}
