package com.space.water.iot.api.protocol;

import org.apache.tomcat.util.buf.HexUtils;

import com.space.water.iot.api.model.common.ControlValveType;
import com.space.water.iot.api.protocol.bean.MeterBase;
import com.space.water.iot.api.protocol.bean.MeterConfig;
import com.space.water.iot.api.protocol.bean.MeterConfigReadCmd;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteCmd;
import com.space.water.iot.api.protocol.bean.MeterReadWaterCmd;
import com.space.water.iot.api.protocol.bean.MeterValveControlCmd;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdCmd;
import com.space.water.iot.api.util.LogUtil;

public class CommandGenerator {
	
	/**
	 * 生成命令字符串
	 * 
	 * @param meterType 表类型
	 * @param meterAddress 表地址
	 * @param meterFactoryCode 表厂商
	 * @param sequence 序号
	 * @param meterBase 命令参数实体类
	 * @return
	 */
    public static String generateCmd(byte meterType,String meterAddress,String meterFactoryCode,byte sequence,MeterBase meterBase) {
    	String cmd = "";
        byte[] packetBytes = PacketCodec.encodeData(meterType,
        		meterAddress,
        		meterFactoryCode,
                sequence,
                meterBase);
        
        cmd = HexUtils.toHexString(packetBytes);
        LogUtil.debug("生成命令:[" +cmd +"]");
    	return cmd;
    }
    
    /**
     * 生成“读取设备配置”指令Sample
     * @return
     */
    private static String readConfigSample() {
    	MeterConfigReadCmd command = new MeterConfigReadCmd();
    	
    	byte meterType = Protocol.METER_TYPE_10H;
    	String meterAddress = "1505900569";
    	String meterFactoryCode = "7833";
    	byte sequence = 0x01;
    	
    	return generateCmd(meterType, meterAddress, meterFactoryCode, sequence, command);
    }

    /**
     * 生成“设置设备配置”指令Sample
     * @return
     */
    private static String writeConfigSample() {
    	MeterConfig meterConfig = new MeterConfig();
        meterConfig.setReportPeriod((short)60);// 定时上传周期
        meterConfig.setReportPeriodUnit(MeterConfig.PERIOD_UNIT_MIN);// 定时上传周期单位
        meterConfig.setReportRation((short)100);// 定量上传值
        meterConfig.setTemporaryTime((byte)2);// 用户临时开阀用水限定时间
        meterConfig.setValveRunTime((byte)3);// 阀门行程时间
        meterConfig.setValveMaintainPeriod((short) 30);// 阀门维护周期
        meterConfig.setMeterBasicValue(3000);// 表底数 TODO 数据类型
        meterConfig.setSampleUnit(Protocol.SAMPLE_UNIT_1M); // 采样参数单位
        meterConfig.setMeterNumber("123456123456"); // 表号
        meterConfig.setMeterTime("20200101235959"); // 表当前时间
        meterConfig.setServerIp("10.88.192.11"); // 服务器IP地址
        meterConfig.setServerPort((short) 6538); // 服务器端口

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

    	byte meterType = Protocol.METER_TYPE_10H;
    	String meterAddress = "1505900569";
    	String meterFactoryCode = "7833";
    	byte sequence = 0x01;
    	
    	return generateCmd(meterType, meterAddress, meterFactoryCode, sequence, command);
    }

    /**
     * 生成“开关阀门控制”指令Sample
     * @return
     */
    private static String controlSample() {
    	MeterValveControlCmd command = new MeterValveControlCmd();
    	command.setAction(ControlValveType.VALVE_OPEN);

    	byte meterType = Protocol.METER_TYPE_10H;
    	String meterAddress = "1505900569";
    	String meterFactoryCode = "7833";
    	byte sequence = 0x01;
    	
    	return generateCmd(meterType, meterAddress, meterFactoryCode, sequence, command);
    }

    /**
     * 生成“配置阈值”指令Sample
     * @return
     */
    private static String setThresholdSample() {
    	MeterVolumeThresholdCmd command = new MeterVolumeThresholdCmd();
    	command.setThreshold((short) 50);
    	
    	byte meterType = Protocol.METER_TYPE_10H;
    	String meterAddress = "1505900569";
    	String meterFactoryCode = "7833";
    	byte sequence = 0x01;
    	
    	return generateCmd(meterType, meterAddress, meterFactoryCode, sequence, command);
    }
    
    
    /**
     * 生成“读取月冻结”指令Sample
     * @return
     */
    private static String setReadMonthSample() {
    	MeterReadWaterCmd command = new MeterReadWaterCmd();
    	
    	byte meterType = Protocol.METER_TYPE_10H;
    	String meterAddress = "1505900569";
    	String meterFactoryCode = "7833";
    	byte sequence = 0x01;
    	
    	return generateCmd(meterType, meterAddress, meterFactoryCode, sequence, command);
    }
    
    public static void main(String[] args) {
    	//readConfigSample();//生成“读取设备配置”指令Sample
    	//writeConfigSample();//生成“设置设备配置”指令Sample
    	//controlSample();//生成“开关阀门控制”指令Sample
    	//setThresholdSample();//生成“配置阈值”指令Sample
	}
    
}
