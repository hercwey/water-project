package com.learnbind.ai.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 * 
 * 拼音工具类
 *
 * @author  cacabin
 * @version  1.0
 */
public class PinYin {
    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String getFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    pinyinName.append(nameChar[i]);
                } catch (Exception e) {
                    pinyinName.append(nameChar[i]);
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
        }
        return pinyinName.toString();
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String getSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    pinyinName.append(nameChar[i]);
                } catch (Exception e) {
                    pinyinName.append(nameChar[i]);
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
        }
        return pinyinName.toString();
    }

    public static String exchange(String str) {
        str = getFirstSpell(str);
        // 第一个数字的位置
        Integer index = getFirstLoactionNum(str);

        if (null != index) {
            String first = str.substring(0, index);

            String en = str.substring(index, str.length());

            // 截取后首个英文字母的位置
            index = getFirstLoactionEn(en);
            String num = "";
            String end = "";
            if (null != index) {
                num = en.substring(0, index);

                end = en.substring(index, en.length());
            } else {
                num = en.substring(0, en.length());
            }

            if (num.length() < 6) {
                int m = num.length();
                for (int i = 0; i < 6 - m; i++) {
                    num = "0" + num;
                }
            }

            str = first + num + end;
        }
        return str.toUpperCase();
    }

    public static Integer getFirstLoactionNum(String args) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(args);
        if (matcher.find()) {
            return args.indexOf(matcher.group());
        }

        return null;
    }

    public static Integer getFirstLoactionEn(String args) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(args);
        if (matcher.find()) {
            return args.indexOf(matcher.group());
        }

        return null;
    }
}
