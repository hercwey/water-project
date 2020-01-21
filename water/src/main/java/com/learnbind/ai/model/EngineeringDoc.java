package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "ENGINEERING_DOC")
public class EngineeringDoc {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "ENGINEERING_ID")
    private Long engineeringId;

    @Column(name = "DOC_TYPE")
    private Integer docType;

    @Column(name = "DOC_COMMENT")
    private String docComment;

    @Column(name = "DOC_PATH")
    private String docPath;

    @Column(name = "FILE_TYPE")
    private Integer fileType;

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
     * @return ENGINEERING_ID
     */
    public Long getEngineeringId() {
        return engineeringId;
    }

    /**
     * @param engineeringId
     */
    public void setEngineeringId(Long engineeringId) {
        this.engineeringId = engineeringId;
    }

    /**
     * @return DOC_TYPE
     */
    public Integer getDocType() {
        return docType;
    }

    /**
     * @param docType
     */
    public void setDocType(Integer docType) {
        this.docType = docType;
    }

    /**
     * @return DOC_COMMENT
     */
    public String getDocComment() {
        return docComment;
    }

    /**
     * @param docComment
     */
    public void setDocComment(String docComment) {
        this.docComment = docComment == null ? null : docComment.trim();
    }

    /**
     * @return DOC_PATH
     */
    public String getDocPath() {
        return docPath;
    }

    /**
     * @param docPath
     */
    public void setDocPath(String docPath) {
        this.docPath = docPath == null ? null : docPath.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", engineeringId=").append(engineeringId);
        sb.append(", docType=").append(docType);
        sb.append(", docComment=").append(docComment);
        sb.append(", docPath=").append(docPath);
        sb.append(", fileType=").append(fileType);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}