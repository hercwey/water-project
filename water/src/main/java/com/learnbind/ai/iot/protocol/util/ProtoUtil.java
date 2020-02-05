package com.space.meter.protocol.util;

public class ProtoUtil {
    /**
     * 计算校验和
     * @param bs 字符缓冲区
     * @param start 开始位置
     * @param length 从开始位置的长度
     * @return
     */
    public static final byte calcChecksum(byte[] bs, int start, int length) {
        if (start < 0 || length > bs.length) {
            throw new ArrayIndexOutOfBoundsException("getCheckSum error : index out of bounds(start=" + start
                    + ",end=" + length + ",bytes length=" + bs.length + ")");
        }

        // 计算加和的校验和
        int sum = 0;
        for (int i = start; i < length; i++) {
            sum = sum + bs[i];
        }

        sum=(sum % 256);
        return (byte)(sum & 0xff);
    }

    /**
     * 表号解析
     * @param dataBytes 表号：6字节水表资产编号，BCD格式
     * @return 数字型字符串, 长度为12
     */
    public static String parseMeterNumber(byte[] dataBytes){
        if (dataBytes.length != 6) {
            throw new RuntimeException("[表号]解析: 长度错误");
        }
        String value = BCDUtil.bcd2String(dataBytes);
        return value;
    }

    /**
     * 表号封装
     * @param dataBytes 目的缓冲区(6字节水表资产编号，BCD格式)
     * @param value 数字型字符串, 长度为12
     */
    public static void packMeterNumber(byte[] dataBytes, String value){
        if ((value.length() != 12 ) || (dataBytes.length != 6)){
            throw new RuntimeException("[表号]封装: 长度错误");
        }

        BCDUtil.setBcdBytes(dataBytes, value);
    }

    /**
     * 表当前时间解析
     * @param dataBytes 日期格式（秒/分/时/日/星期/月/年），BCD格式
     * @return 7字节数字字符串(YYMMWWDDhhmmss)
     */
    public static String parseMeterTime(byte[] dataBytes){
        if (dataBytes.length != 7) {
            throw new RuntimeException("[表时间]解析: 长度错误");
        }
        String value = BCDUtil.bcd2String(dataBytes);
        return value;
    }

    /**
     * 表当前时间封装
     * @param dataBytes 目的缓冲区, 日期格式（秒/分/时/日/星期/月/年），BCD格式
     * @param value 7字节数字字符串(YYMMWWDDhhmmss)
     */
    public static void packMeterTime(byte[] dataBytes, String value){
        if ((value.length() != 14 ) || (dataBytes.length != 7)){
            throw new RuntimeException("[表时间]封装: 长度错误");
        }

        BCDUtil.setBcdBytes(dataBytes, value);
    }

    /**
     * 累计使用量 解析
     * @param dataBytes
     * @return
     */
    public static int parseTotalVolume(byte[] dataBytes){
        if (dataBytes.length != 4){
            throw new RuntimeException("[累计使用量]解析: 长度错误");
        }
        return ByteUtil.getInt(dataBytes);
    }

    /**
     * 累计使用量 封装
     * @param dataBytes
     * @param value
     */
    public static void packTotalVolume(byte[] dataBytes, int value){
        if (dataBytes.length != 4){
            throw new RuntimeException("[累计使用量]封装: 长度错误");
        }
        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 电池电压 解析
     * @param dataBytes 2字节（整型），单位为毫伏，BCD格式
     * @return 电池电压，单位mv(毫伏)
     */
    public static int parseBatteryVoltage(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[电池电压]解析: 长度错误");
        }

        String strValue = BCDUtil.bcd2String(dataBytes);
        int value = Integer.parseInt(strValue);
        return value;
    }

