package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "STAT_MONTH_READ_METER_RATE")
public class StatMonthReadMeterRate {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT STAT_M_R_M_RATE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "RECE_READ_METER")
    private BigDecimal receReadMeter;

    @Column(name = "ACTUAL_READ_METER")
    private BigDecimal actualReadMeter;

    @Column(name = "READ_METER_RATE")
    private BigDecimal readMeterRate;

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
     * @return RECE_READ_METER
     */
    public BigDecimal getReceReadMeter() {
        return receReadMeter;
    }

    /**
     * @param receReadMeter
     */
    public void setReceReadMeter(BigDecimal receReadMeter) {
        this.receReadMeter = receReadMeter;
    }

    /**
     * @return ACTUAL_READ_METER
     */
    public BigDecimal getActualReadMeter() {
        return actualReadMeter;
    }

    /**
     * @param actualReadMeter
     */
    public void setActualReadMeter(BigDecimal actualReadMeter) {
        this.actualReadMeter = actualReadMeter;
    }

    /**
     * @return READ_METER_RATE
     */
    public BigDecimal getReadMeterRate() {
        return readMeterRate;
    }

    /**
     * @param readMeterRate
     */
    public void setReadMeterRate(BigDecimal readMeterRate) {
        this.readMeterRate = readMeterRate;
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
        sb.append(", receReadMeter=").append(receReadMeter);
        sb.append(", actualReadMeter=").append(actualReadMeter);
        sb.append(", readMeterRate=").append(readMeterRate);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}