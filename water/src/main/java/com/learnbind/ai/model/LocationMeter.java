package com.learnbind.ai.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "LOCATION_METER")
public class LocationMeter {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT LOCA_METER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "LOCATION_ID")
    private Long locationId;

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
    
    @Column(name = "METER_STATUS")
    private Integer meterStatus;
    
    @Column(name = "TRACE_IDS")
    private String traceIds;

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
     * @return LOCATION_ID
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId
     */
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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
    
    


	public Integer getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(Integer meterStatus) {
		this.meterStatus = meterStatus;
	}
	
	/**
	 * @Title: getTraceIds
	 * @Description: 获取痕迹地理位置ID（用“-”号间隔）
	 * @return 
	 */
	public String getTraceIds() {
		return traceIds;
	}

	/**
	 * @Title: setTraceIds
	 * @Description: 设置痕迹地理位置ID（用“-”号间隔）
	 * @param traceIds 
	 */
	public void setTraceIds(String traceIds) {
		this.traceIds = traceIds;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", locationId=").append(locationId);
        sb.append(", meterId=").append(meterId);
        sb.append(", remark=").append(remark);
        sb.append(", bindTime=").append(bindTime);
        sb.append(", unbindTime=").append(unbindTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", meterStatus=").append(meterStatus);
        sb.append(", traceIds=").append(traceIds);
        sb.append("]");
        return sb.toString();
    }
}