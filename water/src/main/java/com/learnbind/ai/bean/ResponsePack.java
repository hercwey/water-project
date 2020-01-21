package com.learnbind.ai.bean;

/**
 * 
 * @author lenovo
 * 功能:
 * 	响应数据包格式
 *
 */
public class ResponsePack {
	private int cmd;    //命令码
	private String msg; //命令说明
	private String DataSeg; //请求参数段
	
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDataSeg() {
		return DataSeg;
	}
	public void setDataSeg(String dataSeg) {
		DataSeg = dataSeg;
	}
	
	
}
