package com.learnbind.ai.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CmbcBatchCustomerAccount;

import tk.mybatis.mapper.common.Mapper;

public interface CmbcBatchCustomerAccountMapper extends Mapper<CmbcBatchCustomerAccount> {
	
	/**
	 * @Title: searchAllAccount
	 * @Description: 查询代扣之后的账单
	 * @param searchCond
	 * @param settleAccountStatus
	 * @return 
	 */
	public List<CmbcBatchCustomerAccount> searchAllAccount(@Param("searchCond") String searchCond, @Param("settleAccountStatus") Integer settleAccountStatus, @Param("period") String period);
	
	public List<CmbcBatchCustomerAccount> selectAccountByRecordId(@Param("withRecordId") Long withRecordId, @Param("period") String period, @Param("searchCond") String searchCond, @Param("settleAccountStatus") Integer settleAccountStatus);
	
	public List<CmbcBatchCustomerAccount> getCustomerAccount(@Param("withRecordId") Long withRecordId, @Param("cardNo") String cardNo, @Param("customerName") String customerName, @Param("accountAmount") BigDecimal accountAmount);
	
	
	/**
	 * @Title: selectHoldFileDetail
	 * @Description: 查询代扣结果明细
	 * @param withRecordId
	 * @param searchCond
	 * @param withholdingStatus
	 * @param withholdFailReason
	 * @return 
	 */
	public List<Map<String, Object>> selectHoldFileDetail(@Param("withRecordId") Long withRecordId,@Param("searchCond") String searchCond, @Param("withholdingStatus") Integer withholdingStatus, @Param("withholdFailReason") String withholdFailReason);
}