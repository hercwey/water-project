package com.learnbind.ai.iot.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.learnbind.ai.iot.protocol.util.BCDUtil;
import com.learnbind.ai.iot.protocol.util.ByteUtil;

public class MeterConfig extends MeterBase {

    /* 配置项 */
    public static final short FLAG_PERIOD             = 0x0001;  // 定时上传周期
    public static final short FLAG_PERIOD_UNIT        = 0x0002;  // 定时上传周期单位
    public static final short FLAG_MAX_REPORT         = 0x0004;  // 定量上传值
    public static final short FLAG_EMERG_TIME         = 0x0008;  // 用户临时开阀用水限定时间
    public static final short FLAG_VALVE_TIME          = 0x0010;  // 阀门行程时间
    public static final short FLAG_VALVE_MAINTAIN_TIME = 0x0020;  // 阀门维护周期
    public static final short FLAG_WATER_VLOUME       = 0x0040;  // 累计使用量
    public static final short FLAG_SAMPLE_UNIT        = 0x0080;  // 采样参数
    public static final short FLAG_METER_NUM          = 0x0100;  // 表号
    public static final short FLAG_METER_TIME         = 0x0200;  // 表时间
    public static final short FLAG_METER_STATUS       = 0x0400;  // 表状态字
    public static final short FLAG_SERVER_IP          = 0x0800;  // 服务器IP
    public static final short FLAG_SERVER_PORT        = 0x1000;  // 端口号

    /* 定时上传周期单位 */
    public static final byte PERIOD_UNIT_MIN = 0; // 定时上传周期单位，分钟
    public static final byte PERIOD_UNIT_HOUR = 1; // 定时上传周期单位，小时
    public static final byte PERIOD_UNIT_DAY = 2; // 定时上传周期单位，天

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

    /* 配置成员变量 */
    private short period;            // 定时上传周期：2字节(0-65535)十进制，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台；
    private byte periodUnit;         // 定时上传周期单位：1字节(0-255)十进制，0为分钟，1为小时，2为天；
    private short maxReport;         // 定量上传值：2字节(0-65535)十进制，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台；
    private byte emergencyTime;      // 用户临时开阀用水限定时间：1字节(0-255)十进制，单位为小时，用户可通过磁吸装置实现临时用水；
    private byte valveTime;          // 阀门行程时间：1字节(0-255)十进制，单位为秒，正常情况下阀门单行程的最大时间值；
    private short valveMaintainTime; // 阀门维护周期：2字节(0-65535)十进制，单位为小时，水表以该周期值进行阀门维护操作；
    private int meterBasicValue;     // 表底数: 4字节, 整型
    private float sampleUnit;        // 采样参数：1字节(0-255)十进制，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样；
    private String meterNumber;      // 表号：12字节数字字符串(6字节水表资产编号，BCD格式；)
    private String meterTime;        // 表当前时间：14字节数字字符串格式为yyyymmddHHMMSS (7字节，年、月、星期、日、时、分、秒，BCD格式；)
    private short meterStatus;       // 表状态字：2字节，HEX格式
    private String serverIp;         // 服务器IP：AAA.BBB.CCC.DDD格式
    private short serverPort;        // 端口号：2字节(0-65535)十进制

    /***************************************************************
        私有成员变量, 消息解析和拼装时使用的缓冲区, 总长度为消息缓冲区的总长度
     ***************************************************************/
    private byte[] period_b          = new byte[2]; // 定时上传周期：2字节，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台，HEX格式；
    private byte[] periodUnit_b      = new byte[1]; // 定时上传周期单位：1字节，0为分钟，1为小时，2为天，HEX格式；
    private byte[] maxReport_b       = new byte[2]; // 定量上传值：2字节，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台，HEX格式；
    private byte[] emergencyTime_b   = new byte[1]; // 用户临时开阀用水限定时间：1字节，单位为小时，用户可通过磁吸装置实现临时用水，HEX格式；
    private byte[] valveTime_b       = new byte[1]; // 阀门行程时间：1字节，单位为秒，正常情况下阀门单行程的最大时间值，HEX格式；
    private byte[] valveMaintainTime_b = new byte[2];// 阀门维护周期：2字节，单位为小时，水表以该周期值进行阀门维护操作，HEX格式；
    private byte[] meterBasicValue_b = new byte[4]; // 表底数: 4字节
    private byte[] sampleUnit_b      = new byte[1]; // 采样参数：1字节，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样，HEX格式；
    private byte[] meterNumber_b     = new byte[6]; // 表号：6字节水表资产编号，BCD格式；
    private byte[] meterTime_b       = new byte[7]; // 表当前时间：7字节，年、月、星期、日、时、分、秒，BCD格式；
    private byte[] meterStatus_b     = new byte[2]; // 表状态字：2字节，HEX格式
    private byte[] serverIp_b        = new byte[4]; // 服务器IP：4字节，HEX格式；
    private byte[] serverPort_b      = new byte[2]; // 端口号：2字节，HEX格式；

    public MeterConfig(){

    }

    public MeterConfig(byte[] dataBytes){

        // 校验长度
        int length = getBytesLength();
        if (dataBytes.length != length){
            throw new RuntimeException("解析数据长度错误");
        }

        // 按字节长度解析
        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, period_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, periodUnit_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, maxReport_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, emergencyTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, valveTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, valveMaintainTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterBasicValue_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, sampleUnit_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterNumber_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterStatus_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, serverIp_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, serverPort_b);

