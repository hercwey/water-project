package com.learnbind.ai.service.customers.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.AssistantBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.CMBCAutoDeductReceiptBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerAccountItemLogType;
import com.learnbind.ai.common.enumclass.EnumCustomerChargeMode;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumMakeBillStatus;
import com.learnbind.ai.common.enumclass.EnumOverdueFineStatus;
import com.learnbind.ai.common.enumclass.EnumSendSmsStatus;
import com.learnbind.ai.common.enumclass.EnumSubAccountStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiTraceOperate;
import com.learnbind.ai.common.enumclass.accountitem.EnumPaymentType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.constant.AccountItemConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.dao.CustomerAccountItemMapper;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerAccountItemLog;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SendSmsLog;
import com.learnbind.ai.model.SysOverdueFine;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.business.BillBusiness;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemLogService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.overduefine.OverdueFineService;
import com.learnbind.ai.service.sendlog.SendSmsLogService;
import com.learnbind.ai.service.trace.AccountItemTraceService;
import com.learnbind.ai.service.trace.UseWaterPriceTraceService;
import com.learnbind.ai.sms.SMSConstants;
import com.learnbind.ai.sms.SMSService;
import com.learnbind.ai.sms.SendSingleSMResponse;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomerAccountItemServiceImpl.java
 * @Description: 客户账目service服务实现类
 *
 * @author Thinkpad
 * @date 2019年5月21日 下午5:10:44
 * @version V1.0
 *
 */
