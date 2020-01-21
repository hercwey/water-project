package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "METER_BOOK_METER")
public class MeterBookMeter {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_BOOK_METER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_BOOK_ID")
    private Long meterBookId;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

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

    /**
     * @return METER_ID
     */
    public Long getMeterId() {
        return meterId;
    }

    /**
     * @param meterId
     */
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterBookId=").append(meterBookId);
        sb.append(", meterId=").append(meterId);
        sb.append(", customerId=").append(customerId);
        sb.append("]");
        return sb.toString();
    }
}