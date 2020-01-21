package com.learnbind.ai.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "TAX_RED_INFO")
public class TaxRedInfo {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT TAX_RED_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;
    
    @Column(name = "SQFMC")
    private String sqfmc;

    @Column(name = "SQFSH")
    private String sqfsh;

    @Column(name = "KPDH")
    private String kpdh;

    @Column(name = "SBBH")
    private String sbbh;

    @Column(name = "XXBLSH")
    private String xxblsh;

    @Column(name = "XXBLX")
    private String xxblx;

    @Column(name = "DYLPDM")
    private String dylpdm;

    @Column(name = "DYLPHM")
    private String dylphm;

    @Column(name = "SZLB")
    private String szlb;

    @Column(name = "DSLBZ")
    private String dslbz;

    @Column(name = "TKRQ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tkrq;

    @Column(name = "GFMC")
    private String gfmc;

    @Column(name = "GFSH")
    private String gfsh;

    @Column(name = "XFMC")
    private String xfmc;

    @Column(name = "XFSH")
    private String xfsh;

    @Column(name = "HJJE")
    private BigDecimal hjje;

    @Column(name = "SLV")
    private BigDecimal slv;

    @Column(name = "HJSE")
    private BigDecimal hjse;

    @Column(name = "SQSM")
    private String sqsm;

    @Column(name = "CZR")
    private String czr;

    @Column(name = "CZRQ")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date czrq;

    @Column(name = "DELETED")
    private Integer deleted;
    
    @Column(name = "HZFPDM")
    private String hzfpdm;
    
    @Column(name = "HZFPHM")
    private String hzfphm;
    

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
    
    

    public String getSqfmc() {
		return sqfmc;
	}

	public void setSqfmc(String sqfmc) {
		this.sqfmc = sqfmc;
	}

	/**
     * @return SQFSH
     */
    public String getSqfsh() {
        return sqfsh;
    }

    /**
     * @param sqfsh
     */
    public void setSqfsh(String sqfsh) {
        this.sqfsh = sqfsh == null ? null : sqfsh.trim();
    }

    /**
     * @return KPDH
     */
    public String getKpdh() {
        return kpdh;
    }

    /**
     * @param kpdh
     */
    public void setKpdh(String kpdh) {
        this.kpdh = kpdh == null ? null : kpdh.trim();
    }

    /**
     * @return SBBH
     */
    public String getSbbh() {
        return sbbh;
    }

    /**
     * @param sbbh
     */
    public void setSbbh(String sbbh) {
        this.sbbh = sbbh == null ? null : sbbh.trim();
    }

    /**
     * @return XXBLSH
     */
    public String getXxblsh() {
        return xxblsh;
    }

    /**
     * @param xxblsh
     */
    public void setXxblsh(String xxblsh) {
        this.xxblsh = xxblsh == null ? null : xxblsh.trim();
    }

    /**
     * @return XXBLX
     */
    public String getXxblx() {
        return xxblx;
    }

    /**
     * @param xxblx
     */
    public void setXxblx(String xxblx) {
        this.xxblx = xxblx == null ? null : xxblx.trim();
    }

    /**
     * @return DYLPDM
     */
    public String getDylpdm() {
        return dylpdm;
    }

    /**
     * @param dylpdm
     */
    public void setDylpdm(String dylpdm) {
        this.dylpdm = dylpdm == null ? null : dylpdm.trim();
    }

    /**
     * @return DYLPHM
     */
    public String getDylphm() {
        return dylphm;
    }

    /**
     * @param dylphm
     */
    public void setDylphm(String dylphm) {
        this.dylphm = dylphm == null ? null : dylphm.trim();
    }

    /**
     * @return SZLB
     */
    public String getSzlb() {
        return szlb;
    }

    /**
     * @param szlb
     */
    public void setSzlb(String szlb) {
        this.szlb = szlb == null ? null : szlb.trim();
    }

    /**
     * @return DSLBZ
     */
    public String getDslbz() {
        return dslbz;
    }

    /**
     * @param dslbz
     */
    public void setDslbz(String dslbz) {
        this.dslbz = dslbz == null ? null : dslbz.trim();
    }

    /**
     * @return TKRQ
     */
    public Date getTkrq() {
        return tkrq;
    }

    /**
     * @param tkrq
     */
    public void setTkrq(Date tkrq) {
        this.tkrq = tkrq;
    }
    
    public String getTkrqStr() {
    	if(tkrq!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	return sdf.format(tkrq);
    	}
    	return null;
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
     * @return SQSM
     */
    public String getSqsm() {
        return sqsm;
    }

    /**
     * @param sqsm
     */
    public void setSqsm(String sqsm) {
        this.sqsm = sqsm == null ? null : sqsm.trim();
    }

    /**
     * @return CZR
     */
    public String getCzr() {
        return czr;
    }

    /**
     * @param czr
     */
    public void setCzr(String czr) {
        this.czr = czr == null ? null : czr.trim();
    }

    /**
     * @return CZRQ
     */
    public Date getCzrq() {
        return czrq;
    }

    /**
     * @param czrq
     */
    public void setCzrq(Date czrq) {
        this.czrq = czrq;
    }

    public String getCzrqStr() {
    	if(czrq!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(czrq);
    	}
    	return null;
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
    
	public String getHzfpdm() {
		return hzfpdm;
	}

	public void setHzfpdm(String hzfpdm) {
		this.hzfpdm = hzfpdm;
	}

	public String getHzfphm() {
		return hzfphm;
	}

	public void setHzfphm(String hzfphm) {
		this.hzfphm = hzfphm;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sqfmc=").append(sqfmc);
        sb.append(", sqfsh=").append(sqfsh);
        sb.append(", kpdh=").append(kpdh);
        sb.append(", sbbh=").append(sbbh);
        sb.append(", xxblsh=").append(xxblsh);
        sb.append(", xxblx=").append(xxblx);
        sb.append(", dylpdm=").append(dylpdm);
        sb.append(", dylphm=").append(dylphm);
        sb.append(", szlb=").append(szlb);
        sb.append(", dslbz=").append(dslbz);
        sb.append(", tkrq=").append(tkrq);
        sb.append(", gfmc=").append(gfmc);
        sb.append(", gfsh=").append(gfsh);
        sb.append(", xfmc=").append(xfmc);
        sb.append(", xfsh=").append(xfsh);
        sb.append(", hjje=").append(hjje);
        sb.append(", slv=").append(slv);
        sb.append(", hjse=").append(hjse);
        sb.append(", sqsm=").append(sqsm);
        sb.append(", czr=").append(czr);
        sb.append(", czrq=").append(czrq);
        sb.append(", deleted=").append(deleted);
        sb.append(", hzfpdm=").append(hzfpdm);
        sb.append(", hzfphm=").append(hzfphm);
        sb.append("]");
        return sb.toString();
    }
}