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

import com.learnbind.ai.common.enumclass.EnumReadType;

@Table(name = "METER_RECORD")
public class MeterRecord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_RECORD_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "PERIOD")
    private String period;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CURR_DATE")
    private Date currDate;

    @Column(name = "CURR_READ")
    private String currRead;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "PRE_DATE")
    private Date preDate;

    @Column(name = "PRE_READ")
    private String preRead;

    @Column(name = "CURR_AMOUNT")
    private BigDecimal currAmount;

    @Column(name = "READ_TYPE")
    private Integer readType;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "READ_MODE")
    private String readMode;
    
    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "IS_PART_WATER")
    private Integer isPartWater;//是否已分水量，0=否（默认值）；1=是
    
    @Column(name = "IS_ADD_SUB_WATER")
    private Integer isAddSubWater;//是否已追加/减免水量，，0=否（默认值）；1=是
    
    @Column(name = "METER_USE")
    private String meterUse;//水表用途（如：计费表、计量表等）
    
    @Column(name = "METER_TREE_ID")
    private Long meterTreeId;//表计父子关系表ID
    
    @Column(name = "IS_MAKE_BILL")
    private Integer isMakeBill;//是否已开账：0=未开账（默认值）；1=已开账
    
    @Column(name = "STATUS")
    private Integer status;//抄表记录状态；0正常，1手工修改

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
     * @return CURR_DATE
     */
    public Date getCurrDate() {
        return currDate;
    }

    /**
     * @param currDate
     */
    public void setCurrDate(Date currDate) {
        this.currDate = currDate;
    }
    
    /**
     * @Title: getCurrDateStr
     * @Description: 获取本期抄表日期字符串
     * @return 
     */
    public String getCurrDateStr() {
    	if(currDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(currDate);
    	}
    	return null;
    }

    /**
     * @return CURR_READ
     */
    public String getCurrRead() {
        return currRead;
    }

    /**
     * @param currRead
     */
    public void setCurrRead(String currRead) {
        this.currRead = currRead == null ? null : currRead.trim();
    }

    /**
     * @return PRE_DATE
     */
    public Date getPreDate() {
        return preDate;
    }

    /**
     * @param preDate
     */
    public void setPreDate(Date preDate) {
        this.preDate = preDate;
    }
    
    /**
     * @Title: getPreDateStr
     * @Description: 获取上期抄表日期字符串
     * @return 
     */
    public String getPreDateStr() {
    	if(preDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(preDate);
    	}
    	return null;
    }

    /**
     * @return PRE_READ
     */
    public String getPreRead() {
        return preRead;
    }

    /**
     * @param preRead
     */
    public void setPreRead(String preRead) {
        this.preRead = preRead == null ? null : preRead.trim();
    }

    /**
     * @return CURR_AMOUNT
     */
    public BigDecimal getCurrAmount() {
        return currAmount;
    }

    /**
     * @param currAmount
     */
    public void setCurrAmount(BigDecimal currAmount) {
        this.currAmount = currAmount;
    }

    /**
     * @return READ_TYPE
     */
    public Integer getReadType() {
        return readType;
    }

    /**
     * @param readType
     */
    public void setReadType(Integer readType) {
        this.readType = readType;
    }
    
    /**
     * @Title: getReadTypeStr
     * @Description: 抄表类型字符串
     * @return 
     */
    public String getReadTypeStr() {
    	return EnumReadType.getName(readType);
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
     * @Description: 获取操作日期字符串
     * @return 
     */
    public String getOperationTimeStr() {
    	if(operationTime!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operationTime);
    	}
    	return null;
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
     * @return READ_MODE
     */
    public String getReadMode() {
        return readMode;
    }

    /**
     * @param readMode
     */
    public void setReadMode(String readMode) {
        this.readMode = readMode == null ? null : readMode.trim();
    }
    
    /**
     * @return DELETED
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * @param deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

	/**
	 * @Title: getIsPartWater
	 * @Description: 是否已分水量，0=否（默认值）；1=是
	 * @return 
	 */
	public Integer getIsPartWater() {
		return isPartWater;
	}

	/**
	 * @Title: setIsPartWater
	 * @Description: 是否已分水量，0=否（默认值）；1=是
	 * @param isPartWater 
	 */
	public void setIsPartWater(Integer isPartWater) {
		this.isPartWater = isPartWater;
	}

	/**
	 * @Title: getIsAddSubWater
	 * @Description: 是否已追加/减免水量，，0=否（默认值）；1=是
	 * @return 
	 */
	public Integer getIsAddSubWater() {
		return isAddSubWater;
	}

	/**
	 * @Title: setIsAddSubWater
	 * @Description: 是否已追加/减免水量，，0=否（默认值）；1=是
	 * @param isAddSubWater 
	 */
	public void setIsAddSubWater(Integer isAddSubWater) {
		this.isAddSubWater = isAddSubWater;
	}

	/**
	 * @Title: getMeterUse
	 * @Description: 获取表计用途（如：计费表、计量表等）
	 * @return 
	 */
	public String getMeterUse() {
		return meterUse;
	}

	/**
	 * @Title: setMeterUse
	 * @Description: 设置表计用途（如：计费表、计量表等）
	 * @param meterUse 
	 */
	public void setMeterUse(String meterUse) {
		this.meterUse = meterUse;
	}

	/**
	 * @Title: getMeterTreeId
	 * @Description: 获取表计父子关系ID
	 * @return 
	 */
	public Long getMeterTreeId() {
		return meterTreeId;
	}

	/**
	 * @Title: setMeterTreeId
	 * @Description: 设置表计父子关系ID
	 * @param meterTreeId 
	 */
	public void setMeterTreeId(Long meterTreeId) {
		this.meterTreeId = meterTreeId;
	}

	/**
	 * @Title: getIsMakeBill
	 * @Description: 获取是否已开账状态，0=未开账（默认值）；1=已开账
	 * @return 
	 */
	public Integer getIsMakeBill() {
		return isMakeBill;
	}

	/**
	 * @Title: setIsMakeBill
	 * @Description: 设置是否已开账状态，0=未开账（默认值）；1=已开账
	 * @param isMakeBill 
	 */
	public void setIsMakeBill(Integer isMakeBill) {
		this.isMakeBill = isMakeBill;
	}
	
	
	
	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
        sb.append(", period=").append(period);
        sb.append(", currDate=").append(currDate);
        sb.append(", currRead=").append(currRead);
        sb.append(", preDate=").append(preDate);
        sb.append(", preRead=").append(preRead);
        sb.append(", currAmount=").append(currAmount);
        sb.append(", readType=").append(readType);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append(", readMode=").append(readMode);
        sb.append(", deleted=").append(deleted);
        sb.append(", isPartWater=").append(isPartWater);
        sb.append(", isAddSubWater=").append(isAddSubWater);
        sb.append(", meterUse=").append(meterUse);
        sb.append(", meterTreeId=").append(meterTreeId);
        sb.append(", isMakeBill=").append(isMakeBill);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}