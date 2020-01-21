package com.learnbind.ai.util.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     CreateLabel.java
*	@Description   模拟测试生成瓶签PDF 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月20日 下午4:52:16 
*/
@Component
public class CreateLabel {
	
	@Autowired
	private MeterBookService meterBookService;//表册
	@Autowired
	private MeterRecordService meterRecordService;//地理位置
	@Autowired
	private CustomersService customersService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService;
	/** 
	 *	@Title create 
	 *	@Description 生成瓶签
	 * 
	 *	@throws DocumentException
	 *	@throws IOException
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月26日 上午11:04:52
	*/
	
	public String createSingleBottleLabelPDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,List<Map<String, Object>> recordMapList, String locationName, String period, String pyode) throws DocumentException, IOException {
		//final String FILE_DIR = "src/main/resources/templates/bottlelabel/";  //PDF所在
		//(2)生成瓶签数据(上下文)
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", recordMapList);
		contextData.put("locationName", locationName);
		contextData.put("period", period);
		
		//(3)水费通知单PDF
		//(3.1)模板文件
		//final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		//(3.2)PDF文件名称
		String pdfFileName = buildPDFFileName(FILE_DIR,pyode,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	/**
	 * @Title: createCustomerPeoplePDF
	 * @Description: 打印小区水费通知单
	 * @param FILE_DIR
	 * @param TEMPLATE_PREFIX
	 * @param templateFileName
	 * @param recordMapList
	 * @param locationName
	 * @param period
	 * @param pyode
	 * @param preDate
	 * @param currDate
	 * @return
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public String createCustomerPeoplePDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,List<Map<String, Object>> recordMapList, String locationName, String period, String pyode, String preDate, String currDate, String nextPeriod) throws DocumentException, IOException {
		//final String FILE_DIR = "src/main/resources/templates/bottlelabel/";  //PDF所在
		//(2)生成瓶签数据(上下文)
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", recordMapList);
		contextData.put("locationName", locationName);
		contextData.put("nextPeriod", nextPeriod);
		contextData.put("period", period);
		contextData.put("preDate", preDate);
		contextData.put("currDate", currDate);
		
		//(3)水费通知单PDF
		//(3.1)模板文件
		//final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		//(3.2)PDF文件名称
		String pdfFileName = buildPDFFileName(FILE_DIR,pyode,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	
	/**
	 * @Title: createBatchCompanyNoticePDF
	 * @Description: 批量打印通知单
	 * @param FILE_DIR
	 * @param TEMPLATE_PREFIX
	 * @param templateFileName
	 * @param recordMapList
	 * @param locationName
	 * @param period
	 * @param pyode
	 * @param totalAmount
	 * @param totalWaterFee
	 * @return
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public String createBatchCompanyNoticePDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,List<Map<String, Object>> recordMapList, String locationName, String period) throws DocumentException, IOException {
		//final String FILE_DIR = "src/main/resources/templates/bottlelabel/";  //PDF所在
		//(2)生成瓶签数据(上下文)
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", recordMapList);
		contextData.put("locationName", locationName);
		contextData.put("period", period);
		
		//(3)水费通知单PDF
		//(3.1)模板文件
		//(3.2)PDF文件名称
		if(StringUtils.isBlank(locationName)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = sdf.format(new Date());
			locationName = times+"_"+"通知单";
		}
		String pdfFileName = buildPDFFileName(FILE_DIR,locationName,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	/**
	 * @Title: createSinglePriviewNoticePDF
	 * @Description: 单个预览生成pdf文件
	 * @param FILE_DIR
	 * @param TEMPLATE_PREFIX
	 * @param templateFileName
	 * @param recordMapList
	 * @param locationName
	 * @param period
	 * @param pyode
	 * @return
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public String createSinglePriviewNoticePDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,List<Map<String, Object>> recordMapList, String customerName, String period) throws DocumentException, IOException {
		//(2)生成瓶签数据(上下文)
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", recordMapList);
		contextData.put("customerName", customerName);
		contextData.put("period", period);
		
		//(3)水费通知单PDF
		//(3.1)模板文件
		//(3.2)PDF文件名称
		if(StringUtils.isBlank(customerName)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = sdf.format(new Date());
			customerName = times+"_"+"通知单";
		}
		String pdfFileName = buildPDFFileName(FILE_DIR,customerName,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	//生成PDF文件名
	private String buildPDFFileName(String filePath,String pyode,String period) {
		//final String FILE_DIR = "src/main/resources/templates/bottlelabel/";  //PDF所在
		final String PDF_DIR="/bottlelabelpdf/";
		final String SPLIT_CHAR="_";
		final String PDF_EXT=".pdf";
		//String fileName=filePath + PDF_DIR+bottleLabelSerialNo+SPLIT_CHAR+pageNo+PDF_EXT;
		String fileName=filePath + pyode+SPLIT_CHAR+period+PDF_EXT;
		
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
	public String createCustomerNoticePDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName,List<Map<String, Object>> recordMapList, String locationName, String period) throws DocumentException, IOException {
		//final String FILE_DIR = "src/main/resources/templates/bottlelabel/";  //PDF所在
		//(2)生成瓶签数据(上下文)
		Map<String, Object> contextData = new HashMap<>();
		contextData.put("textData", recordMapList);
		contextData.put("locationName", locationName);
		contextData.put("period", period);
		
		//(3)水费通知单PDF
		//(3.1)模板文件
		//final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		//(3.2)PDF文件名称
		String pdfFileName = buildPDFFileName(FILE_DIR,locationName,period);   	
		//(3.3)生成PDF
		PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
		gen.generate(new File(pdfFileName), templateFileName, contextData);

		return pdfFileName;
	}
	
	

}
