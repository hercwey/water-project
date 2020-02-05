package com.learnbind.ai.iot.protocol;

import com.space.meter.protocol.bean.*;
import com.space.meter.protocol.util.HexStringUtils;
import com.space.meter.protocol.util.ProtoUtil;
import org.apache.tomcat.util.buf.HexUtils;

import java.util.ArrayList;
import java.util.List;

import static com.space.meter.protocol.Protocol.*;

/**
 * 报文编解码器, 解析和打包的总入口
 */
public class PacketCodec {

    /**
     * 解析帧信息
     * 只解析帧信息，不解析具体的数据
     * @param msgBytes 消息字节缓冲区
     * @return
     */
    public static PacketFrame decodeFrame(byte[] msgBytes) {

        // 生成帧对象
        PacketFrame packetFrame = new PacketFrame(msgBytes);

        // 校验报文头尾, 关键信息错误时报异常不继续执行
        if ((0x68 != packetFrame.getHead()) || (0x16 != packetFrame.getTail())){
            throw new RuntimeException("解析报文帧头帧尾错误");
        }

        // 校验和检查, 非关键信息, 只打印
        byte chk = ProtoUtil.calcChecksum(msgBytes, 0, msgBytes.length - 2);  //除最后两个字节外计算校验
        if (packetFrame.getChecksum() != chk) {
            System.out.println("***检验和检查不通过!***");
        }

        return packetFrame;
    }

    /**
     * 解析数据
     * @param packetFrame 帧
     * @return 返回null时为解析失败
     */
    public static MeterBase decodeData(PacketFrame packetFrame) {
        MeterBase obj = null;

        // 根据不同的数据标识, 解析成不同的数据对象
        switch (packetFrame.getDataDI()){
            case Protocol.DATA_DI_METER_REPORT: {
                obj = new MeterReport(packetFrame.getData());
                break;
            }
            case DATA_DI_METER_CONFIG_READ:{
                if (METER_CTR_0 == packetFrame.getCtrlCode()) {
                    obj = new MeterConfigReadCmd();
                } else if (METER_CTR_1 == packetFrame.getCtrlCode()){
                    obj = new MeterConfigReadResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_CONFIG_WRITE:{
                if (METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterConfigWriteCmd(packetFrame.getData());
                } else if (METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterConfigWriteResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_WATER: {
                if (METER_CTR_0 == packetFrame.getCtrlCode()) {
                    obj = new MeterReadWaterCmd();
                } else if (METER_CTR_1 == packetFrame.getCtrlCode()){
                    obj = new MeterReadWaterResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_VALVE_CTRL:{
                if (METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterValveControlCmd(packetFrame.getData());
                } else if (METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterValveControlResp();
                }
                break;
            }
            case Protocol.DATA_DI_METER_VOLUME_THRESHOLD:{
                if (METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterVolumeThresholdCmd(packetFrame.getData());
                } else if (METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterVolumeThresholdResp();
                }
                break;
            }
            default:{
                System.out.println("***错误的数据标识***");
            }
        }

        return obj;
    }

    /**
     * 打包数据
     * @param meterType
     * @param meterAddress
     * @param meterFactoryCode
     * @param ser
     * @param data
     * @return
     */
    public static byte[] encodeData(byte meterType, String meterAddress, String meterFactoryCode, byte ser, MeterBase data){
        PacketFrame packetFrame = new PacketFrame();

        // 设置表类型
        packetFrame.setMeterType(meterType);

        // 设置表地址
        packetFrame.setMeterAddr(meterAddress);

        // 设置表厂商
        packetFrame.setFactoryCode(meterFactoryCode);

        packetFrame.setSequence(ser);

        // 设置数据
        if (data instanceof MeterConfigReadCmd) {
            packetFrame.setCtrlCode(METER_CTR_0);
            packetFrame.setDataDI(DATA_DI_METER_CONFIG_READ);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterConfigReadResp) {
            packetFrame.setCtrlCode(METER_CTR_1);
            packetFrame.setDataDI(DATA_DI_METER_CONFIG_READ);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterConfigWriteCmd) {
            packetFrame.setCtrlCode(METER_CTR_3);
            packetFrame.setDataDI(DATA_DI_METER_CONFIG_WRITE);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterConfigWriteResp) {
            packetFrame.setCtrlCode(METER_CTR_4);
            packetFrame.setDataDI(DATA_DI_METER_CONFIG_WRITE);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterReadWaterCmd){
            packetFrame.setCtrlCode(METER_CTR_0);
            packetFrame.setDataDI(DATA_DI_METER_WATER);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterReadWaterResp){
            packetFrame.setCtrlCode(METER_CTR_1);
            packetFrame.setDataDI(DATA_DI_METER_WATER);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterReport){
            packetFrame.setCtrlCode(METER_CTR_1);
            packetFrame.setDataDI(DATA_DI_METER_REPORT);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterValveControlCmd){
            packetFrame.setCtrlCode(METER_CTR_3);
            packetFrame.setDataDI(DATA_DI_METER_VALVE_CTRL);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterValveControlResp){
            packetFrame.setCtrlCode(METER_CTR_4);
            packetFrame.setDataDI(DATA_DI_METER_VALVE_CTRL);
            packetFrame.setData(data.encodeBytes());
        }else if (data instanceof MeterVolumeThresholdCmd){
            packetFrame.setCtrlCode(METER_CTR_3);
            packetFrame.setDataDI(DATA_DI_METER_VOLUME_THRESHOLD);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterVolumeThresholdResp){
            packetFrame.setCtrlCode(METER_CTR_4);
            packetFrame.setDataDI(DATA_DI_METER_VOLUME_THRESHOLD);
            packetFrame.setData(data.encodeBytes());
        }

        return packetFrame.encodeBytes();
    }

}
