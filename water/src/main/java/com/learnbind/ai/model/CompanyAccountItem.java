package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "COMPANY_ACCOUNT_ITEM")
public class CompanyAccountItem {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
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

    @Column(name = "ACCOUNT_DATE")
    private Date accountDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ACCOUNTER")
    private String accounter;

    @Column(name = "DEBIT_CREDIT")
    private String debitCredit;
    
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
        this.debitCredit = debitCredit == null ? null : debitCredit.trim();
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
        sb.append(", baseWaterFee=").append(baseWaterFee);
        sb.append(", sewageWaterFee=").append(sewageWaterFee);
        sb.append("]");
        return sb.toString();
    }
}