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

@Table(name = "DISCOUNT_FINE_TRACE")
public class DiscountFineTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT DISCOUNT_FINE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ACCOUNT_ITEM_ID")
    private Long accountItemId;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "OVERDUE_FINE_ID")
    private Long overdueFineId;

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
     * @return DISCOUNT_AMOUNT
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    /**
     * @param discountAmount
     */
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
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
     * @return ACCOUNT_ITEM_ID
     */
    public Long getAccountItemId() {
        return accountItemId;
    }

    /**
     * @param accountItemId
     */
    public void setAccountItemId(Long accountItemId) {
        this.accountItemId = accountItemId;
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
    	if (operationTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operationTime);
    	}
    	return null;
    	
    }

    /**
     * @return OVERDUE_FINE_ID
     */
    public Long getOverdueFineId() {
        return overdueFineId;
    }

    /**
     * @param overdueFineId
     */
    public void setOverdueFineId(Long overdueFineId) {
        this.overdueFineId = overdueFineId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", customerId=").append(customerId);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", overdueFineId=").append(overdueFineId);
        sb.append("]");
        return sb.toString();
    }
}