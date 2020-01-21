package com.learnbind.ai.controller.step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/step")
public class StepController {
	private static Log log = LogFactory.getLog(StepController.class);	 
	private static final String TEMPLATE_PATH = "step"; // 页面目录
	
	@RequestMapping(value = "/test")	
	public String testStep(Model model) {
		return TEMPLATE_PATH+"/test/steptest";
		
	}
	
	//ystep测试
	@RequestMapping(value = "/ysteptest")	
	public String ysteptest(Model model) {
		return TEMPLATE_PATH+"/ystep/ysteptest";
		
	}
	
	
}
