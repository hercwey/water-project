package com.learnbind.ai.iot.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.learnbind.ai.iot.protocol.util.BCDUtil;
import com.learnbind.ai.iot.protocol.util.ByteUtil;

public class MeterReport extends MeterBase {

    private String meterNumber;    // 表号: 6字节数字型字符串
    private String meterTime;      // 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
    private int totalVolume;       // 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
    private float sampleUnit;      // 采样参数：单位M3
    private String batteryVoltage; // 电池电压：单位V
    private short meterStatus;     // 表状态字：2字节
    private byte signal;           // 信号强度：1字节
    private float pressure;        // 压力值：xx.yyyy

    // 内部使用缓冲区变量, 共26字节
    private byte[] meterNumberBytes     = new byte[6];    // 表号: 6字节, 水表资产编号，BCD格式；
    private byte[] meterTimeBytes       = new byte[7];    // 表当前时间: 7字节，年、月、星期、日、时、分、秒，BCD格式；
    private byte[] totalVolumeBytes     = new byte[4];    // 累计使用量：4字节，与机械装置对于的表读数，HEX格式；
    private byte[] sampleUnitBytes      = new byte[1];    // 采样参数：1字节，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样，HEX格式；
    private byte[] batteryVoltageBytes  = new byte[2];    // 电池电压：2字节，单位为毫伏，BCD格式；(上层应用使用单位为V, 保留3为小数)
    private byte[] meterStatusBytes     = new byte[2];    // 表状态字：2字节，HEX格式
    private byte[] signalBytes          = new byte[1];    // 信号强度：1字节，HEX格式；
    private byte[] pressureBytes        = new byte[3];    // 压力值：3字节，1字节整数，2字节小数，单位为MPa，BCD格式；

    public MeterReport(){}

    public MeterReport(byte[] dataBytes){

        // 判断长度
        if (dataBytes.length < getBytesLength()){
            throw new RuntimeException("上报数据长度过短, 无法解析");
        }

        // 解析数据缓冲区
        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterNumberBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterTimeBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, totalVolumeBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, sampleUnitBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, batteryVoltageBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterStatusBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, signalBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, pressureBytes);

        meterNumber = BCDUtil.bcd2String(meterNumberBytes);
        meterTime = BCDUtil.bcd2String(meterTimeBytes);
        totalVolume = ByteUtil.getInt(totalVolumeBytes);
        sampleUnit = MeterConfig.convertSampleUnit(sampleUnitBytes);
        batteryVoltage = BCDUtil.bcd2String(batteryVoltageBytes);
        meterStatus = ByteUtil.getShort(meterStatusBytes);
        signal = ByteUtil.getByte(signalBytes);

        // 解析压力值, BCD格式, 前一个字节表示整数, 后两个字节为小数
        byte[] pressureInt = new byte[1];
        byte[] pressureFloat = new byte[2];

        pos = 0;
        pos += ByteUtil.arrayCopy(pressureBytes, pos, pressureInt);
        pos += ByteUtil.arrayCopy(pressureBytes, pos, pressureFloat);

        // 拼接为小数
        String pressureStr = BCDUtil.bcd2String(pressureInt) + "." + BCDUtil.bcd2String(pressureFloat);
        pressure = Float.valueOf(pressureStr);
    }

    public byte[] encodeBytes(){

        BCDUtil.setBcdBytes(meterNumberBytes, meterNumber);
        BCDUtil.setBcdBytes(meterTimeBytes, meterTime);
        ByteUtil.setBytes(totalVolumeBytes, totalVolume);

        sampleUnitBytes[0] =  MeterConfig.convertSampleUnit(sampleUnit);

        BCDUtil.setBcdBytes(batteryVoltageBytes, batteryVoltage);
        ByteUtil.setBytes(meterStatusBytes, meterStatus);

        ByteUtil.setBytes(signalBytes, signal);

        // 转换压力值,
        byte[] pressureInt = new byte[1];
        byte[] pressureFloat = new byte[2];
        BCDUtil.setBcdBytes(pressureInt, pressure * 10000 / 10000 + "");
        BCDUtil.setBcdBytes(pressureFloat, pressure * 10000 % 10000 + "");
        System.arraycopy(pressureInt, 0, pressureBytes, 0, 1);
        System.arraycopy(pressureFloat, 0, pressureBytes, 1, 2);

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

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public short getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(short meterStatus) {
        this.meterStatus = meterStatus;
    }

    public byte getSignal() {
        return signal;
    }

    public void setSignal(byte signal) {
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
