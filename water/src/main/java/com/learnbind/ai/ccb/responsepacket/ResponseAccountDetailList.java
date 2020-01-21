package com.learnbind.ai.ccb.responsepacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询账户详细-响应报文
 * @author lenovo
 *
 */
public class ResponseAccountDetailList {
	List<ResponseAccountDetail> accountDetailList;
	
	public ResponseAccountDetailList() {
		accountDetailList=new ArrayList<>();
	}
	
	public void add(ResponseAccountDetail accountDetail) {
		accountDetailList.add(accountDetail);
	}

	public List<ResponseAccountDetail> getAccountDetailList() {
		return accountDetailList;
	}

	public void setAccountDetailList(List<ResponseAccountDetail> accountDetailList) {
		this.accountDetailList = accountDetailList;
	}
	
	
	
}
