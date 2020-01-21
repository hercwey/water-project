package com.learnbind.ai.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "WATER_NOTIFY_DETAIL")
public class WaterNotifyDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT NOTIFY_DETAIL_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "WATER_NOTIFY_ID")
    private Long waterNotifyId;

    @Column(name = "PART_WATER_ID")
    private String partWaterId;

    @Column(name = "REAL_WATER_AMOUNT")
    private BigDecimal realWaterAmount;

    @Column(name = "BASE_PRICE")
    private BigDecimal basePrice;

    @Column(name = "SEWAGE_PRICE")
    private BigDecimal sewagePrice;

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
     * @return WATER_NOTIFY_ID
     */
    public Long getWaterNotifyId() {
        return waterNotifyId;
    }

    /**
     * @param waterNotifyId
     */
    public void setWaterNotifyId(Long waterNotifyId) {
        this.waterNotifyId = waterNotifyId;
    }

    /**
     * @return PART_WATER_ID
     */
    public String getPartWaterId() {
        return partWaterId;
    }

    /**
     * @param partWaterId
     */
    public void setPartWaterId(String partWaterId) {
        this.partWaterId = partWaterId == null ? null : partWaterId.trim();
    }

    /**
     * @return REAL_WATER_AMOUNT
     */
    public BigDecimal getRealWaterAmount() {
        return realWaterAmount;
    }

    /**
     * @param realWaterAmount
     */
    public void setRealWaterAmount(BigDecimal realWaterAmount) {
        this.realWaterAmount = realWaterAmount;
    }

    /**
     * @return BASE_PRICE
     */
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    /**
     * @param basePrice
     */
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * @return SEWAGE_PRICE
     */
    public BigDecimal getSewagePrice() {
        return sewagePrice;
    }

    /**
     * @param sewagePrice
     */
    public void setSewagePrice(BigDecimal sewagePrice) {
        this.sewagePrice = sewagePrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", waterNotifyId=").append(waterNotifyId);
        sb.append(", partWaterId=").append(partWaterId);
        sb.append(", realWaterAmount=").append(realWaterAmount);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", sewagePrice=").append(sewagePrice);
        sb.append("]");
        return sb.toString();
    }
}