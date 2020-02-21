package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "BUSINESS_OFFICE")
public class BusinessOffice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT BUS_OFFICE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "OFFICE_CODE")
    private String officeCode;

    @Column(name = "OFFICE_NAME")
    private String officeName;

    @Column(name = "OFFICE_STATUS")
    private Integer officeStatus;

    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @Column(name = "PID")
    private Long pid;

    @Column(name = "NODE_TYPE")
    private Integer nodeType;

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
     * @return OFFICE_CODE
     */
    public String getOfficeCode() {
        return officeCode;
    }

    /**
     * @param officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode == null ? null : officeCode.trim();
    }

    /**
     * @return OFFICE_NAME
     */
    public String getOfficeName() {
        return officeName;
    }

    /**
     * @param officeName
     */
    public void setOfficeName(String officeName) {
        this.officeName = officeName == null ? null : officeName.trim();
    }

    /**
     * @return OFFICE_STATUS
     */
    public Integer getOfficeStatus() {
        return officeStatus;
    }

    /**
     * @param officeStatus
     */
    public void setOfficeStatus(Integer officeStatus) {
        this.officeStatus = officeStatus;
    }

    /**
     * @return CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    /**
     * @Title: getOperationTimeStr
     * @Description: 获取创建时间字符串
     * @return 
     */
    public String getCreateDateStr() {
    	if (createDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(createDate);
    	}
    	return null;
    	
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
     * @return NODE_TYPE
     */
    public Integer getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType
     */
    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", officeCode=").append(officeCode);
        sb.append(", officeName=").append(officeName);
        sb.append(", officeStatus=").append(officeStatus);
        sb.append(", createDate=").append(createDate);
        sb.append(", pid=").append(pid);
        sb.append(", nodeType=").append(nodeType);
        sb.append("]");
        return sb.toString();
    }
}