        // 字节数组 ==> 成员变量
        period              = ByteUtil.getShort(period_b);
        periodUnit          = ByteUtil.getByte(periodUnit_b);
        maxReport           = ByteUtil.getShort(maxReport_b);
        emergencyTime       = ByteUtil.getByte(emergencyTime_b);
        valveTime           = ByteUtil.getByte(valveTime_b);
        valveMaintainTime   = ByteUtil.getShort(valveMaintainTime_b);
        meterBasicValue     = ByteUtil.getInt(meterBasicValue_b);
        sampleUnit          = convertSampleUnit(sampleUnit_b);
        meterNumber         = BCDUtil.bcd2String(meterNumber_b);
        meterTime           = BCDUtil.bcd2String(meterTime_b);
        meterStatus         = ByteUtil.getShort(meterStatus_b);
        serverIp            = ByteUtil.getStringIPValue(serverIp_b);
        serverPort          = ByteUtil.getShort(serverPort_b);
    }

    @Override
    public byte[] encodeBytes(){

        // 由成员变量 ==> 字节数组
        ByteUtil.setBytes(period_b, this.period);
        ByteUtil.setBytes(periodUnit_b, this.periodUnit);
        ByteUtil.setBytes(maxReport_b, this.maxReport);
        ByteUtil.setBytes(emergencyTime_b, this.emergencyTime);
        ByteUtil.setBytes(valveTime_b, this.valveTime);
        ByteUtil.setBytes(valveMaintainTime_b, this.valveMaintainTime);
        ByteUtil.setBytes(meterBasicValue_b, this.meterBasicValue);
        sampleUnit_b[0] = convertSampleUnit(this.sampleUnit);
        BCDUtil.setBcdBytes(meterNumber_b, this.meterNumber);
        BCDUtil.setBcdBytes(meterTime_b, this.meterTime);
        ByteUtil.setBytes(meterStatus_b, this.meterStatus);
        ByteUtil.setStringIPValue(serverIp_b, this.serverIp);
        ByteUtil.setBytes(serverPort_b, this.serverPort);

        // 拼装为字节缓冲区
        return ByteUtil.concatAll(period_b,
                periodUnit_b,
                maxReport_b,
                emergencyTime_b,
                valveTime_b,
                valveMaintainTime_b,
                meterBasicValue_b,
                sampleUnit_b,
                meterNumber_b,
                meterTime_b,
                meterStatus_b,
                serverIp_b,
                serverPort_b);
    }

    public static float convertSampleUnit(byte[] bytes){
        float unitValue = 0;
        int value = ByteUtil.getInt(bytes);
        switch (value){
            case 0:
                unitValue = SAMPLE_UNIT_01M;
                break;
            case 1:
                unitValue = SAMPLE_UNIT_1M;
                break;
            case 2:
                unitValue = SAMPLE_UNIT_001M;
                break;
            case 3:
                unitValue = SAMPLE_UNIT_1L;
                break;
        }
        return unitValue;
    }

    public static byte convertSampleUnit(float unit){

        if (SAMPLE_UNIT_01M == unit){
            return 0;
        } else if (SAMPLE_UNIT_1M == unit){
            return 1;
        } else if (SAMPLE_UNIT_001M == unit){
            return 2;
        }else if (SAMPLE_UNIT_1L == unit){
            return 3;
        }

        return 1;
    }

    public short getPeriod() {
        return period;
    }

    public void setPeriod(short period) {
        this.period = period;
    }

    public byte getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(byte periodUnit) {
        this.periodUnit = periodUnit;
    }

    public short getMaxReport() {
        return maxReport;
    }

    public void setMaxReport(short maxReport) {
        this.maxReport = maxReport;
    }

    public byte getEmergencyTime() {
        return emergencyTime;
    }

    public void setEmergencyTime(byte emergencyTime) {
        this.emergencyTime = emergencyTime;
    }

    public byte getValveTime() {
        return valveTime;
    }

    public void setValveTime(byte valveTime) {
        this.valveTime = valveTime;
    }

    public short getValveMaintainTime() {
        return valveMaintainTime;
    }

    public void setValveMaintainTime(short valveMaintainTime) {
        this.valveMaintainTime = valveMaintainTime;
    }

    public int getMeterBasicValue() {
        return meterBasicValue;
    }

    public void setMeterBasicValue(int meterBasicValue) {
        this.meterBasicValue = meterBasicValue;
    }

    public float getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(float sampleUnit) {
        this.sampleUnit = sampleUnit;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getMeterTime() {
        return meterTime;
    }

    public void setMeterTime(String meterTime) {
        this.meterTime = meterTime;
    }

    public short getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(short meterStatus) {
        this.meterStatus = meterStatus;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public short getServerPort() {
        return serverPort;
    }

    public void setServerPort(short serverPort) {
        this.serverPort = serverPort;
    }

    public int getBytesLength(){
        int length = ByteUtil.concatAllLength(period_b,
                periodUnit_b,
                maxReport_b,
                emergencyTime_b,
                valveTime_b,
                valveMaintainTime_b,
                meterBasicValue_b,
                sampleUnit_b,
                meterNumber_b,
                meterTime_b,
                meterStatus_b,
                serverIp_b,
                serverPort_b);
        return length;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("period", period)
                .append("periodUnit", periodUnit)
                .append("maxReport", maxReport)
                .append("emergencyTime", emergencyTime)
                .append("valveTime", valveTime)
                .append("valveMaintainTime", valveMaintainTime)
                .append("meterBasicValue", meterBasicValue)
                .append("sampleUnit", sampleUnit)
                .append("meterNumber", meterNumber)
                .append("meterTime", meterTime)
                .append("meterStatus", meterStatus)
                .append("serverIp", serverIp)
                .append("serverPort", serverPort)
                .toString();
    }
}
