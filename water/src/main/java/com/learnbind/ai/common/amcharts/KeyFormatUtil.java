package com.learnbind.ai.common.amcharts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class KeyFormatUtil {
    public static String formatKey(String key, KeyFormat format) {
        if ((StringUtils.isNotEmpty(key)) && (format != null)) {
            key = key.replace("-", "");
            List<String> keys = new ArrayList();
            if (KeyFormat.YYYY == format) {
                if (key.length() >= 4) {
                    keys.add(key.substring(0, 4));
                    key = StringUtils.join(keys, "");
                }
            } else if (KeyFormat.YYYY_MM == format) {
                if (key.length() >= 6) {
                    keys.add(key.substring(0, 4));
                    keys.add(key.substring(4, 6));
                    key = StringUtils.join(keys, "-");
                }
            } else if (KeyFormat.YYYY_MM_DD == format) {
                if (key.length() >= 8) {
                    keys.add(key.substring(0, 4));
                    keys.add(key.substring(4, 6));
                    keys.add(key.substring(6, 8));
                    key = StringUtils.join(keys, "-");
                }
            } else if (KeyFormat.YYYY_MM_DD_HH == format) {
                if (key.length() >= 10) {
                    keys.add(key.substring(0, 4));
                    keys.add(key.substring(4, 6));
                    keys.add(key.substring(6, 8));
                    key =
                            StringUtils.join(keys, "-") + " " + key.substring(8, 10);
                }
            } else if (KeyFormat.YYYY_MM_DD_HHMM == format) {
                if (key.length() >= 10) {
                    keys.add(key.substring(0, 4));
                    keys.add(key.substring(4, 6));
                    keys.add(key.substring(6, 8));
                    String hhmm = key.substring(8, 10);

                    if (key.length() >= 12) {
                        hhmm = hhmm + ":" + key.substring(10, 12);

                    } else {
                        hhmm = hhmm + ":00";
                    }
                    key = StringUtils.join(keys, "-") + " " + hhmm;
                }
            } else if (KeyFormat.YYYY_MM_DD_HHMMSS == format) {
                if (key.length() >= 12) {
                    keys.add(key.substring(0, 4));
                    keys.add(key.substring(4, 6));
                    keys.add(key.substring(6, 8));
                    String hhmmss = key.substring(8, 10) + ":" + key.substring(10, 12);

                    if (key.length() >= 14) {
                        hhmmss = hhmmss + ":" + key.substring(12, 14);

                    } else {
                        hhmmss = hhmmss + ":00";
                    }
                    key = StringUtils.join(keys, "-") + " " + hhmmss;
                }
            } else if (KeyFormat.LAST_TWO == format) {
                if (key.length() >= 2) {
                    keys.add(key.substring(key.length() - 2, key.length()));
                    key = StringUtils.join(keys, "-");
                }
            }
        }
        return key;
    }
}
