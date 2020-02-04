package com.learnbind.ai.iot.util;

public class StringUtil {

    public static boolean strIsNullOrEmpty(String s) {
        return (null == s || s.trim().length() < 1);
    }
}
