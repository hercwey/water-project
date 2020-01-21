package com.learnbind.ai.util.pinyin4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.util.pinyin4j
 *
 * @Title: Pinyin4jUtils.java
 * @Description: pinyin4j工具类
 *
 * @author Administrator
 * @date 2019年8月15日 下午4:07:47
 * @version V1.0 
 *
 */
public class Pinyin4jUtils {

	private static Log log = LogFactory.getLog(Pinyin4jUtils.class);

	private static HanyuPinyinOutputFormat format = null;

	public static enum Type {
		UPPERCASE, // 全部大写
		LOWERCASE, // 全部小写
		FIRSTUPPER // 首字母大写
	}

	static {
		format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V); 
	}
//    public Pinyin4jUtils(){
//        format = new HanyuPinyinOutputFormat();
//        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//    }

	/**
	 * 将汉字转换成全拼音字母大写
	 * 
	 * @param str 字符串
	 * @return str为'我爱编程' ,return获取到的是'WOAIBIANCHENG'
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String toPyUppercase(String str) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYin(str, "", Type.UPPERCASE);
	}
	/**
	 * 将汉字转换成拼音首字母大写
	 * 
	 * @param str 字符串
	 * @return str为'我爱编程' ,return获取到的是'WABC'
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String toPyFirstUppercase(String str) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYinFirst(str, "", Type.UPPERCASE);
	}

	/**
	 * 将汉字转换成拼音首字母大写,可设置字母间的间隔符
	 * 
	 * @param str   字符串
	 * @param spera 转换字母间隔加的字符串,如果不需要为""
	 * @return str为'我爱编程' ,spera为'@' return获取到的是'W@A@B@C'
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String toPyUppercase(String str, String spera) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYinFirst(str, spera, Type.UPPERCASE);
	}

	/**
	 * 将汉字转换成全拼音字母小写
	 * 
	 * @param str 字符串
	 * @return str为'我爱编程' ,return获取到的是'woaibiancheng'
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String toPyLowercase(String str) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYin(str, "", Type.LOWERCASE);
	}
	
	/**
	 * 将汉字转换成拼音首字母小写
	 * 
	 * @param str 字符串
	 * @return str为'我爱编程' ,return获取到的是'wabc'
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String toPyFirstLowercase(String str) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYinFirst(str, "", Type.LOWERCASE);
	}

	/**
	 * 将汉字转换成全拼音首字母小写,可设置字母间的间隔符
	 * 
	 * @param str   字符串
	 * @param spera 转换字母间隔加的字符串,如果不需要为""
	 * @return str为'我爱编程',spera为'@' return获取到的是'w@a@b@c'
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String toPyLowercase(String str, String spera) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYinFirst(str, spera, Type.LOWERCASE);
	}

	/**
	 * 获取拼音首字母大写
	 * 
	 * @param str 字符串
	 * @return str为'我爱编程' ,return获取到的是'W'
	 * @throws BadHanyuPinyinOutputFormatCombination 异常信息
	 */
	public static String toPinYinUppercaseInitials(String str) throws BadHanyuPinyinOutputFormatCombination {
		String initials = null;
		String py = toPyUppercase(str);
		if (py.length() > 1) {
			initials = py.substring(0, 1);
		}
		if (py.length() <= 1) {
			initials = py;
		}
		return initials.trim();
	}

	/**
	 * 获取拼音首字母小写
	 * 
	 * @param str 字符串
	 * @return str为'我爱编程' ,return获取到的是'w'
	 * @throws BadHanyuPinyinOutputFormatCombination 异常信息
	 */
	public static String toPinYinLowercaseInitials(String str) throws BadHanyuPinyinOutputFormatCombination {
		String initials = null;
		String py = toPyLowercase(str);
		if (py.length() > 1) {
			initials = py.substring(0, 1);
		}
		if (py.length() <= 1) {
			initials = py;
		}
		return initials.trim();
	}

	/**
	 * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换，但如果包含英文则会转为对应的大小写字母
	 * 
	 * @param str   字符串
	 * @param spera 默认,可为""
	 * @param type  转换格式
	 * @return 按照转换格式转换成字符串
	 * @throws BadHanyuPinyinOutputFormatCombination 异常信息
	 */
	public static String toPinYin(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		if (type == Type.UPPERCASE) {
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		} else {
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		}
		String py = "";
		String temp = "";
		String[] t;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((int) c <= 128) {
				if (type == Type.UPPERCASE) {
					py += String.valueOf(c).toUpperCase();
				} else {
					py += String.valueOf(c).toLowerCase();
				}
				//py += c;
			} else {
				t = PinyinHelper.toHanyuPinyinStringArray(c, format);
				if (t == null || t.length<=0) {
					if (type == Type.UPPERCASE) {
						py += String.valueOf(c).toUpperCase();
					} else {
						py += String.valueOf(c).toLowerCase();
					}
					//py += c;
				} else {
					temp = t[0];
					if (type == Type.FIRSTUPPER) {
						temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
					}
//					if (temp.length() >= 1) {
//						temp = temp.substring(0, 1);
//					}
					py += temp + (i == str.length() - 1 ? "" : spera);
				}
			}
		}
		return py.trim();
	}
	
	/**
	 * 将str转换成拼音首字母，如果不是汉字或者没有对应的拼音，则不作转换，但如果包含英文则会转为对应的大小写字母
	 * 
	 * @param str   字符串
	 * @param spera 默认,可为""
	 * @param type  转换格式
	 * @return 按照转换格式转换成字符串
	 * @throws BadHanyuPinyinOutputFormatCombination 异常信息
	 */
	public static String toPinYinFirst(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		if (type == Type.UPPERCASE) {
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		} else {
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		}
		String py = "";
		String temp = "";
		String[] t;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((int) c <= 128) {
				if (type == Type.UPPERCASE) {
					py += String.valueOf(c).toUpperCase();
				} else {
					py += String.valueOf(c).toLowerCase();
				}
				//py += c;
			} else {
				t = PinyinHelper.toHanyuPinyinStringArray(c, format);
				if (t == null || t.length<=0) {
					if (type == Type.UPPERCASE) {
						py += String.valueOf(c).toUpperCase();
					} else {
						py += String.valueOf(c).toLowerCase();
					}
				} else {
					temp = t[0];
					if (type == Type.FIRSTUPPER) {
						temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
					}
					if (temp.length() >= 1) {
						temp = temp.substring(0, 1);
					}
					py += temp + (i == str.length() - 1 ? "" : spera);
				}
			}
		}
		return py.trim();
	}
	
	public static void main(String[] args) {
		String str = "我爱编程-123——ABC-def_我爱编程!@#$%^&*()_+-=;':\"?.,";
		//String str = "我爱编程";
		log.info("原字符串：" + str);
		try {
			String str1 = Pinyin4jUtils.toPyUppercase(str);
			log.info("转拼音大写：" + str1);
			String str2 = Pinyin4jUtils.toPyFirstUppercase(str);
			log.info("转拼音首字母大写：" + str2);
			String str3 = Pinyin4jUtils.toPyLowercase(str);
			log.info("转拼音小写：" + str3);
			String str4 = Pinyin4jUtils.toPyFirstLowercase(str);
			log.info("转拼音首字母小写：" + str4);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}

}
