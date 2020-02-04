package com.learnbind.ai.iot.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.buf.HexUtils;

import com.learnbind.ai.iot.protocol.bean.MeterBase;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.bean.MeterConfigReadCmd;
import com.learnbind.ai.iot.protocol.bean.MeterConfigReadResp;
import com.learnbind.ai.iot.protocol.bean.MeterConfigWriteCmd;
import com.learnbind.ai.iot.protocol.bean.MeterConfigWriteResp;
import com.learnbind.ai.iot.protocol.bean.MeterReadWaterCmd;
import com.learnbind.ai.iot.protocol.bean.MeterReadWaterResp;
import com.learnbind.ai.iot.protocol.bean.MeterReport;
import com.learnbind.ai.iot.protocol.bean.MeterValveControlCmd;
import com.learnbind.ai.iot.protocol.bean.MeterValveControlResp;
import com.learnbind.ai.iot.protocol.bean.MeterVolumeThresholdCmd;
import com.learnbind.ai.iot.protocol.bean.MeterVolumeThresholdResp;

/**
 * 报文编解码器
 */
public class PacketCodec {

    public static PacketFrame decodeFrame(byte[] msgBytes) {

        // 生成帧对象
        PacketFrame packetFrame = new PacketFrame(msgBytes);

        // 报文校验: 检查校验和
        byte chk = Protocol.calcChecksum(msgBytes, 0, msgBytes.length - 2);  //除最后两个字节外计算校验
        if (packetFrame.getChecksum() == chk) {
            System.out.println("检验和校验通过!");
        } else {
            System.out.println("***检验和检查不通过!***");
        }

        return packetFrame;
    }