@Service
public class CustomerAccountItemServiceImpl extends AbstractBaseService<CustomerAccountItem, Long>
		implements CustomerAccountItemService {

	private static Log log = LogFactory.getLog(CustomerAccountItemServiceImpl.class);
	
	@Autowired
	public CustomerAccountItemMapper customerAccountItemMapper;
	
	@Autowired
	public CustomerOverdueFineService customerOverdueFineService;
	@Autowired
	public DiscountFineTraceService discountFineTraceService;
	@Autowired
	private BankService bankService;//客户-银行
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计
	@Autowired
	private CustomerAccountService customerAccountService;//客户-账户
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量
	@Autowired
	private OverdueFineService overdueFineService;//违约金配置
	@Autowired
	private AccountItemTraceService accountItemTraceService;//账目日志
	@Autowired
	private UseWaterPriceTraceService useWaterPriceTraceService;//使用水价日志
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录
	@Autowired
	public CustomerAccountItemLogService customerAccountItemLogService;//账单日志
	@Autowired
	private SMSService smsService;// 发送短信
	@Autowired
	private SendSmsLogService sendSmsLogService;// 短信通知
	@Autowired
	private BillBusiness billBusiness;//账单业务处理部分
	
	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public CustomerAccountItemServiceImpl(CustomerAccountItemMapper mapper) {
		this.customerAccountItemMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<Map<String, Object>> searchCustomerAccountItem(String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, Long operatorId, String startDate, String endDate) {
		
		List<String> creditSubjectList = new ArrayList<>();
		
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.searchCustomerAccountItem(period, traceIds, settlementStatus, accountStatus,  searchCond, operatorId, creditSubjectList, startDate, endDate);
	}
	
	
	/**
	 * (非 Javadoc)
	 * 
	 * @Title: insertCustomerAccountItem
	 * @Description: 添加客户账单
	 * @param accountItem
	 * @return
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#insertCustomerAccountItem(com.learnbind.ai.model.CustomerAccountItem)
	 */
	@Override
	@Transactional
	public int insertCustomerAccountItem(Long customerId, String period, Integer debitCredit, String digest, Long waterPriceId, BigDecimal amount, Long operatorId) {
		int rows=1;
		BigDecimal zero = new BigDecimal("0");//默认值：0
		
		//查询客户-账户ID
		Long accountId = customerAccountService.getAccountIdByCustomerName(customerId);
		
		//添加账单
		CustomerAccountItem account = new CustomerAccountItem();
		account.setCustomerId(customerId);
		account.setAccountId(accountId);
		account.setPeriod(period);
		account.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		account.setAccountDate(new Date());
		account.setAccounter(String.valueOf(operatorId));
		//结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
		account.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_NORMAL.getValue());
		//获取分账状态
		account.setAccountStatus(EnumSubAccountStatus.NO_NEED_SUB_ACCOUNT.getValue());//设置分账状态
		
		if(debitCredit == 1) {//选择充值账单时
			account.setDebitCredit(debitCredit.toString());
			account.setDebitDigest(digest);
			String subject = EnumAiDebitSubjectAction.ADVANCE_WATER_FEE.getKey();
			subject = debitCredit + subject;
			account.setDebitSubject(subject);//预存水费
			account.setDebitAmount(amount);
			account.setDebitDigest(EnumAiDebitSubjectAction.ADVANCE_WATER_FEE.getValue());
			account.setCreditAmount(zero);
		} else {//选择欠费账单时
			account.setDebitCredit(debitCredit.toString());
			account.setCreditDigest(digest);
			String subject = EnumAiCreditSubjectAction.WATER_FEE.getKey();
			subject = debitCredit + subject;
			account.setCreditSubject(subject);//水费账单
			account.setCreditAmount(amount);
			account.setCreditDigest(EnumAiCreditSubjectAction.WATER_FEE.getValue());
			account.setDebitAmount(zero);
		}
		rows = this.insertSelective(account);
		if(rows >0) {
			//添加使用使用水价日志
			rows = useWaterPriceTraceService.inserTrace(waterPriceId, account.getId());
		} else {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		
		return rows;
	}

	/**
	 * (非 Javadoc)
	 * 
	 * @Title: updateCustomerAccountItem
	 * @Description: 修改客户账单
	 * @param accountItem
	 * @return
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#updateCustomerAccountItem(com.learnbind.ai.model.CustomerAccountItem)
	 */
	@Override
	public int updateCustomerAccountItem(CustomerAccountItem accountItem) {
		return customerAccountItemMapper.updateByPrimaryKeySelective(accountItem);
	}

	/**
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCustomerAccountItem
	 * @Description: 删除，物理删除
	 * @param accountItemId
	 * @return
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#deleteCustomerAccountItem(long)
	 */
	@Override
	public int deleteCustomerAccountItem(long accountItemId) {
		try {

			int rows = customerAccountItemMapper.deleteByPrimaryKey(accountItemId);
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<CustomerAccountItem> findAllCustomerAccountItem(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerAccountItem> accountItemList = customerAccountItemMapper.selectAll();
		PageInfo result = new PageInfo(accountItemList);
		return result;
	}

	/**
	 * @Title: generateOverdueBill
	 * @Description: 生成违约金账单
	 * @param pw
	 * @param waterPrice
	 * @param customer
	 * @param accountId
	 * @param operatorId
	 * @return
	 */
	@Transactional
	public int generateOverdueBill(Long accountItemId, Long operatorId) {
		
		BigDecimal rate = this.getOverdueFineRate();//获取违约金计算费率
		log.info("--------------------	计算违约金费率："+rate);
		
		Date sysDate = new Date(); 
		
		//BigDecimal zero = new BigDecimal("0");// 初始化BigDecimal类型的0
		//水费账单
		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		//计算账单欠费金额
		BigDecimal oweAmount = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
		//计算本次违约金金额
		BigDecimal overdueAmount = BigDecimalUtils.multiply(oweAmount, rate);
		
		//生成违约金账单，返回生成的违约金账单的ID
		Long overdueBillId = this.generatorOverdueBill(item.getId(), item.getCustomerId(), overdueAmount);
		
		if (overdueBillId!=null) {
			//添加客户-违约金数据
			CustomerOverdueFine cod = new CustomerOverdueFine();
			cod.setCustomerId(item.getCustomerId());
			cod.setPeriod(item.getPeriod());
			cod.setBaseAmount(oweAmount);//账单基础金额
			cod.setStartTime(sysDate);
			cod.setEndTime(sysDate);
			cod.setOverdueFine(overdueAmount);//违约金金额
			cod.setRemark(item.getRemark());
			cod.setOverdueStatus(EnumOverdueFineStatus.OVERDUE_NO.getValue());//违约金结算状态（未结算）
			cod.setOverdueAccountId(overdueBillId);
			return customerOverdueFineService.insertSelective(cod);
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: generatorOverdueBill
	 * @Description: 生成违约金账单
	 * @param customerId
	 * @param creditAmount
	 * @return 
	 */
	private Long generatorOverdueBill(Long accountItemId, Long customerId, BigDecimal creditAmount) {
		
		//贷方科目
		String creditSubject = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		//贷方摘要
		//String creditDigest = EnumAiDebitCreditStatus.CREDIT.getValue()+EnumAiCreditSubjectAction.OVERDUE_FINE.getValue();
		String creditDigest = EnumAiCreditSubjectAction.OVERDUE_FINE.getValue();
		
		//系统登录用户
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Date sysDate = new Date();//系统时间
		BigDecimal zero = new BigDecimal("0.00");//初始化BigDecimal类型的0
		
		//Customers customer = this.getCustomer(customerId);//客户信息
		CustomerAccount ca = this.getCustomerAccount(customerId);//客户-账户信息
		
		CustomerAccountItem item = new CustomerAccountItem();//账目
		item.setCustomerId(customerId);//客户ID
		item.setAccountId(ca.getId());//账户ID
		item.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//		item.setDebitDigest();//借方摘要
//		item.setDebitSubject();//借方科目
//		item.setDebitAssistant(debitAssistant);//借方辅助核算
		item.setDebitAmount(zero);//借方金额
//		
		item.setCreditSubject(creditSubject);//贷方科目
		item.setCreditDigest(creditDigest);//贷方摘要
		//item.setCreditAssistant(AccountItemConstant.CREDIT_ASSISTANT_SYS_AUTO);//贷方辅助核算
		item.setCreditAmount(creditAmount);//贷方金额
//		
		item.setDebitCredit(EnumAiDebitCreditStatus.CREDIT.getKey());//借/贷;
//		item.setOverdueDate(overdueDate);//违约金计算日期
		
		item.setAccounter(userBean.getId().toString());//记账人
		item.setAccountDate(new Date());//记账日期
		
		item.setStartTime(sysDate);//违约金账单时间
		item.setEndTime(sysDate);//违约金账单时间
		
		//item.setPeriod();//期间
		
		item.setRemark(creditDigest);//备注
		
		item.setPid(accountItemId);//水费账单ID
		
		int rows = customerAccountItemMapper.insertSelective(item);
		if(rows>0) {
			return item.getId();
		}
		return null;
	}
	
	/**
	 * @Title: getOverdueFineRate
	 * @Description: 获取违约金费率，如果未配置，则返回默认值千分之五
	 * @return 
	 */
	private BigDecimal getOverdueFineRate() {
		
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型0
		
		BigDecimal rate = null;//计算违约金费率
		List<SysOverdueFine> ofList = overdueFineService.selectAll();
		if(ofList!=null && ofList.size()>0) {
			SysOverdueFine overdueFine = ofList.get(0);
			rate = overdueFine.getRate();//因为配置的费率是百分比，所以要获取费率需要再除以100
		}
		if(rate!=null && BigDecimalUtils.greaterThan(rate, zero)) {
			return BigDecimalUtils.divide(rate, new BigDecimal("100.00"));
		}
		return new BigDecimal("0.005");
	}

	/** 
	 * @Title: getOverdueValueSum
	 * @Description: 查询某账单违约金总金额之和
	 * @param accountItemId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getOverdueValueSum(java.lang.Long)
	 */
	@Override
	public BigDecimal getOverdueValueSum(Long accountItemId) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		
		return customerAccountItemMapper.getSubjectCreditAmountSum(accountItemId, creditSubjectList);
	}

	/** 
	 * @Title: getOverdueBillList
	 * @Description: 查询某账单的违约金账单集合，把账单ID做为PID即可查询
	 * @param accountItemId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getOverdueBillList(java.lang.Long)
	 */
	@Override
	public List<CustomerAccountItem> getOverdueBillList(Long accountItemId) {
		
		//贷方科目
		String creditSubject = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		
		CustomerAccountItem record = new CustomerAccountItem();
		record.setPid(accountItemId);
		record.setCreditSubject(creditSubject);
		return customerAccountItemMapper.select(record);
	}

	/** 
	 * @Title: getRechargeOverdueValueSum
	 * @Description: 查询某账单已充值违约金之和
	 * @param accountItemId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getRechargeOverdueValueSum(java.lang.Long)
	 */
	@Override
	public BigDecimal getRechargeOverdueValueSum(Long accountItemId) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		
		return customerAccountItemMapper.getSubjectDebitAmountSum(accountItemId, creditSubjectList);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getOverdueBillOweAmountSum
	 * @Description: 查询某账单违约金欠费金额之和
	 * @param accountItemId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getOverdueBillOweAmountSum(java.lang.Long)
	 */
	@Override
	public BigDecimal getOverdueBillOweAmountSum(Long accountItemId) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		
		return customerAccountItemMapper.getSubjectOweAmountSum(accountItemId, creditSubjectList);
	}

	/**
	 * (非 Javadoc)
	 * 
	 * @Title: settleAccount
	 * @Description: 结算生成新的账单
	 * @param accountItemId
	 * @param operatorId
	 * @return
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#settleAccount(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	@Transactional
	public int settleAccount(Long accountItemId, Long operatorId, BigDecimal settleAmount, String debitSubject, String debitDigest) {
		int rows = 1;
		Date sysDate = new Date();
		BigDecimal zero = new BigDecimal("0");
		// 原有账单
		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		// 1、生成充值账单

		List<Map<String, Object>> itemList = new ArrayList<>();// 创建一个集合存储账单用于贷方辅助核算
		List<CustomerAccountItem> currBillList = this.getCurrBill(item.getCustomerId(), item.getPeriod(),
				EnumAiDebitCreditStatus.CREDIT.getKey(), null);
		
		
		// 新的账单对象-水费充值账单
		CustomerAccountItem customerAccountItem = new CustomerAccountItem();
		
		// 计算水费充值账单总金额
		BigDecimal debitAmountSum = customerAccountItemMapper.getDebitAmountSum(item.getCustomerId(), item.getPeriod(),
				EnumAiDebitCreditStatus.DEBIT.getKey(), AccountItemConstant.DEBIT_SUBJECT_RECHARGE);
		// 查询某账单违约金欠费总金额
		BigDecimal currBillOwedAmount = this.getCurrBillOwedAmount(item.getId());
		/**辅助核算格式
		 * id  --->相关联的账目ID
		  *    amount --->相关金额
		  *    desc --->描述
		  *    date --->日期
		  *    operator--->操作员ID
		 */
		//创建一个集合存储新账单贷方辅助核算
		Map<String, Object> map = new HashMap<>();
		map.put("accountItemId", item.getId());//原账单ID
		map.put("operatorTime", new Date());//账单充值时间
		map.put("operator", operatorId.toString());//账单充值操作员
		map.put("remark", AccountItemConstant.WATER_FEE_RECHARGE);//账单充值操作员

		// 计算缴费金额和账单金额关系
		BigDecimal settleDebitAmount = BigDecimalUtils.add(debitAmountSum, settleAmount);
		if (BigDecimalUtils.lessOrEquals(settleDebitAmount, currBillOwedAmount)) {// 充值金额小于等于欠费金额
			customerAccountItem.setCreditAmount(settleAmount);// 贷方金额
			customerAccountItem.setDebitAmount(settleAmount);// 借方金额
			map.put("debitAmount", settleAmount);//充值金额
		} else {
			if(BigDecimalUtils.lessOrEquals(currBillOwedAmount, zero)) {
				currBillOwedAmount = zero;
			}
			map.put("debitAmount", item.getCreditAmount());//充值金额
			customerAccountItem.setCreditAmount(currBillOwedAmount);// 贷方金额
			customerAccountItem.setDebitAmount(settleAmount);// 借方金额
		}
		itemList.add(map);
		String creditAssistant = JSON.toJSONString(itemList);//贷方辅助核算
		
		customerAccountItem.setCustomerId(item.getCustomerId());// 客户id
		customerAccountItem.setPeriod(item.getPeriod());// 期间
		customerAccountItem.setAccountId(item.getAccountId());// 账户id
		customerAccountItem.setCreditAssistant(creditAssistant);//贷方辅助核算 原账单字符串
		customerAccountItem.setDebitCredit(AccountItemConstant.DEBIT);// 借/贷
		customerAccountItem.setDebitDigest(debitDigest);// 借方摘要-水费充值
		customerAccountItem.setDebitSubject(debitSubject);// 借方科目-充值
		customerAccountItem.setAccounter(operatorId.toString());// 记账人
		customerAccountItem.setAccountDate(sysDate);// 记账日期
		customerAccountItem.setStartTime(sysDate);// 账单产生时间段的开始时间
		customerAccountItem.setEndTime(sysDate);// 账单产生时间段的结束时间
		customerAccountItem.setRemark(debitDigest);// 备注

		// 生成新的缴费账单
		rows = customerAccountItemMapper.insertSelective(customerAccountItem);
		if (rows <= 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		rows = this.updateOriginBill(accountItemId, settleAmount, operatorId);//更新原账单信息
		return rows;

	}

	/**
	 * @Title: getCurrBill
	 * @Description: 查询本期的水费账单和违约金账单
	 * @param customerId
	 * @param period
	 * @return
	 */
	private List<CustomerAccountItem> getCurrBill(Long customerId, String period, String debitCredit, String creditDigest) {

		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("period", period)
				.andEqualTo("debitCredit", debitCredit).andEqualTo("creditDigest", creditDigest);
		example.setOrderByClause(" ID ASC");
		return customerAccountItemMapper.selectByExample(example);
	}
	
	/**
	 * @Title: getCurrBill
	 * @Description: 查询本期的水费充值账单和违约金充值账单
	 * @param customerId
	 * @param period
	 * @return
	 */
	private List<CustomerAccountItem> getRechargeCurrBill(Long customerId, String period, String debitCredit, String debitDigest) {

		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("period", period)
				.andEqualTo("debitCredit", debitCredit).andEqualTo("debitDigest", debitDigest);
		example.setOrderByClause(" ID ASC");
		return customerAccountItemMapper.selectByExample(example);
	}

	/** 
	 * @Title: getCurrBillOwedAmount
	 * @Description: 查询某客户水费账单和违约金账单的欠费金额之和
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getCurrBillOwedAmount(java.lang.Long)
	 */
	public BigDecimal getCurrBillOwedAmount(Long customerId) {
		
		//贷方科目水费编码
		String creditSubjectWaterFee = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		//贷方科目违约金编码
		String creditSubjectOverdue = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		
		List<String> creditSubjectList = new ArrayList<>();
		creditSubjectList.add(creditSubjectWaterFee);
		creditSubjectList.add(creditSubjectOverdue);
		BigDecimal owedAmount = customerAccountItemMapper.getSubjectListOwedAmountSum(customerId, creditSubjectList);
		return owedAmount.setScale(2);
	}
	
	@Override
	public List<CustomerAccountItem> getBillArrearsList(String searchCond, Long operatorId) {
		return customerAccountItemMapper.getBillArrearsList(searchCond, operatorId);
	}

	/**
	 * @Title: updateOriginBill
	 * @Description: 更新原账单信息
	 * @param accountItemId
	 * @return
	 */
	@Transactional
	public int updateOriginBill(Long accountItemId, BigDecimal settleAmount, Long operatorId) {
		int rows = 1;
		//UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BigDecimal zero = new BigDecimal("0");

		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(accountItemId);

		// 计算水费充值账单总金额
		//BigDecimal debitAmountSum = customerAccountItemMapper.getDebitAmountSum(item.getCustomerId(), item.getPeriod(), AccountItemConstant.DEBIT, AccountItemConstant.DEBIT_SUBJECT_RECHARGE);
		// 查询账单欠费总金额
		//BigDecimal currBillOwedAmount = this.getCurrBillOwedAmount(item.getCustomerId(), item.getPeriod(), AccountItemConstant.CREDIT);
		// 查询本期的水费账单和违约金账单
		List<CustomerAccountItem> currBillList = this.getCurrBill(item.getCustomerId(), item.getPeriod(),
				EnumAiDebitCreditStatus.CREDIT.getKey(), null);
		List<Map<String, Object>> itemList = new ArrayList<>();// 创建一个集合存储账单用于贷方辅助核算
		
		// 查询本期的充值账单
		List<CustomerAccountItem> rechargeBillList = this.getRechargeCurrBill(item.getCustomerId(), item.getPeriod(),
				EnumAiDebitCreditStatus.DEBIT.getKey(), AccountItemConstant.WATER_FEE_RECHARGE);
		//存储原账单的解放辅助核算
		for (CustomerAccountItem rechargeTemp : rechargeBillList) {
			Map<String, Object> map = new HashMap<>();
			map.put("accountItemId", rechargeTemp.getId());//原账单ID
			map.put("operatorTime", new Date());//账单充值时间
			map.put("operator", operatorId.toString());//账单充值操作员
			map.put("remark", AccountItemConstant.WATER_FEE_RECHARGE);//账单充值操作员
			map.put("debitAmount", rechargeTemp.getDebitAmount());//账单充值金额
			itemList.add(map);
		}
		String debitAssistant = JSON.toJSONString(itemList);
		for (CustomerAccountItem temp : currBillList) {
			
			temp.setDebitAssistant(debitAssistant);

			BigDecimal creditAmount = temp.getCreditAmount();// 贷方金额
			BigDecimal debitAmount = temp.getDebitAmount();// 借方金额
			BigDecimal amount = BigDecimalUtils.subtract(creditAmount, debitAmount);// 欠费金额
			if (BigDecimalUtils.greaterOrEquals(settleAmount, amount)) {// 充值金额大于等于欠费金额
				
				//更新扣费结算状态为交易成功
				this.updateSettlementStatus(accountItemId, EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue(), EnumSettlementStatus.SETTLEMENT_SUCCESS.getName());
				
				temp.setDebitAmount(creditAmount);
				if (BigDecimalUtils.equals(amount, zero)) {
					settleAmount = settleAmount;
				} else {
					settleAmount = BigDecimalUtils.subtract(settleAmount, amount);
				}

				//违约金账单编码
				String subjectOverdue = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
				
				if (subjectOverdue.equals(temp.getCreditSubject())) {// 如果是违约金账单
					//修改客户-违约金数据
					Example example = new Example(CustomerOverdueFine.class);
					example.createCriteria().andEqualTo("customerId", temp.getCustomerId()).andEqualTo("period",
							temp.getPeriod()).andEqualTo("overdueAccountId", temp.getId());
					List<CustomerOverdueFine> codList = customerOverdueFineService.selectByExample(example);
					for (CustomerOverdueFine cod : codList) {
						cod.setOverdueStatus(EnumOverdueFineStatus.OVERDUE_YES.getValue());
						customerOverdueFineService.updateByPrimaryKeySelective(cod);
					}
				}

			} else {
				
				//违约金账单编码
				String subjectOverdue = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
				if (subjectOverdue.equals(temp.getCreditSubject())) {// 如果是违约金账单
					 //修改客户-违约金数据
					 Example example = new Example(CustomerOverdueFine.class);
					example.createCriteria().andEqualTo("customerId", temp.getCustomerId()).andEqualTo("period",
							temp.getPeriod()).andEqualTo("overdueAccountId", temp.getId());
					List<CustomerOverdueFine> codList = customerOverdueFineService.selectByExample(example);
					for (CustomerOverdueFine cod : codList) {
						cod.setOverdueFine(BigDecimalUtils.subtract(cod.getOverdueFine(), settleAmount));
						customerOverdueFineService.updateByPrimaryKeySelective(cod);
					}

				}

				temp.setDebitAmount(BigDecimalUtils.add(settleAmount, debitAmount));
				settleAmount = new BigDecimal("0");
			}
			rows = customerAccountItemMapper.updateByPrimaryKeySelective(temp);

			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}

		return rows;

	}
	
	
	/**
	 * @Title: settleOverdueAccount
	 * @Description: 违约金减免生成充值违约金账单
	 * @param accountItemId
	 * @param creditDigest
	 * @param operatorId
	 * @param settleAmount
	 * @return 
	 */
	@Override
	@Transactional
	public int settleOverdueAccount(Long accountItemId, String creditDigest, Long operatorId, BigDecimal settleAmount) {
		
		//贷方科目
		String creditSubjectWaterFee = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		//贷方科目
		String creditSubjectOverdue = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		
		int rows = 1;
		Date sysdate = new Date(); 
		BigDecimal zero = new BigDecimal("0");
		// 原有账单
		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		// 1、生成充值账单

		List<Map<String, Object>> itemList = new ArrayList<>();// 创建一个集合存储账单用于贷方辅助核算
		//创建一个集合存储新账单贷方辅助核算
		Map<String, Object> map = new HashMap<>();
		map.put("accountItemId", item.getId());//原账单ID
		map.put("operatorTime", sysdate);//账单充值时间
		map.put("operator", operatorId.toString());//账单充值操作员
		map.put("remark", AccountItemConstant.OVERDUE_FEE_RECHARGE);//账单充值操作员
		
		
		this.updateOriginOverdueBill(item.getId(), settleAmount, creditDigest, operatorId);

		// 新的账单对象-水费充值账单
		CustomerAccountItem customerAccountItem = new CustomerAccountItem();

		// 计算水费充值账单总金额
		BigDecimal debitAmountSum = customerAccountItemMapper.getDebitAmountSum(item.getCustomerId(), item.getPeriod(),
				EnumAiDebitCreditStatus.DEBIT.getKey(), AccountItemConstant.DEBIT_SUBJECT_RECHARGE);
		// 查询某账单违约金欠费总金额
		BigDecimal currBillOwedAmount = this.getCurrBillOwedAmount(item.getId());

		// 计算缴费金额和账单金额关系
		BigDecimal settleDebitAmount = BigDecimalUtils.add(debitAmountSum, settleAmount);
		if (BigDecimalUtils.lessOrEquals(settleDebitAmount, currBillOwedAmount)) {// 充值金额小于等于欠费金额
			map.put("debitAmount", settleAmount);//充值金额
			customerAccountItem.setCreditAmount(settleAmount);// 贷方金额
			customerAccountItem.setDebitAmount(settleAmount);// 借方金额
		} else {
			if(BigDecimalUtils.lessOrEquals(currBillOwedAmount, zero)) {
				currBillOwedAmount = zero;
			}
			map.put("debitAmount", currBillOwedAmount);//充值金额
			customerAccountItem.setCreditAmount(currBillOwedAmount);// 贷方金额
			customerAccountItem.setDebitAmount(settleAmount);// 借方金额
		}
		itemList.add(map);
		String creditSubject = JSON.toJSONString(itemList);
		
		customerAccountItem.setCustomerId(item.getCustomerId());// 客户id
		customerAccountItem.setPeriod(item.getPeriod());// 期间
		customerAccountItem.setAccountId(item.getAccountId());// 账户id
		customerAccountItem.setCreditAssistant(creditSubject);// 原账单字符串
		customerAccountItem.setDebitCredit(EnumAiDebitCreditStatus.DEBIT.getKey());// 借/贷
		if(creditDigest.equals(creditSubjectWaterFee)) {
			customerAccountItem.setDebitDigest(AccountItemConstant.WATER_FEE_RECHARGE);// 借方摘要-水费充值
		} else {
			customerAccountItem.setDebitDigest(AccountItemConstant.OVERDUE_FEE_RECHARGE);// 借方摘要-违约金充值
		}
		customerAccountItem.setDebitSubject(AccountItemConstant.DEBIT_SUBJECT_RECHARGE);// 借方科目-充值
		customerAccountItem.setAccounter(operatorId.toString());// 记账人
		customerAccountItem.setAccountDate(sysdate);// 记账日期
		customerAccountItem.setStartTime(sysdate);// 账单产生时间段的开始时间
		customerAccountItem.setEndTime(sysdate);// 账单产生时间段的结束时间
		customerAccountItem.setRemark(AccountItemConstant.OVERDUE_FEE_RECHARGE);// 备注

		// 生成新的缴费账单
		rows = customerAccountItemMapper.insertSelective(customerAccountItem);
		if (rows <= 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return rows;

	}
	
	
	/**
	 * @Title: updateOriginBill
	 * @Description: 更新违约金原账单信息
	 * @param accountItemId
	 * @return
	 */
	public int updateOriginOverdueBill(Long accountItemId, BigDecimal settleAmount, String creditDigest, Long operatorId) {
		int rows = 1;
		//UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BigDecimal zero = new BigDecimal("0");

		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(accountItemId);

//		// 计算水费充值账单总金额
//		BigDecimal debitAmountSum = customerAccountItemMapper.getDebitAmountSum(item.getCustomerId(), item.getPeriod(),
//				AccountItemConstant.DEBIT, AccountItemConstant.DEBIT_SUBJECT_RECHARGE);
//		// 查询账单欠费总金额
//		BigDecimal currBillOwedAmount = this.getCurrBillOwedAmount(item.getCustomerId(), item.getPeriod(),
//				AccountItemConstant.CREDIT);
		
		
		// 查询本期的充值账单
		List<CustomerAccountItem> rechargeBillList = this.getRechargeCurrBill(item.getCustomerId(), item.getPeriod(),
				EnumAiDebitCreditStatus.DEBIT.getKey(), AccountItemConstant.OVERDUE_FEE_RECHARGE);
		
		List<Map<String, Object>> itemList = new ArrayList<>();// 创建一个集合存储账单用于贷方辅助核算
		//存储原账单的解放辅助核算
		for (CustomerAccountItem rechargeTemp : rechargeBillList) {
			Map<String, Object> map = new HashMap<>();
			map.put("accountItemId", item.getId());//原账单ID
			map.put("operatorTime", new Date());//账单充值时间
			map.put("operator", operatorId.toString());//账单充值操作员
			map.put("remark", AccountItemConstant.OVERDUE_FEE_RECHARGE);//账单充值操作员
			map.put("debitAmount", rechargeTemp.getDebitAmount());//账单充值金额
			itemList.add(map);
		}
		
		String debitSubject = JSON.toJSONString(itemList);
		item.setDebitAssistant(debitSubject);

		BigDecimal creditAmount = item.getCreditAmount();// 贷方金额
		BigDecimal debitAmount = item.getDebitAmount();// 借方金额
		BigDecimal amount = BigDecimalUtils.subtract(creditAmount, debitAmount);// 欠费金额
		if (BigDecimalUtils.greaterOrEquals(settleAmount, amount)) {// 充值金额大于等于欠费金额
			item.setDebitAmount(creditAmount);
			if (BigDecimalUtils.equals(amount, zero)) {
				settleAmount = settleAmount;
			} else {
				settleAmount = BigDecimalUtils.subtract(settleAmount, amount);
			}
		} else {
			item.setDebitAmount(BigDecimalUtils.add(settleAmount, debitAmount));
			settleAmount = new BigDecimal("0");
		}
		rows = customerAccountItemMapper.updateByPrimaryKeySelective(item);
		if (rows <= 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	//------------------------------	中国民生银行自动扣费后销账	------------------------------
	@Override
	@Transactional
	public int cmbcSettlementChargeOff(List<CMBCAutoDeductReceiptBean> receiptList, String period) {
		
		int rows = 0;
		for(CMBCAutoDeductReceiptBean receipt : receiptList) {
			String cardNo = receipt.getCardNo();//卡号
			String accountName = receipt.getAccountName();//账户名
			BigDecimal deductAmount = receipt.getDeductAmount();//扣费金额
			Integer deductStatus = receipt.getStatus();//扣费状态
			String errMsg = receipt.getErrMsg();//扣费失败原因
			
			//根据卡号与账户名获取客户信息
			Customers customer = this.getCustomer(cardNo, accountName);
			
			List<CustomerAccountItem> aiList = this.getCurrWaterFeeBill(customer.getId(), period);//查询本期的水费账单
			for(CustomerAccountItem ai : aiList) {
				Long accountItemId = ai.getId();//账单ID
				if(deductStatus!=null && deductStatus==EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue()) {//如果扣费状态不为空时，且状态值=1时，则扣费成功，否则扣费失败；扣费成功时结算，扣费失败时只修改状态
					rows = this.settleAccount(accountItemId, 0l, deductAmount, AccountItemConstant.DEBIT_SUBJECT_CMBC_AUTO_DEDUCT, AccountItemConstant.DEBIT_DIGEST_CMBC_AUTO_DEDUCT);//结算
					if(rows>0) {
						//更新自动扣费结算状态
						this.updateSettlementStatus(accountItemId, deductStatus, errMsg);
					}
				}else {
					//更新自动扣费结算状态
					rows = this.updateSettlementStatus(accountItemId, deductStatus, errMsg);
				}
				
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
			if(rows<=0) {
				break;
			}
		}
		return rows;
	}
	
	/**
	 * @Title: getCurrWaterFeeBill
	 * @Description: 查询本期的水费账单
	 * @param customerId
	 * @param period
	 * @return
	 */
	private List<CustomerAccountItem> getCurrWaterFeeBill(Long customerId, String period) {

		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("period", period)
				.andEqualTo("debitCredit", EnumAiDebitCreditStatus.CREDIT.getKey()).andEqualTo("creditSubject", AccountItemConstant.CREDIT_SUBJECT_WATER_FEE);
		example.setOrderByClause(" ID ASC");
		return customerAccountItemMapper.selectByExample(example);
	}
	
	/**
	 * @Title: updateSettlementStatus
	 * @Description: 更新自动扣费结算状态
	 * @param accountItemId
	 * @param settlementStatus
	 * @param settlementErrMsg
	 * @return 
	 */
	private int updateSettlementStatus(Long accountItemId, int settlementStatus, String settlementErrMsg) {
		CustomerAccountItem ai = new CustomerAccountItem();
		ai.setId(accountItemId);
		ai.setSettlementStatus(settlementStatus);
		ai.setSettlementErrMsg(settlementErrMsg);
		return customerAccountItemMapper.updateByPrimaryKeySelective(ai);
	}
	
	/**
	 * @Title: getCustomer
	 * @Description: 根据卡号与账户名获取客户信息
	 * @param cardNo
	 * @param accountName
	 * @return 
	 */
	private Customers getCustomer(String cardNo, String accountName) {
		
		Example example = new Example(CustomerBanks.class);
		example.createCriteria().andEqualTo("cardNo", cardNo).andEqualTo("accountName", accountName).andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue());
		example.setOrderByClause(" ID DESC");
		List<CustomerBanks> bankList = bankService.selectByExample(example);
		
		CustomerBanks bank = null;
		if(bankList!=null && bankList.size()>0) {
			bank = bankList.get(0);
		}
		Long customerId = bank.getCustomerId();//客户ID
		return customersService.selectByPrimaryKey(customerId);
	}
	
	@Override
	public List<CustomerAccountItem> searchCustomerRecharge(String searchCond) {
		return customerAccountItemMapper.searchCustomerRecharge(searchCond,EnumAiDebitCreditStatus.DEBIT.getKey());
	}
	
	/** 
	 * @Title: saveSubAccount
	 * @Description: 保存分账
	 * @param accountItemList
	 * @param accountItemId
	 * @param waterAmountList
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#saveSubAccount(java.util.List, java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public int saveSubAccount(List<CustomerAccountItem> accountItemList, Long accountItemId, List<BigDecimal> waterAmountList) {
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date sysDate = new Date();
		
		//查询原账单
		CustomerAccountItem originalBill = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		//原账单贷方辅助
		String assistantJSON = this.getAssistantJSON(originalBill.getCreditAssistant(), originalBill.getId(), originalBill.getCreditAmount(), originalBill.getCreditDigest(), originalBill.getCreditSubject(), sysDate, userBean.getId());
		
		List<CustomerAccountItem> subAccountItemList = new ArrayList<>();//分账后的新账单ID集合
		
		int rows = 0;
		for (int i=0; i<accountItemList.size(); i++) {
			BigDecimal waterAmount = waterAmountList.get(i);//按水量分账时的水量
			CustomerAccountItem ca = accountItemList.get(i);
			CustomerAccountItem subAccountItem = this.getSubAccountItem(ca, assistantJSON, accountItemId, sysDate, waterAmount);
			rows = customerAccountItemMapper.insertSelective(subAccountItem);
			if(rows>0) {
				
				PartitionWater partWater = this.getSplitPartWater(subAccountItem.getCustomerId(),originalBill.getId(), subAccountItem.getId(), waterAmount, sysDate);
				rows = partitionWaterService.insertSelective(partWater);//增加新的分水量记录
			}
			
			if (rows<=0) {
				break;
			}
			subAccountItemList.add(subAccountItem);//把分账后的新账单ID添加到List
		}
		if(rows>0) {
			rows = this.updateOriginalAccountItem(accountItemId, subAccountItemList, userBean.getId());//分账单之后，更新原账单信息
			this.deleteOriginalPartWater(accountItemId);//删除原分水量记录
		}
		
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return rows;
	}
	/**
	 * @Title: insertSubAccountItem
	 * @Description: 增加保存分账账单
	 * @param ca
	 * @param assistantJSON
	 * @param accountItemId
	 * @param sysDate
	 * @return 
	 */
	private CustomerAccountItem getSubAccountItem(CustomerAccountItem ca, String assistantJSON, Long accountItemId, Date sysDate, BigDecimal waterAmount) {
		
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型0
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//登录用户
		
		//贷方科目
		String creditSubjectWaterFee = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		//贷方摘要
		String creditDigest = EnumAiCreditSubjectAction.SUB_ACCOUNT.getValue();
		
		//查询原账单
		CustomerAccountItem originalBill = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		//查询客户-账户
		Long accountId = customerAccountService.getAccountIdByCustomerName(ca.getCustomerId());
		
		//生成新账单
		CustomerAccountItem customerAccountItem = new CustomerAccountItem();
		customerAccountItem.setCustomerId(ca.getCustomerId());
		customerAccountItem.setPeriod(ca.getPeriod());
		customerAccountItem.setAccountId(accountId);
		
		customerAccountItem.setDebitCredit(EnumAiDebitCreditStatus.CREDIT.getKey());//借/贷 ： 贷
		customerAccountItem.setCreditSubject(creditSubjectWaterFee);//贷方科目
		customerAccountItem.setCreditAmount(ca.getCreditAmount());//贷方金额
		customerAccountItem.setCreditDigest(creditDigest);// 贷方摘要:水费分账
		customerAccountItem.setCreditAssistant(assistantJSON);//贷方辅助
		
		customerAccountItem.setDebitAmount(zero);//借方金额设置为0
		
		customerAccountItem.setStartTime(originalBill.getStartTime());//开始时间
		customerAccountItem.setEndTime(originalBill.getEndTime());//结束时间
		customerAccountItem.setRemark(creditDigest);//备注
		
		customerAccountItem.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		customerAccountItem.setAccountStatus(EnumSubAccountStatus.SUB_ACCOUNT_YES.getValue());//已分账
		customerAccountItem.setAccountDate(sysDate);
		customerAccountItem.setAccounter(userBean.getId().toString());
		customerAccountItem.setPid(accountItemId);//分账后设置分账的账单PID为原账单ID
		//查询原账单的分水量记录
		PartitionWater pw = partitionWaterService.getPartitionWater(accountItemId);//查询原账单分水量记录
		BigDecimal baseWaterFee = BigDecimalUtils.multiply(pw.getBasePrice(), waterAmount);//计算基础水费
		BigDecimal sewageWaterFee = BigDecimalUtils.multiply(pw.getTreatmentFee(), waterAmount);//计算污水处理费
		
		customerAccountItem.setBaseWaterFee(baseWaterFee);//基础水费
		customerAccountItem.setSewageWaterFee(sewageWaterFee);//污水处理费水费
		
		return customerAccountItem;
	}
	
	/**
	 * @Title: getSplitPartWater
	 * @Description: 获取拆分后的分水量记录
	 * @param originalBillId
	 * @param billId
	 * @param waterAmount
	 * @return 
	 */
	private PartitionWater getSplitPartWater(Long customerId, Long originalBillId, Long billId, BigDecimal waterAmount, Date sysDate){
		
		PartitionWater partWater = partitionWaterService.getPartitionWater(originalBillId);//查询原账单分水量记录
		Long originalPartWaterId = partWater.getId();//分水量ID
		
		PartitionWater partWaterTemp = partWater;
		
		//根据分账的水量重新计算水费
		BigDecimal waterFee = BigDecimalUtils.multiply(waterAmount, partWaterTemp.getWaterPrice());
		
		partWaterTemp.setId(null);
		partWaterTemp.setCustomerId(customerId);
		partWaterTemp.setWaterAmount(waterAmount);
		partWaterTemp.setRealWaterAmount(waterAmount); 
		partWaterTemp.setWaterFee(waterFee);
		
		partWaterTemp.setPid(originalPartWaterId);//设置原分水量ID
		partWaterTemp.setAccountItemId(billId);//设置账单ID
		
		partWaterTemp.setCreateTime(sysDate);//创建时间
		
		partWaterTemp.setOperationTime(sysDate);//操作日期
		//partitionWaterService.insertSelective(partWaterTemp);
		
		return partWaterTemp;
	}
	
	/**
	 * @Title: deleteOriginalPartWater
	 * @Description: 删除原分水量记录
	 * @param accountItemId
	 * @return 
	 */
	private int deleteOriginalPartWater(Long accountItemId) {
		//条件
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andEqualTo("accountItemId", accountItemId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		//修改内容
		PartitionWater pw = new PartitionWater();
		pw.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		return partitionWaterService.updateByExampleSelective(pw, example);
	}
	
	/**
	 * @Title: updateOriginalAccountItem
	 * @Description: 分账单之后，更新原账单信息
	 * @param accountItemId
	 * @param subAccountItemList
	 * @return 
	 */
	private int updateOriginalAccountItem(Long accountItemId, List<CustomerAccountItem> subAccountItemList, Long operatorId) {
		
		//贷方科目
		//String creditSubjectSubAccount = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		
		//int rows = 0;
		CustomerAccountItem oldAccountItem = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		
		//获取借方辅助核算
		String debitAssisantJSON = this.getDebitAssisantJSON(oldAccountItem.getDebitAssistant(), subAccountItemList, operatorId);
		
		//oldAccountItem.setDebitCredit(EnumAiDebitCreditStatus.CREDIT.getKey());//借/贷
		//oldAccountItem.setDebitSubject(creditSubjectSubAccount);
		//oldAccountItem.setDebitDigest(creditSubjectSubAccount);//借方摘要： 分账
		oldAccountItem.setDebitAmount(oldAccountItem.getCreditAmount());//借方金额
		oldAccountItem.setDebitAssistant(debitAssisantJSON);//借方协助核算
		oldAccountItem.setAccountStatus(EnumSubAccountStatus.SUB_ACCOUNT_YES.getValue());//分账状态设置为已分账
		oldAccountItem.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue());//结算状态设置为已结算
		
		return customerAccountItemMapper.updateByPrimaryKeySelective(oldAccountItem);
		
	}
	
	/**
	 * @Title: getDebitAssisantJSON
	 * @Description: 获取分账后的辅助核算
	 * @param originalAssistantJSON
	 * @param subAccountItemList
	 * @return 
	 */
	private String getDebitAssisantJSON(String originalAssistantJSON, List<CustomerAccountItem> subAccountItemList, Long operatorId) {
		
		Date sysDate = new Date();//系统时间
		
		for(CustomerAccountItem accountItem : subAccountItemList) {
			//CustomerAccountItem accountItem = customerAccountItemMapper.selectByPrimaryKey(accountItem);
			originalAssistantJSON = this.getAssistantJSON(originalAssistantJSON, accountItem.getId(), accountItem.getCreditAmount(), accountItem.getCreditDigest(), accountItem.getCreditSubject(), sysDate, operatorId);
		}
		return originalAssistantJSON;
	}
	
	/**
	 * @Title: selectNewCustomerAccountItem
	 * @Description: 查询分账单之后产生的新账单
	 * @param customerId
	 * @param period
	 * @param debitCredit
	 * @return 
	 */
	public List<CustomerAccountItem> selectNewCustomerAccountItem(Long customerId, String period, String debitCredit){
		return customerAccountItemMapper.selectNewCustomerAccountItem(customerId, period, debitCredit);
	}

	/** 
	 * @Title: saveCancelSubAccount
	 * @Description: 保存撤销分账单
	 * @param accountItemId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#saveCancelSubAccount(java.lang.Long)
	 */
	@Override
	@Transactional
	public int saveCancelSubAccount(Long accountItemId) {
		
		final Long zero = 0l;//常量0
		
		int rows = 1;
		// 获取当前账单
		CustomerAccountItem currAccount = this.selectByPrimaryKey(accountItemId);
		if (currAccount.getPid().equals(zero)) {// 判断这条账单是否是主账单； 主账单PID=0
			rows = this.cancelSubAccount(currAccount.getId());//取消分账
			this.cancelSplitPartWater(currAccount.getId());//取消拆分分水量记录
		} else {//如果是分账单,则获取主账单ID
			rows = this.cancelSubAccount(currAccount.getPid());//取消分账
			this.cancelSplitPartWater(currAccount.getPid());//取消拆分分水量记录
		}

		if (rows <= 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

		return rows;
	}
	
	/**
	 * @Title: cancelSubAccount
	 * @Description: 撤销分账更新账单内容
	 * @param accountItemId
	 * @return 
	 */
	private int cancelSubAccount(Long accountItemId) {
		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andEqualTo("pid", accountItemId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerAccountItem> subAccountList = customerAccountItemMapper.selectByExample(example);
		for (CustomerAccountItem item : subAccountList) {// 更新分账单状态
			item.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			customerAccountItemMapper.updateByPrimaryKeySelective(item);
		}
		CustomerAccountItem account = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		// 更新原账单信息
		account.setDebitDigest("");
		account.setDebitAssistant("");
		account.setDebitAmount(new BigDecimal("0"));
		account.setAccountStatus(EnumSubAccountStatus.SUB_ACCOUNT_NO.getValue());
		account.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_NORMAL.getValue());//结算状态设置为已结算
		return customerAccountItemMapper.updateByPrimaryKeySelective(account);
	}
	
	/**
	 * @Title: cancelSplitPartWater
	 * @Description: 取消拆分分水量记录
	 * @param accountItemId	原账单ID(分账前账单)
	 * @return 
	 */
	private int cancelSplitPartWater(Long accountItemId) {

		//原分水量记录
		PartitionWater originalPartWater = partitionWaterService.getPartitionWater(accountItemId);
		
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andEqualTo("pid", originalPartWater.getId()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<PartitionWater> splitPwList = partitionWaterService.selectByExample(example);
		for (PartitionWater item : splitPwList) {// 删除拆分分水量记录
			item.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			partitionWaterService.updateByPrimaryKeySelective(item);
		}
		//更新原分水量记录删除状态
		originalPartWater.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return partitionWaterService.updateByPrimaryKeySelective(originalPartWater);
	}

	@Override
	public List<Map<String, Object>> getCustomerBillList(Long customerId) {
		return customerAccountItemMapper.getCustomerBillList(customerId);
	}
	public List<Map<String, Object>> searchCustomerAccountItemErrorFee(String period, String locationCode,
			Integer settlementStatus, String searchCond, Long operatorId, String startDate , String endDate) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.searchCustomerAccountItemErrorFee(period, locationCode, settlementStatus, searchCond, operatorId, creditSubjectList, startDate, endDate);
	}
	
	
	@Override
	public List<Map<String, Object>> getCustomersListByLocation(String period, String traceIds,
			Integer settlementStatus, String searchCond, Integer customerType) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getCustomersListByLocation(period, traceIds, settlementStatus, searchCond, 
				creditSubjectList, customerType);
	}
	
	@Override
	public BigDecimal getwaterFeeArrears(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getwaterFeeArrears(customerId, period, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey());
	}

	@Override
	public List<Map<String, Object>> getCustomerFeeList(Long customerId,String startDate,String endDate) {
		
		return customerAccountItemMapper.getCustomerFeeList(customerId,startDate,endDate);
	}

	@Override
	public List<CustomerAccountItem> getAllCustomerAccountItemByPeriod(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getAllCustomerAccountItemByPeriod(customerId, period, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey());
	}

	@Override
	public BigDecimal getCustomerBalance(Long customerId) {
		String debitCredit = EnumAiDebitCreditStatus.DEBIT.getKey();//借/贷状态为借
		
		BigDecimal customerBalance = new BigDecimal(0.00);
		BigDecimal balance = customerAccountItemMapper.getCustomerBalance(customerId, debitCredit);
		if(balance!=null) {
			customerBalance = balance;
		}
		return customerBalance;
	}
	
	/** 
	 * @Title: getHaveBalanceRechargeBill
	 * @Description: 查询某客户有余额的充值账单 
	 * 					借/贷状态为借 
	 * 					借方金额!=贷方金额 
	 * 					借方金额-贷方金额>0
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getHaveBalanceRechargeBill(java.lang.Long)
	 */
	@Override
	public List<CustomerAccountItem> getHaveBalanceRechargeBill(Long customerId) {
		String debitCredit = EnumAiDebitCreditStatus.DEBIT.getKey();//借/贷状态为借
		return customerAccountItemMapper.getHaveBalanceRechargeBill(customerId, debitCredit);
	}
	
	/** 
	 * @Title: getHaveBalanceWaterFeeBill
	 * @Description: 查询某客户有余额的水费账单（不包含分账账单） 
	 * 					借/贷状态为贷 贷方科目为 201（其中第一位表示借/贷，第二、三位表示水费；不包含分账账单） 
	 * 					贷方金额!=借方金额 
	 * 					贷方金额-借方金额<0
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getHaveBalanceWaterFeeBill(java.lang.Long)
	 */
	@Override
	public List<CustomerAccountItem> getHaveBalanceWaterFeeBill(Long customerId) {
		String debitCredit = EnumAiDebitCreditStatus.CREDIT.getKey();//借贷为贷
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getHaveBalanceWaterFeeBill(customerId, debitCredit, creditSubjectList);
	}
	
	//-----------------------------生成账单-------------------------------------------------------------------------
	
	/** 
	 * @Title: generatorStandardBill
	 * @Description: 生成标准抄表账单（如果欠费金额有为负值时，自动结算本账单）
	 * @param pw
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#generatorStandardBill(com.learnbind.ai.model.PartitionWater)
	 */
	@Override
	@Transactional
	public Long generatorStandardBill(PartitionWater pw) {
		
		int rows = 0;
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Long accountItemId = this.generatorBill(pw);//生成账单，并返回生成账单的主键ID
		Long customerId = pw.getCustomerId();//获取客户ID
		CustomerAccountItem oweBill = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		//查询客户有余额的水费账单（欠费金额为负）
		List<CustomerAccountItem> waterFeeBillList = this.getHaveBalanceWaterFeeBill(customerId);
		//循环往期水费账单列表，对本次欠费账单结算
		for(CustomerAccountItem waterFeeBill : waterFeeBillList) {
			//如果查询到的往期水费账单与本次欠费账单ID相同，则直接循环下一个
			if(waterFeeBill.getId().equals(accountItemId)) {
				rows = 1;
				continue;
			}
			
			//this.settleAccount(accountItemId, operatorId, settleAmount, subjectAction, subjectPayment);
			rows = this.settleBillToBill(oweBill, waterFeeBill, userBean.getId());//往期水费账单结算本期欠费的水费账单，更新本期欠费账单和往期水费账单
			if(rows<=0) {
				accountItemId = 0l;//如果结算失败则设置账目ID为0
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		
		return accountItemId;
	}
	
	/**
	 * @Title: createBill
	 * @Description: 创建账单
	 * @param pw
	 * @param waterPrice
	 * @param customer
	 * @param accountId
	 * @param operatorId
	 * @return 
	 * 		返回生成账单的主键ID
	 */
	@Override
	@Transactional
	public Long generatorBill(PartitionWater pw) {
		
		log.debug("----------生成账单----------  分水量:"+pw);
		
		//贷方科目
		String creditSubject = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		//贷方摘要
		//String creditDigest = EnumAiDebitCreditStatus.CREDIT.getValue()+EnumAiCreditSubjectAction.WATER_FEE.getValue();
		String creditDigest = EnumAiCreditSubjectAction.WATER_FEE.getValue();
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BigDecimal zero = new BigDecimal("0.00");//初始化BigDecimal类型的0
		
		Long customerId = pw.getCustomerId();//客户ID
		
		//Customers customer = customersService.selectByPrimaryKey(customerId);//客户信息
		CustomerAccount ca = this.getCustomerAccount(customerId);//客户-账户信息
		
		CustomerAccountItem item = new CustomerAccountItem();//账目
		item.setCustomerId(customerId);//客户ID
		item.setAccountId(ca.getId());//账户ID
		item.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//		item.setDebitDigest();//借方摘要
//		item.setDebitSubject();//借方科目
//		item.setDebitAssistant(debitAssistant);//借方辅助核算
		item.setDebitAmount(zero);//借方金额
//		
		item.setCreditSubject(creditSubject);//贷方科目
		item.setCreditDigest(creditDigest);//贷方摘要
		//item.setCreditAssistant(AccountItemConstant.CREDIT_ASSISTANT_SYS_AUTO);//贷方辅助核算
		item.setCreditAmount(pw.getWaterFee().setScale(2));//贷方金额
//		
		item.setDebitCredit(EnumAiDebitCreditStatus.CREDIT.getKey());//借/贷;
//		item.setOverdueDate(overdueDate);//违约金计算日期
		
		item.setAccounter(userBean.getId().toString());//记账人
		item.setAccountDate(new Date());//记账日期
		
		item.setStartTime(pw.getStartTime());//账单产生时间段的开始时间
		item.setEndTime(pw.getEndTime());//账单产生时间段的结束时间
		
		item.setPeriod(pw.getPeriod());//期间
		
		item.setRemark(creditDigest);//备注
		
		BigDecimal baseWaterFee = BigDecimalUtils.multiply(pw.getBasePrice(), pw.getRealWaterAmount());//计算基础水费
		BigDecimal sewageWaterFee = BigDecimalUtils.multiply(pw.getTreatmentFee(), pw.getRealWaterAmount());//计算污水处理费
		
		item.setBaseWaterFee(baseWaterFee);//基础水费
		item.setSewageWaterFee(sewageWaterFee);//污水处理费水费
		
		
		//如果水量>0，则设置结算状态为未结算，否则设置结算状态为全部结算
		//结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
		if(BigDecimalUtils.greaterThan(pw.getRealWaterAmount(), zero)) {
			item.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_NORMAL.getValue());
		}else {
			item.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue());
		}
		
		//获取分账状态
		Integer subAccountStatus = this.getSubAccountStatus(customerId, pw.getMeterId());
		item.setAccountStatus(subAccountStatus);//设置分账状态
		
		int rows = customerAccountItemMapper.insertSelective(item);
		if(rows>0) {
			//更新状态 更新分水量开账状态，并设置账目ID; 更新水价日志表中的账目ID; 更新抄表记录的开账状态；
			this.updateStatus(pw.getId(), item.getId(), pw.getRecordId());
			//增加水费账单日志
			accountItemTraceService.insert(item.getId(), item.getCreditSubject(), userBean.getId().toString(), pw.getWaterFee().setScale(2));
			
			return item.getId();//生成账单成功后返回账单ID
		}
		return 0l;
	}
	
	/**
	 * @Title: updateStatus
	 * @Description: 更新状态
	 * 					更新分水量开账状态，并设置账目ID
	 * 					更新水价日志表中的账目ID
	 * 					更新抄表记录的开账状态
	 * @param partitionWaterId
	 * @param accountItemId
	 * @param recordIds 
	 */
	private void updateStatus(Long partitionWaterId, Long accountItemId, String recordIds) {
		//修改分水量表是否已开账状态为已开账，且设置分水量表中账目ID
		PartitionWater pwRecord = new PartitionWater();
		pwRecord.setId(partitionWaterId);
		pwRecord.setAccountItemId(accountItemId);
		pwRecord.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_YES.getValue());
		partitionWaterService.updateByPrimaryKeySelective(pwRecord);
		//修改水价日志表的账目ID
		useWaterPriceTraceService.updateAccountItemId(partitionWaterId, accountItemId);
		
		//修改抄表记录开账状态
		this.updateMeterRecordMakeBill(recordIds, EnumMakeBillStatus.MAKE_BILL_YES.getValue());
		
	}
	
	/**
	 * @Title: updateMeterRecordMakeBill
	 * @Description: 修改抄表记录开账状态
	 * @param recordIds		抄表记录ID（1，2）
	 * @param isMakeBill 	开账状态
	 */
	private void updateMeterRecordMakeBill(String recordIds, Integer isMakeBill) {
		if(StringUtils.isNotBlank(recordIds)) {
			List<Long> recordIdList = new ArrayList<>();
			String[] recordIdArr = recordIds.split(",");
			for(String recordId : recordIdArr) {
				if(StringUtils.isNotBlank(recordId)) {
					recordIdList.add(Long.valueOf(recordId));
				}
			}
			
			Example example = new Example(MeterRecord.class);
			example.createCriteria().andIn("id", recordIdList);
			MeterRecord record = new MeterRecord();
			record.setIsMakeBill(isMakeBill);
			meterRecordService.updateByExampleSelective(record, example);
		}
		
	}
	
	/**
	 * @Title: getSubAccountStatus
	 * @Description: TODO 获取分水量状态
	 * @param customerId
	 * @param meterIds
	 * @return 
	 */
	private Integer getSubAccountStatus(Long customerId, String meterIds) {
		
		Customers customer = customersService.selectByPrimaryKey(customerId);//客户信息
		Integer customerType = customer.getCustomerType();//客户类型
		Integer chargeMode = customer.getChargeMode();//收费方式
		//如果客户类型为单位 或 （客户类型为个人且收费方式为单独收费）
		if(customerType==EnumCustomerType.CUSTOMER_UNIT.getValue() || (customerType==EnumCustomerType.CUSTOMER_PEOPLE.getValue() && chargeMode==EnumCustomerChargeMode.ALONE_CHARGE.getValue())) {
			
			//拆分表计ID为数据
			String[] meterIdArr = meterIds.split(",");
			meterIdArr = this.meterIdArrUniq(meterIdArr);//去重
			if(meterIdArr!=null && meterIdArr.length>0) {
				String meterId = meterIdArr[0];
				CustomerMeter cm = new CustomerMeter();
				cm.setMeterId(Long.valueOf(meterId));
				//查询表计绑定的客户数量
				int count = customerMeterService.selectCount(cm);
				if(count>1) {//客户数量大于1时设置分账状态为未分账
					return EnumSubAccountStatus.SUB_ACCOUNT_NO.getValue();
				}else {//客户数量等于1时设置分账状态为不需要分账
					EnumSubAccountStatus.NO_NEED_SUB_ACCOUNT.getValue();
				}
			}
		}
		
		return EnumSubAccountStatus.NO_NEED_SUB_ACCOUNT.getValue();
	}
	
	/**
	 * @Title: meterIdArrUniq
	 * @Description: 表计数组去重
	 * @param arrStr
	 * @return 
	 */
	private String[] meterIdArrUniq(String [] arrStr) {
		
		if(arrStr==null || arrStr.length<=0) {
			return null;
		}
		
        Map<String, Object> map = new HashMap<String, Object>();
        for (String str : arrStr) {
            map.put(str, str);
        }
        //返回一个包含所有对象的指定类型的数组
        String[] newArrStr =  map.keySet().toArray(new String[1]);
        System.out.println(Arrays.toString(newArrStr));
        return newArrStr;
    }
	
	@Override
	@Transactional
	public Long generatorRechargeBill(Long customerId, BigDecimal rechargeAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Long operatorId) {
		
		log.debug("----------生成充值账单----------  ");
		
		Date sysDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		//借方科目：借/贷状态+科目动作+科目支付方式
		String debitSubject = EnumAiDebitCreditStatus.DEBIT.getKey()+subjectAction.getKey()+subjectPayment.getKey();
		//借方摘要：借/贷状态+科目动作+科目支付方式
		String debitDigest = subjectAction.getValue()+subjectPayment.getValue();
		
		//登录用户
		//UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BigDecimal zero = new BigDecimal("0.00");//初始化BigDecimal类型的0
		
		//Customers customer = this.getCustomer(customerId);//客户信息
		CustomerAccount ca = this.getCustomerAccount(customerId);//客户-账户信息
		
		CustomerAccountItem item = new CustomerAccountItem();//账目
		item.setCustomerId(customerId);//客户ID
		item.setAccountId(ca.getId());//账户ID
		
		item.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		item.setDebitSubject(debitSubject);//借方科目
		item.setDebitDigest(debitDigest);//借方摘要
//		item.setDebitAssistant(ca.getId().toString());//借方辅助核算 TODO 后面增加日志后需要再维护此字段
		item.setDebitAmount(rechargeAmount);//借方金额
		
//		item.setCreditDigest();//贷方摘要
//		item.setCreditSubject();//贷方科目
//		item.setCreditAssistant();//贷方辅助核算
		item.setCreditAmount(zero);//贷方金额
//		
		item.setDebitCredit(EnumAiDebitCreditStatus.DEBIT.getKey());//借/贷;
//		item.setOverdueDate(overdueDate);//违约金计算日期
		
		item.setAccounter(operatorId.toString());//记账人
		item.setAccountDate(sysDate);//记账日期
		
//		item.setStartTime();//账单产生时间段的开始时间
//		item.setEndTime();//账单产生时间段的结束时间
		
		item.setPeriod(sdf.format(sysDate));//期间为充值日期的年和月
		
//		item.setRemark(debitDigest);//备注
		
//		item.setPartitionWaterId();
		
		int rows = customerAccountItemMapper.insertSelective(item);
		if(rows>0) {
			//增加充值账单日志
			accountItemTraceService.insert(item.getId(), item.getDebitSubject(), operatorId.toString(), rechargeAmount.setScale(2));
		}
		return item.getId();
	}
	
	/**
	 * @Title: getCustomerAccount
	 * @Description: 查询客户-账户
	 * @param customerId
	 * @return 
	 */
	private CustomerAccount getCustomerAccount(Long customerId) {
		CustomerAccount ca = new CustomerAccount();
		ca.setCustomerId(customerId);
		return customerAccountService.selectOne(ca);
	}
	
	//--------------------------------结算部分--------------------------------------------------------------------------------------------------
	@Override
	@Transactional
	public int settleCustomerBill(Long customerId, String period, Long operatorId, BigDecimal settleAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Integer paymentType, String traceOperate) {
		
		try {
			int rows=1;
			
			//登录用户
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
			//生成充值记录账单
			Long rechargeBillId = this.generatorRechargeBill(customerId, settleAmount, subjectAction, subjectPayment, userBean.getId());
			
			//查询本期水费欠费账单
			List<CustomerAccountItem> waterFeeOweBillList = this.getWaterFeeOweBillList(customerId, period);
			for(CustomerAccountItem waterFeeBill : waterFeeOweBillList) {
				
				rows = this.settleCustomerBill(rechargeBillId, waterFeeBill, subjectAction, subjectPayment, paymentType, traceOperate, operatorId, false);
				if(rows<=0) {
					break;
				}
			}
			
			if(rows>0) {
				//查询往期水费欠费账单
				List<CustomerAccountItem> pastWaterFeeOweBillList = this.getPastWaterFeeOweBillList(customerId, period);
				for(CustomerAccountItem pastWaterFeeBill : pastWaterFeeOweBillList) {
					
					rows = this.settleCustomerBill(rechargeBillId, pastWaterFeeBill, subjectAction, subjectPayment, paymentType, traceOperate, operatorId, true);
					if(rows<=0) {
						break;
					}
				}
			}
			
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;

	}
	
	@Override
	@Transactional
	public int balanceSettleCustomerBill(Long customerId, Long operatorId, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, String traceOperate) {
		
		try {
			int rows=1;
			
			//查询客户水费欠费账单
			List<CustomerAccountItem> oweBillList = this.getWaterFeeOweBillList(customerId, null);
			for(CustomerAccountItem oweBill : oweBillList) {
				rows = this.balanceSettleAccount(oweBill.getId(), operatorId, subjectAction, subjectPayment, traceOperate);
				if(rows==-1) {//-1表示客户余额小于0，直接退出
					break;
				}else if(rows==-2) {//-2表示客户余额小于欠费金额，可执行结算下个水费账单
					continue;
				}
			}
			
//			if(rows<=0) {
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;

	}
	
	/**
	 * @Title: settleCustomerBill
	 * @Description: 结算客户账单-子函数
	 * @param rechargeBillId
	 * @param waterFeeBill
	 * @param subjectAction
	 * @param subjectPayment
	 * @param paymentType
	 * @param traceOperate
	 * @param isPastOweBill	是否是往期欠费账单
	 * @return 
	 */
	private int settleCustomerBill(Long rechargeBillId, CustomerAccountItem waterFeeBill, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Integer paymentType, String traceOperate, Long operatorId, boolean isPastOweBill) {
		
		int rows = 1;
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
		
		BigDecimal billOweAmount = this.getBillOweAmount(subjectAction, waterFeeBill);//根据支付类型获取账单欠费金额
		
		//查询结算账单
		//CustomerAccountItem waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		//计算账单欠费金额
		//BigDecimal billOweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
		
		//重新查询充值账单，保证充值账单中各属性都是最新值
		CustomerAccountItem rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeBillId);
		//rechargeBill.setDebitAmount(settleAmount);
		
		boolean isSettleOverdueBill = true;//是否继续结算违约金账单
		BigDecimal debtAmount = new BigDecimal(0.00);//清欠金额
		if(BigDecimalUtils.greaterThan(billOweAmount, zero)) {//如果欠费金额>0时，结算水费账单
			//结算水费账单
			if(BigDecimalUtils.equals(rechargeBill.getDebitAmount(), billOweAmount)) {//如果充值金额=欠费金额
				debtAmount = billOweAmount;//设置清欠金额
				isSettleOverdueBill = false;//是否继续结算违约金账单
				rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
				
			}else if(BigDecimalUtils.greaterThan(rechargeBill.getDebitAmount(), billOweAmount)) {//如果充值金额>欠费金额
				debtAmount = billOweAmount;//设置清欠金额
				rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
				//settleAmount = BigDecimalUtils.subtract(settleAmount, billOweAmount);//设置充值金额=充值金额-欠费账单金额
				//rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeItemId);//结算完成后查询最新的充值记录信息
				//rechargeBill.setDebitAmount(settleAmount);
			}else {//如果充值金额<欠费金额
				debtAmount = rechargeBill.getDebitAmount();//设置清欠金额
				isSettleOverdueBill = false;//是否继续结算违约金账单
				rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
			}
		}
		if(rows>0) {
			if(isPastOweBill || (paymentType!=null && paymentType==EnumPaymentType.PAYMENT_DEBT.getKey())) {
				//生成清欠账单
				this.generatorDebtBill(rechargeBillId, waterFeeBill.getId(), debtAmount, subjectAction, subjectPayment);
			}
		}
		if(isSettleOverdueBill) {
			//TODO 结算水费账单的违约金账单
			//rows = this.settleOverdueBill(waterFeeBill.getId(), rechargeBill, settleAmount, subjectAction, traceOperate);
		}
		
		return rows;
	}
	
	/**
	 * @Title: getBillOweAmount
	 * @Description: 获取账单欠费金额
	 * @param subjectAction
	 * @param waterFeeBill
	 * @return 
	 */
	private BigDecimal getBillOweAmount(EnumAiDebitSubjectAction subjectAction, CustomerAccountItem waterFeeBill) {
		BigDecimal billOweAmount = new BigDecimal(0.00);//欠费金额
		if(subjectAction.getKey().equalsIgnoreCase(EnumAiDebitSubjectAction.PAY_WATER_FEE.getKey())) {//如果缴费类型是综合水费时
			billOweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());//综合水费欠费金额
		}else if(subjectAction.getKey().equalsIgnoreCase(EnumAiDebitSubjectAction.PAY_BASE_FEE.getKey())) {//如果缴费类型是基础水费时
			//查询分水量
			PartitionWater pw = partitionWaterService.getPartitionWater(waterFeeBill.getId());
			//基础水费
			BigDecimal baseAmount = BigDecimalUtils.multiply(pw.getBasePrice(), pw.getRealWaterAmount());
			//基础水费已充值金额
			BigDecimal payAmount = this.getBaseFeePaymetnAmount(waterFeeBill.getDebitAssistant());
			billOweAmount = BigDecimalUtils.subtract(baseAmount, payAmount);//基础水费欠费金额
		}else {//如果缴费类型不是基础水费时
			//查询分水量
			PartitionWater pw = partitionWaterService.getPartitionWater(waterFeeBill.getId());
			//污水处理费
			BigDecimal treatAmount = BigDecimalUtils.multiply(pw.getTreatmentFee(), pw.getRealWaterAmount());
			//污水处理费已充值金额
			BigDecimal payAmount = this.getTreatmentFeePaymentAmount(waterFeeBill.getDebitAssistant());
			billOweAmount = BigDecimalUtils.subtract(treatAmount, payAmount);//污水处理费欠费金额
		}
		return billOweAmount;
	}
	
	/**
	 * @Title: getBaseFeePaymetnAmount
	 * @Description: 获取账单基础水费已充值金额
	 * @param debitAssistant
	 * @return 
	 */
	private BigDecimal getBaseFeePaymetnAmount(String debitAssistant) {
		
		//基础水价科目
		String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_BASE_FEE.getKey();
		
		BigDecimal payAmount = new BigDecimal(0.00);//基础水价已支付金额变量
		
		//assistant
		if(StringUtils.isNotBlank(debitAssistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(debitAssistant, AssistantBean.class);
			
			for(AssistantBean assistantBean : assistantBeanList) {
				String subject = assistantBean.getSubject();
				if(subject.indexOf(subjectBase)!=-1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount); 
				}
			}
		}
		return payAmount;
	}
	
	/**
	 * @Title: getTreatmentFeePaymentAmount
	 * @Description: 获取账单污水处理费已充值金额
	 * @param debitAssistant
	 * @return 
	 */
	private BigDecimal getTreatmentFeePaymentAmount(String debitAssistant) {
		
		//污水处理费科目
		String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_TREATMENT_FEE.getKey();
		
		BigDecimal payAmount = new BigDecimal(0.00);//污水处理费已支付金额变量
		
		//assistant
		if(StringUtils.isNotBlank(debitAssistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(debitAssistant, AssistantBean.class);
			
			for(AssistantBean assistantBean : assistantBeanList) {
				String subject = assistantBean.getSubject();
				if(subject.indexOf(subjectBase)!=-1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount); 
				}
			}
		}
		return payAmount;
	}
	
	@Override
	@Transactional
	public int settleAccount(Long accountItemId, Long operatorId, BigDecimal settleAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, Integer paymentType, String traceOperate) {
		
		try {
			int rows=1;
			
			//登录用户
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
			
			//查询结算账单
			CustomerAccountItem waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
			//计算账单欠费金额
			BigDecimal billOweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
			
			//生成充值记录账单
			Long rechargeItemId = this.generatorRechargeBill(waterFeeBill.getCustomerId(), settleAmount, subjectAction, subjectPayment, userBean.getId());
			CustomerAccountItem rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeItemId);
			rechargeBill.setDebitAmount(settleAmount);
			
			boolean isSettleOverdueBill = true;//是否继续结算违约金账单
			BigDecimal debtAmount = new BigDecimal(0.00);//清欠金额
			if(BigDecimalUtils.greaterThan(billOweAmount, zero)) {//如果欠费金额>0时，结算水费账单
				//结算水费账单
				if(BigDecimalUtils.equals(rechargeBill.getDebitAmount(), billOweAmount)) {//如果充值金额=欠费金额
					debtAmount = billOweAmount;//设置清欠金额
					isSettleOverdueBill = false;//是否继续结算违约金账单
					rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
					
				}else if(BigDecimalUtils.greaterThan(rechargeBill.getDebitAmount(), billOweAmount)) {//如果充值金额>欠费金额
					debtAmount = billOweAmount;//设置清欠金额
					rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
					//settleAmount = BigDecimalUtils.subtract(settleAmount, billOweAmount);//设置充值金额=充值金额-欠费账单金额
					rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeItemId);//结算完成后查询最新的充值记录信息
					//rechargeBill.setDebitAmount(settleAmount);
				}else {//如果充值金额<欠费金额
					debtAmount = settleAmount;//设置清欠金额
					isSettleOverdueBill = false;//是否继续结算违约金账单
					rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
				}
			}
			
			if(rows>0) {
				if(paymentType!=null && paymentType==EnumPaymentType.PAYMENT_DEBT.getKey()) {
					//生成清欠账单
					this.generatorDebtBill(rechargeItemId, waterFeeBill.getId(), debtAmount, subjectAction, subjectPayment);
				}
			}
			
			if(isSettleOverdueBill) {
				//TODO 结算水费账单的违约金账单
				//rows = this.settleOverdueBill(waterFeeBill.getId(), rechargeBill, settleAmount, subjectAction, traceOperate);
			}
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;

	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: balanceSettleAccount
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param accountItemId
	 * @param operatorId
	 * @param subjectAction
	 * @param subjectPayment
	 * @param traceOperate
	 * @return 
	 * 		-1表示客户余额小于0，-2表示客户余额小于欠费金额
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#balanceSettleAccount(java.lang.Long, java.lang.Long, com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction, com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment, java.lang.String)
	 */
	@Override
	@Transactional
	public int balanceSettleAccount(Long accountItemId, Long operatorId, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment, String traceOperate) {
		
		try {
			int rows=1;
			
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
			
			//查询水费账单
			CustomerAccountItem waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
			Long customerId = waterFeeBill.getCustomerId();//客户ID
			BigDecimal customerBalance = this.getCustomerBalance(customerId);//客户余额
			if(BigDecimalUtils.lessOrEquals(customerBalance, zero)) {//如果客户余额<=0时,直接返回
				return -1;
			}
			
			//欠费金额
			BigDecimal oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
			if(BigDecimalUtils.lessThan(customerBalance, oweAmount)) {//如果客户余额<欠费金额，直接返回余额不足
				return -2;
			}
			
			//查询有余额的充值账单
			List<CustomerAccountItem> rechargeBillList = this.getHaveBalanceRechargeBill(customerId);
			for(CustomerAccountItem rechargeBill : rechargeBillList) {
				//欠费金额
				oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
				if(BigDecimalUtils.lessOrEquals(oweAmount, zero)) {//如果欠费金额<=0时，自动执行下一次循环
					break;
				}
				//结算
				rows = this.settleBill(waterFeeBill, rechargeBill, subjectAction, EnumAiTraceOperate.AUTO_SETTLEMENT.getKey(), operatorId);
				if(rows>0) {
					waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(waterFeeBill.getId());
					
				}
			}
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;

	}
	
	/**
	 * @Title: settleOverdueBill
	 * @Description: 结算违约金账单
	 * @param waterFeeBillId
	 * @param rechargeBill
	 * @param settleAmount
	 * @param traceOperate
	 * @return 
	 */
	private int settleOverdueBill(Long waterFeeBillId, CustomerAccountItem rechargeBill, BigDecimal settleAmount, EnumAiDebitSubjectAction subjectAction, String traceOperate, Long operatorId) {
		
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
		
		int rows = 1;
		//查询结算账单的违约金账单
		List<CustomerAccountItem> overdueBillList = this.getOverdueBillList(waterFeeBillId);
		if(overdueBillList!=null && overdueBillList.size()>0) {
			for(CustomerAccountItem overdueBill : overdueBillList) {
				
				//计算违约金的欠费金额
				BigDecimal overdueBillOweAmount = BigDecimalUtils.subtract(overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
				
				if(BigDecimalUtils.lessOrEquals(overdueBillOweAmount, zero)) {//如果欠费金额<=0时，直接结算下一个违约金账单
					continue;
				}
				
				rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeBill.getId());//结算完成后查询最新的充值记录信息
				//rechargeBill.setDebitAmount(settleAmount);
				
				//结算水费账单
				if(BigDecimalUtils.equals(rechargeBill.getDebitAmount(), overdueBillOweAmount)) {//如果充值金额=欠费金额，结算后直接退出
					rows=this.settleBill(overdueBill, rechargeBill, subjectAction, traceOperate, operatorId);
					break;
				}else if(BigDecimalUtils.greaterThan(rechargeBill.getDebitAmount(), overdueBillOweAmount)) {//如果充值金额>欠费金额，结算后继续
					//isSettleOverdueBill = true;//是否继续结算违约金账单
					rows=this.settleBill(overdueBill, rechargeBill, subjectAction, traceOperate, operatorId);
					if(rows<=0) {
						break;
					}
					//settleAmount = BigDecimalUtils.subtract(settleAmount, overdueBillOweAmount);//设置充值金额=充值金额-欠费账单金额
				}else {//如果充值金额<欠费金额，结算后直接退出
					rows=this.settleBill(overdueBill, rechargeBill, subjectAction, traceOperate, operatorId);
					break;
				}
			}
		}
		return rows;
	}
	
	/**
	 * @Title: settleBill
	 * @Description: 结算账单，更新欠费账单和充值账单
	 * @param oweBill
	 * @param rechargeBill
	 * @param subjectAction	缴费类型
	 * @param traceOperate
	 * @return 
	 */
	//@Transactional
	private int settleBill(CustomerAccountItem oweBill, CustomerAccountItem rechargeBill, EnumAiDebitSubjectAction subjectAction, String traceOperate, Long operatorId) {
		
		try {
			int rows = 0;
			
			//计算账单欠费金额
			//BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
			BigDecimal billOweAmount = this.getBillOweAmount(subjectAction, oweBill);//根据支付类型获取账单欠费金额
			//计算充值账单余额
			BigDecimal billBalance = BigDecimalUtils.subtract(rechargeBill.getDebitAmount(), rechargeBill.getCreditAmount());
			
			//欠费账单的贷方金额
			//BigDecimal oweCreditAmount = oweBill.getCreditAmount();
			//欠费账单的借方金额
			BigDecimal oweDebitAmount = oweBill.getDebitAmount();
			//充值账单的借方金额
			//BigDecimal rechargeDebitAmount = rechargeBill.getDebitAmount();
			//充值账单的贷方金额
			BigDecimal rechargeCreditAmount = rechargeBill.getCreditAmount();
			
			BigDecimal debitAmount = new BigDecimal(0.00);//需要更新欠费账单中的借方金额
			BigDecimal creditAmount = new BigDecimal(0.00);//需要更新充值账单中的贷方金额
			
			BigDecimal assistantDebitAmount = new BigDecimal(0.00);//辅助核算的借方金额
			BigDecimal assistantCreditAmount = new BigDecimal(0.00);//辅助核算的贷方金额
			
			if(BigDecimalUtils.equals(billBalance, billOweAmount)) {//如果充值金额=欠费金额
				debitAmount = BigDecimalUtils.add(billBalance, oweDebitAmount);
				creditAmount = BigDecimalUtils.add(billOweAmount, rechargeCreditAmount);
				
				assistantDebitAmount = billBalance;//辅助核算的借方金额
				assistantCreditAmount = billOweAmount;//辅助核算的贷方金额
			}else if(BigDecimalUtils.greaterThan(billBalance, billOweAmount)) {//如果充值金额>欠费金额
				debitAmount = BigDecimalUtils.add(billOweAmount, oweDebitAmount);
				creditAmount = BigDecimalUtils.add(billOweAmount, rechargeCreditAmount);
				
				assistantDebitAmount = billOweAmount;//辅助核算的借方金额
				assistantCreditAmount = billOweAmount;//辅助核算的贷方金额
			}else {//如果充值金额<欠费金额
				debitAmount = BigDecimalUtils.add(billBalance, oweDebitAmount);
				creditAmount = BigDecimalUtils.add(billBalance, rechargeCreditAmount);
				
				assistantDebitAmount = billBalance;//辅助核算的借方金额
				assistantCreditAmount = billBalance;//辅助核算的贷方金额
			}
			
			
			Date sysDate = new Date();//系统时间
			//登录用户
			//UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//-----------------------------更新欠费账单的借方信息------------------------------
			Long rechargeBillId = rechargeBill.getId();
			String debitSubject = rechargeBill.getDebitSubject();
			String debitDigest = rechargeBill.getDebitDigest();
			//充值账单的借方金额
			//BigDecimal rechargeDebitAmount = rechargeBill.getDebitAmount();
			
			//BigDecimal debitAmount = rechargeBill.getDebitAmount();
			
			//欠费账单中原有的借方辅助核算
			String debitAssistant = oweBill.getDebitAssistant();
			//欠费账单的借方辅助核算信息
			String debitAssistantJSON = this.getAssistantJSON(debitAssistant, rechargeBillId, assistantDebitAmount, debitDigest, debitSubject, sysDate, operatorId);
			//更新欠费账单中借方信息
			rows = this.updateOweBillDebitInfo(oweBill.getId(), debitAmount, debitAssistantJSON);
			if(rows>0) {
				accountItemTraceService.insert(oweBill.getId(), oweBill.getCreditSubject(), rechargeBillId, debitSubject, traceOperate, assistantDebitAmount);
			}
			if(rows<=0) {
				//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			//-----------------------------更新充值账单的贷方信息------------------------------
			Long oweBillId = oweBill.getId();
			String creditSubject = oweBill.getCreditSubject();
			//BigDecimal creditAmount = oweBill.getCreditAmount();
			String creditDigest = oweBill.getCreditDigest();
			//充值账单中原有的贷方辅助核算
			String creditAssistant = rechargeBill.getCreditAssistant();
			//充值账单的贷方辅助核算信息
			String creditAssistantJSON = this.getAssistantJSON(creditAssistant, oweBillId, assistantCreditAmount, creditDigest, creditSubject, sysDate, operatorId);
			//更新充值账单中贷方信息
			rows = this.updateRechargeBillCreditInfo(rechargeBill.getId(), creditAmount, creditAssistantJSON);
			if(rows>0) {
				accountItemTraceService.insert(rechargeBill.getId(), rechargeBill.getDebitSubject(), oweBillId, creditSubject, traceOperate, assistantDebitAmount);
			}
			if(rows<=0) {
				//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			
			rows = this.setOweBillSettlementStatus(oweBill.getId());//设置结算状态为成功
			if(rows<=0) {
				//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: settleBillToBill
	 * @Description: 往期水费账单结算本期欠费的水费账单，更新本期欠费账单和往期水费账单
	 * @param oweBill		本期欠费账单
	 * @param waterFeeBill 	往期水费账单
	 */
	@Transactional
	private int settleBillToBill(CustomerAccountItem oweBill, CustomerAccountItem waterFeeBill, Long operatorId) {
		
		try {
			int rows = 0;
			
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型 0
			
			//计算本期账单欠费金额
//			BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
			//计算往期水费账单余额（贷方金额-借方金额<0，结果为负值）
			BigDecimal billBalance = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
			//把计算往期水费账单余额转为正值
			billBalance = BigDecimalUtils.subtract(zero, billBalance);
			
			//欠费账单的贷方金额
			//BigDecimal oweCreditAmount = oweBill.getCreditAmount();
			//欠费账单的借方金额
			BigDecimal oweDebitAmount = oweBill.getDebitAmount();
			//往期水费账单的借方金额
			//BigDecimal rechargeDebitAmount = rechargeBill.getDebitAmount();
			//往期水费账单的借方金额
			BigDecimal waterFeeDebitAmount = waterFeeBill.getDebitAmount();
			
			BigDecimal debitTotalAmount = new BigDecimal(0.00);//需要更新欠费账单中的借方金额
			BigDecimal waterFeeDebitTotalAmount = new BigDecimal(0.00);//需要更新往期水费账单中的借方金额
			
			BigDecimal assistantDebitAmount = new BigDecimal(0.00);//欠费账单的辅助核算的借方金额
			BigDecimal waterFeeAssistantDebitAmount = new BigDecimal(0.00);//往期水费账单的辅助核算的借方金额
			
//			if(BigDecimalUtils.equals(billBalance, billOweAmount)) {//如果往期水费账单余额=欠费金额
				debitTotalAmount = BigDecimalUtils.add(billBalance, oweDebitAmount);
				//因往期水费账单贷方金额为负值，所以此处也需要设置为负值，才可以平账。
				BigDecimal oweAmount = BigDecimalUtils.subtract(zero, billBalance);
				waterFeeDebitTotalAmount = BigDecimalUtils.add(oweAmount, waterFeeDebitAmount);
				
				assistantDebitAmount = billBalance;//辅助核算的借方金额
				waterFeeAssistantDebitAmount = oweAmount;//辅助核算的贷方金额
//			}else if(BigDecimalUtils.greaterThan(billBalance, billOweAmount)) {//如果往期水费账单余额>欠费金额
//				debitTotalAmount = BigDecimalUtils.add(billOweAmount, oweDebitAmount);
//				//因往期水费账单贷方金额为负值，所以此处也需要设置为负值，才可以平账。
//				BigDecimal oweAmount = BigDecimalUtils.subtract(zero, billOweAmount);
//				waterFeeDebitTotalAmount = BigDecimalUtils.add(oweAmount, waterFeeDebitAmount);
//				
//				assistantDebitAmount = billOweAmount;//辅助核算的借方金额
//				waterFeeAssistantDebitAmount = oweAmount;//辅助核算的贷方金额
//			}else {//如果充值金额<欠费金额
//				debitTotalAmount = BigDecimalUtils.add(billBalance, oweDebitAmount);
//				waterFeeDebitTotalAmount = BigDecimalUtils.add(billBalance, waterFeeDebitAmount);
//				
//				assistantDebitAmount = billBalance;//辅助核算的借方金额
//				waterFeeAssistantDebitAmount = billBalance;//辅助核算的贷方金额
//			}
			
			
			Date sysDate = new Date();//系统时间
			//登录用户
			//UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//-----------------------------更新欠费账单的借方信息------------------------------
			Long rechargeBillId = waterFeeBill.getId();
			String debitSubject = waterFeeBill.getDebitSubject();
			String debitDigest = waterFeeBill.getDebitDigest();
			//充值账单的借方金额
			//BigDecimal rechargeDebitAmount = rechargeBill.getDebitAmount();
			
			//BigDecimal debitAmount = rechargeBill.getDebitAmount();
			
			
			//欠费账单中原有的借方辅助核算
			String debitAssistant = oweBill.getDebitAssistant();
			//欠费账单的借方辅助核算信息
			String debitAssistantJSON = this.getAssistantJSON(debitAssistant, rechargeBillId, assistantDebitAmount, debitDigest, debitSubject, sysDate, operatorId);
			//更新欠费账单中借方信息
			rows = this.updateOweBillDebitInfo(oweBill.getId(), debitTotalAmount, debitAssistantJSON);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			//-----------------------------更新往期水费账单的借方信息------------------------------
			Long oweBillId = oweBill.getId();
			String creditSubject = oweBill.getCreditSubject();
			//BigDecimal creditAmount = oweBill.getCreditAmount();
			String creditDigest = oweBill.getCreditDigest();
			//往期水费账单中原有的借方辅助核算
			String waterFeeBillDebitAssistant = waterFeeBill.getDebitAssistant();
			//往期水费账单的借方辅助核算信息
			String waterFeeDebitAssistantJSON = this.getAssistantJSON(waterFeeBillDebitAssistant, oweBillId, waterFeeAssistantDebitAmount, creditDigest, creditSubject, sysDate, operatorId);
			//更新往期水费账单中贷方信息
			rows = this.updateOweBillDebitInfo(waterFeeBill.getId(), waterFeeDebitTotalAmount, waterFeeDebitAssistantJSON);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			
			rows = this.setOweBillSettlementStatus(oweBill.getId());//设置结算状态为成功
			rows = this.setOweBillSettlementStatus(waterFeeBill.getId());//设置结算状态为成功
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: setOweBillSettlementStatus
	 * @Description: 结算成功后根据账单欠费金额判断全部结算或部分结算
	 * @param oweBillId
	 * @return 
	 */
	private int setOweBillSettlementStatus(Long oweBillId) {
		
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
		
		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(oweBillId);
		//计算账单欠费金额
		BigDecimal billOweAmount = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
		
		CustomerAccountItem record = new CustomerAccountItem();
		record.setId(oweBillId);
		
		if(BigDecimalUtils.lessOrEquals(billOweAmount, zero)) {//如果账单欠费金额=0时，则设置结算状态为交易成功；否则设置结算状态为部分结算
			record.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue());
		}else {
			record.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_PART.getValue());
		}
		
		return customerAccountItemMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * @Title: getAssistantJSON
	 * @Description: 获取辅助核算JSON字符串
	 * @param originalAssistant	原账单的辅助核算
	 * @param id				本次辅助核算的账单ID
	 * @param amount			本次辅助核算金额
	 * @param digest			本次辅助核算摘要
	 * @param subject			本次辅助核算科目
	 * @param sysDate			本次结算日期
	 * @return 
	 */
	private String getAssistantJSON(String originalAssistant, Long id, BigDecimal amount, String digest, String subject, Date sysDate, Long operatorId) {
		
		//登录用户
		//UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//原账单中的辅助核算
		List<AssistantBean> assistantBeanList = new ArrayList<>();
		if(StringUtils.isNotBlank(originalAssistant)) {
			assistantBeanList = JSON.parseArray(originalAssistant, AssistantBean.class);
		}
		//本次辅助核算
		AssistantBean assistant = new AssistantBean(id, amount, digest, subject, sysDate, operatorId);
		assistantBeanList.add(assistant);
		//辅助核算信息转JSON字符串
		String assistantJSON = JSON.toJSONString(assistantBeanList);
		return assistantJSON;
	}
	
	/**
	 * @Title: updateOweBillDebitInfo
	 * @Description: 更新欠费账单的借方信息
	 * @param id
	 * @param debitAmount
	 * @param debitAssistantJSON
	 * @return
	 * @throws Exception 
	 */
	//@Transactional
	private int updateOweBillDebitInfo(Long id, BigDecimal debitAmount, String debitAssistantJSON) throws Exception {
		CustomerAccountItem record = new CustomerAccountItem();
		record.setId(id);
		record.setDebitAmount(debitAmount);
		record.setDebitAssistant(debitAssistantJSON);
		int rows = customerAccountItemMapper.updateByPrimaryKeySelective(record);
		return rows;
	}
	
	/**
	 * @Title: updateRechargeBillCreditInfo
	 * @Description: 更新充值记录账单的贷方信息
	 * @param id
	 * @param creditAmount
	 * @param creditAssistantJSON
	 * @return
	 * @throws Exception 
	 */
	private int updateRechargeBillCreditInfo(Long id, BigDecimal creditAmount, String creditAssistantJSON) throws Exception {
		CustomerAccountItem record = new CustomerAccountItem();
		record.setId(id);
		record.setCreditAmount(creditAmount);
		record.setCreditAssistant(creditAssistantJSON);
		int rows = customerAccountItemMapper.updateByPrimaryKeySelective(record);
		return rows;
	}
	
	/**
	 * @Title: getOverdueBill
	 * @Description: 查询某水费账单的违约金账单
	 * @param accountItemId
	 * @return 
	 */
//	private List<CustomerAccountItem> getOverdueBill(Long accountItemId) throws Exception {
//		
//		//贷方科目
//		String creditSubjectOverdue = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
//		
//		CustomerAccountItem ai = new CustomerAccountItem();
//		ai.setPid(accountItemId);
//		ai.setCreditSubject(creditSubjectOverdue);
//		return customerAccountItemMapper.select(ai);
//	}
	
	@Override
	public List<Map<String, Object>> searchCcbCustomerAccountItem(String searchCond, String traceIds, String period,
			Integer deductMode, String startDate, String endDate) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.searchCcbCustomerAccountItem(searchCond, traceIds, period, deductMode, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), startDate, endDate);
	}
	
	@Override
	public List<Map<String, Object>> getListGroupByCustomerId(String searchCond, String traceIds, String period,
			Integer deductMode, String startDate, String endDate, List<Integer> settleStatusList) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.getListGroupByCustomerId(searchCond, traceIds, period, deductMode, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), startDate, endDate, settleStatusList);
	}

	@Override
	public List<Map<String, Object>> getCustomerBalanceList(String searchCond, String traceIds) {
		
		return customerAccountItemMapper.getCustomerBalanceList(searchCond, traceIds, EnumAiDebitCreditStatus.DEBIT.getKey());
	}

	/**
	 * @Title: getCcbWithholdBill
	 * @Description: 获取中国建设银行批量代扣账单
	 * @param customerId
	 * @param period
	 * 			定值条件：结算状态!=1；结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
	 * 			定值条件：扣费方式=2；扣费方式，1=其他/2=建行自动扣费/3=民生银行自动扣费
	 * 			定值条件：欠费账单；贷方金额!=借方金额，且贷方金额>借方金额
	 * @return 
	 * 		返回查询结果账目列表
	 */
	@Override
	public List<CustomerAccountItem> getCcbWithholdBill(Long customerId, String period) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.getCcbWithholdBill(customerId, period, creditSubjectList);
	}
	
	/**
	 * @Title: getCmbcWithholdBill
	 * @Description: 获取中国民生银行批量代扣账单
	 * @param customerId
	 * @param period
	 * 			定值条件：结算状态!=1；结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
	 * 			定值条件：扣费方式=3；扣费方式，1=其他/2=建行自动扣费/3=民生银行自动扣费
	 * 			定值条件：欠费账单；贷方金额!=借方金额，且贷方金额>借方金额
	 * @return 
	 * 		返回查询结果账目列表
	 */
	@Override
	public List<CustomerAccountItem> getCmbcWithholdBill(Long customerId, String period) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getCmbcWithholdBill(customerId, period, creditSubjectList);
	}
	
	@Override
	public List<Map<String, Object>> searchCustomerArrearsAccountItem(String period, String traceIds, Integer settlementStatus, String searchCond, Long operatorId, String startDate, String endDate) {
		
		List<String> creditSubjectList = new ArrayList<>();
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.searchCustomerArrearsAccountItem(period, traceIds,settlementStatus, searchCond, operatorId, creditSubjectList, startDate, endDate);
	}

	/** 
	 * @Title: clearBill
	 * @Description: 后续完善自动销账功能
	 * @param period
	 * @param traceIds
	 * @param accountItemIdList
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#clearBill(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	@Transactional
	public int clearBill(String period, String traceIds, List<Long> accountItemIdList) {
		if(accountItemIdList!=null && accountItemIdList.size()>0) {//按用户选择的账单ID清欠账单
			
		}else if(StringUtils.isNotBlank(period) && StringUtils.isNotBlank(traceIds)) {//按期间和traceIds清欠账单
			
		}else if(StringUtils.isBlank(period) && StringUtils.isNotBlank(traceIds)) {//按traceIds清欠账单
			
		}else if(StringUtils.isNotBlank(period) && StringUtils.isBlank(traceIds)) {//按期间清欠账单
			
		}else {//清欠所有账单
			
		}
		return 0;
	}

	@Override
	public List<CustomerAccountItem> getCustomerAccountItemList(String period, Long customerId,
			Integer settlementStatus) {
		
		List<String> creditSubjectList = new ArrayList<>();
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.getCustomerAccountItemList(period, customerId, settlementStatus, creditSubjectList);
	}
	
	@Override
	public List<Map<String, Object>> searchAutoCustomerAccountItem(String period, String traceIds,
			Integer settlementStatus, String searchCond, Long operatorId, String startDate, String endDate) {
		
		List<String> creditSubjectList = new ArrayList<>();
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.searchAutoCustomerAccountItem(period, traceIds,settlementStatus, searchCond, operatorId, creditSubjectList, startDate, endDate);
	}

	@Override
	public int autoSettleBill(List<Long> billIdList) {
		if(billIdList.size() <= 0) {//判断是否为空
			return -2;
		}
		try {
			
			//登录用户
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型0
			
			int rows = -1;
			for(Long billId : billIdList) {
				CustomerAccountItem waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(billId);
				
				if(waterFeeBill.getAccountStatus() == EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue()) {//判断账单是否结算成功
					continue;
				}
				
				BigDecimal oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());//欠费金额
				if(BigDecimalUtils.greaterThan(oweAmount, zero)) {
					Long customerId = waterFeeBill.getCustomerId();
					List<CustomerAccountItem> rechargeBillList = customerAccountItemMapper.getHaveBalanceRechargeBill(customerId, EnumAiDebitCreditStatus.DEBIT.getKey());
					for(CustomerAccountItem rechargeBill : rechargeBillList) {
						//结算
						rows = this.settleBill(waterFeeBill, rechargeBill, EnumAiDebitSubjectAction.ADVANCE_SETTLE_WATER_FEE, EnumAiTraceOperate.AUTO_SETTLEMENT.getKey(), userBean.getId());
						if(rows>0) {
							waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(billId);
							oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());//欠费金额
							
							if(BigDecimalUtils.lessOrEquals(oweAmount, zero)) {
								break;
							}
						}
						
					}
				}
				
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public List<Map<String, Object>> searchSettleAccountItem(String period, String traceIds, Integer settlementStatus,
			Integer accountStatus, String searchCond, Long operatorId, String startDate, String endDate) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		Integer deductType = EnumDeductType.NO_CARD.getValue();//获取无卡预存水费用户的账单 
		return customerAccountItemMapper.searchSettleAccountItem(period, traceIds, settlementStatus, accountStatus, searchCond, operatorId, creditSubjectList, startDate, endDate, deductType);
	}

	@Override
	public List<Map<String, Object>> getExportCustomerBillData(String searchCond, Integer settlementStatus,
			String traceIds, String period, Integer accountStatus, String startDate, String endDate) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getExportCustomerBillData(searchCond, settlementStatus, traceIds, period, accountStatus, startDate, endDate, creditSubjectList);
	}

	@Override
	public List<Long> getCustomerIdList(String searchCond, String traceIds, Integer customerType) {
		return customerAccountItemMapper.getCustomerIdList(searchCond, traceIds, customerType);
	}

	@Override
	public int delete(Long accountItemId) {
		
		BigDecimal zero = new BigDecimal("0");//默认值：0
		final Long zeroL = 0l;//常量0
		
		CustomerAccountItem item = customerAccountItemMapper.selectByPrimaryKey(accountItemId);
		
		if(!item.getPid().equals(zeroL)) {//如果账单PID!=0表示账单为分账的子账单,需要先撤销分账,再删除账单
			int rows = this.saveCancelSubAccount(item.getId());//撤销分账单
			if(rows>0) {
				log.debug("此账单为分账账单,撤销分账成功;原账单ID:"+item.getPid()+",分账账单ID:"+item.getId());
				item = customerAccountItemMapper.selectByPrimaryKey(item.getPid());//查询原账单
			}else {
				return -1;
			}
		}
		
		//BigDecimal creditAmount = item.getCreditAmount();//贷方金额
		BigDecimal debitAmount = item.getDebitAmount();//借方金额
//		if(BigDecimalUtils.greaterThan(debitAmount, zero)) {
//			return 0;
//		}
		//软删除
		item.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		int rows = customerAccountItemMapper.updateByPrimaryKeySelective(item);
		if(rows>0) {
			//更新分水量开账状态为未开账
			PartitionWater pw = partitionWaterService.getPartitionWater(item.getId());
			if(pw!=null) {
				//pw.setAccountItemId(null);
				pw.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
				partitionWaterService.updateByPrimaryKeySelective(pw);
			}
			
		}
		return rows;
	}

	@Override
	public Map<String, Object> getCustomerAccountItemStatistic(String period, String traceIds, Integer settlementStatus,
			Integer accountStatus, String searchCond, Long operatorId, String startDate, String endDate) {
		List<String> creditSubjectList = new ArrayList<>();
		
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getCustomerAccountItemStatistic(period, traceIds, settlementStatus, accountStatus,  searchCond, operatorId, creditSubjectList, startDate, endDate);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getPastOweAmount
	 * @Description: 查询往期欠费金额（水费+违约金+分账单）
	 * @param accountItemId
	 * @param period
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getPastOweAmount(java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public List<CustomerAccountItem> getPastOweAmount(String period, Long customerId) {
		List<String> creditSubjectList = new ArrayList<>();
		
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		//违约金编码
//		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
//		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getPastOweAmount(period, creditSubjectList,customerId);
	}

	@Override
	public List<CustomerAccountItem> getAllCustomerAccountItemByMeter(Long meterId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getAllCustomerAccountItemByMeter(meterId, period, creditSubjectList);
	}

	@Override
	public List<CustomerAccountItem> getPastOweAmountByMeter(String period, Long meterId) {
		List<String> creditSubjectList = new ArrayList<>();
		
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getPastOweAmountByMeter(period, creditSubjectList,meterId);
	}

	@Override
	public List<CustomerAccountItem> getWaterFeeBillList(Long customerId, String period, Integer settlementStatus) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getWaterFeeBillList(customerId, period, settlementStatus, creditSubjectList);
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
		return customerAccountItemMapper.getOweBillList(customerId, period, creditSubjectList);
	}

	@Override
	public List<CustomerAccountItem> getOverdueOweBillList(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getOweBillList(customerId, period, creditSubjectList);
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
		return customerAccountItemMapper.getOweTotalAmount(customerId, period, creditSubjectList);
	}

	@Override
	public BigDecimal getOverdueOweAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getOweTotalAmount(customerId, period, creditSubjectList);
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
		return customerAccountItemMapper.getPastOweBillList(customerId, period, creditSubjectList);
	}

	@Override
	public List<CustomerAccountItem> getPastOverdueOweBillList(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getPastOweBillList(customerId, period, creditSubjectList);
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
		return customerAccountItemMapper.getPastOweTotalAmount(customerId, period, creditSubjectList);
	}

	@Override
	public BigDecimal getPastOverdueOweAmount(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getPastOweTotalAmount(customerId, period, creditSubjectList);
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
		return customerAccountItemMapper.getWaterFeeOwePeriod(customerId, period, creditSubjectList);
	}

	@Override
	public List<String> getOverdueOwePeriod(Long customerId, String period) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//违约金编码
		String overdueCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.OVERDUE_FINE.getKey();
		creditSubjectList.add(overdueCode);
		return customerAccountItemMapper.getOverdueOwePeriod(customerId, period, creditSubjectList);
	}

	/** 
	 * @Title: cancelRechargeBill
	 * @Description: 撤销充值账单
	 * @param accountItemId
	 * @param operatorId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#cancelRechargeBill(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public int cancelRechargeBill(Long rechargeBillId, Long operatorId) {
		BigDecimal zero = new BigDecimal("0");
		
		//查询需要撤销的充值记录
		CustomerAccountItem rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeBillId);
		
		int rows = 0;
		//解析贷方辅助核算
		List<AssistantBean> assistantBeanList = JSON.parseArray(rechargeBill.getCreditAssistant(), AssistantBean.class);
		for(AssistantBean assis : assistantBeanList) {
			Long waterFeeBillId = assis.getId();//水费账单ID
			//查询需要撤销的充值记录中对应的水费账单
			CustomerAccountItem waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(waterFeeBillId);
			BigDecimal creditAmount = waterFeeBill.getCreditAmount();//水费账单的贷方金额
			BigDecimal debitAmount = waterFeeBill.getDebitAmount();//水费账单的借方金额
			//解析借方辅助核算
			List<AssistantBean> waterFeeDebitAssisBeanList = JSON.parseArray(waterFeeBill.getDebitAssistant(), AssistantBean.class);
			List<AssistantBean> newAssistantBeanList = new ArrayList<>();
			for(AssistantBean bean : waterFeeDebitAssisBeanList) {
				Long currRechargeBillId = bean.getId();//充值记录ID
				BigDecimal amount = bean.getAmount();//充值此水费账单的金额
				if(currRechargeBillId.equals(rechargeBillId)) {
					debitAmount = BigDecimalUtils.subtract(debitAmount, amount);
				}else {
					newAssistantBeanList.add(bean);
				}
			}
			
			String newDebitAssisJson = JSON.toJSONString(newAssistantBeanList);//借方辅助核算JSON
			Integer settlementStatus = EnumSettlementStatus.SETTLEMENT_NORMAL.getValue();//未结算
			if(BigDecimalUtils.equals(creditAmount, debitAmount)) {//如果剩余总金额为0，则设置欠费账单为未结算状态
				settlementStatus = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();//全部结算
			} else if(BigDecimalUtils.greaterThan(debitAmount, zero) && BigDecimalUtils.greaterThan(creditAmount, debitAmount)) {//如果借方金额>0且贷方金额>借方金额
				settlementStatus = EnumSettlementStatus.SETTLEMENT_PART.getValue();//部分结算
			}
			//更新水费账单
			CustomerAccountItem updateObj = new CustomerAccountItem();
			updateObj.setId(waterFeeBillId);
			updateObj.setDebitAssistant(newDebitAssisJson);
			updateObj.setDebitAmount(debitAmount);
			updateObj.setSettlementStatus(settlementStatus);
			rows = customerAccountItemMapper.updateByPrimaryKeySelective(updateObj);
			if(rows<=0) {
				break;
			}
		}
		
		if(rows>0) {
			
			//更新充值记录
			CustomerAccountItem updateObj = new CustomerAccountItem();
			updateObj.setId(rechargeBill.getId());
			updateObj.setCreditAssistant("");
			updateObj.setCreditAmount(zero);
			rows = customerAccountItemMapper.updateByPrimaryKeySelective(updateObj);
			if(rows>0) {
				
				//更新清欠充值记录的贷方辅助核算为空
				this.updateDebitBillCreditAssistantIsNull(rechargeBillId);
				
				//增加日志
				CustomerAccountItemLog log = new CustomerAccountItemLog();
				log.setCustomerId(rechargeBill.getCustomerId());
				log.setAccountItemId(rechargeBill.getId());
				log.setAmount(rechargeBill.getDebitAmount());
				log.setType(EnumCustomerAccountItemLogType.CANCEL_RECHARGE.getValue());
				log.setOperatorId(operatorId);
				log.setOperationTime(new Date());
				log.setRemark("撤销账单金额为："+rechargeBill.getDebitAmount());
				customerAccountItemLogService.insertSelective(log);
			}
			
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
//	private String getWaterFeeBillDebitAssistant(BigDecimal debitAmount, String debitAssistant, Long rechargeBillId) {
//		//解析借方辅助核算
//				List<AssistantBean> assistantBeanList = JSON.parseArray(debitAssistant, AssistantBean.class);
//				List<AssistantBean> newAssistantBeanList = new ArrayList<>();
//				for(AssistantBean bean : assistantBeanList) {
//					Long waterFeeBillId = bean.getId();//水费账单ID
//					BigDecimal amount = bean.getAmount();//金额
//					if(waterFeeBillId.equals(rechargeBillId)) {
//						debitAmount = BigDecimalUtils.subtract(debitAmount, amount);
//					}else {
//						newAssistantBeanList.add(bean);
//					}
//				}
//	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteRecharge
	 * @Description: 删除充值
	 * @param accountItemId
	 * @param arrearsAccountItemId
	 * @param operatorId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#deleteRecharge(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public int deleteRecharge(Long accountItemId, Long arrearsAccountItemId, Long operatorId) {
		BigDecimal zero = new BigDecimal("0");
		//获取欠费账单
		CustomerAccountItem arrearsAccount = this.selectByPrimaryKey(arrearsAccountItemId);
		BigDecimal amount = new BigDecimal("0");
		//解析借方辅助核算
		List<AssistantBean> assistantBeanList = JSON.parseArray(arrearsAccount.getDebitAssistant(), AssistantBean.class);
		for(AssistantBean assis : assistantBeanList) {
			if(assis.getId().equals(accountItemId)) {//查找该充值账单
				amount = assis.getAmount();
				assistantBeanList.remove(assis); 
				break;
			}
		}
		
		//获取借方辅助核算剩余总金额
		BigDecimal surplusAmount = this.getRechargeAmount(assistantBeanList);
		if(BigDecimalUtils.equals(surplusAmount, zero)) {//如果剩余总金额为0，则设置欠费账单为未结算状态
			arrearsAccount.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_NORMAL.getValue());
		} else {//设置欠费账单为部分结算
			arrearsAccount.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_PART.getValue());
		}
	
		//修改欠费账单的借方金额
		arrearsAccount.setDebitAmount(BigDecimalUtils.subtract(arrearsAccount.getDebitAmount(), amount));
		arrearsAccount.setDebitAssistant(JSON.toJSONString(assistantBeanList));
		int rows = this.updateByPrimaryKeySelective(arrearsAccount);
		if(rows >0) {
			//获取充值账单
			CustomerAccountItem rechargeAccount = this.selectByPrimaryKey(accountItemId);
			rechargeAccount.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			this.updateByPrimaryKeySelective(rechargeAccount);
			
			//增加日志
			CustomerAccountItemLog log = new CustomerAccountItemLog();
			log.setCustomerId(rechargeAccount.getCustomerId());
			log.setAccountItemId(arrearsAccountItemId);
			log.setAmount(amount);
			log.setType(EnumCustomerAccountItemLogType.DELETE_RECHARGE.getValue());
			log.setOperatorId(operatorId);
			log.setOperationTime(new Date());
			log.setRemark("删除充值账单");
			rows = customerAccountItemLogService.insertSelective(log);
		}
		return rows;
	}
	
	/**
	 * @Title: getRechargeAmount
	 * @Description: 获取解放辅助核算的总充值金额
	 * @param assistantBeanList
	 * @return 
	 */
	public BigDecimal getRechargeAmount(List<AssistantBean> assistantBeanList) {
		BigDecimal totalRecharge = new BigDecimal("0");
		for(AssistantBean assis : assistantBeanList) {
			totalRecharge = BigDecimalUtils.add(totalRecharge, assis.getAmount());
		}
		return totalRecharge;
	}

	@Override
	public List<CustomerAccountItem> getAllRechargeBill(Long customerId, String startDate, String endDate) {
		String debitCredit = EnumAiDebitCreditStatus.DEBIT.getKey();//借/贷状态为借
		return customerAccountItemMapper.getAllRechargeBill(customerId, startDate, endDate, debitCredit);
	}
	
	@Override
	public List<CustomerAccountItem> getBalanceRechargeBill(Long customerId, String startDate, String endDate) {
		String debitCredit = EnumAiDebitCreditStatus.DEBIT.getKey();//借/贷状态为借
		return customerAccountItemMapper.getBalanceRechargeBill(customerId, startDate, endDate, debitCredit);
	}

	@Override
	public List<CustomerAccountItem> getTaxInvoiceBillList(String periodStart, String periodEnd, Long customerId, Integer settlementStatus, Boolean searchWaterFeeBill, Boolean searchStoredBill) {
		
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		if(searchWaterFeeBill) {
			//水费编码
			String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
			creditSubjectList.add(waterFeeCode);
			//分账编码
			String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
			creditSubjectList.add(subAccountCode);
		}
		String debitSubject = null;
		if(searchStoredBill) {
			//预存
			String advanceWaterFee = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.ADVANCE_WATER_FEE.getKey();
			debitSubject = advanceWaterFee;
		}
		
		return customerAccountItemMapper.getTaxInvoiceBillList(periodStart, periodEnd, customerId, settlementStatus, creditSubjectList, debitSubject);
	}

	@Override
	public List<CustomerAccountItem> getSubAccountItem(Long customerAccountItemId) {
		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("pid", customerAccountItemId);
		return customerAccountItemMapper.selectByExample(example);
	}
	
	/** 
	 * 
	 * @Title: getBaseFeeOweAmount
	 * @Description: 获取账单基础水费欠费金额
	 * @param assistant
	 * @param baseWaterFee
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getBaseFeeOweAmount(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public BigDecimal getBaseFeeOweAmount(String assistant, BigDecimal baseWaterFee) {
		
		try {
			
			log.debug("获取基础水费欠费金额，借方辅助核算："+assistant);
			
			//基础水价科目
			String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_BASE_FEE.getKey();
			
			BigDecimal payAmount = new BigDecimal(0.00);//基础水价已支付金额变量
			
			//assistant
			if(StringUtils.isNotBlank(assistant)) {
				List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);
				
				for(AssistantBean assistantBean : assistantBeanList) {
					String subject = assistantBean.getSubject();
					if(StringUtils.isNotBlank(subject)) {
						if(subject.indexOf(subjectBase)!=-1) {
							BigDecimal amount = assistantBean.getAmount();
							payAmount = BigDecimalUtils.add(payAmount, amount); 
						}
					}
					
				}
			}
			//计算基础水费欠费金额
			BigDecimal basePriceOweAmount = BigDecimalUtils.subtract(baseWaterFee, payAmount);
			return basePriceOweAmount;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseWaterFee;
	}
	
	/** 
	 * 
	 * @Title: getSewageFeeOweAmount
	 * @Description: 获取账单污水处理费欠费金额
	 * @param assistant
	 * @param sewageWaterFee
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getSewageFeeOweAmount(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public BigDecimal getSewageFeeOweAmount(String assistant, BigDecimal sewageWaterFee) {
		
		try {
			
			log.debug("获取基础水费欠费金额，借方辅助核算："+assistant);
			
			//污水处理费科目
			String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_TREATMENT_FEE.getKey();
			
			BigDecimal payAmount = new BigDecimal(0.00);//污水处理费已支付金额变量
			
			//assistant
			if(StringUtils.isNotBlank(assistant)) {
				
				List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);
				
				for(AssistantBean assistantBean : assistantBeanList) {
					String subject = assistantBean.getSubject();
					if(StringUtils.isNotBlank(subject)) {
						if(subject.indexOf(subjectBase)!=-1) {
							BigDecimal amount = assistantBean.getAmount();
							payAmount = BigDecimalUtils.add(payAmount, amount); 
						}
					}
					
				}
			}
			//计算污水处理费欠费金额
			BigDecimal basePriceOweAmount = BigDecimalUtils.subtract(sewageWaterFee, payAmount);
			return basePriceOweAmount;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sewageWaterFee;
	}
	
	@Override
	public List<CustomerAccountItem> getRechargeBillList(String period, String subjectPayment) {
		return customerAccountItemMapper.getRechargeBillList(period, subjectPayment);
	}
	
	@Override
	public List<CustomerAccountItem> getPastRechargeBillList(String period, String subjectPayment) {
		return customerAccountItemMapper.getPastRechargeBillList(period, subjectPayment);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: sendSmsNotice
	 * @Description: 批量发送短信通知
	 * @param customerId
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#sendSmsNotice(java.lang.Long, java.lang.String)
	 */
	@Override
	public Map<String, Object> sendSmsNoticeBatch(List<Map<String, Object>> accountItemMapList) {
		int successNum = 0;
		int failNum = 0;
		Long operatorId = this.getOperatorId();
		try {
			for (Map<String, Object> accountItemMap : accountItemMapList) {
				String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
				Long customerId = Long.valueOf(customerIdStr);// 客户ID
				String period = accountItemMap.get("PERIOD").toString();// 期间
				
				BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型 0
				BigDecimal oweAmount = this.getWaterFeeOweAmount(customerId, period);//获取本期欠费金额
				BigDecimal pastOweAmount = this.getPastWaterFeeOweAmount(customerId, period);//获取往期欠费金额
				
				BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);

				// 如果欠费金额>0，则发送短信通知
				if (BigDecimalUtils.greaterThan(totalOweAmount, zero)) {
					// 发送短信通知
					// 查询客户信息
					Customers customer = customersService.selectByPrimaryKey(customerId);

					// 客户手机号
					String mobileNo = customer.getPropMobile();
					if (StringUtils.isBlank(mobileNo)) {
						mobileNo = customer.getCustomerMobile();
					}
					// 验证发送短信的手机号码是否为空
					if (StringUtils.isBlank(mobileNo)) {
						//("客户手机号为空！");
						failNum+=1;
						continue;
					}

					// 指定模板所对应的参数值
					List<String> tpl_parms = new ArrayList<>();
					// 客户姓名
					String customerName = customer.getPropName();
					if (StringUtils.isBlank(customerName)) {
						customerName = customer.getCustomerName();
					}
					if (StringUtils.isNotBlank(customerName)) {
						tpl_parms.add(customerName);
					}

					//期间
					if (StringUtils.isNotBlank(period)) {
						tpl_parms.add(period);
					}
					tpl_parms.add(totalOweAmount.toString());//总欠费金额（本期欠费+往期欠费）

					// 如果手机号不为空，且模版参数正确，则发送短信
					if (StringUtils.isNotBlank(mobileNo) && tpl_parms.size() == 3) {
						log.debug("----------发送短信通知的参数："+tpl_parms.toString());
						String retJSON = smsService.sendSingleSMS(mobileNo, SMSConstants.SMS_TEMPLATE_ID_FEE, tpl_parms);
						if (StringUtils.isNotBlank(retJSON)) {
							SendSingleSMResponse ret = JSON.parseObject(retJSON, SendSingleSMResponse.class);
							//添加发送短信日志
							SendSmsLog log = new SendSmsLog();
							log.setCustomerId(customerId);
							log.setCustomerMobile(mobileNo);
							log.setPeriod(period);
							log.setExt(ret.getExt());
							log.setFee(ret.getFee());
							log.setSid(ret.getSid());
							log.setOperationTime(new Date());
							log.setOperatorId(operatorId);
							if (ret.getResult() == 0) {
								//("发送短信通知成功！");
								log.setResult(EnumSendSmsStatus.SUCCESS.getValue());
								//添加发送短信日志
								sendSmsLogService.insertSelective(log);
								successNum+=1;
								continue;
							} else {
								//"发送短信通知失败，原因：" + ret.getErrmsg());
								log.setErrmsg(ret.getErrmsg());
								log.setResult(EnumSendSmsStatus.FAIL.getValue());
								//添加发送短信日志
								sendSmsLogService.insertSelective(log);
								failNum+=1;
								continue;
							}
						} else {
							//"发送短信通知失败，返回信息为空！");
							failNum+=1;
							continue;
						}
					} else {
						//"缺少参数！");
						failNum+=1;
						continue;
					}

				} else {
					//"此账单已全部结算，不需要发送短信通知！");
					failNum+=1;
					continue;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", successNum);
		resultMap.put("fail", failNum);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getStatSettlementBillList(String period, String debitSubject, List<String> traceIdsList) {
		return customerAccountItemMapper.getStatSettlementBillList(period, debitSubject, traceIdsList);
	}

	@Override
	public List<Map<String, Object>> getStatPastSettlementBillList(String period, String debitSubject, List<String> traceIdsList) {
		return customerAccountItemMapper.getStatPastSettlementBillList(period, debitSubject, traceIdsList);
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
		BigDecimal amount = customerAccountItemMapper.getActualWaterFeeAmount(customerId, period, creditSubjectList);
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
		BigDecimal amount = customerAccountItemMapper.getReceiveWaterFeeAmount(customerId, period, creditSubjectList);
		if(amount!=null) {
			receiveAmount = amount; 
		}
		return receiveAmount;
	}
	
	@Override
	public List<Map<String, Object>> getStatOweCompanyMeter(String searchCond, String period,
			List<Integer> settleStatusList) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getStatOweCompanyMeter(searchCond, period, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), settleStatusList);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCustomerAccountItemDetail
	 * @Description:财务统计-获取欠费单位明细
	 * @param period
	 * @param customerId
	 * @param settleStatusList
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getCustomerAccountItemDetail(java.lang.String, java.lang.Long, java.util.List)
	 */
	@Override
	public List<Map<String, Object>> getCustomerAccountItemDetail(String period, Long customerId,
			List<Integer> settleStatusList) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getCustomerAccountItemDetail(period, customerId, settleStatusList, creditSubjectList);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getStatNoTaxBillList
	 * @Description: 查询本期已结算（全部结算或部分结算）的水费账单（无票收入）
	 * @param period
	 * @param settleStatusList
	 * @param traceIdsList
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getStatNoTaxBillList(java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public List<Map<String, Object>> getStatNoTaxBillList(String period, List<Integer> settleStatusList,
			List<String> traceIdsList) {
		return customerAccountItemMapper.getStatNoTaxBillList(period, settleStatusList, traceIdsList);
	}

	/**
	 * @Title: generatorDebtBill
	 * @Description: 生成清欠账单
	 * @param oweBillId
	 * @param debtAmount
	 * @param subjectAction
	 * @param subjectPayment
	 * @return 
	 */
	private int generatorDebtBill(Long rechargeBillId, Long oweBillId, BigDecimal debtAmount, EnumAiDebitSubjectAction subjectAction, EnumAiDebitSubjectPayment subjectPayment) {
		log.debug("----------生成清欠账单----------  ");
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		CustomerAccountItem oweBill = customerAccountItemMapper.selectByPrimaryKey(oweBillId);//欠费账单
		
		Date sysDate = new Date();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		//借方科目：借/贷状态+科目动作+科目支付方式
		String debitSubject = "";
		//借方摘要：科目动作+科目支付方式
		String debitDigest = "";
		if(subjectAction.getKey().equalsIgnoreCase(EnumAiDebitSubjectAction.PAY_WATER_FEE.getKey())) {//交水费（综价）
			debitSubject = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.DEBTS_WATER_FEE.getKey()+subjectPayment.getKey();
			debitDigest = EnumAiDebitSubjectAction.DEBTS_WATER_FEE.getValue()+subjectPayment.getValue();
		}else if(subjectAction.getKey().equalsIgnoreCase(EnumAiDebitSubjectAction.PAY_BASE_FEE.getKey())) {//交水费（基价）
			debitSubject = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.DEBTS_BASE_FEE.getKey()+subjectPayment.getKey();
			debitDigest = EnumAiDebitSubjectAction.DEBTS_BASE_FEE.getValue()+subjectPayment.getValue();
		}else if(subjectAction.getKey().equalsIgnoreCase(EnumAiDebitSubjectAction.PAY_TREATMENT_FEE.getKey())) {//交污水处理费
			debitSubject = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.DEBTS_TREATMENT_FEE.getKey()+subjectPayment.getKey();
			debitDigest = EnumAiDebitSubjectAction.DEBTS_TREATMENT_FEE.getValue()+subjectPayment.getValue();
		}
		
		//原账单中的辅助核算
		List<AssistantBean> assistantBeanList = new ArrayList<>();
		//本次辅助核算
		AssistantBean assistant = new AssistantBean(oweBill.getId(), debtAmount, oweBill.getCreditDigest(), oweBill.getCreditSubject(), sysDate, userBean.getId());
		assistantBeanList.add(assistant);
		//辅助核算信息转JSON字符串
		String assistantJSON = JSON.toJSONString(assistantBeanList);
		
		return this.insertDebtBill(rechargeBillId, oweBill.getCustomerId(), oweBill.getPeriod(), debitSubject, debitDigest, debtAmount, assistantJSON);//增加清欠账单
	}
	
	/**
	 * @Title: insertDebtBill
	 * @Description: 增加清欠账单
	 * @param customerId	客户ID
	 * @param period		期间
	 * @param debitSubject	借方科目
	 * @param debitDigest	借方摘要
	 * @param debtAmount	清欠金额
	 * @param assistantJSON	贷方辅助核算
	 * @return 
	 */
	private int insertDebtBill(Long rechargeBillId, Long customerId, String period, String debitSubject, String debitDigest, BigDecimal debtAmount, String assistantJSON) {
		log.debug("----------增加清欠账单----------  ");
		
		Date sysDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BigDecimal zero = new BigDecimal("0.00");//初始化BigDecimal类型的0
		
		//Customers customer = this.getCustomer(customerId);//客户信息
		CustomerAccount ca = this.getCustomerAccount(customerId);//客户-账户信息
		
		CustomerAccountItem item = new CustomerAccountItem();//账目
		item.setCustomerId(customerId);//客户ID
		item.setAccountId(ca.getId());//账户ID
		
		item.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		item.setDebitSubject(debitSubject);//借方科目
		item.setDebitDigest(debitDigest);//借方摘要
//		item.setDebitAssistant(ca.getId().toString());//借方辅助核算 TODO 后面增加日志后需要再维护此字段
		item.setDebitAmount(debtAmount);//借方金额
		
//		item.setCreditDigest();//贷方摘要
//		item.setCreditSubject();//贷方科目
		item.setCreditAssistant(assistantJSON);//贷方辅助核算
		item.setCreditAmount(debtAmount);//贷方金额
//		
		item.setDebitCredit(EnumAiDebitCreditStatus.DEBIT.getKey());//借/贷;
//		item.setOverdueDate(overdueDate);//违约金计算日期
		
		item.setAccounter(userBean.getId().toString());//记账人
		item.setAccountDate(sysDate);//记账日期
		
//		item.setStartTime();//账单产生时间段的开始时间
//		item.setEndTime();//账单产生时间段的结束时间
		
		item.setPeriod(sdf.format(sysDate));//期间为充值日期的年和月
		
//		item.setRemark(debitDigest);//备注
		
//		item.setPartitionWaterId();
		item.setPid(rechargeBillId);//欠费账单的ID
		
		int rows = customerAccountItemMapper.insertSelective(item);
		if(rows>0) {
		}
		return rows;
	}

	@Override
	@Transactional
	public int wechatSettlement(List<Long> oweBillIdList) {
		log.debug("微信用户结算账单："+oweBillIdList.toString());
		try {
			
			Long operatorId = 1l;//操作员ID，默认系统管理员
			int rows=1;
			
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
			
			List<CustomerAccountItem> oweBillList = this.getWechatOweBillList(oweBillIdList);
			
			Long customerId = 0l;//客户ID
			
			BigDecimal oweTotalAmount = new BigDecimal(0.00);//欠费总金额
			for(CustomerAccountItem oweBill : oweBillList) {
				customerId = oweBill.getCustomerId();
				//计算账单欠费金额
				BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
				oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, billOweAmount);
			}
			
			if(customerId==null || customerId<=0) {//如果客户为空,则直接返回
				log.debug("客户ID为空......");
				return 0;
			}
			
			EnumAiDebitSubjectAction subjectAction = EnumAiDebitSubjectAction.PAY_WATER_FEE;//缴费类型：10=交水费（综价）
			String traceOperate = EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey();//账目日志操作
			
			//生成充值记录账单
			Long rechargeItemId = this.generatorRechargeBill(customerId, oweTotalAmount, subjectAction, EnumAiDebitSubjectPayment.PAYMENT_WECHAT, operatorId);
			CustomerAccountItem rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeItemId);
			rechargeBill.setDebitAmount(oweTotalAmount);
			
			for(CustomerAccountItem waterFeeBill : oweBillList) {
				log.debug("微信用户结算水费账单......");
				//计算账单欠费金额
				BigDecimal billOweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
				if(BigDecimalUtils.greaterThan(billOweAmount, zero)) {//如果欠费金额>0时，结算水费账单
					//结算水费账单
					if(BigDecimalUtils.equals(rechargeBill.getDebitAmount(), billOweAmount)) {//如果充值金额=欠费金额
						rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
					}else if(BigDecimalUtils.greaterThan(rechargeBill.getDebitAmount(), billOweAmount)) {//如果充值金额>欠费金额
						rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
						rechargeBill = customerAccountItemMapper.selectByPrimaryKey(rechargeItemId);//结算完成后查询最新的充值记录信息
					}else {//如果充值金额<欠费金额
						rows=this.settleBill(waterFeeBill, rechargeBill, subjectAction, traceOperate, operatorId);
					}
				}
			}
			
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: getWechatOweBillList
	 * @Description: 获取微信用户的欠费账单
	 * @param oweBillIdList
	 * @return 
	 */
	private List<CustomerAccountItem> getWechatOweBillList(List<Long> oweBillIdList){
		
		List<CustomerAccountItem> oweBillList = new ArrayList<>();
		for(Long oweBillId : oweBillIdList) {
			CustomerAccountItem oweBill = customerAccountItemMapper.selectByPrimaryKey(oweBillId);
			if(oweBill!=null) {
				oweBillList.add(oweBill);
			}
		}
		return oweBillList;
	}

	@Override
	public Long wechatPrepayment(Long customerId, BigDecimal amount) {
		
		try {
			EnumAiDebitSubjectAction subjectAction = EnumAiDebitSubjectAction.ADVANCE_WATER_FEE;//51=预存水费
			EnumAiDebitSubjectPayment subjectPayment = EnumAiDebitSubjectPayment.PAYMENT_WECHAT;//020101=微信支付
			Long operatorId = customerId;//操作员ID
			
			//生成充值记录账单
			Long rechargeItemId = this.generatorRechargeBill(customerId, amount, subjectAction, subjectPayment, operatorId);
			return rechargeItemId;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("微信客户预存异常。。。客户ID："+customerId+"，金额："+amount);
		}
		return null;
	}
	
	@Override
	public int cleanRecharge(Long arrearsAccountItemId, Long operatorId) {
		int rows = 0;
		BigDecimal zero = new BigDecimal("0");
		//获取欠费账单
		CustomerAccountItem arrearsAccount = this.selectByPrimaryKey(arrearsAccountItemId);
		BigDecimal amount = new BigDecimal("0");
		//解析借方辅助核算
		if(StringUtils.isNotBlank(arrearsAccount.getDebitAssistant())) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(arrearsAccount.getDebitAssistant(), AssistantBean.class);
			for(AssistantBean assis : assistantBeanList) {
				CustomerAccountItem rechargeAccount = this.selectByPrimaryKey(assis.getId());
				rechargeAccount.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				this.updateByPrimaryKeySelective(rechargeAccount);
			}
			
			arrearsAccount.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_NORMAL.getValue());
			
			//修改欠费账单的借方金额
			arrearsAccount.setDebitAmount(zero);
			arrearsAccount.setDebitAssistant("");
			rows = this.updateByPrimaryKeySelective(arrearsAccount);
		}
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCustomerBalanceStatusList
	 * @Description: 查询客户余额
	 * @param searchCond
	 * @param traceIds
	 * @param balanceStatus
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#getCustomerBalanceStatusList(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> getCustomerBalanceStatusList(String searchCond, String traceIds,
			Integer balanceStatus) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		return customerAccountItemMapper.getCustomerBalanceStatusList(searchCond, traceIds, balanceStatus, creditSubjectList, EnumAiDebitCreditStatus.DEBIT.getKey());
	}
	@Override
	public List<Map<String, Object>> getWechatCustomerOweBillList(String searchCond, String traceIds, String period,
			Integer deductMode, String startDate, String endDate, List<Integer> settleStatusList) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.getWechatCustomerOweBillList(searchCond, traceIds, period, deductMode, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), startDate, endDate, settleStatusList);
	}

	@Override
	public List<Map<String, Object>> getNotWechatCustomerOweBillList(String searchCond, String traceIds, String period,
			Integer deductMode, String startDate, String endDate, List<Integer> settleStatusList) {
		List<String> creditSubjectList = new ArrayList<>();//贷方科目
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		creditSubjectList.add(waterFeeCode);
		//分账编码
		String subAccountCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.SUB_ACCOUNT.getKey();
		creditSubjectList.add(subAccountCode);
		
		return customerAccountItemMapper.getNotWechatCustomerOweBillList(searchCond, traceIds, period, deductMode, creditSubjectList, EnumAiDebitCreditStatus.CREDIT.getKey(), startDate, endDate, settleStatusList);
	}

	@Override
	public List<CustomerAccountItem> getDebitBillList(Long rechargeBillId) {
		return customerAccountItemMapper.getDebitBillList(rechargeBillId);
	}

	@Override
	public void updateDebitBillCreditAssistantIsNull(Long rechargeBillId) {
		customerAccountItemMapper.updateDebitBillCreditAssistantIsNull(rechargeBillId);
	}

	@Override
	public void deleteDebitBill(Long rechargeBillId) {
		customerAccountItemMapper.deleteDebitBill(rechargeBillId);
	}
	
	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;// 操作员ID
		if (userBean != null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for (SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}

			if (roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_METER_READER) != -1) {
				operatorId = userBean.getId();// 操作员ID
			}

		}
		return operatorId;
	}

	//--------------------------------智慧水务平台分水量业务处理部分------------------------------------------------------------------------------------------------------------------------
	/**
	 * @Title: autoGeneratorWaterFeeBill
	 * @Description: 自动生成水费账单
	 * @param pw
	 * @return 
	 */
	@Override
	public Long autoGeneratorWaterFeeBill(PartitionWater pw) {
		
		Long customerId = pw.getCustomerId();//客户ID
		CustomerAccount ca = this.getCustomerAccount(customerId);//获取客户账户信息
		
		Long operatorId = 0l;//操作员ID
		
		CustomerAccountItem item = billBusiness.getWaterFeeBill(pw, ca.getId(), operatorId);
		int rows = customerAccountItemMapper.insertSelective(item);
		if(rows>0) {
			Long billId = item.getId();//水费账单ID
			//更新状态 更新分水量开账状态，并设置账目ID; 更新水价日志表中的账目ID; 更新抄表记录的开账状态；
			this.updateStatus(pw.getId(), item.getId(), pw.getRecordId());
			//增加水费账单日志
			accountItemTraceService.insert(item.getId(), item.getCreditSubject(), operatorId.toString(), pw.getWaterFee().setScale(2));
			//修改分水量表的账目ID
			partitionWaterService.updateAccountItemId(pw.getId(), billId);
			//修改水价日志表的账目ID
			useWaterPriceTraceService.updateAccountItemId(pw.getId(), billId);
			//修改抄表记录的生成账单状态
			//this.updateRecordCreateBillStatus(pw.getRecordId());
			return billId;
		}
		return null;
		
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: balanceAutoSettlement
	 * @Description: 余额自动结算水费账单
	 * @param waterFeeBill
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountItemService#balanceAutoSettlement(com.learnbind.ai.model.CustomerAccountItem)
	 */
	@Override
	@Transactional
	public int balanceAutoSettlement(CustomerAccountItem waterFeeBill) {
			
		try {
			
			Long operatorId = 0l;//操作员ID
			int rows=1;
			
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的0
			
			Long customerId = waterFeeBill.getCustomerId();//客户ID
			BigDecimal customerBalance = this.getCustomerBalance(customerId);//客户余额
			if(BigDecimalUtils.lessOrEquals(customerBalance, zero)) {//如果客户余额<=0时,直接返回
				return -1;
			}
			
			//欠费金额
			BigDecimal oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
			if(BigDecimalUtils.lessThan(customerBalance, oweAmount)) {//如果客户余额<欠费金额，直接返回余额不足
				return -2;
			}
			
			//查询有余额的充值账单
			List<CustomerAccountItem> rechargeBillList = this.getHaveBalanceRechargeBill(customerId);
			for(CustomerAccountItem rechargeBill : rechargeBillList) {
				//欠费金额
				oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());
				if(BigDecimalUtils.lessOrEquals(oweAmount, zero)) {//如果欠费金额<=0时，自动执行下一次循环
					break;
				}
				//结算
				rows = this.settleWaterFeeBill(waterFeeBill, rechargeBill, operatorId);
				if(rows>0) {
					waterFeeBill = customerAccountItemMapper.selectByPrimaryKey(waterFeeBill.getId());
				}else {
					break;
				}
			}
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;

	}
	
	/**
	 * @Title: settleWaterFeeBill
	 * @Description: 结算水费账单
	 * @param oweBill		水费欠费账单
	 * @param rechargeBill	充值账单
	 * @param operatorId	操作员ID
	 * @return 
	 */
	private int settleWaterFeeBill(CustomerAccountItem oweBill, CustomerAccountItem rechargeBill, Long operatorId) {
		
		try {
			
			Date sysDate = new Date();//系统时间
			
			//余额自动销账
			String traceOperate = EnumAiTraceOperate.AUTO_SETTLEMENT.getKey();
			
			int rows = 0;
			
			//计算账单欠费金额
			BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
			//计算充值账单余额
			BigDecimal billBalance = BigDecimalUtils.subtract(rechargeBill.getDebitAmount(), rechargeBill.getCreditAmount());
			
			BigDecimal oweDebitAmount = oweBill.getDebitAmount();//欠费账单的借方金额
			BigDecimal rechargeCreditAmount = rechargeBill.getCreditAmount();//充值账单的贷方金额
			
			//需要更新欠费账单中的借方金额
			BigDecimal debitAmount = billBusiness.getOweBillDebitAmount(billBalance, billOweAmount, oweDebitAmount);
			//需要更新充值账单中的贷方金额
			BigDecimal creditAmount = billBusiness.getRechargeBillCreditAmount(billBalance, billOweAmount, rechargeCreditAmount);
			
			//获取水费账单借方辅助核算中金额
			BigDecimal assistantDebitAmount = billBusiness.getOweBillAssistantDebitAmount(billBalance, billOweAmount);
			//获取充值账单贷方辅助核算中金额
			BigDecimal assistantCreditAmount = billBusiness.getRechargeBillAssistantCreditAmount(billBalance, billOweAmount);
			
			//-----------------------------更新欠费账单的借方信息------------------------------
			Long rechargeBillId = rechargeBill.getId();
			String debitSubject = rechargeBill.getDebitSubject();
			String debitDigest = rechargeBill.getDebitDigest();
			
			//欠费账单中原有的借方辅助核算
			String debitAssistant = oweBill.getDebitAssistant();
			//欠费账单的借方辅助核算信息
			String debitAssistantJSON = this.getAssistantJSON(debitAssistant, rechargeBillId, assistantDebitAmount, debitDigest, debitSubject, sysDate, operatorId);
			//更新欠费账单中借方信息
			rows = this.updateOweBillDebitInfo(oweBill.getId(), debitAmount, debitAssistantJSON);
			if(rows>0) {
				accountItemTraceService.insert(oweBill.getId(), oweBill.getCreditSubject(), rechargeBillId, debitSubject, traceOperate, assistantDebitAmount);
			}
			if(rows<=0) {
				//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			//-----------------------------更新充值账单的贷方信息------------------------------
			Long oweBillId = oweBill.getId();
			String creditSubject = oweBill.getCreditSubject();
			//BigDecimal creditAmount = oweBill.getCreditAmount();
			String creditDigest = oweBill.getCreditDigest();
			//充值账单中原有的贷方辅助核算
			String creditAssistant = rechargeBill.getCreditAssistant();
			//充值账单的贷方辅助核算信息
			String creditAssistantJSON = this.getAssistantJSON(creditAssistant, oweBillId, assistantCreditAmount, creditDigest, creditSubject, sysDate, operatorId);
			//更新充值账单中贷方信息
			rows = this.updateRechargeBillCreditInfo(rechargeBill.getId(), creditAmount, creditAssistantJSON);
			if(rows>0) {
				accountItemTraceService.insert(rechargeBill.getId(), rechargeBill.getDebitSubject(), oweBillId, creditSubject, traceOperate, assistantDebitAmount);
			}
			if(rows<=0) {
				//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			
			rows = this.setOweBillSettlementStatus(oweBill.getId());//设置结算状态为成功
			if(rows<=0) {
				//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rows;
			}
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	public static void main(String[] args) {
		String t1 = "南庄新村1-1-101"+"5.23";
		String t2 = "南庄新村10-1-1101"+"105.23";
		
		System.out.println("t1:"+t1.getBytes().length);
		System.out.println("t2:"+t2.getBytes().length);
		
		String t3 = "雍雅锦江一期1-1-101"+"5.23";
		String t4 = "雍雅锦江一期11-1-1101"+"105.23";
		System.out.println("t3:"+t3.getBytes().length);
		System.out.println("t4:"+t4.getBytes().length);
		
		String t5 = "主语城一期11-1-101"+"15.23";
		System.out.println("t5:"+t5.getBytes().length);
		
		String test = "[{\"amount\":84.2,\"date\":1582654002876,\"desc\":\"水费\",\"id\":130699,\"operator\":0,\"subject\":\"201\"},{\"amount\":15.17,\"date\":1582654003037,\"desc\":\"水费\",\"id\":130702,\"operator\":0,\"subject\":\"201\"},{\"amount\":10.46,\"date\":1582654003076,\"desc\":\"水费\",\"id\":130720,\"operator\":0,\"subject\":\"201\"},{\"amount\":5.75,\"date\":1582654003104,\"desc\":\"水费\",\"id\":130703,\"operator\":0,\"subject\":\"201\"},{\"amount\":2.09,\"date\":1582654003268,\"desc\":\"水费\",\"id\":130707,\"operator\":0,\"subject\":\"201\"},{\"amount\":7.85,\"date\":1582654003302,\"desc\":\"水费\",\"id\":130744,\"operator\":0,\"subject\":\"201\"},{\"amount\":10.46,\"date\":1582654003510,\"desc\":\"水费\",\"id\":130705,\"operator\":0,\"subject\":\"201\"},{\"amount\":62.24,\"date\":1582654003589,\"desc\":\"水费\",\"id\":130756,\"operator\":0,\"subject\":\"201\"},{\"amount\":1.57,\"date\":1582654003607,\"desc\":\"水费\",\"id\":130704,\"operator\":0,\"subject\":\"201\"},{\"amount\":50.21,\"date\":1582654003680,\"desc\":\"水费\",\"id\":130740,\"operator\":0,\"subject\":\"201\"},{\"amount\":27.2,\"date\":1582654003724,\"desc\":\"水费\",\"id\":130757,\"operator\":0,\"subject\":\"201\"},{\"amount\":51.25,\"date\":1582654003769,\"desc\":\"水费\",\"id\":130715,\"operator\":0,\"subject\":\"201\"},{\"amount\":38.7,\"date\":1582654003796,\"desc\":\"水费\",\"id\":130716,\"operator\":0,\"subject\":\"201\"},{\"amount\":57.01,\"date\":1582654003850,\"desc\":\"水费\",\"id\":130766,\"operator\":0,\"subject\":\"201\"},{\"amount\":22.49,\"date\":1582654003906,\"desc\":\"水费\",\"id\":130722,\"operator\":0,\"subject\":\"201\"},{\"amount\":10.46,\"date\":1582654004020,\"desc\":\"水费\",\"id\":130804,\"operator\":0,\"subject\":\"201\"},{\"amount\":4.18,\"date\":1582654004069,\"desc\":\"水费\",\"id\":130808,\"operator\":0,\"subject\":\"201\"},{\"amount\":50.21,\"date\":1582654004110,\"desc\":\"水费\",\"id\":130792,\"operator\":0,\"subject\":\"201\"},{\"amount\":50.21,\"date\":1582654004151,\"desc\":\"水费\",\"id\":130775,\"operator\":0,\"subject\":\"201\"},{\"amount\":54.92,\"date\":1582654004282,\"desc\":\"水费\",\"id\":130822,\"operator\":0,\"subject\":\"201\"},{\"amount\":44.46,\"date\":1582654004303,\"desc\":\"水费\",\"id\":130777,\"operator\":0,\"subject\":\"201\"},{\"amount\":42.36,\"date\":1582654004343,\"desc\":\"水费\",\"id\":130717,\"operator\":0,\"subject\":\"201\"},{\"amount\":40.27,\"date\":1582654004470,\"desc\":\"水费\",\"id\":130790,\"operator\":0,\"subject\":\"201\"},{\"amount\":74.27,\"date\":1582654004531,\"desc\":\"水费\",\"id\":130749,\"operator\":0,\"subject\":\"201\"},{\"amount\":16.21,\"date\":1582654004555,\"desc\":\"水费\",\"id\":130779,\"operator\":0,\"subject\":\"201\"},{\"amount\":7.85,\"date\":1582654004634,\"desc\":\"水费\",\"id\":130730,\"operator\":0,\"subject\":\"201\"},{\"amount\":50.73,\"date\":1582654004675,\"desc\":\"水费\",\"id\":130759,\"operator\":0,\"subject\":\"201\"},{\"amount\":69.04,\"date\":1582654004701,\"desc\":\"水费\",\"id\":130811,\"operator\":0,\"subject\":\"201\"},{\"amount\":25.1,\"date\":1582654004740,\"desc\":\"水费\",\"id\":130815,\"operator\":0,\"subject\":\"201\"},{\"amount\":16.21,\"date\":1582654004766,\"desc\":\"水费\",\"id\":130767,\"operator\":0,\"subject\":\"201\"},{\"amount\":1.57,\"date\":1582654004799,\"desc\":\"水费\",\"id\":130818,\"operator\":0,\"subject\":\"201\"},{\"amount\":41.32,\"date\":1582654004848,\"desc\":\"水费\",\"id\":130789,\"operator\":0,\"subject\":\"201\"},{\"amount\":4.18,\"date\":1582654004891,\"desc\":\"水费\",\"id\":130762,\"operator\":0,\"subject\":\"201\"},{\"amount\":12.03,\"date\":1582654004930,\"desc\":\"水费\",\"id\":130776,\"operator\":0,\"subject\":\"201\"},{\"amount\":1.05,\"date\":1582654004958,\"desc\":\"水费\",\"id\":130778,\"operator\":0,\"subject\":\"201\"},{\"amount\":2.62,\"date\":1582654005000,\"desc\":\"水费\",\"id\":130800,\"operator\":0,\"subject\":\"201\"},{\"amount\":0.52,\"date\":1582654005078,\"desc\":\"水费\",\"id\":130842,\"operator\":0,\"subject\":\"201\"},{\"amount\":68.51,\"date\":1582654005115,\"desc\":\"水费\",\"id\":130737,\"operator\":0,\"subject\":\"201\"},{\"amount\":25.63,\"date\":1582654005174,\"desc\":\"水费\",\"id\":130797,\"operator\":0,\"subject\":\"201\"},{\"amount\":46.02,\"date\":1582654005262,\"desc\":\"水费\",\"id\":130760,\"operator\":0,\"subject\":\"201\"},{\"amount\":4.18,\"date\":1582654005398,\"desc\":\"水费\",\"id\":130827,\"operator\":0,\"subject\":\"201\"},{\"amount\":26.15,\"date\":1582654005481,\"desc\":\"水费\",\"id\":130860,\"operator\":0,\"subject\":\"201\"},{\"amount\":18.31,\"date\":1582654005559,\"desc\":\"水费\",\"id\":130854,\"operator\":0,\"subject\":\"201\"},{\"amount\":1.38,\"date\":1582654693301,\"desc\":\"水费\",\"id\":133199,\"operator\":0,\"subject\":\"201\"}]";
		System.out.println(test.length());
	}
	
}