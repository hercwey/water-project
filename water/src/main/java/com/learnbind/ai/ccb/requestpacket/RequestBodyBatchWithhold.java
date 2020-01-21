package com.learnbind.ai.ccb.requestpacket;


//No.51、代发代扣直联交易申请					
//服务类型	联机服务	服务ID	P1CLP1051	通用域引用范围	com1 file_list_pack
//功能描述	用于直连客户发起代发代扣制单交易	

//请求报文						
//栏位项目名称	中文名称	数据项编号	输入长度	栏位属性	必须	补充说明
//Txn_SN	客户序列号	113000	20	C	Y	客户新增单据时传入标识凭证的唯一客户序列号，企业网银保存客户序列号与凭证号的对应关系
//EBnk_SvAr_ID	电子银行服务合约编号	106120	23	C	Y	客户在建行的客户号（单位编号）
//Entrst_Prj_ID	委托项目编号	117245	a	C	Y	客户在建行所签项目的项目编号
//Prj_Use_ID	项目用途编号	117246	600	C	N	"直连客户的代收付合约在签约时必须选择一个特定用途:
//1. 直连代付项目，签约时需设置用途“直连客户代付专用”，此用途编号字段固定传送 'zldf00001'
//2. 直连代收项目，签约时需设置用途“直连客户代收专用”，此用途编号字段固定传送 'zlds00001'
//如请求报文中此字段为空，则根据合约代收/代付类型默认为'zlds00001'或'zldf00001'"
//EtrUnt_AccNo	委托单位账号	117404	32	C	Y	客户在建行所签项目的结算帐号
//TDP_ID	制单员编号	117348	19	C	Y	发起制单的操作员编号
//TDP_Nm	制单员名称	117349	600	C	N	发起制单的操作员名称
//CntprtAcc	对方账号	116283	40	C	N	单笔必须输入
//Cntrprt_AccNm	对方账户名称	103865	600	C	N	单笔必须输入
//IwBk_BrNo	汇入行行号	117315	32	C	N	跨行对方账号开户行号
//IwBk_BkNm	汇入行行名	117316	600	C	N	跨行对方账号开户行名
//MltltAgrm_No	多方协议号	104433	23	C	N	跨行代发代扣需要填写
//CCY_ID	币种编号	104026	20	C	Y	人民币编号：156
//Orig_File_Nm	原始文件名称	601186	600	C	N	批量时必须输入，用于记录客户上传批量文件的原始文件名称
//SRP_TxnAmt	代收付交易金额	104387	16	C	N	单笔必须输入
//SCSP_Smy_Dsc	代收代付摘要描述	104394	1500	C	N	手工摘要
//Rvw_Ind	按批次复核标志	301699	1	C	Y	1-直连客户不需要网银审核（默认一步流程） 2-需要（自定义流程）
//TAmt	总金额	112846	20	C	N	批量文件中明细总金额
//TDnum	总笔数	200187	10	C	N	批量文件中明细总笔数
//VCHR_TP_CODE	凭证类型	101388	4	C	Y	0-单笔 1-批量
//Lng_Vrsn	语言版本	302397	240	C	N	批量文件语言版本：1-简体 2-繁体


/**
 * 
 * 批量代扣请求报文 BODY <Transaction_Body> <request> 节点下的内容
 * 
 * @author lenovo
 *
 */
public class RequestBodyBatchWithhold {
	// private FileListPack fileListPack; //文件部分

