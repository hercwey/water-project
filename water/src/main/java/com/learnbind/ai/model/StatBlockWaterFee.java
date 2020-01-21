package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "STAT_BLOCK_WATER_FEE")
public class StatBlockWaterFee {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT STAT_B_W_FEE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "PAYMENT_MODE")
    private String paymentMode;

    @Column(name = "STAT_BLOCK_JSON")
    private String statBlockJson;

    @Column(name = "STAT_TOTAL_JSON")
    private String statTotalJson;

    @Column(name = "STAT_WATER_PRICE_JSON")
    private String statWaterPriceJson;

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
     * @return PAYMENT_MODE
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * @param paymentMode
     */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode == null ? null : paymentMode.trim();
    }

    /**
     * @return STAT_BLOCK_JSON
     */
    public String getStatBlockJson() {
        return statBlockJson;
    }

    /**
     * @param statBlockJson
     */
    public void setStatBlockJson(String statBlockJson) {
        this.statBlockJson = statBlockJson == null ? null : statBlockJson.trim();
    }

    /**
     * @return STAT_TOTAL_JSON
     */
    public String getStatTotalJson() {
        return statTotalJson;
    }

    /**
     * @param statTotalJson
     */
    public void setStatTotalJson(String statTotalJson) {
        this.statTotalJson = statTotalJson == null ? null : statTotalJson.trim();
    }

    /**
     * @return STAT_WATER_PRICE_JSON
     */
    public String getStatWaterPriceJson() {
        return statWaterPriceJson;
    }

    /**
     * @param statWaterPriceJson
     */
    public void setStatWaterPriceJson(String statWaterPriceJson) {
        this.statWaterPriceJson = statWaterPriceJson == null ? null : statWaterPriceJson.trim();
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
        sb.append(", paymentMode=").append(paymentMode);
        sb.append(", statBlockJson=").append(statBlockJson);
        sb.append(", statTotalJson=").append(statTotalJson);
        sb.append(", statWaterPriceJson=").append(statWaterPriceJson);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}