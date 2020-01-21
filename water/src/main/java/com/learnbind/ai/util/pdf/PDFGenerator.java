package com.learnbind.ai.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     PDFGenerator.java
*	@Description   HTML生成PDF, 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月26日 上午10:33:14 
*/
public class PDFGenerator {
	private ClassLoaderTemplateResolver templateResolver;
	private TemplateEngine templateEngine;

	/** 
	*	<p>Title </p> 
	*	<p>Description </p> 
	*	@param templatePrefix  模板文件所在路径前缀
	*	@param templateSuffix  模板文件扩展名
	*/
	public PDFGenerator(final String templatePrefix,
						final String templateSuffix){
		this(templatePrefix, templateSuffix, "HTML5", "UTF-8");
	}

	/** 
	*	<p>Title </p> 
	*	<p>Description </p> 
	*	@param templatePrefix  	模板文件所在路径前缀(路径)
	*	@param templateSuffix	模板文件扩展名
	*	@param templateMode		模板文件类型
	*	@param templateEncoding 模板文件编码
	*/
	public PDFGenerator(final String templatePrefix,
						final String templateSuffix,
						final String templateMode,
						final String templateEncoding){

		this(new ClassLoaderTemplateResolver());

		this.templateResolver.setPrefix(templatePrefix);
		this.templateResolver.setSuffix(templateSuffix);
		this.templateResolver.setTemplateMode(templateMode);
		this.templateResolver.setCharacterEncoding(templateEncoding);
	}

	/** 
	*	<p>Title </p> 
	*	<p>Description </p> 
	*	@param classLoaderTemplateResolver 
	*/
	public PDFGenerator(ClassLoaderTemplateResolver classLoaderTemplateResolver){
		this.templateResolver = classLoaderTemplateResolver;
	}

	/** 
	*	<p>Title </p> 
	*	<p>Description </p> 
	*	@param templateEngine 
	*/
	public PDFGenerator(TemplateEngine templateEngine){
		this.templateEngine = templateEngine;
	}

	private TemplateEngine getTemplateEngine() {
		if(templateEngine == null){
			templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(templateResolver);
		}

		return templateEngine;
	}

	/**
	 * Process the template and generate a PDF of this rendered template.
	 *
	 * @param ouputPDF Target pdf file.
	 * @param template Source template.
	 * @param model The data for the template.
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public void generate(File ouputPDF, String template, Map<String, Object> model) throws DocumentException, IOException {
		final Context ctx = new Context();
	    ctx.setVariables(model);
	    

	    //thymeleaf 解析引擎
	    final TemplateEngine templateEngine = getTemplateEngine();	    
	    String htmlContent = templateEngine.process(template, ctx);

	    //PDF渲染器,用于根据HTML生成PDF.
	    ITextRenderer renderer = new ITextRenderer();
	    ITextFontResolver fontResolver = renderer.getFontResolver(); 
	    try {
			//fontResolver.addFont("templates/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    	//fontResolver.addFont("宋体", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    	//fontResolver.addFont("c:/Windows/Fonts/SimSun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    	//fontResolver.addFont("C:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    	fontResolver.addFont("/print/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    	//fontResolver.addFont("C:/Windows/Fonts/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    	//fontResolver.addFont("C:/Windows/Fonts/simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    // 解决图片的相对路径问题      
        //renderer.getSharedContext().setBaseURL("file:/C:/Users/Administrator.WIN7-1610080938/Desktop/word2pdf/");
	    
	    //renderer.setScaleToFit(true);
	    renderer.setDocumentFromString(htmlContent);
	    renderer.layout();
	    renderer.createPDF(new FileOutputStream(ouputPDF));
	    
	}
}