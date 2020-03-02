package com.space.water.iot.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringUtil {

	public static boolean strIsNullOrEmpty(String s) {
		return (null == s || s.trim().length() < 1);
	}

	public static Date timeZoneTrans(String date) {
		date = date.replace("T", "");
		date = date.replace("Z", "");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		try {
			Date dateTemp = sdf.parse(date);
			date = sdf2.format(dateTemp);
			return sdf2.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(0);
	}

	public static Date meterTimeTrans(String meterTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

		meterTime = "20" + meterTime;
		String tempFront = meterTime.substring(0, 6);
		String tempBack = meterTime.substring(8);
		meterTime = tempFront + tempBack;

		try {
			Date dateTemp = sdf.parse(meterTime);
			meterTime = sdf2.format(dateTemp);
			return sdf2.parse(meterTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(0);
	}
	
	public static String toMeterTime(Date time) {
		String meterTime = "00010101000000";
		if (time == null) {
			time = new Date(0L);
		}
		//日期格式（秒/分/时/日/星期/月/年），7字节数字字符串(YYMMWWDDhhmmss)
		SimpleDateFormat sdf = new SimpleDateFormat("YYMMwwddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String tempTime = sdf.format(time);
		System.out.println(tempTime);
		
		return meterTime;
	}
	
	public static void main(String[] args) {
		toMeterTime(new Date(0L));
	}
}
