package com.learnbind.ai.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_WATER_PRICE")
public class SysWaterPrice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT WATER_PRICE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PRICE_TYPE_NAME")
    private String priceTypeName;

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

    @Column(name = "PRICE_TYPE_CODE")
    private String priceTypeCode;
    
    @Column(name = "PRICE_CODE")
    private String priceCode;
    
    @Column(name = "IS_LADDER_PRICE")
    private Integer isLadderPrice;
    
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
    
    

    public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}
	
	


	public Integer getIsLadderPrice() {
		return isLadderPrice;
	}

	public void setIsLadderPrice(Integer isLadderPrice) {
		this.isLadderPrice = isLadderPrice;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "SysWaterPrice [id=" + id + ", priceTypeName=" + priceTypeName + ", ladderName=" + ladderName
				+ ", ladderStart=" + ladderStart + ", ladderEnd=" + ladderEnd + ", basePrice=" + basePrice
				+ ", treatmentFee=" + treatmentFee + ", totalPrice=" + totalPrice + ", remark=" + remark
				+ ", priceTypeCode=" + priceTypeCode + ", priceCode=" + priceCode + ", isLadderPrice=" + isLadderPrice
				+ ", deleted=" + deleted + "]";
	}

	
}