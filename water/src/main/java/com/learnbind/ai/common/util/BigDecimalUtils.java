package com.learnbind.ai.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.common.util
 *
 * @Title: BigDecimalUtils.java
 * @Description: BigDecimal工具类
 *
 * @author Administrator
 * @date 2019年6月17日 下午12:12:23
 * @version V1.0 
 *
 */
public class BigDecimalUtils {

    /**
     * @Fields precision：小数的精度
     */
    private static int precision = 2;

    /**
     * @Title: add
     * @Description: 两数相加
     * @param b1	b1 操作数1
     * @param b2	操作数2
     * @return 	两数相加的和
     */
    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.add(b2);
    }

    /**
     * @Title: subtract
     * @Description: 两数相减
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	两数相减的差
     */
    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.subtract(b2);
    }

    /**
     * @Title: multiply
     * @Description: 两数相乘，默认保留两位小数（四舍五入）
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	两数相乘的积，保留2位小数
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.multiply(b2).setScale(precision,RoundingMode.HALF_UP);
    }

    /**
     * @Title: multiply
     * @Description: 两数相乘，可设置保留小数位数（四舍五入）
     * @param b1	操作数1
     * @param b2	操作数2
     * @param scale	小数位数
     * @return 	两数相乘的积
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2,int scale) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.multiply(b2).setScale(scale,RoundingMode.HALF_UP);
    }
    
    /**
     * @Title: divide
     * @Description: 两数相除，默认留两位小数（四舍五入）
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 两数相除的商，保留2位小数
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
        if(b1 == null) {
            return new BigDecimal(0);
        }
        if(b2  == null || equals(b2,new BigDecimal(0))){
            throw new ArithmeticException("b2 is zero ! ");
        }
        return b1.divide(b2, precision, RoundingMode.HALF_UP);
    }

    /**
     * @Title: divide
     * @Description: 两数相除，可设置保留小数位数（四舍五入）
     * @param b1	操作数1
     * @param b2	操作数2
     * @param scale	小数位数
     * @return 	两数相除的商
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2,int scale) {
        if(b1 == null) {
            return new BigDecimal(0);
        }
        if(b2  == null || equals(b2,new BigDecimal(0))){
            throw new ArithmeticException("b2 is zero ! ");
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }

    /**
     * @Title: greaterThan
     * @Description: 大于
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	true=大于；false=非大于
     */
    public static boolean greaterThan(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2)>0;
    }

    /**
     * @Title: greaterOrEquals
     * @Description: 大于或等于
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	true=大于或等于；false=小于
     */
    public static boolean greaterOrEquals(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2)>=0;
    }

    /**
     * @Title: lessThan
     * @Description: 小于
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	true=小于；false=大于或等于
     */
    public static boolean lessThan(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2) < 0;
    }

    /**
     * @Title: lessOrEquals
     * @Description: 小于或等于
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	true=小于或等于；false=大于
     */
    public static boolean lessOrEquals(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2) <= 0;
    }

    /**
     * @Title: equals
     * @Description: 等于
     * @param b1	操作数1
     * @param b2	操作数2
     * @return 	true=等于；false=非等于
     */
    public static boolean equals(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2) == 0;
    }

    /**
     * @Title: main
     * @Description: main方法
     * @param args 
     */
    public static void main(String[] args) {
    	BigDecimal first = new BigDecimal("123");
    	BigDecimal second = new BigDecimal("789");
    	//boolean flag = BigDecimalUtils.lessOrEquals(first, second);
    	boolean flag = BigDecimalUtils.greaterThan(first, second);
		System.out.println(flag);
	}
    
}
