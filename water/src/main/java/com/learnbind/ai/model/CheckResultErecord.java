package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "CHECK_RESULT_ERECORD")
public class CheckResultErecord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CHECK_RESULT_E_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CHECK_RESULT_ID")
    private Long checkResultId;

    @Column(name = "ERECORD_TYPE")
    private Integer erecordType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "FILE_TYPE")
    private Integer fileType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "SORT_VALUE")
    private String sortValue;

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
     * @return CHECK_RESULT_ID
     */
    public Long getCheckResultId() {
        return checkResultId;
    }

    /**
     * @param checkResultId
     */
    public void setCheckResultId(Long checkResultId) {
        this.checkResultId = checkResultId;
    }

    /**
     * @return ERECORD_TYPE
     */
    public Integer getErecordType() {
        return erecordType;
    }

    /**
     * @param erecordType
     */
    public void setErecordType(Integer erecordType) {
        this.erecordType = erecordType;
    }

    /**
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * @return FILE_PATH
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    /**
     * @return FILE_TYPE
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
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
     * @return SORT_VALUE
     */
    public String getSortValue() {
        return sortValue;
    }

    /**
     * @param sortValue
     */
    public void setSortValue(String sortValue) {
        this.sortValue = sortValue == null ? null : sortValue.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", checkResultId=").append(checkResultId);
        sb.append(", erecordType=").append(erecordType);
        sb.append(", description=").append(description);
        sb.append(", filePath=").append(filePath);
        sb.append(", fileType=").append(fileType);
        sb.append(", remark=").append(remark);
        sb.append(", sortValue=").append(sortValue);
        sb.append("]");
        return sb.toString();
    }
}