package com.learnbind.ai.ccb.responsepacket;

/**
 * 申请批量代扣-响应报文
 * @author lenovo
 * 此类中的字段信息来自于测试范本.
 */
public class ResponseBatchWithhold {
	
	/**
	 * 客户(凭证)序列号.  客户新增单据时传入标识凭证的唯一客户序列号，企业网银保存客户序列号与凭证号的对应关系
	 * 
	 * 注:客户端所生成凭证的ID(即上传批量代扣文件的ID,此ID在生成批量文件时一同生成)
	 */
	private String Txn_SN; //<![CDATA[113810458305358140]]></Txn_SN>
	/**
	 * 电子银行服务合约编号
	 * 客户在建行的客户号（单位编号）
	 * 
	 * 注:水司与CCB签订合约时生成.
	 */
	private String EBnk_SvAr_ID; //<![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>
	/**
	 * 凭证编号
	 * 由建行网银代收付流程组件生成的凭证编号，用于标识一笔代发代扣单据
	 * 
	 * 注:CCB针对Txn_SN所生成的CCB端凭证ID    Txn_SN<---search each other--->VchID
	 */
	private String VchID; //<![CDATA[VS201810110000093989]]></VchID>
	/**
	 * #处理方式
	 * 用于标识建行网银处理凭证方式  0-同步;1-异步
	 */
	private String PROCESS_FLAG; //<![CDATA[0]]></PROCESS_FLAG>
	/**
	 * 批量文件中明细总金额
	 */
	private String TAmt;			//11.00</TAmt>
	/**
	 * 批量文件中明细总笔数
	 */
	private String TDnum; //<![CDATA[5]]></TDnum>
	/**
	 * 原因描述   失败原因
	 */
	private String RDsc; //<![CDATA[委托项目编号：130130070,实时批量交易中]]></RDsc>
	/**
	 * 凭证状态
	 * 100-不确定,
		200-待复核,
		300-复核不通过,
		400-复核完毕,待账务系统处理,
		401-复核完毕,待发送账务系统,
		500-已收回,
		600-已删除(直连单据复核不通过时状态置为600),
		700-交易成功,
		800-交易失败,
		901-账务系统成功已接收,
		902-账务系统检核完毕,
		903-账务系统下账,
		904-账务系统处理完成,
		429-单笔跨行终审成功,待人行处理
		
		注:700-交易成功是运营系统所期望的状态.如果不是继续采用查询凭证状态来处理.
	 */
	private String Vchr_St; //<![CDATA[400]]></Vchr_St>
	/**
	 *  暂不明确
	 */
	private String SRP_Err_Cd; //<![CDATA[]]></SRP_Err_Cd>
	/**
	 * 	暂不明确
	 */
	private String HJNo; //<![CDATA[]]></HJNo>
	
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
	public String getVchID() {
		return VchID;
	}
	public void setVchID(String vchID) {
		VchID = vchID;
	}
	public String getPROCESS_FLAG() {
		return PROCESS_FLAG;
	}
	public void setPROCESS_FLAG(String pROCESS_FLAG) {
		PROCESS_FLAG = pROCESS_FLAG;
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
	public String getRDsc() {
		return RDsc;
	}
	public void setRDsc(String rDsc) {
		RDsc = rDsc;
	}
	public String getVchr_St() {
		return Vchr_St;
	}
	public void setVchr_St(String vchr_St) {
		Vchr_St = vchr_St;
	}
	public String getSRP_Err_Cd() {
		return SRP_Err_Cd;
	}
	public void setSRP_Err_Cd(String sRP_Err_Cd) {
		SRP_Err_Cd = sRP_Err_Cd;
	}
	public String getHJNo() {
		return HJNo;
	}
	public void setHJNo(String hJNo) {
		HJNo = hJNo;
	}
	
	@Override
	public String toString() {
		return "ResponseBatchWithhold [Txn_SN=" + Txn_SN + ", EBnk_SvAr_ID=" + EBnk_SvAr_ID + ", VchID=" + VchID
				+ ", PROCESS_FLAG=" + PROCESS_FLAG + ", TAmt=" + TAmt + ", TDnum=" + TDnum + ", RDsc=" + RDsc
				+ ", Vchr_St=" + Vchr_St + ", SRP_Err_Cd=" + SRP_Err_Cd + ", HJNo=" + HJNo + "]";
	}
	
}
