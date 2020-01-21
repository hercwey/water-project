package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumCcbSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;



@Table(name = "CCB_BATCH_CUSTOMER_ACCOUNT")
public class CcbBatchCustomerAccount {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CCB_WITH_CUS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
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
    @DateTimeFormat(pattern = "yyyy-MM")
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
     * @return OPERATOR_NAMR
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorNamr
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
    
    

    public BigDecimal getAccountAmount() {
		return accountAmount;
	}

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
		return "CcbBatchCustomerAccount [id=" + id + ", bathWithholdingId=" + bathWithholdingId + ", accountItemId="
				+ accountItemId + ", withholdingStatus=" + withholdingStatus + ", settleAccountStatus="
				+ settleAccountStatus + ", joinTime=" + joinTime + ", settleTime=" + settleTime + ", operatorName="
				+ operatorName + ", operatorId=" + operatorId + ", customerName=" + customerName + ", customerCardNo="
				+ customerCardNo + ", withholdingNo=" + withholdingNo + ", accountAmount=" + accountAmount
				+ ", withholdFailReason=" + withholdFailReason + ", settleFailReason=" + settleFailReason + "]";
	}

	
}