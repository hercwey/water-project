package com.learnbind.ai.service.customers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.cmbc.CMBCAutoDeductReceiptBean;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: CustomerAccountItemService.java
 * @Description: 客户账目
 *
 * @author Thinkpad
 * @date 2019年6月3日 下午4:15:03
 * @version V1.0 
 *
 */
public interface CustomerAccountItemService extends IBaseService<CustomerAccountItem, Long> {

	/**
	 * @Title: searchCustomerAccountItem
	 * @Description: 查询客户账目
	 * @param period
	 * @param traceIds
	 * @param searchCond
	 * @param operatorId
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerAccountItem(String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, Long operatorId, String startDate, String endDate);
	
    /**
     * @Title: insertCustomerAccountItem
     * @Description: 增加
     * @param accountItem
     * @return 
     */
    public int insertCustomerAccountItem(Long customerId, String period, Integer debitCredit, String digest, Long waterPriceId, BigDecimal amount, Long operatorId);
    
    /**
     * @Title: updateCustomerAccountItem
     * @Description: 修改
     * @param accountItem
     * @return 
     */
    public int updateCustomerAccountItem(CustomerAccountItem accountItem);
    
    /**
     * @Title: deleteCustomerAccountItem
     * @Description: 删除客户账单
     * @param accountItemId
     * @return 
     */
    public int deleteCustomerAccountItem(long accountItemId);

    /**
     * @Title: findAllCustomerAccountItem
     * @Description: 分页查询客户账目
     * @param pageNum
     * @param pageSize
     * @return 
     */
    PageInfo<CustomerAccountItem> findAllCustomerAccountItem(int pageNum, int pageSize);
    
    /**
     * @Title: generateBill
     * @Description: 生成账单
     * @param customerId
     * @param period
     * @param operatorId
     * @return 
     */
    public int generateOverdueBill(Long accountItemId, Long operatorId);
    
	/**
	 * @Title: getOverdueValueSum
	 * @Description: 查询某账单违约金之和
	 * @param accountItemId
	 * @return 
	 */
	public BigDecimal getOverdueValueSum(Long accountItemId);
	
	/**
	 * @Title: getOverdueBillList
	 * @Description: 查询某账单的违约金账单集合，把账单ID做为PID即可查询
	 * @param accountItemId
	 * @return 
	 */
	public List<CustomerAccountItem> getOverdueBillList(Long accountItemId);
	
	/**
	 * @Title: getRechargeOverdueValueSum
	 * @Description: 查询某账单已充值违约金之和
	 * @param accountItemId
	 * @return 
	 */
	public BigDecimal getRechargeOverdueValueSum(Long accountItemId);
	
	/**
	 * @Title: getOverdueBillOweAmountSum
	 * @Description: 查询某账单违约金欠费金额之和
	 * @param accountItemId
	 * @return 
	 */
	public BigDecimal getOverdueBillOweAmountSum(Long accountItemId);
    
    /**
     * @Title: settleAccount
     * @Description: 结算生成新的账单
     * @param accountItemId	账单ID
     * @param operatorId	操作员ID
     * @param settleAmount	结算金额
     * @param debitSubject	借方科目
     * @param debitDigest	借方摘要
     * @return 
     */
    public int settleAccount(Long accountItemId, Long operatorId, BigDecimal settleAmount, String debitSubject, String debitDigest);
    
    /**
     * @Title: getCurrBillOwedAmount
     * @Description: 查询某客户水费账单和违约金账单的欠费金额之和
     * @param customerId
     * @return 
     */
    public BigDecimal getCurrBillOwedAmount(Long customerId);
    
