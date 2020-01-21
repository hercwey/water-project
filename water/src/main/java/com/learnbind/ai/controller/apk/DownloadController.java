package com.learnbind.ai.controller.apk;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.apk
 *
 * @Title: DownloadController.java
 * @Description: 下载前端控制器
 *
 * @author Administrator
 * @date 2019年10月29日 下午4:56:11
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/download")
public class DownloadController {
	
	private static Log log = LogFactory.getLog(DownloadController.class);
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	
	/**
	 * @Title: downloadApk
	 * @Description: 下载APK
	 * @param model
	 * @param request
	 * @param response 
	 */
	@RequestMapping(value = "/apk")
	public void downloadApk(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			
			log.debug("----------下载APK----------");
			
			String path = uploadFileConfig.getUploadFolder();
			String filePath = path+File.separator+"app"+File.separator+"app-aiwater.apk";
			
			String fileName = "app-aiwater.apk";
			//String filePath = "D:\\upload\\app\\app-aiwater.apk";
			DownLoadFileUtil.downLoad(fileName, "apk", filePath, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}