    /**
     * 电池电压 封装
     * @param dataBytes 目的缓冲区 2字节（整型），单位为毫伏，BCD格式
     * @param value 电压值, 单位mv(毫伏)
     */
    public static void packBatteryVoltage(byte[] dataBytes, int value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[电池电压]封装: 长度错误");
        }
        String strValue = String.valueOf(value);
        ByteUtil.setBytes(dataBytes, strValue);
    }

    /**
     * 信号强度 解析
     * @param dataBytes 1字节，Hex格式
     * @return 信号强度值
     */
    public static String parseSignal(byte[] dataBytes){
        if (dataBytes.length != 1){
            throw new RuntimeException("[信号强度]解析: 长度错误");
        }

        return BCDUtil.bcd2String(dataBytes);
    }

    /**
     * 信号强度 封装
     * @param dataBytes 1字节，Hex格式
     * @param value 信号强度值
     * @return
     */
    public static void packSignal(byte[] dataBytes, String value){
        if (dataBytes.length != 1){
            throw new RuntimeException("[信号强度]封装: 长度错误");
        }

        BCDUtil.setBcdBytes(dataBytes, value);
    }

    /**
     * 压力值 解析
     * @param dataBytes 3字节，1字节整数，2字节小数，单位为MPa，BCD格式；
     * @return float值, 单位为MPa
     */
    public static float parsePressure(byte[] dataBytes){
        if (dataBytes.length != 3){
            throw new RuntimeException("[压力值]解析: 长度错误");
        }
        byte[] pressureInt = new byte[1];
        byte[] pressureFloat = new byte[2];

        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, pressureInt);
        pos += ByteUtil.arrayCopy(dataBytes, pos, pressureFloat);

        // 拼接为小数
        String pressureStr = BCDUtil.bcd2String(pressureInt) + "." + BCDUtil.bcd2String(pressureFloat);
        return Float.valueOf(pressureStr);
    }

    /**
     * 压力值 封装
     * @param dataBytes
     * @param value
     */
    public static void packPressure(byte[] dataBytes, float value){
        if (dataBytes.length != 3){
            throw new RuntimeException("[压力值]封装: 长度错误");
        }

        byte[] pressureInt = new byte[1];
        byte[] pressureFloat = new byte[2];
        BCDUtil.setBcdBytes(pressureInt, (byte)(value * 10000 / 10000) + "");
        BCDUtil.setBcdBytes(pressureFloat, (short)(value * 10000 % 10000) + "");
        System.arraycopy(pressureInt, 0, dataBytes, 0, 1);
        System.arraycopy(pressureFloat, 0, dataBytes, 1, 2);
    }

    /**
     * 定时上传周期 解析
     * @param dataBytes 1字节，HEX格式。0为分钟，1为小时，2为天；
     * @return 枚举变量
     */
    public static byte parseReportPeriodUnit(byte[] dataBytes){
        if (dataBytes.length != 1){
            throw new RuntimeException("[定时上传周期单位]解析: 长度错误");
        }

        return ByteUtil.getByte(dataBytes);
    }

    /**
     * 定时上传周期 封装
     * @param dataBytes
     * @param value 枚举
     */
    public static void packReportPeriodUnit(byte[] dataBytes, byte value){
        if (dataBytes.length != 1){
            throw new RuntimeException("[定时上传周期单位]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 定时上传周期 解析
     * @param dataBytes 2字节，HEX格式
     * @return
     */
    public static short parseReportPeriod(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[定时上传周期]解析: 长度错误");
        }

        short value = ByteUtil.getShort(dataBytes);
        return value;
    }

    /**
     * 定时上传周期 封装
     * @param dataBytes
     * @param value
     */
    public static void packReportPeriod(byte[] dataBytes, short value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[定时上传周期]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 定量上传值 解析
     * @param dataBytes 2字节，HEX格式
     * @return
     */
    public static short parseReportRation(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[定量上传值]解析: 长度错误");
        }

        short value = ByteUtil.getShort(dataBytes);
        return value;
    }

    /**
     * 定量上传值 封装
     * @param dataBytes
     * @param value
     */
    public static void packReportRation(byte[] dataBytes, short value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[定量上传周期]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 用户临时开阀用水限定时间
     * @param dataBytes 1字节，HEX格式
     * @return
     */
    public static byte parseTemporaryTime(byte[] dataBytes){
        if (dataBytes.length != 1){
            throw new RuntimeException("[用户临时开阀用水限定时间]解析: 长度错误");
        }

        return ByteUtil.getByte(dataBytes);
    }

    /**
     * 用户临时开阀用水限定时间
     * @param dataBytes
     * @param value
     */
    public static void packTemporaryTime(byte[] dataBytes, byte value){
        if (dataBytes.length != 1){
            throw new RuntimeException("[定量上传周期]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 阀门行程时间
     * @param dataBytes 1字节，单位为秒，正常情况下阀门单行程的最大时间值，HEX格式
     * @return
     */
    public static byte parseValveRunTime(byte[] dataBytes){
        if (dataBytes.length != 1){
            throw new RuntimeException("[阀门行程时间]解析: 长度错误");
        }
        return ByteUtil.getByte(dataBytes);
    }

    /**
     * 阀门行程时间
     * @param dataBytes
     * @param value
     */
    public static void packValveRunTime(byte[] dataBytes, byte value){
        if (dataBytes.length != 1){
            throw new RuntimeException("[阀门行程时间]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 阀门维护周期
     * @param dataBytes 2字节，单位为小时，水表以该周期值进行阀门维护操作，HEX格式
     * @return
     */
    public static short parseValveMaintainPeriod(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[阀门维护周期]解析: 长度错误");
        }
        return ByteUtil.getShort(dataBytes);
    }

    /**
     * 阀门维护周期
     * @param dataBytes
     * @param value
     */
    public static void packValveMaintainPeriod(byte[] dataBytes, short value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[阀门维护周期]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 参数修改标识
     * @param dataBytes 2字节, HEX格式
     * @return
     */
    public static short parseMeterConfigFlag(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[参数修改标识]解析: 长度错误");
        }
        return ByteUtil.getShort(dataBytes);
    }

    public static void packMeterConfigFlag(byte[] dataBytes, short value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[参数修改标识]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    /**
     * 采样参数
     * @param dataBytes 1字节，HEX格式
     * @return 返回单位为M3的数值
     */
    public static float parseSampleUnit(byte[] dataBytes){
        if (dataBytes.length != 1){
            throw new RuntimeException("[采样参数]解析: 长度错误");
        }

        byte value = ByteUtil.getByte(dataBytes);
        if (0 == value){
            return 0.1f; // 0.1M3采样
        } else if (1 == value) {
            return 1.0f; // 1M3采样
        } else if (2 == value){
            return 0.01f; // 0.01M3采样
        } else if (3 == value){
            return 0.001f; // 1L采样
        }else {
            throw new RuntimeException("[采样参数]解析: 采样参数字节错误");
        }
    }

    public static void packSampleUnit(byte[] dataBytes, float value){
        if (dataBytes.length != 1){
            throw new RuntimeException("[采样参数]封装: 长度错误");
        }

        byte bValue = 0;
        if (0.1f == value){
            bValue = 0;
        } else if (1.0f == value){
            bValue = 1;
        } else if (0.01f == value){
            bValue = 2;
        } else if (0.001f == value){
            bValue = 3;
        }else {
            throw new RuntimeException("[采样参数]封装: 采样参数值错误");
        }

        ByteUtil.setBytes(dataBytes, bValue);
    }

    public static short parseMeterStatusFlag(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[表状态字]解析: 长度错误");
        }
        return ByteUtil.getShort(dataBytes);
    }

    public static void packMeterStatusFlag(byte[] dataBytes, short value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[表状态字]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }

    public static String parseServerIP(byte[] dataBytes){
        if (dataBytes.length != 4){
            throw new RuntimeException("[服务器IP]解析: 长度错误");
        }

        return ByteUtil.getStringIPValue(dataBytes);
    }

    public static void packServerIP(byte[] dataBytes, String value){
        if (dataBytes.length != 4){
            throw new RuntimeException("[服务器IP]封装: 长度错误");
        }

        ByteUtil.setStringIPValue(dataBytes, value);
    }

    public static short parseServerPort(byte[] dataBytes){
        if (dataBytes.length != 2){
            throw new RuntimeException("[服务器端口]解析: 长度错误");
        }
        return ByteUtil.getShort(dataBytes);
    }

    public static void packServerPort(byte[] dataBytes, short value){
        if (dataBytes.length != 2){
            throw new RuntimeException("[服务器端口]封装: 长度错误");
        }

        ByteUtil.setBytes(dataBytes, value);
    }
}