	/**
	 * @Title: getBillArrears
	 * @Description: 获取欠费账单
	 * @return 
	 */
	/**
	 * @Title: getBillArrearsList
	 * @Description: 根据条件查询账单
	 * @param searchCond
	 * @param operatorId
	 * @return 
	 */
	public List<CustomerAccountItem> getBillArrearsList(String searchCond, Long operatorId);
	
	
	/**
	 * @Title: settleOverdueAccount
	 * @Description: 违约金减免生成充值违约金账单
	 * @param accountItemId
	 * @param creditDigest
	 * @param operatorId
	 * @param settleAmount
	 * @return 
	 */
	public int settleOverdueAccount(Long accountItemId, String creditDigest, Long operatorId, BigDecimal settleAmount);
	
	/**
	 * @Title: cmbcSettlementChargeOff
	 * @Description: 中国民生银行扣费后结算销账
	 * @param receiptList
	 * @param period
	 * @return 
	 */
	public int cmbcSettlementChargeOff(List<CMBCAutoDeductReceiptBean> receiptList, String period);
	
	/**
	 * @Title: searchCustomerRecharge
	 * @Description: 查询客户充值账单
	 * @param searchCond
	 * @return 
	 */
	public List<CustomerAccountItem> searchCustomerRecharge(String searchCond);
	
	/**
	 * @Title: saveSubAccount
	 * @Description: 保存分账
	 * @param accountItemList
	 * @param accountItemId
	 * @param waterAmountList	分账的水量集合
	 * @return 
	 */
	public int saveSubAccount(List<CustomerAccountItem> accountItemList, Long accountItemId, List<BigDecimal> waterAmountList);
	
	
	/**
	 * @Title: selectNewCustomerAccountItem
	 * @Description: 查询分账单之后产生的新账单
	 * @param customerId
	 * @param period
	 * @param debitCredit
	 * @return 
	 */
	public List<CustomerAccountItem> selectNewCustomerAccountItem(Long customerId, String period, String debitCredit);
	
	/**
	 * @Title: saveCancelSubAccount
	 * @Description: 保存撤销分账单
	 * @param accountItemId
	 * @return 
	 */
	public int saveCancelSubAccount(Long accountItemId);
	
	/**
	 * 查询用户账单 
	 * @param customerId  客户ID
	 * @return
	 */
	public List<Map<String, Object>> getCustomerBillList(Long customerId);
	
	/**
	 * 自帐目表查询用户某段时间的缴费记录
	 * @param customerId  客户ID
	 * @param startDate   缴费时间-起始条件   
	 * @param endDate	     缴费时间-结束条件 
	 * @return				
	 */
	public List<Map<String,Object>> getCustomerFeeList(Long customerId,String startDate,String endDate);
	
	
	 /** @Title: searchCustomerAccountItem
	 * @Description: 异常水费报警
	 * @param period
	 * @param locationCode
	 * @param searchCond
	 * @param operatorId
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerAccountItemErrorFee(String period, String locationCode, Integer settlementStatus, String searchCond, Long operatorId, String startDate, String endDate);
	
	/**
	 * @Title: searchCustomerAccountItem
	 * @Description: 查询客户账目
	 * @param period
	 * @param locationCode
	 * @param searchCond
	 * @param operatorId
	 * @return 
	 */
	public List<Map<String, Object>> getCustomersListByLocation(String period, String traceIds, Integer settlementStatus, String searchCond, Integer customerType);
	
	public BigDecimal getwaterFeeArrears(Long customerId, String period);
	
	
	/**
	 * @param customerId
	 * @param period
	 * @return
	 * 查询客户所有账单
	 */
	public List<CustomerAccountItem> getAllCustomerAccountItemByPeriod(Long customerId, String period);
	
	/**
	 * 查询指定客户账户余额
	 * @param customerId  客户ID
	 * @return 账户余额
	 */
	public BigDecimal getCustomerBalance(Long customerId);
	
	/**
	 * @Title: getHaveBalanceRechargeBill
	 * @Description: 查询某客户有余额的充值账单
	 * 					借/贷状态为借
	 * 					借方科目为 11（其中第一位表示借/贷，第二位表示10、11、12的第一位；10、11、12分别表示：10-交水费（综价）11-交水费（基价） 12-交污水处理费）
	 * 					借方金额!=贷方金额
	 * 					借方金额-贷方金额>0
	 * 					
	 * @param customerId
	 * @return 
	 */
	public List<CustomerAccountItem> getHaveBalanceRechargeBill(Long customerId);
	
