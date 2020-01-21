package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumEnabledStatus;

@Table(name = "CUSTOMER_PEOPLE_ADJUST")
public class CustomerPeopleAdjust {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUSTOMER_PEOPLE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ADD_NUM")
    private Integer addNum;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "START_TIME")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;
    
    @Column(name = "OPERATOR_ID")
    private Long operatorId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "ENABLED")
    private Integer enabled;

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
     * @return ADD_NUM
     */
    public Integer getAddNum() {
        return addNum;
    }

    /**
     * @param addNum
     */
    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }

    /**
     * @return START_TIME
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @Title: getStartTimeStr
     * @Description: 获取检测时间字符串
     * @return 
     */
    public String getStartTimeStr() {
    	if (startTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(startTime);
    	}
    	return null;
    	
    }

    /**
     * @return END_TIME
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    /**
     * @Title: getEndTimeStr
     * @Description: 获取检测时间字符串
     * @return 
     */
    public String getEndTimeStr() {
    	if (endTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(endTime);
    	}
    	return null;
    }
    
    

    public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	/**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @Title: getCreateTimeStr
     * @Description: 获取检测时间字符串
     * @return 
     */
    public String getCreateTimeStr() {
    	if (createTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(createTime);
    	}
    	return null;
    }

    /**
     * @return ENABLED
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    
    public String getEnabledStr() {
    	return EnumEnabledStatus.getName(enabled);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", addNum=").append(addNum);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", createTime=").append(createTime);
        sb.append(", enabled=").append(enabled);
        sb.append("]");
        return sb.toString();
    }
}