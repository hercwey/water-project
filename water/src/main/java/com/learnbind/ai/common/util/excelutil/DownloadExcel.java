package com.learnbind.ai.common.util.excelutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.pivas.common.util.excelutil
 *
 * @Title: DownloadExcel.java
 * @Description: 下载EXCEL
 *
 * @author Administrator
 * @date 2019年3月2日 下午2:07:34
 * @version V1.0
 *
 */
public class DownloadExcel {

	private static final Log log = LogFactory.getLog(DownloadExcel.class);
	
	/**
	 * @Title: download
	 * @Description: EXCEL下载
	 * @param request
	 * @param response
	 * @param wb 
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, String excelName, HSSFWorkbook wb) {

		// 获取导出EXCEL的文件路径
		String realPath = this.getExcelRealPath(request);
		// 获取导出EXCEL的文件名
		if(StringUtils.isBlank(excelName)) {
			excelName = this.getExcelFileName();
		}
		

		File file = new File(realPath + excelName);

		// 文件输出流
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			wb.write(outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		log.info("导出2007文件成功！文件导出路径：--" + file);

		try {
			DownLoadFileUtil.downLoad(excelName, "xls", realPath + excelName, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 获取文件路径
	 * 
	 * @param request
	 * @return
	 */
	private String getExcelRealPath(HttpServletRequest request) {
		String realPath = FileUploadUtil.getRealPath(request);
		realPath = realPath + File.separator + "export excel" + File.separator + "company consumer" + File.separator;

		File temp = new File(realPath);
		// 如果文件路径不存在，则创建
		if (!temp.exists()) {
			temp.mkdirs();
		}
		return realPath;
	}

	/**
	 * 获取导出销售指标EXCEL文件名
	 * 
	 * @return
	 */
	public String getExcelFileName() {

		String name = "EXCEL";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String times = sdf.format(new Date());

		// excel文件名
		String fileName = name + times + ".xls";
		return fileName;
	}

}
