package com.learnbind.ai.service.ccb;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.CcbBatchCustomerAccount;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbBatchCustomerAccountService.java
 * @Description: CCB代扣文件与客户账单关联表
 *
 * @author Thinkpad
 * @date 2019年8月20日 上午11:26:49
 * @version V1.0 
 *
 */
public interface CcbBatchCustomerAccountService extends IBaseService<CcbBatchCustomerAccount, Long> {
	
	/**
	 * @Title: searchAllAccount
	 * @Description: 查询代扣之后的账单
	 * @param searchCond
	 * @param settleAccountStatus
	 * @return 
	 */
	public List<CcbBatchCustomerAccount> searchAllAccount(String searchCond, Integer settleAccountStatus, String period);
	
	/**
	 * @Title: selectAccountByRecordId
	 * @Description:根据代扣文件id查询账单
	 * @param withRecordId
	 * @param period
	 * @return 
	 */
	public List<CcbBatchCustomerAccount> selectAccountByRecordId(Long withRecordId, String period, Integer settleAccountStatus, String searchCond);
	
	/**
	 * @Title: getAccountStatistic
	 * @Description: 获取销账统计结果
	 * @param withRecordId
	 * @param period
	 * @param settleAccountStatus
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> getAccountStatistic(Long withRecordId, String period, Integer settleAccountStatus, String searchCond);
	
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
	 * @Title: getWithHoldFileDeatil
	 * @Description: 查询代扣文件详情
	 * @param searchCond
	 * @param withHoldFileId
	 * @param withHoldStatus
	 * @param settleAccountStatus
	 * @return 
	 */
	public List<CcbBatchCustomerAccount> getWithHoldFileDeatil(String searchCond, Long withHoldFileId, Integer withHoldStatus, Integer settleAccountStatus);
	
	/**
	 * @Title: selectHoldFileDetail
	 * @Description: 查询批量代扣结果明细
	 * @param withRecordId
	 * @param searchCond
	 * @param withholdingStatus
	 * @param withholdFailReason
	 * @return 
	 */
	public List<Map<String, Object>> selectHoldFileDetail(Long withRecordId, String searchCond, Integer withholdingStatus, String withholdFailReason);
	
}
