package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "STAT_WATER_FEE_NO_TAX_DETAIL")
public class StatWaterFeeNoTaxDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT STAT_WF_NT_D_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "STAT_JSON")
    private String statJson;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "NOT_TAX_TOTAL_AMOUNT")
    private BigDecimal notTaxTotalAmount;

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
     * @return STAT_JSON
     */
    public String getStatJson() {
        return statJson;
    }

    /**
     * @param statJson
     */
    public void setStatJson(String statJson) {
        this.statJson = statJson == null ? null : statJson.trim();
    }

    /**
     * @return TOTAL_AMOUNT
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return NOT_TAX_TOTAL_AMOUNT
     */
    public BigDecimal getNotTaxTotalAmount() {
        return notTaxTotalAmount;
    }

    /**
     * @param notTaxTotalAmount
     */
    public void setNotTaxTotalAmount(BigDecimal notTaxTotalAmount) {
        this.notTaxTotalAmount = notTaxTotalAmount;
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
        sb.append(", statJson=").append(statJson);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", notTaxTotalAmount=").append(notTaxTotalAmount);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}