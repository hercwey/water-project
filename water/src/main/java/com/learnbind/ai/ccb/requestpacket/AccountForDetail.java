package com.learnbind.ai.ccb.requestpacket;

/**
 * 活期存款账户  
 * 用于:查询帐户明细（P1CMSER65）
 * @author lenovo
 */
public class AccountForDetail {
	
	/**
	 * --------------
	 * 账号(需查询明细的帐号)
	 * --------------
	 * 需要进行配置.
	 */
	private String AccNo;  //<![CDATA[13001618801050519604]]></AccNo>
    /**
     * 起始明细号
     */
    private String Beg_Dtl_No="";  //<![CDATA[]]></Beg_Dtl_No>
    /**
     * 终止明细号
     */
    private String End_Dtl_No="";  //<![CDATA[]]></End_Dtl_No>
    
    
    
	public String getAccNo() {
		return AccNo;
	}
	public void setAccNo(String accNo) {
		AccNo = accNo;
	}
	public String getBeg_Dtl_No() {
		return Beg_Dtl_No;
	}
	public void setBeg_Dtl_No(String beg_Dtl_No) {
		Beg_Dtl_No = beg_Dtl_No;
	}
	public String getEnd_Dtl_No() {
		return End_Dtl_No;
	}
	public void setEnd_Dtl_No(String end_Dtl_No) {
		End_Dtl_No = end_Dtl_No;
	}

    
}
