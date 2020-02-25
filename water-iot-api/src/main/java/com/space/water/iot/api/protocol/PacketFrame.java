package com.space.water.iot.api.protocol;

import static com.space.water.iot.api.protocol.Protocol.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.space.water.iot.api.protocol.util.ByteUtil;
import com.space.water.iot.api.protocol.util.HexStringUtils;
import com.space.water.iot.api.protocol.util.ProtoUtil;

/**
 * CJ-T188数据帧结构
 */
public class PacketFrame {

    /* 对外接口成员 */
    private byte head;              // 帧头
    private byte meterType;         // 表类型
    private String meterAddr;       // 表地址
    private String factoryCode;     // 表厂商代码
    private byte ctrlCode;          // 控制码
    private short dataDI;           // 数据标识
    private String dataDIStr;           // 数据标识Str
    private int sequence;           // 序列号
    private byte[] data;            // 数据字节
    private byte checksum;          // 校验和
    private byte tail;              // 帧尾

    public PacketFrame(){ }

    public PacketFrame(byte[] msgBytes){
        byte[] soi  = new byte[1];      // 帧起始符 1字节, 68H
        byte[] type = new byte[1];      // 表类型   1字节
        byte[] addr = new byte[5];      // 地址域（A0～A6）由七个字节组成，每个字节为 2 位 BCD 码格式。地址长度为 14 位十进制数，低地址在前，高地址在后。其中 A5、A6 为厂商代码
        byte[] factory = new byte[2];   // 地址域（A0～A6）由七个字节组成，每个字节为 2 位 BCD 码格式。地址长度为 14 位十进制数，低地址在前，高地址在后。其中 A5、A6 为厂商代码
        byte[] cmd  = new byte[1];      // 控制码   1字节
        byte[] len  = new byte[1];      // 数据域长度 1字节
        // begin of 数据域字段
        byte[] di   = new byte[2];      // 数据域: 数据标识 2字节 (cmd为CRT_0,CRT_1,CRT_3,CRT_4 有效)
        byte[] seq  = new byte[1];      // 数据域: 序列号 1字节
        byte[] st   = new byte[1];      // 数据域: 异常状态 1字节 (cmd为CRT_2,CRT_5 有效)
        // end of 数据域字段
        byte[] chk  = new byte[1];      // 校验码  校验码（CS）为一个字节，从帧起始符开始到校验码之前的所有各字节进行二进制算术累加，不计超过 FFH 的溢出值
        byte[] eoi  = new byte[1];      // 帧结束符 1字节 16H

        // 从字节流中拷贝内存到字节数组
        int pos = 0;
        pos += ByteUtil.arrayCopy(msgBytes, pos, soi);
        pos += ByteUtil.arrayCopy(msgBytes, pos, type);
        pos += ByteUtil.arrayCopy(msgBytes, pos, addr);
        pos += ByteUtil.arrayCopy(msgBytes, pos, factory);
        pos += ByteUtil.arrayCopy(msgBytes, pos, cmd);
        pos += ByteUtil.arrayCopy(msgBytes, pos, len);

        // 根据控制码的不同, 获取数据区
        ctrlCode = ByteUtil.getByte(cmd);
        if ((METER_CTR_2 == ctrlCode) || (METER_CTR_5 == ctrlCode)){
            pos += ByteUtil.arrayCopy(msgBytes, pos, seq);
            pos += ByteUtil.arrayCopy(msgBytes, pos, st);
        } else {
            pos += ByteUtil.arrayCopy(msgBytes, pos, di);
            pos += ByteUtil.arrayCopy(msgBytes, pos, seq);
            
            // 计算数据区的长度
            int dataLen = ByteUtil.getInt(len) - di.length - seq.length;
            this.data = new byte[dataLen];
            pos += ByteUtil.arrayCopy(msgBytes, pos, this.data);
        }

        pos += ByteUtil.arrayCopy(msgBytes, pos, chk);
        pos += ByteUtil.arrayCopy(msgBytes, pos, eoi);

        // 根据字节数组解析为成员
        meterType = ProtoUtil.parseMeterType(type);
        meterAddr = ProtoUtil.parseMeterAddr(addr);
        factoryCode = ProtoUtil.parseFactoryCode(factory);

        sequence = ByteUtil.getInt(seq);
        if ((METER_CTR_2 == ctrlCode) || (METER_CTR_5 == ctrlCode)) {
            
        } else {
            dataDI = ByteUtil.getShort(di);
            dataDIStr = HexStringUtils.bytesToHexString(di);
        }

        checksum = ByteUtil.getByte(chk);
        head = soi[0];
        tail = eoi[0];
    }

