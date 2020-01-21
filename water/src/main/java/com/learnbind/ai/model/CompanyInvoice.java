package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;

@Table(name = "COMPANY_INVOICE")
public class CompanyInvoice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT COMPANY_INVOICE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "SHORT_CODE")
    private String shortCode;

    @Column(name = "COMPANY_NAME")
    private String companyName;

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

    @Column(name = "ENABLED")
    private Integer enabled;
    
    @Column(name = "IS_DEFAULT")
    private Integer isDefault;
    
    @Column(name = "CHECKER")
    private String checker;//复核人
    
    @Column(name = "PAYEE")
    private String payee;//收款人
    
    @Column(name = "SPECIAL_INVOICE_AMOUNT")
    private String specialInvoiceAmount;//专用发票额度版本
    
    @Column(name = "NORMAL_INVOICE_AMOUNT")
    private String normalInvoiceAmount;//普通发票额度版本

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
     * @return SHORT_CODE
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode == null ? null : shortCode.trim();
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

    /**
     * @Title: getIsDefault
     * @Description: 获取是否默认，0=是，1=否
     * @return 
     */
    public Integer getIsDefault() {
		return isDefault;
	}
    
    /**
     * @Title: getIsDefaultStr
     * @Description: 获取默认状态字符串
     * @return 
     */
    public String getIsDefaultStr() {
    	if(isDefault!=null) {
    		return EnumDefaultStatus.getName(isDefault);
    	}
		return null;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}
	
	

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getSpecialInvoiceAmount() {
		return specialInvoiceAmount;
	}

	public void setSpecialInvoiceAmount(String specialInvoiceAmount) {
		this.specialInvoiceAmount = specialInvoiceAmount;
	}

	public String getNormalInvoiceAmount() {
		return normalInvoiceAmount;
	}

	public void setNormalInvoiceAmount(String normalInvoiceAmount) {
		this.normalInvoiceAmount = normalInvoiceAmount;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", shortCode=").append(shortCode);
        sb.append(", companyName=").append(companyName);
        sb.append(", regAddress=").append(regAddress);
        sb.append(", telephone=").append(telephone);
        sb.append(", accountBank=").append(accountBank);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", taxNo=").append(taxNo);
        sb.append(", enabled=").append(enabled);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", checker=").append(checker);
        sb.append(", payee=").append(payee);
        sb.append(", specialInvoiceAmount=").append(specialInvoiceAmount);
        sb.append(", normalInvoiceAmount=").append(normalInvoiceAmount);
        sb.append("]");
        return sb.toString();
    }
}