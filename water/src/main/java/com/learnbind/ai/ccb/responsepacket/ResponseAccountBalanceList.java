package com.learnbind.ai.ccb.responsepacket;

import java.util.ArrayList;
import java.util.List;

/**
 * RESPONSE报文 帐户余额列表对象
 * 
 * @author lenovo
 *
 */
public class ResponseAccountBalanceList {
	private List<ResponseAccountBalance> accountBalanceList;

	/**
	 * constructor
	 */
	public ResponseAccountBalanceList() {
		accountBalanceList = new ArrayList<ResponseAccountBalance>();
	}

	/**
	 * 增加一个帐户余额对象
	 * 
	 * @param responseAccountBalance 帐户余额对象
	 */
	public void add(ResponseAccountBalance responseAccountBalance) {
		accountBalanceList.add(responseAccountBalance);
	}

	// ---------------getter and setter--------------------

	public List<ResponseAccountBalance> getAccountBalanceList() {
		return accountBalanceList;
	}

	public void setAccountBalanceList(List<ResponseAccountBalance> accountBalanceList) {
		this.accountBalanceList = accountBalanceList;
	}

}
