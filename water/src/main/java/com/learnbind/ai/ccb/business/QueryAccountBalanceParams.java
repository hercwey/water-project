package com.learnbind.ai.ccb.business;

public class QueryAccountBalanceParams {
	/**
	 * 客户编号  18!n,客户通过签约 唯一确认，固定
	 * 需要设置
	 */
	private String Cst_ID;		//<![CDATA[973850458304234973]]></Cst_ID>
	/**
	 * 现金客户树编号  CMN+10位顺序号，客户通过签约唯一确认，固定
	 * 需要设置
	 */
	private String CCstTr_ID;	//<![CDATA[CMN0000093068]]></CCstTr_ID>
	/**
	 * 现金客户树节点编号  ND+18位客户编号+5位顺序号，客户通过签约唯一确认，固定
	 * 需要设置
	 */
	private String CCstTrNdID;	//<![CDATA[ND97385045830423497300001]]></CCstTrNdID>
	/**
	 * 电子合约编号
	 * 
	 * 需要设置 
	 */
	private String ElcSubAR_ID;	//<![CDATA[HE13000009021440001]]></ElcSubAR_ID>
	
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
	public String getElcSubAR_ID() {
		return ElcSubAR_ID;
	}
	public void setElcSubAR_ID(String elcSubAR_ID) {
		ElcSubAR_ID = elcSubAR_ID;
	}
	
	
}