	/**
	 * @Title: getHaveBalanceWaterFeeBill
	 * @Description: 查询某客户有余额的充值账单（不包含分账账单）
	 * 					借/贷状态为贷
	 * 					贷方科目为 201（其中第一位表示借/贷，第二、三位表示水费；不包含分账账单）
	 * 					贷方金额!=借方金额
	 * 					贷方金额-借方金额<0
	 * 					
	 * @param customerId
	 * @return 
	 */
	public List<CustomerAccountItem> getHaveBalanceWaterFeeBill(Long customerId);
	
	/**
	 * @Title: generatorNonstandardBill
	 * @Description: 生成标准抄表账单（如果欠费金额有为负值时，自动结算本账单）
	 * @param pw
	 * @return 
	 */
	public Long generatorStandardBill(PartitionWater pw);
	
	/**
	 * @Title: generatorBill
	 * @Description: 生成水费账单
	 * @param pw
	 * @return 
	 */
	public Long generatorBill(PartitionWater pw);
	
	/**
	 * @Title: generatorRechargeBill
	 * @Description: 生成充值记录账单
	 * @param customerId
	 * @param rechargeAmount
	 * @param subjectAction		科目动作
	 * @param subjectPayment	科目支付方式
	 * @return 
	 * 		返回充值记录ID
	 */
	public Long generatorRechargeBill(Long customerId, BigDecimal rechargeAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Long operatorId);

	/**
	 * @Title: settleCustomerBill
	 * @Description: 结算客户账单（按客户结算）
	 * @param customerId
	 * @param period
	 * @param operatorId
	 * @param settleAmount
	 * @param subjectAction
	 * @param subjectPayment
	 * @param paymentType	缴费类型，1=充值缴费；2=清欠缴费
	 * @param traceOperate
	 * @return 
	 */
	public int settleCustomerBill(Long customerId, String period, Long operatorId, BigDecimal settleAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Integer paymentType, String traceOperate);
	
	/**
	 * @Title: balanceSettleCustomerBill
	 * @Description: 余额结算客户账单
	 * @param customerId
	 * @param operatorId
	 * @param subjectAction
	 * @param subjectPayment
	 * @param traceOperate
	 * @return 
	 */
	public int balanceSettleCustomerBill(Long customerId, Long operatorId, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, String traceOperate);
	
	/**
	 * @Title: settleAccount
	 * @Description: 结算单个账单（按账单结算）
	 * @param accountItemId
	 * @param operatorId
	 * @param settleAmount
	 * @param subjectAction		科目动作
	 * @param subjectPayment	科目支付方式
	 * @param paymentType		缴费类型，1=充值缴费；2=清欠缴费
	 * @param traceOperate
	 * @return 
	 */
	public int settleAccount(Long accountItemId, Long operatorId, BigDecimal settleAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Integer paymentType, String traceOperate);
	
	/**
	 * @Title: balanceSettleAccount
	 * @Description: 余额结算账单
	 * @param accountItemId
	 * @param operatorId
	 * @param subjectAction
	 * @param subjectPayment
	 * @param traceOperate
	 * @return 
	 */
	public int balanceSettleAccount(Long accountItemId, Long operatorId, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, String traceOperate);
	
	/**
	 * @Title: searchCcbCustomerAccountItem
	 * @Description: 建设银行代扣查询客户账单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @return 
	 * 
	 * 		
	 */
	public List<Map<String, Object>> searchCcbCustomerAccountItem(String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate);
	/**
	 * @Title: getListGroupByCustomerId
	 * @Description: 获取客户账单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param startDate
	 * @param endDate
	 * @return 
	 * 		I.CUSTOMER_ID,
			C.CUSTOMER_NAME, 
			I.PERIOD,
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,
			( SUM(I.CREDIT_AMOUNT-I.DEBIT_AMOUNT) ) AS OWE_AMOUNT, 
			LM.TRACE_IDS 
	 */
	public List<Map<String, Object>> getListGroupByCustomerId(String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate, List<Integer> settleStatusList);
	
