package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumEnabledStatus;

@Table(name = "CUSTOMER_BANKS")
public class CustomerBanks {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUSTOMER_BANKS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "CARD_NO")
    private String cardNo;

    @Column(name = "ACCOUNT_BANK")
    private String accountBank;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ENABLED")
    private Integer enabled;
    
    @Column(name = "ACCOUNT_NAME")
    private String accountName;

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
     * @return BANK_CODE
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
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
     * @return CARD_NO
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    /**
     * @return ACCOUNT_BANK
     */
    public String getAccountBank() {
        return accountBank;
    }

    /**
     * @param accountBank
     */
    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank == null ? null : accountBank.trim();
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
     * @return ENABLED
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    
    public String getEnabledStr() {
    	return EnumEnabledStatus.getName(enabled);
    }

    public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", bankCode=").append(bankCode);
        sb.append(", bankName=").append(bankName);
        sb.append(", cardNo=").append(cardNo);
        sb.append(", accountBank=").append(accountBank);
        sb.append(", remark=").append(remark);
        sb.append(", enabled=").append(enabled);
        sb.append(", accountName=").append(accountName);
        sb.append("]");
        return sb.toString();
    }
}