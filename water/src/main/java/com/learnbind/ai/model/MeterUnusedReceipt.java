package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "METER_UNUSED_RECEIPT")
public class MeterUnusedReceipt {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_UN_RE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "INSTALL_PLACE")
    private String installPlace;

    @Column(name = "CONSTRUCTION_COMPANY")
    private String constructionCompany;

    @Column(name = "METER_NO")
    private String meterNo;

    @Column(name = "END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @Column(name = "CERTIFICATE_NO")
    private String certificateNo;

    @Column(name = "CALIBER")
    private String caliber;

    @Column(name = "RETURN_METER_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnMeterDate;

    @Column(name = "METER_BOTTOM")
    private String meterBottom;

    @Column(name = "YYC_PEOPLE")
    private String yycPeople;

    @Column(name = "WZC_PEOPLE")
    private String wzcPeople;

    @Column(name = "SCRAP_REASON")
    private String scrapReason;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "RETURN_PEOPLE")
    private String returnPeople;
    
    @Column(name = "WZC_RECEIVE_PEOPLE")
    private String wzcReceivePeople;

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
     * @return CUSTOMER_NAME
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    /**
     * @return INSTALL_PLACE
     */
    public String getInstallPlace() {
        return installPlace;
    }

    /**
     * @param installPlace
     */
    public void setInstallPlace(String installPlace) {
        this.installPlace = installPlace == null ? null : installPlace.trim();
    }

    /**
     * @return CONSTRUCTION_COMPANY
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
     * @return END_DATE
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * @Title: getEndDateStr
     * @Description: 截止日期
     * @return 
     */
    public String getEndDateStr() {
    	if(endDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(endDate);
    	}
    	return null;
    }

    /**
     * @return CERTIFICATE_NO
     */
    public String getCertificateNo() {
        return certificateNo;
    }

    /**
     * @param certificateNo
     */
    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo == null ? null : certificateNo.trim();
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
     * @return RETURN_METER_DATE
     */
    public Date getReturnMeterDate() {
        return returnMeterDate;
    }

    /**
     * @param returnMeterDate
     */
    public void setReturnMeterDate(Date returnMeterDate) {
        this.returnMeterDate = returnMeterDate;
    }
    
    public String getReturnMeterDateStr() {
    	if(returnMeterDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(returnMeterDate);
    	}
    	return null;
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
     * @return SCRAP_REASON
     */
    public String getScrapReason() {
        return scrapReason;
    }

    /**
     * @param scrapReason
     */
    public void setScrapReason(String scrapReason) {
        this.scrapReason = scrapReason == null ? null : scrapReason.trim();
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
    
    

    public String getReturnPeople() {
		return returnPeople;
	}

	public void setReturnPeople(String returnPeople) {
		this.returnPeople = returnPeople;
	}

	public String getWzcReceivePeople() {
		return wzcReceivePeople;
	}

	public void setWzcReceivePeople(String wzcReceivePeople) {
		this.wzcReceivePeople = wzcReceivePeople;
	}

	@Override
	public String toString() {
		return "MeterUnusedReceipt [id=" + id + ", meterId=" + meterId + ", customerName=" + customerName
				+ ", installPlace=" + installPlace + ", constructionCompany=" + constructionCompany + ", meterNo="
				+ meterNo + ", endDate=" + endDate + ", certificateNo=" + certificateNo + ", caliber=" + caliber
				+ ", returnMeterDate=" + returnMeterDate + ", meterBottom=" + meterBottom + ", yycPeople=" + yycPeople
				+ ", wzcPeople=" + wzcPeople + ", scrapReason=" + scrapReason + ", remark=" + remark + ", returnPeople="
				+ returnPeople + ", wzcReceivePeople=" + wzcReceivePeople + "]";
	}

	
}