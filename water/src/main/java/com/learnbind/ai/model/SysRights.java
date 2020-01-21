package com.learnbind.ai.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_RIGHTS")
public class SysRights {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT RIGHTS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "RIGHT_CODE")
    private String rightCode;

    @Column(name = "RIGHT_NAME")
    private String rightName;

    @Column(name = "RIGHT_LINK")
    private String rightLink;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "PID")
    private Long pid;

    @Column(name = "RIGHT_LEVEL")
    private Integer rightLevel;

    @Column(name = "RIGHT_ICON")
    private String rightIcon;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "CREATE_TIME")
    private Date createTime;

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
     * @return RIGHT_CODE
     */
    public String getRightCode() {
        return rightCode;
    }

    /**
     * @param rightCode
     */
    public void setRightCode(String rightCode) {
        this.rightCode = rightCode == null ? null : rightCode.trim();
    }

    /**
     * @return RIGHT_NAME
     */
    public String getRightName() {
        return rightName;
    }

    /**
     * @param rightName
     */
    public void setRightName(String rightName) {
        this.rightName = rightName == null ? null : rightName.trim();
    }

    /**
     * @return RIGHT_LINK
     */
    public String getRightLink() {
        return rightLink;
    }

    /**
     * @param rightLink
     */
    public void setRightLink(String rightLink) {
        this.rightLink = rightLink == null ? null : rightLink.trim();
    }

    /**
     * @return SORT
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
     * @return RIGHT_LEVEL
     */
    public Integer getRightLevel() {
        return rightLevel;
    }

    /**
     * @param rightLevel
     */
    public void setRightLevel(Integer rightLevel) {
        this.rightLevel = rightLevel;
    }

    /**
     * @return RIGHT_ICON
     */
    public String getRightIcon() {
        return rightIcon;
    }

    /**
     * @param rightIcon
     */
    public void setRightIcon(String rightIcon) {
        this.rightIcon = rightIcon == null ? null : rightIcon.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", rightCode=").append(rightCode);
        sb.append(", rightName=").append(rightName);
        sb.append(", rightLink=").append(rightLink);
        sb.append(", sort=").append(sort);
        sb.append(", pid=").append(pid);
        sb.append(", rightLevel=").append(rightLevel);
        sb.append(", rightIcon=").append(rightIcon);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}