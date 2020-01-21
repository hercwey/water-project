package com.learnbind.ai.ccb.responsepacket;

/**
 * 查询单据状态-响应报文 
 * 注:此类的内容来自于测试范本
 * 
 * @author lenovo
 */
public class ResponseCertState {
	
	/**
	 * 凭证-客户端编号(ID)
	 */
	private String Txn_SN;  //<![CDATA[113810458305358140]]></Txn_SN>
	/**
	 * CCB端对此凭证的编号(ID) 
	 * 凭证-CCB端编号(ID)
	 */
	private String VchID;  //<![CDATA[VS201810110000093989]]></VchID>
	/**
	 * 委托单位帐号
	 * 客户在建行所签项目的结算帐号  
	 * 
	 * 注:本项目中为水司在建设银行所开立的结算帐号
	 */
	private String EtrUnt_AccNo;  //<![CDATA[13001618801050519642]]></EtrUnt_AccNo>
	/**
	 * 委托单位户名
	 * 客户在建行所签项目的结算帐号户名
	 * 
	 * 注:本项目中为水司名称
	 */
	private String EtrUnt_AccNm;  //<![CDATA[公司七八]]></EtrUnt_AccNm>
	/**
	 * 委托单位账户开户行号
	 * 客户在建行所签项目的结算帐号开户行号 
	 */
	private String EtrUnt_Acc_DepBnk_No;  //<![CDATA[130618801]]></EtrUnt_Acc_DepBnk_No>
	/**
	 *委托单位账户开户行名  客户在建行所签项目的结算帐号开户行名
	 * 
	 */
	private String EtrUnt_Acc_DpBkNm;  //<![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></EtrUnt_Acc_DpBkNm>
	/**
	 * 对方账号  单笔凭证用于标识对方帐号

	 */
	private String CntprtAcc;  //<![CDATA[]]></CntprtAcc>
	/**
	 * 对方账户名称  单笔凭证用户标识对方帐号名称
	 */
	private String Cntrprt_AccNm;  //<![CDATA[]]></Cntrprt_AccNm>
	/**
	 * 汇入行行号  跨行对方账号开户行号
	 */
	private String IwBk_BrNo;  //<![CDATA[]]></IwBk_BrNo>
	/**
	 * 汇入行行名  跨行对方账号开户行名
	 */
	private String IwBk_BkNm;  //<![CDATA[]]></IwBk_BkNm>
	/**
	 * 总金额  批量文件中明细总金额
	 */
	private String TAmt;        //>11.00</TAmt>
	/**
	 * 总笔数  批量文件中明细总笔数
	 */
	private String TDnum;  //<![CDATA[5]]></TDnum>
	/**
	 * 凭证类型  0-单笔 1-批量
	 */
	private String VCHR_TP_CODE;  //<![CDATA[1]]></VCHR_TP_CODE>
	/**
	 * 凭证状态"100-不确定,
		200-待复核,
		300-复核不通过,
		400-复核完毕,待账务系统处理,
		401-复核完毕,待发送账务系统,
		500-已收回,
		600-已删除(直连单据复核不通过时状态置为600),
		700-交易成功,
		800-交易失败,
		901-账务系统成功已接收,
		902-账务系统检核完毕,
		903-账务系统下账,
		904-账务系统处理完成,
		429-单笔跨行终审成功,待人行处理"
		
		注:此状态可为上层业务逻辑使用

	 */
	private String Vchr_St;  //<![CDATA[700]]></Vchr_St>
	/**
	 * 
	 */
	private String SRP_Err_Cd;  //<![CDATA[]]></SRP_Err_Cd>
	
