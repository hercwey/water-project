package com.learnbind.ai.common.amcharts;

public enum KeyFormat {
    YYYY {
        public String getValue() {
            return "yyyy";
        }
    },
    YYYY_MM {
        public String getValue() {
            return "yyyy-MM";
        }
    },
    YYYY_MM_DD {
        public String getValue() {
            return "yyyy-MM-dd";
        }
    },
    YYYY_MM_DD_HH {
        public String getValue() {
            return "yyyy-MM-dd HH";
        }
    },
    YYYY_MM_DD_HHMM {
        public String getValue() {
            return "yyyy-MM-dd HH:mm";
        }
    },
    YYYY_MM_DD_HHMMSS {
        public String getValue() {
            return "yyyy-MM-dd HH:mm:ss";
        }
    },
    LAST_TWO {
        public String getValue() {
            return "LAST_TWO";
        }
    };

    private KeyFormat() {
    }

    public abstract String getValue();
}
