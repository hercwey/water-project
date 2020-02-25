package com.space.water.iot.api.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.space.water.iot.api.protocol.util.ByteUtil;

public class MeterValveControlCmd extends MeterBase {

    private byte action;

    private byte[] actionBytes = new byte[1]; // 阀门动作: 1字节, 开阀门：55H，关阀门：99H

    public MeterValveControlCmd(){}
    public MeterValveControlCmd(byte[] dataBytes){
        if (actionBytes.length < dataBytes.length){
            throw new RuntimeException("解析数据长度错误");
        }

        ByteUtil.arrayCopy(dataBytes, 0, actionBytes);
        action = ByteUtil.getByte(actionBytes);
    }

    public byte[] encodeBytes(){

        actionBytes[0] = action;

        return actionBytes;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("action", action)
                .toString();
    }
}
