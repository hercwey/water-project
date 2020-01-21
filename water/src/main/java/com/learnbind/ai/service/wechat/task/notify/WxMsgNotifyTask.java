package com.learnbind.ai.service.wechat.task.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.util.wx.WxTemplateUtils;
import com.learnbind.ai.util.wx.template.Template;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.task.notify
 *
 * @Title: WxMsgNotifyTask.java
 * @Description: 微信消息通知任务
 *
 * @author Administrator
 * @date 2019年8月2日 下午12:03:36
 * @version V1.0 
 *
 */
public class WxMsgNotifyTask {
	
	private Template template;
	private String accessToken;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public WxMsgNotifyTask(String accessToken, Template template) {
		this.accessToken = accessToken;
		this.template = template;
	}
	
	public void exec() {
		
		try {
			JSONObject obj = WxTemplateUtils.sendTemplate(accessToken, template);
			logger.info("发送模版消息返回结果："+obj.toJSONString()); 
		} catch (Exception e) {
			logger.error("err", e);
		}
	}

}
