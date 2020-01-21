package com.learnbind.ai.tax.packet.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: DownloadRedInfo.java
 * @Description: 红字信息表下载-响应报文(业务主体)
 *
 * @author lenovo
 * @date 2019年10月20日 下午3:59:27
 * @version V1.0 
 *
 */
public class DownloadRedInfo {
	DownloadRedInfoHead redInfoHead;
	List<DownloadRedInfoDetail> redInfoDetailList;
	
	/**
	 * 创建一个新的实例 DownloadRedInfo.
	 */
	public DownloadRedInfo() {
		redInfoHead=new DownloadRedInfoHead();
		redInfoDetailList=new ArrayList<DownloadRedInfoDetail>();
	}
	
	/**
	 * @Title: addRedInfoDetail
	 * @Description: 增加详情
	 * @param redInfoDetail 
	 */
	public void addRedInfoDetail(DownloadRedInfoDetail redInfoDetail) {
		redInfoDetailList.add(redInfoDetail);
	}
	
	/**
	 * @Title: getDetailNum
	 * @Description: 获取详情列表的大小
	 * @return 
	 */
	public int getDetailNum() {
		return redInfoDetailList.size();
	}

	public DownloadRedInfoHead getRedInfoHead() {
		return redInfoHead;
	}

	public void setRedInfoHead(DownloadRedInfoHead redInfoHead) {
		this.redInfoHead = redInfoHead;
	}

	public List<DownloadRedInfoDetail> getRedInfoDetailList() {
		return redInfoDetailList;
	}

	public void setRedInfoDetailList(List<DownloadRedInfoDetail> redInfoDetailList) {
		this.redInfoDetailList = redInfoDetailList;
	}
	
	
	
}
