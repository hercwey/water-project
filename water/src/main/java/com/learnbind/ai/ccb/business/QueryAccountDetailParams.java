package com.learnbind.ai.ccb.business;

/**
 * 查询帐户明细参数对象
 * 业务代码:P1CMSER65
 * @author lenovo
 *
 */
public class QueryAccountDetailParams {
	/**
     * 客户编号
     * 需要设置
     */
    private String  Cst_ID;  //<!![CDATA[973850458304234973]]></Cst_ID>
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
     * 开始日期  DF数据格式:yyyymmdd
     * 需要设置
     */
    private String  StDt;  //<!![CDATA[20181001]]></StDt>
    /**
     * 结束日期  DF数据格式:yyyymmdd
     * 需要设置  
     */
    private String  EdDt;  //<!![CDATA[20181009]]></EdDt>
    
    /**
     * ------------------
     * 对方账户名称
     * -----------------
     * 需要设置
     * 
     * 注:此处为水司在建设银行开立的账户名称
     */
    private String  Cntrprt_AccNm;  //<!![CDATA[公司九七]]></Cntrprt_AccNm>
    /**
     * --------------
     * 对方账户
     * --------------
     * 需要设置
     * 
     * 注:此处为水司在建设银行开立的账户-账号
     */
    private String  Cntrprt_Acc;  //<!![CDATA[13001635808050514014]]></Cntrprt_Acc>
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
    
    
}
