package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "STAT_OWE_COMPANY_METER")
public class StatOweCompanyMeter {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT STAT_O_C_METER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OWE_TOTAL_AMOUNT")
    private BigDecimal oweTotalAmount;

    @Column(name = "PRE_OWE_AMOUNT")
    private BigDecimal preOweAmount;

    @Column(name = "CURR_OWE_AMOUNT")
    private BigDecimal currOweAmount;

    @Column(name = "SEWAGE_OWE_AMOUNT")
    private BigDecimal sewageOweAmount;

    @Column(name = "WATER_RESOURCE")
    private BigDecimal waterResource;

    @Column(name = "REAMRK")
    private String reamrk;

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
     * @return OWE_TOTAL_AMOUNT
     */
    public BigDecimal getOweTotalAmount() {
        return oweTotalAmount;
    }

    /**
     * @param oweTotalAmount
     */
    public void setOweTotalAmount(BigDecimal oweTotalAmount) {
        this.oweTotalAmount = oweTotalAmount;
    }

    /**
     * @return PRE_OWE_AMOUNT
     */
    public BigDecimal getPreOweAmount() {
        return preOweAmount;
    }

    /**
     * @param preOweAmount
     */
    public void setPreOweAmount(BigDecimal preOweAmount) {
        this.preOweAmount = preOweAmount;
    }

    /**
     * @return CURR_OWE_AMOUNT
     */
    public BigDecimal getCurrOweAmount() {
        return currOweAmount;
    }

    /**
     * @param currOweAmount
     */
    public void setCurrOweAmount(BigDecimal currOweAmount) {
        this.currOweAmount = currOweAmount;
    }

    /**
     * @return SEWAGE_OWE_AMOUNT
     */
    public BigDecimal getSewageOweAmount() {
        return sewageOweAmount;
    }

    /**
     * @param sewageOweAmount
     */
    public void setSewageOweAmount(BigDecimal sewageOweAmount) {
        this.sewageOweAmount = sewageOweAmount;
    }

    /**
     * @return WATER_RESOURCE
     */
    public BigDecimal getWaterResource() {
        return waterResource;
    }

    /**
     * @param waterResource
     */
    public void setWaterResource(BigDecimal waterResource) {
        this.waterResource = waterResource;
    }

    /**
     * @return REAMRK
     */
    public String getReamrk() {
        return reamrk;
    }

    /**
     * @param reamrk
     */
    public void setReamrk(String reamrk) {
        this.reamrk = reamrk == null ? null : reamrk.trim();
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
        sb.append(", name=").append(name);
        sb.append(", oweTotalAmount=").append(oweTotalAmount);
        sb.append(", preOweAmount=").append(preOweAmount);
        sb.append(", currOweAmount=").append(currOweAmount);
        sb.append(", sewageOweAmount=").append(sewageOweAmount);
        sb.append(", waterResource=").append(waterResource);
        sb.append(", reamrk=").append(reamrk);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}