package com.learnbind.ai.iot.protocol.bean;

import com.space.meter.protocol.util.BCDUtil;
import com.space.meter.protocol.util.ByteUtil;
import com.space.meter.protocol.util.ProtoUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MeterReport extends MeterBase {

    private String meterNumber;    // 表号: 6字节数字型字符串
    private String meterTime;      // 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
    private int totalVolume;       // 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
    private float sampleUnit;      // 采样参数：单位M3
    private int batteryVoltage;    // 电池电压：单位V
    private short meterStatus;     // 表状态字：2字节
    private String signal;         // 信号强度
    private float pressure;        // 压力值：xx.yyyy

    // 内部使用缓冲区变量, 共26字节
    private byte[] meterNumberBytes     = new byte[6];    // 表号: 6字节, 水表资产编号，BCD格式；
    private byte[] meterTimeBytes       = new byte[7];    // 表当前时间: 7字节，秒/分/时/日/星期/月/年，BCD格式；
    private byte[] totalVolumeBytes     = new byte[4];    // 累计使用量：4字节，与机械装置对于的表读数，HEX格式；
    private byte[] sampleUnitBytes      = new byte[1];    // 采样参数：1字节，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样，HEX格式；
    private byte[] batteryVoltageBytes  = new byte[2];    // 电池电压：2字节，单位为毫伏，BCD格式；(上层应用使用单位为V, 保留3为小数)
    private byte[] meterStatusBytes     = new byte[2];    // 表状态字：2字节，HEX格式
    private byte[] signalBytes          = new byte[1];    // 信号强度：1字节，HEX格式；
    private byte[] pressureBytes        = new byte[3];    // 压力值：3字节，1字节整数，2字节小数，单位为MPa，BCD格式；

    public MeterReport(){}

    public MeterReport(byte[] dataBytes){

        // 校验接收数据的长度
        if (dataBytes.length < getBytesLength()){
            throw new RuntimeException("上报数据解析: 长度过短, 无法解析");
        }

        // 将缓冲区的数据按字段顺序拆分
        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterNumberBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterTimeBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, totalVolumeBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, sampleUnitBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, batteryVoltageBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterStatusBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, signalBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, pressureBytes);

        // 解析到成员变量
        meterNumber     = ProtoUtil.parseMeterNumber(meterNumberBytes);
        meterTime       = ProtoUtil.parseMeterTime(meterTimeBytes); // TODO: 可优化为Date
        totalVolume     = ProtoUtil.parseTotalVolume(totalVolumeBytes);
        sampleUnit      = ProtoUtil.parseSampleUnit(sampleUnitBytes);
        batteryVoltage  = ProtoUtil.parseBatteryVoltage(batteryVoltageBytes);
        meterStatus     = ProtoUtil.parseMeterStatusFlag(meterStatusBytes);
        signal          = ProtoUtil.parseSignal(signalBytes);
        pressure        = ProtoUtil.parsePressure(pressureBytes);
    }

    public byte[] encodeBytes(){

        ProtoUtil.packMeterNumber(meterNumberBytes, meterNumber);
        ProtoUtil.packMeterTime(meterTimeBytes, meterTime);
        ProtoUtil.packTotalVolume(totalVolumeBytes, totalVolume);
        ProtoUtil.packSampleUnit(sampleUnitBytes, sampleUnit);
        ProtoUtil.packBatteryVoltage(batteryVoltageBytes, batteryVoltage);
        ProtoUtil.packMeterStatusFlag(meterStatusBytes, meterStatus);
        ProtoUtil.packSignal(signalBytes, signal);
        ProtoUtil.packPressure(pressureBytes, pressure);

        return ByteUtil.concatAll(meterNumberBytes,
                meterTimeBytes,
                totalVolumeBytes,
                sampleUnitBytes,
                batteryVoltageBytes,
                meterStatusBytes,
                signalBytes,
                pressureBytes);
    }

    public int getBytesLength(){
        return ByteUtil.concatAllLength(meterNumberBytes,
                meterTimeBytes,
                totalVolumeBytes,
                sampleUnitBytes,
                batteryVoltageBytes,
                meterStatusBytes,
                signalBytes,
                pressureBytes);
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

    public int getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
    }

    public float getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(float sampleUnit) {
        this.sampleUnit = sampleUnit;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public short getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(short meterStatus) {
        this.meterStatus = meterStatus;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("meterNumber", meterNumber)
                .append("meterTime", meterTime)
                .append("totalVolume", totalVolume)
                .append("sampleUnit", sampleUnit)
                .append("batteryVoltage", batteryVoltage)
                .append("meterStatus", meterStatus)
                .append("signal", signal)
                .append("pressure", pressure)
                .toString();
    }

}
