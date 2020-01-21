package com.learnbind.ai.service.wechat.entity;

/**
 * @author srd
 * jsapi_ticket
 *
 */
public class JsapiTicket {

	private Integer errcode;
	
	private String errmsg;
	
	private String ticket;
	
	private String expires_in;//有效期，默认为7200秒
	
	private Long createTime;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 验证jsapi_ticket有效期
	 * 		真正有效期为7200秒，此处验证为7000秒
	 * @return
	 */
	public Boolean isValid(){
		Long currentTime = System.currentTimeMillis()/1000;
		if(createTime-currentTime>200){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "JsapiTicket [errcode=" + errcode + ", errmsg=" + errmsg + ", ticket=" + ticket + ", expires_in="
				+ expires_in + ", createTime=" + createTime + "]";
	}
	
}
