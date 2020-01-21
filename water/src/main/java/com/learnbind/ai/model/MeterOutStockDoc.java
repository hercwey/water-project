package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "METER_OUT_STOCK_DOC")
public class MeterOutStockDoc {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_OUT_STOCK_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "KNOCK_DOWN_UNIT")
    private String knockDownUnit;

    @Column(name = "METER_NO")
    private String meterNo;

    @Column(name = "CONSTRUCTION_COMPANY")
    private String constructionCompany;

    @Column(name = "METER_BOTTOM")
    private String meterBottom;

    @Column(name = "OUT_STOCK_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outStockDate;

    @Column(name = "CALIBER")
    private String caliber;

    @Column(name = "YYC_PEOPLE")
    private String yycPeople;

    @Column(name = "WZC_PEOPLE")
    private String wzcPeople;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "METER_PLACE")
    private String meterPlace;
    
    @Column(name = "CONSTRUCTION_PEOPLE")
    private String constructionPeople;
    
    @Column(name = "STORAGE_RECEIVE_PEOPLE")
    private String storageReceivePeople;

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
     * @return KNOCK_DOWN_UNIT
     */
    public String getKnockDownUnit() {
        return knockDownUnit;
    }

    /**
     * @param knockDownUnit
     */
    public void setKnockDownUnit(String knockDownUnit) {
        this.knockDownUnit = knockDownUnit == null ? null : knockDownUnit.trim();
    }

    /**
     * @return METER_NO
     */
    public String getMeterNo() {
        return meterNo;
    }

    /**
     * @param meterNo
     */
    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo == null ? null : meterNo.trim();
    }

    /**
     * @return CONSTRUCTION__COMPANY
     */
    public String getConstructionCompany() {
        return constructionCompany;
    }

    /**
     * @param constructionCompany
     */
    public void setConstructionCompany(String constructionCompany) {
        this.constructionCompany = constructionCompany == null ? null : constructionCompany.trim();
    }

    /**
     * @return METER_BOTTOM
     */
    public String getMeterBottom() {
        return meterBottom;
    }

    /**
     * @param meterBottom
     */
    public void setMeterBottom(String meterBottom) {
        this.meterBottom = meterBottom == null ? null : meterBottom.trim();
    }

    /**
     * @return OUT_STOCK_DATE
     */
    public Date getOutStockDate() {
        return outStockDate;
    }

    /**
     * @param outStockDate
     */
    public void setOutStockDate(Date outStockDate) {
        this.outStockDate = outStockDate;
    }
    
    /**
     * @Title: getOutStockDateStr
     * @Description: 出库日期字符串
     * @return 
     */
    public String getOutStockDateStr() {
    	if(outStockDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	return sdf.format(outStockDate);
    	}
    	return null;
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
     * @return YYC_PEOPLE
     */
    public String getYycPeople() {
        return yycPeople;
    }

    /**
     * @param yycPeople
     */
    public void setYycPeople(String yycPeople) {
        this.yycPeople = yycPeople == null ? null : yycPeople.trim();
    }

    /**
     * @return WZC_PEOPLE
     */
    public String getWzcPeople() {
        return wzcPeople;
    }

    /**
     * @param wzcPeople
     */
    public void setWzcPeople(String wzcPeople) {
        this.wzcPeople = wzcPeople == null ? null : wzcPeople.trim();
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
    
    

    public String getMeterPlace() {
		return meterPlace;
	}

	public void setMeterPlace(String meterPlace) {
		this.meterPlace = meterPlace;
	}

	public String getConstructionPeople() {
		return constructionPeople;
	}

	public void setConstructionPeople(String constructionPeople) {
		this.constructionPeople = constructionPeople;
	}

	public String getStorageReceivePeople() {
		return storageReceivePeople;
	}

	public void setStorageReceivePeople(String storageReceivePeople) {
		this.storageReceivePeople = storageReceivePeople;
	}

	@Override
	public String toString() {
		return "MeterOutStockDoc [id=" + id + ", meterId=" + meterId + ", knockDownUnit=" + knockDownUnit + ", meterNo="
				+ meterNo + ", constructionCompany=" + constructionCompany + ", meterBottom=" + meterBottom
				+ ", outStockDate=" + outStockDate + ", caliber=" + caliber + ", yycPeople=" + yycPeople
				+ ", wzcPeople=" + wzcPeople + ", remark=" + remark + ", meterPlace=" + meterPlace
				+ ", constructionPeople=" + constructionPeople + ", storageReceivePeople=" + storageReceivePeople + "]";
	}



	
}