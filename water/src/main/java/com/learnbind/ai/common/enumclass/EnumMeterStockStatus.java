package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMeterStockStatus.java
 * @Description: 库存管理日志-操作类型
 *
 * @author Thinkpad
 * @date 2019年10月27日 上午11:22:19
 * @version V1.0 
 *
 */
public enum EnumMeterStockStatus {

	//0：入库
	//1：安装领用
	//2：换表领用
	//3：撤销领用
	//4：出库
	//5：开始使用
	//6：检测通过
	//7：退库
	//8：报废
	
	
	/**
	 * @Fields IN_STORAGE：0 =入库
	 */
	IN_STORAGE(0, "入库"),
	
	/**
	 * @Fields INSTALL_RECEIVE：1=安装领用
	 */
	INSTALL_RECEIVE(1, "安装领用"),
	
	/**
	 * @Fields CHANGE_RECEIVE：2=换表领用
	 */
	CHANGE_RECEIVE(2, "换表领用"),
	
	/**
	 * @Fields CANCEL_RECEIVE：3=撤销领用
	 */
	CANCEL_RECEIVE(3, "撤销领用"),

	/**
	 * @Fields OUT_STORAGE：4=出库
	 */
	OUT_STORAGE(4, "出库"),
	
	/**
	 * @Fields START_USING：5=投入使用
	 */
	START_USING(5, "投入使用"),
	
	/**
	 * @Fields DETECTION：6=检测通过
	 */
	DETECTION(6, "检测通过"),
	
	/**
	 * @Fields RETURN_STORAGE：7=退库
	 */
	RETURN_STORAGE(7, "退库"),
	
	/**
	 * @Fields SCRAP：8=报废
	 */
	SCRAP(8, "报废");
	

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param value
	 * @param name
	 */
	private EnumMeterStockStatus(Integer value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * @Title: getValue
	 * @Description: 获取异常类型值，用于数据库保存
	 * @return 
	 */
	public Integer getValue() {
		return value;
	}
	
	/**
	 * @Title: getName
	 * @Description: 获取异常类型名称，用于页面显示
	 * @return 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @Title: getName
	 * @Description: 根据异常类型值获取名称
	 * @param value
	 * @return 
	 */
	public static String getName(Integer value) {
		for (EnumMeterStockStatus type : EnumMeterStockStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
