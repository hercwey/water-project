package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "METER_TREE")
public class MeterTree {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NODE_TYPE")
    private String nodeType;

    @Column(name = "PID")
    private Long pid;

    @Column(name = "NODE_LEVEL")
    private Integer nodeLevel;

    @Column(name = "SORT_VALUE")
    private Integer sortValue;

    @Column(name = "TRACE_IDS")
    private String traceIds;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "METER_ID")
    private Long meterId;

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
     * @return NODE_TYPE
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
     * @return NODE_LEVEL
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
    public Integer getSortValue() {
        return sortValue;
    }

    /**
     * @param sortValue
     */
    public void setSortValue(Integer sortValue) {
        this.sortValue = sortValue;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", code=").append(code);
        sb.append(", nodeType=").append(nodeType);
        sb.append(", pid=").append(pid);
        sb.append(", nodeLevel=").append(nodeLevel);
        sb.append(", sortValue=").append(sortValue);
        sb.append(", traceIds=").append(traceIds);
        sb.append(", deleted=").append(deleted);
        sb.append(", remark=").append(remark);
        sb.append(", meterId=").append(meterId);
        sb.append("]");
        return sb.toString();
    }
}