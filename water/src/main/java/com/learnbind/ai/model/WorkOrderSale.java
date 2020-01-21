package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "WORK_ORDER_SALE")
public class WorkOrderSale {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT WORK_ORDER_S_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "WORK_ORDER_ID")
    private Long workOrderId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "REMARK")
    private String remark;

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
     * @return WORD_ORDER_ID
     */
    public Long getWorkOrderId() {
        return workOrderId;
    }

    /**
     * @param wordOrderId
     */
    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return USER_ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
        sb.append(", workOrderId=").append(workOrderId);
        sb.append(", userId=").append(userId);
        sb.append(", remark=").append(remark);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}