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

import com.learnbind.ai.common.enumclass.EnumMakeBillStatus;

@Table(name = "PARTITION_WATER")
public class PartitionWater {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT PARTITION_WATER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private String meterId;

    @Column(name = "RECORD_ID")
    private String recordId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "WATER_AMOUNT")
    private BigDecimal waterAmount;

    @Column(name = "WATER_USE")
    private String waterUse;

    @Column(name = "WATER_PRICE")
    private BigDecimal waterPrice;

    @Column(name = "WATER_FEE")
    private BigDecimal waterFee;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "REMARK")
    private String remark;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "START_TIME")
    private Date startTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "END_TIME")
    private Date endTime;
    
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    
    @Column(name = "BASE_PRICE")
    private BigDecimal basePrice;
    
    @Column(name = "TREATMENT_FEE")
    private BigDecimal treatmentFee;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATE_TIME")
    private Date createTime;//记录创建时间
    
    @Column(name = "IS_MAKE_BILL")
    private Integer isMakeBill;
    
    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "PID")
    private Long pid;
    
    @Column(name = "ACCOUNT_ITEM_ID")
    private Long accountItemId;
    
    /**
     * @Fields realWaterAmount：实际水量
     */
    @Column(name = "REAL_WATER_AMOUNT")
    private BigDecimal realWaterAmount;
    
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
     * @return RECORD_ID
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * @param recordId
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
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
     * @return WATER_USE
     */
    public String getWaterUse() {
        return waterUse;
    }

    /**
     * @param waterUse
     */
    public void setWaterUse(String waterUse) {
        this.waterUse = waterUse == null ? null : waterUse.trim();
    }

    /**
     * @return WATER_PRICE
     */
    public BigDecimal getWaterPrice() {
        return waterPrice;
    }

    /**
     * @param waterPrice
     */
    public void setWaterPrice(BigDecimal waterPrice) {
        this.waterPrice = waterPrice;
    }

    /**
     * @return WATER_FEE
     */
    public BigDecimal getWaterFee() {
        return waterFee;
    }

    /**
     * @param waterFee
     */
    public void setWaterFee(BigDecimal waterFee) {
        this.waterFee = waterFee;
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

    /**
     * @return START_TIME
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @Title: getStartTimeStr
     * @Description: 获取账单开始时间字符串
     * @return 
     */
    public String getStartTimeStr() {
    	if (startTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(startTime);
    	}
    	return null;
    	
    }

    /**
     * @return END_TIME
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    /**
     * @Title: getEndTimeStr
     * @Description: 获取结束时间字符串
     * @return 
     */
    public String getEndTimeStr() {
    	if (endTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(endTime);
    	}
    	return null;
    	
    }
    
    /**
     * @Title: getCustomerId
     * @Description: 获取客户ID
     * @return 
     */
    public Long getCustomerId() {
		return customerId;
	}

	/**
	 * @Title: setCustomerId
	 * @Description: 设置客户ID
	 * @param customerId 
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public BigDecimal getTreatmentFee() {
		return treatmentFee;
	}

	public void setTreatmentFee(BigDecimal treatmentFee) {
		this.treatmentFee = treatmentFee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsMakeBill() {
		return isMakeBill;
	}
	
	public String getIsMakeBillStr() {
		return EnumMakeBillStatus.getName(isMakeBill);
	}

	public void setIsMakeBill(Integer isMakeBill) {
		this.isMakeBill = isMakeBill;
	}
	
	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * @Title: getAccountItemId
	 * @Description: 获取账目ID
	 * @return 
	 */
	public Long getAccountItemId() {
		return accountItemId;
	}

	/**
	 * @Title: setAccountItemId
	 * @Description: 设置账目ID
	 * @param accountItemId 
	 */
	public void setAccountItemId(Long accountItemId) {
		this.accountItemId = accountItemId;
	}

	public BigDecimal getRealWaterAmount() {
		return realWaterAmount;
	}

	public void setRealWaterAmount(BigDecimal realWaterAmount) {
		this.realWaterAmount = realWaterAmount;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterId=").append(meterId);
        sb.append(", recordId=").append(recordId);
        sb.append(", period=").append(period);
        sb.append(", waterAmount=").append(waterAmount);
        sb.append(", waterUse=").append(waterUse);
        sb.append(", waterPrice=").append(waterPrice);
        sb.append(", waterFee=").append(waterFee);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", customerId=").append(customerId);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", treatmentFee=").append(treatmentFee);
        sb.append(", createTime=").append(createTime);
        sb.append(", isMakeBill=").append(isMakeBill);
        sb.append(", deleted=").append(deleted);
        sb.append(", pid=").append(pid);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append(", realWaterAmount=").append(realWaterAmount);
        sb.append("]");
        return sb.toString();
    }
}