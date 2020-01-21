package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "CUSTOMER_METER")
public class CustomerMeter {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUST_METER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "BIND_TIME")
    private Date bindTime;

    @Column(name = "UNBIND_TIME")
    private Date unbindTime;

    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "DEFAULT_CUSTOMER")
    private Integer defaultCustomer;
    
    @Column(name = "METER_STATUS")
    private Integer meterStatus;
    
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
     * @return CUSTOMER_ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
     * @return BIND_TIME
     */
    public Date getBindTime() {
        return bindTime;
    }

    /**
     * @param bindTime
     */
    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }
    
    public String getBindTimeStr() {
    	if(bindTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(bindTime);
    	}
    	return null;
    }


    /**
     * @return UNBIND_TIME
     */
    public Date getUnbindTime() {
        return unbindTime;
    }

    /**
     * @param unbindTime
     */
    public void setUnbindTime(Date unbindTime) {
        this.unbindTime = unbindTime;
    }
    
    public String getUnBindTimeStr() {
    	if(unbindTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(unbindTime);
    	}
    	return null;
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

    public Integer getDefaultCustomer() {
		return defaultCustomer;
	}

	public void setDefaultCustomer(Integer defaultCustomer) {
		this.defaultCustomer = defaultCustomer;
	}
	
	

	public Integer getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(Integer meterStatus) {
		this.meterStatus = meterStatus;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", meterId=").append(meterId);
        sb.append(", remark=").append(remark);
        sb.append(", bindTime=").append(bindTime);
        sb.append(", unbindTime=").append(unbindTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", defaultCustomer=").append(defaultCustomer);
        sb.append(", meterStatus=").append(meterStatus);
        sb.append("]");
        return sb.toString();
    }
}