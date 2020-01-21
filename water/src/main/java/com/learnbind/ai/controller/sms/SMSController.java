package com.learnbind.ai.controller.sms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.controller.settleaccount.SettleAccountController;
import com.learnbind.ai.sms.CallbackResponseParams;
import com.learnbind.ai.sms.SMSConstants;
import com.learnbind.ai.sms.SMSService;
import com.learnbind.ai.sms.SendMultSMResponse;
import com.learnbind.ai.sms.SendResultDetail;
import com.learnbind.ai.sms.SendSingleSMResponse;

@Controller
@RequestMapping(value = "/sms")
public class SMSController {
	private static Log log = LogFactory.getLog(SettleAccountController.class);
	//private static final String TEMPLATE_PATH = ""; // 页面目录
	
	@Autowired 
	SMSService smsService;
	
	/**
	 * @Title: sendSingle
	 * @Description: 单发短信
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/sendsingle")
	@ResponseBody
	public String sendSingle(Model model) {		
		//(1)短信模板值		
		long tpl_id=SMSConstants.SMS_TEMPLATE_ID_FEE;  //模板ID
		//(2)模板中参数,模板的定义在Tencent Cloud中进行配置,
		//某个模板的样式如下所示: {1}您好,您的{2}水费为{3}元,请及时缴费以免影响正常用水
		//每个{}表示一个参数.在设置参数时顺序同模板中的顺序
		List<String> tplParms=new ArrayList<>();   
		tplParms.add("宋瑞冬");       	//姓名
		tplParms.add("2019年8月份");		//水费期间
		tplParms.add("88万");			//水费值
		
		//(3)电话号码
		//String mobileNo="15831442733";
		String mobileNo="15383010505";
		
		//(4)发送短信
		String response=smsService.sendSingleSMS(mobileNo,tpl_id,tplParms);
		log.debug("单发短信时,短信服务器返回:"+response);
		log.debug("短信发送成功,短信模板为:"+tpl_id);
		//(5)解析响应参数
		SendSingleSMResponse singleResponse=JSON.parseObject(response,SendSingleSMResponse.class);
		
		//(6)响应前台请求.
		String result=JSON.toJSONString(RequestResultUtil.getResultSuccess("发送短信成功"));
		return result;
	}
	
	
	
	/**
	 * @Title: sendGroup
	 * @Description: 群发短信
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/sendmult")
	@ResponseBody
	public Object sendMult(Model model) {
		//(1)短信模板值		
		long tpl_id=SMSConstants.SMS_TEMPLATE_ID_FEE;  //模板ID
		//(2)模板中参数,模板的定义在Tencent Cloud中进行配置,
		//某个模板的样式如下所示: {1}您好,您的{2}水费为{3}元,请及时缴费以免影响正常用水
		//每个{}表示一个参数.在设置参数时顺序同模板中的顺序
		List<String> tplParms=new ArrayList<>();   
		tplParms.add("用水户");       	//姓名
		tplParms.add("2019年8月份");		//水费期间
		tplParms.add("0.05");			//水费值
		
		//(3)电话号码列表(多个)
		List<String> mobileNoList=new ArrayList<>();
		mobileNoList.add("15383010505");
		mobileNoList.add("15831442733");		
		//(4)发送短信
		String response=smsService.sendMultSMS(mobileNoList,tpl_id,tplParms);
		log.debug("群发短信,短信服务器返回:"+response);
		log.debug("短信发送成功,短信模板为:"+tpl_id);
		//(5)解析响应参数
		SendMultSMResponse multResponse=JSON.parseObject(response,SendMultSMResponse.class);
		
		//(6)响应前台请求.
		String result=JSON.toJSONString(RequestResultUtil.getResultSuccess("发送短信成功"));
		return result;		
	}
	
	
	
	/**
	 * @Title: callbackFunc
	 * @Description: SMS Server回调处理  (回调函数的配置需在Tencent Clound进行配置)
	 * @param parm  JSON格式参数,具体参见 SendResultDetail 定义
	 * @param model
	 * @return   CallbackResponseParams 类型的JSON格式 
	 * 	TODO 注:此函数暂时未测试,由于需要外网服务器地址.可上传阿里云后进行测试.
	 */
	@RequestMapping(value = "/callback")
	@ResponseBody
	public Object callbackFunc(@RequestParam String parm, Model model) {
		//由SMS Server发送过来,说明短信的发送情况.
		//(1)解析发送结果参数
		log.debug("回调函数,SMS 短信服务器回调时发送的参数为:"+parm);
		List<SendResultDetail>  sendResultList= JSON.parseArray(parm, SendResultDetail.class);
		//(2)对解析后的结果进行处理
		//TODO  ADD CODE HERE
		
		//(3)回调函数响应
		CallbackResponseParams parms=new  CallbackResponseParams();
		parms.setResult(CallbackResponseParams.SUCCESS);
		parms.setErrmsg("");		
		return JSON.toJSONString(parms);
	}
	
}
