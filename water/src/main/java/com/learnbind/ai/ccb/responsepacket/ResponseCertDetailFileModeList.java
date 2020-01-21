package com.learnbind.ai.ccb.responsepacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 凭证明细(文件形式)对象列表
 * 注:一般情况下只返回一个文件,此处采用List容器,便于扩展.
 * @author lenovo
 */
public class ResponseCertDetailFileModeList {
	List<ResponseCertDetailFileMode> certFileDetailList;
	
	public ResponseCertDetailFileModeList() {
		certFileDetailList=new ArrayList<>();
	}
	
	public void add(ResponseCertDetailFileMode centFileDetail) {
		certFileDetailList.add(centFileDetail);
	}

	public List<ResponseCertDetailFileMode> getCertFileDetailList() {
		return certFileDetailList;
	}

	public void setCertFileDetailList(List<ResponseCertDetailFileMode> certFileDetailList) {
		this.certFileDetailList = certFileDetailList;
	}

	@Override
	public String toString() {
		return "ResponseCertDetailFileModeList [certFileDetailList=" + certFileDetailList + "]";
	}
	
}
