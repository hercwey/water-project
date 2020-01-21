package com.learnbind.ai.ccb.responsepacket;

/** 
 * 响应报文-转账
 * @author lenovo
 * 
 * 
 * 	CshMgt_TxnSrlNo #现金管理交易流水号 32 C N DF数据格式：32!an
	Csz_Txn_ID 定制交易编号 50 C N DF数据格式：n..50
	CCBS_Jrnl_No #CCBS流水号 20 C N DF数据格式：an..20
	Hst_Txn_Dt #主机交易日期 8 C N DF数据格式：yyyymmdd
	CshMgt_Txn_Rslt_Cd #现金管理交易结果代码 1 C Y
				1-交易成功2-交易失败3-处理中
											DF数据格式：1!n
	Err_Inf #错误信息 600 C N DF数据格式：anc..200
	CshMgt_Err_Cd #现金管理错误代码 12 C N DF数据格式：12!an
 */
public class ResponseTransfer {
	
	/**
	 * #服务方业务日期  8!n 格式 ：YYYYMMDD；
	 */
	private String SvPt_Bsn_Dt; //<![CDATA[]]></SvPt_Bsn_Dt>
	/**
	 * #返回文件名
	 */
	private String Ret_File_Nm; //<![CDATA[]]></Ret_File_Nm>
	/**
	 * 可售产品编码
	 */
	private String ASPD_ECD; //<![CDATA[]]></ASPD_ECD>
	/**
	 * #流程功能编号
	 */
	private String WF_FCN_ID; //<![CDATA[]]></WF_FCN_ID>
	/**
	 * #现金管理交易流水号
	 */
	private String CshMgt_TxnSrlNo; //<![CDATA[CN000s61201810091602084192307198]]></CshMgt_TxnSrlNo>
	/**
	 * 定制交易编号
	 */
	private String Csz_Txn_ID; //<![CDATA[]]></Csz_Txn_ID>
	/**
	 * #CCBS流水
	 */
	private String CCBS_Jrnl_No; //<![CDATA[1306358080NOPC7WZOU]]></CCBS_Jrnl_No>
	
	/**
	 * #主机交易日期
	 */
	private String Hst_Txn_Dt; //<![CDATA[20181009]]></Hst_Txn_Dt>
	/**
	 * #现金管理交易结果代码
	 * 1-交易成功2-交易失败3-处理中
	 */
	private String CshMgt_Txn_Rslt_Cd; //<![CDATA[1]]></CshMgt_Txn_Rslt_Cd>
	
