package com.learnbind.ai.service.cmbc;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.CmbcBatchCustomerAccount;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.cmbc
 *
 * @Title: CmbcBatchCustomerAccountService.java
 * @Description: CMBC代扣文件与客户账单关联表
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午4:40:58
 * @version V1.0 
 *
 */
public interface CmbcBatchCustomerAccountService extends IBaseService<CmbcBatchCustomerAccount, Long> {
	
	/**
	 * @Title: searchAllAccount
	 * @Description: 查询代扣之后的账单
	 * @param searchCond
	 * @param settleAccountStatus
	 * @return 
	 */
	public List<CmbcBatchCustomerAccount> searchAllAccount(String searchCond, Integer settleAccountStatus, String period);
	
	/**
	 * @Title: selectAccountByRecordId
	 * @Description:根据代扣文件id查询账单
	 * @param withRecordId
	 * @param period
	 * @return 
	 */
	public List<CmbcBatchCustomerAccount> selectAccountByRecordId(Long withRecordId, String period, String searchCond, Integer settleAccountStatus);
	
	/**
	 * @Title: settleAccount
	 * @Description: 代扣文件销账
	 * @param holdFileId
	 * @param userId
	 * @return 
	 */
	public int settleAccount(Long holdFileId, Long userId);
	
	
	/**
	 * @Title: advanceWithHold
	 * @Description:代扣文件预处理
	 * @param holdFileId
	 * @return 
	 */
	public int advanceWithHold(Long holdFileId);
	
	/**
	 * @Title: isAgainDeduct
	 * @Description: 是否需要再次扣费
	 * @param accountItemId
	 * @return 
	 */
	public boolean isAgainDeduct(Long accountItemId);
	
	/**
	 * @Title: selectAccountByRecordId
	 * @Description:查询代扣结果明细
	 * @param withRecordId
	 * @param period
	 * @return 
	 */
	public List<Map<String, Object>> selectHoldFileDetail(Long withRecordId, String searchCond, Integer withholdingStatus, String withholdFailReason);
	
}
