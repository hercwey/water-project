package com.learnbind.ai.bean;

import java.util.List;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: TaxInvoiceBean.java
 * @Description: 发票查询bean
 *
 * @author Thinkpad
 * @date 2019年12月19日 下午4:40:29
 * @version V1.0 
 *
 */
public class TaxInvoiceBean {
	
    /**
     * @Fields gfmc：购方名称
     */
    private String gfmc;
   
    /**
     * @Fields gfsh：购方税号
     */
    private String gfsh;
    
    /**
     * @Fields fpdm：发票代码
     */
    private	String fpdm;
    
    /**
     * @Fields fphm：发票号码
     */
    private String fphm;
    
    /**
     * @Fields fpzl：发票种类；固定值：0：专用发票 2：普通发票12：机动车票51：电子发票
     */
    private String fpzl;
    
    /**
     * @Fields dybz：打印标志。0=未打印1=已打印
     */
    private List<String> dybzArr;
    
    /**
     * @Fields zfbz：作废标志。,0=未作废；1=已作废
     */
    private List<String> zfbzArr;
    
    /**
     * @Fields startDate：开票日期，起始日期
     */
    private String startDate;
    
    /**
     * @Fields endDate：开票日期，截至日期
     */
    private String endDate;
    
    /**
     * @Fields bsbz：报送标志。固定值 0：未报送1：已报送2：报送失败3：报送中4：验签失败空为全部
     */
    private List<String> bsbzArr;
    
    /**
     * @Fields period：年份
     */
    private String year;
    
    /**
     * @Fields month：月份
     */
    private String month;
    
    /**
     * @Fields searchCond：关键字
     */
    private String searchCond;

	public String getGfmc() {
		return gfmc;
	}

	public void setGfmc(String gfmc) {
		this.gfmc = gfmc;
	}

	public String getGfsh() {
		return gfsh;
	}

	public void setGfsh(String gfsh) {
		this.gfsh = gfsh;
	}

	public String getFpdm() {
		return fpdm;
	}

	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}

	public String getFphm() {
		return fphm;
	}

	public void setFphm(String fphm) {
		this.fphm = fphm;
	}

	public String getFpzl() {
		return fpzl;
	}

	public void setFpzl(String fpzl) {
		this.fpzl = fpzl;
	}

	public List<String> getDybzArr() {
		return dybzArr;
	}

	public void setDybzArr(List<String> dybzArr) {
		this.dybzArr = dybzArr;
	}

	public List<String> getZfbzArr() {
		return zfbzArr;
	}

	public void setZfbzArr(List<String> zfbzArr) {
		this.zfbzArr = zfbzArr;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getBsbzArr() {
		return bsbzArr;
	}

	public void setBsbzArr(List<String> bsbzArr) {
		this.bsbzArr = bsbzArr;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSearchCond() {
		return searchCond;
	}

	public void setSearchCond(String searchCond) {
		this.searchCond = searchCond;
	}

	@Override
	public String toString() {
		return "TaxInvoiceBean [gfmc=" + gfmc + ", gfsh=" + gfsh + ", fpdm=" + fpdm + ", fphm=" + fphm + ", fpzl="
				+ fpzl + ", dybzArr=" + dybzArr + ", zfbzArr=" + zfbzArr + ", startDate=" + startDate + ", endDate="
				+ endDate + ", bsbzArr=" + bsbzArr + ", year=" + year + ", month=" + month + ", searchCond="
				+ searchCond + "]";
	}

	
    
    
    
    
    
}
