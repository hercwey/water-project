package com.learnbind.ai.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "CUSTOMER_POLICY_DISCOUNT")
public class CustomerPolicyDiscount {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "WATER_NUM")
    private String waterNum;

    @Column(name = "DISCOUNT_NUM")
    private String discountNum;

    @Column(name = "BASE_PRICE")
    private BigDecimal basePrice;

    @Column(name = "TREATMENT_FEE")
    private BigDecimal treatmentFee;

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
     * @return WATER_NUM
     */
    public String getWaterNum() {
        return waterNum;
    }

    /**
     * @param waterNum
     */
    public void setWaterNum(String waterNum) {
        this.waterNum = waterNum == null ? null : waterNum.trim();
    }

    /**
     * @return DISCOUNT_NUM
     */
    public String getDiscountNum() {
        return discountNum;
    }

    /**
     * @param discountNum
     */
    public void setDiscountNum(String discountNum) {
        this.discountNum = discountNum == null ? null : discountNum.trim();
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
     * @return TREATMENT_FEE
     */
    public BigDecimal getTreatmentFee() {
        return treatmentFee;
    }

    /**
     * @param treatmentFee
     */
    public void setTreatmentFee(BigDecimal treatmentFee) {
        this.treatmentFee = treatmentFee;
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
        sb.append(", waterNum=").append(waterNum);
        sb.append(", discountNum=").append(discountNum);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", treatmentFee=").append(treatmentFee);
        sb.append("]");
        return sb.toString();
    }
}