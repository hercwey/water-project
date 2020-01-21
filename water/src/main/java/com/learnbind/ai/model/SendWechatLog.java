package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "SEND_WECHAT_LOG")
public class SendWechatLog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT SEND_WC_L_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "OPEN_ID")
    private String openId;

    @Column(name = "TPL_ID")
    private Long tplId;

    @Column(name = "WECHAT_CONTENT")
    private String wechatContent;

    @Column(name = "RESULT")
    private Integer result;

    @Column(name = "ERRMSG")
    private String errmsg;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATION_TIME")
    private Date operationTime;

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
     * @return OPEN_ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
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
     * @return WECHAT_CONTENT
     */
    public String getWechatContent() {
        return wechatContent;
    }

    /**
     * @param wechatContent
     */
    public void setWechatContent(String wechatContent) {
        this.wechatContent = wechatContent == null ? null : wechatContent.trim();
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
     * @return PERIOD
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", openId=").append(openId);
        sb.append(", tplId=").append(tplId);
        sb.append(", wechatContent=").append(wechatContent);
        sb.append(", result=").append(result);
        sb.append(", errmsg=").append(errmsg);
        sb.append(", period=").append(period);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}