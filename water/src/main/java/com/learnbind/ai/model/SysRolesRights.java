package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_ROLES_RIGHTS")
public class SysRolesRights {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT ROLES_RIGHTS_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "RIGHT_ID")
    private Long rightId;

    @Column(name = "REMARK")
    private String remark;

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
     * @return ROLE_ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * @return RIGHT_ID
     */
    public Long getRightId() {
        return rightId;
    }

    /**
     * @param rightId
     */
    public void setRightId(Long rightId) {
        this.rightId = rightId;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleId=").append(roleId);
        sb.append(", rightId=").append(rightId);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}