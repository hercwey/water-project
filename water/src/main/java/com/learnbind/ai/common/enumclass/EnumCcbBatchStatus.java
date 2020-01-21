package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumIdType.java
 * @Description: CCB代扣文件状态-枚举类
 *
 * @author Thinkpad
 * @date 2019年5月16日 下午4:53:24
 * @version V1.0 
 *
 */
public enum EnumCcbBatchStatus {

	/**
	 * @Fields GENERATE：0=已生成
	 */
	GENERATE(0, "已生成"),
	/**
	 * @Fields UPLOAD：1=已上传
	 */
	UPLOAD(1, "已上传"),
	/**
	 * @Fields APPLY_PROCESS：2=已申请处理
	 */
	APPLY_PROCESS(2, "已申请处理"),
	/**
	 * @Fields PROCESS_COMPLETE：3=处理完成
	 */
	PROCESS_COMPLETE(3, "处理完成"),
	/**
	 * @Fields DOWNLOAD_RETURN：4=已下载回盘
	 */
	DOWNLOAD_RETURN(4, "已下载回盘"),
	/**
	 * @Fields HANDLE_RETURN：5=已处理回盘
	 */
	HANDLE_RETURN(5, "已处理回盘"),
	/**
	 * @Fields SALE_ACCOUNT：6=已销账
	 */
	SALE_ACCOUNT(6, "已销账");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 AbnormityTypeConstant.
	 * @param value
	 * @param name
	 */
	private EnumCcbBatchStatus(Integer value, String name) {
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
	public static String getName(int value) {
		for (EnumCcbBatchStatus type : EnumCcbBatchStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
