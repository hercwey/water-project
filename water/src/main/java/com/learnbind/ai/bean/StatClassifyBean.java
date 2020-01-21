package com.learnbind.ai.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class StatClassifyBean {
	
	@JSONField(name="isParent")
	private String isParent;//是否是父节点

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	
    private Long id;

    private String classifyName;

    private Integer classifyType;

    private Long locationId;

    private Integer deleted;

    private Long pid;

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
     * @return CLASSIFY_NAME
     */
    public String getClassifyName() {
        return classifyName;
    }

    /**
     * @param classifyName
     */
    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName == null ? null : classifyName.trim();
    }

    /**
     * @return CLASSIFY_TYPE
     */
    public Integer getClassifyType() {
        return classifyType;
    }

    /**
     * @param classifyType
     */
    public void setClassifyType(Integer classifyType) {
        this.classifyType = classifyType;
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
     * @return TRACE_IDS
     */
    public String getTraceIds() {
        return traceIds;
    }

    /**
     * @param traceIds
     */
    public void setTraceIds(String traceIds) {
        this.traceIds = traceIds == null ? null : traceIds.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", classifyName=").append(classifyName);
        sb.append(", classifyType=").append(classifyType);
        sb.append(", locationId=").append(locationId);
        sb.append(", deleted=").append(deleted);
        sb.append(", pid=").append(pid);
        sb.append(", traceIds=").append(traceIds);
        sb.append("]");
        return sb.toString();
    }
}