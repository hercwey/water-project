package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "ADD_SUB_WATER")
public class AddSubWater {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT AS_WATER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "METER_ID")
    private String meterId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "WATER_BEFORE")
    private BigDecimal waterBefore;

    @Column(name = "WATER_AFTER")
    private BigDecimal waterAfter;

    @Column(name = "COMPENSATION")
    private BigDecimal compensation;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OPERATION_TIME")
    private Date operationTime;
    
    @Column(name = "PARTITION_WATER_ID")
    private Long partitionWaterId;
    
    @Column(name = "METER_RECORD_ID")
    private Long meterRecordId;//抄表记录ID，一户多表时显示其中一个抄表ID
    

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
     * @return METER_ID
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * @param meterId
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
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
     * @return WATER_BEFORE
     */
    public BigDecimal getWaterBefore() {
        return waterBefore;
    }

    /**
     * @param waterBefore
     */
    public void setWaterBefore(BigDecimal waterBefore) {
        this.waterBefore = waterBefore;
    }

    /**
     * @return WATER_AFTER
     */
    public BigDecimal getWaterAfter() {
        return waterAfter;
    }

    /**
     * @param waterAfter
     */
    public void setWaterAfter(BigDecimal waterAfter) {
        this.waterAfter = waterAfter;
    }

    /**
     * @return COMPENSATION
     */
    public BigDecimal getCompensation() {
        return compensation;
    }

    /**
     * @param compensation
     */
    public void setCompensation(BigDecimal compensation) {
        this.compensation = compensation;
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
     * @Title: getOperationTimeStr
     * @Description: 获取操作时间字符串
     * @return 
     */
    public String getOperationTimeStr() {
    	if (operationTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operationTime);
    	}
    	return null;
    	
    }
    
    public Long getPartitionWaterId() {
		return partitionWaterId;
	}

	public void setPartitionWaterId(Long partitionWaterId) {
		this.partitionWaterId = partitionWaterId;
	}

	/**
	 * @Title: getMeterRecordId
	 * @Description: 获取抄表记录ID，一户多表时显示其中一个抄表ID
	 * @return 
	 */
	public Long getMeterRecordId() {
		return meterRecordId;
	}

	/**
	 * @Title: setMeterRecordId
	 * @Description: 设置抄表记录ID，一户多表时显示其中一个抄表ID
	 * @param meterRecordId 
	 */
	public void setMeterRecordId(Long meterRecordId) {
		this.meterRecordId = meterRecordId;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", meterId=").append(meterId);
        sb.append(", period=").append(period);
        sb.append(", waterBefore=").append(waterBefore);
        sb.append(", waterAfter=").append(waterAfter);
        sb.append(", compensation=").append(compensation);
        sb.append(", remark=").append(remark);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", partitionWaterId=").append(partitionWaterId);
        sb.append(", meterRecordId=").append(meterRecordId);
        sb.append("]");
        return sb.toString();
    }
}