
package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;

public class Meters {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METERS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CALIBER")
    private String caliber;

    @Column(name = "METER_NO")
    private String meterNo;

    @Column(name = "PLACE")
    private String place;

    @Column(name = "FACTORY")
    private String factory;

    @Column(name = "FACTORY_PHONE")
    private String factoryPhone;

    @Column(name = "CERTIFICATE_NO")
    private String certificateNo;
    
   
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CHECK_DATE")
    private Date checkDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;

    @Column(name = "PID")
    private Long pid;

    @Column(name = "STEEL_SEAL_NO")
    private String steelSealNo;

    @Column(name = "NEW_METER_BOTTOM")
    private String newMeterBottom;

    @Column(name = "CHANGE_METER_BOTTOM")
    private String changeMeterBottom;

    @Column(name = "METER_USE")
    private String meterUse;

    @Column(name = "METER_TYPE")
    private String meterType;

    @Column(name = "UPPER_LIMIT")
    private String upperLimit;

    @Column(name = "METER_STATUS")
    private Integer meterStatus;

    @Column(name = "METER_MODEL")
    private String meterModel;

    @Column(name = "READ_MODE")
    private String readMode;
    
    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "METER_ADDRESS")
    private String meterAddress;
    
    @Column(name = "VIRTUAL_REAL")
    private Integer virtualReal;
    
    @Column(name = "COLLECTOR_ADDR")
    private String collectorAddr;
    
    @Column(name = "CHANNEL_NO")
    private String channelNo;
    
    @Column(name = "STATUS")
    private Integer status;
    
    @Column(name = "CYCLE_STATUS")
    private Integer cycleStatus;
    
    @Column(name = "WATER_USE")
    private String waterUse;
    
    @Column(name = "PRICE_CODE")
    private String priceCode;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "SORT_VALUE")
    private Integer sortValue;
    
    @Column(name = "DEVICE_ID")
    private String deviceId;
    
    @Column(name = "PROTOCOL_TYPE")
    private String protocolType;
    
    @Column(name = "METER_NUMBER")
    private String meterNumber;
    
    @Column(name = "METER_CONFIG")
    private String meterConfig;
    
    @Column(name = "METER_FREEZE")
    private String meterFreeze;
    
    @Column(name = "METER_NAME")
    private String meterName;
    
    @Column(name = "SAMPLE_UNIT")
    private String sampleUnit;
    
    @Column(name = "METER_USE_TYPE")
    private String meterUseType;
    
    @Column(name = "METER_FACTORY_CODE")
    private String meterFactoryCode;
    
    @Column(name = "METER_SEQUENCE")
    private Integer meterSequence;
    
    @Column(name = "ACCOUNT_STATUS")
    private Integer accountStatus;

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
     * @return METER_NO
     */
    public String getMeterNo() {
        return meterNo;
    }

    /**
     * @param meterNo
     */
    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    /**
     * @return PLACE
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     */
    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    /**
     * @return FACTORY
     */
    public String getFactory() {
        return factory;
    }

    /**
     * @param factory
     */
    public void setFactory(String factory) {
        this.factory = factory == null ? null : factory.trim();
    }

    /**
     * @return FACTORY_PHONE
     */
    public String getFactoryPhone() {
        return factoryPhone;
    }

    /**
     * @param factoryPhone
     */
    public void setFactoryPhone(String factoryPhone) {
        this.factoryPhone = factoryPhone == null ? null : factoryPhone.trim();
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
        this.certificateNo = certificateNo;
    }

    /**
     * @return CHECK_DATE
     */
    public Date getCheckDate() {
        return checkDate;
    }

    /**
     * @param checkDate
     */
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    
    /**
     * @Title: getCheckDateStr
     * @Description: 获取检测时间字符串
     * @return 
     */
    public String getCheckDateStr() {
    	if(checkDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(checkDate);
    	}
    	return null;
    }

    /**
     * @return EFFECTIVE_DATE
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param effectiveDate
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    /**
     * @Title: getCheckDateStr
     * @Description: 获取有效时间字符串
     * @return 
     */
    public String getEffectiveDateStr() {
    	if(effectiveDate!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(effectiveDate);
    	}
    	return null;
    }

    /**
     * @return PID
     */
    public Long getPid() {
        return pid;
    }

    /**
     * @param pid
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * @return STEEL_SEAL_NO
     */
    public String getSteelSealNo() {
        return steelSealNo;
    }

    /**
     * @param steelSealNo
     */
    public void setSteelSealNo(String steelSealNo) {
        this.steelSealNo = steelSealNo == null ? null : steelSealNo.trim();
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
     * @return CHANGE_METER_BOTTOM
     */
    public String getChangeMeterBottom() {
        return changeMeterBottom;
    }

    /**
     * @param changeMeterBottom
     */
    public void setChangeMeterBottom(String changeMeterBottom) {
        this.changeMeterBottom = changeMeterBottom == null ? null : changeMeterBottom.trim();
    }

    /**
     * @return METER_USE
     */
    public String getMeterUse() {
        return meterUse;
    }

    /**
     * @param meterUse
     */
    public void setMeterUse(String meterUse) {
        this.meterUse = meterUse == null ? null : meterUse.trim();
    }

    /**
     * @return METER_TYPE
     */
    public String getMeterType() {
        return meterType;
    }

    /**
     * @param meterType
     */
    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }

    /**
     * @return UPPER_LIMIT
     */
    public String getUpperLimit() {
        return upperLimit;
    }

    /**
     * @param upperLimit
     */
    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit == null ? null : upperLimit.trim();
    }

    /**
     * @return METER_STATUS
     */
    public Integer getMeterStatus() {
        return meterStatus;
    }

    /**
     * @param meterStatus
     */
    public void setMeterStatus(Integer meterStatus) {
        this.meterStatus = meterStatus;
    }
    
    public String getMeterStatusStr() {
    	if(meterStatus!=null) {
    		return EnumMeterStatus.getName(meterStatus);
    	}
    	return null;
    }

    /**
     * @return METER_MODEL
     */
    public String getMeterModel() {
        return meterModel;
    }

    /**
     * @param meterModel
     */
    public void setMeterModel(String meterModel) {
        this.meterModel = meterModel == null ? null : meterModel.trim();
    }

    /**
     * @return READ_MODE
     */
    public String getReadMode() {
        return readMode;
    }

    /**
     * @param readMode
     */
    public void setReadMode(String readMode) {
        this.readMode = readMode == null ? null : readMode.trim();
    }
    
    /**
     * @return DELETED
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * @param deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
    
    
    
    public String getMeterAddress() {
		return meterAddress;
	}

	public void setMeterAddress(String meterAddress) {
		this.meterAddress = meterAddress;
	}

	public Integer getVirtualReal() {
		return virtualReal;
	}

	public void setVirtualReal(Integer virtualReal) {
		this.virtualReal = virtualReal;
	}
	
	/**
	 * @Title: getVirtualRealStr
	 * @Description: 获取虚实表
	 * @return 
	 */
	public String getVirtualRealStr() {
    	if(virtualReal!=null) {
    		return EnumMeterVirtualReal.getName(virtualReal);
    	}
    	return null;
    }

	public String getCollectorAddr() {
		return collectorAddr;
	}

	public void setCollectorAddr(String collectorAddr) {
		this.collectorAddr = collectorAddr;
	}
	
	

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	
	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
    
    /**
     * @Title: getStatusStr
     * @Description: 获取表计状态
     * @return 
     */
    public String getStatusStr() {
    	if(status!=null) {
    		return EnumMeterSettingStatus.getName(status);
    	}
    	return null;
    }

	/**
	 * @Title: getCycleStatus
	 * @Description: 获取水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
	 * @return 
	 */
	public Integer getCycleStatus() {
		return cycleStatus;
	}

	/**
	 * @Title: setCycleStatus
	 * @Description: 设置水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
	 * @param cycleStatus 
	 */
	public void setCycleStatus(Integer cycleStatus) {
		this.cycleStatus = cycleStatus;
	}
	
	/**
     * @Title: getCycleStatusStr
     * @Description: 获取表计生命周期状态
     * @return 
     */
    public String getCycleStatusStr() {
    	if(cycleStatus!=null) {
    		return EnumMeterCycleStatus.getName(cycleStatus);
    	}
    	return null;
    }
    
    

	public String getWaterUse() {
		return waterUse;
	}

	public void setWaterUse(String waterUse) {
		this.waterUse = waterUse;
	}

	/**
	 * @Title: getPriceCode
	 * @Description: 获取水价编码
	 * @return 
	 */
	public String getPriceCode() {
		return priceCode;
	}

	/**
	 * @Title: setPriceCode
	 * @Description: 设置水价编码
	 * @param priceCode 
	 */
	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	/**
	 * @Title: getDescription
	 * @Description: 描述
	 * @return 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @Title: setDescription
	 * @Description: 描述
	 * @param description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public Integer getSortValue() {
		return sortValue;
	}

	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getMeterNumber() {
		return meterNumber;
	}

	public void setMeterNumber(String meterNumber) {
		this.meterNumber = meterNumber;
	}

	public String getMeterConfig() {
		return meterConfig;
	}

	public void setMeterConfig(String meterConfig) {
		this.meterConfig = meterConfig;
	}

	public String getMeterFreeze() {
		return meterFreeze;
	}

	public void setMeterFreeze(String meterFreeze) {
		this.meterFreeze = meterFreeze;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	
	

	public String getSampleUnit() {
		return sampleUnit;
	}

	public void setSampleUnit(String sampleUnit) {
		this.sampleUnit = sampleUnit;
	}
	
	public String getMeterUseType() {
		return meterUseType;
	}

	public void setMeterUseType(String meterUseType) {
		this.meterUseType = meterUseType;
	}

	public String getMeterFactoryCode() {
		return meterFactoryCode;
	}

	public void setMeterFactoryCode(String meterFactoryCode) {
		this.meterFactoryCode = meterFactoryCode;
	}

	public Integer getMeterSequence() {
		return meterSequence;
	}

	public void setMeterSequence(Integer meterSequence) {
		this.meterSequence = meterSequence;
	}
	
	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Override
	public String toString() {
		return "Meters [id=" + id + ", caliber=" + caliber + ", meterNo=" + meterNo + ", place=" + place + ", factory="
				+ factory + ", factoryPhone=" + factoryPhone + ", certificateNo=" + certificateNo + ", checkDate="
				+ checkDate + ", effectiveDate=" + effectiveDate + ", pid=" + pid + ", steelSealNo=" + steelSealNo
				+ ", newMeterBottom=" + newMeterBottom + ", changeMeterBottom=" + changeMeterBottom + ", meterUse="
				+ meterUse + ", meterType=" + meterType + ", upperLimit=" + upperLimit + ", meterStatus=" + meterStatus
				+ ", meterModel=" + meterModel + ", readMode=" + readMode + ", deleted=" + deleted + ", remark="
				+ remark + ", meterAddress=" + meterAddress + ", virtualReal=" + virtualReal + ", collectorAddr="
				+ collectorAddr + ", channelNo=" + channelNo + ", status=" + status + ", cycleStatus=" + cycleStatus
				+ ", waterUse=" + waterUse + ", priceCode=" + priceCode + ", description=" + description
				+ ", sortValue=" + sortValue + ", deviceId=" + deviceId + ", protocolType=" + protocolType
				+ ", meterNumber=" + meterNumber + ", meterConfig=" + meterConfig + ", meterFreeze=" + meterFreeze
				+ ", meterName=" + meterName + ", sampleUnit=" + sampleUnit + ", meterUseType=" + meterUseType
				+ ", meterFactoryCode=" + meterFactoryCode + ", meterSequence=" + meterSequence + ", accountStatus="
				+ accountStatus + "]";
	}

}