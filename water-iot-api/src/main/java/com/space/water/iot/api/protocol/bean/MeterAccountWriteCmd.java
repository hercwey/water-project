package com.space.water.iot.api.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.space.water.iot.api.protocol.util.ByteUtil;

public class MeterAccountWriteCmd extends MeterBase {

    public static final byte ACCOUNT_OPEN             = (byte)0xAA;   // 已开户
    public static final byte ACCOUNT_CLOSE            = (byte)0x55;   // 未开户

    private byte action;

    private byte[] actionBytes = new byte[1]; // 水表开户指令: 1字节, AAH:已开户, 55H：未开户

    public MeterAccountWriteCmd(){}

    /**
     * 构造函数解析消息
     * @param dataBytes
     */
    public MeterAccountWriteCmd(byte[] dataBytes){
        if (actionBytes.length < dataBytes.length){
            throw new RuntimeException("解析数据长度错误");
        }

        ByteUtil.arrayCopy(dataBytes, 0, actionBytes);
        action = ByteUtil.getByte(actionBytes);
    }

    /**
     * 封包
     * @return
     */
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
