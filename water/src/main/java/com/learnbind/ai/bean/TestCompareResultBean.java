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
public class TestCompareResultBean {
	private String room;    //门牌号
	private String newMeterAddr; //表地址（采集器地址+表地址）
	private String oldMeterAddr; //表地址（采集器地址+表地址）
	
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getNewMeterAddr() {
		return newMeterAddr;
	}
	public void setNewMeterAddr(String newMeterAddr) {
		this.newMeterAddr = newMeterAddr;
	}
	public String getOldMeterAddr() {
		return oldMeterAddr;
	}
	public void setOldMeterAddr(String oldMeterAddr) {
		this.oldMeterAddr = oldMeterAddr;
	}
	
	@Override
	public String toString() {
		return "TestCompareResultBean [room=" + room + ", newMeterAddr=" + newMeterAddr + ", oldMeterAddr="
				+ oldMeterAddr + "]";
	}
	
}
