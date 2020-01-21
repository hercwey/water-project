package com.learnbind.ai.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_PEOPLE_ADJUST")
public class SysPeopleAdjust {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "WATER_AMOUNT")
    private BigDecimal waterAmount;

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
     * @return WATER_AMOUNT
     */
    public BigDecimal getWaterAmount() {
        return waterAmount;
    }

    /**
     * @param waterAmount
     */
    public void setWaterAmount(BigDecimal waterAmount) {
        this.waterAmount = waterAmount;
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
        sb.append(", waterAmount=").append(waterAmount);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}