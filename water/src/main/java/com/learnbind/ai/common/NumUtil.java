package com.learnbind.ai.common;
/**
 * 
 * 数字工具类
 * <功能详细描述>
 *
 * @author  cacabin
 * @version  1.0
 */
public class NumUtil {

    public static int getObjInt(Object obj, int i) {
        if (obj == null || obj.equals("") || obj.equals("null")) {
            return i;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return i;
        }
    }

    public static Integer getObjInt(Object obj) {
        if (obj == null || obj.equals("") || obj.equals("null")) {
            return null;
        }
        try {
            Double i = Double.valueOf(obj.toString());
            return i.intValue();
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getObjLong(Object obj, Long i) {
        if (obj == null || obj.equals("") || obj.equals("null")) {
            return i;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return i;
        }
    }

    public static Long getObjLong(Object obj) {
        if (obj == null || obj.equals("") || obj.equals("null")) {
            return null;
        }
        try {
            Double i = Double.valueOf(obj.toString());
            return i.longValue();
        } catch (Exception e) {
            return null;
        }
    }
}
