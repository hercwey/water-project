package com.space.water.iot.api.model.common;

public class ControlValveType {
    public static final byte VALVE_OPEN             = (byte)0x55;   // 开
    public static final byte VALVE_CLOSE            = (byte)0x99;   // 关
    public static final byte VALVE_OPEN_PER_50      = (byte)0xA1;   // 打开50%
    public static final byte VALVE_OPEN_PER_25      = (byte)0xA2;   // 打开25%
    public static final byte VALVE_OPEN_PER_12_5    = (byte)0xA3;   // 打开12.5%
}
