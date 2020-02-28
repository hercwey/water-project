package com.learnbind.ai.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MODULE_PRODUCT_NO")
public class ModuleProductNo {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT M_P_NO_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "MODULE_NO")
    private String moduleNo;

    @Column(name = "PRODUCT_NO")
    private String productNo;

    @Column(name = "PRINTED")
    private Integer printed;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_DATE")
    private Date operatorDate;

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
     * @return MODULE_NO
     */
    public String getModuleNo() {
        return moduleNo;
    }

    /**
     * @param moduleNo
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo == null ? null : moduleNo.trim();
    }

    /**
     * @return PRODUCT_NO
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * @param productNo
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo == null ? null : productNo.trim();
    }

    /**
     * @return PRINTED
     */
    public Integer getPrinted() {
        return printed;
    }

    /**
     * @param printed
     */
    public void setPrinted(Integer printed) {
        this.printed = printed;
    }

    /**
     * @return OPERATER_ID
     */
    public Long getOperaterId() {
        return operatorId;
    }

    /**
     * @param operaterId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return OPERATER_NAME
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operaterName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * @return OPERATER_DATE
     */
    public Date getOperatorDate() {
        return operatorDate;
    }

    /**
     * @param operaterDate
     */
    public void setOperatorDate(Date operatorDate) {
        this.operatorDate = operatorDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", moduleNo=").append(moduleNo);
        sb.append(", productNo=").append(productNo);
        sb.append(", printed=").append(printed);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorDate=").append(operatorDate);
        sb.append("]");
        return sb.toString();
    }
}