	/**
	 * @Title: getListGroupByCustomerId
	 * @Description: 获取客户账单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param startDate
	 * @param endDate
	 * @return 
	 * 		I.CUSTOMER_ID,	客户ID
			C.CUSTOMER_NAME, 	客户名称
			( SUM(I.DEBIT_AMOUNT) - SUM(I.CREDIT_AMOUNT) ) AS BALANCE,	客户余额 
	 */
	public List<Map<String, Object>> getCustomerBalanceList(String searchCond, String traceIds);
	
	/**
	 * @Title: getCcbWithholdBill
	 * @Description: 获取中国建设银行批量代扣账单
	 * @param customerId
	 * @param period	账单期间<=period
	 * 			定值条件：结算状态!=1；结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
	 * 			定值条件：扣费方式=2；扣费方式，1=其他/2=建行自动扣费/3=民生银行自动扣费
	 * 			定值条件：欠费账单；贷方金额!=借方金额，且贷方金额>借方金额
	 * @return 
	 * 		返回查询结果账目列表
	 */
	public List<CustomerAccountItem> getCcbWithholdBill(Long customerId, String period);
	
	/**
	 * @Title: getCmbcWithholdBill
	 * @Description: 获取中国民生银行批量代扣账单
	 * @param customerId
	 * @param period	账单期间<=period
	 * 			定值条件：结算状态!=1；结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
	 * 			定值条件：扣费方式=3；扣费方式，1=其他/2=建行自动扣费/3=民生银行自动扣费
	 * 			定值条件：欠费账单；贷方金额!=借方金额，且贷方金额>借方金额
	 * @return 
	 * 		返回查询结果账目列表
	 */
	public List<CustomerAccountItem> getCmbcWithholdBill(Long customerId, String period);
	
	
	/**
	 * @Title: searchCustomerArrearsAccountItem
	 * @Description: 查询客户除结算成功之外的账目
	 * @param period
	 * @param traceIds
	 * @param searchCond
	 * @param operatorId
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerArrearsAccountItem(String period, String traceIds, Integer settlementStatus, String searchCond, Long operatorId, String startDate, String endDate);
	
	/**
	 * @Title: clearBill
	 * @Description: 清欠账单
	 * @param period
	 * @param traceIds
	 * @param accountItemId
	 * @return 
	 */
	public int clearBill(String period, String traceIds, List<Long> accountItemId);
	
	public List<CustomerAccountItem> getCustomerAccountItemList(String period, Long customerId, Integer settlementStatus);
	
	/**
	 * @Title: searchAutoCustomerAccountItem
	 * @Description: 查询需要自动扣费的账单
	 * @param period
	 * @param traceIds
	 * @param settlementStatus
	 * @param searchCond
	 * @param operatorId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> searchAutoCustomerAccountItem(String period, String traceIds, Integer settlementStatus, String searchCond, Long operatorId, String startDate, String endDate);
	
	/**
	 * @Title: autoSettleBill
	 * @Description: 用户选择账单自动扣费
	 * @param billIdList
	 * @return 
	 */
	public int autoSettleBill(List<Long> billIdList);
	
	public List<Map<String, Object>> searchSettleAccountItem(String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, Long operatorId, String startDate, String endDate);
	
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
	public List<Map<String, Object>> getExportCustomerBillData(String searchCond, Integer settlementStatus, String traceIds, String period, Integer accountStatus,String startDate, String endDate);
	
	
	public List<Long> getCustomerIdList(String searchCond, String traceIds, Integer customerType);
	
