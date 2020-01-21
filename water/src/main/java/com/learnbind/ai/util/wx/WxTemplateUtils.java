package com.learnbind.ai.util.wx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.util.wx.template.SendAllBase;
import com.learnbind.ai.util.wx.template.Template;

/**
 * 模板消息
 * @author srd
 *
 */
public class WxTemplateUtils {
	
	private static Log log = LogFactory.getLog(WxTemplateUtils.class);
	
	/**
	 * @Fields TEMPLATE_SETINDUSTRY_URL：设置所属行业
	 */
	private static final String TEMPLATE_SETINDUSTRY_URL="https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";

	/**
	 * @Fields TEMPLATE_ADD_URL：获得模板ID
	 */
	private static final String TEMPLATE_ADD_URL="https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";

	/**
	 * @Fields TEMPLATE_SEND_URL：发送模板消息
	 */
	private static final String TEMPLATE_SEND_URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	private static final String MESSAGE_SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	
//	public static final String PAYOK_TEMPLATE = "b8Ly85rNB4Zg86dmAqGZgALC6OzhCDhnkGqzfPNk7Wk";

	
	public static JSONObject sendToAll(String accessToken, SendAllBase sendAll){
		return HttpUtils.doPostJson(getUrl(MESSAGE_SEND_ALL,accessToken), JSON.toJSONString(sendAll));
	}
	
	/**
	 * 发送模板消息
	 * @param accessToken
	 * @param template
	 * @return
	 */
	public static JSONObject sendTemplate(String accessToken,Template template){
		log.debug("发送模版消息："+JSON.toJSONString(template));
		return HttpUtils.doPostJson(getUrl(TEMPLATE_SEND_URL,accessToken), JSON.toJSONString(template));
	}
	
	/**
	 * 获得模板ID
	 * @param accessToken
	 * @param templateIdShort 模板库中模板的编号,有“TM**”和“OPENTMTM**”等形式
	 * @return
	 */
	public static JSONObject addTemplate(String accessToken,String templateIdShort){
		Map<String, String> map = new HashMap<String,String>();
		map.put("template_id_short", templateIdShort);
		return HttpUtils.doPostJson(getUrl(TEMPLATE_ADD_URL,accessToken), JSON.toJSONString(map));
	}
	
	/**
	 * 设置所属行业
	 * 每月可修改行业1次，账号仅可使用所属行业中相关的模板
	 * @param accessToken
	 * @param industryId1 公众号模板消息所属行业编号
	 * @param industryId2 公众号模板消息所属行业编号
	 * @return
	 */
	public static JSONObject setIndustry(String accessToken,String industryId1,String industryId2){
		Map<String, String> map = new HashMap<String,String>();
		map.put("industry_id1", industryId1);
		map.put("industry_id2", industryId2);
		return HttpUtils.doPostJson(getUrl(TEMPLATE_SETINDUSTRY_URL,accessToken), JSON.toJSONString(map));
	}
	
	private static String getUrl(String url,String accessToken){
		String str = url.replace("ACCESS_TOKEN", accessToken);
		if(log.isDebugEnabled()){
			log.debug(str);
		}
		return str;
	}
	
	
}
