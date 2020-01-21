package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "METERS_TRACE")
public class MetersTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METERS_TRACE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "OPERATION_TYPE")
    private Integer operationType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "OPERATION_DATE")
    private Date operationDate;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

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
     * @return OPERATION_TYPE
     */
    public Integer getOperationType() {
        return operationType;
    }

    /**
     * @param operationType
     */
    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
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

    /**
     * @return OPERATION_DATE
     */
    public Date getOperationDate() {
        return operationDate;
    }

    /**
     * @param operationDate
     */
    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterId=").append(meterId);
        sb.append(", customerId=").append(customerId);
        sb.append(", operationType=").append(operationType);
        sb.append(", remark=").append(remark);
        sb.append(", operationDate=").append(operationDate);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append("]");
        return sb.toString();
    }
}