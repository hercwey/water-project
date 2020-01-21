package com.learnbind.ai.ccb.requestpacket;

/**
 * 查询代扣单据请求报文 BODY
 * 
 * @author lenovo
 *
 */


//No.54、代发代扣直联单据查询						
//服务类型	联机服务	服务ID	P1CLP1054	通用域引用范围	com1  com4 comB	
//功能描述	用于直连客户根据查询条件分页查询凭证状态信息					



//功能描述	用于直连客户根据查询条件分页查询凭证状态信息								
//
//请求报文									
		//栏位项目名称	中文名称				数据项编号	输入长度		栏位属性		必须			补充说明			
		//Txn_SN		客户序列号			113000		20				C		N			客户新增单据时传入标识凭证的唯一客户序列号，企业网银保存客户序列号与凭证号的对应关系			
		//EBnk_SvAr_ID	电子银行服务合约编号	106120		23				C		Y			客户在建行的客户号（单位编号）			
		//EtrUnt_AccNo	委托单位账号			117404		32				C		N			客户在建行所签项目的结算帐号			
		//VchID			凭证编号				108720		150				C		N			由建行网银代收付流程组件生成的凭证编号，用于标识一笔代发代扣单据			
		//Vchr_StDt		凭证起始日期			117467		8				C		N			查询一段时间内凭证信息需要输入			
		//Vchr_CODt		凭证截止日期			117468		8				C		N			查询一段时间内凭证信息需要输入			
		//VCHR_TP_CODE	凭证类型				101388		4				C		N			0-单笔 1-批量			
		//Vchr_St		凭证状态				117441		1500			C		N			100-不确定，200复核中，600复核不通过，单据删除 400终审成功，等待账务系统处理 700 成功 800 失败			
		//Vchr_Beg_Amt	凭证起始金额			117469		20				C		N			查询金额区间内的凭证信息需要输入			
		//Vchr_CtOf_Amt	凭证截止金额			117470		20				C		N			查询金额区间内的凭证信息需要输入			
		//Entrst_Prj_ID	委托项目编号			117245		120				C		N			客户在建行所签项目的项目编号			
		//TDP_ID		制单员编号			117348		19				C		N			查询某个制单员所制凭证			
//SBSTRCVPY_PRJ_TPCD	代收付项目类型代码	104435		4				C		N			0：代发 1：代扣 			
//IntrBnk_IndCd			跨行标志代码			104493		2				C		N			查询建行行内或跨行凭证	空-全部;0-行内;1-二代支付跨行；2-大小额跨行		

public class RequestBodyQueryCertificateState {

	/**
	 * 交易号码,由企业端自动生成(发送至CCB)
	 * 
	 */
	private String Txn_SN; // <![CDATA[113810458305358140]]></Txn_SN>
	/**
	 * 电子银行合约编号:need set
	 */
	private String EBnk_SvAr_ID; // <![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>
	/**
	 * 委托单位帐号: need set
	 */
	private String EtrUnt_AccNo; // <![CDATA[13001618801050519642]]></EtrUnt_AccNo>
	/**
	 * 
	 */
	private String VchID; // <![CDATA[]]></VchID>
	private String Vchr_StDt; // <![CDATA[]]></Vchr_StDt>
	private String Vchr_CODt; // <![CDATA[]]></Vchr_CODt>
	private String VCHR_TP_CODE; // <![CDATA[]]></VCHR_TP_CODE>
	private String Vchr_St; // <![CDATA[]]></Vchr_St>
	private String Vchr_Beg_Amt; // <![CDATA[]]></Vchr_Beg_Amt>
	private String Vchr_CtOf_Amt; // <![CDATA[]]></Vchr_CtOf_Amt>
	/**
	 * 委托项目编号  need set
	 */
	private String Entrst_Prj_ID; // <![CDATA[130130070]]></Entrst_Prj_ID>
	private String TDP_ID; // <![CDATA[]]></TDP_ID>
	private String SBSTRCVPY_PRJ_TPCD="1"; // <![CDATA[]]></SBSTRCVPY_PRJ_TPCD>  //代扣
	private String IntrBnk_IndCd; // <![CDATA[]]></IntrBnk_IndCd>
	
	public String getTxn_SN() {
		return Txn_SN;
	}
	public void setTxn_SN(String txn_SN) {
		Txn_SN = txn_SN;
	}
	public String getEBnk_SvAr_ID() {
		return EBnk_SvAr_ID;
	}
	public void setEBnk_SvAr_ID(String eBnk_SvAr_ID) {
		EBnk_SvAr_ID = eBnk_SvAr_ID;
	}
	public String getEtrUnt_AccNo() {
		return EtrUnt_AccNo;
	}
	public void setEtrUnt_AccNo(String etrUnt_AccNo) {
		EtrUnt_AccNo = etrUnt_AccNo;
	}
	public String getVchID() {
		return VchID;
	}
	public void setVchID(String vchID) {
		VchID = vchID;
	}
	public String getVchr_StDt() {
		return Vchr_StDt;
	}
	public void setVchr_StDt(String vchr_StDt) {
		Vchr_StDt = vchr_StDt;
	}
	public String getVchr_CODt() {
		return Vchr_CODt;
	}
	public void setVchr_CODt(String vchr_CODt) {
		Vchr_CODt = vchr_CODt;
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
	public String getVchr_Beg_Amt() {
		return Vchr_Beg_Amt;
	}
	public void setVchr_Beg_Amt(String vchr_Beg_Amt) {
		Vchr_Beg_Amt = vchr_Beg_Amt;
	}
	public String getVchr_CtOf_Amt() {
		return Vchr_CtOf_Amt;
	}
	public void setVchr_CtOf_Amt(String vchr_CtOf_Amt) {
		Vchr_CtOf_Amt = vchr_CtOf_Amt;
	}
	public String getEntrst_Prj_ID() {
		return Entrst_Prj_ID;
	}
	public void setEntrst_Prj_ID(String entrst_Prj_ID) {
		Entrst_Prj_ID = entrst_Prj_ID;
	}
	public String getTDP_ID() {
		return TDP_ID;
	}
	public void setTDP_ID(String tDP_ID) {
		TDP_ID = tDP_ID;
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
	
	

}
