package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "MODULE_PRODUCT_NO")
public class ModuleProductNo {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private BigDecimal id;

    @Column(name = "PRODUCT_NO")
    private String productNo;

    @Column(name = "PRINTED")
    private Integer printed;

    @Column(name = "OPERATER_ID")
    private BigDecimal operaterId;

    @Column(name = "OPERATER_NAME")
    private String operaterName;

    @Column(name = "OPERATER_DATE")
    private Date operaterDate;

    /**
     * @return ID
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(BigDecimal id) {
        this.id = id;
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
    public BigDecimal getOperaterId() {
        return operaterId;
    }

    /**
     * @param operaterId
     */
    public void setOperaterId(BigDecimal operaterId) {
        this.operaterId = operaterId;
    }

    /**
     * @return OPERATER_NAME
     */
    public String getOperaterName() {
        return operaterName;
    }

    /**
     * @param operaterName
     */
    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName == null ? null : operaterName.trim();
    }

    /**
     * @return OPERATER_DATE
     */
    public Date getOperaterDate() {
        return operaterDate;
    }

    /**
     * @param operaterDate
     */
    public void setOperaterDate(Date operaterDate) {
        this.operaterDate = operaterDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productNo=").append(productNo);
        sb.append(", printed=").append(printed);
        sb.append(", operaterId=").append(operaterId);
        sb.append(", operaterName=").append(operaterName);
        sb.append(", operaterDate=").append(operaterDate);
        sb.append("]");
        return sb.toString();
    }
}