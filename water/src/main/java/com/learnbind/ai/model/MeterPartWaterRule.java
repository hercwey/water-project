package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "METER_PART_WATER_RULE")
public class MeterPartWaterRule {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT PART_WATER_RULE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "RULE_TYPE")
    private Integer ruleType;

    @Column(name = "RULE_SHOW")
    private String ruleShow;

    @Column(name = "RULE_REAL")
    private String ruleReal;

    @Column(name = "WATER_PRICE_ID")
    private Long waterPriceId;

    @Column(name = "IS_DEFAULT")
    private Integer isDefault;

    @Column(name = "METER_ID")
    private Long meterId;

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
     * @return RULE_TYPE
     */
    public Integer getRuleType() {
        return ruleType;
    }

    /**
     * @param ruleType
     */
    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * @return RULE_SHOW
     */
    public String getRuleShow() {
        return ruleShow;
    }

    /**
     * @param ruleShow
     */
    public void setRuleShow(String ruleShow) {
        this.ruleShow = ruleShow == null ? null : ruleShow.trim();
    }

    /**
     * @return RULE_REAL
     */
    public String getRuleReal() {
        return ruleReal;
    }

    /**
     * @param ruleReal
     */
    public void setRuleReal(String ruleReal) {
        this.ruleReal = ruleReal == null ? null : ruleReal.trim();
    }

    /**
     * @return WATER_PRICE_ID
     */
    public Long getWaterPriceId() {
        return waterPriceId;
    }

    /**
     * @param waterPriceId
     */
    public void setWaterPriceId(Long waterPriceId) {
        this.waterPriceId = waterPriceId;
    }

    /**
     * @return IS_DEFAULT
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * @return METER_ID
     */
    public Long getMeterId() {
        return meterId;
    }

    /**
     * @param meterId
     */
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ruleType=").append(ruleType);
        sb.append(", ruleShow=").append(ruleShow);
        sb.append(", ruleReal=").append(ruleReal);
        sb.append(", waterPriceId=").append(waterPriceId);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", meterId=").append(meterId);
        sb.append("]");
        return sb.toString();
    }
}