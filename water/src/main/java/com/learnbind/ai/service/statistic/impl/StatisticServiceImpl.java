package com.learnbind.ai.service.statistic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.dao.StatisticTempTableMapper;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.StatisticTempTable;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.service.statistic.impl
 *
 * @Title: StatisticServiceImpl.java
 * @Description: 统计服务
 *
 * @author lenovo
 * @date 2019年9月3日 上午12:00:05
 * @version V1.0 
 *
 */
@Service
public class StatisticServiceImpl extends AbstractBaseService<StatisticTempTable, Long> implements  StatisticService {
	
	@Autowired
	public StatisticTempTableMapper statisticTempTableMapper;
	@Autowired
	private CustomerAccountItemService customerAccountItemService;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public StatisticServiceImpl(StatisticTempTableMapper mapper) {
		this.statisticTempTableMapper=mapper;
		this.setMapper(mapper);
	}


	@Override
	public List<Map<String, Object>> test() {
		return statisticTempTableMapper.test();		
	}


	@Override
	public List<Map<String, Object>> statCustomerPerDay(String period, String traceIds) {		
		return statisticTempTableMapper.statCustomerPerDay(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> statCustomerPerMonth(String period, String traceIds) {
		return statisticTempTableMapper.statCustomerPerMonth(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> searchCustomerPerMonth(String period, String traceIds) {
		return statisticTempTableMapper.searchCustomerPerMonth(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> searchCustomerPerYear(String period, String traceIds) {
		return statisticTempTableMapper.searchCustomerPerYear(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> searchReadMeterRecordPerMonth(String period, String traceIds) {
		return statisticTempTableMapper.searchReadMeterRecordPerMonth(period, traceIds);
	}

	@Override
	public List<Map<String, Object>> searchReadMeterRecordPerYear(String period, String traceIds) {
		return statisticTempTableMapper.searchReadMeterRecordPerYear(period, traceIds);
	}
	

	@Override
	public List<Map<String, Object>> statReadRecordNumPerDay(String period, String traceIds) {
		return statisticTempTableMapper.statReadRecordNumPerDay(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> statReadRecordNumPerMonth(String period, String traceIds) {
		return statisticTempTableMapper.statReadRecordNumPerMonth(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> statReadRecordMonthSummary(String period, String traceIds) {
		return statisticTempTableMapper.statReadRecordMonthSummary(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> statReadRecordYearSummary(String period, String traceIds) {
		return statisticTempTableMapper.statReadRecordYearSummary(period, traceIds);
		
	}


	@Override
	public int statNeedReadMeterAmount(String traceIds) {
		
		return statisticTempTableMapper.statNeedReadMeterAmount(traceIds);
	}


	@Override
	public int statHasReadMeterAmount(String period, String traceIds) {
		return statisticTempTableMapper.statHasReadMeterAmount(period, traceIds);
	}
	
	@Override
	public int statHasReadMeterYearAmount(String period, String traceIds) {
		return statisticTempTableMapper.statHasReadMeterYearAmount(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> searchPartRecordPerMonth(String traceIds, String period) {
		return statisticTempTableMapper.searchPartRecordPerMonth(traceIds, period);
	}


	@Override
	public List<Map<String, Object>> statWaterAmountPerDay(String traceIds, String period) {
		return statisticTempTableMapper.statWaterAmountPerDay(traceIds, period);
	}


	@Override
	public List<Map<String, Object>> searchPartRecordPerYear(String period, String traceIds) {
		return statisticTempTableMapper.searchPartRecordPerYear(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> statWaterAmountPerMonth(String period, String traceIds) {
		return statisticTempTableMapper.statWaterAmountPerMonth(period, traceIds);
	}


	@Override
	public List<Map<String, Object>> statWaterMonthSummary(String period, String traceIds) {
		return statisticTempTableMapper.statWaterMonthSummary(period, traceIds);	
	}
	
	@Override
	public List<Map<String,Object>> getParentNodeListByMonth(String period){
		return statisticTempTableMapper.getParentNodeListByMonth(period);
	}


	@Override
	public List<Map<String, Object>> statChildAmountOfParentNode(String period) {
		return statisticTempTableMapper.statChildAmountOfParentNode(period);
	}


	@Override
	public List<Map<String, Object>> getParentNodeAmountListByMonth(String period) {
		return statisticTempTableMapper.getParentNodeAmountListByMonth(period);
	}


	@Override
	public List<Map<String, Object>> searchReceivableList(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.searchReceivableList(periodStart,periodEnd, traceIds);
	}


	@Override
	public List<Map<String, Object>> statReceivableSum(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.statReceivableSum(periodStart,periodEnd, traceIds);
	}


	@Override
	public List<Map<String, Object>> searchActualReceList(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.searchActualReceList(periodStart,periodEnd, traceIds);
	}

	@Override
	public List<Map<String, Object>> searchDebtDetailList(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.searchDebtDetailList(periodStart,periodEnd, traceIds);
	}

	@Override
	public List<Map<String, Object>> statDebtSum(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.statDebtSum(periodStart,periodEnd, traceIds);
	}


	@Override
	public List<Map<String, Object>> statDebtSummaryByPriceCode(String periodStart, String periodEnd) {
		return statisticTempTableMapper.statDebtSummaryByPriceCode(periodStart,periodEnd);
	}


	@Override
	public List<Map<String, Object>> getListGroupByCustomerId(String searchCond, String traceIds, String period,
			Integer deductMode, String startDate, String endDate) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return statisticTempTableMapper.getListGroupByCustomerId(searchCond, traceIds, period, deductMode, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), startDate, endDate);
	}
	
	@Override
	public List<Map<String, Object>> getListGroupByPriceCode(String traceIds, String period, String startDate, String endDate) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getListGroupByPriceCode(traceIds, period, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), startDate, endDate);
	}

	@Override
	public List<CustomerAccountItem> getWaterFeeOweBillList(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getOweBillList(customerId, period, creditSubjectList);
	}

	@Override
	public List<CustomerAccountItem> getOverdueOweBillList(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return statisticTempTableMapper.getOweBillList(customerId, period, creditSubjectList);
	}
	
	@Override
	public BigDecimal getWaterFeeOweAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getOweTotalAmount(customerId, period, creditSubjectList);
	}

	@Override
	public BigDecimal getOverdueOweAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return statisticTempTableMapper.getOweTotalAmount(customerId, period, creditSubjectList);
	}

	@Override
	public List<CustomerAccountItem> getPastWaterFeeOweBillList(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getPastOweBillList(customerId, period, creditSubjectList);
	}

	@Override
	public List<CustomerAccountItem> getPastOverdueOweBillList(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return statisticTempTableMapper.getPastOweBillList(customerId, period, creditSubjectList);
	}

	@Override
	public BigDecimal getPastWaterFeeOweAmount(Long customerId, String period) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getPastOweTotalAmount(customerId, period, creditSubjectList);
	}

	@Override
	public BigDecimal getPastOverdueOweAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return statisticTempTableMapper.getPastOweTotalAmount(customerId, period, creditSubjectList);
	}
	
	@Override
	public List<String> getWaterFeeOwePeriod(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getWaterFeeOwePeriod(customerId, period, creditSubjectList);
	}

	@Override
	public List<String> getOverdueOwePeriod(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return statisticTempTableMapper.getOverdueOwePeriod(customerId, period, creditSubjectList);
	}


	@Override
	public List<Map<String, Object>> searchYycArrearsList(String period, Integer customerType) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.searchYycArrearsList(period,customerType, creditSubjectList);
	}


	@Override
	public BigDecimal searchYycArrearsTotal(String period, Integer customerType) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.searchYycArrearsTotal(period,customerType, creditSubjectList);
	}
	
	@Override
	public List<Map<String, Object>> searchYycBlockArrearsList(String period, Integer customerType) {
		String debitCredit = EnumAiDebitCreditStatus.CREDIT.getKey();
		return statisticTempTableMapper.searchYycBlockArrearsList(period,customerType, debitCredit);
	}
	
	@Override
	public List<Map<String, Object>> searchYycCleanList(String period, Integer customerType) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.searchYycCleanList(period,customerType, creditSubjectList);
	}


	@Override
	public BigDecimal searchYycCleanTotal(String period, Integer customerType) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.searchYycCleanTotal(period,customerType, creditSubjectList);
	}
	
	@Override
	public List<Map<String, Object>> searchYycBlockCleanList(String period, Integer customerType) {
		String debitCredit = EnumAiDebitCreditStatus.CREDIT.getKey();
		return statisticTempTableMapper.searchYycBlockCleanList(period,customerType, debitCredit);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCurrCleanAmount
	 * @Description: 获取本月清欠
	 * @param customerId
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.statistic.StatisticService#getCurrCleanAmount(java.lang.Long, java.lang.String)
	 */
	@Override
	public BigDecimal getCurrCleanAmount(Long customerId, String period, Integer customerType) {
		return statisticTempTableMapper.getCurrCleanAmount(customerId,period, customerType);
	}
	
	@Override
	public BigDecimal getCurrCleanBlockAmount(Integer customerType, String period) {
		return statisticTempTableMapper.getCurrCleanBlockAmount(customerType,period);
	}
	
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: geActualSummary
	 * @Description: 实收水价分类汇总
	 * @param traceIds
	 * @param periodStart
	 * @param periodEnd
	 * @return 
	 * @see com.learnbind.ai.service.statistic.StatisticService#geActualSummary(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> geActualSummary(String traceIds, String periodStart, String periodEnd) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.geActualSummary(traceIds, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), periodStart, periodEnd);
	}


	@Override
	public List<Map<String, Object>> statCompareYearAmount(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.statCompareYearAmount(periodStart, periodEnd, traceIds);
	}
	
	@Override
	public List<Map<String, Object>> searchCompareYearAmount(String periodStart, String periodEnd, String traceIds) {
		return statisticTempTableMapper.searchCompareYearAmount(periodStart, periodEnd, traceIds);
	}


	@Override
	public int statNeedReadMeterYear(String period, String traceIds) {
		return statisticTempTableMapper.statNeedReadMeterYear(period, traceIds);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getPastArrearsRecord
	 * @Description: 获取往年拖欠水费用户登记
	 * @param period
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.statistic.StatisticService#getPastArrearsRecord(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getPastArrearsRecord(String period, String searchCond) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getPastArrearsRecord(period,searchCond, creditSubjectList);
	}


	@Override
	public BigDecimal getPastArrearsRecordTotal(String period, String searchCond) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getPastArrearsRecordTotal(period,searchCond, creditSubjectList);
	}
	
	@Override
	public List<Map<String, Object>> searchYycBlockRecordList(String period, Integer customerType) {
		String debitCredit = EnumAiDebitCreditStatus.CREDIT.getKey();
		return statisticTempTableMapper.searchYycBlockRecordList(period,customerType, debitCredit);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getWaterFeeCollect
	 * @Description: 水费收缴情况表
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.statistic.StatisticService#getWaterFeeCollect(java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getWaterFeeCollect(String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getWaterFeeCollect(period,creditSubjectList);
	}
	
	@Override
	public BigDecimal getYearCleanAmount(String year) {
		BigDecimal cleanAmount = new BigDecimal(0.00);
		BigDecimal amount = statisticTempTableMapper.getYearCleanAmount(year);
		if(amount!=null) {
			cleanAmount = amount;
		}
		return cleanAmount;
	}
	
	@Override
	public BigDecimal getPastYearCleanAmount(String year) {
		BigDecimal cleanAmount = new BigDecimal(0.00);
		BigDecimal amount = statisticTempTableMapper.getPastYearCleanAmount(year);
		if(amount!=null) {
			cleanAmount = amount;
		}
		return cleanAmount;
	}

	@Override
	public BigDecimal getPersonCustomerTaxInvoiceAmount(String period, String meterType) {
		int customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();//客户类型：个人
		BigDecimal invoiceAmount = new BigDecimal(0.00);
		BigDecimal amount = statisticTempTableMapper.getTaxInvoiceAmount(customerType, period, meterType);
		if(amount!=null) {
			invoiceAmount = amount;
		}
		return invoiceAmount;
	}

	@Override
	public BigDecimal getCompanyCustomerTaxInvoiceAmount(String period, String meterType) {
		int customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();//客户类型：单位
		BigDecimal invoiceAmount = new BigDecimal(0.00);
		BigDecimal amount = statisticTempTableMapper.getTaxInvoiceAmount(customerType, period, meterType);
		if(amount!=null) {
			invoiceAmount = amount;
		}
		return invoiceAmount;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCzcRecord
	 * @Description:  营业处统计城中村收缴情况
	 * @param period
	 * @param priceCode
	 * @return 
	 * @see com.learnbind.ai.service.statistic.StatisticService#getCzcRecord(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getCzcRecord(String period, String priceCode) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return statisticTempTableMapper.getCzcRecord(period, priceCode, creditSubjectList);
	}
	
	@Override
	public BigDecimal getActualWaterFeeAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		BigDecimal actualAmount = new BigDecimal(0.00);
		BigDecimal amount = statisticTempTableMapper.getActualWaterFeeAmount(customerId, period, creditSubjectList);
		if(amount!=null) {
			actualAmount = amount; 
		}
		return actualAmount;
	}

	@Override
	public BigDecimal getReceiveWaterFeeAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		BigDecimal receiveAmount= new BigDecimal(0.00);
		BigDecimal amount = statisticTempTableMapper.getReceiveWaterFeeAmount(customerId, period, creditSubjectList);
		if(amount!=null) {
			receiveAmount = amount; 
		}
		return receiveAmount;
	}
	
}