	/**
	 * @Title: delete
	 * @Description: 删除账单，并更新分水量状态为未开账
	 * @param accountItemId
	 * @return 
	 */
	public int delete(Long accountItemId);
	
	/**
	 * @Title: getCustomerAccountItemStatistic
	 * @Description:获取账单的统计数据
	 * @param period
	 * @param traceIds
	 * @param settlementStatus
	 * @param accountStatus
	 * @param searchCond
	 * @param operatorId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public Map<String, Object> getCustomerAccountItemStatistic(String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, Long operatorId, String startDate, String endDate);
	
	
	/**
	 * @Title: getPastOweAmount
	 * @Description: 查询往期欠费金额（水费+违约金+分账单）
	 * @param accountItemId
	 * @param period
	 * @param customerId
	 * @return 
	 */
	public List<CustomerAccountItem> getPastOweAmount(String period, Long customerId);
	
	/**
	 * @param customerId
	 * @param period
	 * @return
	 * 查询表计所有账单
	 */
	public List<CustomerAccountItem> getAllCustomerAccountItemByMeter(Long meterId, String period);
	
	
	/**
	 * @Title: getPastOweAmountByMeter
	 * @Description:查询改水表的往期欠费账单
	 * @param period
	 * @param meterId
	 * @return 
	 */
	public List<CustomerAccountItem> getPastOweAmountByMeter(String period, Long meterId);
	
	/**
	 * @Title: getWaterFeeBillList
	 * @Description: 获取水费账单
	 * @param customerId
	 * @param period
	 * @param settlementStatus
	 * @return 
	 */
	public List<CustomerAccountItem> getWaterFeeBillList(Long customerId, String period, Integer settlementStatus);
	
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
	 * @Title: cancelRechargeBill
	 * @Description: 撤销充值
	 * @param rechargeBillId	充值记录ID
	 * @param operatorId
	 * @return 
	 */
	public int cancelRechargeBill(Long rechargeBillId, Long operatorId);
	
	/**
	 * @Title: deleteRecharge
	 * @Description:删除充值
	 * @param accountItemId
	 * @param arrearsAccountItemId
	 * @param operatorId
	 * @return 
	 */
	public int deleteRecharge(Long accountItemId, Long arrearsAccountItemId, Long operatorId);
	
	
	/**
	 * @Title: getAllRechargeBill
	 * @Description: 查询客户所有充值账单
	 * @param customerId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<CustomerAccountItem> getAllRechargeBill(Long customerId, String startDate, String endDate);
	
	/**
	 * @Title: getBalanceRechargeBill
	 * @Description: 查询客户有余额的充值账单
	 * @param customerId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<CustomerAccountItem> getBalanceRechargeBill(Long customerId, String startDate, String endDate);
	
	/**
	 * @Title: getTaxInvoiceBillList
	 * @Description: 获取税务开票软件账单集合（水费账单，且金额>0或预存账单，且金额>0）
	 * @param periodStart
	 * @param periodEnd
	 * @param customerId
	 * @param settlementStatus
	 * @param searchWaterFeeBill
	 * @param searchStoredBill
	 * @return 
	 */
	public List<CustomerAccountItem> getTaxInvoiceBillList(String periodStart, String periodEnd, Long customerId, Integer settlementStatus, Boolean searchWaterFeeBill, Boolean searchStoredBill);
	
	public List<CustomerAccountItem> getSubAccountItem(Long customerAccountItemId);
	
	/**
	 * @Title: getBaseFeeOweAmount
	 * @Description: 获取账单基础水费欠费金额
	 * @param assistant		辅助核算
	 * @param baseWaterFee	基础水费
	 * @return 
	 */
	public BigDecimal getBaseFeeOweAmount(String assistant, BigDecimal baseWaterFee);
	
	/**
	 * @Title: getSewageFeeOweAmount
	 * @Description: 获取账单污水处理费欠费金额
	 * @param assistant			辅助核算
	 * @param sewageWaterFee	污水水费
	 * @return 
	 */
	public BigDecimal getSewageFeeOweAmount(String assistant, BigDecimal sewageWaterFee);
	
