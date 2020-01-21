package com.learnbind.ai.ccb.requestpacket;

/**
 * 功能:查询转账结果 请求报文   
 * 业务代码:P1CMSET36  BODY
 * @author lenovo
 * 10个参数需要设置
 */
public class RequestBodyQueryTransferResult {
	
	/**
	 * 可售产品编码  8!n
		常用值如下：
			客户内部账户管理业务 00000879
			票据池 00000880
			现金管理收款	00000881
			现金管理付款00000882
			定时资金池00000883
			资金预算管理00000887
			现金管理信息服务00000886
			智能理财现金池00104080
			价格 99999999
			第三方支付00000915
			账簿现金池00107826
	 */
	private String ASPD_ECD="00000886";//<![CDATA[00000886]]></ASPD_ECD>
    /**
     * 
     */
    private String SChl_No="000000000000000";//<![CDATA[000000000000000]]></SChl_No>
    /**
     * 
     */
    private String FwCtl_Node_ID="000000000000000";//<![CDATA[000000000000000]]></FwCtl_Node_ID>
    /**
     * 客户编号
     * 需要设置(可放在CCB配置中)
     */
    private String Cst_ID;//<![CDATA[973850458304234973]]></Cst_ID>
    private String SvM24Hr_Ind="0";//<![CDATA[0]]></SvM24Hr_Ind>
    private String Tmzon_ECD="08";//<![CDATA[08]]></Tmzon_ECD>
    private String Cmpt_Ent_ID="0000CN000";//<![CDATA[0000CN000]]></Cmpt_Ent_ID>
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
     * #查询日期类型代码  
     * 		1-录入日期
     * 		2-提交日期
     * 		组件关联流水号为空时，此项必输DF数据格式：1!n
     */
    private String Enqr_Dt_TpCd="2";//<![CDATA[2]]></Enqr_Dt_TpCd>
    
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
    private String CshMgt_Txn_Rslt_Cd;//<![CDATA[]]></CshMgt_Txn_Rslt_Cd>
    private String Enqr_Strt_Amt;//<![CDATA[]]></Enqr_Strt_Amt>
    private String Tmt_Amt;//<![CDATA[]]></Tmt_Amt>
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
    private String Cmpt_Rltv_Jrnl_No;//<![CDATA[]]></Cmpt_Rltv_Jrnl_No>
    /**
     * #客户方支付流水号 由营收系统自行提供
     * 需要设置
     */
    private String CstPty_Py_Jrnl_No;//<![CDATA[1484304938975]]></CstPty_Py_Jrnl_No>
    /**
     * 付款条件类型代码
     */
    private String Py_Cnd_TpCd="00" ;//<![CDATA[00]]></Py_Cnd_TpCd >
    private String Enqr_Strt_STBal_Val ;//<![CDATA[]]></Enqr_Strt_STBal_Val >
    private String Enqr_Tmt_STBal_Val ;//<![CDATA[]]></Enqr_Tmt_STBal_Val >
    private String Enqr_Strt_Scl_Ceft ;//<![CDATA[]]></Enqr_Strt_Scl_Ceft >
    private String Enqr_Tmt_Scl_Ceft ;//<![CDATA[]]></Enqr_Tmt_Scl_Ceft >
    private String RoudNbrOfBit  ;//<![CDATA[]]></RoudNbrOfBit  >
    
