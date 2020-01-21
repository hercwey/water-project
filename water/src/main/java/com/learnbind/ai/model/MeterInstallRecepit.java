package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "METER_INSTALL_RECEIPT")
public class MeterInstallRecepit {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_INS_RE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "RECEIVE_COMPANY")
    private String receiveCompany;

    @Column(name = "ENTRUST_DEPART_PEOPLE")
    private String entrustDepartPeople;

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

    @Column(name = "INSTALL_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date installDate;

    @Column(name = "NEW_METER_BOTTOM")
    private String newMeterBottom;

    @Column(name = "YYC_PEOPLE")
    private String yycPeople;

    @Column(name = "OUT_STOCK_PEOPLE")
    private String outStockPeople;

    @Column(name = "USER_SIGNATURE")
    private String userSignature;

    @Column(name = "RECEIVE_METER_PEOPLE")
    private String receiveMeterPeople;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "CHECK_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDate;

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
     * @return RECEIVE_COMPANY
     */
    public String getReceiveCompany() {
        return receiveCompany;
    }

    /**
     * @param receiveCompany
     */
    public void setReceiveCompany(String receiveCompany) {
        this.receiveCompany = receiveCompany == null ? null : receiveCompany.trim();
    }

    /**
     * @return ENTRUST_DEPART_PEOPLE
     */
    public String getEntrustDepartPeople() {
        return entrustDepartPeople;
    }

    /**
     * @param entrustDepartPeople
     */
    public void setEntrustDepartPeople(String entrustDepartPeople) {
        this.entrustDepartPeople = entrustDepartPeople == null ? null : entrustDepartPeople.trim();
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
     * @Description: 获取截止日期字符串
     * @return 
     */
    public String getEndDateStr() {
    	if(endDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
     * @return INSTALL_DATE
     */
    public Date getInstallDate() {
        return installDate;
    }

    /**
     * @param installDate
     */
    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }
    
    /**
     * @Title: getInstallDateStr
     * @Description: 获取安装日期字符串
     * @return 
     */
    public String getInstallDateStr() {
    	if(installDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	return sdf.format(installDate);
    	}
    	return null;
    }

    /**
     * @return NEW_METER_BOTTOM
     */
    public String getNewMeterBottom() {
        return newMeterBottom;
    }

    /**
     * @param newMeterBottom
     */
    public void setNewMeterBottom(String newMeterBottom) {
        this.newMeterBottom = newMeterBottom == null ? null : newMeterBottom.trim();
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
     * @return OUT_STOCK_PEOPLE
     */
    public String getOutStockPeople() {
        return outStockPeople;
    }

    /**
     * @param outStockPeople
     */
    public void setOutStockPeople(String outStockPeople) {
        this.outStockPeople = outStockPeople == null ? null : outStockPeople.trim();
    }

    /**
     * @return USER_SIGNATURE
     */
    public String getUserSignature() {
        return userSignature;
    }

    /**
     * @param userSignature
     */
    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature == null ? null : userSignature.trim();
    }

    /**
     * @return RECEIVE_METER_PEOPLE
     */
    public String getReceiveMeterPeople() {
        return receiveMeterPeople;
    }

    /**
     * @param receiveMeterPeople
     */
    public void setReceiveMeterPeople(String receiveMeterPeople) {
        this.receiveMeterPeople = receiveMeterPeople == null ? null : receiveMeterPeople.trim();
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
    
    

    public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	public String getCheckDateStr() {
    	if(checkDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	return sdf.format(checkDate);
    	}
    	return null;
    }

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterId=").append(meterId);
        sb.append(", receiveCompany=").append(receiveCompany);
        sb.append(", entrustDepartPeople=").append(entrustDepartPeople);
        sb.append(", installPlace=").append(installPlace);
        sb.append(", constructionCompany=").append(constructionCompany);
        sb.append(", meterNo=").append(meterNo);
        sb.append(", endDate=").append(endDate);
        sb.append(", certificateNo=").append(certificateNo);
        sb.append(", caliber=").append(caliber);
        sb.append(", installDate=").append(installDate);
        sb.append(", newMeterBottom=").append(newMeterBottom);
        sb.append(", yycPeople=").append(yycPeople);
        sb.append(", outStockPeople=").append(outStockPeople);
        sb.append(", userSignature=").append(userSignature);
        sb.append(", receiveMeterPeople=").append(receiveMeterPeople);
        sb.append(", remark=").append(remark);
        sb.append(", checkDate=").append(checkDate);
        sb.append("]");
        return sb.toString();
    }
}