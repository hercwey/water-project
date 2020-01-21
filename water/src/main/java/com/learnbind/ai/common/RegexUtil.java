package com.learnbind.ai.common;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     RegexUtil.java
*	@Description   正则表达式工具类 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月13日 上午10:20:12 
*
*	注:此类可用,暂时未重构.
*/
public class RegexUtil {
	
	//TODO 采用常量代替程序中的字符串表达式.
    private static final String ALPHANUMERIC = "^[a-zA-Z0-9_]+$";
    private static final String ALPHANUMERIC_LEN = "^[_0-9a-zA-Z]{%d,%d}$";
    private static final String INTEGER = "\\d+";
    private static final String INTEGER_LEN = "\\d{%d,%d}";
    private static final String LETTERSONLY = "^[a-z]+$";
    private static final String LETTERSONLY_LEN = "^[a-z]{%d,%d}$";
    private static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final String NUMBER = "^\\d{1,%d}+$|^\\d{1,%d}+\\.{1}\\d{1,%d}+$";
    private static final String PHONE = "^[0-9-]+$";
    private static final String PHONE_LEN = "^[0-9-]{%d,%d}$";

    /** 
     *	@Title isAlphaNumeric 
     *	@Description 字母数字
     * 
     *	@param str  需要判定的字符串
     *
     *	@return  如果是字母数字组成则返回true,否则返回false     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:20:39
    */
    public static boolean isAlphaNumeric(String str) {
        return test(str, "^[a-zA-Z0-9_]+$");
    }


    /** 
     *	@Title isAlphaNumericLen 
     *	@Description 判定字符串是否由字母数字组成,且长度在minLen(最小值)与maxLen(最大值)之间
     * 
     *	@param str	需要测试的字符串
     *	@param minLen 最小长度
     *	@param maxLen 最大长度
     *
     *	@return  如果字符串由最字母数字组成,且长度在minLen(最小值)与maxLen(最大值)之间,返回true,否则返回false
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:23:26
    */
    public static boolean isAlphaNumericLen(String str, int minLen, int maxLen) {
        String regex = String.format("^[_0-9a-zA-Z]{%d,%d}$", 
        							new Object[]{Integer.valueOf(minLen), Integer.valueOf(maxLen)});
        return test(str, regex);
    }


    /** 
     *	@Title isLettersOnly 
     *	@Description 测试字符串是否只由字母组成
     * 
     *	@param str 被测试字符串
     *
     *	@return		如果字符串只由字母组成,则返回true,否则返回false     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:25:44
    */
    public static boolean isLettersOnly(String str) {
        return test(str, "^[a-z]+$");
    }


    /** 
     *	@Title isLettersOnlyLen 
     *	@Description 测试字符串只由字母组成,且长度在minLen-maxLen之间
     * 
     *	@param str		被测试字符串
     *	@param minLen	最小长度
     *	@param maxLen	最大长度
     *
     *	@return			如测试字符串只由字母组成,且长度在minLen-maxLen之间,则返回true,否则返回false
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:28:01
    */
    public static boolean isLettersOnlyLen(String str, int minLen, int maxLen) {
        String regex = String.format("^[a-z]{%d,%d}$", 
        							new Object[]{Integer.valueOf(minLen), Integer.valueOf(maxLen)});
        return test(str, regex);
    }


    /** 
     *	@Title isInteger 
     *	@Description 测试字符串是否为整数.
     * 
     *	@param str	被测试字符串
     *	@return		如果被测试字符串为整型,则返回true,否则返回false
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:29:38
    */
    public static boolean isInteger(String str) {
        return test(str, "\\d+");
    }


    /** 
     *	@Title isIntegerLen 
     *	@Description 测试字符串为整数,则长度在minLen-maxLen之间
     * 
     *	@param str		被测试字符串
     *	@param minLen	最小长度
     *	@param maxLen	最大长度
     *
     *	@return  如果测试字符串为整数,则长度在minLen-maxLen之间,则返回true,否则返回false
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:31:00
    */
    public static boolean isIntegerLen(String str, int minLen, int maxLen) {
        String regex = String.format("\\d{%d,%d}", 
        							new Object[]{Integer.valueOf(minLen), Integer.valueOf(maxLen)});
        return test(str, regex);
    }


    /** 
     *	@Title isNumber 
     *	@Description  测试字符串是否为数字,且整数部分的长度在
     * 
     *	@param str		被测试字符串
     *	@param intLen	整数部分的最大长度
     *	@param decLen	小数部分的最大长度
     *	@return			如果被测试字符串是数字(整型或小数)则返回true,否则返回false
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:32:13
    */
    public static boolean isNumber(String str, int intLen, int decLen) {
        String regex = String.format("^\\d{1,%d}+$|^\\d{1,%d}+\\.{1}\\d{1,%d}+$", 
        							new Object[]{Integer.valueOf(intLen), Integer.valueOf(intLen), Integer.valueOf(decLen)});
        return test(str, regex);
    }


    /** 
     *	@Title isEMail 
     *	@Description 测试字符串是否为邮件地址
     * 
     *	@param str	被测试字符串
     *	@return		如果被测试字符串为邮件地址,则返回true,否则返回false
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:34:08
    */
    public static boolean isEMail(String str) {
        return test(str, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }


    /** 
     *	@Title isPhone 
     *	@Description 测试字符串是否为电话号码.
     * 
     *	@param str		被测试字符串
     *	@return			如果是电话号码,则返回true,否则返回false
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:35:11
    */
    public static boolean isPhone(String str) {
        return test(str, "^[0-9-]+$");
    }


    /** 
     *	@Title isPhoneLen 
     *	@Description  测试字符串是否为电话号码,且长度在minLen-maxLen之间
     * 
     *	@param str		被测试字符串
     *	@param minLen	最小长度
     *	@param maxLen	最大长度
     *	@return			如果测试字符串是否为电话号码,且长度在minLen-maxLen之间,则返回true;否则返回false;
     *     
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:36:14
    */
    public static boolean isPhoneLen(String str, int minLen, int maxLen) {
        String regex = String.format("^[0-9-]{%d,%d}$", 
        							new Object[]{Integer.valueOf(minLen), Integer.valueOf(maxLen)});
        return test(str, regex);
    }
    
    /** 
     *	@Title test 
     *	@Description 测试指定字符串是否满足正则表达式
     * 
     *	@param str		被测试字符串
     *	@param regex	正则表达式
     *
     *	@return			如果被测试字符串满足正则表达式,则返回true,否则返回false
     *	@Return boolean    返回类型 
     *
     *	@Date 2018年12月13日 上午10:37:50
    */
    public static boolean test(String str, String regex) {
        if (str != null) {
            return str.matches(regex);
        }
        return false;
    }


    public static void main(String[] args) {
        //System.out.println(isPhoneLen("123123",4,5));  	//返回false
        System.out.println(isNumber("123123.2222",6,3));  	//返回false,由于小数点后有4位小数
    }
}
