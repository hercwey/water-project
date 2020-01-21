package com.learnbind.ai.sms;

import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: SendMultSMRequestParams.java
 * @Description: 请求参数-群发短信
 *
 * @author lenovo
 * @date 2019年8月20日 下午12:15:11
 * @version V1.0 
 */
public class SendMultSMRequestParams {
	/**
	    ext		否	string	用户的 session 内容，腾讯 server 回包中会原样返回，可选字段，不需要就填空
		extend	否	string	短信码号扩展号，格式为纯数字串，其他格式无效。默认没有开通，开通请联系 sms helper
		
		params	是	array	模板参数，若模板没有参数，请提供为空数组
		sig		是	string	App 凭证，具体计算方式见下注
		sign	是	string	短信签名内容，使用 UTF-8 编码。缺省使用第一个审核通过的签名
							签名信息可登录 短信控制台 查看
		
		tel		是	array	群发号码数组，群发时一次最多不要超过200个手机号
		time	是	number	请求发起时间，UNIX 时间戳（单位：秒），如果和系统时间相差超过10分钟则会返回失败
		tpl_id	是	number	模板 ID，在 控制台 审核通过的模板 ID
		
	 */
	
	private String ext="";
	private String extent="";
	private List<String> params;
	private String sig;
	private String sign;
	private List<TelephoneNo> tel;
	private Long time;
	private Long tpl_id;
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getExtent() {
		return extent;
	}
	public void setExtent(String extent) {
		this.extent = extent;
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public List<TelephoneNo> getTel() {
		return tel;
	}
	public void setTel(List<TelephoneNo> tel) {
		this.tel = tel;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getTpl_id() {
		return tpl_id;
	}
	public void setTpl_id(Long tpl_id) {
		this.tpl_id = tpl_id;
	}
	
	
	
	
}
