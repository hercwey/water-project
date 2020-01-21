package com.learnbind.ai.common;


public class DefineStringUtil {

    public static String escapeAllLike(String content) {
        return escapeLike(content, 1);
    }

    private static String escapeLike(String content, int type) {
        if (isNotEmpty(content)) {
            content = content.replace("&amp;", "&");
            content = content.replace("\\", "\\\\");
            content = content.replace("%", "\\%");
            content = content.replace("_", "\\_");


            switch (type) {
                case 1:
                    return "%" + content + "%";
                case 2:
                    return "%" + content;
                case 3:
                    return content + "%";
            }

            return "%" + content + "%";
        }


        return content;
    }


    public static boolean isBlank(String str) {
        if ((str == null) || ("".equals(str.trim()))) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNumber(String str) {
        return (isNotEmpty(str)) && (str.matches("^\\d+$"));
    }

    public static String parseNull(Object objStr) {
        return objStr == null ? "" : objStr.toString();
    }

    public static boolean isArrayEmpty(Object[] array) {
        return (array == null) || (array.length == 0);
    }

    public static boolean isArrayNotEmpty(Object[] array) {
        return !isArrayEmpty(array);
    }


    public static String htmlEncode(String str) {
        if ((str != null) && (!"".equals(str))) {

            str = str.replaceAll("\\\\", "\\\\\\\\").replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&#39;");
        }
        return str;
    }
}
