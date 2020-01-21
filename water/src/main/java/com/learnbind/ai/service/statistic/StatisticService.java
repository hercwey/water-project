package com.learnbind.ai.service.statistic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.StatisticTempTable;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface StatisticService extends IBaseService<StatisticTempTable, Long> {
    
	/**
	 * @Title: test
	 * @Description: 用于测试服务可用性
	 * @return 
	 */
	List<Map<String,Object>> test();
	
	/**
	 * @Title: searchCustomerPerMonth
	 * @Description: 查询指定月份客户列表
	 * @param period  月份 ,格式:yyyy-mm
	 * @return 
	 * 				[
	 * 					{},
	 * 					{}
	 * 				]
	 */
	List<Map<String,Object>> searchCustomerPerMonth(String period, String traceIds);
	
	
	/**
	 * @Title: searchCustomerPerMonth
	 * @Description: 查询指定年份客户列表
	 * @param period  月份 ,格式:yyyy
	 * @return 
	 * 				[
	 * 					{},
	 * 					{}
	 * 				]
	 */
	List<Map<String,Object>> searchCustomerPerYear(String period, String traceIds);
	
	
	/**
	 * @Title: statCustomerPerDay
	 * @Description: 统计月份每天新增客户数
	 * @param period  期间   yyyy-mm
	 * @return 
	 * 		[  </br>
	 * 			&nbsp;&nbsp;{	</br>
	 * 				&nbsp;&nbsp;&nbsp;&nbsp;SETTLE_TIME  &nbsp;    yyyy-mm-dd, </br>
	 * 				&nbsp;&nbsp;&nbsp;&nbsp;CUSTOMERAMOUNT  &nbsp;  数量
	 * 			&nbsp;&nbsp;},</br>
	 * 			{} </br>
	 * 		]
	 */
	List<Map<String,Object>> statCustomerPerDay(String period, String traceIds);
	/**
	 * @Title: statCustomerPerMohtn
	 * @Description: 统计年份每月新增客户数
	 * @param period  期间  yyyy
	 * @return 
	 * 	[
	 * 		{
	 * 			STATMONTH		yyyy-mm
	 * 			CUSTOMERAMOUNT	数量
	 * 		},
	 * 		{}
	 * ]
	 */
	List<Map<String,Object>> statCustomerPerMonth(String period, String traceIds);
	
	/**
	 * @Title: searchReadMeterRecordPerMonth
	 * @Description: 查询指定月份的抄表记录
	 * @param period  月份  yyyy-mm
	 * @return
	 * 	 [
	 * 		{
	 * 			C.PROP_NAME,	//用户名称
				C.PROP_TEL,		//用户电话
				C.ADDRESS,		//用户地址
				R.*           	//抄表记录 
	 * 		},
	 * 		{}
	 * ]
	 */
	List<Map<String,Object>> searchReadMeterRecordPerMonth(String period, String traceIds);
	
	/**
	 * @Title: searchReadMeterRecordPerYear
	 * @Description: 查询指定年份的抄表记录  (抄表时间=yyyy)
	 * @param period  年份  yyyy
	 * @return
	 * [
	 * 		{
	 * 			C.PROP_NAME,	//用户名称
				C.PROP_TEL,		//用户电话
				C.ADDRESS,		//用户地址
				R.*           	//抄表记录 
	 * 		},
	 * 		{}
	 * ] 
	 */
	List<Map<String,Object>> searchReadMeterRecordPerYear(String period, String traceIds);
	
	/**
	 * @Title: statReadRecordNumPerDay
	 * @Description: 统计指定期间每天的抄表数量
	 * @param period 期间  yyyy-mm
	 * @return 抄表数量列表   天--->抄表数量
	 * 		格式:
	 * 		[
	 * 			{
	 * 				READ_NUM:  1,
	 * 				READ_DATE: "2019-08-20"
	 * 			},
	 * 			{}
	 * 		]
	 */
	List<Map<String,Object>> statReadRecordNumPerDay(String period, String traceIds);
	
	/**
	 * @Title: statReadRecordNumPerMonth
	 * @Description: 统计指定期间每月的抄表数量
	 * @param period  期间  yyyy
	 * @return  抄表数量列表   月--->抄表数量
	 * 		格式:
	 * 		[
	 * 			{
	 * 				READ_NUM:  1,
	 * 				READ_DATE: "2019-01"
	 * 			},
	 * 			{}
	 * 		] 
	 */
	List<Map<String,Object>> statReadRecordNumPerMonth(String period, String traceIds);
	
	/**
	 * @Title: statReadRecordMonthSummary
	 * @Description: 抄表汇总-月
	 * @param period 期间  yyyy-mm
	 * @return 
	 * 		[
	 * 			{
	 * 				WATER_USE:JUMIN,
	 * 				READ_NUM:1
	 * 			},
	 * 			{}
	 * 		]
	 */
	List<Map<String,Object>> statReadRecordMonthSummary(String period, String traceIds);
	
	
	/**
	 * @Title: statReadRecordYearSummary
	 * @Description: 抄表汇总-年
	 * @param period 期间  yyyy
	 * @return 
	 * 		[
	 * 			{
	 * 				WATER_USE:JUMIN,
	 * 				READ_NUM:1
	 * 			},
	 * 			{}
	 * 		]
	 */
	List<Map<String,Object>> statReadRecordYearSummary(String period, String traceIds);
	
	/**
	 * @Title: statNeedReadMeterAmount
	 * @Description: 统计需要抄表的数量
	 * @return 
	 */
	int statNeedReadMeterAmount(String traceIds);  
	
	/**
	 * @Title: statHasReadMeterAmount
	 * @Description: 统计指定月份实抄表数量
	 * @param period  yyyy-mm
	 * @return 
	 */
	int statHasReadMeterAmount(String period, String traceIds);
	
	/**
	 * @Title: statHasReadMeterAmount
	 * @Description: 统计指定年份实抄表数量
	 * @param period  yyyy-mm
	 * @return 
	 */
	int statHasReadMeterYearAmount(String period, String traceIds);
	
	/**
	 * @Title: searchPartRecordPerMonth
	 * @Description: 查询指定月份的分水量列表
	 * @param period  yyyy-mm
	 * @return 
	 * 		[
	 * 			{},  //参见分水量实体记录
	 * 			{}
	 * 		]
	 */
	List<Map<String,Object>> searchPartRecordPerMonth(String traceIds, String period);
	
	/**
	 * @Title: statWaterAmountPerDay
	 * @Description: 统计一月当中每天的售水量
	 * @param period 期间   yyyy-mm
	 * @return 
	 * 		[
	 * 			{
	 * 				DAY_DATE:yyyy-mm-dd,
	 * 				WATER_AMOUNT:xxxx
	 * 			},
	 * 			{}
	 * 		]
	 * 
	 * 
	 */
	List<Map<String,Object>> statWaterAmountPerDay(String traceIds, String period);
	
	/**
	 * @Title: searchPartRecordPerYear
	 * @Description: 查询指定年份的分水量列表
	 * @param period  yyyy
	 * @return 
	 * 
	 * 		[
	 * 			{},  //参见分水量记录
	 * 			{}
	 * 		]
	 * 
	 */
	List<Map<String,Object>> searchPartRecordPerYear(String period, String traceIds);
	
	/**
	 * @Title: statWaterAmountPerYear
	 * @Description: 统计一年当中每月的售水量
	 * @param period 期间   yyyy
	 * @return 
	 * 		[
	 * 			{
	 * 				MONTH_DATE:yyyy-mm
	 * 				WATER_AMOUNT:xxxx
	 * 			},
	 * 			{}
	 * 		]
	 * 
	 * 
	 */
	List<Map<String,Object>> statWaterAmountPerMonth(String period, String traceIds);
	
	/**
	 * @Title: statWaterMonthSummary
	 * @Description: 按水价分类汇总售水量,指定月份
	 * @param period  yyyy-mm
	 * @return 
	 * 		[
	 * 			{
	 * 				PRICE_CODE:JUMIN,
	 * 				WATER_AMOUNT:XXXX
	 * 			},
	 * 			{}
	 * 		]
	 * 
	 */
	List<Map<String,Object>> statWaterMonthSummary(String period, String traceIds);
	
	
	/**
	 * @Title: getParentNodeListByMonth
	 * @Description: 获取指定月份抄表记录中所有表计的父结点
	 * @param period  期间  yyyy-mm
	 * @return 
	 * 		父结点ID列表
	 */
	List<Map<String,Object>> getParentNodeListByMonth(String period);
	
	
	/**
	 * @Title: getChildAmountOfParentNode
	 * @Description: 统计指定月份抄表记录中,所有父NODE下子节点的水量和
	 * @param period  时间   yyyy-mm
	 * @return 
	 * 		[
	 * 		{
	 * 			CHILD_SUM:  xxx,  此节点下所有直接子结点的水量和
	 * 			NODE_ID:   yyy   节点ID(非叶结点)  meterId
	 * 		},
	 * 		{}
	 * 		]
	 */
	List<Map<String,Object>> statChildAmountOfParentNode(String period);
	
	
	
	/**
	 * @Title: getParentNodeAmountListByMonth
	 * @Description: 获取非叶结点的水量(计算减免水量后)
	 * @param period  期间   yyyy-mm
	 * @return 
	 * 		[
		  		{
		  			REAL_AMOUNT:  xxx,  此节点水量和
		  			METER_RECORD.*      //其中包括相应的METER_TREE_ID   		
		  		},
		  		{}
	 * 		]
	 */
	List<Map<String,Object>> getParentNodeAmountListByMonth(String period);
	
	/**
	 * @Title: searchReceivableList
	 * @Description: 查询指定期间列表
	 * @param periodStart  起始期间
	 * @param periodEnd    终止期间
	 * @return 
	  		[
	  			{
	  				C.CUSTOMER_NAME,   //用户名称
					C.ADDRESS,		   //用户地址
					C.ROOM,			   //用户房号
					P.START_TIME,      //抄表起始时间
					P.END_TIME,		   //抄表结束时间
					P.BASE_PRICE,	   //水价-基础水价
					P.TREATMENT_FEE,   //水价-污水处理费
					P.WATER_PRICE,     //水价-综合水价=基础水价+污水处理费
					P.WATER_AMOUNT,	   //水量	
					P.WATER_AMOUNT * P.BASE_PRICE BASE_AMOUNT,  //基础水价-水费
					P.WATER_AMOUNT * P.TREATMENT_FEE TREATMENT_AMOUNT,//污水处理费-水费
					A.CREDIT_AMOUNT   //自帐务中获取的应收水费	  
	  			},
	  			{}
	  		]
	 */
	List<Map<String,Object>> searchReceivableList(String periodStart,String periodEnd, String traceIds);
	
	/**
	 * @Title: statReceivableSum
	 * @Description: 统计指定期间应收合计值
	 * @param periodStart  起始期间  yyyy-mm
	 * @param periodEnd   结束期间  yyyy-mm
	 * @return 
	 * 		[
	 * 			{
	 * 				BASE_AMOUNT_SUM:"",
	 * 				TREATMENT_AMOUNT_SUM:"",
	 * 				CREDIT_AMOUNT_SUM:""
	 * 			},
	 * 			{}
	 * 		]
	 */
	List<Map<String,Object>> statReceivableSum(String periodStart, String periodEnd, String traceIds);
	
	
	/**
	 * @Title: searchActualReceList
	 * @Description: 实收列表  按基础水价,污水处理费
	 * @param periodStart  起始期间  yyyy-mm
	 * @param periodEnd  结束期间  yyyy-mm
	 * @return 
	  	[
			  	{
			  		C.CUSTOMER_NAME,
					C.ADDRESS,
					C.ROOM,
					A.PERIOD,
					A.DEBIT_AMOUNT AMOUNT,  
					AMOUNT_TYPE     //TREATMENT_AMOUNT-水费(污水费)  BASE_AMOUNT-水费(基价)					
			    },
			    {}
	   ]
	 */
	List<Map<String,Object>> searchActualReceList(String periodStart, String periodEnd, String traceIds);
	
	/**
	 * @Title: searchDebtDetailList
	 * @Description: 查询欠费详情.(用户列表)
	 * @param periodStart
	 * @param periodEnd
	 * @return 
	  
	        C.CUSTOMER_NAME,
			C.ADDRESS,
			C.ROOM,	
			A.PERIOD,
			A.CREDIT_AMOUNT-A.DEBIT_AMOUNT DEBT_AMOUNT
	  
	 */
	List<Map<String,Object>> searchDebtDetailList(String periodStart,String periodEnd, String traceIds);
	
	/**
	 * @Title: searchDebtStatList
	 * @Description: 统计指定期间的欠费金额
	 * @param periodStart  起始期间 yyyy-mm
	 * @param periodEnd  结束期间  yyyy-mm
	 * @return 
	 * 		[
	 * 			{DEBT_AMOUNT_SUM}
	 * 		]
	 */
	List<Map<String,Object>>  statDebtSum(String periodStart,String periodEnd, String traceIds);
	
	
	/**
	 * @Title: statDebtSummaryByPriceCode
	 * @Description: 按价格代码对欠费进行分类
	 * @param periodStart  起始期间 yyyy-mm
	 * @param periodEnd  结束期间  yyyy-mm
	 * @return 
	 * 	[
	 * 		{
	 * 			PRICE_CODE: xxx,
	 * 			STAT_AMOUNT:nnn
	 * 		},
	 * 		{}
	 *  ]
	 */
	List<Map<String,Object>>  statDebtSummaryByPriceCode(String periodStart,String periodEnd);
	
	/**
	 * @Title: getListGroupByCustomerId
	 * @Description: 统计客户欠费
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param startDate
	 * @param endDate
	 * @return 
	 * 		I.CUSTOMER_ID,	客户ID
			LISTAGG ( I.ID, ',' ) WITHIN GROUP ( ORDER BY I.ID ) AS BILL_IDS,	账单ID,多条记录用逗号","分隔
			I.PERIOD,	期间
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,	账单金额
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,	已充值金额
			( SUM(I.CREDIT_AMOUNT) - SUM(I.DEBIT_AMOUNT) ) AS OWE_AMOUNT	欠费金额
	 */
	public List<Map<String, Object>> getListGroupByCustomerId(String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate);
	
	/**
	 * @Title: getListGroupByPriceCode
	 * @Description: 根据条件查询并按priceCode分组统计
	 * @param traceIds
	 * @param period
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> getListGroupByPriceCode(String traceIds, String period, String startDate, String endDate);
	
	/**
	 * @Title: getWaterFeeOweBillList
	 * @Description: 获取本期水费欠费账单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<CustomerAccountItem> getWaterFeeOweBillList(Long customerId, String period);
	
	/**
	 * @Title: getOverdueOweBillList
	 * @Description: 获取本期违约金欠费账单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<CustomerAccountItem> getOverdueOweBillList(Long customerId, String period);
	
	/**
	 * @Title: getWaterFeeOweAmount
	 * @Description: 获取本期水费欠费金额
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getWaterFeeOweAmount(Long customerId, String period);
	
	/**
	 * @Title: getOverdueOweAmount
	 * @Description: 获取本期违约金欠费金额
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getOverdueOweAmount(Long customerId, String period);
	
	/**
	 * @Title: getPastWaterFeeOweBillList
	 * @Description: 获取往期水费欠费账单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<CustomerAccountItem> getPastWaterFeeOweBillList(Long customerId, String period);
	
	/**
	 * @Title: getPastOverdueOweBillList
	 * @Description: 获取往期违约金欠费账单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<CustomerAccountItem> getPastOverdueOweBillList(Long customerId, String period);
	
	/**
	 * @Title: getPastWaterFeeOwe
	 * @Description: 获取往期水费欠费金额
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getPastWaterFeeOweAmount(Long customerId, String period);
	
	/**
	 * @Title: getPastOverdueOweAmount
	 * @Description: 获取往期违约金欠费金额
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getPastOverdueOweAmount(Long customerId, String period);
	
	/**
	 * @Title: getWaterFeeOwePeriod
	 * @Description: 获取水费欠费月份（包含当前期间）
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<String> getWaterFeeOwePeriod(Long customerId, String period);
	/**
	 * @Title: getOverdueOwePeriod
	 * @Description: 获取违约金欠费月份（包含当前期间）
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<String> getOverdueOwePeriod(Long customerId, String period);
	
	/**
	 * @Title: searchYycArrearsList
	 * @Description:获取营业处统计的欠费表信息
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<Map<String, Object>> searchYycArrearsList(String period, Integer customerType);
	
	/**
	 * @Title: searchYycArrearsTotal
	 * @Description: 获取欠费总和
	 * @param period
	 * @param customerType
	 * @return 
	 */
	public BigDecimal searchYycArrearsTotal(String period, Integer customerType);
	
	/**
	 * @Title: searchYycArrearsList
	 * @Description:获取营业处统计的欠费表信息(小区)
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<Map<String, Object>> searchYycBlockArrearsList(String period, Integer customerType);
	
	/**
	 * @Title: searchYycCleanList
	 * @Description: 获取营业处统计的清欠表信息
	 * @param period
	 * @param customerType
	 * @return 
	 */
	public List<Map<String, Object>> searchYycCleanList(String period, Integer customerType);
	
	/**
	 * @Title: searchYycCleanTotal
	 * @Description: 获取清欠总和
	 * @param period
	 * @param customerType
	 * @return 
	 */
	public BigDecimal searchYycCleanTotal(String period, Integer customerType);
	
	public BigDecimal getCurrCleanAmount(Long customerId, String period, Integer customerType);
	
	/**
	 * @Title: getCurrCleanBlockAmount
	 * @Description: 查询小区当月清欠
	 * @param customerType
	 * @param period
	 * @return 
	 */
	public BigDecimal getCurrCleanBlockAmount(Integer customerType, String period);
	
	/**
	 * @Title: searchYycBlockCleanList
	 * @Description: 获取营业处统计的清欠（小区）
	 * @param period
	 * @param customerType
	 * @return 
	 */
	public List<Map<String, Object>> searchYycBlockCleanList(String period, Integer customerType);
	
	
	/**
	 * @Title: geActualSummary
	 * @Description: 实收水价分类汇总
	 * @param traceIds
	 * @param periodStart
	 * @param periodEnd
	 * @return 
	 */
	public List<Map<String, Object>> geActualSummary(String traceIds, String periodStart, String periodEnd);
	
	/**
	 * @Title: statCompareYearAmount
	 * @Description: T年售水量对比统计table
	 * @param periodStart
	 * @param periodEnd
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String,Object>> statCompareYearAmount(String periodStart, String periodEnd,String traceIds);
	/**
	 * @Title: searchCompareYearAmount
	 * @Description: 年售水量对比统计
	 * @param periodStart
	 * @param periodEnd
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String,Object>> searchCompareYearAmount(String periodStart, String periodEnd,String traceIds);
	
	/**
	 * @Title: statNeedReadMeterYear
	 * @Description: 获取年抄表率应抄
	 * @param period
	 * @param traceIds
	 * @return 
	 */
	int statNeedReadMeterYear(String period, String traceIds);
	
	/**
	 * @Title: getPastArrearsRecord
	 * @Description: 获取往年拖欠水费用户登记
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String,Object>> getPastArrearsRecord(String period, String searchCond);
	
	/**
	 * @Title: getPastArrearsRecordTotal
	 * @Description: 获取往年拖欠水费用户总和
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public BigDecimal getPastArrearsRecordTotal(String period, String searchCond);
	
	public List<Map<String, Object>> searchYycBlockRecordList(String period, Integer customerType);
	
	/**
	 * @Title: getWaterFeeCollect
	 * @Description: 水费收缴情况表
	 * @param period
	 * @return 
	 */
	public List<Map<String,Object>> getWaterFeeCollect(String period);
	
	/**
	 * @Title: getPastYearCleanAmount
	 * @Description: 获取本年清欠金额（等于当前年）
	 * @param period
	 * @return 
	 */
	public BigDecimal getYearCleanAmount(String year);
	/**
	 * @Title: getPastYearCleanAmount
	 * @Description: 获取往年清欠金额（小于当前年）
	 * @param period
	 * @return 
	 */
	public BigDecimal getPastYearCleanAmount(String year);
	
	/**
	 * @Title: getPersonCustomerTaxInvoiceAmount
	 * @Description: 获取个人客户开票金额
	 * @param period
	 * @param meterType
	 * @return 
	 */
	public BigDecimal getPersonCustomerTaxInvoiceAmount(String period, String meterType);
	
	/**
	 * @Title: getCompanyCustomerTaxInvoiceAmount
	 * @Description: 获取单位客户开票金额
	 * @param period
	 * @param meterType
	 * @return 
	 */
	public BigDecimal getCompanyCustomerTaxInvoiceAmount(String period, String meterType);
	
	/**
	 * @Title: getCzcRecord
	 * @Description: 营业处统计城中村收缴情况
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String,Object>> getCzcRecord(String period, String priceCode);
	
	/**
	 * @Title: getActualWaterFeeAmount
	 * @Description: 按期间查询应收水费金额（已开票）
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getActualWaterFeeAmount(Long customerId, String period);
	
	/**
	 * @Title: getReceiveWaterFeeAmount
	 * @Description: 按期间统计实收水费金额（已开票）
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getReceiveWaterFeeAmount(Long customerId, String period);
}
