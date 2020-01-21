package com.learnbind.ai.util.wx.template;

import java.util.Map;

public class Template {
	
	private String touser;  
	
	private String template_id;
	
	private String url;
	
	private Map<String,Data> data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Map<String, Data> getData() {
		return data;
	}

	public void setData(Map<String, Data> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Template [touser=" + touser + ", template_id=" + template_id
				+ ", url=" + url + ", data=" + data + "]";
	}
	

}
