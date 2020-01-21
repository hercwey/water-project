package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumCcbSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;

@Table(name = "CMBC_BATCH_CUSTOMER_ACCOUNT")
public class CmbcBatchCustomerAccount {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CMBC_WITH_CUS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "BATH_WITHHOLDING_ID")
    private Long bathWithholdingId;

    @Column(name = "ACCOUNT_ITEM_ID")
    private Long accountItemId;

    @Column(name = "WITHHOLDING_STATUS")
    private Integer withholdingStatus;

    @Column(name = "SETTLE_ACCOUNT_STATUS")
    private Integer settleAccountStatus;

    @Column(name = "JOIN_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinTime;

    @Column(name = "SETTLE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date settleTime;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_CARD_NO")
    private String customerCardNo;

    @Column(name = "WITHHOLDING_NO")
    private Integer withholdingNo;

    @Column(name = "ACCOUNT_AMOUNT")
    private BigDecimal accountAmount;
    
    @Column(name = "WITHHOLD_FAIL_REASON")
    private String withholdFailReason;
    
    @Column(name = "SETTLE_FAIL_REASON")
    private String settleFailReason;

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
     * @return BATH_WITHHOLDING_ID
     */
    public Long getBathWithholdingId() {
        return bathWithholdingId;
    }

    /**
     * @param bathWithholdingId
     */
    public void setBathWithholdingId(Long bathWithholdingId) {
        this.bathWithholdingId = bathWithholdingId;
    }

    /**
     * @return ACCOUNT_ITEM_ID
     */
    public Long getAccountItemId() {
        return accountItemId;
    }

    /**
     * @param accountItemId
     */
    public void setAccountItemId(Long accountItemId) {
        this.accountItemId = accountItemId;
    }

    /**
     * @return WITHHOLDING_STATUS
     */
    public Integer getWithholdingStatus() {
        return withholdingStatus;
    }

    /**
     * @param withholdingStatus
     */
    public void setWithholdingStatus(Integer withholdingStatus) {
        this.withholdingStatus = withholdingStatus;
    }
    
    /**
     * @Title: getWithholdingStatusStr
     * @Description: CCB文件代扣状态
     * @return 
     */
    public String getWithholdingStatusStr() {
    	return EnumCcbWhHoldStatus.getName(withholdingStatus);
    }

    /**
     * @return SETTLE_ACCOUNT_STATUS
     */
    public Integer getSettleAccountStatus() {
        return settleAccountStatus;
    }

    /**
     * @param settleAccountStatus
     */
    public void setSettleAccountStatus(Integer settleAccountStatus) {
        this.settleAccountStatus = settleAccountStatus;
    }
    
    /**
     * @Title: getSettleAccountStatusStatusStr
     * @Description: 销账状态
     * @return 
     */
    public String getSettleAccountStatusStatusStr() {
    	return EnumCcbSettleAccountStatus.getName(settleAccountStatus);
    }

    /**
     * @return JOIN_TIME
     */
    public Date getJoinTime() {
        return joinTime;
    }

    /**
     * @param joinTime
     */
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
    
    /**
     * @Title: getJoinTimeStr
     * @Description: 获取加入批量代扣时间字符串
     * @return 
     */
    public String getJoinTimeStr() {
    	if(joinTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(joinTime);
    	}
    	return null;
    }

    /**
     * @return SETTLE_TIME
     */
    public Date getSettleTime() {
        return settleTime;
    }

    /**
     * @param settleTime
     */
    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }
    
    /**
     * @Title: getSettleTimeStr
     * @Description: 获取销账事件字符串
     * @return 
     */
    public String getSettleTimeStr() {
    	if(settleTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(settleTime);
    	}
    	return null;
    }


    /**
     * @return OPERATOR_NAME
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * @return OPERATOR_ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return CUSTOMER_NAME
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    /**
     * @return CUSTOMER_CARD_NO
     */
    public String getCustomerCardNo() {
        return customerCardNo;
    }

    /**
     * @param customerCardNo
     */
    public void setCustomerCardNo(String customerCardNo) {
        this.customerCardNo = customerCardNo == null ? null : customerCardNo.trim();
    }

    /**
     * @return WITHHOLDING_NO
     */
    public Integer getWithholdingNo() {
        return withholdingNo;
    }

    /**
     * @param withholdingNo
     */
    public void setWithholdingNo(Integer withholdingNo) {
        this.withholdingNo = withholdingNo;
    }

    /**
     * @return ACCOUNT_AMOUNT
     */
    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    /**
     * @param accountAmount
     */
    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }
    
    public String getWithholdFailReason() {
		return withholdFailReason;
	}

	public void setWithholdFailReason(String withholdFailReason) {
		this.withholdFailReason = withholdFailReason;
	}

	public String getSettleFailReason() {
		return settleFailReason;
	}

	public void setSettleFailReason(String settleFailReason) {
		this.settleFailReason = settleFailReason;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", bathWithholdingId=").append(bathWithholdingId);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append(", withholdingStatus=").append(withholdingStatus);
        sb.append(", settleAccountStatus=").append(settleAccountStatus);
        sb.append(", joinTime=").append(joinTime);
        sb.append(", settleTime=").append(settleTime);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", customerName=").append(customerName);
        sb.append(", customerCardNo=").append(customerCardNo);
        sb.append(", withholdingNo=").append(withholdingNo);
        sb.append(", accountAmount=").append(accountAmount);
        sb.append(", withholdFailReason=").append(withholdFailReason);
        sb.append(", settleFailReason=").append(settleFailReason);
        sb.append("]");
        return sb.toString();
    }
}