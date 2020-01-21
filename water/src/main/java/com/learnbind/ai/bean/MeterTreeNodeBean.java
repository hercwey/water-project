package com.learnbind.ai.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: MeterTreeBean.java
 * @Description: 表计关系树结点Bean
 *
 * @author lenovo
 * @date 2019年10月6日 下午11:55:25
 * @version V1.0 
 *
 */
public class MeterTreeNodeBean{

	@JSONField(name="isParent")
	private String isParent;//是否是父节点
	
    private Long id;

    private String name;

    private String code;

    private String nodeType;
    
    private String icon;  //图标

    public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	private Long pid;

    private Integer nodeLevel;

    private Long sortValue;

    private String remark;

    private Integer deleted;
    
    private String traceIds;
    
    private Long meterId;
    
    
    
    public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

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
     * @return nodeType
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType == null ? null : nodeType.trim();
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
     * @return nodeLevel
     */
    public Integer getNodeLevel() {
        return nodeLevel;
    }

    /**
     * @param nodeLevel
     */
    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
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
		return "MeterTreeBean [isParent=" + isParent + ", id=" + id + ", name=" + name + ", code=" + code
				+ ", nodeType=" + nodeType + ", pid=" + pid + ", nodeLevel=" + nodeLevel + ", sortValue=" + sortValue
				+ ", remark=" + remark + ", deleted=" + deleted + ", traceIds=" + traceIds + "]";
	}
	
}
