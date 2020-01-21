package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStockStatus;

@Table(name = "METER_STOCK_TRACE")
public class MeterStockTrace {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_STOCK_T_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "METER_NO")
    private String meterNo;

    @Column(name = "CALIBER")
    private String caliber;

    @Column(name = "FACTORY")
    private String factory;

    @Column(name = "OPERATION_TYPE")
    private Integer operationType;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATOR_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatorTime;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "OPERATOR_NAME")
    private String operatorName;

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
     * @return OPERATION_TYPE
     */
    public Integer getOperationType() {
        return operationType;
    }

    /**
     * @param operationType
     */
    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
    
    public String getOperationTypeStr() {
    	if(operationType!=null) {
    		return EnumMeterStockStatus.getName(operationType);
    	}
    	return null;
    }

    /**
     * @return OPERATOR_ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return OPERATOR_TIME
     */
    public Date getOperatorTime() {
        return operatorTime;
    }

    /**
     * @param operatorTime
     */
    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }
    
    public String getOperatorTimeStr() {
    	if(operatorTime!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operatorTime);
    	}
    	return null;
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
    
    
    public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterId=").append(meterId);
        sb.append(", meterNo=").append(meterNo);
        sb.append(", caliber=").append(caliber);
        sb.append(", factory=").append(factory);
        sb.append(", operationType=").append(operationType);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorTime=").append(operatorTime);
        sb.append(", remark=").append(remark);
        sb.append(", operatorName=").append(operatorName);
        sb.append("]");
        return sb.toString();
    }
}