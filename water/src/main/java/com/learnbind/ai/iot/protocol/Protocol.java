package com.learnbind.ai.iot.protocol;

public class Protocol {

    /* 协议数据标识 */
    public static final int DATA_DI_METER_REPORT            = 0x1F90;   // 数据上报
    public static final int DATA_DI_METER_WATER             = 0x0181;   // 月冻结数据
    public static final int DATA_DI_METER_CONFIG_READ       = 0x2181;   // 读取配置
    public static final int DATA_DI_METER_CONFIG_WRITE      = 0x01A0;   // 写入配置
    public static final int DATA_DI_METER_VALVE_CTRL        = 0x9060;   // 阀门控制
    public static final int DATA_DI_METER_VOLUME_THRESHOLD  = 0x9070;   // 设置水量阈值

    /* 表类型 */
    public static final int METER_TYPE_10H = 0x10;  // 冷水表
    public static final int METER_TYPE_11H = 0x11;  // 生活热水表
    public static final int METER_TYPE_12H = 0x12;  // 直饮水水表
    public static final int METER_TYPE_13H = 0x13;  // 中水水表

    /* 控制码 */
    public static final int METER_CTR_0 = 0x01;     // CTR_0 主站读
    public static final int METER_CTR_1 = 0x81;     // CTR_1 从站读正常应答
    public static final int METER_CTR_2 = 0xC1;     // CTR_2 从站读异常应答
    public static final int METER_CTR_3 = 0x04;     // CTR_3 主站写
    public static final int METER_CTR_4 = 0x84;     // CTR_4 从站写正常应答
    public static final int METER_CTR_5 = 0xC4;     // CTR_5 从站写异常应答

    public Protocol(){}

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
//        if (sum > 0xff) { //超过了255，使用补码（补码 = 原码取反 + 1）
//            sum = ~sum;
//            sum = sum + 1;
//        }

        return (byte) (sum & 0xff);
    }
}
