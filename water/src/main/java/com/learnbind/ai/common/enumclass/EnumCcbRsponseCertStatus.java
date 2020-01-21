package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumCcbRsponseCertStatus.java
 * @Description: 查询凭证状态时CCB返回的所有状态-枚举类
 *
 * @author Administrator
 * @date 2019年7月18日 下午5:05:04
 * @version V1.0 
 *
 */
public enum EnumCcbRsponseCertStatus {

//	凭证状态"100-不确定,
//	200-待复核,
//	300-复核不通过,
//	400-复核完毕,待账务系统处理,
//	401-复核完毕,待发送账务系统,
//	500-已收回,
//	600-已删除(直连单据复核不通过时状态置为600),
//	700-交易成功,
//	800-交易失败,
//	901-账务系统成功已接收,
//	902-账务系统检核完毕,
//	903-账务系统下账,
//	904-账务系统处理完成,
//	429-单笔跨行终审成功,待人行处理"
	
	/**
	 * @Fields STATUS_100：凭证状态：不确定
	 */
	STATUS_100("100", "不确定"),
	/**
	 * @Fields STATUS_200：凭证状态：待复核
	 */
	STATUS_200("200", "待复核"),
	/**
	 * @Fields STATUS_300：凭证状态：复核不通过
	 */
	STATUS_300("300", "复核不通过"),
	/**
	 * @Fields STATUS_400：凭证状态：复核完毕，待账务系统处理
	 */
	STATUS_400("400", "复核完毕,待账务系统处理"),
	/**
	 * @Fields STATUS_401：凭证状态：复核完毕，待发送账务系统
	 */
	STATUS_401("401", "复核完毕,待发送账务系统"),
	/**
	 * @Fields STATUS_500：凭证状态：已收回
	 */
	STATUS_500("500", "已收回"),
	/**
	 * @Fields STATUS_600：凭证状态：已删除(直连单据复核不通过时状态置为600)
	 */
	STATUS_600("600", "已删除(直连单据复核不通过时状态置为600)"),
	/**
	 * @Fields STATUS_700：凭证状态：交易成功
	 */
	STATUS_700("700", "交易成功"),
	/**
	 * @Fields STATUS_800：凭证状态：交易失败
	 */
	STATUS_800("800", "交易失败"),
	/**
	 * @Fields STATUS_901：凭证状态：账务系统成功已接收
	 */
	STATUS_901("901", "账务系统成功已接收"),
	/**
	 * @Fields STATUS_902：凭证状态：账务系统检核完毕
	 */
	STATUS_902("902", "账务系统检核完毕"),
	/**
	 * @Fields STATUS_903：凭证状态：账务系统下账
	 */
	STATUS_903("903", "账务系统下账"),
	/**
	 * @Fields STATUS_904：凭证状态：账务系统处理完成
	 */
	STATUS_904("904", "账务系统处理完成"),
	/**
	 * @Fields STATUS_429：凭证状态：单笔跨行终审成功,待人行处理
	 */
	STATUS_429("429", "单笔跨行终审成功,待人行处理");

	/**
	 * @Fields status：状态
	 */
	private String status;
	/**
	 * @Fields name：名称
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumCcbRsponseCertStatus.
	 * @param value
	 * @param name
	 */
	private EnumCcbRsponseCertStatus(String status, String name) {
		this.status = status;
		this.name = name;
	}
	
	/**
	 * @Title: getStatus
	 * @Description: 获取状态
	 * @return 
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * @Title: getName
	 * @Description: 获取状态名称
	 * @return 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @Title: getName
	 * @Description: 根据状态获取状态名称
	 * @param status
	 * @return 
	 */
	public static String getName(String status) {
		for (EnumCcbRsponseCertStatus type : EnumCcbRsponseCertStatus.values()) {
			if (type.getStatus().equals(status)) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
