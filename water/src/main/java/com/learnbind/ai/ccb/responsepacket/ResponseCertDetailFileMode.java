package com.learnbind.ai.ccb.responsepacket;

/**
 * 凭证明细(文件形式)对象
 * @author lenovo
 *
 */
public class ResponseCertDetailFileMode {
	/**
	 * CCB端凭证处理结果文件的文件名称(可为汇总文件,成功文件,失败文件)
	 * 由CCB返回
	 */
	private String FILE_NAME; //文件名称
	/**
	 * CCB端凭证处理结果文件的文件路径
	 * 由CCB返回
	 */
	private String FILE_PATH; //文件路径
	
	public String getFILE_NAME() {
		return FILE_NAME;
	}
	public void setFILE_NAME(String fILE_NAME) {
		FILE_NAME = fILE_NAME;
	}
	public String getFILE_PATH() {
		return FILE_PATH;
	}
	public void setFILE_PATH(String fILE_PATH) {
		FILE_PATH = fILE_PATH;
	}
	
	@Override
	public String toString() {
		return "ResponseCertDetailFileMode [FILE_NAME=" + FILE_NAME + ", FILE_PATH=" + FILE_PATH + "]";
	}
	
}
