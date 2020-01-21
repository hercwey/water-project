package com.learnbind.ai.bean;

import java.util.List;

/**
 * 抄表结果-实体
 *
 * MeterReadResult--user-->MeterRead
 *
 * 用于向服务器上传抄表数据
 */
public class MeterReadResultBean {
	
	/**
	 * @Fields userId：抄表员ID
	 */
	private Long userId;//抄表员ID
	/**
	 * @Fields meterBookId：表册ID
	 */
	private Long meterBookId;//表册ID
    /**
     * @Fields meters：表计读数结果列表
     */
    List<MeterReadBean> meters;//表计读数结果列表

    
    //getter and setter
    /**
     * @Title: getUserId
     * @Description: 获取抄表员ID
     * @return 
     */
    public Long getUserId() {
		return userId;
	}

	/**
	 * @Title: setUserId
	 * @Description: 设置抄表员ID
	 * @param userId 
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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

	/**
	 * @Title: getMeters
	 * @Description: 获取
	 * @return 
	 */
	public List<MeterReadBean> getMeters() {
		return meters;
	}

	/**
	 * @Title: setMeters
	 * @Description: 设置表计读数结果列表
	 * @param meters 
	 */
	public void setMeters(List<MeterReadBean> meters) {
		this.meters = meters;
	}

	@Override
	public String toString() {
		return "MeterReadResult [userId=" + userId + ", meterBookId=" + meterBookId + ", meters=" + meters + "]";
	}

	/**
	 * @Title: testMeterReadResult
	 * @Description: 测试数据 
	 */
	private static void testMeterReadResult(){
        String testData="{" +
                "    \"meterBookId\":\"1\",       " +
                "    \"meters\": [" +
                "        {" +
                "            \"meterId\": \"1\"," +
                "            \"customerId\":\"1\"," +
                "            \"meterNum\": \"12\"," +
                "            \"readDate\":\"2019-06-06\"" +
                "        }," +
                "        {" +
                "            \"meterId\": \"2\"," +
                "            \"customerId\":\"2\"," +
                "            \"meterNum\": \"13\"," +
                "            \"readDate\":\"2019-06-06\"" +
                "        }," +
                "        {" +
                "            \"meterId\": \"3\"," +
                "            \"customerId\":\"3\"," +
                "            \"meterNum\": \"14\"," +
                "            \"readDate\":\"2019-06-06\"" +
                "        }" +
                "    ]" +
                "}";


        MeterReadResultBean meterReadResult= FastJsonUtils.getObject(testData,MeterReadResultBean.class);

        System.out.println("表册ID"+meterReadResult.getMeterBookId());

        for(MeterReadBean meterRead:meterReadResult.getMeters()){
            System.out.println("表计ID:"+meterRead.getMeterId());
            System.out.println("客户ID:"+meterRead.getCustomerId());
            System.out.println("表计读数:"+meterRead.getMeterNum());
            System.out.println("抄表日期:"+meterRead.getReadDate());

            System.out.println("-------------------------------------");
        }

    }
	
	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args){
        testMeterReadResult();
    }

}
