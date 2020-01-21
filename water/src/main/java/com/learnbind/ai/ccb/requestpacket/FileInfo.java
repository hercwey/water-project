package com.learnbind.ai.ccb.requestpacket;


/**
 * FILEINFO 段描述
 * 代收代付
 * @author lenovo
 *
 */
public class FileInfo {
	
	/**
	 * #访问文件名
	 * CCB返回的文件名称
	 */
	private String FILE_NAME;
	/**
	 * 消息摘要 采用MD5得到的base64摘要(UTF-8编码) 
	 * 文件内容的摘要.  
	 */
	private String Msg_Smy;		
	
	/**
	 * added by hz 2019/07/11
	 * 此文件所对应的凭证序列号
	 */
	private String Txn_SN;
	
	
	public String getFILE_NAME() {
		return FILE_NAME;
	}
	public void setFILE_NAME(String fILE_NAME) {
		FILE_NAME = fILE_NAME;
	}
	public String getMsg_Smy() {
		return Msg_Smy;
	}
	public void setMsg_Smy(String msg_Smy) {
		Msg_Smy = msg_Smy;
	}
	public String getTxn_SN() {
		return Txn_SN;
	}
	public void setTxn_SN(String txn_SN) {
		Txn_SN = txn_SN;
	}
	
	
	
	
	
}
