package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "USERS_BUS_OFFICE")
public class UsersBusOffice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT USER_OFFICE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "OFFICE_ID")
    private Long officeId;

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
     * @return USER_ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return OFFICE_ID
     */
    public Long getOfficeId() {
        return officeId;
    }

    /**
     * @param officeId
     */
    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", officeId=").append(officeId);
        sb.append("]");
        return sb.toString();
    }
}