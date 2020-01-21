package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "INVOICE_ERECORD")
public class InvoiceErecord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT INVOICE_ERECORD_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "INVOICE_ID")
    private Long invoiceId;

    @Column(name = "ERECORD_TYPE")
    private Integer erecordType;

    @Column(name = "ERECORD_COMMENT")
    private String erecordComment;

    @Column(name = "ERECORD_PATH")
    private String erecordPath;

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
     * @return INVOICE_ID
     */
    public Long getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId
     */
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
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
     * @return ERECORD_COMMENT
     */
    public String getErecordComment() {
        return erecordComment;
    }

    /**
     * @param erecordComment
     */
    public void setErecordComment(String erecordComment) {
        this.erecordComment = erecordComment == null ? null : erecordComment.trim();
    }

    /**
     * @return ERECORD_PATH
     */
    public String getErecordPath() {
        return erecordPath;
    }

    /**
     * @param erecordPath
     */
    public void setErecordPath(String erecordPath) {
        this.erecordPath = erecordPath == null ? null : erecordPath.trim();
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
        sb.append(", invoiceId=").append(invoiceId);
        sb.append(", erecordType=").append(erecordType);
        sb.append(", erecordComment=").append(erecordComment);
        sb.append(", erecordPath=").append(erecordPath);
        sb.append(", fileType=").append(fileType);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}