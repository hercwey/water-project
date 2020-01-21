package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "STAT_MONTH_USE_WATER")
public class StatMonthUseWater {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT STAT_M_U_WATER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "WATER_AMOUNT")
    private BigDecimal waterAmount;

    @Column(name = "REAL_WATER_AMOUNT")
    private BigDecimal realWaterAmount;

    @Column(name = "CREATE_TIME")
    private Date createTime;

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
     * @return REAL_WATER_AMOUNT
     */
    public BigDecimal getRealWaterAmount() {
        return realWaterAmount;
    }

    /**
     * @param realWaterAmount
     */
    public void setRealWaterAmount(BigDecimal realWaterAmount) {
        this.realWaterAmount = realWaterAmount;
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", period=").append(period);
        sb.append(", waterAmount=").append(waterAmount);
        sb.append(", realWaterAmount=").append(realWaterAmount);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}