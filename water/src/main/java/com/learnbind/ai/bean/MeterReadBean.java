package com.learnbind.ai.bean;

/**
 * 表读抄表实体
 */
public class MeterReadBean {
	
    /**
     * @Fields meterId：表计ID
     */
    private Long meterId;//表计ID
    /**
     * @Fields customerId：客户ID
     */
    private Long customerId;//客户ID
    /**
     * @Fields meterNum：表计读数
     */
    private String meterNum;//表计读数
    /**
     * @Fields readDate：抄表日期
     */
    private String readDate;//抄表日期

    private Integer readState;//读表状态 //0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表

    
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
     * @Title: getMeterNum
     * @Description: 获取表计读数
     * @return 
     */
    public String getMeterNum() {
        return meterNum;
    }

    /**
     * @Title: setMeterNum
     * @Description: 设置表计读数
     * @param meterNum 
     */
    public void setMeterNum(String meterNum) {
        this.meterNum = meterNum;
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
	 * @Title: getReadState
	 * @Description: 获取读表状态//0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表
	 * @return 
	 */
	public Integer getReadState() {
		return readState;
	}

	/**
	 * @Title: setReadState
	 * @Description: 设置读表状态//0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表

	 * @param readState 
	 */
	public void setReadState(Integer readState) {
		this.readState = readState;
	}

	@Override
	public String toString() {
		return "MeterReadBean [meterId=" + meterId + ", customerId=" + customerId + ", meterNum=" + meterNum
				+ ", readDate=" + readDate + ", readState=" + readState + "]";
	}
    
}
