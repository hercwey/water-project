package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

public class Invoice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT INVOICE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "TAX_NO")
    private String taxNo;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_NO")
    private String bankNo;

    @Column(name = "INVOICE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;

    @Column(name = "INVOICE_PEOPLE")
    private String invoicePeople;

    @Column(name = "INVOICE_TYPE")
    private Integer invoiceType;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "FEE_AMOUNT")
    private BigDecimal feeAmount;

    @Column(name = "TAX_RATE")
    private BigDecimal taxRate;

    @Column(name = "TAX_AMOUNT")
    private BigDecimal taxAmount;

    @Column(name = "E_RECORD_ID")
    private Long eRecordId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "INVOICE_USE")
    private String invoiceUse;
    
    @Column(name = "ACCOUNT_ITEM_ID")
    private Long accountItemId;

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
     * @return INVOICE_NO
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    /**
     * @return COMPANY_NAME
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    /**
     * @return TAX_NO
     */
    public String getTaxNo() {
        return taxNo;
    }

    /**
     * @param taxNo
     */
    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo == null ? null : taxNo.trim();
    }

    /**
     * @return BANK_NAME
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    /**
     * @return BANK_NO
     */
    public String getBankNo() {
        return bankNo;
    }

    /**
     * @param bankNo
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    /**
     * @return INVOICE_DATE
     */
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate
     */
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
     /**
     * @Title: getInvoiceDateStr
     * @Description: 获取开票日期字符串
     * @return 
     */
    public String getInvoiceDateStr() {
    	if(invoiceDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(invoiceDate);
    	}
    	return null;
    }

    /**
     * @return INVOICE_PEOPLE
     */
    public String getInvoicePeople() {
        return invoicePeople;
    }

    /**
     * @param invoicePeople
     */
    public void setInvoicePeople(String invoicePeople) {
        this.invoicePeople = invoicePeople == null ? null : invoicePeople.trim();
    }

    /**
     * @return INVOICE_TYPE
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * @return TOTAL_AMOUNT
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return FEE_AMOUNT
     */
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    /**
     * @param feeAmount
     */
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    /**
     * @return TAX_RATE
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate
     */
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return TAX_AMOUNT
     */
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    /**
     * @param taxAmount
     */
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**
     * @return E_RECORD_ID
     */
    public Long geteRecordId() {
        return eRecordId;
    }

    /**
     * @param eRecordId
     */
    public void seteRecordId(Long eRecordId) {
        this.eRecordId = eRecordId;
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
     * @return INVOICE_USE
     */
    public String getInvoiceUse() {
        return invoiceUse;
    }

    /**
     * @param invoiceUse
     */
    public void setInvoiceUse(String invoiceUse) {
        this.invoiceUse = invoiceUse == null ? null : invoiceUse.trim();
    }
    
    

    public Long getAccountItemId() {
		return accountItemId;
	}

	public void setAccountItemId(Long accountItemId) {
		this.accountItemId = accountItemId;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", invoiceNo=").append(invoiceNo);
        sb.append(", companyName=").append(companyName);
        sb.append(", taxNo=").append(taxNo);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNo=").append(bankNo);
        sb.append(", invoiceDate=").append(invoiceDate);
        sb.append(", invoicePeople=").append(invoicePeople);
        sb.append(", invoiceType=").append(invoiceType);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", feeAmount=").append(feeAmount);
        sb.append(", taxRate=").append(taxRate);
        sb.append(", taxAmount=").append(taxAmount);
        sb.append(", eRecordId=").append(eRecordId);
        sb.append(", period=").append(period);
        sb.append(", invoiceUse=").append(invoiceUse);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append("]");
        return sb.toString();
    }
}