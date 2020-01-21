package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumOperationType;

@Table(name = "CUSTOMERS_TRACE")
public class CustomersTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUS_TRACE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "OPERATION_TYPE")
    private Integer operationType;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    @Column(name = "REMARK")
	private String remark; 

    @Column(name = "CHANGE_BEFORE")
    private String changeBefore;

    @Column(name = "CHANGE_AFTER")
    private String changeAfter;

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
     * @return CUSTOMER_NAME
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
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
     * @Title: getWaterStatusStr
     * @Description: 获取客户用水状态字符串
     * @return 
     */
    public String getOperationTypeStr() {
    	if(operationType!=null) {
    		return EnumOperationType.getName(operationType);
    	}
    	return null;
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
    	if(operationTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operationTime);
    	}
    	return null;
    }

    

//    public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}

	/**
     * @return CHANGE_BEFORE
     */
    public String getChangeBefore() {
        return changeBefore;
    }

    /**
     * @param changeBefore
     */
    public void setChangeBefore(String changeBefore) {
        this.changeBefore = changeBefore == null ? null : changeBefore.trim();
    }

    /**
     * @return CHANGE_AFTER
     */
    public String getChangeAfter() {
        return changeAfter;
    }

    /**
     * @param changeAfter
     */
    public void setChangeAfter(String changeAfter) {
        this.changeAfter = changeAfter == null ? null : changeAfter.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName=").append(customerName);
        sb.append(", operationType=").append(operationType);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operationTime=").append(operationTime);
       // sb.append(", remark=").append(remark);
        sb.append(", changeBefore=").append(changeBefore);
        sb.append(", changeAfter=").append(changeAfter);
        sb.append("]");
        return sb.toString();
    }
}