    public byte[] encodeBytes(){
        byte[] soi  = new byte[1];      // 帧起始符 1字节, 68H
        byte[] type = new byte[1];      // 表类型   1字节
        byte[] addr = new byte[5];      // 地址域（A0～A6）由七个字节组成，每个字节为 2 位 BCD 码格式。地址长度为 14 位十进制数，低地址在前，高地址在后。其中 A5、A6 为厂商代码
        byte[] factory = new byte[2];   // 地址域（A0～A6）由七个字节组成，每个字节为 2 位 BCD 码格式。地址长度为 14 位十进制数，低地址在前，高地址在后。其中 A5、A6 为厂商代码
        byte[] cmd  = new byte[1];      // 控制码   1字节
        byte[] len  = new byte[1];      // 数据域长度 1字节
        // begin of 数据域字段
        byte[] di   = new byte[2];      // 数据域: 数据标识 2字节 (cmd为CRT_0,CRT_1,CRT_3,CRT_4 有效)
        byte[] seq  = new byte[1];      // 数据域: 序列号 1字节
        byte[] st   = new byte[1];      // 数据域: 异常状态 1字节 (cmd为CRT_2,CRT_5 有效)
        // end of 数据域字段
        byte[] chk  = new byte[1];      // 校验码  校验码（CS）为一个字节，从帧起始符开始到校验码之前的所有各字节进行二进制算术累加，不计超过 FFH 的溢出值
        byte[] eoi  = new byte[1];      // 帧结束符 1字节 16H
        
        soi[0] = 0x68;                                      // 设置帧头
        ProtoUtil.packMeterType(type, meterType);           // 设置表类型
        ProtoUtil.packMeterAddr(addr, meterAddr);           // 设置表地址
        ProtoUtil.packFactoryCode(factory, factoryCode);    // 设置厂商代码
        ByteUtil.setBytes(cmd, (byte)ctrlCode);             // 设置控制码

        // 拼接帧前半部
        byte[] dataBytes0 = ByteUtil.concatAll(soi, type, addr, factory, cmd);

        // 设置报文体
        int dataLen = 0;
        if ((METER_CTR_2 == ctrlCode) || (METER_CTR_5 == ctrlCode)) {
            ByteUtil.setBytes(seq, (byte)sequence);
            dataLen = seq.length + st.length;
        }else{
            ByteUtil.setBytes(di, (short)dataDI);
            ByteUtil.setBytes(seq, (byte)sequence);
            if ((null != this.data) && (this.data.length > 0)) {
                dataLen = di.length + seq.length + this.data.length;
            }else{
                dataLen = di.length + seq.length;
            }
        }
        
        ByteUtil.setBytes(len, (byte)dataLen);
        
        byte[] dataBytes1 = null;
        if ((METER_CTR_2 == ctrlCode) || (METER_CTR_5 == ctrlCode)) {
            dataBytes1 = ByteUtil.concatAll(len, seq, st);
        } else {
            if ((null != this.data) && (this.data.length > 0)) {
                dataBytes1 = ByteUtil.concatAll(len, di, seq, this.data);
            }else{
                dataBytes1 = ByteUtil.concatAll(len, di, seq);
            }
        }
        
        // 计算校验和
        byte[] dataBytes2 = ByteUtil.concatAll(dataBytes0, dataBytes1);
        byte chksum = ProtoUtil.calcChecksum(dataBytes2, 0, dataBytes2.length);
        
        chk[0] = chksum;
        eoi[0] = 0x16;

        return ByteUtil.concatAll(dataBytes2, chk, eoi);
    }

    public int getMeterType() {
        return meterType;
    }

    public void setMeterType(byte meterType) {
        this.meterType = meterType;
    }

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public int getCtrlCode() {
        return ctrlCode;
    }

    public void setCtrlCode(byte ctrlCode) {
        this.ctrlCode = ctrlCode;
    }
    
    public String getCtrlCodeStr() {
    	int i = ctrlCode;	
    	i = i&0xff;
    	return Integer.toHexString(i);
    }

    public short getDataDI() {
        return dataDI;
    }
    public String getDataDiStr() {
    	return dataDIStr;
    }

    public void setDataDI(short dataDI) {
        this.dataDI = dataDI;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getChecksum() {
        return checksum;
    }

    public byte getHead() {
        return head;
    }

    public byte getTail() {
        return tail;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("meterType", meterType)
                .append("meterAddr", meterAddr)
                .append("factoryCode", factoryCode)
                .append("ctrlCode", ctrlCode)
                .append("dataDI", dataDI)
                .append("sequence", sequence)
                .append("data", data)
                .append("checksum", checksum)
                .toString();
    }
}
