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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date(0);

	}
}
