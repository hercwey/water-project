package com.learnbind.ai.util.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     HTML2PDF.java
*	@Description   根据HTML生成PDF对象.  暂时未用.
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月20日 下午3:49:43 
*/
public class HTML2PDF{

	/** 
	 *	@Title html2pdf 
	 *	@Description 根据HTML生成PDF
	 * 
	 *	@param htmlFileName	HTML文件名称
	 *	@param pdfFileName	PDF文件名称
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月20日 下午3:45:49
	*/
	public void html2pdf(String htmlFileName,String pdfFileName,String cssFileName) {
		//创建PDF文档对象
		Document document = new Document();
		try {
			//在此处并未进行文件是否存在的判定.
			FileOutputStream file = new FileOutputStream(new File(pdfFileName));
			PdfWriter writer = PdfWriter.getInstance(document, file);			
			document.open();
			
			//读取HTML文件内容并置于字符串中.			
			String htmlFile=htmlFileName;
			StringBuilder htmlStr = new StringBuilder();
			htmlStr.append("");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
			String t = "";
			while ((t = br.readLine()) != null) {
				//System.out.println(t);
				//htmlStr=htmlStr+t;
				htmlStr.append(t);
			}
			br.close();
			
			//读取css文件内容			
			StringBuilder cssStr = new StringBuilder();
			cssStr.append("");
			//String cssStr="";
			//读取css文件内容并置于字符串中.			
			if(!StringUtils.isBlank(cssFileName)) {
				String cssFile=cssFileName;			
				BufferedReader brcss = new BufferedReader(new InputStreamReader(new FileInputStream(cssFile), "UTF-8"));
				String tcss = "";
				while ((tcss = brcss.readLine()) != null) {
					cssStr.append(tcss);
					//cssStr=cssStr+tcss;
				}
				brcss.close();				
			}
			
			//将HTML的内容转换到PDF中.
			Paragraph context = new Paragraph();			
			ElementList elementList =MyXMLWorkerHelper.parseToElementList(htmlStr.toString(), cssStr.toString());
			for (Element element : elementList) {
			    context.add(element);
			}
			document.add(context);
			
			document.close();
			
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		finally {
			//document.close();
		}
		
		
	}
	
}
