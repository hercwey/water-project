package com.learnbind.ai.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 表册-表计实体
 */
public class MeterBookMeterBean {
    /**
     * @Fields name：表册名称 格式：小区名称-楼号-单元
     */
    private String name;//表册名称 小区名称-10-1
    /**
     * @Fields equipType：表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
     */
    private String equipType;//表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
    /**
     * @Fields readType：抄表方式
     */
    private String readType;//抄表方式
    /**
     * @Fields meterBookId：表册ID
     */
    private Long meterBookId;//表册ID
    /**
     * @Fields meters：表计列表
     */
    private List<MeterBean> meters;//表计列表
    
    
    /**
     * 创建一个新的实例 MeterBookMeterBean.
     */
    public MeterBookMeterBean() {
    	meters=new ArrayList<>();
    }

    
    /**
     * @Title: getName
     * @Description: 获取表册名称 格式：小区名称-楼号-单元
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * @Title: setName
     * @Description: 设置表册名称 格式：小区名称-楼号-单元
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @Title: getEquipType
     * @Description: 获取表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
     * @return 
     */
    public String getEquipType() {
        return equipType;
    }

    /**
     * @Title: setEquipType
     * @Description: 设置表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
     * @param equipType 
     */
    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    /**
     * @Title: getReadType
     * @Description: 获取抄表方式
     * @return 
     */
    public String getReadType() {
		return readType;
	}


	/**
	 * @Title: setReadType
	 * @Description: 设置抄表方式
	 * @param readType 
	 */
	public void setReadType(String readType) {
		this.readType = readType;
	}

	/**
     * @Title: getMeters
     * @Description: 获取表计列表
     * @return 
     */
    public List<MeterBean> getMeters() {
        return meters;
    }

    /**
     * @Title: setMeters
     * @Description: 表计列表
     * @param meters 
     */
    public void setMeters(List<MeterBean> meters) {
        this.meters = meters;
    }

    /**
     * @Title: getMeterBookId
     * @Description: 获取表册ID
     * @return 
     */
    public Long getMeterBookId() {
        return meterBookId;
    }

    /**
     * @Title: setMeterBookId
     * @Description: 设置表册ID
     * @param meterBookId 
     */
    public void setMeterBookId(Long meterBookId) {
        this.meterBookId = meterBookId;
    }

    //测试JSON解析
    private static void  testMeterBook(){
        String testData="  {" +
                "    \"name\": \"长久花园10-1\"," +
                "    \"equipType\": \"JC\"," +
                "    \"meterBookId\":\"1\",    " +
                "    \"meters\": [" +
                "        {" +
                "            \"addr\": \"1803030183\"," +
                "            \"meterId\":\"1\"," +
                "            \"customer\": \"张三\"," +
                "            \"customerId\":\"1\"," +
                "            \"lastNum\":\"12\"," +
                "            \"location\":\"10-1-101\"" +
                "        }," +
                "        {" +
                "            \"addr\": \"1803030184\"," +
                "            \"meterId\":\"2\"," +
                "            \"customer\": \"李四\"," +
                "            \"customerId\":\"2\"," +
                "            \"lastNum\":\"10\"," +
                "            \"location\":\"10-1-102\"" +
                "        }," +
                "        {" +
                "            \"addr\": \"1803030185\"," +
                "            \"meterId\":\"3\"," +
                "            \"customer\": \"王五\"," +
                "            \"customerId\":\"3\"," +
                "            \"lastNum\":\"5\"," +
                "            \"location\":\"10-1-101\"" +
                "        }" +
                "    ]" +
                "} ";

        MeterBookMeterBean meterBookMeter =FastJsonUtils.getObject(testData, MeterBookMeterBean.class);
        System.out.println("表册名称:"+ meterBookMeter.getName());
        System.out.println("表册ID:"+ meterBookMeter.getMeterBookId());
        System.out.println("表册-设备类型名称:"+ meterBookMeter.getEquipType());

        int equipType= EquipType.getEquipType(meterBookMeter.getEquipType());
        System.out.println("表册-设备类型:"+equipType);


        System.out.println("-----------------------------------------");

        List<MeterBean> meterList = meterBookMeter.getMeters();
        for (MeterBean meter:meterList){
            /*private String addr;  //表计地址
            private Long meterId;  //表计ID
            private String customer; //客户名称
            private Long customerId;  //客户ID
            private String location;  //表计地址(客户地址)  小区-楼号-单元-户号
            private String lastNum;  //最后一次读数*/

            System.out.println("表计地址:"+meter.getAddr());
            System.out.println("表计ID:"+meter.getMeterId());
            System.out.println("客户名称:"+meter.getCustomer());
            System.out.println("客户ID:"+meter.getCustomerId());
            System.out.println("地理位置:"+meter.getLocation());
            System.out.println("最后一次读数:"+meter.getLastNum());

            System.out.println("表计当前读数:"+meter.getCurrNum());
            System.out.println("抄表日期:"+meter.getReadDate());

            System.out.println("-----------------------------------------");

        }

    }



    public static void main(String[] args){
        testMeterBook();
    }


}
