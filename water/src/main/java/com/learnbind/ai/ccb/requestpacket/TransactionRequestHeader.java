package com.learnbind.ai.ccb.requestpacket;

/**
 * 
 * 请求报文头(Transaction header)  header
 * 
 * @author lenovo
 *
 */
public class TransactionRequestHeader {	
	
	/**
	 * 服务名  定长
	 */
	private String SYS_TX_CODE="P1OPME001";  //<![CDATA[P1OPME001]]></SYS_TX_CODE>
    /**
     * 应用报文长度
     * 定长10位，不足10位前面充0
     */
    private String SYS_MSG_LEN="0000000000";   			//<![CDATA[0000001004]]></SYS_MSG_LEN>
    /**
     * 发起方交易时间
     * YYYYMMDDHHMMSSXXX   XXX:毫秒数
     */
    private String SYS_REQ_TIME;  			//<![CDATA[20181009114504215]]></SYS_REQ_TIME>
    /**
     * 版本号  固定值
     */
    private String SYS_TX_VRSN=CCBTransaction.TRANSACTION_SYS_TX_VRSN;		//<![CDATA[01]]></SYS_TX_VRSN>
    /**
     * 交易日期  YYYYMMDD
     */
    private String TXN_DT;					//<![CDATA[20181009]]></TXN_DT>
    /**
     * 交易时间 
     * HHMMSS
     */
    private String TXN_TM;					//<![CDATA[114504]]></TXN_TM>
    /**
     * 交易人员编号
     */
    private String TXN_STFF_ID;				//<![CDATA[333333]]></TXN_STFF_ID>
    /**
     * 多实体标识
     */
    private String MULTI_TENANCY_ID=CCBTransaction.TRANSACTION_MULTI_TENANCY_ID;		//<![CDATA[CN000]]></MULTI_TENANCY_ID>
    /**
     * 语言标识
     */
    private String LNG_ID=CCBTransaction.TRANSACTION_LNG_ID;			//<![CDATA[zh-cn]]></LNG_ID>
    /**
     * 多页查询每页笔数
     * 分页查询时使用
     */
    private String REC_IN_PAGE="";			//<![CDATA[]]></REC_IN_PAGE>
    /**
     * 多页查询跳转页码
     */
    private String PAGE_JUMP="";			//<![CDATA[]]></PAGE_JUMP>
    /**
     * 状态跟踪号
     */
    private String STS_TRACE_ID="";			//<![CDATA[]]></STS_TRACE_ID>
    /**
     * 电子银行合约编号
     */
    private String CHNL_CUST_NO;			//<![CDATA[HE13000009021440001]]></CHNL_CUST_NO>
    /**
     * 发起方流水号
     */
    private String IttParty_Jrnl_No="";		//<![CDATA[]]></IttParty_Jrnl_No>
    /**
     * 交易发起方 IP 地址
     */
    private String Txn_Itt_IP_Adr;			//<![CDATA[121.28.4.58]]></Txn_Itt_IP_Adr>
    
    
	public String getSYS_TX_CODE() {
		return SYS_TX_CODE;
	}
	public void setSYS_TX_CODE(String sYS_TX_CODE) {
		SYS_TX_CODE = sYS_TX_CODE;
	}
	public String getSYS_MSG_LEN() {
		return SYS_MSG_LEN;
	}
	public void setSYS_MSG_LEN(String sYS_MSG_LEN) {
		SYS_MSG_LEN = sYS_MSG_LEN;
	}
	public String getSYS_REQ_TIME() {
		return SYS_REQ_TIME;
	}
	public void setSYS_REQ_TIME(String sYS_REQ_TIME) {
		SYS_REQ_TIME = sYS_REQ_TIME;
	}
	public String getSYS_TX_VRSN() {
		return SYS_TX_VRSN;
	}
	public void setSYS_TX_VRSN(String sYS_TX_VRSN) {
		SYS_TX_VRSN = sYS_TX_VRSN;
	}
	public String getTXN_DT() {
		return TXN_DT;
	}
	public void setTXN_DT(String tXN_DT) {
		TXN_DT = tXN_DT;
	}
	public String getTXN_TM() {
		return TXN_TM;
	}
	public void setTXN_TM(String tXN_TM) {
		TXN_TM = tXN_TM;
	}
	public String getTXN_STFF_ID() {
		return TXN_STFF_ID;
	}
	public void setTXN_STFF_ID(String tXN_STFF_ID) {
		TXN_STFF_ID = tXN_STFF_ID;
	}
	public String getMULTI_TENANCY_ID() {
		return MULTI_TENANCY_ID;
	}
	public void setMULTI_TENANCY_ID(String mULTI_TENANCY_ID) {
		MULTI_TENANCY_ID = mULTI_TENANCY_ID;
	}
	public String getLNG_ID() {
		return LNG_ID;
	}
	public void setLNG_ID(String lNG_ID) {
		LNG_ID = lNG_ID;
	}
	public String getREC_IN_PAGE() {
		return REC_IN_PAGE;
	}
	public void setREC_IN_PAGE(String rEC_IN_PAGE) {
		REC_IN_PAGE = rEC_IN_PAGE;
	}
	public String getPAGE_JUMP() {
		return PAGE_JUMP;
	}
	public void setPAGE_JUMP(String pAGE_JUMP) {
		PAGE_JUMP = pAGE_JUMP;
	}
	public String getSTS_TRACE_ID() {
		return STS_TRACE_ID;
	}
	public void setSTS_TRACE_ID(String sTS_TRACE_ID) {
		STS_TRACE_ID = sTS_TRACE_ID;
	}
	public String getCHNL_CUST_NO() {
		return CHNL_CUST_NO;
	}
	public void setCHNL_CUST_NO(String cHNL_CUST_NO) {
		CHNL_CUST_NO = cHNL_CUST_NO;
	}
	public String getIttParty_Jrnl_No() {
		return IttParty_Jrnl_No;
	}
	public void setIttParty_Jrnl_No(String ittParty_Jrnl_No) {
		IttParty_Jrnl_No = ittParty_Jrnl_No;
	}
	public String getTxn_Itt_IP_Adr() {
		return Txn_Itt_IP_Adr;
	}
	public void setTxn_Itt_IP_Adr(String txn_Itt_IP_Adr) {
		Txn_Itt_IP_Adr = txn_Itt_IP_Adr;
	}
    
    
    
}
