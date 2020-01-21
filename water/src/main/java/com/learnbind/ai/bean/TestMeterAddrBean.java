package com.learnbind.ai.bean;

/**
 * 
 * @author lenovo
 * 
 * 功能:
 * 	请求数据包格式(JSON)
 *
 *
 */
public class TestMeterAddrBean {
	private String room;    //门牌号
	private String meterAddr; //表地址（采集器地址+表地址）
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	
	
	@Override
	public String toString() {
		return "TestMeterAddrBean [room=" + room + ", meterAddr=" + meterAddr + "]";
	}
	
	
}
