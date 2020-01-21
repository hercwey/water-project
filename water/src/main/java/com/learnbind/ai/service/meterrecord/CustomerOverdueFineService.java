package com.learnbind.ai.service.meterrecord;

import java.util.List;

import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.overduefine
 *
 * @Title: OverdueFineService.java
 * @Description: 每日违约金比例配置服务接口类
 *
 * @author Administrator
 * @date 2019年5月15日 下午4:27:03
 * @version V1.0 
 *
 */
public interface CustomerOverdueFineService extends IBaseService<CustomerOverdueFine, Long> {
	
	/**
	 * @Title: selectObjectByAccountId
	 * @Description: 根据违约金账单ID超找违约金记录
	 * @param overdueAccountId
	 * @return 
	 */
	public List<CustomerOverdueFine> selectIdByAccountIdList(List<Long> accountIdIdList);
	
	/**
	 * @Title: saveList
	 * @Description: 修改违约金表记录数据
	 * @param customerId
	 * @param accountItemId
	 * @param dfList
	 * @return 
	 */
	public int saveList(List<CustomerOverdueFine> dfList);
}
