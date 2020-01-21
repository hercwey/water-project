package com.learnbind.ai.controller.pdfviewer;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.util.pdf.PdfPathUtil;

@Controller
public class PDFViewer {
	/** 
	*	@Fields logger  logger对象 
	*/
	private static final Log logger=LogFactory.getLog(PDFViewer.class);

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	
	/**
     * 	预览pdf文件
     * 	@param fileName
     */
    @RequestMapping(value = "/preview")
    public void pdfStreamHandler(String fileName,HttpServletRequest request,HttpServletResponse response) {
    	System.out.print(fileName);
    	//final String FILE_DIR="src/main/resources/templates/bottlelabel//bottlelabelpdf/";
    	String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());//TODO
    	String pdfFileName=FILE_DIR+fileName;
        File file = new File(pdfFileName);
        if (file.exists()){
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                response.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                logger.error("pdf文件处理异常：" + e.getMessage());
            }

        }else{
            return;
        }
    }

}
