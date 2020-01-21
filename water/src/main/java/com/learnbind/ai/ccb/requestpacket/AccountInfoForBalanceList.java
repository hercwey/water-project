package com.learnbind.ai.ccb.requestpacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询帐户余额  帐户信息列表
 * 业务代码:P1CMSER18
 * @author lenovo
 *
 */
public class AccountInfoForBalanceList {	
	private List<AccountInfoForBalance> accountList;  //账户列表
	
	public AccountInfoForBalanceList() {		
		accountList=new ArrayList<>();
	}
	
	public void addAccount(AccountInfoForBalance account) {
		accountList.add(account);
	}

	public List<AccountInfoForBalance> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<AccountInfoForBalance> accountList) {
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
