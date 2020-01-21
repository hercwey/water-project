package com.learnbind.ai.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "USE_WATER_PRICE_TRACE")
public class UseWaterPriceTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT USE_WP_TRACE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PRICE_TYPE_NAME")
    private String priceTypeName;

    @Column(name = "PRICE_TYPE_CODE")
    private String priceTypeCode;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "LADDER_NAME")
    private String ladderName;

    @Column(name = "LADDER_START")
    private String ladderStart;

    @Column(name = "LADDER_END")
    private String ladderEnd;

    @Column(name = "BASE_PRICE")
    private BigDecimal basePrice;

    @Column(name = "TREATMENT_FEE")
    private BigDecimal treatmentFee;

    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "PARTITION_WATER_ID")
    private Long partitionWaterId;

    @Column(name = "ACCOUNT_ITEM_ID")
    private Long accountItemId;

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
     * @return PRICE_TYPE_NAME
     */
    public String getPriceTypeName() {
        return priceTypeName;
    }

    /**
     * @param priceTypeName
     */
    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName == null ? null : priceTypeName.trim();
    }

    /**
     * @return PRICE_TYPE_CODE
     */
    public String getPriceTypeCode() {
        return priceTypeCode;
    }

    /**
     * @param priceTypeCode
     */
    public void setPriceTypeCode(String priceTypeCode) {
        this.priceTypeCode = priceTypeCode == null ? null : priceTypeCode.trim();
    }

    /**
     * @return PRICE_CODE
     */
    public String getPriceCode() {
        return priceCode;
    }

    /**
     * @param priceCode
     */
    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode == null ? null : priceCode.trim();
    }

    /**
     * @return LADDER_NAME
     */
    public String getLadderName() {
        return ladderName;
    }

    /**
     * @param ladderName
     */
    public void setLadderName(String ladderName) {
        this.ladderName = ladderName == null ? null : ladderName.trim();
    }

    /**
     * @return LADDER_START
     */
    public String getLadderStart() {
        return ladderStart;
    }

    /**
     * @param ladderStart
     */
    public void setLadderStart(String ladderStart) {
        this.ladderStart = ladderStart == null ? null : ladderStart.trim();
    }

    /**
     * @return LADDER_END
     */
    public String getLadderEnd() {
        return ladderEnd;
    }

    /**
     * @param ladderEnd
     */
    public void setLadderEnd(String ladderEnd) {
        this.ladderEnd = ladderEnd == null ? null : ladderEnd.trim();
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

    /**
     * @return TOTAL_PRICE
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    /**
     * @return ACCOUNT_ITEM_ID
     */
    public Long getAccountItemId() {
        return accountItemId;
    }

    /**
     * @param accountItemId
     */
    public void setAccountItemId(Long accountItemId) {
        this.accountItemId = accountItemId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", priceTypeName=").append(priceTypeName);
        sb.append(", priceTypeCode=").append(priceTypeCode);
        sb.append(", priceCode=").append(priceCode);
        sb.append(", ladderName=").append(ladderName);
        sb.append(", ladderStart=").append(ladderStart);
        sb.append(", ladderEnd=").append(ladderEnd);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", treatmentFee=").append(treatmentFee);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", remark=").append(remark);
        sb.append(", partitionWaterId=").append(partitionWaterId);
        sb.append(", accountItemId=").append(accountItemId);
        sb.append("]");
        return sb.toString();
    }
}