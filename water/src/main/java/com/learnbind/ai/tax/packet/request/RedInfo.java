package com.learnbind.ai.tax.packet.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: RedInfo.java
 * @Description: 红字信息
 *
 * @author lenovo
 * @date 2019年10月18日 上午9:43:34
 * @version V1.0 
 *
 */
public class RedInfo {
	/**
	 * @Fields invoiceHead：红字发票头信息
	 */
	private RedInfoHead redInfoHead;
	/**
	 * @Fields redInfoDetailList：发票明细
	 */
	private List<RedInfoDetail> redInfoDetailList;
	
	/**
	 * 创建一个新的实例 RedInfo.
	 */
	public RedInfo() {
		redInfoDetailList=new ArrayList<RedInfoDetail>();
	}
	
	/**
	 * @Title: addDetail
	 * @Description: 增加明细
	 * @param redInfoDetail 
	 */
	public void addDetail(RedInfoDetail redInfoDetail) {
		redInfoDetailList.add(redInfoDetail);
	}
	
	/**
	 * @Title: getDetailSize
	 * @Description: 获取明细长度
	 * @return 
	 */
	public int getDetailSize() {
		return redInfoDetailList.size();
	}
	
	//------------getter and setter-----------

	public RedInfoHead getRedInfoHead() {
		return redInfoHead;
	}

	public void setRedInfoHead(RedInfoHead redInfoHead) {
		this.redInfoHead = redInfoHead;
	}

	public List<RedInfoDetail> getRedInfoDetailList() {
		return redInfoDetailList;
	}

	
	public void setRedInfoDetailList(List<RedInfoDetail> redInfoDetailList) {
		this.redInfoDetailList = redInfoDetailList;
	}

	
	
}
