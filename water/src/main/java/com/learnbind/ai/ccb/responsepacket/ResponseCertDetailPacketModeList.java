package com.learnbind.ai.ccb.responsepacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 凭证明细列表-响应报文
 * @author lenovo
 */
public class ResponseCertDetailPacketModeList {
	List<ResponseCertDetailPacketMode> certDetailList;
	
	public  ResponseCertDetailPacketModeList() {
		certDetailList=new ArrayList<>();
	}
	
	public void add(ResponseCertDetailPacketMode certDetail) {
		certDetailList.add(certDetail);
	}

	public List<ResponseCertDetailPacketMode> getCertDetailList() {
		return certDetailList;
	}

	public void setCertDetailList(List<ResponseCertDetailPacketMode> certDetailList) {
		this.certDetailList = certDetailList;
	}
	
	
}
