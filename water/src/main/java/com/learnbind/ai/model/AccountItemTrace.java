package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ACCOUNT_ITEM_TRACE")
public class AccountItemTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT AI_TRACE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PRIMARY_ID")
    private Long primaryId;

    @Column(name = "PRIMARY_SUBJECT")
    private String primarySubject;

    @Column(name = "SECONDARY_ID")
    private Long secondaryId;

    @Column(name = "SECONDARY_SUBJECT")
    private String secondarySubject;

    @Column(name = "OPERATE")
    private String operate;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CREATE_TIME")
    private Date createTime;

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
     * @return PRIMARY_ID
     */
    public Long getPrimaryId() {
        return primaryId;
    }

    /**
     * @param primaryId
     */
    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    /**
     * @return PRIMARY_SUBJECT
     */
    public String getPrimarySubject() {
        return primarySubject;
    }

    /**
     * @param primarySubject
     */
    public void setPrimarySubject(String primarySubject) {
        this.primarySubject = primarySubject == null ? null : primarySubject.trim();
    }

    /**
     * @return SECONDARY_ID
     */
    public Long getSecondaryId() {
        return secondaryId;
    }

    /**
     * @param secondaryId
     */
    public void setSecondaryId(Long secondaryId) {
        this.secondaryId = secondaryId;
    }

    /**
     * @return SECONDARY_SUBJECT
     */
    public String getSecondarySubject() {
        return secondarySubject;
    }

    /**
     * @param secondarySubject
     */
    public void setSecondarySubject(String secondarySubject) {
        this.secondarySubject = secondarySubject == null ? null : secondarySubject.trim();
    }

    /**
     * @return OPERATE
     */
    public String getOperate() {
        return operate;
    }

    /**
     * @param operate
     */
    public void setOperate(String operate) {
        this.operate = operate == null ? null : operate.trim();
    }

    /**
     * @return AMOUNT
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", primaryId=").append(primaryId);
        sb.append(", primarySubject=").append(primarySubject);
        sb.append(", secondaryId=").append(secondaryId);
        sb.append(", secondarySubject=").append(secondarySubject);
        sb.append(", operate=").append(operate);
        sb.append(", amount=").append(amount);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}