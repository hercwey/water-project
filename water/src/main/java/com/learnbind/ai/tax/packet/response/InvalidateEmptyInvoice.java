package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: InvalidateEmptyInvoice.java
 * @Description: 空白发票作废返回报文
 *
 * @author lenovo
 * @date 2019年10月19日 下午2:08:39
 * @version V1.0 
 *
 */
public class InvalidateEmptyInvoice {
	/*
	 
	 	RETCODE	错误代码	字符	100	是	
		RETMSG	错误信息	字符	100	是	
		FPZL	发票种类	字符	2		固定值：
									0：专用发票 
									2：普通发票
									12：机动车票
									51：电子发票
		FPDM	发票代码	字符	15		
		FPHM	发票号码	字符	15
	*/		
	
	private String RETCODE; //	错误代码	字符	100	是	
	private String RETMSG; //	错误信息	字符	100	是	
	private String FPZL; //	发票种类	字符	2		固定值：
							//	0：专用发票 
							//	2：普通发票
							//	12：机动车票
							//	51：电子发票
	private String FPDM;//	发票代码	字符	15		
	private String FPHM; //	发票号码	字符	15
	
}
