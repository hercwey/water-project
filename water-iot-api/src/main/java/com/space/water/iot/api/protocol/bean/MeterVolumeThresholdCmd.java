package com.space.water.iot.api.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.space.water.iot.api.protocol.util.ByteUtil;

public class MeterVolumeThresholdCmd extends MeterBase{

    private short threshold;
    private byte[] thresholdBytes = new byte[2];    // 水量阈值

    public MeterVolumeThresholdCmd(){}

    public MeterVolumeThresholdCmd(byte[] dataBytes){
        if (dataBytes.length < thresholdBytes.length){
            throw new RuntimeException("解析水量阈值数据长度错误");
        }
        ByteUtil.arrayCopy(dataBytes, 0, thresholdBytes);

        threshold = ByteUtil.getShort(thresholdBytes);
    }

    public byte[] encodeBytes(){
        ByteUtil.setBytes(thresholdBytes, threshold);
        return thresholdBytes;
    }

    public short getThreshold() {
        return threshold;
    }

    public void setThreshold(short threshold) {
        this.threshold = threshold;
    }
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("threshold", threshold)
                .toString();
    }
}
