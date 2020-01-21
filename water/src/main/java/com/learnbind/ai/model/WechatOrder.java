package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "WECHAT_ORDER")
public class WechatOrder {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT WECHAT_ORDER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "OPEN_ID")
    private String openId;

    @Column(name = "UNION_ID")
    private String unionId;

    @Column(name = "APP_ID")
    private String appId;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "ORDER_DATE")
    private Date orderDate;

    @Column(name = "PREPAY_ID")
    private String prepayId;

    @Column(name = "RESULT_CODE")
    private Integer resultCode;

    @Column(name = "ERR_CODE")
    private String errCode;

    @Column(name = "ERR_CODE_DES")
    private String errCodeDes;

    @Column(name = "ORDER_AMOUNT")
    private BigDecimal orderAmount;

    @Column(name = "PAY_AMOUNT")
    private BigDecimal payAmount;

    @Column(name = "BILL_IDS")
    private String billIds;

    @Column(name = "ORDER_TYPE")
    private Integer orderType;

    @Column(name = "MCH_ID")
    private String mchId;
    
    @Column(name = "ACCOUNT_ITEM_ID")
    private String accountItemId;
    
    @Column(name = "TRANSACTION_ID")
    private String transactionId;
    
    @Column(name = "RESULT_CODE_CALLBACK")
    private Integer resultCodeCallback;
    
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return OPEN_ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * @return UNION_ID
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * @param unionId
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId == null ? null : unionId.trim();
    }

    /**
     * @return APP_ID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * @return ORDER_NO
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @param orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * @return ORDER_DATE
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return PREPAY_ID
     */
    public String getPrepayId() {
        return prepayId;
    }

    /**
     * @param prepayId
     */
    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId == null ? null : prepayId.trim();
    }

    /**
     * @return RESULT_CODE
     */
    public Integer getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode
     */
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return ERR_CODE
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * @param errCode
     */
    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    /**
     * @return ERR_CODE_DES
     */
    public String getErrCodeDes() {
        return errCodeDes;
    }

    /**
     * @param errCodeDes
     */
    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes == null ? null : errCodeDes.trim();
    }

    /**
     * @return ORDER_AMOUNT
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * @param orderAmount
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * @return PAY_AMOUNT
     */
    public BigDecimal getPayAmount() {
        return payAmount;
    }

    /**
     * @param payAmount
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * @return BILL_IDS
     */
    public String getBillIds() {
        return billIds;
    }

    /**
     * @param billIds
     */
    public void setBillIds(String billIds) {
        this.billIds = billIds == null ? null : billIds.trim();
    }

    /**
     * @return ORDER_TYPE
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * @param orderType
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * @return MCH_ID
     */
    public String getMchId() {
        return mchId;
    }

    /**
     * @param mchId
     */
    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getAccountItemId() {
		return accountItemId;
	}

	public void setAccountItemId(String accountItemId) {
		this.accountItemId = accountItemId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getResultCodeCallback() {
		return resultCodeCallback;
	}

	public void setResultCodeCallback(Integer resultCodeCallback) {
		this.resultCodeCallback = resultCodeCallback;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", openId=").append(openId);
        sb.append(", unionId=").append(unionId);
        sb.append(", appId=").append(appId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", prepayId=").append(prepayId);
        sb.append(", resultCode=").append(resultCode);
        sb.append(", errCode=").append(errCode);
        sb.append(", errCodeDes=").append(errCodeDes);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", payAmount=").append(payAmount);
        sb.append(", billIds=").append(billIds);
        sb.append(", orderType=").append(orderType);
        sb.append(", mchId=").append(mchId);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", resultCodeCallback=").append(resultCodeCallback);
        sb.append(", customerId=").append(customerId);
        sb.append("]");
        return sb.toString();
    }
}