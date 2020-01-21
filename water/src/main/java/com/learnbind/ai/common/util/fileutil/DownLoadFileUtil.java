package com.learnbind.ai.common.util.fileutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.ai.upload.utils
 *
 * @Title: DownLoadFileUtil.java
 * @Description: 文件下载工具类
 *
 * @author Administrator
 * @date 2019年5月23日 下午6:42:18
 * @version V1.0 
 *
 */
public class DownLoadFileUtil {
	
	private static final Log log = LogFactory.getLog(DownLoadFileUtil.class);
	
	/**
	 * @Title: getFileRealPath
	 * @Description: 获取文件的真实路径
	 * @param request
	 * @param filePath
	 * 		文件相对路径（/upload/gen/attached/20170116/2017011614005634889063.pdf）
	 * @return 
	 */
	public static String getFileRealPath(HttpServletRequest request, String filePath){
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String temp = realPath.substring(0, realPath.lastIndexOf(File.separator));
			temp = temp.substring(0, temp.lastIndexOf(File.separator));
			return temp + filePath;
		} catch (Exception e) {
			log.error("获取文件的真实路径异常", e);
		}
		return null;
	}
	
	/**
	 * @Title: downLoad
	 * @Description: 下载文件
	 * @param fileName
	 * 		文件名（可以自定义文件名，下载后的文件名与此文件名相同，注：要加扩展名，如：下载文件PDF.pdf）
	 * @param fileType
	 * 		文件类型（扩展名）
	 * @param realPath
	 * 		文件真实路径（D:\Tomcat\apache-tomcat-8.5.8\webapps\gwkj-back\/upload/gen/attached/20170116/2017011614005634889063.pdf）
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public static boolean downLoad(String fileName, String fileType, String realPath, HttpServletResponse response)
			throws Exception {
		
		boolean flag = false;//下载状态
		
		OutputStream os = response.getOutputStream();// 取得输出流
		response.reset();// 清空输出流
		
		File file = new File(realPath); // 根据文件路径获得File文件
		// 设置文件类型
		if ("pdf".equals(fileType)) {
			response.setContentType("application/pdf;charset=UTF-8");
		} else if ("csv".equals(fileType)) {
			response.setContentType("application/msexcel;charset=UTF-8");
		} else if ("doc".equals(fileType)) {
			response.setContentType("application/msword;charset=UTF-8");
		} else if ("xls".equals(fileType)) {
			response.setContentType("application/msexcel;charset=UTF-8");
		}else if("apk".equals(fileType)){
			response.setContentType("application/vnd.android.package-archive;charset=UTF-8");
		}else {
			response.setContentType("application/octet-stream");
		}
		// 文件名
		response.setHeader("Content-Disposition",
				"attachment;filename=\"" + new String(fileName.getBytes(), "ISO8859-1") + "\"");
		// + new String(fileName.getBytes("gb2312"), "ISO8859-1" ) + "\"");
		response.setContentLength((int) file.length());
		byte[] buffer = new byte[4096];// 缓冲区
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(os);
			input = new BufferedInputStream(new FileInputStream(file));
			int n = -1;
			// 遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush(); // 不可少
			response.flushBuffer();// 不可少
			flag = true;//下载状态
		} catch (Exception e) {
			e.printStackTrace();
			// 异常自己捕捉
		} finally {
			// 关闭流，不可少
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			
		}
		return flag;
	}
}
