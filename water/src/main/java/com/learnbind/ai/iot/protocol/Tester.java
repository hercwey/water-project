package com.learnbind.ai.iot.protocol;

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
import com.learnbind.ai.iot.protocol.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;
import static com.learnbind.ai.iot.protocol.PacketCodec.decodeData;
import static com.learnbind.ai.iot.protocol.PacketCodec.encodeData;

public class Tester {

    private static void unitTestValveControl() {

        System.out.println("测试阀门控制");

        /////////////////////////////////////////////////////////////////////////
        // [主站] 申请命令对象, 设置阀门开阀命令
        MeterValveControlCmd valveCmd = new MeterValveControlCmd();
        valveCmd.setAction(MeterValveControlCmd.VALVE_OPEN);

        // [主站] 封装为字节消息
        byte[] packetBytes = encodeData(Protocol.METER_TYPE_10H,
                "1234567890",
                "1234",
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
        byte[] respBytes = encodeData(Protocol.METER_TYPE_10H,
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
        report.setSampleUnit(Protocol.SAMPLE_UNIT_1M);
        report.setBatteryVoltage(12);
        report.setMeterStatus((short)1);
        report.setSignal("99");
        report.setPressure(5.5f);

        // [从站] 封装为字节消息
        byte[] reportBytes = encodeData(Protocol.METER_TYPE_10H,
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
        byte[] packetBytes = encodeData(Protocol.METER_TYPE_10H,
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
        cmdResp.setReportPeriod((short)60);// 定时上传周期
        cmdResp.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
        cmdResp.setReportRation((short)100);// 定量上传值
        cmdResp.setTemporaryTime((byte)2);// 用户临时开阀用水限定时间
        cmdResp.setValveRunTime((byte)3);// 阀门行程时间
        cmdResp.setValveMaintainPeriod((short) 30);// 阀门维护周期
        cmdResp.setMeterBasicValue(3000);// 表底数 TODO 数据类型
        cmdResp.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位
        cmdResp.setMeterNumber("123456123456"); // 表号
        cmdResp.setMeterTime("20200101235959"); // 表当前时间

        // 表状态字
        cmdResp.setMeterStatusFlag((short)(Protocol.METER_STATUS_VALVE_OPEN |
                Protocol.METER_STATUS_PERIOD_ON |
                Protocol.METER_STATUS_MAX_REPORT_ON |
                Protocol.METER_STATUS_MAGNETIC_ON ));
        cmdResp.setServerIp("10.88.192.11");
        cmdResp.setServerPort((short) 6538);


        // [从站] 封装为字节消息
        byte[] respBytes = encodeData(Protocol.METER_TYPE_10H,
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
        // [主站] 【1】 申请命令对象
        MeterConfig meterConfig = new MeterConfig();
        meterConfig.setReportPeriod((short)60);                         // 定时上传周期
        meterConfig.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);   // 定时上传周期单位
        meterConfig.setReportRation((short)100);                        // 定量上传值
        meterConfig.setTemporaryTime((byte)2);                          // 用户临时开阀用水限定时间
        meterConfig.setValveRunTime((byte)3);                           // 阀门行程时间
        meterConfig.setValveMaintainPeriod((short) 30);                 // 阀门维护周期
        meterConfig.setMeterBasicValue(3000);                           // 表底数
        meterConfig.setSampleUnit(Protocol.SAMPLE_UNIT_1M);             // 采样参数单位
        meterConfig.setMeterNumber("123456123456");                     // 表号
        meterConfig.setMeterTime("20200101235959");                     // 表当前时间
        meterConfig.setServerIp("10.88.192.11");                        // 设置IP地址
        meterConfig.setServerPort((short) 6538);                        // 设置端口号

        //设置表状态字
        meterConfig.setMeterStatusFlag((short)(Protocol.METER_STATUS_MAGNETIC_ON |
                ~Protocol.METER_STATUS_VALVE_OPEN |
                ~Protocol.METER_STATUS_PERIOD_ON));

        // [主站] 【2】 申请写表配置数据对象
        MeterConfigWriteCmd command = new MeterConfigWriteCmd();

        // 设置 参数修改标识(修改了哪些参数，就设置哪些flag)
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
                MeterConfig.FLAG_METER_STATUS |
                MeterConfig.FLAG_SERVER_IP |
                MeterConfig.FLAG_SERVER_PORT));
        command.setConfig(meterConfig);

        // [主站] 【3】 封装为字节消息
        byte[] packetBytes = encodeData(Protocol.METER_TYPE_10H,
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
        cmdResp.setReportPeriod((short)60);// 定时上传周期
        cmdResp.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
        cmdResp.setReportRation((short)100);// 定量上传值
        cmdResp.setTemporaryTime((byte)2);// 用户临时开阀用水限定时间
        cmdResp.setValveRunTime((byte)3);// 阀门行程时间
        cmdResp.setValveMaintainPeriod((short) 30);// 阀门维护周期
        cmdResp.setMeterBasicValue(3000);// 表底数 TODO 数据类型
        cmdResp.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位
        cmdResp.setMeterNumber("123456123456"); // 表号
        cmdResp.setMeterTime("20200101235959"); // 表当前时间

        // 表状态字
        cmdResp.setMeterStatusFlag((short)(Protocol.METER_STATUS_VALVE_OPEN |
                Protocol.METER_STATUS_PERIOD_ON |
                Protocol.METER_STATUS_MAX_REPORT_ON |
                Protocol.METER_STATUS_MAGNETIC_ON ));
        cmdResp.setServerIp("10.88.192.11");
        cmdResp.setServerPort((short) 6538);


        // [从站] 封装为字节消息
        byte[] respBytes = encodeData(Protocol.METER_TYPE_10H,
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
        byte[] packetBytes = encodeData(Protocol.METER_TYPE_10H,
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
        cmdResp.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位

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
        byte[] respBytes = encodeData(Protocol.METER_TYPE_10H,
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

    /**********************************************************************
     * 接收到字节缓冲区的处理流程
     **********************************************************************/
    public static void ST_ReceiveByteBuffer(){

        /* ========== begin of 报文数据 ============ */
        //String msg = "681091002809191100811d1f90009100280919110000002806121906000000023236595d130000002f16";
        //String msg = "681091002809191100811d1f900054360745404352240905010220222222220303358108230000002f16";
        //String msg =   "681091002801000105000c070100aaaaaaaa0154360745404307080506010220290089633c7533162f16";
        String msg = "6810543607454043580404906001557716";
        byte[] recvBytes = ByteUtil.hexStrToByteArray(msg);
        /* ========== end of 报文数据 ============ */

        // [主站] 1. 通过解码器调用解析帧
        PacketFrame packetFrame = PacketCodec.decodeFrame(recvBytes);
        System.out.println("[主站] 接收数据帧:{" + packetFrame.toString() +"}");

        // [主站] 2. 解码器解析数据, 需要判断null的情况
        MeterBase msgData = PacketCodec.decodeData(packetFrame);
        if (null == msgData){
            System.out.println("解析失败");
            return;
        }

        // [主站] 3. 通过基类的实例化分别判断对象实例
        if (msgData instanceof MeterConfigReadCmd) {
            // 接收函数中可忽略 (主站发起的读配置命令)
            System.out.println("[主站] 接收 数据体:{" + ((MeterConfigReadCmd)msgData).toString() +"}");
        } else if (msgData instanceof MeterConfigReadResp) {
            // 接收函数需处理 (从站回应的读取配置结果)
            System.out.println("[主站] 接收 数据体:{" + ((MeterConfigReadResp)msgData).toString() +"}");
        } else if (msgData instanceof MeterConfigWriteCmd) {
            // 接收函数中可忽略 (主站发起的写配置命令)
            System.out.println("[主站] 接收 数据体:{" + ((MeterConfigWriteCmd)msgData).toString() +"}");
        } else if (msgData instanceof MeterConfigWriteResp) {
            // 接收函数需处理 (从站回应的写配置结果)
            System.out.println("[主站] 接收 数据体:{" + ((MeterConfigWriteResp)msgData).toString() +"}");
        } else if (msgData instanceof MeterReadWaterCmd){
            // 接收函数中可忽略 (主站发起的读月冻结量的指令)
            System.out.println("[主站] 接收 数据体:{" + ((MeterReadWaterCmd)msgData).toString() +"}");
        } else if (msgData instanceof MeterReadWaterResp){
            // 接收函数需处理 (从站回应的读月冻结量的结果)
            System.out.println("[主站] 接收 数据体:{" + ((MeterReadWaterResp)msgData).toString() +"}");
        } else if (msgData instanceof MeterReport){
            // 接收函数需处理 (从站上报数据)
            System.out.println("[主站] 接收 数据体:{" + ((MeterReport)msgData).toString() +"}");
        } else if (msgData instanceof MeterValveControlCmd){
            // 接收函数中可忽略 (主站发起的阀门控制指令)
            System.out.println("[主站] 接收 数据体:{" + ((MeterValveControlCmd)msgData).toString() +"}");
        } else if (msgData instanceof MeterValveControlResp){
            // 接收函数需处理 (从站回应的阀门控制的结果)
            System.out.println("[主站] 接收 数据体:{" + ((MeterValveControlResp)msgData).toString() +"}");
        }else if (msgData instanceof MeterVolumeThresholdCmd){
            // 接收函数中可忽略 (主站发起的设置水量阈值的指令)
            System.out.println("[主站] 接收 数据体:{" + ((MeterVolumeThresholdCmd)msgData).toString() +"}");
        } else if (msgData instanceof MeterVolumeThresholdResp){
            // 接收函数需处理 (从站回应的设置水量阈值的结果)
            System.out.println("[主站] 接收 数据体:{" + ((MeterVolumeThresholdResp)msgData).toString() +"}");
        }

    }


    private static void UT_WriteConfig() {
        System.out.println("========== 测试下发写配置 配置不写时，默认填充0 ============");

        /////////////////////////////////////////////////////////////////////////
        // [主站] 【1】 申请命令对象
        MeterConfig meterConfig = new MeterConfig();
        meterConfig.setReportPeriod((short)60);                         // 定时上传周期
//        meterConfig.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);   // 定时上传周期单位
        meterConfig.setReportRation((short)100);                        // 定量上传值
//        meterConfig.setTemporaryTime((byte)2);                          // 用户临时开阀用水限定时间
//        meterConfig.setValveRunTime((byte)3);                           // 阀门行程时间
//        meterConfig.setValveMaintainPeriod((short) 30);                 // 阀门维护周期
//        meterConfig.setMeterBasicValue(3000);                           // 表底数
//        meterConfig.setSampleUnit(Protocol.SAMPLE_UNIT_1M);             // 采样参数单位
        meterConfig.setMeterNumber("123456123456");                     // 表号
//        meterConfig.setMeterTime("20200101235959");                     // 表当前时间
//        meterConfig.setServerIp("10.88.192.11");                        // 设置IP地址
//        meterConfig.setServerPort((short) 6538);                        // 设置端口号

        //设置表状态字
        meterConfig.setMeterStatusFlag((short)(Protocol.METER_STATUS_MAGNETIC_ON |
                Protocol.METER_STATUS_VALVE_OPEN |
                ~Protocol.METER_STATUS_PERIOD_ON));

        // [主站] 【2】 申请写表配置数据对象
        MeterConfigWriteCmd command = new MeterConfigWriteCmd();

        // 设置 参数修改标识(修改了哪些参数，就设置哪些flag)
        command.setConfigFlag((short)(MeterConfig.FLAG_PERIOD));
        command.setConfig(meterConfig);

        // [主站] 【3】 封装为字节消息
        byte[] packetBytes = encodeData(Protocol.METER_TYPE_10H,
                "1505900569",
                "7833",
                (byte)0x02,
                command);

        // [主站] 发送 ...
        System.out.println("[主站] 发送数据:[" + HexUtils.toHexString(packetBytes)+"]");

    }


    // 单元测试
    public static void main(String[] args) {
        unitTestReport();
        unitTestReadConfig();
        unitTestWriteConfig();
        unitTestReadWaterVolume();
        unitTestValveControl();

        UT_WriteConfig();       // 测试写配置命令, 配置不设置时，填写默认值
        ST_ReceiveByteBuffer();


    }
}
