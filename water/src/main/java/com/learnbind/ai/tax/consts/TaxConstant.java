package com.learnbind.ai.tax.consts;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax
 *
 * @Title: TaxConstant.java
 * @Description: 税务服务常量
 *
 * @author lenovo
 * @date 2019年9月29日 下午5:18:52
 * @version V1.0 
 *
 */
public class TaxConstant {

	//清单标志  QDBZ
	public static final String QDBZ_0="0";  //不开据清单
	public static final String QDBZ_1="1";  //开据清单
	
	//开票标志  KPBZ
	public static final String KPBZ_0="0";  //开票
	public static final String KPBZ="1";	//校验
	
	//含税标志 HSBZ
	public static final String HSBZ_0="0";  //不含税
	public static final String HSBZ_1="1";  //含税
	
	//以下两个字段需要进一步的细化.
	//BMBBH  编码版本号  详见国税局网站发布的分类编码表格
	//SSFLBM 税收分类编码  详见国税局网站发布的分类编码表格
	
	//YHZC  优惠政策
	public static final String YHZC_0="0";  //不享受;
	public static final String YHZC_1="1";  //享受
			
	//LSLBS  零税率标识	
	public static final String  LSLBS_EMPTY="";  //空：非零税率
	public static final String  LSLBS_0="0";  // 出口退税
	public static final String  LSLBS_1="1";//1：免税
	public static final String  LSLBS_2="1";//2：不征税
	public static final String  LSLBS_3="3";//3：普通零税率
	
	//TCBZ  弹窗标志
	public static final String TCBZ_0="0"; //0：不弹出参数设置窗口
	public static final String TCBZ_1="1";  //1：弹出参数设置窗口

	//QDDYFS 清单打印方式
	public static final String  QDDYFS_0="0";  //套打
	public static final String  QDDYFS_1="1";  //全打
	
	//XXBLX  信息表类型
	public static final String 	XXBLX_0="0";	//0：正常
	
	// SZLB  税种类别
	public static final String  SZLB_1="1";    //1：增值税 
	
	//DSLBZ  多税率标志
	public static final String  DSLBZ_0="0";  // 0：一票一税率；
	public static final String  DSLBZ_1="1";  // 1：一票多税率
	
	//YQZT  逾期状态	
	public static final String  YQZT_N="N";  //N：非逾期
	public static final String  YQZT_Y="Y";  //Y：逾期
	
	//XXBFW  信息表范围	
	public static final String  XXBFW_0="0";  //0：全部
	public static final String  XXBFW_1="1";  //1：本企业申请
	public static final String  XXBFW_2="2";  //2：其他企业申请
	
	//kpkz  开票控制
	public static final String  KPKZ_0="0";   //允许该客户端开票
	public static final String  KPKZ_1="1";   //不允许该客户端开票

	//ZFBZ  作废标志
	public static final String  ZFBZ_0="0";  //0：未作废
	public static final String  ZFBZ_1="1";  //1：己作废
	public static final String  ZFBZ_ALL="";  //空为全部
	
	//DYBZ  打印标志
	public static final String DYBZ_0="0";   //0：未打印
	public static final String DYBZ_1="1";  //1：已打印
	public static final String DYBZ_ALL=""; //空为全部

	//BSBZ  报送标志
	public static final String  BSBZ_0="0"; // 0：未报送
	public static final String  BSBZ_1="1";  //1：已报送
	public static final String  BSBZ_2="2";  //2：报送失败
	public static final String  BSBZ_3="3";  //3：报送中
	public static final String  BSBZ_4="4";  //4：验签失败
	public static final String  BSBZ_ALL="";  //空为全部
	
	//FPZL  发票种类(开票服务器)
	public static final String  FPZL_0="0";	//0-增值税专用发票；
	public static final String  FPZL_2="2";	//2-增值税普通发票；
	public static final String  FPZL_11="11";//11-货物运输业增值税专用发票；
	public static final String  FPZL_12="12";//12-机动车销售统一发票


	
	
	
		
	
}
