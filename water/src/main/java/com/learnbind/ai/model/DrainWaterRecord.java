package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "DRAIN_WATER_RECORD")
public class DrainWaterRecord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT DRAIN_WATER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "DRAIN_NAME")
    private String drainName;

    @Column(name = "CALIBER")
    private String caliber;

    @Column(name = "DRAIN_CALIBER")
    private String drainCaliber;

    @Column(name = "WATER_AMOUNT")
    private BigDecimal waterAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "PERIOD")
    private String period;

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
     * @return DRAIN_NAME
     */
    public String getDrainName() {
        return drainName;
    }

    /**
     * @param drainName
     */
    public void setDrainName(String drainName) {
        this.drainName = drainName == null ? null : drainName.trim();
    }

    /**
     * @return CALIBER
     */
    public String getCaliber() {
        return caliber;
    }

    /**
     * @param caliber
     */
    public void setCaliber(String caliber) {
        this.caliber = caliber == null ? null : caliber.trim();
    }

    /**
     * @return DRAIN_CALIBER
     */
    public String getDrainCaliber() {
        return drainCaliber;
    }

    /**
     * @param drainCaliber
     */
    public void setDrainCaliber(String drainCaliber) {
        this.drainCaliber = drainCaliber == null ? null : drainCaliber.trim();
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
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", drainName=").append(drainName);
        sb.append(", caliber=").append(caliber);
        sb.append(", drainCaliber=").append(drainCaliber);
        sb.append(", waterAmount=").append(waterAmount);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append(", period=").append(period);
        sb.append("]");
        return sb.toString();
    }
}