package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "METER_RECORD_EDIT_LOG")
public class MeterRecordEditLog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_R_EL_TRACE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "METER_RECORD_ID")
    private Long meterRecordId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "RECORD_TYPE")
    private Integer recordType;

    @Column(name = "BEFORE_METER_READ")
    private String beforeMeterRead;

    @Column(name = "AFTER_METER_READ")
    private String afterMeterRead;

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
     * @return METER_RECORD_ID
     */
    public Long getMeterRecordId() {
        return meterRecordId;
    }

    /**
     * @param meterRecordId
     */
    public void setMeterRecordId(Long meterRecordId) {
        this.meterRecordId = meterRecordId;
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
     * @return BEFORE_METER_READ
     */
    public String getBeforeMeterRead() {
        return beforeMeterRead;
    }

    /**
     * @param beforeMeterRead
     */
    public void setBeforeMeterRead(String beforeMeterRead) {
        this.beforeMeterRead = beforeMeterRead == null ? null : beforeMeterRead.trim();
    }

    /**
     * @return AFTER_METER_READ
     */
    public String getAfterMeterRead() {
        return afterMeterRead;
    }

    /**
     * @param afterMeterRead
     */
    public void setAfterMeterRead(String afterMeterRead) {
        this.afterMeterRead = afterMeterRead == null ? null : afterMeterRead.trim();
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
        sb.append(", meterRecordId=").append(meterRecordId);
        sb.append(", period=").append(period);
        sb.append(", recordType=").append(recordType);
        sb.append(", beforeMeterRead=").append(beforeMeterRead);
        sb.append(", afterMeterRead=").append(afterMeterRead);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}