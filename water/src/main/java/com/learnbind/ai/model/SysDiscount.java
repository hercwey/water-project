package com.learnbind.ai.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_DISCOUNT")
public class SysDiscount {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BASE_PRICE")
    private BigDecimal basePrice;

    @Column(name = "TREATMENT_FEE")
    private BigDecimal treatmentFee;

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
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return BASE_PRICE
     */
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    /**
     * @param basePrice
     */
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * @return TREATMENT_FEE
     */
    public BigDecimal getTreatmentFee() {
        return treatmentFee;
    }

    /**
     * @param treatmentFee
     */
    public void setTreatmentFee(BigDecimal treatmentFee) {
        this.treatmentFee = treatmentFee;
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
        sb.append(", name=").append(name);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", treatmentFee=").append(treatmentFee);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}