	/**
	 * 客户新增单据时传入标识凭证的唯一客户序列号，企业网银保存客户序列号与凭证号的对应关系 
	 * 注(HZ):每次交易时需要先生成一个序列号
	 * 需要设置
	 */
	String Txn_SN = ""; // <![CDATA[113810458305358140]]></Txn_SN>
	/**
	 * 电子银行服务合约编号
	 * 需要设置
	 */
	String EBnk_SvAr_ID; // <![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>
	/**
	 * 委托项目编号
	 * 需要设置
	 */
	String Entrst_Prj_ID; // <![CDATA[130130070]]></Entrst_Prj_ID>
	/**
	 * 项目用途编号
	 * 需要设置
	 */
	String Prj_Use_ID; // <![CDATA[zldf00001]]></Prj_Use_ID>
	/**
	 * 委托单位账号 客户在建行所签项目的结算帐号
	 * 需要设置
	 */
	String EtrUnt_AccNo; // <![CDATA[13001618801050519642]]></EtrUnt_AccNo>
	/**
	 * 操作员编号 
	 * 需要根据项目的实际情况进行变更
	 * 需要设置 
	 */
	String TDP_ID; // <![CDATA[333333]]></TDP_ID>
	String TDP_Nm = ""; // <![CDATA[]]></TDP_Nm>
	String CntprtAcc = ""; // <![CDATA[]]></CntprtAcc>
	String Cntrprt_AccNm = ""; // <![CDATA[]]></Cntrprt_AccNm>
	String IwBk_BrNo = ""; // <![CDATA[]]></IwBk_BrNo>
	String IwBk_BkNm = ""; // <![CDATA[]]></IwBk_BkNm>
	String MltltAgrm_No = ""; // <![CDATA[]]></MltltAgrm_No>
	String CCY_ID = CCBTransaction.TRANSACTION_CCY_ID; // <![CDATA[156]]></CCY_ID>
	/**
	 * 原始文件名称  
	 * 注:批量时必须输入，用于记录客户上传批量文件的原始文件名称
	 * 需要设置
	 */
	String Orig_File_Nm = ""; // <![CDATA[20181011/641/Fil.txt]]></Orig_File_Nm>
	String SRP_TxnAmt = ""; // <![CDATA[]]></SRP_TxnAmt>
	String SCSP_Smy_Dsc = ""; // <![CDATA[]]></SCSP_Smy_Dsc>
	/**
	 * 按批次复核标志 
	 * 1-直连客户不需要网银审核（默认一步流程） 
	 * 2-需要（自定义流程
	 */
	String Rvw_Ind=CCBTransaction.TRANSACTION_Rvw_Ind	;		//<![CDATA[1]]></Rvw_Ind>  
	String TAmt = ""; // <![CDATA[]]></TAmt>
	String TDnum = ""; // <![CDATA[]]></TDnum>
	/**
	 * 凭证类型 0-单笔 1-批量
	 * 需要设置
	 */
	String VCHR_TP_CODE = CCBTransaction.TRANSACTION_VCHR_TP_CODE_BATCH; // <![CDATA[1]]></VCHR_TP_CODE>
	String Lng_Vrsn = ""; // <![CDATA[]]></Lng_Vrsn>
	
	
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
	public String getEntrst_Prj_ID() {
		return Entrst_Prj_ID;
	}
	public void setEntrst_Prj_ID(String entrst_Prj_ID) {
		Entrst_Prj_ID = entrst_Prj_ID;
	}
	public String getPrj_Use_ID() {
		return Prj_Use_ID;
	}
	public void setPrj_Use_ID(String prj_Use_ID) {
		Prj_Use_ID = prj_Use_ID;
	}
	public String getEtrUnt_AccNo() {
		return EtrUnt_AccNo;
	}
	public void setEtrUnt_AccNo(String etrUnt_AccNo) {
		EtrUnt_AccNo = etrUnt_AccNo;
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
	public String getMltltAgrm_No() {
		return MltltAgrm_No;
	}
	public void setMltltAgrm_No(String mltltAgrm_No) {
		MltltAgrm_No = mltltAgrm_No;
	}
	public String getCCY_ID() {
		return CCY_ID;
	}
	public void setCCY_ID(String cCY_ID) {
		CCY_ID = cCY_ID;
	}
	public String getOrig_File_Nm() {
		return Orig_File_Nm;
	}
	public void setOrig_File_Nm(String orig_File_Nm) {
		Orig_File_Nm = orig_File_Nm;
	}
	public String getSRP_TxnAmt() {
		return SRP_TxnAmt;
	}
	public void setSRP_TxnAmt(String sRP_TxnAmt) {
		SRP_TxnAmt = sRP_TxnAmt;
	}
	public String getSCSP_Smy_Dsc() {
		return SCSP_Smy_Dsc;
	}
	public void setSCSP_Smy_Dsc(String sCSP_Smy_Dsc) {
		SCSP_Smy_Dsc = sCSP_Smy_Dsc;
	}
	public String getRvw_Ind() {
		return Rvw_Ind;
	}
	public void setRvw_Ind(String rvw_Ind) {
		Rvw_Ind = rvw_Ind;
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
	public String getLng_Vrsn() {
		return Lng_Vrsn;
	}
	public void setLng_Vrsn(String lng_Vrsn) {
		Lng_Vrsn = lng_Vrsn;
	}
	
	

}
