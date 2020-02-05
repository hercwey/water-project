package com.learnbind.ai.iot.protocol.bean;

import com.space.meter.protocol.util.BCDUtil;
import com.space.meter.protocol.util.ByteUtil;
import com.space.meter.protocol.util.ProtoUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeterReadWaterResp extends MeterBase {

    private String meterNumber;             // 表号， 6字节数字字符串
    private float sampleUnit;               // 数据采样单位M3
    private List<WaterVolume> listWater;    // 月冻结水量数据链表

    public class WaterVolume {
        public String date;     // 时间格式: yyyymmdd
        public int volume;      // 累积使用水量

        public WaterVolume(){}
        public WaterVolume(byte[] waterBytes) {
            byte[] dateBytes = new byte[3];
            byte[] valueBytes = new byte[4];

            System.arraycopy(waterBytes, 0, dateBytes, 0, 3);
            System.arraycopy(waterBytes, 3, valueBytes, 0, 4);

            date = BCDUtil.bcd2String(dateBytes);
            volume = ByteUtil.getInt(valueBytes);
        }
        public byte[] encodeBytes(){
            byte[] dateBytes = new byte[3];
            byte[] valueBytes = new byte[4];
            BCDUtil.setBcdBytes(dateBytes, date);
            ByteUtil.setBytes(valueBytes, volume);

            return ByteUtil.concatAll(dateBytes, valueBytes);
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }
        public String toString() {

            return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                    .append("date", date)
                    .append("volume", volume)
                    .toString();
        }
    }


    public MeterReadWaterResp(){}
    public MeterReadWaterResp(byte[] dataBytes) {

        byte[] meterNumberBytes = new byte[6];    // 表号: 6字节, 水表资产编号，BCD格式；
        byte[] sampleUnitBytes  = new byte[1];    // 采样参数: 1字节，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样，HEX格式；
        byte[] waterVolumeBytes = new byte[12*7]; // 月冻结：12 * 7个字节, (上1月冻结数据7；上2月冻结数据7；……上12月冻结数据7)

        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, meterNumberBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, sampleUnitBytes);
        pos += ByteUtil.arrayCopy(dataBytes, pos, waterVolumeBytes);

        // 解析数据到成员变量
        meterNumber = ProtoUtil.parseMeterNumber(meterNumberBytes); // 解析表号
        sampleUnit = ProtoUtil.parseSampleUnit(sampleUnitBytes); // 解析数据采样单位

        // 解析月冻结数据为List数组
        listWater = new ArrayList<>();

        for (int i = 0; i < waterVolumeBytes.length; i = i + 7) {
            byte[] waterBytes = new byte[7];
            System.arraycopy(waterVolumeBytes, i, waterBytes, 0, 7);
            WaterVolume water = new WaterVolume(waterBytes);
            listWater.add(water);
        }
    }

    public byte[] encodeBytes(){
        byte[] meterNumberBytes = new byte[6];    // 表号: 6字节, 水表资产编号，BCD格式；
        byte[] sampleUnitBytes  = new byte[1];    // 采样参数: 1字节，0为0.1M3采样，1为1M3采样，2为0.01M3采样，3为1L采样，HEX格式；

        // 封装
        ProtoUtil.packMeterNumber(meterNumberBytes, meterNumber);
        ProtoUtil.packSampleUnit(sampleUnitBytes, sampleUnit);

        List<byte[]> waterVolumeBytesList = new ArrayList<>(); // 月冻结：12 * 7个字节, (上1月冻结数据7；上2月冻结数据7；……上12月冻结数据7)
        for (WaterVolume volume : listWater){
            waterVolumeBytesList.add(volume.encodeBytes());
        }

        byte[] waterVolumeBytes = ByteUtil.concatAll(waterVolumeBytesList);
        return ByteUtil.concatAll(meterNumberBytes, sampleUnitBytes, waterVolumeBytes);
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public float getSampleUnit() {
        return sampleUnit;
    }

    public void setSampleUnit(float sampleUnit) {
        this.sampleUnit = sampleUnit;
    }

    public List<WaterVolume> getListWater() {
        return listWater;
    }

    public void setListWater(List<WaterVolume> listWater) {
        this.listWater = listWater;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("meterNumber", meterNumber)
                .append("sampleUnit", sampleUnit)
                .append("listWater", listWater)
                .toString();
    }

}
