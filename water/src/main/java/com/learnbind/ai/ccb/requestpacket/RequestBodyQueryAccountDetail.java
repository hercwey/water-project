package com.learnbind.ai.ccb.requestpacket;

//目的:按活期存款账号查询活期账户明细信息
//定义:通过账户名称、账号等信息查询活期账户明细信息|分页
//范围:渠道：电子渠道；直连渠道客户：对公客户；产品： 信息报告
/**
 * 
 * 功能描述: 请求账户明细BODY
 * 业务代码:P1CMSER65 
 * 请求报文 BODY  
 * @author lenovo
 * 
 * 需要配置的参数:8个
 */
public class RequestBodyQueryAccountDetail {
	
	/**
	 * 可售产品编码
	 */
	private String  ASPD_ECD="00000886";  //<!![CDATA[00000886]]></ASPD_ECD>
    /**
     * #子渠道号
     */
    private String  SChl_No="000000000000000";  //<!![CDATA[000000000000000]]></SChl_No>
    private String  FwCtl_Node_ID="000000000000000";  //<!![CDATA[000000000000000]]></FwCtl_Node_ID>
    /**
     * 客户编号
     * 需要设置
     */
    private String  Cst_ID;  //<!![CDATA[973850458304234973]]></Cst_ID>
    /**
     * 
     */
    private String  SvM24Hr_Ind="0";  //<!![CDATA[0]]></SvM24Hr_Ind>
    /**
     * 
     */
    private String  Tmzon_ECD="08";  //<!![CDATA[08]]></Tmzon_ECD>
    /**
     * 
     */
    private String  Cmpt_Ent_ID="0000CN000";  //<!![CDATA[0000CN000]]></Cmpt_Ent_ID>
    /**
     * 现金客户树编号
     * 需要设置
     */
    private String  CCstTr_ID;  //<!![CDATA[CMN0000093068]]></CCstTr_ID>
    /**
     * 现金客户树节点编号
     * 需要设置
     */
    private String  CCstTrNdID;  //<!![CDATA[ND97385045830423497300001]]></CCstTrNdID>
    /**
     * #循环记录条数
     * ???需要设置
     */
    private String  Rvl_Rcrd_Num="1";  //<!![CDATA[1]]></Rvl_Rcrd_Num>
    /**
     * 开始日期  DF数据格式:yyyymmdd
     * 需要设置
     */
    private String  StDt;  //<!![CDATA[20181001]]></StDt>
    /**
     * 结束日期  DF数据格式:yyyymmdd
     * 需要设置  
     */
    private String  EdDt;  //<!![CDATA[20181009]]></EdDt>
    private String  Amt_LwrLmt_Val;  //<!![CDATA[]]></Amt_LwrLmt_Val>
    private String  ZONE_TP;  //<!![CDATA[]]></ZONE_TP>
    private String  Amt_UpLm_Val;  //<!![CDATA[]]></Amt_UpLm_Val>
    private String  DbtCrDrcCd;  //<!![CDATA[]]></DbtCrDrcCd>
    /**
     * 对方账户名称
     * 需要设置
     */
    private String  Cntrprt_AccNm;  //<!![CDATA[公司九七]]></Cntrprt_AccNm>
    /**
     * 帐户帐号
     * 需要设置
     */
    private String  Cntrprt_Acc;  //<!![CDATA[13001635808050514014]]></Cntrprt_Acc>
    private String  Enqr_Dtl_TpCd;  //<!![CDATA[]]></Enqr_Dtl_TpCd>
    private String  CcyCd;  //<!![CDATA[]]></CcyCd>
    /**
     * #精确查询标志  
     * DF数据格式:1!n0：否1：是
     */
    private String  Acrt_Enqr_Ind="1";  //<!![CDATA[1]]></Acrt_Enqr_Ind>
    private String  Rmrk;  //<!![CDATA[]]></Rmrk>
    private String  Rvrs_Txn_Ind;  //<!![CDATA[]]></Rvrs_Txn_Ind>
    
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
	public String getRvl_Rcrd_Num() {
		return Rvl_Rcrd_Num;
	}
	public void setRvl_Rcrd_Num(String rvl_Rcrd_Num) {
		Rvl_Rcrd_Num = rvl_Rcrd_Num;
	}
	public String getStDt() {
		return StDt;
	}
	public void setStDt(String stDt) {
		StDt = stDt;
	}
	public String getEdDt() {
		return EdDt;
	}
	public void setEdDt(String edDt) {
		EdDt = edDt;
	}
	public String getAmt_LwrLmt_Val() {
		return Amt_LwrLmt_Val;
	}
	public void setAmt_LwrLmt_Val(String amt_LwrLmt_Val) {
		Amt_LwrLmt_Val = amt_LwrLmt_Val;
	}
	public String getZONE_TP() {
		return ZONE_TP;
	}
	public void setZONE_TP(String zONE_TP) {
		ZONE_TP = zONE_TP;
	}
	public String getAmt_UpLm_Val() {
		return Amt_UpLm_Val;
	}
	public void setAmt_UpLm_Val(String amt_UpLm_Val) {
		Amt_UpLm_Val = amt_UpLm_Val;
	}
	public String getDbtCrDrcCd() {
		return DbtCrDrcCd;
	}
	public void setDbtCrDrcCd(String dbtCrDrcCd) {
		DbtCrDrcCd = dbtCrDrcCd;
	}
	public String getCntrprt_AccNm() {
		return Cntrprt_AccNm;
	}
	public void setCntrprt_AccNm(String cntrprt_AccNm) {
		Cntrprt_AccNm = cntrprt_AccNm;
	}
	public String getCntrprt_Acc() {
		return Cntrprt_Acc;
	}
	public void setCntrprt_Acc(String cntrprt_Acc) {
		Cntrprt_Acc = cntrprt_Acc;
	}
	public String getEnqr_Dtl_TpCd() {
		return Enqr_Dtl_TpCd;
	}
	public void setEnqr_Dtl_TpCd(String enqr_Dtl_TpCd) {
		Enqr_Dtl_TpCd = enqr_Dtl_TpCd;
	}
	public String getCcyCd() {
		return CcyCd;
	}
	public void setCcyCd(String ccyCd) {
		CcyCd = ccyCd;
	}
	public String getAcrt_Enqr_Ind() {
		return Acrt_Enqr_Ind;
	}
	public void setAcrt_Enqr_Ind(String acrt_Enqr_Ind) {
		Acrt_Enqr_Ind = acrt_Enqr_Ind;
	}
	public String getRmrk() {
		return Rmrk;
	}
	public void setRmrk(String rmrk) {
		Rmrk = rmrk;
	}
	public String getRvrs_Txn_Ind() {
		return Rvrs_Txn_Ind;
	}
	public void setRvrs_Txn_Ind(String rvrs_Txn_Ind) {
		Rvrs_Txn_Ind = rvrs_Txn_Ind;
	}
    
    

}
