package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SEND_SMS_LOG")
public class SendSmsLog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT SEND_SMS_L_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CUSTOMER_MOBILE")
    private String customerMobile;

    @Column(name = "TPL_ID")
    private Long tplId;

    @Column(name = "SMS_CONTENT")
    private String smsContent;

    @Column(name = "RESULT")
    private Integer result;

    @Column(name = "ERRMSG")
    private String errmsg;

    @Column(name = "EXT")
    private String ext;

    @Column(name = "FEE")
    private Integer fee;

    @Column(name = "SID")
    private String sid;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "REMARK")
    private String remark;
    @Column(name = "PERIOD")
    private String period;

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
     * @return CUSTOMER_ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return CUSTOMER_MOBILE
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile == null ? null : customerMobile.trim();
    }

    /**
     * @return TPL_ID
     */
    public Long getTplId() {
        return tplId;
    }

    /**
     * @param tplId
     */
    public void setTplId(Long tplId) {
        this.tplId = tplId;
    }

    /**
     * @return SMS_CONTENT
     */
    public String getSmsContent() {
        return smsContent;
    }

    /**
     * @param smsContent
     */
    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent == null ? null : smsContent.trim();
    }

    /**
     * @return RESULT
     */
    public Integer getResult() {
        return result;
    }

    /**
     * @param result
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * @return ERRMSG
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * @param errmsg
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg == null ? null : errmsg.trim();
    }

    /**
     * @return EXT
     */
    public String getExt() {
        return ext;
    }

    /**
     * @param ext
     */
    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    /**
     * @return FEE
     */
    public Integer getFee() {
        return fee;
    }

    /**
     * @param fee
     */
    public void setFee(Integer fee) {
        this.fee = fee;
    }

    /**
     * @return SID
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid
     */
    public void setSid(String sid) {
        this.sid = sid == null ? null : sid.trim();
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
     * @return OPERATION_TIME
     */
    public Date getOperationTime() {
        return operationTime;
    }

    /**
     * @param operationTime
     */
    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
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
    
    

    public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", customerMobile=").append(customerMobile);
        sb.append(", tplId=").append(tplId);
        sb.append(", smsContent=").append(smsContent);
        sb.append(", result=").append(result);
        sb.append(", errmsg=").append(errmsg);
        sb.append(", ext=").append(ext);
        sb.append(", fee=").append(fee);
        sb.append(", sid=").append(sid);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append(", period=").append(period);
        sb.append("]");
        return sb.toString();
    }
}