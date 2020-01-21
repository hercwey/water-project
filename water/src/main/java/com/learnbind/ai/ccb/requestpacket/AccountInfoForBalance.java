package com.learnbind.ai.ccb.requestpacket;

/**
 * 查询帐户余额  帐户信息 
 * 业务代码:P1CMSER18
 * @author lenovo
 *
 */
public class AccountInfoForBalance {
	
	/**
	 * #指令序列号
	 */
	private String Insn_Seq_No="";	//<![CDATA[]]></Insn_Seq_No>
    /**
     * ----------------------
     * 现金客户账号
     * ----------------------
     * 需要在项目中进行配置.
     * 
     * 注:需要设置 
     */
    private String Cash_Cst_AccNo;	//<![CDATA[13001618801050519642]]></Cash_Cst_AccNo>
    /**
     * 现金管理账户类型代码  1-活期2-定期DF数据格式：1!n
     */
    private String CshMgt_Acc_TpCd="1";	//<![CDATA[1]]></CshMgt_Acc_TpCd>
    /**
     * 币种代码
     */
    private String CcyCd="156";	//<![CDATA[156]]></CcyCd>
    private String BkltNo;	//<![CDATA[]]></BkltNo>
    private String Dep_DepSeqNo;	//<![CDATA[]]></Dep_DepSeqNo>
    
    
	public String getInsn_Seq_No() {
		return Insn_Seq_No;
	}
	public void setInsn_Seq_No(String insn_Seq_No) {
		Insn_Seq_No = insn_Seq_No;
	}
	public String getCash_Cst_AccNo() {
		return Cash_Cst_AccNo;
	}
	public void setCash_Cst_AccNo(String cash_Cst_AccNo) {
		Cash_Cst_AccNo = cash_Cst_AccNo;
	}
	public String getCshMgt_Acc_TpCd() {
		return CshMgt_Acc_TpCd;
	}
	public void setCshMgt_Acc_TpCd(String cshMgt_Acc_TpCd) {
		CshMgt_Acc_TpCd = cshMgt_Acc_TpCd;
	}
	public String getCcyCd() {
		return CcyCd;
	}
	public void setCcyCd(String ccyCd) {
		CcyCd = ccyCd;
	}
	public String getBkltNo() {
		return BkltNo;
	}
	public void setBkltNo(String bkltNo) {
		BkltNo = bkltNo;
	}
	public String getDep_DepSeqNo() {
		return Dep_DepSeqNo;
	}
	public void setDep_DepSeqNo(String dep_DepSeqNo) {
		Dep_DepSeqNo = dep_DepSeqNo;
	}
    
    

}
