package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumReadType;

@Table(name = "METER_RECORD_TEMP")
public class MeterRecordTemp {
	
    @Id
    @Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_RECORD_T_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CURR_DATE")
    private Date currDate;

    @Column(name = "CURR_READ")
    private String currRead;
    
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    
    @Column(name = "READ_RESULT")
    private Integer readResult;

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
    		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
     * @Title: getReadResult
     * @Description: 获取抄表结果 0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表
     * @return 
     */
    public Integer getReadResult() {
		return readResult;
	}

	/**
	 * @Title: setReadResult
	 * @Description: 设置抄表结果 0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表
	 * @param readResult 
	 */
	public void setReadResult(Integer readResult) {
		this.readResult = readResult;
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
        sb.append(", readResult=").append(readResult);
        sb.append("]");
        return sb.toString();
    }
}