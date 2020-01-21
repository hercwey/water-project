package com.learnbind.ai.util.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

/*import com.lowagie.text.pdf.BaseFont;*/
import com.itextpdf.text.pdf.BaseFont;


/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     FileTypeConvertUtil.java
*	@Description   HTML转PDF  图片路径采用相对路径. 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月26日 上午10:32:19 
*/
public class FileTypeConvertUtil {
	    /**
	     * 将HTML转成PD格式的文件。html文件的格式比较严格
	     * @param htmlFile
	     * @param pdfFile
	     * @throws Exception
	     */
	    // <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd ">
	    public static void html2pdf(String htmlFile, String pdfFile) throws Exception {
	        // step 1
	        String url = new File(htmlFile).toURI().toURL().toString();
	        System.out.println(url);
	        // step 2
	        OutputStream os = new FileOutputStream(pdfFile);
	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocument(url);
	 
	        // step 3 解决中文支持
	        ITextFontResolver fontResolver = renderer.getFontResolver();
	        if("linux".equals(getCurrentOperatingSystem())){
	            fontResolver.addFont("/usr/share/fonts/chiness/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);  
	        }else{
	            fontResolver.addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	        }
	 
	        renderer.layout();
	        renderer.createPDF(os);
	        os.close();
	 
	        System.out.println("create pdf done!!");
	 
	    }
	 
	    public static String getCurrentOperatingSystem(){
	        String os = System.getProperty("os.name").toLowerCase();
	        System.out.println("---------当前操作系统是-----------" + os);
	        return os;
	    }
	    
	    
	    public static void main(String[] args) {
	    	final String FILE_DIR="src/main/resources/templates/bottlelabel/";
//	        String htmlFile = "/home/lbj/sign.jsp";
//	        String pdfFile = "/home/lbj/sign.pdf";        
	        String htmlFile = FILE_DIR+"/bottlelabelhtml/bottlelabelhtml1.html";
	        String pdfFile = "D:/temp/word2pdf/sign.pdf";        
	        try {
	            FileTypeConvertUtil.html2pdf(htmlFile, pdfFile);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
