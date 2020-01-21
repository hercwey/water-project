package com.learnbind.ai.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "TAX_INVOICE_DETAIL")
public class TaxInvoiceDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT TAX_DETAIL_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "INVOICE_ID")
    private Long invoiceId;

    @Column(name = "SPMC")
    private String spmc;

    @Column(name = "HSBZ")
    private String hsbz;

    @Column(name = "SLV")
    private BigDecimal slv;

    @Column(name = "JE")
    private BigDecimal je;

    @Column(name = "DJ")
    private BigDecimal dj;

    @Column(name = "JLDW")
    private String jldw;

    @Column(name = "GGXH")
    private String ggxh;

    @Column(name = "SE")
    private BigDecimal se;

    @Column(name = "SL")
    private BigDecimal sl;

    @Column(name = "BMBBH")
    private String bmbbh;

    @Column(name = "SSFLBM")
    private String ssflbm;

    @Column(name = "YHZC")
    private String yhzc;

    @Column(name = "YHZCNR")
    private String yhzcnr;

    @Column(name = "LSLBS")
    private String lslbs;

    @Column(name = "QYZBM")
    private String qyzbm;

    @Column(name = "KCE")
    private BigDecimal kce;

    @Column(name = "DELETED")
    private Integer deleted;

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
     * @return SPMC
     */
    public String getSpmc() {
        return spmc;
    }

    /**
     * @param spmc
     */
    public void setSpmc(String spmc) {
        this.spmc = spmc == null ? null : spmc.trim();
    }

    /**
     * @return HSBZ
     */
    public String getHsbz() {
        return hsbz;
    }

    /**
     * @param hsbz
     */
    public void setHsbz(String hsbz) {
        this.hsbz = hsbz == null ? null : hsbz.trim();
    }

    /**
     * @return SLV
     */
    public BigDecimal getSlv() {
        return slv;
    }

    /**
     * @param slv
     */
    public void setSlv(BigDecimal slv) {
        this.slv = slv;
    }

    /**
     * @return JE
     */
    public BigDecimal getJe() {
        return je;
    }

    /**
     * @param je
     */
    public void setJe(BigDecimal je) {
        this.je = je;
    }

    /**
     * @return DJ
     */
    public BigDecimal getDj() {
        return dj;
    }

    /**
     * @param dj
     */
    public void setDj(BigDecimal dj) {
        this.dj = dj;
    }

    /**
     * @return JLDW
     */
    public String getJldw() {
        return jldw;
    }

    /**
     * @param jldw
     */
    public void setJldw(String jldw) {
        this.jldw = jldw == null ? null : jldw.trim();
    }

    /**
     * @return GGXH
     */
    public String getGgxh() {
        return ggxh;
    }

    /**
     * @param ggxh
     */
    public void setGgxh(String ggxh) {
        this.ggxh = ggxh == null ? null : ggxh.trim();
    }

    /**
     * @return SE
     */
    public BigDecimal getSe() {
        return se;
    }

    /**
     * @param se
     */
    public void setSe(BigDecimal se) {
        this.se = se;
    }

    /**
     * @return SL
     */
    public BigDecimal getSl() {
        return sl;
    }

    /**
     * @param sl
     */
    public void setSl(BigDecimal sl) {
        this.sl = sl;
    }

    /**
     * @return BMBBH
     */
    public String getBmbbh() {
        return bmbbh;
    }

    /**
     * @param bmbbh
     */
    public void setBmbbh(String bmbbh) {
        this.bmbbh = bmbbh == null ? null : bmbbh.trim();
    }

    /**
     * @return SSFLBM
     */
    public String getSsflbm() {
        return ssflbm;
    }

    /**
     * @param ssflbm
     */
    public void setSsflbm(String ssflbm) {
        this.ssflbm = ssflbm == null ? null : ssflbm.trim();
    }

    /**
     * @return YHZC
     */
    public String getYhzc() {
        return yhzc;
    }

    /**
     * @param yhzc
     */
    public void setYhzc(String yhzc) {
        this.yhzc = yhzc == null ? null : yhzc.trim();
    }

    /**
     * @return YHZCNR
     */
    public String getYhzcnr() {
        return yhzcnr;
    }

    /**
     * @param yhzcnr
     */
    public void setYhzcnr(String yhzcnr) {
        this.yhzcnr = yhzcnr == null ? null : yhzcnr.trim();
    }

    /**
     * @return LSLBS
     */
    public String getLslbs() {
        return lslbs;
    }

    /**
     * @param lslbs
     */
    public void setLslbs(String lslbs) {
        this.lslbs = lslbs == null ? null : lslbs.trim();
    }

    /**
     * @return QYZBM
     */
    public String getQyzbm() {
        return qyzbm;
    }

    /**
     * @param qyzbm
     */
    public void setQyzbm(String qyzbm) {
        this.qyzbm = qyzbm == null ? null : qyzbm.trim();
    }

    /**
     * @return KCE
     */
    public BigDecimal getKce() {
        return kce;
    }

    /**
     * @param kce
     */
    public void setKce(BigDecimal kce) {
        this.kce = kce;
    }

    /**
     * @return DELETED
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * @param deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", invoiceId=").append(invoiceId);
        sb.append(", spmc=").append(spmc);
        sb.append(", hsbz=").append(hsbz);
        sb.append(", slv=").append(slv);
        sb.append(", je=").append(je);
        sb.append(", dj=").append(dj);
        sb.append(", jldw=").append(jldw);
        sb.append(", ggxh=").append(ggxh);
        sb.append(", se=").append(se);
        sb.append(", sl=").append(sl);
        sb.append(", bmbbh=").append(bmbbh);
        sb.append(", ssflbm=").append(ssflbm);
        sb.append(", yhzc=").append(yhzc);
        sb.append(", yhzcnr=").append(yhzcnr);
        sb.append(", lslbs=").append(lslbs);
        sb.append(", qyzbm=").append(qyzbm);
        sb.append(", kce=").append(kce);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}