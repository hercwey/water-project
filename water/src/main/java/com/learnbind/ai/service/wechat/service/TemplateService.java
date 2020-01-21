package com.learnbind.ai.service.wechat.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.service.wechat.constant.WxTemplateConstant;
import com.learnbind.ai.service.wechat.task.notify.WxMsgNotifyTask;
import com.learnbind.ai.service.wechat.task.thread.NotifyThread;
import com.learnbind.ai.util.wx.template.Data;
import com.learnbind.ai.util.wx.template.Template;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.service
 *
 * @Title: TemplateService.java
 * @Description: 模板消息-服务
 *
 * @author lenovo
 * @date 2019年8月14日 下午10:43:49
 * @version V1.0 
 *
 */
@Service
public class TemplateService {

	private static final Log log = LogFactory.getLog(TemplateService.class);

	@Resource
	private WechatService wechatService;
	@Resource
	private NotifyThread notifyThread;

	public static final String TEMPLATEFONTCOLOR = "#173177";

	//-----------------------水费催缴模版------------------------------------------------------------------------------------------------------
	/**
	 * @Title: sendWaterFeeNofity
	 * @Description: 水费催缴模版
	 * @param openid
	 * @param moneySumYuan
	 * @param accountItem 
	 */
	public void sendWaterFeeNofity(String openid, String customerName, BigDecimal waterFeeAmount) {
		
		try {
			
			String accessToken = wechatService.getAccessToken();
			String templateId = WxTemplateConstant.PAY_WATER_FEE_NOTIFY; //可根据微信公众号模板消息相关文档中查看ID并自定义常量
			
			Template temp = this.createWaterFeeNofityTemp(customerName, waterFeeAmount, openid, templateId);
			sendTemplate(accessToken, temp);
		} catch (Exception e) {
			log.error("error", e);
		}
	}
	
	/**
	 * @Title: createWaterFeeNofityTemp
	 * @Description: 创建水费通知模版
	 * @param customerName
	 * @param waterFeeAmount
	 * @param openid
	 * @param templateId
	 * @return 
	 */
	private Template createWaterFeeNofityTemp(String customerName, BigDecimal waterFeeAmount, String openid, String templateId) {
//		{{first.DATA}}
//		客户姓名：{{keyword1.DATA}}
//		待缴总金额：{{keyword2.DATA}}
//		{{remark.DATA}}
		Template temp = new Template();
		Map<String, Data> map = new HashMap<String, Data>();
		map.put("first", new Data("尊敬的客户，截止今日，您存在待缴费用！", TEMPLATEFONTCOLOR));
		map.put("keyword1", new Data(customerName, TEMPLATEFONTCOLOR));
		map.put("keyword2", new Data(waterFeeAmount.toString(), TEMPLATEFONTCOLOR));
		map.put("Remark", new Data("点击查看费用详情，请您尽快缴纳！", TEMPLATEFONTCOLOR));

		temp.setTouser(openid);
		temp.setUrl(WxTemplateConstant.TEMPLATE_URL);   //点击通知时页面跳转的URL
		temp.setTemplate_id(templateId);
		temp.setData(map);
		log.debug("水费缴费提醒模板消息" + temp);
		return temp;
	}
	
	//-----------------------------水费支付成功模版---------------------------------------------------------------------------------
	/**
	 * 发送支付成功模板消息
	 * 
	 * @param openId
	 * @param moneySumYuan
	 * @param productName
	 */
	public void sendPayTemplate(String openid, String moneySumYuan, CustomerAccountItem accountItem) {
		try {
			
			String accessToken = wechatService.getAccessToken();
			
			String templateId = null; //可根据微信公众号模板消息相关文档中查看ID并自定义常量
			
			//TODO 此处需要确认
			//CustomerAccountItem accountItem = new CustomerAccountItem();
			Template temp = createPayOkTemp(moneySumYuan, accountItem.getPeriod(), openid, templateId);

			sendTemplate(accessToken, temp);
		} catch (Exception e) {
			log.error("error", e);
		}
	}
	
	public void sendOrderOkTemplate(String openid,CustomerAccountItem accountItem) {
		try {
			String accessToken = wechatService.getAccessToken();
			String templateId = null;//可根据设置自定义常量
			
			//CustomerAccountItem accountItem = new CustomerAccountItem();

			Template temp = createOrderOkTemp(openid, accountItem, templateId);
			sendTemplate(accessToken, temp);
		} catch (Exception e) {
			log.error("error", e);
		}
	}
	
	/**
	 * 发送模板消息
	 * 
	 * @param accessToken
	 * @param temp
	 */
	public void sendTemplate(String accessToken, Template temp) {
		WxMsgNotifyTask task = new WxMsgNotifyTask(accessToken, temp);
		notifyThread.offerTask(task);
	}
	

	/**
	 * @Title: createPayOkTemp
	 * @Description: 创建支付成功模板
	 * @param moneySumYuan
	 * @param period
	 * @param openid
	 * @param templateId
	 * @return 
	 */
	public Template createPayOkTemp(String moneySumYuan, String period, String openid, String templateId) {
		Template temp = new Template();
		Map<String, Data> map = new HashMap<String, Data>();
		map.put("first", new Data("恭喜您，支付成功！", TEMPLATEFONTCOLOR));
		map.put("orderMoneySum", new Data(moneySumYuan + "元", TEMPLATEFONTCOLOR));
		map.put("orderProductName", new Data(period=" 的水费已缴清", TEMPLATEFONTCOLOR));
		map.put("Remark", new Data("感谢您的支持！", TEMPLATEFONTCOLOR));

		temp.setTouser(openid);
		temp.setUrl("http://127.0.0.1/");   //点击通知时页面跳转的URL
		temp.setTemplate_id(templateId);
		temp.setData(map);
		if (log.isDebugEnabled()) {
			log.debug("支付成功模板消息" + temp);
		}
		return temp;
	}

	/**
	 * @Title: createOrderOkTemp
	 * @Description: 创建水费账单模板
	 * @param openid
	 * @param accountItem
	 * @param templateId
	 * @return 
	 */
	public Template createOrderOkTemp(String openid, CustomerAccountItem accountItem,String templateId) {
		Template temp = new Template();
		
		Map<String, Data> map = new HashMap<String, Data>();
		//TODO 需要自定义
//		map.put("first", new Data("尊敬的用户", TEMPLATEFONTCOLOR));
//		map.put("OrderSn", new Data(order.getId(), TEMPLATEFONTCOLOR));
//		map.put("OrderStatus", new Data(order.getStatestr(), TEMPLATEFONTCOLOR));
//		
//		map.put("remark", new Data("您的水费账单已生成", TEMPLATEFONTCOLOR));
		temp.setUrl("http://127.0.0.1/");
		
		
		temp.setTouser(openid);
		temp.setTemplate_id(templateId);
		temp.setData(map);
		if(log.isDebugEnabled()){
			log.debug("订单更新模板消息" + temp);
		}
		return temp;
		
	}
	
}