	public String getASPD_ECD() {
		return ASPD_ECD;
	}
	public void setASPD_ECD(String aSPD_ECD) {
		ASPD_ECD = aSPD_ECD;
	}
	public String getSChl_No() {
		return SChl_No;
	}
	public void setSChl_No(String sChl_No) {
		SChl_No = sChl_No;
	}
	public String getFwCtl_Node_ID() {
		return FwCtl_Node_ID;
	}
	public void setFwCtl_Node_ID(String fwCtl_Node_ID) {
		FwCtl_Node_ID = fwCtl_Node_ID;
	}
	public String getCst_ID() {
		return Cst_ID;
	}
	public void setCst_ID(String cst_ID) {
		Cst_ID = cst_ID;
	}
	public String getSvM24Hr_Ind() {
		return SvM24Hr_Ind;
	}
	public void setSvM24Hr_Ind(String svM24Hr_Ind) {
		SvM24Hr_Ind = svM24Hr_Ind;
	}
	public String getTmzon_ECD() {
		return Tmzon_ECD;
	}
	public void setTmzon_ECD(String tmzon_ECD) {
		Tmzon_ECD = tmzon_ECD;
	}
	public String getCmpt_Ent_ID() {
		return Cmpt_Ent_ID;
	}
	public void setCmpt_Ent_ID(String cmpt_Ent_ID) {
		Cmpt_Ent_ID = cmpt_Ent_ID;
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
	public String getEnqr_Dt_TpCd() {
		return Enqr_Dt_TpCd;
	}
	public void setEnqr_Dt_TpCd(String enqr_Dt_TpCd) {
		Enqr_Dt_TpCd = enqr_Dt_TpCd;
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
	public String getCshMgt_Txn_Rslt_Cd() {
		return CshMgt_Txn_Rslt_Cd;
	}
	public void setCshMgt_Txn_Rslt_Cd(String cshMgt_Txn_Rslt_Cd) {
		CshMgt_Txn_Rslt_Cd = cshMgt_Txn_Rslt_Cd;
	}
	public String getEnqr_Strt_Amt() {
		return Enqr_Strt_Amt;
	}
	public void setEnqr_Strt_Amt(String enqr_Strt_Amt) {
		Enqr_Strt_Amt = enqr_Strt_Amt;
	}
	public String getTmt_Amt() {
		return Tmt_Amt;
	}
	public void setTmt_Amt(String tmt_Amt) {
		Tmt_Amt = tmt_Amt;
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
	public String getCmpt_Rltv_Jrnl_No() {
		return Cmpt_Rltv_Jrnl_No;
	}
	public void setCmpt_Rltv_Jrnl_No(String cmpt_Rltv_Jrnl_No) {
		Cmpt_Rltv_Jrnl_No = cmpt_Rltv_Jrnl_No;
	}
	public String getCstPty_Py_Jrnl_No() {
		return CstPty_Py_Jrnl_No;
	}
	public void setCstPty_Py_Jrnl_No(String cstPty_Py_Jrnl_No) {
		CstPty_Py_Jrnl_No = cstPty_Py_Jrnl_No;
	}
	public String getPy_Cnd_TpCd() {
		return Py_Cnd_TpCd;
	}
	public void setPy_Cnd_TpCd(String py_Cnd_TpCd) {
		Py_Cnd_TpCd = py_Cnd_TpCd;
	}
	public String getEnqr_Strt_STBal_Val() {
		return Enqr_Strt_STBal_Val;
	}
	public void setEnqr_Strt_STBal_Val(String enqr_Strt_STBal_Val) {
		Enqr_Strt_STBal_Val = enqr_Strt_STBal_Val;
	}
	public String getEnqr_Tmt_STBal_Val() {
		return Enqr_Tmt_STBal_Val;
	}
	public void setEnqr_Tmt_STBal_Val(String enqr_Tmt_STBal_Val) {
		Enqr_Tmt_STBal_Val = enqr_Tmt_STBal_Val;
	}
	public String getEnqr_Strt_Scl_Ceft() {
		return Enqr_Strt_Scl_Ceft;
	}
	public void setEnqr_Strt_Scl_Ceft(String enqr_Strt_Scl_Ceft) {
		Enqr_Strt_Scl_Ceft = enqr_Strt_Scl_Ceft;
	}
	public String getEnqr_Tmt_Scl_Ceft() {
		return Enqr_Tmt_Scl_Ceft;
	}
	public void setEnqr_Tmt_Scl_Ceft(String enqr_Tmt_Scl_Ceft) {
		Enqr_Tmt_Scl_Ceft = enqr_Tmt_Scl_Ceft;
	}
	public String getRoudNbrOfBit() {
		return RoudNbrOfBit;
	}
	public void setRoudNbrOfBit(String roudNbrOfBit) {
		RoudNbrOfBit = roudNbrOfBit;
	}
    
    
    

}
