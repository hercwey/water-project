package com.learnbind.ai.ccb.responsepacket;

/**
 * 凭证明细-响应报文
 * @author lenovo
 * 由于项目中采用文件模式获取凭证明细的处理状态,此处的状态暂不由上层业务逻辑处理.
 */
public class ResponseCertDetailPacketMode {
	
	/**
	 * 
	 */
	private String SCSP_SN; //<![CDATA[1]]></SCSP_SN>
	private String TrdPCt_AccNo; //<![CDATA[4340610130352336]]></TrdPCt_AccNo>
	private String TrdPCt_Nm; //<![CDATA[杨三八]]></TrdPCt_Nm>
	private String SRP_TxnAmt; //<![CDATA[1]]></SRP_TxnAmt>
	private String TrdPt_Acc_DepBnk_No; //<![CDATA[]]></TrdPt_Acc_DepBnk_No>
	private String TrdPt_Acc_DpBkNm; //<![CDATA[]]></TrdPt_Acc_DpBkNm>
	private String SCSP_Txn_StCd; //<![CDATA[2]]></SCSP_Txn_StCd>
	private String Err_Rsn; //<![CDATA[交易成功]]></Err_Rsn>
	private String SCSP_Smy_Dsc; //<![CDATA[安心付测试1]]></SCSP_Smy_Dsc>
	private String Rmrk1; //<![CDATA[测试一分钱]]></Rmrk1>
	private String Rmrk2; //<![CDATA[]]></Rmrk2>
	
	
	public String getSCSP_SN() {
		return SCSP_SN;
	}
	public void setSCSP_SN(String sCSP_SN) {
		SCSP_SN = sCSP_SN;
	}
	public String getTrdPCt_AccNo() {
		return TrdPCt_AccNo;
	}
	public void setTrdPCt_AccNo(String trdPCt_AccNo) {
		TrdPCt_AccNo = trdPCt_AccNo;
	}
	public String getTrdPCt_Nm() {
		return TrdPCt_Nm;
	}
	public void setTrdPCt_Nm(String trdPCt_Nm) {
		TrdPCt_Nm = trdPCt_Nm;
	}
	public String getSRP_TxnAmt() {
		return SRP_TxnAmt;
	}
	public void setSRP_TxnAmt(String sRP_TxnAmt) {
		SRP_TxnAmt = sRP_TxnAmt;
	}
	public String getTrdPt_Acc_DepBnk_No() {
		return TrdPt_Acc_DepBnk_No;
	}
	public void setTrdPt_Acc_DepBnk_No(String trdPt_Acc_DepBnk_No) {
		TrdPt_Acc_DepBnk_No = trdPt_Acc_DepBnk_No;
	}
	public String getTrdPt_Acc_DpBkNm() {
		return TrdPt_Acc_DpBkNm;
	}
	public void setTrdPt_Acc_DpBkNm(String trdPt_Acc_DpBkNm) {
		TrdPt_Acc_DpBkNm = trdPt_Acc_DpBkNm;
	}
	public String getSCSP_Txn_StCd() {
		return SCSP_Txn_StCd;
	}
	public void setSCSP_Txn_StCd(String sCSP_Txn_StCd) {
		SCSP_Txn_StCd = sCSP_Txn_StCd;
	}
	public String getErr_Rsn() {
		return Err_Rsn;
	}
	public void setErr_Rsn(String err_Rsn) {
		Err_Rsn = err_Rsn;
	}
	public String getSCSP_Smy_Dsc() {
		return SCSP_Smy_Dsc;
	}
	public void setSCSP_Smy_Dsc(String sCSP_Smy_Dsc) {
		SCSP_Smy_Dsc = sCSP_Smy_Dsc;
	}
	public String getRmrk1() {
		return Rmrk1;
	}
	public void setRmrk1(String rmrk1) {
		Rmrk1 = rmrk1;
	}
	public String getRmrk2() {
		return Rmrk2;
	}
	public void setRmrk2(String rmrk2) {
		Rmrk2 = rmrk2;
	}
	
	
}
