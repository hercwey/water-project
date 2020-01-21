package com.learnbind.ai.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     MyXMLWorkerHelper.java 
*	@Description   HTML转换为PDF  HTML2PDF  
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月19日 上午10:39:18 
*/
public class MyXMLWorkerHelper {
    /**
    *	@Copyright (c) 2018 by Hz
    *	@ClassName     MyXMLWorkerHelper.java
    *	@Description   用于解决PDF中无法显示中文的问题. 
    * 
    *	@author        lenovo
    *	@version       V1.0  
    *	@Date          2018年12月20日 上午10:47:07 
    */
    public static class MyFontsProvider extends XMLWorkerFontProvider {
    	/** 
    	*	@Fields FONT_NAME 默认字体为宋体. 
    	*/
    	private static final String FONT_NAME="宋体"; 
    	
        public MyFontsProvider() {
            super(null, null);
        }

        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {
            String fntname = fontname;
            if (fntname == null) {
                fntname = FONT_NAME;
            }
            return super.getFont(fntname, encoding, size, style);
        }
    }

    /** 
     *	@Title parseToElementList 
     *	@Description 根据HTML字符串生成iText Element列表.
     * 
     *	@param html  HTML字符串
     *	@param css	 CSS字符串
     *	@return	iTextElement列表,可以写入PDF中
     *	@throws IOException
     *     
     *	@Return ElementList    返回类型 
     *
     *	@Date 2018年12月20日 上午10:42:47
    */
    public static ElementList parseToElementList(String html, String css) throws IOException {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        if (css != null) {
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
            cssResolver.addCss(cssFile);
        }

        // HTML
        MyFontsProvider fontProvider = new MyFontsProvider();
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);

        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

        // XML Worker
        XMLWorker worker = new XMLWorker(cssPipeline, true);
        XMLParser p = new XMLParser(worker);
        html = html.replace("<br>", "").replace("<hr>", "").replace("<img>", "").replace("<param>", "")
                .replace("<link>", "");
        p.parse(new ByteArrayInputStream(html.getBytes()));

        return elements;
    }

}
