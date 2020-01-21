package com.learnbind.ai.model;

import java.math.BigDecimal;
import javax.persistence.*;

import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;

@Table(name = "CMBC_BATCH_WITHHOLD_RECORD")
public class CmbcBatchWithholdRecord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CMBC_WITH_RECORD_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_SN")
    private String fileSn;

    @Column(name = "WITHHOLD_FILE")
    private String withholdFile;

    @Column(name = "SUMMARY_FILE")
    private String summaryFile;

    @Column(name = "CMBC_VCHR_ID")
    private String cmbcVchrId;

    @Column(name = "CMBC_VCHR_FILE")
    private String cmbcVchrFile;

    @Column(name = "CMBC_SUMMARY_FIEL")
    private String cmbcSummaryFiel;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "SUCCESS_TOTAL_AMOUNT")
    private BigDecimal successTotalAmount;

    @Column(name = "SUCCESS_TOTAL_NUM")
    private Integer successTotalNum;

    @Column(name = "FAIL_TOTAL_AMOUNT")
    private BigDecimal failTotalAmount;

    @Column(name = "FAIL_TOTAL_NUM")
    private BigDecimal failTotalNum;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "TRACE_ID")
    private String traceId;

    @Column(name = "PIN_YIN_CODE")
    private String pinYinCode;

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
     * @return FILE_TYPE
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     */
    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    /**
     * @return FILE_SN
     */
    public String getFileSn() {
        return fileSn;
    }

    /**
     * @param fileSn
     */
    public void setFileSn(String fileSn) {
        this.fileSn = fileSn == null ? null : fileSn.trim();
    }

    /**
     * @return WITHHOLD_FILE
     */
    public String getWithholdFile() {
        return withholdFile;
    }

    /**
     * @param withholdFile
     */
    public void setWithholdFile(String withholdFile) {
        this.withholdFile = withholdFile == null ? null : withholdFile.trim();
    }

    /**
     * @return SUMMARY_FILE
     */
    public String getSummaryFile() {
        return summaryFile;
    }

    /**
     * @param summaryFile
     */
    public void setSummaryFile(String summaryFile) {
        this.summaryFile = summaryFile == null ? null : summaryFile.trim();
    }

    /**
     * @return CMBC_VCHR_ID
     */
    public String getCmbcVchrId() {
        return cmbcVchrId;
    }

    /**
     * @param cmbcVchrId
     */
    public void setCmbcVchrId(String cmbcVchrId) {
        this.cmbcVchrId = cmbcVchrId == null ? null : cmbcVchrId.trim();
    }

    /**
     * @return CMBC_VCHR_FILE
     */
    public String getCmbcVchrFile() {
        return cmbcVchrFile;
    }

    /**
     * @param cmbcVchrFile
     */
    public void setCmbcVchrFile(String cmbcVchrFile) {
        this.cmbcVchrFile = cmbcVchrFile == null ? null : cmbcVchrFile.trim();
    }

    /**
     * @return CMBC_SUMMARY_FIEL
     */
    public String getCmbcSummaryFiel() {
        return cmbcSummaryFiel;
    }

    /**
     * @param cmbcSummaryFiel
     */
    public void setCmbcSummaryFiel(String cmbcSummaryFiel) {
        this.cmbcSummaryFiel = cmbcSummaryFiel == null ? null : cmbcSummaryFiel.trim();
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
     * @Title: getStatusStr
     * @Description: CCB文件状态
     * @return 
     */
    public String getStatusStr() {
    	return EnumCcbBatchStatus.getName(status);
    }

    /**
     * @return SUCCESS_TOTAL_AMOUNT
     */
    public BigDecimal getSuccessTotalAmount() {
        return successTotalAmount;
    }

    /**
     * @param successTotalAmount
     */
    public void setSuccessTotalAmount(BigDecimal successTotalAmount) {
        this.successTotalAmount = successTotalAmount;
    }

    /**
     * @return SUCCESS_TOTAL_NUM
     */
    public Integer getSuccessTotalNum() {
        return successTotalNum;
    }

    /**
     * @param successTotalNum
     */
    public void setSuccessTotalNum(Integer successTotalNum) {
        this.successTotalNum = successTotalNum;
    }

    /**
     * @return FAIL_TOTAL_AMOUNT
     */
    public BigDecimal getFailTotalAmount() {
        return failTotalAmount;
    }

    /**
     * @param failTotalAmount
     */
    public void setFailTotalAmount(BigDecimal failTotalAmount) {
        this.failTotalAmount = failTotalAmount;
    }

    /**
     * @return FAIL_TOTAL_NUM
     */
    public BigDecimal getFailTotalNum() {
        return failTotalNum;
    }

    /**
     * @param failTotalNum
     */
    public void setFailTotalNum(BigDecimal failTotalNum) {
        this.failTotalNum = failTotalNum;
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
     * @return TRACE_ID
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * @param traceId
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId == null ? null : traceId.trim();
    }

    /**
     * @return PIN_YIN_CODE
     */
    public String getPinYinCode() {
        return pinYinCode;
    }

    /**
     * @param pinYinCode
     */
    public void setPinYinCode(String pinYinCode) {
        this.pinYinCode = pinYinCode == null ? null : pinYinCode.trim();
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
        sb.append(", fileType=").append(fileType);
        sb.append(", fileSn=").append(fileSn);
        sb.append(", withholdFile=").append(withholdFile);
        sb.append(", summaryFile=").append(summaryFile);
        sb.append(", cmbcVchrId=").append(cmbcVchrId);
        sb.append(", cmbcVchrFile=").append(cmbcVchrFile);
        sb.append(", cmbcSummaryFiel=").append(cmbcSummaryFiel);
        sb.append(", status=").append(status);
        sb.append(", successTotalAmount=").append(successTotalAmount);
        sb.append(", successTotalNum=").append(successTotalNum);
        sb.append(", failTotalAmount=").append(failTotalAmount);
        sb.append(", failTotalNum=").append(failTotalNum);
        sb.append(", period=").append(period);
        sb.append(", traceId=").append(traceId);
        sb.append(", pinYinCode=").append(pinYinCode);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}