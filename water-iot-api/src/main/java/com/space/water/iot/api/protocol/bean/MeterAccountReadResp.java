package com.space.water.iot.api.protocol.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.space.water.iot.api.protocol.util.ByteUtil;

public class MeterAccountReadResp extends MeterBase {
    public static final byte ACCOUNT_OPEN             = (byte)0xAA;   // 已开户
    public static final byte ACCOUNT_CLOSE            = (byte)0x55;   // 未开户

    private byte status; // 开户状态

    /* 内部使用 */
    private byte[] statusBytes = new byte[1]; // 开户状态字节：0xAA 已开户, 0x55 未开户

    public MeterAccountReadResp(){}

    /**
     * 构造函数解析
     * @param dataBytes
     */
    public MeterAccountReadResp(byte[] dataBytes){
        if (statusBytes.length < dataBytes.length){
            throw new RuntimeException("解析数据长度错误");
        }

        ByteUtil.arrayCopy(dataBytes, 0, statusBytes);
        status = ByteUtil.getByte(statusBytes);
    }

    /**
     * 封包
     * @return
     */
    public byte[] encodeBytes(){
        statusBytes[0] = status;
        return statusBytes;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("status", status)
                .toString();
    }
}
