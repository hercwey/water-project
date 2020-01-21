package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.model
 *
 * @Title: Location.java
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @date 2019年8月15日 下午12:59:16
 * @version V1.0 
 *
 */
public class Location {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT LOCATION_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "LOCAL_NODE_TYPE")
    private String localNodeType;

    @Column(name = "PID")
    private Long pid;

    @Column(name = "LOCATION_LEVEL")
    private Integer locationLevel;

    @Column(name = "SORT_VALUE")
    private Long sortValue;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "LONG_CODE")
    private String longCode;
    
    @Column(name = "TRACE_IDS")
    private String traceIds;

    @Column(name = "PYCODE")
    private String pycode;
    
    @Column(name = "PYLONGCODE")
    private String pylongcode;
    
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
     * @return LOCAL_NODE_TYPE
     */
    public String getLocalNodeType() {
        return localNodeType;
    }

    /**
     * @param localNodeType
     */
    public void setLocalNodeType(String localNodeType) {
        this.localNodeType = localNodeType == null ? null : localNodeType.trim();
    }

    /**
     * @return PID
     */
    public Long getPid() {
        return pid;
    }

    /**
     * @param pid
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * @return LOCATION_LEVEL
     */
    public Integer getLocationLevel() {
        return locationLevel;
    }

    /**
     * @param locationLevel
     */
    public void setLocationLevel(Integer locationLevel) {
        this.locationLevel = locationLevel;
    }

    /**
     * @return SORT_VALUE
     */
    public Long getSortValue() {
        return sortValue;
    }

    /**
     * @param sortNo
     */
    public void setSortValue(Long sortValue) {
        this.sortValue = sortValue;
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
    
    

    public String getLongCode() {
		return longCode;
	}

	public void setLongCode(String longCode) {
		this.longCode = longCode;
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

	public String getPycode() {
		return pycode;
	}

	public void setPycode(String pycode) {
		this.pycode = pycode;
	}

	public String getPylongcode() {
		return pylongcode;
	}

	public void setPylongcode(String pylongcode) {
		this.pylongcode = pylongcode;
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
        sb.append(", localNodeType=").append(localNodeType);
        sb.append(", pid=").append(pid);
        sb.append(", locationLevel=").append(locationLevel);
        sb.append(", sortValue=").append(sortValue);
        sb.append(", remark=").append(remark);
        sb.append(", deleted=").append(deleted);
        sb.append(", longCode=").append(longCode);
        sb.append(", traceIds=").append(traceIds);
        sb.append(", pycode=").append(pycode);
        sb.append(", pylongcode=").append(pylongcode);
        sb.append("]");
        return sb.toString();
    }
}