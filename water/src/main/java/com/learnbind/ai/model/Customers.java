package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumCustomerChargeMode;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.enumclass.EnumIdType;
import com.learnbind.ai.common.enumclass.EnumNotifyMode;
import com.learnbind.ai.common.enumclass.EnumPartitionWaterStatus;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;

public class Customers {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUSTOMERS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PROP_NAME")
    private String propName;

    @Column(name = "PROP_TEL")
    private String propTel;

    @Column(name = "PROP_MOBILE")
    private String propMobile;

    @Column(name = "PROP_EMAIL")
    private String propEmail;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "USED_NAME")
    private String usedName;

    @Column(name = "SETTLE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date settleTime;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ID_TYPE")
    private Integer idType;

    @Column(name = "ID_NO")
    private String idNo;

    @Column(name = "CUSTOMER_TYPE")
    private Integer customerType;

    @Column(name = "CUSTOMER_TEL")
    private String customerTel;

    @Column(name = "CUSTOMER_MOBILE")
    private String customerMobile;

    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @Column(name = "WATER_USE")
    private String waterUse;

    @Column(name = "INDUSTRY")
    private String industry;

    @Column(name = "USE_NUM")
    private Integer useNum;

    @Column(name = "WATER_STATUS")
    private Integer waterStatus;

    @Column(name = "NOTIFY_MODE")
    private Integer notifyMode;

    @Column(name = "DISCOUNT_TYPE")
    private Long discountType;
    
    @Column(name = "CUSTOMER_CODE")
    private String customerCode;
    
    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "DEDUCT_TYPE")
    private Integer deductType;
    
    @Column(name = "ROOM")
    private String room;
    
    @Column(name = "STATUS")
    private Integer status;
    
    /**
     * @Fields isPartWater：是否分水量，0=否；1=是；
     */
    @Column(name = "IS_PART_WATER")
    private Integer isPartWater;//是否分水量，0=否；1=是；
    
    /**
     * @Fields chargeMode：一表多户收费方式，0=合并收费；1=单独收费
     */
    @Column(name = "CHARGE_MODE")
    private Integer chargeMode;
    
    /**
     * @Fields priceCode：水价
     */
    @Column(name = "PRICE_CODE")
    private String priceCode;
    
    /**
     * @Fields remark：备注
     */
    @Column(name = "REMARK")
    private String remark;
    
    /**
     * @Fields prepaymentCustomer：是否预存客户：0=普通客户（默认值）；1=预存客户；
     */
    @Column(name = "PREPAYMENT_CUSTOMER")
    private Integer prepaymentCustomer;

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
     * @return PROP_NAME
     */
    public String getPropName() {
        return propName;
    }

    /**
     * @param propName
     */
    public void setPropName(String propName) {
        this.propName = propName == null ? null : propName.trim();
    }

    /**
     * @return PROP_TEL
     */
    public String getPropTel() {
        return propTel;
    }

    /**
     * @param propTel
     */
    public void setPropTel(String propTel) {
        this.propTel = propTel == null ? null : propTel.trim();
    }

    /**
     * @return PROP_MOBILE
     */
    public String getPropMobile() {
        return propMobile;
    }

    /**
     * @param propMobile
     */
    public void setPropMobile(String propMobile) {
        this.propMobile = propMobile;
    }

    /**
     * @return PROP_EMAIL
     */
    public String getPropEmail() {
        return propEmail;
    }

    /**
     * @param propEmail
     */
    public void setPropEmail(String propEmail) {
        this.propEmail = propEmail == null ? null : propEmail.trim();
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
     * @return USED_NAME
     */
    public String getUsedName() {
        return usedName;
    }

    /**
     * @param usedName
     */
    public void setUsedName(String usedName) {
        this.usedName = usedName == null ? null : usedName.trim();
    }

    /**
     * @return SETTLE_TIME
     */
    public Date getSettleTime() {
        return settleTime;
    }

    /**
     * @param settleTime
     */
    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }
    
    /**
     * @Title: getSettleTimeStr
     * @Description: 获取立户时间字符串
     * @return 
     */
    public String getSettleTimeStr() {
    	if(settleTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(settleTime);
    	}
    	return null;
    }

    /**
     * @return ADDRESS
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * @return ID_TYPE
     */
    public Integer getIdType() {
        return idType;
    }

    /**
     * @param idType
     */
    public void setIdType(Integer idType) {
        this.idType = idType;
    }
    
    /**
     * @Title: getIdTypeStr
     * @Description: 证件类型字符串
     * @return 
     */
    public String getIdTypeStr() {
    	if(idType==null) {
    		return null;
    	}
    	return EnumIdType.getName(idType);
    }
    

    /**
     * @return ID_NO
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * @param idNo
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    /**
     * @return CUSTOMER_TYPE
     */
    public Integer getCustomerType() {
        return customerType;
    }

    /**
     * @param customerType
     */
    public void setCustomerType(Integer customerType) {
    	this.customerType = customerType;
    }
    
    public String getCustomerTypeStr() {
    	if(customerType!=null) {
    		return EnumCustomerType.getName(customerType);
    	}
    	return null;
    }

    /**
     * @return CUSTOMER_TEL
     */
    public String getCustomerTel() {
        return customerTel;
    }

    /**
     * @param customerTel
     */
    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel == null ? null : customerTel.trim();
    }

    /**
     * @return CUSTOMER_MOBILE
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile == null ? null : customerMobile.trim();
    }

    /**
     * @return CUSTOMER_EMAIL
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customerEmail
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail == null ? null : customerEmail.trim();
    }

    /**
     * @return WATER_USE
     */
    public String getWaterUse() {
        return waterUse;
    }

    /**
     * @param waterUse
     */
    public void setWaterUse(String waterUse) {
        this.waterUse = waterUse == null ? null : waterUse.trim();
    }

    /**
     * @return INDUSTRY
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * @param industry
     */
    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    /**
     * @return USE_NUM
     */
    public Integer getUseNum() {
        return useNum;
    }

    /**
     * @param useNum
     */
    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    /**
     * @return WATER_STATUS
     */
    public Integer getWaterStatus() {
        return waterStatus;
    }

    /**
     * @param waterStatus
     */
    public void setWaterStatus(Integer waterStatus) {
    	this.waterStatus = waterStatus;
    }
    
    /**
     * @Title: getWaterStatusStr
     * @Description: 获取客户用水状态字符串
     * @return 
     */
    public String getWaterStatusStr() {
    	if(waterStatus!=null) {
    		return EnumWaterStatus.getName(waterStatus);
    	}
    	return null;
    }

    /**
     * @return NOTIFY_MODE
     */
    public Integer getNotifyMode() {
        return notifyMode;
    }

    /**
     * @param notifyMode
     */
    public void setNotifyMode(Integer notifyMode) {
    	this.notifyMode = notifyMode;
    }
    
    public String getNotifyModeStr() {
    	if(notifyMode!=null) {
    		return EnumNotifyMode.getName(notifyMode);
    	}
    	return null;
    }

    /**
     * @return DISCOUNT_TYPE
     */
    public Long getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType
     */
    public void setDiscountType(Long discountType) {
        this.discountType = discountType;
    }
    
    /**
     * @return CUSTOMRE_CODE
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode == null ? null : customerCode.trim();
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
    
    
    

	public Integer getDeductType() {
		return deductType;
	}

	public void setDeductType(Integer deductType) {
		this.deductType = deductType;
	}
	
	/**
     * @Title: getDeductTypeStr
     * @Description: 扣费方式字符串
     * @return 
     */
    public String getDeductTypeStr() {
    	if(deductType!=null) {
    		return EnumDeductType.getName(deductType);
    	}
    	return null;
    }
    
	/**
	 * @Title: getRoom
	 * @Description: 获取门牌号
	 * @return 
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @Title: setRoom
	 * @Description: 设置门牌号
	 * @param room 
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @Title: getStatus
	 * @Description: 获取客户状态：-1=未立户；0=已立户；1=已销户；2=已过户
	 * @return 
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * @Title: getStatusStr
	 * @Description: 获取客户状态字符串：-1=未立户；0=已立户；1=已销户；2=已过户
	 * @return 
	 */
	public String getStatusStr() {
		return EnumCustomerStatus.getName(status);
	}

	/**
	 * @Title: setStatus
	 * @Description: 设置客户状态：-1=未立户；0=已立户；1=已销户；2=已过户
	 * @param status 
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @Title: getIsPartWater
	 * @Description: 获取是否分水量，0=否；1=是；
	 * @return 
	 */
	public Integer getIsPartWater() {
		return isPartWater;
	}

	/**
	 * @Title: setIsPartWater
	 * @Description: 设置是否分水量，0=否；1=是；
	 * @param isPartWater 
	 */
	public void setIsPartWater(Integer isPartWater) {
		this.isPartWater = isPartWater;
	}
	
	public String getIsPartWaterStr() {

		if(isPartWater==null) {
			return null;
		}
		return EnumPartitionWaterStatus.getName(isPartWater);

	}
	

	/**
	 * @Title: getChargeMode
	 * @Description: 获取收费方式；0=合并收费；1=单独收费
	 * @return 
	 */
	public Integer getChargeMode() {
		return chargeMode;
	}

	/**
	 * @Title: setChargeMode
	 * @Description: 设置收费方式；0=合并收费；1=单独收费
	 * @param chargeMode 
	 */
	public void setChargeMode(Integer chargeMode) {
		this.chargeMode = chargeMode;
	}
	
	 /**
     * @Title: getIdTypeStr
     * @Description: 证件类型字符串
     * @return 
     */
    public String getChargeModeStr() {
    	if(chargeMode!=null) {
    		return EnumCustomerChargeMode.getName(chargeMode);
    	}
    	return null;
    }

	/**
	 * @Title: getPriceCode
	 * @Description: 获取水价
	 * @return 
	 */
	public String getPriceCode() {
		return priceCode;
	}

	/**
	 * @Title: setPriceCode
	 * @Description: 设置水价
	 * @param priceCode 
	 */
	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}
	
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @Title: getPrepaymentCustomer
	 * @Description: 是否预存客户：0=普通客户（默认值）；1=预存客户；
	 * @return 
	 */
	public Integer getPrepaymentCustomer() {
		return prepaymentCustomer;
	}

	/**
	 * @Title: setPrepaymentCustomer
	 * @Description: 是否预存客户：0=普通客户（默认值）；1=预存客户；
	 * @param prepaymentCustomer 
	 */
	public void setPrepaymentCustomer(Integer prepaymentCustomer) {
		this.prepaymentCustomer = prepaymentCustomer;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", propName=").append(propName);
        sb.append(", propTel=").append(propTel);
        sb.append(", propMobile=").append(propMobile);
        sb.append(", propEmail=").append(propEmail);
        sb.append(", customerName=").append(customerName);
        sb.append(", usedName=").append(usedName);
        sb.append(", settleTime=").append(settleTime);
        sb.append(", address=").append(address);
        sb.append(", idType=").append(idType);
        sb.append(", idNo=").append(idNo);
        sb.append(", customerType=").append(customerType);
        sb.append(", customerTel=").append(customerTel);
        sb.append(", customerMobile=").append(customerMobile);
        sb.append(", customerEmail=").append(customerEmail);
        sb.append(", waterUse=").append(waterUse);
        sb.append(", industry=").append(industry);
        sb.append(", useNum=").append(useNum);
        sb.append(", waterStatus=").append(waterStatus);
        sb.append(", notifyMode=").append(notifyMode);
        sb.append(", discountType=").append(discountType);
        sb.append(", customerCode=").append(customerCode);
        sb.append(", deleted=").append(deleted);
        sb.append(", deductType=").append(deductType);
        sb.append(", room=").append(room);
        sb.append(", status=").append(status);
        sb.append(", isPartWater=").append(isPartWater);
        sb.append(", chargeMode=").append(chargeMode);
        sb.append(", priceCode=").append(priceCode);
        sb.append(", remark=").append(remark);
        sb.append(", prepaymentCustomer=").append(prepaymentCustomer);
        sb.append("]");
        return sb.toString();
    }
}