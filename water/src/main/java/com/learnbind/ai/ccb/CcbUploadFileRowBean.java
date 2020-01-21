package com.learnbind.ai.ccb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.ccb
 *
 * @Title: CcbUploadFileRowBean.java
 * @Description: 中国建设银行生成批量代扣上传文件的单行内容
 *
 * @author Administrator
 * @date 2019年7月14日 下午4:57:53
 * @version V1.0 
 *
 */
@SuppressWarnings("serial")
public class CcbUploadFileRowBean implements Serializable {

	private Long accountItemId;//账单ID，只在创建文件与账单关系时使用
	private Integer sn;//序号
	private String cardNo;//账号
	private String accountName;//户名
	private BigDecimal totalAmount;//金额
	
	public Long getAccountItemId() {
		return accountItemId;
	}
	public void setAccountItemId(Long accountItemId) {
		this.accountItemId = accountItemId;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
    /**
     * @Title: getFileRowContent
     * @Description: 获取格式化后的中国建设银行批量代扣文件内容
     * @return 
     * 		序号|账号|户名|金额|跨行标识|行名|联行号|多方协议号|标识号|摘要|备注
     * 		序号|账号|户名|金额|跨行标识|行名|联行行号|多方协议号|标识号|摘要|备注|
     * @throws UnsupportedEncodingException 
     */
    public String getFileRowContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(sn);
        sb.append("|").append(cardNo);
        sb.append("|").append(accountName);
        sb.append("|").append(totalAmount.setScale(2));//金额保留两个小数
        sb.append("|").append("0");
        sb.append("|||||水费|");
		//return new String(sb.toString().getBytes(), "GBK");
        return sb.toString();
    }
    
	@Override
	public String toString() {
		return "CcbFileFormatBean [sn=" + sn + ", cardNo=" + cardNo + ", accountName=" + accountName + ", totalAmount="
				+ totalAmount + "]";
	}
	
}
