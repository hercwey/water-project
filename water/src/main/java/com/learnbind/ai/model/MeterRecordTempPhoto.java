package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "METER_RECORD_TEMP_PHOTO")
public class MeterRecordTempPhoto {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @Column(name = "IMAGE_FORMAT")
    private String imageFormat;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @Column(name = "OPERATION_TIME")
    private Date operationTime;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

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
     * @return IMAGE_PATH
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    /**
     * @return IMAGE_FORMAT
     */
    public String getImageFormat() {
        return imageFormat;
    }

    /**
     * @param imageFormat
     */
    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat == null ? null : imageFormat.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterId=").append(meterId);
        sb.append(", period=").append(period);
        sb.append(", imagePath=").append(imagePath);
        sb.append(", imageFormat=").append(imageFormat);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", customerId=").append(customerId);
        sb.append("]");
        return sb.toString();
    }
}