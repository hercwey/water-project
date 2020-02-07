package com.learnbind.ai.iot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringUtil {

    public static boolean strIsNullOrEmpty(String s) {
        return (null == s || s.trim().length() < 1);
    }
    
    public static String timeZoneTrans(String date) {
    	String time = date;
    	date = date.replace("T", "");
    	date = date.replace("Z", "");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
    	try {
			Date dateTemp = sdf.parse(date);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			time = sdf2.format(dateTemp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return time;
    }
}