    public static MeterBase decodeData(PacketFrame packetFrame) {
        MeterBase obj = null;

        // 根据不同的数据标识, 解析成不同的数据对象
        switch (packetFrame.getDataDI()){
            case Protocol.DATA_DI_METER_REPORT: {
                obj = new MeterReport(packetFrame.getData());
                break;
            }
            case Protocol.DATA_DI_METER_CONFIG_READ:{
                if (Protocol.METER_CTR_0 == packetFrame.getCtrlCode()) {
                    obj = new MeterConfigReadCmd();
                } else if (Protocol.METER_CTR_1 == packetFrame.getCtrlCode()){
                    obj = new MeterConfigReadResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_CONFIG_WRITE:{
                if (Protocol.METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterConfigWriteCmd(packetFrame.getData());
                } else if (Protocol.METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterConfigWriteResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_WATER: {
                if (Protocol.METER_CTR_0 == packetFrame.getCtrlCode()) {
                    obj = new MeterReadWaterCmd();
                } else if (Protocol.METER_CTR_1 == packetFrame.getCtrlCode()){
                    obj = new MeterReadWaterResp(packetFrame.getData());
                }
                break;
            }
            case Protocol.DATA_DI_METER_VALVE_CTRL:{
                if (Protocol.METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterValveControlCmd(packetFrame.getData());
                } else if (Protocol.METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterValveControlResp();
                }
                break;
            }
            case Protocol.DATA_DI_METER_VOLUME_THRESHOLD:{
                if (Protocol.METER_CTR_3 == packetFrame.getCtrlCode()) {
                    obj = new MeterVolumeThresholdCmd(packetFrame.getData());
                } else if (Protocol.METER_CTR_4 == packetFrame.getCtrlCode()){
                    obj = new MeterVolumeThresholdResp();
                }
                break;
            }
        }

        return obj;
    }

    public static byte[] encode(int meterType, String meterAddress, String meterFactoryCode, byte ser, MeterBase data){
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
            packetFrame.setCtrlCode(Protocol.METER_CTR_0);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_CONFIG_READ);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterConfigReadResp) {
            packetFrame.setCtrlCode(Protocol.METER_CTR_1);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_CONFIG_READ);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterConfigWriteCmd) {
            packetFrame.setCtrlCode(Protocol.METER_CTR_3);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_CONFIG_WRITE);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterConfigWriteResp) {
            packetFrame.setCtrlCode(Protocol.METER_CTR_4);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_CONFIG_WRITE);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterReadWaterCmd){
            packetFrame.setCtrlCode(Protocol.METER_CTR_0);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_WATER);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterReadWaterResp){
            packetFrame.setCtrlCode(Protocol.METER_CTR_1);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_WATER);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterReport){
            packetFrame.setCtrlCode(Protocol.METER_CTR_1);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_REPORT);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterValveControlCmd){
            packetFrame.setCtrlCode(Protocol.METER_CTR_3);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_VALVE_CTRL);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterValveControlResp){
            packetFrame.setCtrlCode(Protocol.METER_CTR_4);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_VALVE_CTRL);
            packetFrame.setData(data.encodeBytes());
        }else if (data instanceof MeterVolumeThresholdCmd){
            packetFrame.setCtrlCode(Protocol.METER_CTR_3);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_VOLUME_THRESHOLD);
            packetFrame.setData(data.encodeBytes());
        } else if (data instanceof MeterVolumeThresholdResp){
            packetFrame.setCtrlCode(Protocol.METER_CTR_4);
            packetFrame.setDataDI(Protocol.DATA_DI_METER_VOLUME_THRESHOLD);
            packetFrame.setData(data.encodeBytes());
        }

        return packetFrame.encodeBytes();
    }

    private static void unitTestValveControl() {

        System.out.println("测试阀门控制");

        /////////////////////////////////////////////////////////////////////////
        // [主站] 申请命令对象, 设置阀门开阀命令
        MeterValveControlCmd valveCmd = new MeterValveControlCmd();
        valveCmd.setAction(MeterValveControlCmd.VALVE_OPEN);

        // [主站] 封装为字节消息
        byte[] packetBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x01,
                valveCmd);

        // [主站] 发送 ...
        System.out.println("[主站] 发送数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [从站] 接收 解析字节消息
        PacketFrame packetFrame = new PacketFrame(packetBytes);
        MeterValveControlCmd recvCmd = (MeterValveControlCmd)decodeData(packetFrame);
        System.out.println("[从站] 接收到消息帧{" + packetFrame.toString() + "}");
        System.out.println("[从站] 接收到消息体{" + recvCmd.toString() + "}");

        // [从站] 封装应答消息
        MeterValveControlResp response = new MeterValveControlResp();

        // [从站] 封装为字节消息
        byte[] respBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x01,
                response);
        System.out.println("[从站] 发送应答数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [主站] 接收并解析消息
        PacketFrame packetFrame2 = new PacketFrame(respBytes);
        MeterValveControlResp respMsg = (MeterValveControlResp)decodeData(packetFrame2);

        System.out.println("[主站] 接收应答帧:{" + packetFrame2.toString() +"}");
        System.out.println("[主站] 接收应答体:{" + respMsg.toString() +"}");
    }

    private static void unitTestReport() {

        System.out.println("测试数据上报");

        // [从站] 封装应答消息
        MeterReport report = new MeterReport();
        report.setMeterNumber("123456123456");  // 表号为6*2=12个字节长度的字符串
        report.setMeterTime("20200101235959");  // 时间为7*2=14个字节长度的字符串
        report.setTotalVolume(23);
        report.setSampleUnit(MeterConfig.SAMPLE_UNIT_1M);
        report.setBatteryVoltage("12");
        report.setMeterStatus((short)1);
        report.setSignal((byte)99);
        report.setPressure(5.5f);

        // [从站] 封装为字节消息
        byte[] reportBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x02,
                report);
        System.out.println("[从站] 发送数据:[" + HexUtils.toHexString(reportBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [主站] 接收并解析消息
        PacketFrame packetFrame2 = new PacketFrame(reportBytes);
        MeterReport respMsg = (MeterReport)decodeData(packetFrame2);

        System.out.println("[主站] 接收数据帧:{" + packetFrame2.toString() +"}");
        System.out.println("[主站] 接收数据体:{" + respMsg.toString() +"}");

        System.out.println("======================================");
    }

    private static void unitTestReadConfig() {
        System.out.println("测试读取配置");

        /////////////////////////////////////////////////////////////////////////
        // [主站] 申请命令对象
        MeterConfigReadCmd command = new MeterConfigReadCmd();

        // [主站] 封装为字节消息
        byte[] packetBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x02,
                command);

        // [主站] 发送 ...
        System.out.println("[主站] 发送数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [从站] 接收 解析字节消息
        PacketFrame packetFrame = new PacketFrame(packetBytes);
        MeterConfigReadCmd recvCmd = (MeterConfigReadCmd)decodeData(packetFrame);
        System.out.println("[从站] 接收到消息帧{" + packetFrame.toString() + "}");
        System.out.println("[从站] 接收到消息体{" + recvCmd.toString() + "}");

        // [从站] 封装应答消息
        MeterConfigReadResp cmdResp = new MeterConfigReadResp();
        cmdResp.setPeriod((short)60);// 定时上传周期
        cmdResp.setPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
        cmdResp.setMaxReport((short)100);// 定量上传值
        cmdResp.setEmergencyTime((byte)2);// 用户临时开阀用水限定时间
        cmdResp.setValveTime((byte)3);// 阀门行程时间
        cmdResp.setValveMaintainTime((short) 30);// 阀门维护周期
        cmdResp.setMeterBasicValue(3000);// 表底数 TODO 数据类型
        cmdResp.setSampleUnit(MeterConfig.SAMPLE_UNIT_1M); // 采样参数单位
        cmdResp.setMeterNumber("123456123456"); // 表号
        cmdResp.setMeterTime("20200101235959"); // 表当前时间

        // 表状态字
        cmdResp.setMeterStatus((short)(MeterConfig.METER_STATUS_VALVE_OPEN |
                MeterConfig.METER_STATUS_PERIOD_ON |
                MeterConfig.METER_STATUS_MAX_REPORT_ON |
                MeterConfig.METER_STATUS_MAGNETIC_ON ));
        cmdResp.setServerIp("10.88.192.11");
        cmdResp.setServerPort((short) 6538);


        // [从站] 封装为字节消息
        byte[] respBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x01,
                cmdResp);
        System.out.println("[从站] 发送应答数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [主站] 接收并解析消息
        PacketFrame packetFrame2 = new PacketFrame(respBytes);
        MeterConfigReadResp respMsg = (MeterConfigReadResp)decodeData(packetFrame2);

        System.out.println("[主站] 接收应答帧:{" + packetFrame2.toString() +"}");
        System.out.println("[主站] 接收应答体:{" + respMsg.toString() +"}");
    }

    private static void unitTestWriteConfig() {
        System.out.println("测试下发写配置");

        /////////////////////////////////////////////////////////////////////////
        // [主站] 申请命令对象
        MeterConfig meterConfig = new MeterConfig();
        meterConfig.setPeriod((short)60);// 定时上传周期
        meterConfig.setPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
        meterConfig.setMaxReport((short)100);// 定量上传值
        meterConfig.setEmergencyTime((byte)2);// 用户临时开阀用水限定时间
        meterConfig.setValveTime((byte)3);// 阀门行程时间
        meterConfig.setValveMaintainTime((short) 30);// 阀门维护周期
        meterConfig.setMeterBasicValue(3000);// 表底数 TODO 数据类型
        meterConfig.setSampleUnit(MeterConfig.SAMPLE_UNIT_1M); // 采样参数单位
        meterConfig.setMeterNumber("123456123456"); // 表号
        meterConfig.setMeterTime("20200101235959"); // 表当前时间
        meterConfig.setServerIp("10.88.192.11");
        meterConfig.setServerPort((short) 6538);

        MeterConfigWriteCmd command = new MeterConfigWriteCmd();
        command.setConfigFlag((short)(MeterConfig.FLAG_PERIOD |
                MeterConfig.FLAG_PERIOD |
                MeterConfig.FLAG_PERIOD_UNIT |
                MeterConfig.FLAG_MAX_REPORT |
                MeterConfig.FLAG_EMERG_TIME |
                MeterConfig.FLAG_VALVE_TIME |
                MeterConfig.FLAG_VALVE_MAINTAIN_TIME |
                MeterConfig.FLAG_WATER_VLOUME |
                MeterConfig.FLAG_SAMPLE_UNIT |
                MeterConfig.FLAG_METER_NUM |
                MeterConfig.FLAG_METER_TIME |
                MeterConfig.FLAG_SERVER_IP |
                MeterConfig.FLAG_SERVER_PORT));
        command.setConfig(meterConfig);

        // [主站] 封装为字节消息
        byte[] packetBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x02,
                command);

        // [主站] 发送 ...
        System.out.println("[主站] 发送数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [从站] 接收 解析字节消息
        PacketFrame packetFrame = new PacketFrame(packetBytes);
        MeterConfigWriteCmd recvCmd = (MeterConfigWriteCmd)decodeData(packetFrame);
        System.out.println("[从站] 接收到消息帧{" + packetFrame.toString() + "}");
        System.out.println("[从站] 接收到消息体{" + recvCmd.toString() + "}");

        // [从站] 封装应答消息
        MeterConfigWriteResp cmdResp = new MeterConfigWriteResp();
        cmdResp.setPeriod((short)60);// 定时上传周期
        cmdResp.setPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
        cmdResp.setMaxReport((short)100);// 定量上传值
        cmdResp.setEmergencyTime((byte)2);// 用户临时开阀用水限定时间
        cmdResp.setValveTime((byte)3);// 阀门行程时间
        cmdResp.setValveMaintainTime((short) 30);// 阀门维护周期
        cmdResp.setMeterBasicValue(3000);// 表底数 TODO 数据类型
        cmdResp.setSampleUnit(MeterConfig.SAMPLE_UNIT_1M); // 采样参数单位
        cmdResp.setMeterNumber("123456123456"); // 表号
        cmdResp.setMeterTime("20200101235959"); // 表当前时间

        // 表状态字
        cmdResp.setMeterStatus((short)(MeterConfig.METER_STATUS_VALVE_OPEN |
                MeterConfig.METER_STATUS_PERIOD_ON |
                MeterConfig.METER_STATUS_MAX_REPORT_ON |
                MeterConfig.METER_STATUS_MAGNETIC_ON ));
        cmdResp.setServerIp("10.88.192.11");
        cmdResp.setServerPort((short) 6538);


        // [从站] 封装为字节消息
        byte[] respBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x01,
                cmdResp);
        System.out.println("[从站] 发送应答数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [主站] 接收并解析消息
        PacketFrame packetFrame2 = new PacketFrame(respBytes);
        MeterConfigWriteResp respMsg = (MeterConfigWriteResp)decodeData(packetFrame2);

        System.out.println("[主站] 接收应答帧:{" + packetFrame2.toString() +"}");
        System.out.println("[主站] 接收应答体:{" + respMsg.toString() +"}");
    }

    private static void unitTestReadWaterVolume() {
        System.out.println("测试冻结水量");

        /////////////////////////////////////////////////////////////////////////
        // [主站] 申请命令对象
        MeterReadWaterCmd command = new MeterReadWaterCmd();

        // [主站] 封装为字节消息
        byte[] packetBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x02,
                command);

        // [主站] 发送 ...
        System.out.println("[主站] 发送数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [从站] 接收 解析字节消息
        PacketFrame packetFrame = new PacketFrame(packetBytes);
        MeterReadWaterCmd recvCmd = (MeterReadWaterCmd)decodeData(packetFrame);
        System.out.println("[从站] 接收到消息帧{" + packetFrame.toString() + "}");
        System.out.println("[从站] 接收到消息体{" + recvCmd.toString() + "}");

        // [从站] 封装应答消息
        MeterReadWaterResp cmdResp = new MeterReadWaterResp();
        cmdResp.setMeterNumber("123456123456"); // 采样单位
        cmdResp.setSampleUnit(MeterConfig.SAMPLE_UNIT_1M); // 采样参数单位

        List<MeterReadWaterResp.WaterVolume> listVolume = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            MeterReadWaterResp.WaterVolume volume = cmdResp.new WaterVolume();
            int m = i+1;
            if (m < 10) {
                volume.date = "20200" + m; //TODO 年月
            } else {
                volume.date = "2020" + m;
            }
            volume.volume = 100 + i;

            listVolume.add(volume);
        }
        cmdResp.setListWater(listVolume);

        // [从站] 封装为字节消息
        byte[] respBytes = encode(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x01,
                cmdResp);
        System.out.println("[从站] 发送应答数据:[" + HexUtils.toHexString(packetBytes)+"]");

        /////////////////////////////////////////////////////////////////////////

        // [主站] 接收并解析消息
        PacketFrame packetFrame2 = new PacketFrame(respBytes);
        MeterReadWaterResp respMsg = (MeterReadWaterResp)decodeData(packetFrame2);

        System.out.println("[主站] 接收应答帧:{" + packetFrame2.toString() +"}");
        System.out.println("[主站] 接收应答体:{" + respMsg.toString() +"}");

    }

    // 单元测试
    public static void main(String[] args) {
        //unitTestValveControl();
        //unitTestReport();
        //unitTestReadConfig();
        //unitTestWriteConfig();
        unitTestReadWaterVolume();
    }
}
