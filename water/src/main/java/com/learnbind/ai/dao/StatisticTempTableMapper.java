package com.learnbind.ai.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.StatisticTempTable;

import tk.mybatis.mapper.common.Mapper;

public interface StatisticTempTableMapper extends Mapper<StatisticTempTable> {
	public List<Map<String, Object>> test();
	
	public List<Map<String, Object>> statCustomerPerDay(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> statCustomerPerMonth(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> searchCustomerPerMonth(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> searchCustomerPerYear(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> searchReadMeterRecordPerMonth(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> searchReadMeterRecordPerYear(@Param("period") String period, @Param("traceIds") String traceIds) ;
	public List<Map<String, Object>> statReadRecordNumPerDay(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> statReadRecordNumPerMonth(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> statReadRecordMonthSummary(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> statReadRecordYearSummary(@Param("period") String period, @Param("traceIds") String traceIds);
	
	public int statNeedReadMeterAmount(@Param("traceIds") String traceIds);
	public int statHasReadMeterAmount(@Param("period") String period, @Param("traceIds") String traceIds);
	public int statHasReadMeterYearAmount(@Param("period") String period, @Param("traceIds") String traceIds);	
	public  List<Map<String, Object>> searchPartRecordPerMonth(@Param("traceIds") String traceIds, @Param("period") String period);	
	public List<Map<String, Object>> statWaterAmountPerDay(@Param("traceIds") String traceIds, @Param("period") String period);
	
	public List<Map<String, Object>> searchPartRecordPerYear(@Param("period") String period, @Param("traceIds") String traceIds);
	public List<Map<String, Object>> statWaterAmountPerMonth(@Param("period") String period, @Param("traceIds") String traceIds) ;
	
	public List<Map<String, Object>> statWaterMonthSummary(@Param("period") String period, @Param("traceIds") String traceIds);
	
	public List<Map<String,Object>> getParentNodeListByMonth(@Param("period") String period);
	
	public 	List<Map<String,Object>> statChildAmountOfParentNode(@Param("period") String period);
	public List<Map<String, Object>> getParentNodeAmountListByMonth(@Param("period") String period);
	
	public List<Map<String, Object>> searchReceivableList(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd, @Param("traceIds") String traceIds);
	
	public List<Map<String, Object>> statReceivableSum(@Param("periodStart") String periodStart,@Param("periodEnd") String periodEnd, @Param("traceIds") String traceIds);
	
	public List<Map<String, Object>> searchActualReceList(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd, @Param("traceIds") String traceIds);
	
	public List<Map<String, Object>> searchDebtDetailList(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd, @Param("traceIds") String traceIds) ;
	
	public List<Map<String, Object>> statDebtSum(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd, @Param("traceIds") String traceIds) ;
	
	public List<Map<String, Object>> statDebtSummaryByPriceCode(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd);
	
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
	public List<Map<String, Object>> getListGroupByCustomerId(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("deductMode") Integer deductMode,  @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getListGroupByPriceCode
	 * @Description: 根据条件查询并按priceCode分组统计
	 * @param traceIds
	 * @param period
	 * @param creditSubjectList
	 * @param debitCredit
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> getListGroupByPriceCode(@Param("traceIds") String traceIds, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
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
	 * @Title: getOweBillList
	 * @Description: 获取欠费账单
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
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
	 * @Title: searchYycArrearsList
	 * @Description: 获取营业处统计的欠费表
	 * @param period
	 * @param customerType
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> searchYycArrearsList(@Param("period") String period, @Param("customerType") Integer customerType,@Param("creditSubjectList") List<String> creditSubjectList);
	
	public BigDecimal searchYycArrearsTotal(@Param("period") String period, @Param("customerType") Integer customerType,@Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: searchYycArrearsList
	 * @Description: 获取营业处统计的欠费表(小区)
	 * @param period
	 * @param customerType
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> searchYycBlockArrearsList(@Param("period") String period, @Param("customerType") Integer customerType,@Param("debitCredit") String debitCredit);
	/**
	 * @Title: searchYycArrearsList
	 * @Description: 获取营业处统计的清欠
	 * @param period
	 * @param customerType
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String, Object>> searchYycCleanList(@Param("period") String period, @Param("customerType") Integer customerType,@Param("creditSubjectList") List<String> creditSubjectList);
	
	public BigDecimal searchYycCleanTotal(@Param("period") String period, @Param("customerType") Integer customerType,@Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: searchYycBlockCleanList
	 * @Description: 获取营业处统计的清欠（小区）
	 * @param period
	 * @param customerType
	 * @param debitCredit
	 * @return 
	 */
	public List<Map<String, Object>> searchYycBlockCleanList(@Param("period") String period, @Param("customerType") Integer customerType,@Param("debitCredit") String debitCredit);
	/**
	 * @Title: getCurrCleanAmount
	 * @Description: 获取本月清欠
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getCurrCleanAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("customerType") Integer customerType);
	
	public BigDecimal getCurrCleanBlockAmount(@Param("customerType") Integer customerType, @Param("period") String period);
	
	
	/**
	 * @Title: geActualSummary
	 * @Description: 实收水价分类汇总
	 * @param traceIds
	 * @param creditSubjectList
	 * @param debitCredit
	 * @param periodStart
	 * @param periodEnd
	 * @return 
	 */
	public List<Map<String, Object>> geActualSummary(@Param("traceIds") String traceIds, @Param("creditSubjectList") List<String> creditSubjectList, @Param("debitCredit") String debitCredit, @Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd);
	
	public List<Map<String, Object>> statCompareYearAmount(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd, @Param("traceIds") String traceIds);
	
	public List<Map<String, Object>> searchCompareYearAmount(@Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd,@Param("traceIds") String traceIds) ;
	
	
	public int statNeedReadMeterYear(@Param("period") String period, @Param("traceIds") String traceIds);
	

	/**
	 * @Title: getPastArrearsRecord
	 * @Description: 获取往年拖欠水费用户登记
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String,Object>> getPastArrearsRecord(@Param("period") String period, @Param("searchCond") String searchCond, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getPastArrearsRecordTotal
	 * @Description: 获取往年拖欠水费用户总和
	 * @param period
	 * @param searchCond
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal  getPastArrearsRecordTotal(@Param("period") String period, @Param("searchCond") String searchCond, @Param("creditSubjectList") List<String> creditSubjectList);
	
	public List<Map<String, Object>> searchYycBlockRecordList(@Param("period") String period, @Param("customerType") Integer customerType,@Param("debitCredit") String debitCredit);
	/**
	 * @Title: getWaterFeeCollect
	 * @Description: 水费收缴情况表
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public List<Map<String,Object>> getWaterFeeCollect(@Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getYearCleanAmount
	 * @Description: 获取本年清欠金额（等于当前年）
	 * @param year
	 * @return 
	 */
	public BigDecimal getYearCleanAmount(@Param("year") String year);
	/**
	 * @Title: getPastYearCleanAmount
	 * @Description: 获取往年清欠金额（小于当前年）
	 * @param year
	 * @return 
	 */
	public BigDecimal getPastYearCleanAmount(@Param("year") String year);
	
	/**
	 * @Title: getTaxInvoiceAmount
	 * @Description: 获取客户开票金额
	 * @param customerType
	 * @param period
	 * @param meterType
	 * @return 
	 */
	public BigDecimal getTaxInvoiceAmount(@Param("customerType") int customerType, @Param("period") String period, @Param("meterType") String meterType);
	
	/**
	 * @Title: getCzcRecord
	 * @Description: 营业处统计城中村收缴情况
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String,Object>> getCzcRecord(@Param("period") String period, @Param("priceCode") String priceCode, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getActualWaterFeeAmount
	 * @Description: 按期间统计应收水费金额（已开票）
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getActualWaterFeeAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
	/**
	 * @Title: getReceiveWaterFeeAmount
	 * @Description: 按期间统计实收水费金额（已开票）
	 * @param customerId
	 * @param period
	 * @param creditSubjectList
	 * @return 
	 */
	public BigDecimal getReceiveWaterFeeAmount(@Param("customerId") Long customerId, @Param("period") String period, @Param("creditSubjectList") List<String> creditSubjectList);
	
}