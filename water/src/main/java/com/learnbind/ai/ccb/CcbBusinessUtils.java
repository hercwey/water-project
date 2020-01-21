package com.learnbind.ai.ccb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.ccb
 *
 * @Title: CcbBusinessUtils.java
 * @Description: 中国建设银行业务部分工作类
 *
 * @author Administrator
 * @date 2019年7月14日 下午6:55:57
 * @version V1.0 
 *
 */
public class CcbBusinessUtils {

	//------------------------------	创建批量代扣文件部分	------------------------------
	/**
	 * @Title: getSn
	 * @Description: 获取批量代扣文件SN（唯一）
	 * @return 
	 */
	public static String getSn() {
//		String yyyyMMdd = getDateYYYYMMDD();
//		String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-+", "");
//		return yyyyMMdd+uuid;
		return getDateRandom();
	}
	
	/**
	 * @Title: getDateYYYYMMDD
	 * @Description: 获取类型
	 * @return 
	 */
	public static String getDateYYYYMMDD() {
		String s=new SimpleDateFormat("yyyyMMdd").format(new Date());
        return s;
	}
	
	/**
	 * 未用
	 * @Title: getFileName
	 * @Description: 获取文件名(yyyyMMddHHmmssSSS+uuid)
	 * @return 
	 */
	private String getFileName() {
//		String dateRandom = this.getDateRandom();
//		String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-+", "");
//		return dateRandom+"-"+uuid+".txt";
		return getSn()+".txt";
	}
	
	/**
     * @Title: getType
     * @Description: 获取type(yyyyMMddHHmmssSSS)
     * @return 
     */
    public static String getType(){
        String s=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return s;
    }
	
	/**
     * @Title: getDateRandom
     * @Description: 生成时间随机数(yyyyMMddHHmmssSSS)
     * @return 
     */
    public static String getDateRandom(){
        String s=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return s;
    }
	
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	public static boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}
	
}
