package com.learnbind.ai.controller.uedit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;

@Controller
@RequestMapping(value = "/ueditor/")
public class UEditorController {
	
	private static final Logger logger = LoggerFactory.getLogger(UEditorController.class);
	
	@RequestMapping(value = "action")
	public void getConfig(Model model,HttpServletRequest request,HttpServletResponse response) throws IOException {
		request.setCharacterEncoding( "utf-8" );
		response.setHeader("Content-Type" , "text/html");	
		ClassPathResource classPathResource = new ClassPathResource("/static/tools");
		String tempStr=classPathResource.getURI().toString();
		
		String rootPath=tempStr.replace("file:/", "");
		
		try {			
			response.getWriter().write(new ActionEnter( request, rootPath ).exec());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String xxx="\u65e0\u6548\u7684";
		System.out.println(xxx);
	}
	
	
	
}
