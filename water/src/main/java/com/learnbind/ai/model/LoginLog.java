package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumLoginStatus;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;

@Table(name = "LOGIN")
public class LoginLog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT LOGIN_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "LOGIN_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private Date loginDate;

    @Column(name = "STATUS")
    private Integer status;

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
     * @return USERNAME
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
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
     * @return LOGIN_DATE
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * @param loginDate
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    
    /**
     * @Title: getLoginDateStr
     * @Description: 获取操作时间字符串
     * @return 
     */
    public String getLoginDateStr() {
    	if(loginDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        	return sdf.format(loginDate);
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
    
    /**
     * @Title: getWaterStatusStr
     * @Description: 获取客户用水状态字符串
     * @return 
     */
    public String getLoginStatusStr() {
    	if(status!=null) {
    		return EnumLoginStatus.getName(status);
    	}
    	return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", userId=").append(userId);
        sb.append(", loginDate=").append(loginDate);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}