	/**
	 * @Title: getRechargeBillList
	 * @Description: 获取充值账单
	 * @param period
	 * @param subjectPayment
	 * @return 
	 */
	public List<CustomerAccountItem> getRechargeBillList(String period, String subjectPayment);
	
	/**
	 * @Title: getPastRechargeBillList
	 * @Description: 获取往期充值账单
	 * @param period
	 * @param debitSubjectPayment
	 * @return 
	 * 		COUNT(1) AS COUNT,
			SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
			MAX( PW.BASE_PRICE ) AS BASE_PRICE,
			MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
			SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
			SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
	 */
	public List<CustomerAccountItem> getPastRechargeBillList(String period, String subjectPayment);
	
	/**
	 * @Title: getStatSettlementBillList
	 * @Description: 查询本期已结算（全部结算或部分结算）的水费账单
	 * @param period
	 * @param traceIdsList
	 * @return 
	 * 		COUNT(1) AS COUNT,
			SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
			MAX( PW.BASE_PRICE ) AS BASE_PRICE,
			MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
			SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
			SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
	 */
	public List<Map<String, Object>> getStatSettlementBillList(String period, String debitSubject, List<String> traceIdsList);
	
	/**
	 * @Title: getPastWaterFeeSettleBillList
	 * @Description: 查询往期已结算（全部结算或部分结算）的水费账单
	 * @param period
	 * @param traceIdsList
	 * @return 
	 */
	public List<Map<String, Object>> getStatPastSettlementBillList(String period, String debitSubject, List<String> traceIdsList);
	
	/**
	 * @Title: getActualWaterFeeAmount
	 * @Description: 按期间查询应收水费金额
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getActualWaterFeeAmount(Long customerId, String period);
	
	/**
	 * @Title: getReceiveWaterFeeAmount
	 * @Description: 按期间统计实收水费金额
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getReceiveWaterFeeAmount(Long customerId, String period);
	
	/**
	 * @Title: getStatOweCompanyMeter
	 * @Description: 统计欠费单位大表
	 * @param searchCond
	 * @param period
	 * @param settleStatusList
	 * @return 
	 * 		I.CUSTOMER_ID,	客户ID
			LISTAGG ( I.ID, ',' ) WITHIN GROUP ( ORDER BY I.ID ) AS BILL_IDS,	账单ID,多条记录用逗号","分隔
			I.PERIOD,	期间
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,	账单金额
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,	已充值金额
			( SUM(I.CREDIT_AMOUNT) - SUM(I.DEBIT_AMOUNT) ) AS OWE_AMOUNT	欠费金额
	 */
	public List<Map<String, Object>> getStatOweCompanyMeter(String searchCond, String period, List<Integer> settleStatusList);
	
	/**
	 * @Title: sendSmsNotice
	 * @Description: 发送短信通知
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public Map<String, Object> sendSmsNoticeBatch(List<Map<String, Object>> accountItemMapList);
	
	/**
	 * @Title: getCustomerAccountItemDetail
	 * @Description: 财务统计-获取欠费单位明细
	 * @param period
	 * @param customerId
	 * @param settleStatusList
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerAccountItemDetail(String period,Long customerId, List<Integer> settleStatusList);
	
	/**
	 * @Title: getStatNoTaxBillList
	 * @Description: 查询本期已结算（全部结算或部分结算）的水费账单（无票收入）
	 * @param period
	 * @param settleStatusList
	 * @param traceIdsList
	 * @return 
	 */
	public List<Map<String, Object>> getStatNoTaxBillList(String period, List<Integer> settleStatusList, List<String> traceIdsList);
	
	/**
	 * @Title: wechatSettlement
	 * @Description: 微信结算
	 * @param oweBillIdList
	 * @return 
	 * 		0=销账成功；1=销账失败；
	 */
	public int wechatSettlement(List<Long> oweBillIdList);
	
