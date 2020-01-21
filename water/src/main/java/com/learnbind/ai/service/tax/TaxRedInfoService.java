package com.learnbind.ai.service.tax;

import com.learnbind.ai.model.TaxRedInfo;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice
 *
 * @Title: TaxRedInfoService.java
 * @Description: 红字信息service接口类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:06:14
 * @version V1.0 
 *
 */
public interface TaxRedInfoService extends IBaseService<TaxRedInfo, Long> {
	
	/**
	 * @Title: getTaxRedMessage
	 * @Description:根据发票号码和发票代码获取红字信息
	 * @param fphm
	 * @param fpdm
	 * @return 
	 */
	public TaxRedInfo getTaxRedMessage(String fpdm, String fphm);
}
