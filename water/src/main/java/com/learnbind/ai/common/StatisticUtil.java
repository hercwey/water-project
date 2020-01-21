package com.learnbind.ai.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.common
 *
 * @Title: StatisticUtil.java
 * @Description: 统计时所使用的公共函数
 *
 * @author lenovo
 * @date 2019年8月26日 下午4:55:11
 * @version V1.0 
 *
 */
public class StatisticUtil {

	/**
	 * @Title: getDaysOfMonth
	 * @Description: 获取指定日期中月份中的天数
	 * @param date  日期(Date类型)
	 * @return   月份的天数
	 */
	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * @Title: getDaysOfMonth
	 * @Description: 获取指定日期中月份中的天数
	 * @param datex 日期(String类型,格式yyyy-mm-dd)  
	 * @return  月份的天数
	 * @throws ParseException 
	 */
	public static int getDaysOfMonth(String datex)  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		try {
			date = sdf.parse(datex);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDaysOfMonth(date);
	}
	
 
	public static void main(String[] args) throws ParseException  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(getDaysOfMonth(sdf.parse("2015-02-2")));
	}
	
}