	/**
	 * #错误信息
	 */
	private String Err_Inf; //<![CDATA[]]></Err_Inf>
	/**
	 * #现金管理错误代码
	 */
	private String CshMgt_Err_Cd; //<![CDATA[]]></CshMgt_Err_Cd>
	private String TOTAL_PAGE;			//0</TOTAL_PAGE>
	private String TOTAL_REC;			//0</TOTAL_REC>
	private String CURR_TOTAL_PAGE;		//0</CURR_TOTAL_PAGE>
	private String CURR_TOTAL_REC; 	//<![CDATA[0]]></CURR_TOTAL_REC>
	private String STS_TRACE_ID; 	//<![CDATA[]]></STS_TRACE_ID>
	public String getSvPt_Bsn_Dt() {
		return SvPt_Bsn_Dt;
	}
	public void setSvPt_Bsn_Dt(String svPt_Bsn_Dt) {
		SvPt_Bsn_Dt = svPt_Bsn_Dt;
	}
	public String getRet_File_Nm() {
		return Ret_File_Nm;
	}
	public void setRet_File_Nm(String ret_File_Nm) {
		Ret_File_Nm = ret_File_Nm;
	}
	public String getASPD_ECD() {
		return ASPD_ECD;
	}
	public void setASPD_ECD(String aSPD_ECD) {
		ASPD_ECD = aSPD_ECD;
	}
	public String getWF_FCN_ID() {
		return WF_FCN_ID;
	}
	public void setWF_FCN_ID(String wF_FCN_ID) {
		WF_FCN_ID = wF_FCN_ID;
	}
	public String getCshMgt_TxnSrlNo() {
		return CshMgt_TxnSrlNo;
	}
	public void setCshMgt_TxnSrlNo(String cshMgt_TxnSrlNo) {
		CshMgt_TxnSrlNo = cshMgt_TxnSrlNo;
	}
	public String getCsz_Txn_ID() {
		return Csz_Txn_ID;
	}
	public void setCsz_Txn_ID(String csz_Txn_ID) {
		Csz_Txn_ID = csz_Txn_ID;
	}
	public String getCCBS_Jrnl_No() {
		return CCBS_Jrnl_No;
	}
	public void setCCBS_Jrnl_No(String cCBS_Jrnl_No) {
		CCBS_Jrnl_No = cCBS_Jrnl_No;
	}
	public String getHst_Txn_Dt() {
		return Hst_Txn_Dt;
	}
	public void setHst_Txn_Dt(String hst_Txn_Dt) {
		Hst_Txn_Dt = hst_Txn_Dt;
	}
	public String getCshMgt_Txn_Rslt_Cd() {
		return CshMgt_Txn_Rslt_Cd;
	}
	public void setCshMgt_Txn_Rslt_Cd(String cshMgt_Txn_Rslt_Cd) {
		CshMgt_Txn_Rslt_Cd = cshMgt_Txn_Rslt_Cd;
	}
	public String getErr_Inf() {
		return Err_Inf;
	}
	public void setErr_Inf(String err_Inf) {
		Err_Inf = err_Inf;
	}
	public String getCshMgt_Err_Cd() {
		return CshMgt_Err_Cd;
	}
	public void setCshMgt_Err_Cd(String cshMgt_Err_Cd) {
		CshMgt_Err_Cd = cshMgt_Err_Cd;
	}
	public String getTOTAL_PAGE() {
		return TOTAL_PAGE;
	}
	public void setTOTAL_PAGE(String tOTAL_PAGE) {
		TOTAL_PAGE = tOTAL_PAGE;
	}
	public String getTOTAL_REC() {
		return TOTAL_REC;
	}
	public void setTOTAL_REC(String tOTAL_REC) {
		TOTAL_REC = tOTAL_REC;
	}
	public String getCURR_TOTAL_PAGE() {
		return CURR_TOTAL_PAGE;
	}
	public void setCURR_TOTAL_PAGE(String cURR_TOTAL_PAGE) {
		CURR_TOTAL_PAGE = cURR_TOTAL_PAGE;
	}
	public String getCURR_TOTAL_REC() {
		return CURR_TOTAL_REC;
	}
	public void setCURR_TOTAL_REC(String cURR_TOTAL_REC) {
		CURR_TOTAL_REC = cURR_TOTAL_REC;
	}
	public String getSTS_TRACE_ID() {
		return STS_TRACE_ID;
	}
	public void setSTS_TRACE_ID(String sTS_TRACE_ID) {
		STS_TRACE_ID = sTS_TRACE_ID;
	}
	
	@Override
	public String toString() {
		return "ResponseTransfer [SvPt_Bsn_Dt=" + SvPt_Bsn_Dt + ", Ret_File_Nm=" + Ret_File_Nm + ", ASPD_ECD="
				+ ASPD_ECD + ", WF_FCN_ID=" + WF_FCN_ID + ", CshMgt_TxnSrlNo=" + CshMgt_TxnSrlNo + ", Csz_Txn_ID="
				+ Csz_Txn_ID + ", CCBS_Jrnl_No=" + CCBS_Jrnl_No + ", Hst_Txn_Dt=" + Hst_Txn_Dt + ", CshMgt_Txn_Rslt_Cd="
				+ CshMgt_Txn_Rslt_Cd + ", Err_Inf=" + Err_Inf + ", CshMgt_Err_Cd=" + CshMgt_Err_Cd + ", TOTAL_PAGE="
				+ TOTAL_PAGE + ", TOTAL_REC=" + TOTAL_REC + ", CURR_TOTAL_PAGE=" + CURR_TOTAL_PAGE + ", CURR_TOTAL_REC="
				+ CURR_TOTAL_REC + ", STS_TRACE_ID=" + STS_TRACE_ID + "]";
	}
	
}
