package com.learnbind.ai.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerAccountItem;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerAccountItemMapper extends Mapper<CustomerAccountItem> {
	
	/**
	 * @Title: searchCustomerAccountItem
	 * @Description: 根据条件查询和操作人ID查询客户账目
	 * @param period
	 * @param traceIds
	 * @param searchCond
	 * @param operatorId
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerAccountItem(@Param("period") String period, @Param("traceIds") String traceIds, @Param("settlementStatus") Integer settlementStatus, @Param("accountStatus") Integer accountStatus, @Param("searchCond") String searchCond, @Param("operatorId") Long operatorId, @Param("creditSubjectList") List<String> creditSubjectList, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getSubjectCreditAmountSum
	 * @Description: 获取科目总金额之和
	 * @param accountItemId
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getSubjectCreditAmountSum(@Param("accountItemId") Long accountItemId, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getSubjectDebitAmountSum
	 * @Description: 获取科目已充值金额之和
	 * @param accountItemId
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getSubjectDebitAmountSum(@Param("accountItemId") Long accountItemId, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getSubjectOweAmountSum
	 * @Description: 获取科目欠费金额之和
	 * @param accountItemId
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getSubjectOweAmountSum(@Param("accountItemId") Long accountItemId, @Param("creditSubjectList") List<String> creditSubjectList);
	
	
	/**
	 * @Title: getDebitAmountSum
	 * @Description: 查询水费充值账单的总充值金额
	 * @param accountItemId
	 * @param period
	 * @return 
	 */
	public BigDecimal getDebitAmountSum(@Param("customerId") Long customerId, @Param("period") String period, @Param("debitCredit") String debitCredit, @Param("debitSubject") String debitSubject);
	
	/**
	 * @Title: getSubjectListOwedAmountSum
	 * @Description: 获取某客户科目集合中的欠费金额之和
	 * @param customerId
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getSubjectListOwedAmountSum(@Param("customerId") Long customerId, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getBillArrearsList
	 * @Description: 根据条件查询欠费账单
	 * @param searchCond
	 * @param operatorId
	 * @return 
	 */
	public List<CustomerAccountItem> getBillArrearsList(@Param("searchCond") String searchCond, @Param("operatorId") Long operatorId);
	
	/**
	 * @Title: searchCustomerRecharge
	 * @Description: 查看用户充值账单
	 * @param searchCond
	 * @param debitCredit
	 * @return 
	 */
	public List<CustomerAccountItem> searchCustomerRecharge(String searchCond, String debitCredit);
	
	/**
	 * @Title: selectNewCustomerAccountItem
	 * @Description: 查询分账单之后产生的新账单
	 * @param customerId
	 * @param period
	 * @param debitCredit
	 * @return 
	 */
	public List<CustomerAccountItem> selectNewCustomerAccountItem(@Param("customerId")Long customerId, @Param("period")String period, @Param("debitCredit")String debitCredit);
	
	
	public List<Map<String, Object>> getCustomerBillList(@Param("customerId") Long customerId);
	
	public List<Map<String, Object>> getCustomerFeeList(@Param("customerId") Long customerId,@Param("startDate") String startDate,@Param("endDate")String endDate);
	
	/**
	 * @Title: searchCustomerAccountItem
	 * @Description: 异常水费报警
	 * @param period
	 * @param locationCode
	 * @param searchCond
	 * @param operatorId
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerAccountItemErrorFee(@Param("period") String period, @Param("traceIds") String traceIds, @Param("settlementStatus") Integer settlementStatus, @Param("searchCond") String searchCond, @Param("operatorId") Long operatorId, @Param("creditSubjectList") List<String> creditSubjectList, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: searchCustomerAccountItem
	 * @Description: 根据条件查询和操作人ID查询客户账目
	 * @param period
	 * @param locationCode
	 * @param searchCond
	 * @param operatorId
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> getCustomersListByLocation(@Param("period") String period, @Param("traceIds") String traceIds, @Param("settlementStatus") Integer settlementStatus, @Param("searchCond") String searchCond, @Param("creditSubjectList") List<String> creditSubjectList, @Param("customerType") Integer customerType);
	
	
	
	
	public BigDecimal getwaterFeeArrears(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit);
	
	
	/**
	 * @param customerId
	 * @param period
	 * @return
	 * 查询客户所有账单
	 */
	public List<CustomerAccountItem> getAllCustomerAccountItemByPeriod(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: searchCcbCustomerAccountItem
	 * @Description: 建设银行代扣查询客户账单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @return 
	 */
	public List<Map<String, Object>> searchCcbCustomerAccountItem(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("deductMode") Integer deductMode, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getListGroupByCustomerId
	 * @Description: 获取账单按客户分组
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param creditSubjectList
	 * @param debitCredit
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> getListGroupByCustomerId(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("deductMode") Integer deductMode,  @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("settleStatusList") List<Integer> settleStatusList);
	
	/**
	 * @Title: getCustomerBalanceList
	 * @Description: 查询客户余额集合
	 * @param searchCond
	 * @param traceIds
	 * @param creditSubjectList
	 * @param debitCredit
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerBalanceList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: getCcbWithholdBill
	 * @Description: 获取中国建设银行批量代扣账单
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * 			定值条件：结算状态!=1；结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
	 * 			定值条件：扣费方式=2；扣费方式，1=其他/2=建行自动扣费/3=民生银行自动扣费
	 * 			定值条件：欠费账单；贷方金额!=借方金额，且贷方金额>借方金额
	 * @return 
	 * 		返回查询结果账目列表
	 */
	public List<CustomerAccountItem> getCcbWithholdBill(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getCmbcWithholdBill
	 * @Description: 获取中国民生银行批量代扣账单
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * 			定值条件：结算状态!=1；结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
	 * 			定值条件：扣费方式=3；扣费方式，1=其他/2=建行自动扣费/3=民生银行自动扣费
	 * 			定值条件：欠费账单；贷方金额!=借方金额，且贷方金额>借方金额
	 * @return 
	 * 		返回查询结果账目列表
	 */
	public List<CustomerAccountItem> getCmbcWithholdBill(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	
	/**
	 * @Title: searchCustomerAccountItem
	 * @Description: 查询除结算成功之外的账单
	 * @param period
	 * @param traceIds
	 * @param searchCond
	 * @param operatorId
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerArrearsAccountItem(@Param("period") String period, @Param("traceIds") String traceIds , @Param("settlementStatus") Integer settlementStatus, @Param("searchCond") String searchCond, @Param("operatorId") Long operatorId, @Param("creditSubjectList") List<String> creditSubjectList,  @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: searchAutoCustomerArrearsAccountItem
	 * @Description: 查询自动扣费账单
	 * @param period
	 * @param traceIds
	 * @param settlementStatus
	 * @param searchCond
	 * @param operatorId
	 * @param creditSubjectList
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> searchAutoCustomerAccountItem(@Param("period") String period, @Param("traceIds") String traceIds , @Param("settlementStatus") Integer settlementStatus, @Param("searchCond") String searchCond, @Param("operatorId") Long operatorId, @Param("creditSubjectList") List<String> creditSubjectList,  @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getHaveBalanceRechargeBill
	 * @Description: 查询某客户有余额的充值账单
	 * 					借/贷状态为借
	 * 					借方金额!=贷方金额
	 * 					借方金额-贷方金额>0
	 * 					
	 * @param customerId
	 * @param debitCredit
	 * @return 
	 */
	public List<CustomerAccountItem> getHaveBalanceRechargeBill(@Param("customerId") Long customerId, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: getCustomerBalance
	 * @Description: 查询客户余额
	 * @param customerId
	 * @param debitCredit
	 * @return 
	 */
	public BigDecimal getCustomerBalance(@Param("customerId") Long customerId, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: getHaveBalanceWaterFeeBill
	 * @Description: 查询某客户有余额的水费账单（不包含分账账单）
	 * 					借/贷状态为贷
	 * 					贷方科目为 201（其中第一位表示借/贷，第二、三位表示水费；不包含分账账单）
	 * 					贷方金额!=借方金额
	 * 					贷方金额-借方金额<0
	 * 					
	 * @param customerId
	 * @param debitCredit
	 * @param creditSubjectList
	 * @return 
	 */
	public List<CustomerAccountItem> getHaveBalanceWaterFeeBill(@Param("customerId") Long customerId, @Param("debitCredit") String debitCredit, @Param("creditSubjectList") List<String> creditSubjectList);
	
	
	public List<CustomerAccountItem> getCustomerAccountItemList(@Param("period") String period, @Param("customerId") Long customerId, @Param("settlementStatus") Integer settlementStatus, @Param("creditSubjectList") List<String> creditSubjectList);
	
	
	public List<Map<String, Object>> searchSettleAccountItem(@Param("period") String period, @Param("traceIds") String traceIds, @Param("settlementStatus") Integer settlementStatus, @Param("accountStatus") Integer accountStatus, @Param("searchCond") String searchCond, @Param("operatorId") Long operatorId, @Param("creditSubjectList") List<String> creditSubjectList, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("deductType") Integer deductType);
	
	
	/**
	 * @Title: getExportCustomerBillData
	 * @Description: 导出账单时获取账单数据
	 * @param searchCond
	 * @param settlementStatus
	 * @param traceIds
	 * @param period
	 * @param accountStatus
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> getExportCustomerBillData(@Param("searchCond") String searchCond, @Param("settlementStatus") Integer settlementStatus, @Param("traceIds") String traceIds, @Param("period") String period, @Param("accountStatus") Integer accountStatus,@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("creditSubjectList") List<String> creditSubjectList);
	
	
	public List<Long> getCustomerIdList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("customerType") Integer customerType);
	
	public Map<String, Object> getCustomerAccountItemStatistic(@Param("period") String period, @Param("traceIds") String traceIds, @Param("settlementStatus") Integer settlementStatus, @Param("accountStatus") Integer accountStatus, @Param("searchCond") String searchCond, @Param("operatorId") Long operatorId, @Param("creditSubjectList") List<String> creditSubjectList, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getPastOweAmount
	 * @Description: 查询往期欠费（水费+违约金+分账单）
	 * @param period
	 * @param creditSubjectList
	 * @param accountItemId
	 * @param customerId
	 * @return 
	 */
	public List<CustomerAccountItem> getPastOweAmount(@Param("period") String period,  @Param("creditSubjectList") List<String> creditSubjectList,  @Param("customerId") Long customerId);
	

	
	/**
	 * @param customerId
	 * @param period
	 * @return
	 * 查询表计所有账单
	 */
	public List<CustomerAccountItem> getAllCustomerAccountItemByMeter(@Param("meterId") Long meterId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getPastOweAmountByMeter
	 * @Description: 查询改水表的往期欠费
	 * @param period
	 * @param creditSubjectList
	 * @param meterId
	 * @return 
	 */
	public List<CustomerAccountItem> getPastOweAmountByMeter(@Param("period") String period,  @Param("creditSubjectList") List<String> creditSubjectList,  @Param("meterId") Long meterId);
	
	/**
	 * @Title: getWaterFeeBillList
	 * @Description: 获取水费账单
	 * @param customerId
	 * @param period
	 * @param settlementStatus
	 * @return 
	 */
	public List<CustomerAccountItem> getWaterFeeBillList(@Param("customerId") Long customerId, @Param("period") String period, @Param("settlementStatus") Integer settlementStatus, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getOweBillList
	 * @Description: 获取欠费账单
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public List<CustomerAccountItem> getOweBillList(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getOweTotalAmount
	 * @Description: 获取欠费总金额
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getOweTotalAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getPastOweBillList
	 * @Description: 获取往期欠费账单
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public List<CustomerAccountItem> getPastOweBillList(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getPastWaterFeeOweAmount
	 * @Description: 获取往期欠费总金额
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getPastOweTotalAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getPastBaseFeeOweTotalAmount
	 * @Description: 获取往期基础水费欠费总金额
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getPastBaseFeeOweTotalAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getWaterFeeOwePeriod
	 * @Description: 获取水费欠费月份
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<String> getWaterFeeOwePeriod(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	/**
	 * @Title: getOverdueOwePeriod
	 * @Description: 获取违约金欠费月份
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<String> getOverdueOwePeriod(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);

	/**
	 * @Title: getAllRechargeBill
	 * @Description: 查询客户所有充值账单
	 * @param customerId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<CustomerAccountItem> getAllRechargeBill(@Param("customerId") Long customerId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: getBalanceRechargeBill
	 * @Description: 查询额客户有余额的充值账单
	 * @param customerId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<CustomerAccountItem> getBalanceRechargeBill(@Param("customerId") Long customerId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: getTaxInvoiceBillList
	 * @Description: 获取税务开票软件账单集合（水费账单，且金额>0或预存账单，且金额>0）
	 * @param periodStart
	 * @param periodEnd
	 * @param customerId
	 * @param settlementStatus
	 * @param creditSubjectList
	 * @param debitSubject
	 * @return 
	 */
	public List<CustomerAccountItem> getTaxInvoiceBillList(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd, @Param("customerId") Long customerId, @Param("settlementStatus") Integer settlementStatus, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitSubject") String debitSubject);
	
	/**
	 * @Title: getRechargeBillList
	 * @Description: 获取本期充值账单
	 * @param period
	 * @param debitSubjectPayment
	 * @return 
	 */
	public List<CustomerAccountItem> getRechargeBillList(@Param("period") String period, @Param("debitSubjectPayment") String debitSubjectPayment);
	
	/**
	 * @Title: getPastRechargeBillList
	 * @Description: 获取往期充值账单
	 * @param period
	 * @param debitSubjectPayment
	 * @return 
	 */
	public List<CustomerAccountItem> getPastRechargeBillList(@Param("period") String period, @Param("debitSubjectPayment") String debitSubjectPayment);
	
	/**
	 * @Title: getStatSettlementBillList
	 * @Description: 查询本期已结算（全部结算或部分结算）的水费账单
	 * @param period
	 * @param debitSubject
	 * @param traceIds
	 * @return 
	 * 		COUNT(1) AS COUNT,
			SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
			MAX( PW.BASE_PRICE ) AS BASE_PRICE,
			MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
			SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
			SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
	 */
	public List<Map<String, Object>> getStatSettlementBillList(@Param("period") String period, @Param("debitSubject") String debitSubject, @Param("traceIdsList") List<String> traceIdsList);
	
	/**
	 * @Title: getStatPastSettlementBillList
	 * @Description: 查询往期已结算（全部结算或部分结算）的水费账单
	 * @param period
	 * @param debitSubject
	 * @param traceIds
	 * @return 
	 * 		COUNT(1) AS COUNT,
			SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
			MAX( PW.BASE_PRICE ) AS BASE_PRICE,
			MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
			SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
			SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
	 */
	public List<Map<String, Object>> getStatPastSettlementBillList(@Param("period") String period, @Param("debitSubject") String debitSubject, @Param("traceIdsList") List<String> traceIdsList);
	
	/**
	 * @Title: getActualWaterFeeAmount
	 * @Description: 按期间统计应收水费金额
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getActualWaterFeeAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getReceiveWaterFeeAmount
	 * @Description: 按期间统计实收水费金额
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getReceiveWaterFeeAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getStatOweCompanyMeter
	 * @Description: 统计欠费单位大表
	 * @param searchCond
	 * @param period
	 * @param settleStatusList
	 * @return 
	 */
	public List<Map<String, Object>> getStatOweCompanyMeter(@Param("searchCond") String searchCond, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("settleStatusList") List<Integer> settleStatusList);
	
	/**
	 * @Title: getCustomerAccountItemDetail
	 * @Description: 财务统计-获取欠费单位明细
	 * @param period
	 * @param customerId
	 * @param settleStatusList
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerAccountItemDetail(@Param("period") String period, @Param("customerId") Long customerId, @Param("settleStatusList") List<Integer> settleStatusList, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getStatNoTaxBillList
	 * @Description: 查询本期已结算（全部结算或部分结算）的水费账单（无票收入）)
	 * @param period
	 * @param settleStatusList
	 * @param traceIdsList
	 * @return 
	 */
	public List<Map<String, Object>> getStatNoTaxBillList(@Param("period") String period, @Param("settleStatusList") List<Integer> settleStatusList, @Param("traceIdsList") List<String> traceIdsList);
	
	public List<Map<String, Object>> getCustomerBalanceStatusList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("balanceStatus") Integer balanceStatus, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit);
	
	/**
	 * @Title: getWechatCustomerOweBillList
	 * @Description: 查询微信客户的账单，按客户ID分组,返回列表
	 * 					与getListGroupByCustomerId查询SQL相同，在原SQL的基础上增加了EXISTS条件，用于判断是否绑定微信，是否绑定微信判断规则是微信-客户关系表中是否存在记录，有记录表示已绑定微信，无记录表示未绑定微信
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param creditSubjectList
	 * @param debitCredit
	 * @param startDate
	 * @param endDate
	 * @param settleStatusList
	 * @return 
	 * 		I.CUSTOMER_ID,
			C.CUSTOMER_NAME, 
			I.PERIOD,
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,
			( SUM(I.CREDIT_AMOUNT-I.DEBIT_AMOUNT) ) AS OWE_AMOUNT, 
			LM.TRACE_IDS 
	 */
	public List<Map<String, Object>> getWechatCustomerOweBillList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("deductMode") Integer deductMode,  @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("settleStatusList") List<Integer> settleStatusList);
	
	/**
	 * @Title: getNotWechatCustomerOweBillList
	 * @Description: 查询非微信客户的账单，按客户ID分组,返回列表
	 * 			与getListGroupByCustomerId查询SQL相同，在原SQL的基础上增加了NOT EXISTS条件，用于判断是否绑定微信，是否绑定微信判断规则是微信-客户关系表中是否存在记录，有记录表示已绑定微信，无记录表示未绑定微信
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param creditSubjectList
	 * @param debitCredit
	 * @param startDate
	 * @param endDate
	 * @param settleStatusList
	 * @return 
	 * 		I.CUSTOMER_ID,
			C.CUSTOMER_NAME, 
			I.PERIOD,
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,
			( SUM(I.CREDIT_AMOUNT-I.DEBIT_AMOUNT) ) AS OWE_AMOUNT, 
			LM.TRACE_IDS 
	 */
	public List<Map<String, Object>> getNotWechatCustomerOweBillList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("deductMode") Integer deductMode,  @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("settleStatusList") List<Integer> settleStatusList);
	
	/**
	 * @Title: getDebitBillList
	 * @Description: 根据充值记录ID查询清欠充值记录
	 * @param rechargeBillId
	 * @return 
	 */
	public List<CustomerAccountItem> getDebitBillList(@Param("rechargeBillId") Long rechargeBillId);
	
	/**
	 * @Title: updateDebitBillCreditAssistantIsNull
	 * @Description: 更新清欠充值记录中贷方辅助核算为空
	 * @param rechargeBillId
	 */
	public void updateDebitBillCreditAssistantIsNull(@Param("rechargeBillId") Long rechargeBillId);
	
	/**
	 * @Title: deleteDebitBill
	 * @Description: 删除清欠的充值记录（逻辑删除）
	 * @param rechargeBillId 
	 */
	public void deleteDebitBill(@Param("rechargeBillId") Long rechargeBillId);
	
}