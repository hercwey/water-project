package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_CHECK_METER")
public class SysCheckMeter {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "CALIBER")
    private String caliber;

    @Column(name = "DETECTION_PERIOD")
    private Integer detectionPeriod;

    @Column(name = "REMARK")
    private String remark;

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

    
    
    public Integer getDetectionPeriod() {
		return detectionPeriod;
	}

	public void setDetectionPeriod(Integer detectionPeriod) {
		this.detectionPeriod = detectionPeriod;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", caliber=").append(caliber);
        sb.append(", detectionPeriod=").append(detectionPeriod);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}