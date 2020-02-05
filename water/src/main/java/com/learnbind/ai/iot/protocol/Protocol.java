package com.learnbind.ai.iot.protocol;

import com.space.meter.protocol.util.BCDUtil;
import com.space.meter.protocol.util.ByteUtil;

public class Protocol {

    /* 协议数据标识 */
    public static final short DATA_DI_METER_REPORT            = (short)0x901F;   // 数据上报  (DI0=1F, DI1=90)
    public static final short DATA_DI_METER_WATER             = (short)0x8101;   // 月冻结数据(DI0=01, DI1=81)
    public static final short DATA_DI_METER_CONFIG_READ       = (short)0x8121;   // 读取配置  (DI0=21, DI1=81)
    public static final short DATA_DI_METER_CONFIG_WRITE      = (short)0xA001;   // 写入配置  (DI0=01, DI1=A0)
    public static final short DATA_DI_METER_VALVE_CTRL        = (short)0x6090;   // 阀门控制  (DI0=90, DI1=60)
    public static final short DATA_DI_METER_VOLUME_THRESHOLD  = (short)0x7090;   // 设置水量阈值(DI0=90, DI1=70)

    /* 表类型, 1字节 */
    public static final byte METER_TYPE_10H = (byte)0x10;  // 冷水表
    public static final byte METER_TYPE_11H = (byte)0x11;  // 生活热水表
    public static final byte METER_TYPE_12H = (byte)0x12;  // 直饮水水表
    public static final byte METER_TYPE_13H = (byte)0x13;  // 中水水表

    /* 控制码 */
    public static final byte METER_CTR_0 = (byte)0x01;     // CTR_0 主站读
    public static final byte METER_CTR_1 = (byte)0x81;     // CTR_1 从站读正常应答
    public static final byte METER_CTR_2 = (byte)0xC1;     // CTR_2 从站读异常应答
    public static final byte METER_CTR_3 = (byte)0x04;     // CTR_3 主站写
    public static final byte METER_CTR_4 = (byte)0x84;     // CTR_4 从站写正常应答
    public static final byte METER_CTR_5 = (byte)0xC4;     // CTR_5 从站写异常应答

    /* 采样水量单位 */
    public static final float SAMPLE_UNIT_01M = 0.1f;    // 0.1 立方
    public static final float SAMPLE_UNIT_1M = 1f;       // 1   立方
    public static final float SAMPLE_UNIT_001M = 0.01f;  // 0.01立方
    public static final float SAMPLE_UNIT_1L = 0.001f;   // 1 升

    /* 表状态字 */
    public static final short METER_STATUS_VALVE_OPEN       = 0x0001;    // 阀门状态开 (开1 / 关0)
    public static final short METER_STATUS_VALVE_ABNORMAL   = 0x0002;    // 阀门故障 (1有效 / 0无效)
    public static final short METER_STATUS_BATTERY_LOW      = 0x0004;    // 电池电压低 (1有效 / 0无效)
    public static final short METER_STATUS_PERIOD_ON        = 0x0008;    // 定时上传功能开关 (1开 / 0关)
    public static final short METER_STATUS_MAX_REPORT_ON    = 0x0010;    // 定量上传功能开关 (1开 / 0关)
    public static final short METER_STATUS_MAGNETIC_ON      = 0x0020;    // 磁干扰关阀功能开关 (1开 / 0关)
    public static final short METER_STATUS_SMAPLE_LINE_CUT  = 0x0040;    // 采样线断线报警状态 (1有效 / 0无效)

    public Protocol(){}

}
