package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "INSTALL_ENGINEERING")
public class InstallEngineering {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(generator = "JDBC")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT ENGINEERING_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "START_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "ADDRESS")
    private String address;

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
     * @return ORGANIZATION
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization
     */
    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    /**
     * @return START_DATE
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * @return START_DATE字符串
     */
    public String getStartDateStr() {
    	if(startDate != null) {
    		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(startDate);
    	}
    	return null;
    }

    /**
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return END_DATE
     */
    public Date getEndDate() {
        return endDate;
    }
    
    /**
     * @return END_DATE字符串
     */
    public String getEndDateStr() {
    	if(endDate != null) {
    		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(endDate);
    	}
    	return null;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
     * @return OPERATOR_ID
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return OPERATOR_NAME
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
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

    /**
     * @return ADDRESS
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", organization=").append(organization);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", remark=").append(remark);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", createTime=").append(createTime);
        sb.append(", address=").append(address);
        sb.append("]");
        return sb.toString();
    }
}