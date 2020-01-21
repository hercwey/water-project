package com.learnbind.ai.common;

import java.math.BigDecimal;


public final class NumberUtil {
    public static final int TWO = 2;

    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, scale, 4).doubleValue());
    }

    public static int getObjInt(Object obj, int i) {
        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return i;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
        }

        return i;
    }


    public static Integer getObjInt(Object obj) {
        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return null;
        }
        try {
            Double i = Double.valueOf(obj.toString());
            return Integer.valueOf(i.intValue());
        } catch (Exception e) {
        }

        return null;
    }


    public static Long getObjLong(Object obj, Long i) {
        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return i;
        }
        try {
            return Long.valueOf(Long.parseLong(obj.toString()));
        } catch (Exception e) {
        }

        return i;
    }


    public static Long getObjLong(Object obj) {
        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return null;
        }
        try {
            Double i = Double.valueOf(obj.toString());
            return Long.valueOf(i.longValue());
        } catch (Exception e) {
        }

        return null;
    }

    public static double getObjDou(Object obj, int i) {
        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return new Double(i).doubleValue();
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
        }

        return Double.valueOf(i).doubleValue();
    }


    public static Double getObjDou(Object obj) {
        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return null;
        }
        try {
            return Double.valueOf(Double.parseDouble(obj.toString()));
        } catch (Exception e) {
        }

        return null;
    }
}
