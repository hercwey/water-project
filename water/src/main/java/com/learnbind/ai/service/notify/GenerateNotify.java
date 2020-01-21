package com.learnbind.ai.service.notify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.util.pdf.PDFGenerator;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.notify
 *
 * @Title: GenerateNotify.java
 * @Description: 生成通知单
 *
 * @author Thinkpad
 * @date 2019年12月13日 下午3:08:10
 * @version V1.0 
 *
 */
@Component
public class GenerateNotify {
	

	//生成PDF文件名
	private String buildPDFFileName(String filePath,String serialNo,String period) {
		final String SPLIT_CHAR="_";
		final String PDF_EXT=".pdf";
		String fileName=filePath + period+SPLIT_CHAR+serialNo+PDF_EXT;
		
		File path =new File(filePath);    
		//如果文件夹不存在则创建    
		if (!path.exists() && !path.isDirectory()) {
			boolean flag = path.mkdirs();
			System.out.println("目录不存在   创建目录: "+path+" 结果："+flag);
		} else {
			System.out.println("目录存在 : "+path);
		}
		
		return fileName;
	}
	
	
	
	//*******************************************大表通知单打印（按客户）****************************************************
	public String generateNotifyPDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,Map<String, Object> customerMap, String serialNo, String period) throws DocumentException, IOException {
		//生成通知单
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", customerMap);
		//(3)水费通知单PDF
		//(3.2)PDF文件名称
		String pdfFileName = buildPDFFileName(FILE_DIR,serialNo,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	
	//*******************************************水表报废单****************************************************
	public String meterScarpBatchPDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,List<Map<String, Object>> meterList, String serialNo, String period) throws DocumentException, IOException {
		//生成通知单
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", meterList);
		//(3)水费通知单PDF
		//(3.2)PDF文件名称
		String pdfFileName = buildPDFFileName(FILE_DIR,serialNo,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	
	

}
