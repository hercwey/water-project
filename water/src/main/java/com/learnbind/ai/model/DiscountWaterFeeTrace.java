package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "DISCOUNT_WATER_FEE_TRACE")
public class DiscountWaterFeeTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;

    @Column(name = "ACCOUNT_ITEM_ID")
    private Long accountItemId;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operationTime=").append(operationTime);
        sb.append("]");
        return sb.toString();
    }
}