	/**
	 * @Title: wechatPrepayment
	 * @Description: 微信用户预付水费
	 * @param customerId
	 * @param amount
	 * @return 
	 * 		返回充值账目ID，null表示失败；非null表示成功
	 */
	public Long wechatPrepayment(Long customerId, BigDecimal amount);
	
	public int cleanRecharge(Long customerId, Long operatorId);
	
	public List<Map<String, Object>> getCustomerBalanceStatusList(String searchCond, String traceIds, Integer balanceStatus);
	/**
	 * @Title: getListGroupByCustomerId
	 * @Description: 查询微信客户的账单，按客户ID分组,返回列表
	 * 					与getListGroupByCustomerId查询SQL相同，在原SQL的基础上增加了EXISTS条件，用于判断是否绑定微信，是否绑定微信判断规则是微信-客户关系表中是否存在记录，有记录表示已绑定微信，无记录表示未绑定微信
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param startDate
	 * @param endDate
	 * @return 
	 * 		I.CUSTOMER_ID,
			C.CUSTOMER_NAME, 
			I.PERIOD,
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,
			( SUM(I.CREDIT_AMOUNT-I.DEBIT_AMOUNT) ) AS OWE_AMOUNT, 
			LM.TRACE_IDS 
	 */
	public List<Map<String, Object>> getWechatCustomerOweBillList(String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate, List<Integer> settleStatusList);
	
	/**
	 * @Title: getListGroupByCustomerId
	 * @Description: 查询非微信客户的账单，按客户ID分组,返回列表
	 * 			与getListGroupByCustomerId查询SQL相同，在原SQL的基础上增加了NOT EXISTS条件，用于判断是否绑定微信，是否绑定微信判断规则是微信-客户关系表中是否存在记录，有记录表示已绑定微信，无记录表示未绑定微信
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param deductMode
	 * @param startDate
	 * @param endDate
	 * @return 
	 * 		I.CUSTOMER_ID,
			C.CUSTOMER_NAME, 
			I.PERIOD,
			SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,
			SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,
			( SUM(I.CREDIT_AMOUNT-I.DEBIT_AMOUNT) ) AS OWE_AMOUNT, 
			LM.TRACE_IDS 
	 */
	public List<Map<String, Object>> getNotWechatCustomerOweBillList(String searchCond, String traceIds, String period, Integer deductMode, String startDate, String endDate, List<Integer> settleStatusList);
	
	/**
	 * @Title: getDebitBillList
	 * @Description: 根据充值记录ID查询清欠充值记录
	 * @param rechargeBillId
	 * @return 
	 */
	public List<CustomerAccountItem> getDebitBillList(Long rechargeBillId);
	
	/**
	 * @Title: updateDebitBillCreditAssistantIsNull
	 * @Description: 更新清欠充值记录中贷方辅助核算为空
	 * @param rechargeBillId
	 */
	public void updateDebitBillCreditAssistantIsNull(Long rechargeBillId);
	
	/**
	 * @Title: deleteDebitBill
	 * @Description: 删除清欠的充值记录（逻辑删除）
	 * @param rechargeBillId 
	 */
	public void deleteDebitBill(Long rechargeBillId);
	
	//--------------------------------智慧水务平台分水量业务处理部分------------------------------------------------------------------------------------------------------------------------
	/**
	 * @Title: autoGeneratorWaterFeeBill
	 * @Description: 自动生成水费账单
	 * @param pw
	 * @return 
	 */
	public Long autoGeneratorWaterFeeBill(PartitionWater pw);
	
	/**
	 * @Title: balanceAutoSettlement
	 * @Description: 余额自动结算水费账单
	 * @param waterFeeBill
	 * @return 
	 * 		rows>0时余额自动结算成功
	 * 		rows=0时余额自动结算失败
	 * 		rows<0时余额不足
	 */
	public int balanceAutoSettlement(CustomerAccountItem waterFeeBill);
	
}