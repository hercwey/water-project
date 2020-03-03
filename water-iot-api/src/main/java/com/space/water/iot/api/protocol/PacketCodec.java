package com.space.water.iot.api.protocol;

import static com.space.water.iot.api.protocol.Protocol.*;

import com.space.water.iot.api.protocol.bean.MeterAccountReadCmd;
import com.space.water.iot.api.protocol.bean.MeterAccountReadResp;
import com.space.water.iot.api.protocol.bean.MeterAccountWriteCmd;
import com.space.water.iot.api.protocol.bean.MeterAccountWriteResp;
import com.space.water.iot.api.protocol.bean.MeterBase;
import com.space.water.iot.api.protocol.bean.MeterConfigReadCmd;
import com.space.water.iot.api.protocol.bean.MeterConfigReadResp;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteCmd;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteResp;
import com.space.water.iot.api.protocol.bean.MeterReadWaterCmd;
import com.space.water.iot.api.protocol.bean.MeterReadWaterResp;
import com.space.water.iot.api.protocol.bean.MeterReport;
import com.space.water.iot.api.protocol.bean.MeterValveControlCmd;
import com.space.water.iot.api.protocol.bean.MeterValveControlResp;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdCmd;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdResp;
import com.space.water.iot.api.protocol.util.ProtoUtil;
import com.space.water.iot.api.util.LogUtil;
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
            LogUtil.error("***检验和检查不通过!***");
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
            case Protocol.DATA_DI_METER_ACCOUNT_READ:{
                if (METER_CTR_0 == packetFrame.getCtrlCode()) {
                    obj = new MeterAccountReadCmd();
                } else if (METER_CTR_1 == packetFrame.getCtrlCode()){
                    obj = new MeterAccountReadResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_ACCOUNT_WRITE:{
                if (METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterAccountWriteCmd(packetFrame.getData());
                } else if (METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterAccountWriteResp();
                }
                break;
            }
            default:{
                LogUtil.error("***错误的数据标识***");
            }
        }

        return obj;
    }

    /**
     * 打包数据
     * @param meterType         使用Protocol.METER_TYPE_* 类型
     * @param meterAddress      长度为10的数字型字符串
     * @param meterFactoryCode  长度为4的数字型字符串
     * @param ser               序列号, 主站发送的序号SER，在每次通讯前，按模256加1运算后产生
     * @param data              bean下的对象
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

        // 设置数据, 根据对象实例填充 控制码
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
        }else if (data instanceof MeterAccountReadCmd){
            packetFrame.setCtrlCode(METER_CTR_0);
            packetFrame.setDataDI(DATA_DI_METER_ACCOUNT_READ);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterAccountReadResp){
            packetFrame.setCtrlCode(METER_CTR_1);
            packetFrame.setDataDI(DATA_DI_METER_ACCOUNT_READ);
            packetFrame.setData(data.encodeBytes());
        }else if (data instanceof MeterAccountWriteCmd){
            packetFrame.setCtrlCode(METER_CTR_3);
            packetFrame.setDataDI(DATA_DI_METER_ACCOUNT_WRITE);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterAccountWriteResp){
            packetFrame.setCtrlCode(METER_CTR_4);
            packetFrame.setDataDI(DATA_DI_METER_ACCOUNT_WRITE);
            packetFrame.setData(data.encodeBytes());
        }

        // 打包生成字节缓冲区
        return packetFrame.encodeBytes();
    }

}
