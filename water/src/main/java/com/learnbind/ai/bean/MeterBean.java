package com.learnbind.ai.bean;

import java.math.BigDecimal;

/**
 * 表计对象
 * meterBookMeter---use--->Meter
 */
public class MeterBean {
	
    /**
     * @Fields addr：表计地址
     */
    private String addr;//表计地址
    /**
     * @Fields meterId：表计ID
     */
    private Long meterId;//表计ID
    /**
     * @Fields customer：客户名称
     */
    private String customer;//客户名称
    /**
     * @Fields customerId：客户ID
     */
    private Long customerId;//客户ID
    /**
     * @Fields location：表计地址(客户地址)  小区-楼号-单元-户号
     */
    private String location;//表计地址(客户地址)  小区-楼号-单元-户号
    /**
     * @Fields lastNum：最后一次读数
     */
    private String lastNum;//最后一次读数

    /**
     * @Fields currNum：当前读数
     */
    private String currNum;//当前读数
    /**
     * @Fields readDate：抄表日期
     */
    private String readDate;//抄表日期
    
    /**
     * @Fields collectorAddr：采集器地址（只有海大艺高的表计需要此属性）
     */
    private String collectorAddr;//采集器地址（只有海大艺高的表计需要此属性）

    /**
     * @Fields meterNo：表计编号
     */
    private String meterNo;
    
    /**
     * @Fields description：表计描述
     */
    private String description;
    
    /**
     * @Fields preAmount：最后一次读数的水量
     */
    private BigDecimal preAmount;
    
    //getter and setter

    /**
     * @Title: getAddr
     * @Description: 获取表计地址
     * @return 
     */
    public String getAddr() {
        return addr;
    }

    /**
     * @Title: setAddr
     * @Description: 设置表计地址
     * @param addr 
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * @Title: getMeterId
     * @Description: 获取表计ID
     * @return 
     */
    public Long getMeterId() {
        return meterId;
    }

    /**
     * @Title: setMeterId
     * @Description: 设置表计ID
     * @param meterId 
     */
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    /**
     * @Title: getCustomer
     * @Description: 获取客户名称
     * @return 
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * @Title: setCustomer
     * @Description: 设置客户名称
     * @param customer 
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * @Title: getCustomerId
     * @Description: 获取客户ID
     * @return 
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @Title: setCustomerId
     * @Description: 设置客户ID
     * @param customerId 
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @Title: getLocation
     * @Description: 获取表计地址(客户地址)  小区-楼号-单元-户号
     * @return 
     */
    public String getLocation() {
        return location;
    }

    /**
     * @Title: setLocation
     * @Description: 设置表计地址(客户地址)  小区-楼号-单元-户号
     * @param location 
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @Title: getLastNum
     * @Description: 获取最后一次读数
     * @return 
     */
    public String getLastNum() {
        return lastNum;
    }

    /**
     * @Title: setLastNum
     * @Description: 设置最后一次读数
     * @param lastNum 
     */
    public void setLastNum(String lastNum) {
        this.lastNum = lastNum;
    }

    /**
     * @Title: getCurrNum
     * @Description: 获取当前读数
     * @return 
     */
    public String getCurrNum() {
        return currNum;
    }

    /**
     * @Title: setCurrNum
     * @Description: 设置当前读数
     * @param currNum 
     */
    public void setCurrNum(String currNum) {
        this.currNum = currNum;
    }

    /**
     * @Title: getReadDate
     * @Description: 获取抄表日期
     * @return 
     */
    public String getReadDate() {
        return readDate;
    }

    /**
     * @Title: setReadDate
     * @Description: 设置抄表日期
     * @param readDate 
     */
    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

	/**
	 * @Title: getCollectorAddr
	 * @Description: 获取采集器地址（只有海大艺高的表计需要此属性）
	 * @return 
	 */
	public String getCollectorAddr() {
		return collectorAddr;
	}

	/**
	 * @Title: setCollectorAddr
	 * @Description: 设置采集器地址（只有海大艺高的表计需要此属性）
	 * @param collectorAddr 
	 */
	public void setCollectorAddr(String collectorAddr) {
		this.collectorAddr = collectorAddr;
	}

	/**
	 * @Title: getMeterNo
	 * @Description: 获取表计编号
	 * @return 
	 */
	public String getMeterNo() {
		return meterNo;
	}

	/**
	 * @Title: setMeterNo
	 * @Description: 设置表计编号
	 * @param meterNo 
	 */
	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	/**
	 * @Title: getDescription
	 * @Description: 获取表计描述
	 * @return 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @Title: setDescription
	 * @Description: 设置表计描述
	 * @param description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	
	/**
	 * @Title: getPreAmount
	 * @Description: 获取上期水量
	 * @return 
	 */
	public BigDecimal getPreAmount() {
		return preAmount;
	}

	/**
	 * @Title: setPreAmount
	 * @Description: 设置上期水量
	 * @param preAmount 
	 */
	public void setPreAmount(BigDecimal preAmount) {
		this.preAmount = preAmount;
	}

	@Override
	public String toString() {
		return "MeterBean [addr=" + addr + ", meterId=" + meterId + ", customer=" + customer + ", customerId="
				+ customerId + ", location=" + location + ", lastNum=" + lastNum + ", currNum=" + currNum
				+ ", readDate=" + readDate + ", collectorAddr=" + collectorAddr + ", meterNo=" + meterNo
				+ ", description=" + description + ", preAmount=" + preAmount + "]";
	}

}
