package com.learnbind.ai.ccb.responsepacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 单据状态列表-响应报文
 * @author lenovo
 *
 */
public class ResponseCertStateList {
	private List<ResponseCertState> certificateList;
	
	public ResponseCertStateList() {
		certificateList=new ArrayList<>();
	}
	
	public void add(ResponseCertState certificate) {
		certificateList.add(certificate);
	}

	public List<ResponseCertState> getCertificateList() {
		return certificateList;
	}

	public void setCertificateList(List<ResponseCertState> certificateList) {
		this.certificateList = certificateList;
	}
	
	
}
