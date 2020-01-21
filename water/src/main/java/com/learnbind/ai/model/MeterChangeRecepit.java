package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "METER_CHANGE_RECEIPT")
public class MeterChangeRecepit {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_CH_RE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "NEW_METER_NO")
    private String newMeterNo;

    @Column(name = "OLD_METER_NO")
    private String oldMeterNo;

    @Column(name = "CERTIFICATE_NO")
    private String certificateNo;

    @Column(name = "NEW_METER_BOTTOM")
    private String newMeterBottom;

    @Column(name = "OLD_METER_BOTTOM")
    private String oldMeterBottom;

    @Column(name = "END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @Column(name = "NEW_CALIBER")
    private String newCaliber;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "INSTALL_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date installDate;

    @Column(name = "STOCK_ADMIN_SIGN")
    private String stockAdminSign;

    @Column(name = "INSTALL_PLACE")
    private String installPlace;

    @Column(name = "USER_SIGNATURE")
    private String userSignature;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "OLD_CALIBER")
    private String oldCaliber;
    
    @Column(name = "UNIT_NAME")
    private String unitName;
    
    @Column(name = "RECEIVE_METER_PEOPLE")
    private String receiveMeterPeople;

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
     * @return NEW_METER_NO
     */
    public String getNewMeterNo() {
        return newMeterNo;
    }

    /**
     * @param newMeterNo
     */
    public void setNewMeterNo(String newMeterNo) {
        this.newMeterNo = newMeterNo == null ? null : newMeterNo.trim();
    }

    /**
     * @return OLD_METER_NO
     */
    public String getOldMeterNo() {
        return oldMeterNo;
    }

    /**
     * @param oldMeterNo
     */
    public void setOldMeterNo(String oldMeterNo) {
        this.oldMeterNo = oldMeterNo == null ? null : oldMeterNo.trim();
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
     * @return OLD_METER_BOTTOM
     */
    public String getOldMeterBottom() {
        return oldMeterBottom;
    }

    /**
     * @param oldMeterBottom
     */
    public void setOldMeterBottom(String oldMeterBottom) {
        this.oldMeterBottom = oldMeterBottom == null ? null : oldMeterBottom.trim();
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
     * @return OPERATOR
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
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
     * @return STOCK_ADMIN_SIGN
     */
    public String getStockAdminSign() {
        return stockAdminSign;
    }

    /**
     * @param stockAdminSign
     */
    public void setStockAdminSign(String stockAdminSign) {
        this.stockAdminSign = stockAdminSign == null ? null : stockAdminSign.trim();
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
    
    

    public String getNewCaliber() {
		return newCaliber;
	}

	public void setNewCaliber(String newCaliber) {
		this.newCaliber = newCaliber;
	}

	public String getOldCaliber() {
		return oldCaliber;
	}

	public void setOldCaliber(String oldCaliber) {
		this.oldCaliber = oldCaliber;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getReceiveMeterPeople() {
		return receiveMeterPeople;
	}

	public void setReceiveMeterPeople(String receiveMeterPeople) {
		this.receiveMeterPeople = receiveMeterPeople;
	}
	
	

	@Override
	public String toString() {
		return "MeterChangeRecepit [id=" + id + ", meterId=" + meterId + ", newMeterNo=" + newMeterNo + ", oldMeterNo="
				+ oldMeterNo + ", certificateNo=" + certificateNo + ", newMeterBottom=" + newMeterBottom
				+ ", oldMeterBottom=" + oldMeterBottom + ", endDate=" + endDate + ", newCaliber=" + newCaliber
				+ ", operator=" + operator + ", installDate=" + installDate + ", stockAdminSign=" + stockAdminSign
				+ ", installPlace=" + installPlace + ", userSignature=" + userSignature + ", remark=" + remark
				+ ", oldCaliber=" + oldCaliber + ", unitName=" + unitName + ", receiveMeterPeople=" + receiveMeterPeople
				+ "]";
	}
}