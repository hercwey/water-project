package com.learnbind.ai.ccb.responsepacket;

import java.util.ArrayList;
import java.util.List;

public class ResponseTransferResultList {
	private List<ResponseTransferResult> tranferResultList;

	/**
	 * constructor
	 */
	public ResponseTransferResultList() {
		tranferResultList = new ArrayList<ResponseTransferResult>();
	}

	/**
	 * 增加一个对象
	 * @param responseTransferResult
	 */
	public void add(ResponseTransferResult responseTransferResult) {
		tranferResultList.add(responseTransferResult);
	}
	
	
	// ---------------getter and setter--------------------

	public List<ResponseTransferResult> getTranferResultList() {
		return tranferResultList;
	}

	public void setTranferResultList(List<ResponseTransferResult> tranferResultList) {
		this.tranferResultList = tranferResultList;
	}

	

	
}
