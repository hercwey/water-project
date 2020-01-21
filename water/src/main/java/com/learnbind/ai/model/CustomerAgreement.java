package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumElectronicArchiveType;

@Table(name = "CUSTOMER_AGREEMENT")
public class CustomerAgreement {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUST_AGREEMENT_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    /**
     * @Fields agreementType：协议类型
     */
    @Column(name = "AGREEMENT_TYPE")
    private Integer agreementType;

    @Column(name = "PATH")
    private String path;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "AGREEMENT_NAME")
    private String agreementName;

    @Column(name = "REMARK")
    private String remark;

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
     * @return AGREEMENT_TYPE
     */
    public Integer getAgreementType() {
        return agreementType;
    }
    
    /**
     * @return 获取类型字符串
     */
    public String getAgreementTypeStr() {
    	if(agreementType==null) {
    		return null;
    	}
        return EnumElectronicArchiveType.getName(agreementType);
    }

    /**
     * @param agreementType
     */
    public void setAgreementType(Integer agreementType) {
        this.agreementType = agreementType;
    }

    /**
     * @return PATH
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * @return DELETED
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * @param deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * @return AGREEMENT_NAME
     */
    public String getAgreementName() {
        return agreementName;
    }

    /**
     * @param agreementName
     */
    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName == null ? null : agreementName.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", agreementType=").append(agreementType);
        sb.append(", path=").append(path);
        sb.append(", deleted=").append(deleted);
        sb.append(", agreementName=").append(agreementName);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}