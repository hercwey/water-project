package com.learnbind.ai.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: LocationBean.java
 * @Description: 地理位置Bean
 *
 * @author Administrator
 * @date 2019年7月29日 下午4:06:49
 * @version V1.0 
 *
 */
public class LocationBean   {

	@JSONField(name="isParent")
	private String isParent;//是否是父节点

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	
    private Long id;

    private String name;

    private String code;

    private String localNodeType;

    private Long pid;

    private Integer locationLevel;

    private Long sortValue;

    private String remark;

    private Integer deleted;
    
    private String longCode;
    
    private String traceIds;
    
    private String pycode;
    
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
     * @param sortValue
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
		return "LocationBean [isParent=" + isParent + ", id=" + id + ", name=" + name + ", code=" + code
				+ ", localNodeType=" + localNodeType + ", pid=" + pid + ", locationLevel=" + locationLevel
				+ ", sortValue=" + sortValue + ", remark=" + remark + ", deleted=" + deleted + ", longCode=" + longCode
				+ ", traceIds=" + traceIds + ", pycode=" + pycode + ", pylongcode=" + pylongcode + "]";
	}

	
}
