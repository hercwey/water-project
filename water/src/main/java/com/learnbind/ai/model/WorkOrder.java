package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.tools.ant.types.DataType;
import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.enumclass.EnumWorkOrderStatus;
import com.learnbind.ai.common.util.dict.DataDictType;

@Table(name = "WORK_ORDER")
public class WorkOrder {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT WORK_ORDER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "SERIAL_NUM")
    private Long serialNum;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "STATUS")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "START_TIME")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "OPERATOR")
    private String operator;
    
    @Column(name = "DEAL_SITUATION")
    private String dealSituation;

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
     * @return SERIAL_NUM
     */
    public Long getSerialNum() {
        return serialNum;
    }

    /**
     * @param serialNum
     */
    public void setSerialNum(Long serialNum) {
        this.serialNum = serialNum;
    }

    /**
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

   


    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeStr() {
		if(type!=null) {
    		return DataDictType.getValue(type);  	
    	}
    	return null;
	}

	/**
     * @return STATUS
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getStatusStr() {
    	if(status!=null) {
    		return EnumWorkOrderStatus.getName(status);
    	}
    	return null;
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
     * @Title: getCheckDateStr
     * @Description: 获取结束时间字符串
     * @return 
     */
    public String getStartTimeStr() {
    	if(startTime!=null) {
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
     * @Title: getCheckDateStr
     * @Description: 获取结束时间字符串
     * @return 
     */
    public String getEndTimeStr() {
    	if(endTime!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(endTime);
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
    
    

    public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getDealSituation() {
		return dealSituation;
	}

	public void setDealSituation(String dealSituation) {
		this.dealSituation = dealSituation;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serialNum=").append(serialNum);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", remark=").append(remark);
        sb.append(", deleted=").append(deleted);
        sb.append(", operator=").append(operator);
        sb.append(", dealSituation=").append(dealSituation);
        sb.append("]");
        return sb.toString();
    }
}