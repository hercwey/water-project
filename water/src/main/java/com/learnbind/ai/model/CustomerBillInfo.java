package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumEnabledStatus;

@Table(name = "CUSTOMER_BILL_INFO")
public class CustomerBillInfo {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT BILL_INFO_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "REG_ADDRESS")
    private String regAddress;

    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "ACCOUNT_BANK")
    private String accountBank;

    @Column(name = "ACCOUNT_NO")
    private String accountNo;

    @Column(name = "TAX_NO")
    private String taxNo;

    @Column(name = "POST_ADDRESS")
    private String postAddress;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "ENABLED")
    private Integer enabled;
    
    @Column(name = "BILL_TYPE")
    private Integer billType;
    
    @Column(name = "BILL_NO")
    private String billNo;
    
    @Column(name = "SHORT_CODE")
    private String shortCode;
    
    @Column(name = "IS_DEFAULT")
    private Integer isDefault;

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
     * @return REG_ADDRESS
     */
    public String getRegAddress() {
        return regAddress;
    }

    /**
     * @param regAddress
     */
    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    /**
     * @return TELEPHONE
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
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
     * @return ACCOUNT_NO
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * @param accountNo
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
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
     * @return POST_ADDRESS
     */
    public String getPostAddress() {
        return postAddress;
    }

    /**
     * @param postAddress
     */
    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress == null ? null : postAddress.trim();
    }

    /**
     * @return CONTACT
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
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
    
    

    public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	
	

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}


	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName=").append(customerName);
        sb.append(", regAddress=").append(regAddress);
        sb.append(", telephone=").append(telephone);
        sb.append(", accountBank=").append(accountBank);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", taxNo=").append(taxNo);
        sb.append(", postAddress=").append(postAddress);
        sb.append(", contact=").append(contact);
        sb.append(", enabled=").append(enabled);
        sb.append(", billType=").append(billType);
        sb.append(", billNo=").append(billNo);
        sb.append(", shortCode=").append(shortCode);
        sb.append(", isDefault=").append(isDefault);
        sb.append("]");
        return sb.toString();
    }
}