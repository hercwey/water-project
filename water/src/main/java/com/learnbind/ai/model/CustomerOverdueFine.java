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

@Table(name = "CUSTOMER_OVERDUE_FINE")
public class CustomerOverdueFine {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUS_OVERDUE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "BASE_AMOUNT")
    private BigDecimal baseAmount;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "START_TIME")
    private Date startTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "OVERDUE_FINE")
    private BigDecimal overdueFine;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "OVERDUE_STATUS")
    private Integer overdueStatus;
    
    @Column(name = "OVERDUE_ACCOUNT_ID")
    private Long overdueAccountId;

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
     * @return BASE_AMOUNT
     */
    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    /**
     * @param baseAmount
     */
    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
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
     * @Description: 获取计算开始日期字符串
     * @return 
     */
    public String getStartTimeStr() {
    	if (startTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
     * @Description: 获取计算终止日期字符串
     * @return 
     */
    public String getEndTimeStr() {
    	if (endTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(endTime);
    	}
    	return null;
    }

    /**
     * @return OVERDUE_FINE
     */
    public BigDecimal getOverdueFine() {
        return overdueFine;
    }

    /**
     * @param overdueFine
     */
    public void setOverdueFine(BigDecimal overdueFine) {
        this.overdueFine = overdueFine;
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
     * @return OVERDUE_STATUS
     */
    public Integer getOverdueStatus() {
        return overdueStatus;
    }

    /**
     * @param overdueStatus
     */
    public void setOverdueStatus(Integer overdueStatus) {
        this.overdueStatus = overdueStatus;
    }
    
    

    public Long getOverdueAccountId() {
		return overdueAccountId;
	}

	public void setOverdueAccountId(Long overdueAccountId) {
		this.overdueAccountId = overdueAccountId;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", period=").append(period);
        sb.append(", baseAmount=").append(baseAmount);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", overdueFine=").append(overdueFine);
        sb.append(", remark=").append(remark);
        sb.append(", overdueStatus=").append(overdueStatus);
        sb.append(", overdueAccountId=").append(overdueAccountId);
        sb.append("]");
        return sb.toString();
    }
}