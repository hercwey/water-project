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

import com.learnbind.ai.common.enumclass.EnumWaterNotifyStatus;
import com.learnbind.ai.common.enumclass.EnumNotifyUseLocation;

@Table(name = "WATER_NOTIFY")
public class WaterNotify {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT WATER_NOTIFY_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "METER_IDS")
    private String meterIds;

    @Column(name = "METER_RECORD")
    private String meterRecord;

    @Column(name = "SUM_BASE_AMOUNT")
    private BigDecimal sumBaseAmount;

    @Column(name = "SUM_SEWAGE_AMOUNT")
    private BigDecimal sumSewageAmount;

    @Column(name = "PAST_BASE_OWE_AMOUNT")
    private BigDecimal pastBaseOweAmount;

    @Column(name = "PAST_SEWAGE_OWE_AMOUNT")
    private BigDecimal pastSewageOweAmount;

    @Column(name = "TOTAL_OWE_AMOUNT")
    private BigDecimal totalOweAmount;

    @Column(name = "CUSTOMER_BALANCE")
    private BigDecimal customerBalance;

    @Column(name = "METER_READER")
    private String meterReader;

    @Column(name = "USE_LOCATION")
    private Integer useLocation;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "SERIAL_NO")
    private String serialNo;
    
    @Column(name = "REMARK")
    private String remark;

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
     * @return CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    /**
     * @Title: getCreateDateStr
     * @Description: 创建时间字符串
     * @return 
     */
    public String getCreateDateStr() {
    	if(createDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(createDate);
    	}
    	return null;
    }

    /**
     * @return OPERATOR
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
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
     * @return METER_IDS
     */
    public String getMeterIds() {
        return meterIds;
    }

    /**
     * @param meterIds
     */
    public void setMeterIds(String meterIds) {
        this.meterIds = meterIds == null ? null : meterIds.trim();
    }

    /**
     * @return METER_RECORD
     */
    public String getMeterRecord() {
        return meterRecord;
    }

    /**
     * @param meterRecord
     */
    public void setMeterRecord(String meterRecord) {
        this.meterRecord = meterRecord == null ? null : meterRecord.trim();
    }

    /**
     * @return SUM_BASE_AMOUNT
     */
    public BigDecimal getSumBaseAmount() {
        return sumBaseAmount;
    }

    /**
     * @param sumBaseAmount
     */
    public void setSumBaseAmount(BigDecimal sumBaseAmount) {
        this.sumBaseAmount = sumBaseAmount;
    }

    /**
     * @return SUM_SEWAGE_AMOUNT
     */
    public BigDecimal getSumSewageAmount() {
        return sumSewageAmount;
    }

    /**
     * @param sumSewageAmount
     */
    public void setSumSewageAmount(BigDecimal sumSewageAmount) {
        this.sumSewageAmount = sumSewageAmount;
    }

    /**
     * @return PAST_BASE_OWE_AMOUNT
     */
    public BigDecimal getPastBaseOweAmount() {
        return pastBaseOweAmount;
    }

    /**
     * @param pastBaseOweAmount
     */
    public void setPastBaseOweAmount(BigDecimal pastBaseOweAmount) {
        this.pastBaseOweAmount = pastBaseOweAmount;
    }

    /**
     * @return PAST_SEWAGE_OWE_AMOUNT
     */
    public BigDecimal getPastSewageOweAmount() {
        return pastSewageOweAmount;
    }

    /**
     * @param pastSewageOweAmount
     */
    public void setPastSewageOweAmount(BigDecimal pastSewageOweAmount) {
        this.pastSewageOweAmount = pastSewageOweAmount;
    }

    /**
     * @return TOTAL_OWE_AMOUNT
     */
    public BigDecimal getTotalOweAmount() {
        return totalOweAmount;
    }

    /**
     * @param totalOweAmount
     */
    public void setTotalOweAmount(BigDecimal totalOweAmount) {
        this.totalOweAmount = totalOweAmount;
    }

    /**
     * @return CUSTOMER_BALANCE
     */
    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    /**
     * @param customerBalance
     */
    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    /**
     * @return METER_READER
     */
    public String getMeterReader() {
        return meterReader;
    }

    /**
     * @param meterReader
     */
    public void setMeterReader(String meterReader) {
        this.meterReader = meterReader == null ? null : meterReader.trim();
    }

    /**
     * @return USE_LOCATION
     */
    public Integer getUseLocation() {
        return useLocation;
    }

    /**
     * @param useLocation
     */
    public void setUseLocation(Integer useLocation) {
        this.useLocation = useLocation;
    }
    
    /**
     * @Title: getUseLocationStr
     * @Description:是否使用地理位置查询界面
     * @return 
     */
    public String getUseLocationStr() {
    	if(useLocation!=null) {
    		return EnumNotifyUseLocation.getName(useLocation);
    	}
    	return null;
    }

    /**
     * @return STATUS
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @Title: getStatusStr
     * @Description: 通知单状态字符串
     * @return 
     */
    public String getStatusStr() {
    	if(status!=null) {
    		return EnumWaterNotifyStatus.getName(status);
    	}
    	return null;
    }

    /**
     * @return SERIAL_NO
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * @param serialNo
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }
    
    

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", createDate=").append(createDate);
        sb.append(", operator=").append(operator);
        sb.append(", period=").append(period);
        sb.append(", meterIds=").append(meterIds);
        sb.append(", meterRecord=").append(meterRecord);
        sb.append(", sumBaseAmount=").append(sumBaseAmount);
        sb.append(", sumSewageAmount=").append(sumSewageAmount);
        sb.append(", pastBaseOweAmount=").append(pastBaseOweAmount);
        sb.append(", pastSewageOweAmount=").append(pastSewageOweAmount);
        sb.append(", totalOweAmount=").append(totalOweAmount);
        sb.append(", customerBalance=").append(customerBalance);
        sb.append(", meterReader=").append(meterReader);
        sb.append(", useLocation=").append(useLocation);
        sb.append(", status=").append(status);
        sb.append(", serialNo=").append(serialNo);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}