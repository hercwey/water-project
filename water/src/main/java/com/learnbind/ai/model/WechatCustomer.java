package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "WECHAT_CUSTOMER")
public class WechatCustomer {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "WECHAT_ID")
    private Long wechatId;

    @Column(name = "OPENID")
    private String openid;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "ID_CARD_NO")
    private String idCardNo;

    @Column(name = "CUSTOMER_CODE")
    private String customerCode;

    @Column(name = "BIND_DATE")
    private Date bindDate;

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
     * @return WECHAT_ID
     */
    public Long getWechatId() {
        return wechatId;
    }

    /**
     * @param wechatId
     */
    public void setWechatId(Long wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * @return OPENID
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
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
     * @return MOBILE
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * @return ID_CARD_NO
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * @param idCardNo
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    /**
     * @return CUSTOMER_CODE
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode == null ? null : customerCode.trim();
    }

    /**
     * @return BIND_DATE
     */
    public Date getBindDate() {
        return bindDate;
    }

    /**
     * @param bindDate
     */
    public void setBindDate(Date bindDate) {
        this.bindDate = bindDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wechatId=").append(wechatId);
        sb.append(", openid=").append(openid);
        sb.append(", customerId=").append(customerId);
        sb.append(", mobile=").append(mobile);
        sb.append(", idCardNo=").append(idCardNo);
        sb.append(", customerCode=").append(customerCode);
        sb.append(", bindDate=").append(bindDate);
        sb.append("]");
        return sb.toString();
    }
}