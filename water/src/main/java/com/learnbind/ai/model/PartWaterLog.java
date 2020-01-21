package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "PART_WATER_LOG")
public class PartWaterLog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT PART_WATER_L_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ORIGINAL_PART_WATER")
    private String originalPartWater;

    @Column(name = "REGEN_PART_WATER")
    private String regenPartWater;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "RECORD_TYPE")
    private Integer recordType;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATION_TIME")
    private Date operationTime;

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

    /**
     * @return CUSTOMER_ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return ORIGINAL_PART_WATER
     */
    public String getOriginalPartWater() {
        return originalPartWater;
    }

    /**
     * @param originalPartWater
     */
    public void setOriginalPartWater(String originalPartWater) {
        this.originalPartWater = originalPartWater == null ? null : originalPartWater.trim();
    }

    /**
     * @return REGEN_PART_WATER
     */
    public String getRegenPartWater() {
        return regenPartWater;
    }

    /**
     * @param regenPartWater
     */
    public void setRegenPartWater(String regenPartWater) {
        this.regenPartWater = regenPartWater == null ? null : regenPartWater.trim();
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
     * @return RECORD_TYPE
     */
    public Integer getRecordType() {
        return recordType;
    }

    /**
     * @param recordType
     */
    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    /**
     * @return OPERATOR_NAME
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * @return OPERATOR_ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return OPERATION_TIME
     */
    public Date getOperationTime() {
        return operationTime;
    }

    /**
     * @param operationTime
     */
    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
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
        sb.append(", meterId=").append(meterId);
        sb.append(", customerId=").append(customerId);
        sb.append(", originalPartWater=").append(originalPartWater);
        sb.append(", regenPartWater=").append(regenPartWater);
        sb.append(", period=").append(period);
        sb.append(", recordType=").append(recordType);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}