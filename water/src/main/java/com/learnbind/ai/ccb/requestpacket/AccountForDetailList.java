package com.learnbind.ai.ccb.requestpacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询帐户明细  帐户信息列表
 * 业务代码:P1CMSER65
 * @author lenovo
 *
 */
public class AccountForDetailList {	
	private List<AccountForDetail> accountList;  //账户列表
	
	public AccountForDetailList() {		
		accountList=new ArrayList<>();
	}
	
	public void addAccount(AccountForDetail account) {
		accountList.add(account);
	}

	public List<AccountForDetail> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<AccountForDetail> accountList) {
		this.accountList = accountList;
	}
	
	/**
	 * 返回账户个数
	 * @return
	 */
	public int getAccountNum() {
		return accountList.size();
	}
	
	
	
	
}
