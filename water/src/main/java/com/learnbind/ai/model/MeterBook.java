package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumMeterBookGenerateStatus;

@Table(name = "METER_BOOK")
public class MeterBook {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT METER_BOOK_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name = "FACTORY")
    private String factory;
    
   
	@Column(name = "TYPE_CODE")
    private String typeCode;
    
    @Column(name = "TYPE_NAME")
    private String typeName;
    
    @Column(name = "READ_MODE")
    private String readMode;
    
    @Column(name = "TRACE_IDS")
    private String traceIds;
    
    @Column(name = "GENERATE_STATUS")
    private Integer generateStatus;
    
    @Column(name = "BOOK_USER_TYPE")
    private Integer bookUserType;

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
     * @return CODE
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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
     * @Description: 获取创建时间字符串
     * @return 
     */
    public String getCreateTimeStr() {
    	if(createTime!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(createTime);
    	}
    	return null;
    }

    /**
     * @return OPERATOR_NAME
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
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
     * @return MODIFIED_DATE
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param modifiedDate
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
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
    
    public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
    public String getReadMode() {
		return readMode;
	}

	public void setReadMode(String readMode) {
		this.readMode = readMode;
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
	
	

	public Integer getGenerateStatus() {
		return generateStatus;
	}

	public void setGenerateStatus(Integer generateStatus) {
		this.generateStatus = generateStatus;
	}
	
	 /**
     * @Title: getIdTypeStr
     * @Description: 证件类型字符串
     * @return 
     */
    public String getGenerateStatusStr() {
    	return EnumMeterBookGenerateStatus.getName(generateStatus);
    }

	public Integer getBookUserType() {
		return bookUserType;
	}

	public void setBookUserType(Integer bookUserType) {
		this.bookUserType = bookUserType;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", code=").append(code);
        sb.append(", createTime=").append(createTime);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", modifiedDate=").append(modifiedDate);
        sb.append(", factory=").append(factory);
        sb.append(", typeCode=").append(typeCode);
        sb.append(", typeName=").append(typeName);
        sb.append(", readMode=").append(readMode);
        sb.append(", traceIds=").append(traceIds);
        sb.append(", generateStatus=").append(generateStatus);
        sb.append(", bookUserType=").append(bookUserType);
        sb.append("]");
        return sb.toString();
    }
}