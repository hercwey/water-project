package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: TaxEquipInfo.java
 * @Description: 金税设备状态-金税盘状态查询返回报文
 *
 * @author lenovo
 * @date 2019年10月19日 下午2:55:44
 * @version V1.0 
 *
 */
public class TaxEquipInfo {
	/*
	Nsrsbh	纳税人识别号	字符	20		
	Bkpjlx	本开票机类型	字符	1		固定值
									0：主开票机
									1：开票机
	Fkpjsm	分开票机数目	字符	100		
	Kpdh	开票点号	字符	100		单机版时，是开票机号
								服务器版时，是服务器号
	Jssbdqrq	金税设备当前日期	日期	8		格式为yyyy-MM-dd
	Fpsfyw	发票是否用完	字符	1		固定值
								0：无可用发票
								1：有可用发票
	Scjzrq	上传截止日期	字符	100		1至31
								例如：
								20，指每月20日
	Lxsx	离线时限	字符	100		单位：小时
	Qdcxbbh	驱动程序版本号	字符	100		
	Dccxbbh	底层程序版本号	字符	100		
	Jspbh	金税盘编号	字符	20		
	Bspbh	报税盘编号	字符	20		
	Bspdcbbh	报税盘底层版本号	字符	100		
	Hzfwsq	汉字防伪授权	字符	1		固定值
								0：非汉字防伪
								1：汉字防伪
	Fxsqxx	发行授权信息	字符	1		固定值
								0：无
								1：有
	Bsprl	报税盘容量	字符	100	
	*/
	
	private String Nsrsbh; //	纳税人识别号	字符	20		
	private String Bkpjlx; //	本开票机类型	字符	1		固定值
							//		0：主开票机
							//		1：开票机
	private String  Fkpjsm; //	分开票机数目	字符	100		
	private String Kpdh; //	开票点号	字符	100		单机版时，是开票机号
							//	服务器版时，是服务器号
	private String Jssbdqrq;//	金税设备当前日期	日期	8		格式为yyyy-MM-dd
	private String Fpsfyw;//	发票是否用完	字符	1		固定值
							//	0：无可用发票
							//	1：有可用发票
	private String Scjzrq; //	上传截止日期	字符	100		1至31
						//		例如：
						//		20，指每月20日
	private String Lxsx;  //	离线时限	字符	100		单位：小时
	private String Qdcxbbh; //	驱动程序版本号	字符	100		
	private String Dccxbbh;//	底层程序版本号	字符	100		
	private String Jspbh; //	金税盘编号	字符	20		
	private String Bspbh;//	报税盘编号	字符	20		
	private String Bspdcbbh;//	报税盘底层版本号	字符	100		
	private String Hzfwsq;//	汉字防伪授权	字符	1		固定值
							//	0：非汉字防伪
							//	1：汉字防伪
	private String Fxsqxx; //	发行授权信息	字符	1		固定值
							//	0：无
							//	1：有
	private String Bsprl;//	报税盘容量	字符	100
	
	public String getNsrsbh() {
		return Nsrsbh;
	}
	public void setNsrsbh(String nsrsbh) {
		Nsrsbh = nsrsbh;
	}
	public String getBkpjlx() {
		return Bkpjlx;
	}
	public void setBkpjlx(String bkpjlx) {
		Bkpjlx = bkpjlx;
	}
	public String getFkpjsm() {
		return Fkpjsm;
	}
	public void setFkpjsm(String fkpjsm) {
		Fkpjsm = fkpjsm;
	}
	public String getKpdh() {
		return Kpdh;
	}
	public void setKpdh(String kpdh) {
		Kpdh = kpdh;
	}
	public String getJssbdqrq() {
		return Jssbdqrq;
	}
	public void setJssbdqrq(String jssbdqrq) {
		Jssbdqrq = jssbdqrq;
	}
	public String getFpsfyw() {
		return Fpsfyw;
	}
	public void setFpsfyw(String fpsfyw) {
		Fpsfyw = fpsfyw;
	}
	public String getScjzrq() {
		return Scjzrq;
	}
	public void setScjzrq(String scjzrq) {
		Scjzrq = scjzrq;
	}
	public String getLxsx() {
		return Lxsx;
	}
	public void setLxsx(String lxsx) {
		Lxsx = lxsx;
	}
	public String getQdcxbbh() {
		return Qdcxbbh;
	}
	public void setQdcxbbh(String qdcxbbh) {
		Qdcxbbh = qdcxbbh;
	}
	public String getDccxbbh() {
		return Dccxbbh;
	}
	public void setDccxbbh(String dccxbbh) {
		Dccxbbh = dccxbbh;
	}
	public String getJspbh() {
		return Jspbh;
	}
	public void setJspbh(String jspbh) {
		Jspbh = jspbh;
	}
	public String getBspbh() {
		return Bspbh;
	}
	public void setBspbh(String bspbh) {
		Bspbh = bspbh;
	}
	public String getBspdcbbh() {
		return Bspdcbbh;
	}
	public void setBspdcbbh(String bspdcbbh) {
		Bspdcbbh = bspdcbbh;
	}
	public String getHzfwsq() {
		return Hzfwsq;
	}
	public void setHzfwsq(String hzfwsq) {
		Hzfwsq = hzfwsq;
	}
	public String getFxsqxx() {
		return Fxsqxx;
	}
	public void setFxsqxx(String fxsqxx) {
		Fxsqxx = fxsqxx;
	}
	public String getBsprl() {
		return Bsprl;
	}
	public void setBsprl(String bsprl) {
		Bsprl = bsprl;
	}
	
	

}
