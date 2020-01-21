package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "TAX_INVOICE")
public class TaxInvoice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT TAX_INVOICE_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    
    @Column(name = "FPJH")
    private String fpjh;

    @Column(name = "FPZL")
    private String fpzl;

    @Column(name = "FPDM")
    private String fpdm;

    @Column(name = "FPHM")
    private String fphm;

    @Column(name = "JYM")
    private String jym;

    @Column(name = "XFMC")
    private String xfmc;

    @Column(name = "XFSH")
    private String xfsh;

    @Column(name = "XFDZDH")
    private String xfdzdh;

    @Column(name = "XFYHZH")
    private String xfyhzh;

    @Column(name = "GFMC")
    private String gfmc;

    @Column(name = "GFSH")
    private String gfsh;

    @Column(name = "GFDZDH")
    private String gfdzdh;

    @Column(name = "GFYHZH")
    private String gfyhzh;

    @Column(name = "BZ")
    private String bz;

    @Column(name = "SKR")
    private String skr;

    @Column(name = "FHR")
    private String fhr;

    @Column(name = "KPR")
    private String kpr;

    @Column(name = "HJJE")
    private BigDecimal hjje;

    @Column(name = "HJSE")
    private BigDecimal hjse;

    @Column(name = "QDBZ")
    private String qdbz;

    @Column(name = "XSDJBH")
    private String xsdjbh;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "KPRQ")
    private Date kprq;

    @Column(name = "KPBZ")
    private String kpbz;

    @Column(name = "JQGG")
    private String jqgg;

    @Column(name = "ZFBZ")
    private Integer zfbz;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ZFRQ")
    private Date zfrq;

    @Column(name = "ZFR")
    private String zfr;

    @Column(name = "DYBZ")
    private String dybz;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "DYRQ")
    private Date dyrq;

    @Column(name = "DYR")
    private String dyr;

    @Column(name = "CHBZ")
    private Integer chbz;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CHRQ")
    private Date chrq;

    @Column(name = "CHR")
    private String chr;

    @Column(name = "BSBZ")
    private String bsbz;

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

    public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
     * @return FPJH
     */
    public String getFpjh() {
        return fpjh;
    }

    /**
     * @param fpjh
     */
    public void setFpjh(String fpjh) {
        this.fpjh = fpjh == null ? null : fpjh.trim();
    }

    /**
     * @return FPZL
     */
    public String getFpzl() {
        return fpzl;
    }

    /**
     * @param fpzl
     */
    public void setFpzl(String fpzl) {
        this.fpzl = fpzl == null ? null : fpzl.trim();
    }

    /**
     * @return FPDM
     */
    public String getFpdm() {
        return fpdm;
    }

    /**
     * @param fpdm
     */
    public void setFpdm(String fpdm) {
        this.fpdm = fpdm == null ? null : fpdm.trim();
    }

    /**
     * @return FPHM
     */
    public String getFphm() {
        return fphm;
    }

    /**
     * @param fphm
     */
    public void setFphm(String fphm) {
        this.fphm = fphm == null ? null : fphm.trim();
    }

    /**
     * @return JYM
     */
    public String getJym() {
        return jym;
    }

    /**
     * @param jym
     */
    public void setJym(String jym) {
        this.jym = jym == null ? null : jym.trim();
    }

    /**
     * @return XFMC
     */
    public String getXfmc() {
        return xfmc;
    }

    /**
     * @param xfmc
     */
    public void setXfmc(String xfmc) {
        this.xfmc = xfmc == null ? null : xfmc.trim();
    }

    /**
     * @return XFSH
     */
    public String getXfsh() {
        return xfsh;
    }

    /**
     * @param xfsh
     */
    public void setXfsh(String xfsh) {
        this.xfsh = xfsh == null ? null : xfsh.trim();
    }

    /**
     * @return XFDZDH
     */
    public String getXfdzdh() {
        return xfdzdh;
    }

    /**
     * @param xfdzdh
     */
    public void setXfdzdh(String xfdzdh) {
        this.xfdzdh = xfdzdh == null ? null : xfdzdh.trim();
    }

    /**
     * @return XFYHZH
     */
    public String getXfyhzh() {
        return xfyhzh;
    }

    /**
     * @param xfyhzh
     */
    public void setXfyhzh(String xfyhzh) {
        this.xfyhzh = xfyhzh == null ? null : xfyhzh.trim();
    }

    /**
     * @return GFMC
     */
    public String getGfmc() {
        return gfmc;
    }

    /**
     * @param gfmc
     */
    public void setGfmc(String gfmc) {
        this.gfmc = gfmc == null ? null : gfmc.trim();
    }

    /**
     * @return GFSH
     */
    public String getGfsh() {
        return gfsh;
    }

    /**
     * @param gfsh
     */
    public void setGfsh(String gfsh) {
        this.gfsh = gfsh == null ? null : gfsh.trim();
    }

    /**
     * @return GFDZDH
     */
    public String getGfdzdh() {
        return gfdzdh;
    }

    /**
     * @param gfdzdh
     */
    public void setGfdzdh(String gfdzdh) {
        this.gfdzdh = gfdzdh == null ? null : gfdzdh.trim();
    }

    /**
     * @return GFYHZH
     */
    public String getGfyhzh() {
        return gfyhzh;
    }

    /**
     * @param gfyhzh
     */
    public void setGfyhzh(String gfyhzh) {
        this.gfyhzh = gfyhzh == null ? null : gfyhzh.trim();
    }

    /**
     * @return BZ
     */
    public String getBz() {
        return bz;
    }

    /**
     * @param bz
     */
    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    /**
     * @return SKR
     */
    public String getSkr() {
        return skr;
    }

    /**
     * @param skr
     */
    public void setSkr(String skr) {
        this.skr = skr == null ? null : skr.trim();
    }

    /**
     * @return FHR
     */
    public String getFhr() {
        return fhr;
    }

    /**
     * @param fhr
     */
    public void setFhr(String fhr) {
        this.fhr = fhr == null ? null : fhr.trim();
    }

    /**
     * @return KPR
     */
    public String getKpr() {
        return kpr;
    }

    /**
     * @param kpr
     */
    public void setKpr(String kpr) {
        this.kpr = kpr == null ? null : kpr.trim();
    }

    /**
     * @return HJJE
     */
    public BigDecimal getHjje() {
        return hjje;
    }

    /**
     * @param hjje
     */
    public void setHjje(BigDecimal hjje) {
        this.hjje = hjje;
    }

    /**
     * @return HJSE
     */
    public BigDecimal getHjse() {
        return hjse;
    }

    /**
     * @param hjse
     */
    public void setHjse(BigDecimal hjse) {
        this.hjse = hjse;
    }

    /**
     * @return QDBZ
     */
    public String getQdbz() {
        return qdbz;
    }

    /**
     * @param qdbz
     */
    public void setQdbz(String qdbz) {
        this.qdbz = qdbz == null ? null : qdbz.trim();
    }

    /**
     * @return XSDJBH
     */
    public String getXsdjbh() {
        return xsdjbh;
    }

    /**
     * @param xsdjbh
     */
    public void setXsdjbh(String xsdjbh) {
        this.xsdjbh = xsdjbh == null ? null : xsdjbh.trim();
    }

    /**
     * @return KPRQ
     */
    public Date getKprq() {
        return kprq;
    }
    
    /**
     * @Title: getKprqStr
     * @Description: 获取开票日期字符串
     * @return 
     */
    public String getKprqStr() {
    	if(kprq!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(kprq);
    	}
    	return null;
    }

    /**
     * @param kprq
     */
    public void setKprq(Date kprq) {
        this.kprq = kprq;
    }

    /**
     * @return KPBZ
     */
    public String getKpbz() {
        return kpbz;
    }

    /**
     * @param kpbz
     */
    public void setKpbz(String kpbz) {
        this.kpbz = kpbz == null ? null : kpbz.trim();
    }

    /**
     * @return JQGG
     */
    public String getJqgg() {
        return jqgg;
    }

    /**
     * @param jqgg
     */
    public void setJqgg(String jqgg) {
        this.jqgg = jqgg == null ? null : jqgg.trim();
    }

    /**
     * @return ZFBZ
     */
    public Integer getZfbz() {
        return zfbz;
    }

    /**
     * @param zfbz
     */
    public void setZfbz(Integer zfbz) {
        this.zfbz = zfbz;
    }

    /**
     * @return ZFRQ
     */
    public Date getZfrq() {
        return zfrq;
    }

    /**
     * @param zfrq
     */
    public void setZfrq(Date zfrq) {
        this.zfrq = zfrq;
    }
    
    /**
     * @Title: getZfrqStr
     * @Description: 获取作废日期字符串
     * @return 
     */
    public String getZfrqStr() {
    	if(zfrq!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(zfrq);
    	}
    	return null;
    }

    /**
     * @return ZFR
     */
    public String getZfr() {
        return zfr;
    }

    /**
     * @param zfr
     */
    public void setZfr(String zfr) {
        this.zfr = zfr == null ? null : zfr.trim();
    }

    /**
     * @return DYBZ
     */
    public String getDybz() {
        return dybz;
    }

    /**
     * @param dybz
     */
    public void setDybz(String dybz) {
        this.dybz = dybz == null ? null : dybz.trim();
    }

    /**
     * @return DYRQ
     */
    public Date getDyrq() {
        return dyrq;
    }

    /**
     * @param dyrq
     */
    public void setDyrq(Date dyrq) {
        this.dyrq = dyrq;
    }
    
    /**
     * @Title: getDyrqStr
     * @Description: 打印日期字符串
     * @return 
     */
    public String getDyrqStr() {
    	if(dyrq!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(dyrq);
    	}
    	return null;
    }

    /**
     * @return DYR
     */
    public String getDyr() {
        return dyr;
    }

    /**
     * @param dyr
     */
    public void setDyr(String dyr) {
        this.dyr = dyr == null ? null : dyr.trim();
    }

    /**
     * @return CHBZ
     */
    public Integer getChbz() {
        return chbz;
    }

    /**
     * @param chbz
     */
    public void setChbz(Integer chbz) {
        this.chbz = chbz;
    }

    /**
     * @return CHRQ
     */
    public Date getChrq() {
        return chrq;
    }

    /**
     * @param chrq
     */
    public void setChrq(Date chrq) {
        this.chrq = chrq;
    }
    
    /**
     * @Title: getChrqStr
     * @Description: 冲红日期字符串
     * @return 
     */
    public String getChrqStr() {
    	if(chrq!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(chrq);
    	}
    	return null;
    }

    /**
     * @return CHR
     */
    public String getChr() {
        return chr;
    }

    /**
     * @param chr
     */
    public void setChr(String chr) {
        this.chr = chr == null ? null : chr.trim();
    }

    /**
     * @return BSBZ
     */
    public String getBsbz() {
        return bsbz;
    }

    /**
     * @param bsbz
     */
    public void setBsbz(String bsbz) {
        this.bsbz = bsbz == null ? null : bsbz.trim();
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
        sb.append(", customerId=").append(customerId);
        sb.append(", fpjh=").append(fpjh);
        sb.append(", fpzl=").append(fpzl);
        sb.append(", fpdm=").append(fpdm);
        sb.append(", fphm=").append(fphm);
        sb.append(", jym=").append(jym);
        sb.append(", xfmc=").append(xfmc);
        sb.append(", xfsh=").append(xfsh);
        sb.append(", xfdzdh=").append(xfdzdh);
        sb.append(", xfyhzh=").append(xfyhzh);
        sb.append(", gfmc=").append(gfmc);
        sb.append(", gfsh=").append(gfsh);
        sb.append(", gfdzdh=").append(gfdzdh);
        sb.append(", gfyhzh=").append(gfyhzh);
        sb.append(", bz=").append(bz);
        sb.append(", skr=").append(skr);
        sb.append(", fhr=").append(fhr);
        sb.append(", kpr=").append(kpr);
        sb.append(", hjje=").append(hjje);
        sb.append(", hjse=").append(hjse);
        sb.append(", qdbz=").append(qdbz);
        sb.append(", xsdjbh=").append(xsdjbh);
        sb.append(", kprq=").append(kprq);
        sb.append(", kpbz=").append(kpbz);
        sb.append(", jqgg=").append(jqgg);
        sb.append(", zfbz=").append(zfbz);
        sb.append(", zfrq=").append(zfrq);
        sb.append(", zfr=").append(zfr);
        sb.append(", dybz=").append(dybz);
        sb.append(", dyrq=").append(dyrq);
        sb.append(", dyr=").append(dyr);
        sb.append(", chbz=").append(chbz);
        sb.append(", chrq=").append(chrq);
        sb.append(", chr=").append(chr);
        sb.append(", bsbz=").append(bsbz);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}