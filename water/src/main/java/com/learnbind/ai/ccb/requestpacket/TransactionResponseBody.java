package com.learnbind.ai.ccb.requestpacket;

/**
 * 
 * @author lenovo
 *
 */
public class TransactionResponseBody {
	//报文类型常量  SYS_PKG_STS_TYPE
	public static final String TRANSACTION_SYS_PKG_STS_TYPE_REQ="00";    //报文类型:request报文
	public static final String TRANSACTION_SYS_PKG_STS_TYPE_RESP="01";   //报文类型:response报文
	
	//服务状态   00-成功 	01-失败	02-不确定
	// SYS_TX_STATUS
	public static final String TRANSACTION_SYS_TX_STATUS_SUCCESS="00";   	//成功
	public static final String TRANSACTION_SYS_TX_STATUS_FAIL="01";   		//失败
	public static final String TRANSACTION_SYS_TX_STATUS_UNCERTAIN="02";   	//不确定
	
	//tran_response
	//     status
	//TRAN_RESPONSE_STATUS
	//交易成功时的应答报文，报文头中tran_response 节点下的 status 为 “COMPLETE”。
	//交易错误时的应答报文，状态、错误码和错误信息分别填写在报文头中 tran_response 节
	// 点下的status为 “FAIL”、code （错误码）和desc （错误信息）域中；报文体（Transaction_Body）节点为空。
	public static final String  TRANSACTION_TRAN_RESPONSE_STATUS_COMPLETE="COMPLETE";     //交易成功时的应答报文
	public static final String  TRANSACTION_TRAN_RESPONSE_STATUS_FAIL="FAIL";     //交易成功时的应答报文
	
	//服务响应码 SYS_RESP_CODE
	public static final String TRANSACTION_SYS_RESP_CODE_SUCCESS="000000000000";     //12个0表示成功,其他表示失败
		
	
}
