package com.learnbind.ai.ccb.requestpacket;

/**
 * 查询帐户余额  请求报文  BODY  P1CMSER18
 * @author lenovo
 * 需要配置的参数个数:4
 */
public class RequestBodyQueryAccountBalance {
	
	/**
	 * 客户编号
	 * 需要设置
	 */
	private String Cst_ID;		//<![CDATA[973850458304234973]]></Cst_ID>
	/**
	 * 现金管理信息服务
	 */	
	private String ASPD_ECD="00000886";	//<![CDATA[00000886]]></ASPD_ECD>
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
	 * 需要设置 
	 */
	private String ElcSubAR_ID;	//<![CDATA[HE13000009021440001]]></ElcSubAR_ID>
	/**
	 * #循环记录条数
	 */
	private String Rvl_Rcrd_Num="1";	//<![CDATA[1]]></Rvl_Rcrd_Num>
	
	
	public String getCst_ID() {
		return Cst_ID;
	}
	public void setCst_ID(String cst_ID) {
		Cst_ID = cst_ID;
	}
	public String getASPD_ECD() {
		return ASPD_ECD;
	}
	public void setASPD_ECD(String aSPD_ECD) {
		ASPD_ECD = aSPD_ECD;
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
	public String getRvl_Rcrd_Num() {
		return Rvl_Rcrd_Num;
	}
	public void setRvl_Rcrd_Num(String rvl_Rcrd_Num) {
		Rvl_Rcrd_Num = rvl_Rcrd_Num;
	}
	
	
	
	

}
