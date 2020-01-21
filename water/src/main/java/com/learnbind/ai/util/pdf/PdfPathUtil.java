package com.learnbind.ai.util.pdf;

import java.io.File;
import java.util.Date;

import com.learnbind.ai.base.common.DateUtil;

public class PdfPathUtil {

	/**
	 * @return	
	 */
	public static String getFTPPath(String uploadFolder) {
		
		String ymd = DateUtil.getYYYYMMDDDate(new Date());//返回YYYYMMDD格式的日期
		
		uploadFolder = uploadFolder + File.separator+"pdf"+File.separator+ymd+File.separator;
		return uploadFolder;
	}
	
}
