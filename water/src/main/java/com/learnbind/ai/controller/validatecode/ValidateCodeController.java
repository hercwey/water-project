package com.learnbind.ai.controller.validatecode;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.common.validatecode.RandomValidateCode;
import com.learnbind.ai.common.validatecode.ValidateCode;

@Controller
public class ValidateCodeController {

	private static Log log = LogFactory.getLog(ValidateCodeController.class);
	
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping("/back/getValidateCode")
	public void getValidateCode(HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);

		RandomValidateCode randomValidateCode = new RandomValidateCode();
		try {
			randomValidateCode.getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取运算验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping("/back/getOperationValidateCode")
	public void getOperationValidateCode(HttpServletRequest request, HttpServletResponse response) {
		
		//调用生成图片验证码类
		ValidateCode vcode = new ValidateCode(150,40,5,50);
		try {
			//写图片
			vcode.write(response.getOutputStream());
			//获取图片验证码计算后的值
			String code = vcode.getCode();
			request.getSession().setAttribute(ValidateCode.VALIDATE_CODE_SESSION_KEY, code);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
