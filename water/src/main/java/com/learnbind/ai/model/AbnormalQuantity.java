package com.learnbind.ai.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ABNORMAL_QUANTITY")
public class AbnormalQuantity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "MONTHS")
    private Integer months;

    @Column(name = "LOWER_BOUND")
    private BigDecimal lowerBound;

    @Column(name = "UPPER_BOUND")
    private BigDecimal upperBound;

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
     * @return TYPE
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return MONTHS
     */
    public Integer getMonths() {
        return months;
    }

    /**
     * @param months
     */
    public void setMonths(Integer months) {
        this.months = months;
    }

    /**
     * @return LOWER_BOUND
     */
    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    /**
     * @param lowerBound
     */
    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * @return UPPER_BOUND
     */
    public BigDecimal getUpperBound() {
        return upperBound;
    }

    /**
     * @param upperBound
     */
    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
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
        sb.append(", type=").append(type);
        sb.append(", months=").append(months);
        sb.append(", lowerBound=").append(lowerBound);
        sb.append(", upperBound=").append(upperBound);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}