package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "USER_METER_BOOK")
public class UserMeterBook {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT USER_METER_BOOK_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "METER_BOOK_ID")
    private Long meterBookId;

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
     * @return METER_BOOK_ID
     */
    public Long getMeterBookId() {
        return meterBookId;
    }

    /**
     * @param meterBookId
     */
    public void setMeterBookId(Long meterBookId) {
        this.meterBookId = meterBookId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", meterBookId=").append(meterBookId);
        sb.append("]");
        return sb.toString();
    }
}