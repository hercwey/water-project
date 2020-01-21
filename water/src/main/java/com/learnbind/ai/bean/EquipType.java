package com.learnbind.ai.bean;

/**
 * 设备类型
 * 此处采用常量定义,也可以采用枚举类型
 */
public class EquipType {
    public static final int EQUIP_TYPE_EG=1;  //艺高
    public static final int EQUIP_TYPE_JC=2;  //积成
    public static final int EQUIP_TYPE_SC=3;  //三川

    /*
        功能:
            根据设备类型名称-->设备类型
        参数说明:
            equipTypeName  设备类型名称
        返回:
            设备类型编码(int类型)
     */
    public static int getEquipType(String equipTypeName){
        String tempName=equipTypeName.toUpperCase();
        int type=0;
        switch (tempName){
            case "JC":
                type=EQUIP_TYPE_JC;
                break;
            case "EG":
                type=EQUIP_TYPE_EG;
                break;
            case "SC":
                type=EQUIP_TYPE_SC;
                break;
        }
        return type;
    }
}
