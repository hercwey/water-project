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

import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumSubAccountStatus;

@Table(name = "CUSTOMER_ACCOUNT_ITEM")
public class CustomerAccountItem {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT ACCOUNT_ITEM_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "DEBIT_DIGEST")
    private String debitDigest;

    @Column(name = "DEBIT_SUBJECT")
    private String debitSubject;

    @Column(name = "DEBIT_ASSISTANT")
    private String debitAssistant;

    @Column(name = "DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @Column(name = "CREDIT_DIGEST")
    private String creditDigest;

    @Column(name = "CREDIT_SUBJECT")
    private String creditSubject;

    @Column(name = "CREDIT_ASSISTANT")
    private String creditAssistant;

    @Column(name = "CREDIT_AMOUNT")
    private BigDecimal creditAmount;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ACCOUNT_DATE")
    private Date accountDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ACCOUNTER")
    private String accounter;

    @Column(name = "DEBIT_CREDIT")
    private String debitCredit;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OVERDUE_DATE")
    private Date overdueDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "START_TIME")
    private Date startTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "PERIOD")
    private String period;
    
    @Column(name = "SETTLEMENT_STATUS")
    private Integer settlementStatus;//自动扣费结算状态 0=默认值；1=结算成功；2=结算失败
    
    @Column(name = "SETTLEMENT_ERR_MSG")
    private String settlementErrMsg;
    
    @Column(name = "ACCOUNT_STATUS")
    private Integer accountStatus;//0=不需要分账；1=未分账；2=已分账
    
    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "PID")
    private Long pid;//账目ID,自引用;父节点ID，默认值=0；只在违约金账单和分账账单中设置此值，其他都为0
    
    @Column(name = "BASE_WATER_FEE")
    private BigDecimal baseWaterFee;//基础水费
    
    @Column(name = "SEWAGE_WATER_FEE")
    private BigDecimal sewageWaterFee;//污水处理费

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
     * @return CUSTOMER_ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return ACCOUNT_ID
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return DEBIT_DIGEST
     */
    public String getDebitDigest() {
        return debitDigest;
    }

    /**
     * @param debitDigest
     */
    public void setDebitDigest(String debitDigest) {
        this.debitDigest = debitDigest == null ? null : debitDigest.trim();
    }

    /**
     * @return DEBIT_SUBJECT
     */
    public String getDebitSubject() {
        return debitSubject;
    }

    /**
     * @param debitSubject
     */
    public void setDebitSubject(String debitSubject) {
        this.debitSubject = debitSubject == null ? null : debitSubject.trim();
    }

    /**
     * @return DEBIT_ASSISTANT
     */
    public String getDebitAssistant() {
        return debitAssistant;
    }

    /**
     * @param debitAssistant
     */
    public void setDebitAssistant(String debitAssistant) {
        this.debitAssistant = debitAssistant == null ? null : debitAssistant.trim();
    }

    /**
     * @return DEBIT_AMOUNT
     */
    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    /**
     * @param debitAmount
     */
    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    /**
     * @return CREDIT_DIGEST
     */
    public String getCreditDigest() {
        return creditDigest;
    }

    /**
     * @param creditDigest
     */
    public void setCreditDigest(String creditDigest) {
        this.creditDigest = creditDigest == null ? null : creditDigest.trim();
    }

    /**
     * @return CREDIT_SUBJECT
     */
    public String getCreditSubject() {
        return creditSubject;
    }

    /**
     * @param creditSubject
     */
    public void setCreditSubject(String creditSubject) {
        this.creditSubject = creditSubject == null ? null : creditSubject.trim();
    }

    /**
     * @return CREDIT_ASSISTANT
     */
    public String getCreditAssistant() {
        return creditAssistant;
    }

    /**
     * @param creditAssistant
     */
    public void setCreditAssistant(String creditAssistant) {
        this.creditAssistant = creditAssistant == null ? null : creditAssistant.trim();
    }

    /**
     * @return CREDIT_AMOUNT
     */
    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    /**
     * @param creditAmount
     */
    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    /**
     * @return ACCOUNT_DATE
     */
    public Date getAccountDate() {
        return accountDate;
    }

    /**
     * @param accountDate
     */
    public void setAccountDate(Date accountDate) {
        this.accountDate = accountDate;
    }
    
    /**
     * @Title: getAccountDateStr
     * @Description: 获取账目日期字符串
     * @return 
     */
    public String getAccountDateStr() {
    	if (accountDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(accountDate);
    	}
    	return null;
    	
    }

    /**
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return ACCOUNTER
     */
    public String getAccounter() {
        return accounter;
    }

    /**
     * @param accounter
     */
    public void setAccounter(String accounter) {
        this.accounter = accounter == null ? null : accounter.trim();
    }

    /**
     * @return DEBIT_CREDIT
     */
    public String getDebitCredit() {
        return debitCredit;
    }

    /**
     * @param debitCredit
     */
    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
    }

    /**
     * @return OVERDUE_DATE
     */
    public Date getOverdueDate() {
        return overdueDate;
    }

    /**
     * @param overdueDate
     */
    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }
    
    /**
     * @Title: getAccountDateStr
     * @Description: 获取违约金计算日期字符串
     * @return 
     */
    public String getOverdueDateStr() {
    	if (overdueDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(overdueDate);
    	}
    	return null;
    	
    }
    
    /**
     * @return START_TIME
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @Title: getStartTimeStr
     * @Description: 获取账单开始时间字符串
     * @return 
     */
    public String getStartTimeStr() {
    	if (startTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(startTime);
    	}
    	return null;
    	
    }

    /**
     * @return END_TIME
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    /**
     * @Title: getEndTimeStr
     * @Description: 获取账单开结束时间字符串
     * @return 
     */
    public String getEndTimeStr() {
    	if (endTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(endTime);
    	}
    	return null;
    	
    }

    /**
     * @return PERIOD
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }

    /**
     * @Title: getSettlementStatus
     * @Description: 自动扣费结算状态 0=默认值；1=结算成功；2=结算失败
     * @return 
     */
    public Integer getSettlementStatus() {
		return settlementStatus;
	}
    
    /**
     * @Title: getSettlementStatusStr
     * @Description: 自动扣费结算状态字符串 0=默认值；1=结算成功；2=结算失败
     * @return 
     */
    public String getSettlementStatusStr() {
    	return EnumSettlementStatus.getName(settlementStatus);
    }

	public void setSettlementStatus(Integer settlementStatus) {
		this.settlementStatus = settlementStatus;
	}

	public String getSettlementErrMsg() {
		return settlementErrMsg;
	}

	public void setSettlementErrMsg(String settlementErrMsg) {
		this.settlementErrMsg = settlementErrMsg;
	}
	
	

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	/**
     * @Title: getAccountStatusStr
     * @Description: ，0=不需要分账；1=未分账；2=已分账
     * @return 
     */
    public String getAccountStatusStr() {
    	return EnumSubAccountStatus.getName(accountStatus);
    }

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public BigDecimal getBaseWaterFee() {
		return baseWaterFee;
	}

	public void setBaseWaterFee(BigDecimal baseWaterFee) {
		this.baseWaterFee = baseWaterFee;
	}

	public BigDecimal getSewageWaterFee() {
		return sewageWaterFee;
	}

	public void setSewageWaterFee(BigDecimal sewageWaterFee) {
		this.sewageWaterFee = sewageWaterFee;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", accountId=").append(accountId);
        sb.append(", debitDigest=").append(debitDigest);
        sb.append(", debitSubject=").append(debitSubject);
        sb.append(", debitAssistant=").append(debitAssistant);
        sb.append(", debitAmount=").append(debitAmount);
        sb.append(", creditDigest=").append(creditDigest);
        sb.append(", creditSubject=").append(creditSubject);
        sb.append(", creditAssistant=").append(creditAssistant);
        sb.append(", creditAmount=").append(creditAmount);
        sb.append(", accountDate=").append(accountDate);
        sb.append(", remark=").append(remark);
        sb.append(", accounter=").append(accounter);
        sb.append(", debitCredit=").append(debitCredit);
        sb.append(", overdueDate=").append(overdueDate);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", period=").append(period);
        sb.append(", settlementStatus=").append(settlementStatus);
        sb.append(", settlementErrMsg=").append(settlementErrMsg);
        sb.append(", accountStatus=").append(accountStatus);
        sb.append(", deleted=").append(deleted);
        sb.append(", pid=").append(pid);
        sb.append(", baseWaterFee=").append(baseWaterFee);
        sb.append(", sewageWaterFee=").append(sewageWaterFee);
        sb.append("]");
        return sb.toString();
    }
}