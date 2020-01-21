package com.learnbind.ai.ccb.business;

/**
 * 转帐参数
 * 注:此处的参数名称需与RequestBodyTransfer.java中字段名称相同.
 * @author lenovo
 * 
 */
public class TransferParams {
	
    /**
     * 客户编号  8!n,客户通过签约 唯一确认，固定
     * 需要设置
     */
    private String   Cst_ID;			//<![CDATA[973850458304234973]]></Cst_ID>
    /**
     * 现金客户树编号 13!an CMN+10位顺序号，客户通过签约唯一确认，固定
     * 需要设置
     */
    private String   CCstTr_ID;			//<![CDATA[CMN0000093068]]></CCstTr_ID>
    /**
     * 现金客户树节点编号   25!an ND+18位客户编号+5位顺序号，客户通过签约唯一确认，固定
     * 需要设置
     */
    private String   CCstTrNdID;			//<![CDATA[ND97385045830423497300001]]></CCstTrNdID>
    /**
     * ------------------------------------
     * #客户方交易流水号  DF数据格式：an..60
     * ----------------------------------- 
     * 需要设置
     * 
     * 注:此序列号由上层业务系统自行生成.用于标识此次交易.
     */
    private String   CstPty_TxnSrlNo;//<![CDATA[1484304938977]]></CstPty_TxnSrlNo>
    /**
     * 付款方行别代码 （01本行02国内他行03国外他行）DF数据格式：2!n
     * 需要设置
     * 注:由于此项目中为本行内部转账,此值暂定为   01<--->本行
     */
    private String   Pyr_BkCgyCd;//<![CDATA[01]]></Pyr_BkCgyCd>
    /**
     * --------------------
     * 付款方客户账号
     * --------------------
     * 需要设置
     */
    private String   Pyr_Cst_AccNo;//<![CDATA[13001635808050514014]]></Pyr_Cst_AccNo>
    /**
     * 付款方账户名称
     * 需要设置
     */
    private String   Pyr_AccNm;//<![CDATA[公司九七]]></Pyr_AccNm>
    
    /**
     * 收款方行别代码 （01本行 02国内他行 03国外他行）DF数据格式：2!n
     * 需要设置
     */
    private String   RcvPrt_BkCgyCd;//<![CDATA[01]]></RcvPrt_BkCgyCd>
    /**
     * --------------------
     * 收款方客户账号
     * --------------------
     * 需要设置
     */
    private String   RcvPrt_Cst_AccNo;//<![CDATA[130016101050519604]]></RcvPrt_Cst_AccNo>
    /**
     * 收款方账户名称
     * 需要设置
     */
    private String   RcvPtAc_Nm;//<![CDATA[公司某某]]></RcvPtAc_Nm>
    /**
     * ----------------------
     * 请求金额
     * ----------------------
     * 需要设置
     */
    private String   Rqs_Amt;//<![CDATA[190.00]]></Rqs_Amt>
    /**
     * 备注
     * 需要设置
     */
    private String   Rmrk;//<![CDATA[11]]></Rmrk>
    
    /**
     * #客户方支付流水号(暂定:与客户方交易流水号相同) 
     * 需要设置 
     */
    private String   CstPty_Py_Jrnl_No;  //>1484304938977</CstPty_Py_Jrnl_No>	

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

	public String getCstPty_TxnSrlNo() {
		return CstPty_TxnSrlNo;
	}

	public void setCstPty_TxnSrlNo(String cstPty_TxnSrlNo) {
		CstPty_TxnSrlNo = cstPty_TxnSrlNo;
	}

	public String getPyr_BkCgyCd() {
		return Pyr_BkCgyCd;
	}

	public void setPyr_BkCgyCd(String pyr_BkCgyCd) {
		Pyr_BkCgyCd = pyr_BkCgyCd;
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

	public String getRcvPrt_BkCgyCd() {
		return RcvPrt_BkCgyCd;
	}

	public void setRcvPrt_BkCgyCd(String rcvPrt_BkCgyCd) {
		RcvPrt_BkCgyCd = rcvPrt_BkCgyCd;
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

	public String getRqs_Amt() {
		return Rqs_Amt;
	}

	public void setRqs_Amt(String rqs_Amt) {
		Rqs_Amt = rqs_Amt;
	}

	public String getRmrk() {
		return Rmrk;
	}

	public void setRmrk(String rmrk) {
		Rmrk = rmrk;
	}

	public String getCstPty_Py_Jrnl_No() {
		return CstPty_Py_Jrnl_No;
	}

	public void setCstPty_Py_Jrnl_No(String cstPty_Py_Jrnl_No) {
		CstPty_Py_Jrnl_No = cstPty_Py_Jrnl_No;
	}
    
	
}
