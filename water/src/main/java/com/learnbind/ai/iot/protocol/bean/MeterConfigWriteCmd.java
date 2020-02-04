package com.learnbind.ai.iot.protocol.bean;

import com.learnbind.ai.iot.protocol.util.ByteUtil;

public class MeterConfigWriteCmd extends MeterBase {

    private short configFlag;     // 参数修改标识：2字节, 写表配置数据时，以此值（HEX格式）定义
    private MeterConfig config;

    public MeterConfigWriteCmd(){
        config = new MeterConfig();
    }

    public MeterConfigWriteCmd(byte[] dataBytes){
        byte[] configFlagBytes = new byte[2];     // 参数修改标识：2字节, 写表配置数据时，以此值（HEX格式）定义

        int pos = 0;
        pos += ByteUtil.arrayCopy(dataBytes, pos, configFlagBytes);

        byte[] configBytes = new byte[dataBytes.length - configFlagBytes.length];
        pos += ByteUtil.arrayCopy(dataBytes, pos, configBytes);

        config = new MeterConfig(configBytes);
    }

    public byte[] encodeBytes(){
        byte[] configFlagBytes = new byte[2];     // 参数修改标识：2字节, 写表配置数据时，以此值（HEX格式）定义
        ByteUtil.setBytes(configFlagBytes, configFlag);

        return ByteUtil.concatAll(configFlagBytes, config.encodeBytes());
    }

    public short getConfigFlag() {
        return configFlag;
    }

    public void setConfigFlag(short configFlag) {
        this.configFlag = configFlag;
    }

    public MeterConfig getConfig() {
        return config;
    }

    public void setConfig(MeterConfig config) {
        this.config = config;
    }
}
