package com.learnbind.ai.ccb.requestpacket;

public class CCBTransaction {
	//请求码常量(业务代码)
	public static final String TRANSACTION_CODE_LOGIN="P1OPME001"; 						//签到
	public static final String TRANSACTION_CODE_LOGOUT="P1OPME002"; 						//签退
	
	public static final String TRANSACTION_CODE_QUERY_ACCOUNT_BALANCE="P1CMSER18"; 		//帐户余额查询
	
	public static final String TRANSACTION_CODE_TRANSFER="P1CMSET35"; 					//转帐
	public static final String TRANSACTION_CODE_QUERY_TRANSFER_RESULT="P1CMSET36"; 		//转帐结果
	
	public static final String TRANSACTION_CODE_BATCH_WITHHOLD="P1CLP1051"; 				//批量代扣制单	
	public static final String TRANSACTION_CODE_QUERY_WITHHOLD_RECEIPT ="P1CLP1054"; 	//代发代扣直连单据查询	
	public static final String TRANSACTION_CODE_QUERY_VOUCHER ="P1CLP1055"; 				//查询凭证明细结果
	public static final String TRANSACTION_CODE_QUERY_ACCOUNT_DETAIL="P1CMSER65"; 		//查询账户明细
	
	//请求报文头字段常量 
	public static final String TRANSACTION_SYS_TX_VRSN="01";  					//服务版本号
	public static final String TRANSACTION_MULTI_TENANCY_ID="CN000";  			//多实体标识
	public static final String TRANSACTION_LNG_ID="zh-cn";  		  				//语言标识	
	
	//需要根据实际情况进行修改
//	public static final String TRANSACTION_TXN_STFF_ID="333333";   				//交易人员编号
//	public static final String TRANSACTION_CHNL_CUST_NO="HE13000009021440001";   //电子银行合约编号
//	public static final String TRANSACTION_EBnk_SvAr_ID="HE13000009021440001";   //电子银行合约编号
//	public static final String TRANSACTION_EtrUnt_AccNo="13001618801050519642";  //委托单位帐号  客户在建行所签项目的结算帐号
//	
//	
//	public static final String TRANSACTION_Txn_Itt_IP_Adr="192.168.1.123";   	//交易发起方 IP 地址	
//	
//	//代收代付部分常量
//	public static final String TRANSACTION_Entrst_Prj_ID="130130070";   	        //委托项目编号  客户在建行所签项目的项目编号
//	public static final String TRANSACTION_Prj_Use_ID="zldf00001";   	        	//项目用途编号  或 zlds00001
	  	
	
	public static final String TRANSACTION_Rvw_Ind="1";  							//按批次复核标志 1-直连客户不需要网银审核（默认一步流程） 2-需要（自定义流程
	
	public static final String TRANSACTION_VCHR_TP_CODE_BATCH_SINGLE="0"; 		//凭证类型 0-单笔 1-批量
	public static final String TRANSACTION_VCHR_TP_CODE_BATCH="1";  				//凭证类型 0-单笔 1-批量
	
	public static final String TRANSACTION_Lng_Vrsn="1";  						//批量文件语言版本：1-简体 2-繁体	
	
	public static final String TRANSACTION_CCY_ID="156";   						//人民币编号
	
	//query voucher 
	//1：报文、2：文件
	//如批量凭证交易完成，则根据此字段方式返回明细，默认为报文方式
	public static final String TRANSACTION_RETURN_RESULT_CODE_PACKET="1";   			//明细返回方式_报文
	public static final String TRANSACTION_RETURN_RESULT_CODE_FILE="2";   				//明细返回方式_文件	
	
	/**
	 * #格式文件类型代码
	 * 当明细结果返回方式选择文件时，需设置返回文件的类型，默认为 0:汇总文件
	 * 0:汇总文件 1:成功明细 2:失败明细
	 */
	public static final String TRANSACTION_FORMAT_FILE_TYPE_CODE_SUMMARY_FILE="0";     //汇总文件
	public static final String TRANSACTION_FORMAT_FILE_TYPE_CODE_SUCCESS_DETAIL="1";   //成功明细
	public static final String TRANSACTION_FORMAT_FILE_TYPE_CODE_FAIL_DETAIL="2";      //失败明细
	
	//测试数据
	//public static final String TRANSACTION_Txn_SN="113810458305358140";      		//
	

}
