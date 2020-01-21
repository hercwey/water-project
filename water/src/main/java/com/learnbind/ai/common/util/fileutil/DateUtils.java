package com.learnbind.ai.common.util.fileutil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.ai.upload.utils
 *
 * @Title: DateUtils.java
 * @Description: 日期时间工具类
 *
 * @author Administrator
 * @date 2019年5月23日 下午6:31:44
 * @version V1.0 
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
     
    private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
        "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };
 
    /**
     * @Title: formatTimeToLong
     * @Description: 时间字符串转为Long类型
     * @param str
     * @return
     * @throws ParseException 
     */
    public static Long formatTimeToLong(String str) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	return sdf.parse(str).getTime();
    }
    
    /**
     * @Title: getDateTimeLong
     * @Description: 获取1970年距今的秒数
     * @return 
     */
    public static Long getDateTimeLong(){
    	return new Date().getTime()/1000;
    }

    /**
     * @Title: getDateTime
     * @Description: LongTime转为yyyy-MM-dd HH:mm:ss
     * @param time
     * @return 
     */
    public static String getDateTime(Long time){
    	return formatDateTime(new Date(time));
    }
    
    /**
     * @Title: getDate
     * @Description: LongTime转为yyyy-MM-dd
     * @param time
     * @return 
     */
    public static String getDate(Long time){
    	return formatDate(new Date(time));
    }

    /**
     * @Title: getDate
     * @Description: 获取当前日期字符串 格式（yyyy-MM-dd）
     * @return 
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }
     
    /**
     * @Title: getDate
     * @Description: 获取当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     * @param pattern
     * @return 
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }
     
    /**
     * @Title: formatDate
     * @Description: 获取日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     * @param date
     * @param pattern
     * @return 
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }
     
    /**
     * @Title: formatDateTime
     * @Description: 日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     * @param date
     * @return 
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * @Title: getTime
     * @Description: 获取当前时间字符串 格式（HH:mm:ss）
     * @return 
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }
 
    /**
     * @Title: getDateTime
     * @Description: 获取当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     * @return 
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
 
    /**
     * @Title: getYear
     * @Description: 获取当前年份字符串 格式（yyyy）
     * @return 
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }
 
    /**
     * @Title: getMonth
     * @Description: 获取当前月份字符串 格式（MM）
     * @return 
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }
    
    /**
     * @Title: getMonth
     * @Description: 获取当前年月份字符串 格式（yyyy-MM）
     * @return 
     */
    public static String getYearMonth() {
        return formatDate(new Date(), "yyyy-MM");
    }
 
    /**
     * @Title: getDay
     * @Description: 获取当天字符串 格式（dd）
     * @return 
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }
 
    /**
     * @Title: getWeek
     * @Description: 获取当前星期字符串 格式（E）星期几
     * @return 
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }
    
    /**
     * @Title: getYYYYMMDDStr
     * @Description: 获取当前日期字符串（yyyyMMdd）
     * @return 
     * 		返回格式：yyyyMMdd
     */
    public static String getYYYYMMDDStr() {
        return formatDate(new Date(), "yyyyMMdd");
    }
     
    /**
     * @Title: parseDate
     * @Description: 日期型字符串转化为日期 格式
     * 		{ "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
     *   		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
     * @param str
     * @return 
     */
    public static Date parseDate(Object str) {
        if (str == null){
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }
 
    /**
     * @Title: pastDays
     * @Description: 获取过去的天数
     * @param date
     * @return 
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime()-date.getTime();
        return t/(24*60*60*1000);
    }
     
    /**
     * @Title: getDateStart
     * @Description: 获取开始日期
     * @param date
     * @return 
     */
    public static Date getDateStart(Date date) {
        if(date==null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date= sdf.parse(formatDate(date, "yyyy-MM-dd")+" 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
     
    /**
     * @Title: getDateEnd
     * @Description: 获取结束日期
     * @param date
     * @return 
     */
    public static Date getDateEnd(Date date) {
        if(date==null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            date= sdf.parse(formatDate(date, "yyyy-MM-dd") +" 23:59:59.999");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * @Title: isDate
     * @Description: 判断字符串是否是日期
     * @param timeString
     * @return 
     */
    public static boolean isDate(String timeString){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try{
            format.parse(timeString);
        }catch(Exception e){
            return false;
        }
        return true;
    }
     
    /**
     * @Title: dateFormat
     * @Description: 格式化时间
     * @param timestamp
     * @return 
     * 		yyyy-MM-dd HH:mm:ss
     */
    public static String dateFormat(Date timestamp){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }
     
    /**
     * @Title: getSysTimestamp
     * @Description: 获取系统时间Timestamp
     * @return 
     */
    public static Timestamp getSysTimestamp(){
        return new Timestamp(new Date().getTime());
    }
     
    /**
     * @Title: getSysDate
     * @Description: 获取系统时间Date
     * @return 
     */
    public static Date getSysDate(){
        return new Date();
    }
     
    /**
     * @Title: getDateRandom
     * @Description: 生成时间随机数（yyyyMMddHHmmssSSS）
     * @return 
     */
    public static String getDateRandom(){
        String s=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return s;
    }
    
    /**
     * @Title: getDaysFrom1970ToToday
     * @Description: 从 1970-01-01 00:00:00 开始到今天的天数
     * @param todayMS
     * @return
     * @throws ParseException 
     */
    public static int getDaysFrom1970ToToday(Long todayMS) throws ParseException{
    	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long dateMS = sdf.parse("1970-01-01 00:00:00").getTime();
		Long days = (todayMS-dateMS)/1000/60/60/24;
        return days.intValue();
    }
    
    /**
     * @Title: getYesterdayDatetimeStart
     * @Description: 获取昨天开始时间
     * @param date
     * @return 
     */
    public static Long getYesterdayDatetimeStart(Date date){
    	return getDateStart(addDays(date, -1)).getTime();
    }

    /**
     * @Title: getYesterdayDatetimeEnd
     * @Description: 获取昨天的结束日期
     * @param date
     * @return 
     */
    public static Long getYesterdayDatetimeEnd(Date date){
    	return getDateEnd(addDays(date, -1)).getTime();
    }
    
    
    /**
     * 返回当前日期及时间
     * @return
     */
    public static String nowDateTime() {
    	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    	
    	return sdf.format(new Date());
    }
    
    
    //------------------------------ 对日期进行判定	------------------------------
    
	/**
	 * @Title: isEffectiveDate
	 * @Description: 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 * @param nowTime	当前时间
	 * @param startTime	开始时间
	 * @param endTime	结束时间
	 * @return 
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args) {
		String format = "HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		String now = sf.format(new Date());
		now = "16:59:59";
		Date nowTime;
		try {
			nowTime = new SimpleDateFormat(format).parse(now);
			Date startTime = new SimpleDateFormat(format).parse("09:00:00");
			Date endTime = new SimpleDateFormat(format).parse("17:00:00");
			if (isEffectiveDate(nowTime, startTime, endTime)) {
				System.out.println("系统时间在早上9点到下午17点之间.");
			} else {
				System.out.println("系统时间不在早上9点到下午17点之间.");
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
     
}