	/**
	 * 代收付项目类型代码  0-代发 1-代扣
	 */
	private String SBSTRCVPY_PRJ_TPCD;  //<![CDATA[0]]></SBSTRCVPY_PRJ_TPCD>
	/**
	 * 跨行标志代码
		凭证属于建行行内或跨行凭证
		空-全部;0-行内;1-二代支付跨行；2-大小额跨行
	 */
	private String IntrBnk_IndCd;  //<![CDATA[1]]></IntrBnk_IndCd>
	/**
	 * 委托项目编号
		客户在建行所签项目的项目编号

	 */
	private String Entrst_Prj_ID;  //<![CDATA[130130070]]></Entrst_Prj_ID>
	/**
	 * 委托项目名称
		客户在建行所签项目的项目名称

	 */
	private String Entrst_Prj_Nm;  //<![CDATA[公司七八代付产品9642]]></Entrst_Prj_Nm>
	/**
	 * 项目用途编号
		凭证所属用途编号

	 */
	private String Prj_Use_ID;  //<![CDATA[zldf00001]]></Prj_Use_ID>
	/**
	 * 项目用途名称
		凭证所属用途名称

	 */
	private String Prj_Use_Nm;  //<![CDATA[直连客户代付专用]]></Prj_Use_Nm>
	/**
	 * 代收代付摘要描述
手工摘要

	 */
	private String SCSP_Smy_Dsc;  //<![CDATA[]]></SCSP_Smy_Dsc>
	/**
	 * 文件名称
批量文件文件名称

	 */
	private String FILE_NM;  //<![CDATA[1010010011539229524167010.txt]]></FILE_NM>
	/**
	 * 凭证生成日期
制单日期

	 */
	private String Vchr_Gen_Dt;  //<![CDATA[20181011114459]]></Vchr_Gen_Dt>
	/**
	 * 制单员编号
发起制单的操作员编号

	 */
	private String TDP_ID;  //<![CDATA[333333]]></TDP_ID>
	/**
	 * 制单员名称
发起制单的操作员名称

	 */
	private String TDP_Nm;  //<![CDATA[]]></TDP_Nm>
	/**
	 * 成功金额
	 * 		批量凭证成功金额
	 * 注:可用于项目中,记录批量处理文件的状态
	 */
	private String Scss_Amt;  //<![CDATA[11.00]]></Scss_Amt>
	/**
	 * 成功笔数
	 * 	批量凭证成功笔数
	 * 		注:可用于项目中,记录批量处理文件的状态
	 */
	private String Scss_Dnum;  //<![CDATA[5]]></Scss_Dnum>
	/**
	 * #失败金额  批量凭证失败金额
		注:可用于项目中,记录批量处理文件的状态
	 */
	private String Fail_Amt;  //<![CDATA[0.00]]></Fail_Amt>
	/**
	 * #失败笔数
	 * 批量凭证失败笔数
	 * 
	 * 	注:可用于项目中,记录批量处理文件的状态
	 */
	private String Fail_Dnum;  //<![CDATA[0]]></Fail_Dnum>
	
	
	public String getTxn_SN() {
		return Txn_SN;
	}
	public void setTxn_SN(String txn_SN) {
		Txn_SN = txn_SN;
	}
	public String getVchID() {
		return VchID;
	}
	public void setVchID(String vchID) {
		VchID = vchID;
	}
	public String getEtrUnt_AccNo() {
		return EtrUnt_AccNo;
	}
	public void setEtrUnt_AccNo(String etrUnt_AccNo) {
		EtrUnt_AccNo = etrUnt_AccNo;
	}
	public String getEtrUnt_AccNm() {
		return EtrUnt_AccNm;
	}
	public void setEtrUnt_AccNm(String etrUnt_AccNm) {
		EtrUnt_AccNm = etrUnt_AccNm;
	}
	public String getEtrUnt_Acc_DepBnk_No() {
		return EtrUnt_Acc_DepBnk_No;
	}
	public void setEtrUnt_Acc_DepBnk_No(String etrUnt_Acc_DepBnk_No) {
		EtrUnt_Acc_DepBnk_No = etrUnt_Acc_DepBnk_No;
	}
	public String getEtrUnt_Acc_DpBkNm() {
		return EtrUnt_Acc_DpBkNm;
	}
	public void setEtrUnt_Acc_DpBkNm(String etrUnt_Acc_DpBkNm) {
		EtrUnt_Acc_DpBkNm = etrUnt_Acc_DpBkNm;
	}
	public String getCntprtAcc() {
		return CntprtAcc;
	}
	public void setCntprtAcc(String cntprtAcc) {
		CntprtAcc = cntprtAcc;
	}
	public String getCntrprt_AccNm() {
		return Cntrprt_AccNm;
	}
	public void setCntrprt_AccNm(String cntrprt_AccNm) {
		Cntrprt_AccNm = cntrprt_AccNm;
	}
	public String getIwBk_BrNo() {
		return IwBk_BrNo;
	}
	public void setIwBk_BrNo(String iwBk_BrNo) {
		IwBk_BrNo = iwBk_BrNo;
	}
	public String getIwBk_BkNm() {
		return IwBk_BkNm;
	}
	public void setIwBk_BkNm(String iwBk_BkNm) {
		IwBk_BkNm = iwBk_BkNm;
	}
	public String getTAmt() {
		return TAmt;
	}
	public void setTAmt(String tAmt) {
		TAmt = tAmt;
	}
	public String getTDnum() {
		return TDnum;
	}
	public void setTDnum(String tDnum) {
		TDnum = tDnum;
	}
	public String getVCHR_TP_CODE() {
		return VCHR_TP_CODE;
	}
	public void setVCHR_TP_CODE(String vCHR_TP_CODE) {
		VCHR_TP_CODE = vCHR_TP_CODE;
	}
	public String getVchr_St() {
		return Vchr_St;
	}
	public void setVchr_St(String vchr_St) {
		Vchr_St = vchr_St;
	}
	public String getSRP_Err_Cd() {
		return SRP_Err_Cd;
	}
	public void setSRP_Err_Cd(String sRP_Err_Cd) {
		SRP_Err_Cd = sRP_Err_Cd;
	}
	public String getSBSTRCVPY_PRJ_TPCD() {
		return SBSTRCVPY_PRJ_TPCD;
	}
	public void setSBSTRCVPY_PRJ_TPCD(String sBSTRCVPY_PRJ_TPCD) {
		SBSTRCVPY_PRJ_TPCD = sBSTRCVPY_PRJ_TPCD;
	}
	public String getIntrBnk_IndCd() {
		return IntrBnk_IndCd;
	}
	public void setIntrBnk_IndCd(String intrBnk_IndCd) {
		IntrBnk_IndCd = intrBnk_IndCd;
	}
	public String getEntrst_Prj_ID() {
		return Entrst_Prj_ID;
	}
	public void setEntrst_Prj_ID(String entrst_Prj_ID) {
		Entrst_Prj_ID = entrst_Prj_ID;
	}
	public String getEntrst_Prj_Nm() {
		return Entrst_Prj_Nm;
	}
	public void setEntrst_Prj_Nm(String entrst_Prj_Nm) {
		Entrst_Prj_Nm = entrst_Prj_Nm;
	}
	public String getPrj_Use_ID() {
		return Prj_Use_ID;
	}
	public void setPrj_Use_ID(String prj_Use_ID) {
		Prj_Use_ID = prj_Use_ID;
	}
	public String getPrj_Use_Nm() {
		return Prj_Use_Nm;
	}
	public void setPrj_Use_Nm(String prj_Use_Nm) {
		Prj_Use_Nm = prj_Use_Nm;
	}
	public String getSCSP_Smy_Dsc() {
		return SCSP_Smy_Dsc;
	}
	public void setSCSP_Smy_Dsc(String sCSP_Smy_Dsc) {
		SCSP_Smy_Dsc = sCSP_Smy_Dsc;
	}
	public String getFILE_NM() {
		return FILE_NM;
	}
	public void setFILE_NM(String fILE_NM) {
		FILE_NM = fILE_NM;
	}
	public String getVchr_Gen_Dt() {
		return Vchr_Gen_Dt;
	}
	public void setVchr_Gen_Dt(String vchr_Gen_Dt) {
		Vchr_Gen_Dt = vchr_Gen_Dt;
	}
	public String getTDP_ID() {
		return TDP_ID;
	}
	public void setTDP_ID(String tDP_ID) {
		TDP_ID = tDP_ID;
	}
	public String getTDP_Nm() {
		return TDP_Nm;
	}
	public void setTDP_Nm(String tDP_Nm) {
		TDP_Nm = tDP_Nm;
	}
	public String getScss_Amt() {
		return Scss_Amt;
	}
	public void setScss_Amt(String scss_Amt) {
		Scss_Amt = scss_Amt;
	}
	public String getScss_Dnum() {
		return Scss_Dnum;
	}
	public void setScss_Dnum(String scss_Dnum) {
		Scss_Dnum = scss_Dnum;
	}
	public String getFail_Amt() {
		return Fail_Amt;
	}
	public void setFail_Amt(String fail_Amt) {
		Fail_Amt = fail_Amt;
	}
	public String getFail_Dnum() {
		return Fail_Dnum;
	}
	public void setFail_Dnum(String fail_Dnum) {
		Fail_Dnum = fail_Dnum;
	}
	
	
}
