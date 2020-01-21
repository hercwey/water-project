package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumIsRefund;
import com.learnbind.ai.common.enumclass.EnumPledgeType;

@Table(name = "CUSTOMER_PLEDGE")
public class CustomerPledge {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUSTOMER_PLEDGE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "PLEDGE_TYPE")
    private Integer pledgeType;

    @Column(name = "PLEDGE_AMOUNT")
    private Long pledgeAmount;

    @Column(name = "IS_REFUND")
    private Integer isRefund;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;
    
    @Column(name = "OPERATOR_ID")
    private Long operatorId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "DELETED")
    private Integer deleted;

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
     * @return PLEDGE_TYPE
     */
    public Integer getPledgeType() {
        return pledgeType;
    }

    /**
     * @param pledgeType
     */
    public void setPledgeType(Integer pledgeType) {
        this.pledgeType = pledgeType;
    }
    
    /**
     * @Title: getPledgeTypeStr
     * @Description: 押金类型字符串
     * @return 
     */
    public String getPledgeTypeStr() {
    	return EnumPledgeType.getName(pledgeType);
    }

    /**
     * @return PLEDGE_AMOUNT
     */
    public Long getPledgeAmount() {
        return pledgeAmount;
    }

    /**
     * @param pledgeAmount
     */
    public void setPledgeAmount(Long pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    /**
     * @return IS_REFUND
     */
    public Integer getIsRefund() {
        return isRefund;
    }

    /**
     * @param isRefund
     */
    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }
    
    /**
     * @Title: getIsRefundStr
     * @Description: 是否字符串
     * @return 
     */
    public String getIsRefundStr() {
    	return EnumIsRefund.getName(isRefund);
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
    
    

    public Long getOperatorId() {
		return operatorId;
	}

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
     * @Title: getStartTimeStr
     * @Description: 获取检测时间字符串
     * @return 
     */
    public String getOperationTimeStr() {
    	if (operationTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operationTime);
    	}
    	return null;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName=").append(customerName);
        sb.append(", pledgeType=").append(pledgeType);
        sb.append(", pledgeAmount=").append(pledgeAmount);
        sb.append(", isRefund=").append(isRefund);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
    
    
}