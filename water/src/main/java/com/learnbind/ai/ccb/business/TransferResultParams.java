package com.learnbind.ai.ccb.business;

/**
 * 查询转帐结果参数
 * @author lenovo
 *
 */
public class TransferResultParams {
	/**
     * 客户编号
     * 需要设置(可放在CCB配置中)
     */
    private String Cst_ID;//<![CDATA[973850458304234973]]></Cst_ID>
    /**
     * 现金客户树编号
     * 需要设置
     */
    private String CCstTr_ID;//<![CDATA[CMN0000093068]]></CCstTr_ID>
    /**
     * 现金客户树节点编号
     * 需要设置
     */
    private String CCstTrNdID;//<![CDATA[ND97385045830423497300001]]></CCstTrNdID>
    /**
     * #查询起始日期  DF数据格式:yyyymmdd
     * 需要设置
     */
    private String Enqr_StDt;//<![CDATA[20181001]]></Enqr_StDt>
    /**
     * #查询终止日期  DF数据格式:yyyymmdd
     * 需要设置
     */
    private String Enqr_CODt;//<![CDATA[20181009]]></Enqr_CODt>
    /**
     * 付款方客户账号(水司帐号)
     * 需要设置
     */
    private String Pyr_Cst_AccNo;//<![CDATA[130016101050519642]]></Pyr_Cst_AccNo>
    /**
     * 付款方帐户名称
     * 需要设置
     */
    private String Pyr_AccNm;//<![CDATA[公司某某]]></Pyr_AccNm>
    /**
     * 收款方客户帐号
     * 需要设置
     */
    private String RcvPrt_Cst_AccNo;//<![CDATA[13001635808050514014]]></RcvPrt_Cst_AccNo>
    /**
     * 收款方账户名称
     * 需要设置
     */
    private String RcvPtAc_Nm;//<![CDATA[公司九七]]></RcvPtAc_Nm>
    /**
     * #客户方支付流水号 由营收系统自行提供
     * 需要设置
     */
    private String CstPty_Py_Jrnl_No;//<![CDATA[1484304938975]]></CstPty_Py_Jrnl_No>
    
    
	public String getCst_ID() {
		return Cst_ID;
	}
	public void setCst_ID(String cst_ID) {
		Cst_ID = cst_ID;
	}
	public String getCCstTr_ID() {
		return CCstTr_ID;
	}
	public void setCCstTr_ID(String cCstTr_ID) {
		CCstTr_ID = cCstTr_ID;
	}
	public String getCCstTrNdID() {
		return CCstTrNdID;
	}
	public void setCCstTrNdID(String cCstTrNdID) {
		CCstTrNdID = cCstTrNdID;
	}
	public String getEnqr_StDt() {
		return Enqr_StDt;
	}
	public void setEnqr_StDt(String enqr_StDt) {
		Enqr_StDt = enqr_StDt;
	}
	public String getEnqr_CODt() {
		return Enqr_CODt;
	}
	public void setEnqr_CODt(String enqr_CODt) {
		Enqr_CODt = enqr_CODt;
	}
	public String getPyr_Cst_AccNo() {
		return Pyr_Cst_AccNo;
	}
	public void setPyr_Cst_AccNo(String pyr_Cst_AccNo) {
		Pyr_Cst_AccNo = pyr_Cst_AccNo;
	}
	public String getPyr_AccNm() {
		return Pyr_AccNm;
	}
	public void setPyr_AccNm(String pyr_AccNm) {
		Pyr_AccNm = pyr_AccNm;
	}
	public String getRcvPrt_Cst_AccNo() {
		return RcvPrt_Cst_AccNo;
	}
	public void setRcvPrt_Cst_AccNo(String rcvPrt_Cst_AccNo) {
		RcvPrt_Cst_AccNo = rcvPrt_Cst_AccNo;
	}
	public String getRcvPtAc_Nm() {
		return RcvPtAc_Nm;
	}
	public void setRcvPtAc_Nm(String rcvPtAc_Nm) {
		RcvPtAc_Nm = rcvPtAc_Nm;
	}
	public String getCstPty_Py_Jrnl_No() {
		return CstPty_Py_Jrnl_No;
	}
	public void setCstPty_Py_Jrnl_No(String cstPty_Py_Jrnl_No) {
		CstPty_Py_Jrnl_No = cstPty_Py_Jrnl_No;
	}
    
    
}
