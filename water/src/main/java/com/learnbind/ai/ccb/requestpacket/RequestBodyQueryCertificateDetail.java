package com.learnbind.ai.ccb.requestpacket;

//No.55 代发代扣直联单据明细查询						
//服务类型	联机服务	联机服务	P1CLP1055	通用域引用范围	COM1，COM4，COMB，file_list_pack	
//功能描述	用于直连客户查询凭证明细结果					

//请求报文							
//栏位项目名称	中文名称					数据项编号		输出长度			栏位属性			必须			标志	补充说明
//Txn_SN		客户序列号				113000			20				C				N			客户新增单据时传入标识凭证的唯一客户序列号，企业网银保存客户序列号与凭证号的对应关系
//EBnk_SvAr_ID	电子银行服务合约编号		106120			23				C				Y			客户在建行的客户号（单位编号）
//VchID			凭证编号					108720			150				C				N			客户序列号与凭证编号必须输入一个
//Ret_Rslt_Cd	明细返回方式				600020			2				C				N			1：报文、2：文件	如批量凭证交易完成，则根据此字段方式返回明细，默认为报文方式
//Fmt_File_TpCd	#格式文件类型代码		302165			1				C				N			0:汇总文件 1:成功明细 2:失败明细	当明细结果返回方式选择文件时，需设置返回文件的类型，默认为 0:汇总文件

/**
 * 查询凭证明细请求报文  BODY  P1CLP1055 
 * @author lenovo
 */
public class RequestBodyQueryCertificateDetail {

	/**
	 * 客户序列号
	 * 需要设置
	 */
	private String Txn_SN;			//<![CDATA[113810458305358140]]></Txn_SN>
	/**
	 * 电子合约编号
	 * 需要设置 
	 */
	private String EBnk_SvAr_ID;	//<![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>
	/**
	 * 凭证编号 
	 */
	private String VchID="";			//<![CDATA[]]></VchID>
	/**
	 * 明细返回方式  根据业务进行设置()
	 * 参见CCBTransaction常量定义
	 * 	public static final String TRANSACTION_Ret_Rslt_Cd_PACKET="1";   			//明细返回方式_报文
	 *	public static final String TRANSACTION_Ret_Rslt_Cd_FILE="2";   				//明细返回方式_文件
	 */
	private String Ret_Rslt_Cd="";		//<![CDATA[1]]></Ret_Rslt_Cd>
	/**
	 * 如果是#格式文件类型代码
	 * 参见常量定义:
	 * 	CCBTransaction
	 * 	public static final String TRANSACTION_Fmt_File_TpCd_SUMMARY_FILE="0";     //汇总文件
	 *	public static final String TRANSACTION_Fmt_File_TpCd_SUCCESS_DETAIL="1";   //成功明细
	 *	public static final String TRANSACTION_Fmt_File_TpCd_FAIL_DETAIL="2";      //失败明细
	 *	需要设置
	 */
	private String Fmt_File_TpCd="";		//<![CDATA[0]]></Fmt_File_TpCd>
	
	
	
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
	public String getRet_Rslt_Cd() {
		return Ret_Rslt_Cd;
	}
	public void setRet_Rslt_Cd(String ret_Rslt_Cd) {
		Ret_Rslt_Cd = ret_Rslt_Cd;
	}
	public String getFmt_File_TpCd() {
		return Fmt_File_TpCd;
	}
	public void setFmt_File_TpCd(String fmt_File_TpCd) {
		Fmt_File_TpCd = fmt_File_TpCd;
	}
	
	
	
}
