package com.learnbind.ai.util.wx.template;


public class SendAllBase {
	
	private SendAllFilter filter;

	private String touser;//预览接口,对openID进行预览
	
	private String towxname;//预览接口,针对微信号进行预览,towxname和touser同时赋值时，以towxname优先
	
	private String msgtype;

	public SendAllFilter getFilter() {
		return filter;
	}

	public void setFilter(SendAllFilter filter) {
		this.filter = filter;
	}
	
	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTowxname() {
		return towxname;
	}

	public void setTowxname(String towxname) {
		this.towxname = towxname;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	@Override
	public String toString() {
		return "SendAllBase [filter=" + filter + ", touser=" + touser
				+ ", towxname=" + towxname + ", msgtype=" + msgtype + "]";
	}
	
}
