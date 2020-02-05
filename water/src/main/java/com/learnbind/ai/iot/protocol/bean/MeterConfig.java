package com.learnbind.ai.iot.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.learnbind.ai.iot.protocol.util.BCDUtil;
import com.learnbind.ai.iot.protocol.util.ByteUtil;
import com.learnbind.ai.iot.protocol.util.ProtoUtil;

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

    /* 配置成员变量 */
    private short reportPeriod;            // 定时上传周期：2字节(0-65535)十进制，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台；
    private byte reportPeriodUnit;         // 定时上传周期单位：1字节(0-255)十进制，0为分钟，1为小时，2为天；
    private short reportRation;         // 定量上传值：2字节(0-65535)十进制，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台；
    private byte temporaryTime;      // 用户临时开阀用水限定时间：1字节(0-255)十进制，单位为小时，用户可通过磁吸装置实现临时用水；
    private byte valveRunTime;          // 阀门行程时间：1字节(0-255)十进制，单位为秒，正常情况下阀门单行程的最大时间值；
    private short valveMaintainPeriod; // 阀门维护周期：2字节(0-65535)十进制，单位为小时，水表以该周期值进行阀门维护操作；
    private int meterBasicValue;     // 表底数: 4字节, 整型
    private float sampleUnit;        // 采样参数：1字节(0-255)十进制，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样；
    private String meterNumber;      // 表号：12字节数字字符串(6字节水表资产编号，BCD格式；)
    private String meterTime;        // 表当前时间：14字节数字字符串格式为yyyymmddHHMMSS (7字节，年、月、星期、日、时、分、秒，BCD格式；)
    private short meterStatusFlag;       // 表状态字：2字节，HEX格式
    private String serverIp;         // 服务器IP：AAA.BBB.CCC.DDD格式
    private short serverPort;        // 端口号：2字节(0-65535)十进制

    /***************************************************************
        私有成员变量, 消息解析和拼装时使用的缓冲区, 总长度为消息缓冲区的总长度
     ***************************************************************/
    private byte[] reportPeriod_b       = new byte[2]; // 定时上传周期：2字节，按定时上传周期单位值计数，到该值则定时上传数据包至系统后台，HEX格式；
    private byte[] reportPeriodUnit_b   = new byte[1]; // 定时上传周期单位：1字节，0为分钟，1为小时，2为天，HEX格式；
    private byte[] reportRation_b       = new byte[2]; // 定量上传值：2字节，在本次计量周期内，累计使用量达到该值则上传数据包至系统后台，HEX格式；
    private byte[] temporaryTime_b      = new byte[1]; // 用户临时开阀用水限定时间：1字节，单位为小时，用户可通过磁吸装置实现临时用水，HEX格式；
    private byte[] valveRunTime_b       = new byte[1]; // 阀门行程时间：1字节，单位为秒，正常情况下阀门单行程的最大时间值，HEX格式；
    private byte[] valveMaintainPeriod_b= new byte[2];// 阀门维护周期：2字节，单位为小时，水表以该周期值进行阀门维护操作，HEX格式；
    private byte[] meterBasicValue_b    = new byte[4]; // 表底数: 4字节
    private byte[] sampleUnit_b         = new byte[1]; // 采样参数：1字节，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样，HEX格式；
    private byte[] meterNumber_b        = new byte[6]; // 表号：6字节水表资产编号，BCD格式；
    private byte[] meterTime_b          = new byte[7]; // 表当前时间：7字节，年、月、星期、日、时、分、秒，BCD格式；
    private byte[] meterStatusFlag_b    = new byte[2]; // 表状态字：2字节，HEX格式
    private byte[] serverIp_b           = new byte[4]; // 服务器IP：4字节，HEX格式；
    private byte[] serverPort_b         = new byte[2]; // 端口号：2字节，HEX格式；

    public MeterConfig(){}

    public MeterConfig(byte[] dataBytes){

        // 校验长度
        int length = getBytesLength();
        if (dataBytes.length != length){
            throw new RuntimeException("解析数据长度错误");
        }

        // 按字节长度解析
        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, reportPeriod_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, reportPeriodUnit_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, reportRation_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, temporaryTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, valveRunTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, valveMaintainPeriod_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterBasicValue_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, sampleUnit_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterNumber_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterTime_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterStatusFlag_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, serverIp_b);
        pos += ByteUtil.arrayCopy(dataBytes, pos, serverPort_b);

        // 字节数组 ==> 成员变量
        reportPeriod        = ProtoUtil.parseReportPeriod(reportPeriod_b);
        reportPeriodUnit    = ProtoUtil.parseReportPeriodUnit(reportPeriodUnit_b);
        reportRation        = ProtoUtil.parseReportRation(reportRation_b);
        temporaryTime       = ProtoUtil.parseTemporaryTime(temporaryTime_b);
        valveRunTime        = ProtoUtil.parseValveRunTime(valveRunTime_b);
        valveMaintainPeriod = ProtoUtil.parseValveMaintainPeriod(valveMaintainPeriod_b);
        meterBasicValue     = ByteUtil.getInt(meterBasicValue_b);   // TODO: 可以优化
        sampleUnit          = ProtoUtil.parseSampleUnit(sampleUnit_b);
        meterNumber         = ProtoUtil.parseMeterNumber(meterNumber_b);
        meterTime           = ProtoUtil.parseMeterTime(meterTime_b);
        meterStatusFlag     = ProtoUtil.parseMeterStatusFlag(meterStatusFlag_b);
        serverIp            = ProtoUtil.parseServerIP(serverIp_b);
        serverPort          = ProtoUtil.parseServerPort(serverPort_b);
    }

    @Override
    public byte[] encodeBytes(){

        // 由成员变量 ==> 字节数组
        ProtoUtil.packReportPeriod(reportPeriod_b, this.reportPeriod);
        ProtoUtil.packReportPeriodUnit(reportPeriodUnit_b, this.reportPeriodUnit);
        ProtoUtil.packReportRation(reportRation_b, this.reportRation);
        ProtoUtil.packTemporaryTime(temporaryTime_b, this.temporaryTime);
        ProtoUtil.packValveRunTime(valveRunTime_b, this.valveRunTime);
        ProtoUtil.packValveMaintainPeriod(valveMaintainPeriod_b, this.valveMaintainPeriod);
        ByteUtil.setBytes(meterBasicValue_b, this.meterBasicValue);
        ProtoUtil.packSampleUnit(sampleUnit_b, this.sampleUnit);
        ProtoUtil.packMeterNumber(meterNumber_b, this.meterNumber);
        ProtoUtil.packMeterTime(meterTime_b, this.meterTime);
        ProtoUtil.packMeterStatusFlag(meterStatusFlag_b, this.meterStatusFlag);
        ProtoUtil.packServerIP(serverIp_b, this.serverIp);
        ProtoUtil.packServerPort(serverPort_b, this.serverPort);

        // 拼装为字节缓冲区
        return ByteUtil.concatAll(reportPeriod_b,
                reportPeriodUnit_b,
                reportRation_b,
                temporaryTime_b,
                valveRunTime_b,
                valveMaintainPeriod_b,
                meterBasicValue_b,
                sampleUnit_b,
                meterNumber_b,
                meterTime_b,
                meterStatusFlag_b,
                serverIp_b,
                serverPort_b);
    }

    public short getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(short reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public byte getReportPeriodUnit() {
        return reportPeriodUnit;
    }

    public void setReportPeriodUnit(byte reportPeriodUnit) {
        this.reportPeriodUnit = reportPeriodUnit;
    }

    public short getReportRation() {
        return reportRation;
    }

    public void setReportRation(short reportRation) {
        this.reportRation = reportRation;
    }

    public byte getTemporaryTime() {
        return temporaryTime;
    }

    public void setTemporaryTime(byte temporaryTime) {
        this.temporaryTime = temporaryTime;
    }

    public byte getValveRunTime() {
        return valveRunTime;
    }

    public void setValveRunTime(byte valveRunTime) {
        this.valveRunTime = valveRunTime;
    }

    public short getValveMaintainPeriod() {
        return valveMaintainPeriod;
    }

    public void setValveMaintainPeriod(short valveMaintainPeriod) {
        this.valveMaintainPeriod = valveMaintainPeriod;
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

    public short getMeterStatusFlag() {
        return meterStatusFlag;
    }

    public void setMeterStatusFlag(short meterStatusFlag) {
        this.meterStatusFlag = meterStatusFlag;
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
        int length = ByteUtil.concatAllLength(reportPeriod_b,
                reportPeriodUnit_b,
                reportRation_b,
                temporaryTime_b,
                valveRunTime_b,
                valveMaintainPeriod_b,
                meterBasicValue_b,
                sampleUnit_b,
                meterNumber_b,
                meterTime_b,
                meterStatusFlag_b,
                serverIp_b,
                serverPort_b);
        return length;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("reportPeriod", reportPeriod)
                .append("reportPeriodUnit", reportPeriodUnit)
                .append("reportRation", reportRation)
                .append("temporaryTime", temporaryTime)
                .append("valveRunTime", valveRunTime)
                .append("valveMaintainPeriod", valveMaintainPeriod)
                .append("meterBasicValue", meterBasicValue)
                .append("sampleUnit", sampleUnit)
                .append("meterNumber", meterNumber)
                .append("meterTime", meterTime)
                .append("meterStatusFlag", meterStatusFlag)
                .append("serverIp", serverIp)
                .append("serverPort", serverPort)
                .toString();
    }
}
