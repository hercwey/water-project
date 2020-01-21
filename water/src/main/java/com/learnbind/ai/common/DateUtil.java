package com.learnbind.ai.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtil {
    private static final Log log = LogFactory.getLog(DateUtil.class);

    public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_DAY = "yyyy-MM-dd";

    public static Date setDate(String str, String format) {
        if ((str == null) || (str.trim().length() == 0)) {
            return null;
        }

        if (("yyyy-MM-dd".equals(format)) && (str.trim().length() == 10)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(str.substring(0, 4)),
                    Integer.parseInt(str.substring(5, 7)) - 1,
                    Integer.parseInt(str.substring(8, 10)));
            return cal.getTime();
        }
        if (("yyyy-MM-dd HH:mm:ss".equals(format)) && (str.trim().length() == 19)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(str.substring(0, 4)),
                    Integer.parseInt(str.substring(5, 7)) - 1,
                    Integer.parseInt(str.substring(8, 10)),
                    Integer.parseInt(str.substring(11, 13)),
                    Integer.parseInt(str.substring(14, 16)),
                    Integer.parseInt(str.substring(17, 19)));
            return cal.getTime();
        }
        if (("HH:mm:ss".equals(format)) && (str.trim().length() == 8)) {
            Calendar cal = Calendar.getInstance();
            cal.set(9999,
                    11,
                    31,
                    Integer.parseInt(str.substring(0, 2)),
                    Integer.parseInt(str.substring(3, 5)),
                    Integer.parseInt(str.substring(6, 8)));
            return cal.getTime();
        }


        return null;
    }


    /**
     * @Title: get
     * @Description: 获取日期的指定部分
     * @param date  日期
     * @param type  类型
     * 				可取值如下: 
     * 				"year"  "month"   "day"  "hour"
     * @return 		日期的指定部分,类型int 				
     */
    public static int get(Date date, String type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (type.equalsIgnoreCase("year")) {
            return cal.get(1);
        }
        if (type.equalsIgnoreCase("month")) {
            return cal.get(2) + 1;
        }
        if (type.equalsIgnoreCase("day")) {
            return cal.get(5);
        }
        if (type.equalsIgnoreCase("hour")) {
            return cal.get(11);
        }
        return 0;
    }


    public static String sysdate(String format) {
        return new SimpleDateFormat(format).format(sysDateTime());
    }

    public static Timestamp sysDateTime() {
        return new Timestamp(System.currentTimeMillis());
    }


    public static Date parse(String date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.parse(date);
        } catch (ParseException e) {
            log.error(e);
        }
        return null;
    }

    public static List<String> interval(String time) {
        List<String> lists = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            Date date = dateFormat.parse(time);
            lists = new ArrayList();

            Date current_date = new Date();


            Calendar cal = Calendar.getInstance();

            double hours = Math.abs((date.getTime() - current_date.getTime()) / 3600000L);
            double minus_hours_temp = Math.ceil(hours);

            int minus_hours = (int) minus_hours_temp;


            if (minus_hours > 1) {


                if (date.before(current_date)) {

                    cal.setTime(date);

                    for (int i = 0; i < minus_hours; i++) {
                        cal.add(11, 1);
                        lists.add(dateFormat.format(cal.getTime()));
                    }
                }
            }


            if (!lists.isEmpty()) {
                String lastDate = (String) lists.get(lists.size() - 1);

                Date temp_date = dateFormat.parse(lastDate);
                cal.setTime(temp_date);

                Calendar cal_temp = Calendar.getInstance();
                cal_temp.setTime(current_date);
                if (cal.get(11) == cal_temp.get(11)) {
                    lists.remove(lists.size() - 1);
                }
            }
        } catch (ParseException e) {
            log.error(e);
        }

        return lists;
    }

    public static String concat(String s, String format) {
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(s);

            result = sdf.format(date);
        } catch (ParseException e) {
            log.error(e);
        }

        return result;
    }


    public static String getCurrentYear() {
        Calendar cal1 = Calendar.getInstance();
        Timestamp currentTime = sysDateTime();

        cal1.setTime(new Date(currentTime.getTime()));
        Integer year = Integer.valueOf(cal1.get(1));
        return year.toString();
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        calendar.add(5, day);
        return calendar.getTime();
    }

    public static String getYYYYMMDDDate(Date date) {
        if (date == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }


    public static String getYYYYMMDDHHMMSSDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }


    public static Date getDateFromYYYYMMDD(String dateString)
            throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);

        return df.parse(dateString);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String addMinute(Date date, int minute) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        calendar.add(12, minute);
        return dateFormat.format(calendar.getTime());
    }


    public static String getCurrentDateStr(String formatStr) {
        SimpleDateFormat dateFormate = new SimpleDateFormat(formatStr);
        return dateFormate.format(new Date());
    }

    public static String getCurrentDay8Str() {
        return getCurrentDateStr("yyyy-MM-dd");
    }

    public static String getDay8StrByDay(Date date) {
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormate.format(date);
    }

    public static void main(String[] args) {
        System.out.println(getCurrentDay8Str());
    }
}
