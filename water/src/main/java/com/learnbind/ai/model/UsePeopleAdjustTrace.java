package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "USE_PEOPLE_ADJUST_TRACE")
public class UsePeopleAdjustTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ADD_NUM")
    private Integer addNum;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "ENABLED")
    private Integer enabled;

    @Column(name = "WATER_AMOUNT")
    private BigDecimal waterAmount;

    @Column(name = "PARTITION_WATER_ID")
    private Long partitionWaterId;

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
     * @return ADD_NUM
     */
    public Integer getAddNum() {
        return addNum;
    }

    /**
     * @param addNum
     */
    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
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
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return ENABLED
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
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
     * @return PARTITION_WATER_ID
     */
    public Long getPartitionWaterId() {
        return partitionWaterId;
    }

    /**
     * @param partitionWaterId
     */
    public void setPartitionWaterId(Long partitionWaterId) {
        this.partitionWaterId = partitionWaterId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", addNum=").append(addNum);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", enabled=").append(enabled);
        sb.append(", waterAmount=").append(waterAmount);
        sb.append(", partitionWaterId=").append(partitionWaterId);
        sb.append("]");
        return sb.toString();
    }
}