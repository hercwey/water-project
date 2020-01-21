package com.learnbind.ai.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CcbBatchCustomerAccount;

import tk.mybatis.mapper.common.Mapper;

public interface CcbBatchCustomerAccountMapper extends Mapper<CcbBatchCustomerAccount> {
	
	/**
	 * @Title: searchAllAccount
	 * @Description: 查询代扣之后的账单
	 * @param searchCond
	 * @param settleAccountStatus
	 * @return 
	 */
	public List<CcbBatchCustomerAccount> searchAllAccount(@Param("searchCond") String searchCond, @Param("settleAccountStatus") Integer settleAccountStatus, @Param("period") String period);
	
	/**
	 * @Title: selectAccountByRecordId
	 * @Description:根据代扣文件id查询账单
	 * @param withRecordId
	 * @param period
	 * @return 
	 */
	public List<CcbBatchCustomerAccount> selectAccountByRecordId(@Param("withRecordId") Long withRecordId,@Param("period") String period, @Param("settleAccountStatus") Integer settleAccountStatus, @Param("searchCond") String searchCond);
	
	public CcbBatchCustomerAccount getCustomerAccount(@Param("withRecordId") Long withRecordId, @Param("withHoldNo") Integer withHoldNo, @Param("cardNo") String cardNo, @Param("customerName") String customerName, @Param("accountAmount") BigDecimal accountAmount);
	
	
	/**
	 * @Title: getWithHoldFileDeatil
	 * @Description: 查询代扣文件详情
	 * @param searchCond
	 * @param withHoldFileId
	 * @param withHoldStatus
	 * @param settleAccountStatus
	 * @return 
	 */
	public List<CcbBatchCustomerAccount> getWithHoldFileDeatil(@Param("searchCond") String searchCond, @Param("withHoldFileId") Long withHoldFileId, @Param("withHoldStatus") Integer withHoldStatus, @Param("settleAccountStatus") Integer settleAccountStatus);
	
	
	/**
	 * @Title: selectHoldFileDetail
	 * @Description: 查询批量代扣结果明细
	 * @param withRecordId
	 * @param searchCond
	 * @param withholdingStatus
	 * @param withholdFailReason
	 * @return 
	 */
	public List<Map<String, Object>> selectHoldFileDetail(@Param("withRecordId") Long withRecordId,@Param("searchCond") String searchCond, @Param("withholdingStatus") Integer withholdingStatus, @Param("withholdFailReason") String withholdFailReason);
	
	
	public List<Map<String, Object>> getAccountStatistic(@Param("withRecordId") Long withRecordId,@Param("period") String period, @Param("settleAccountStatus") Integer settleAccountStatus, @Param("searchCond") String searchCond);
}