package com.learnbind.ai.service.meterrecord.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumOverdueFineStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumPaymentType;
import com.learnbind.ai.dao.CustomerOverdueFineMapper;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.overduefine.impl
 *
 * @Title: OverdueFineServiceImpl.java
 * @Description: 每日违约金比例配置服务实现类
 *
 * @author Administrator
 * @date 2019年5月15日 下午4:28:38
 * @version V1.0 
 *
 */
@Service
public class CustomerOverdueFineServiceImpl extends AbstractBaseService<CustomerOverdueFine, Long> implements  CustomerOverdueFineService {
	
	@Autowired
	public CustomerOverdueFineMapper customerOverdueFineMapper;
	@Autowired
	public DiscountFineTraceService discountFineTraceService;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerOverdueFineServiceImpl(CustomerOverdueFineMapper mapper) {
		this.customerOverdueFineMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: selectObjectByAccountId
	 * @Description: 根据违约金账单id查找违约金记录
	 * @param overdueAccountId
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.CustomerOverdueFineService#selectObjectByAccountId(java.lang.Long)
	 */
	@Override
	public List<CustomerOverdueFine> selectIdByAccountIdList(List<Long> accountIdIdList) {
		return customerOverdueFineMapper.selectIdByAccountIdList(accountIdIdList);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: saveList
	 * @Description: 修改违约金记录数据，添加日志
	 * @param customerId
	 * @param accountItemId
	 * @param dfList
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.CustomerOverdueFineService#saveList(java.lang.Long, java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public int saveList(List<CustomerOverdueFine> dfList) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int rows = 0;
		for (CustomerOverdueFine df : dfList) {
			
			//修改减免违约金记录为已结算
			CustomerOverdueFine customerOverdueFine =this.selectByPrimaryKey(df.getId());
			customerOverdueFine.setOverdueStatus(EnumOverdueFineStatus.OVERDUE_YES.getValue()); 
			rows = this.updateByPrimaryKeySelective(customerOverdueFine) ;
			
			DiscountFineTrace discountFineTrace = new DiscountFineTrace();
			discountFineTrace.setCustomerId(df.getCustomerId());
			discountFineTrace.setAccountItemId(df.getOverdueAccountId());
			discountFineTrace.setOverdueFineId(df.getId());
			discountFineTrace.setDiscountAmount(df.getOverdueFine());
			discountFineTrace.setOperationTime(new Date());
			discountFineTrace.setOperatorId(userBean.getId());
			discountFineTrace.setOperatorName(userBean.getRealname()); 
			rows = discountFineTraceService.insertSelective(discountFineTrace);
			
			if (rows>0) {
				//结算违约金账单
				//customerAccountItemService.settleOverdueAccount(df.getOverdueAccountId(),AccountItemConstant.CREDIT_SUBJECT_OVERDUE_FINE,userBean.getId(),df.getOverdueFine());
				//customerAccountItemService.generatorRechargeBill(df.getCustomerId(), df.getOverdueFine(), EnumAiSubject.DEBIT_REDUCTION_OVERDUE_FINE);
				//EnumAiDebitCreditStatus.DEBIT
				customerAccountItemService.settleAccount(df.getOverdueAccountId(), userBean.getId(), df.getOverdueFine(), EnumAiDebitSubjectAction.DISCOUNT_OVERDUE_FINE, EnumAiDebitSubjectPayment.PAYMENT_DISCOUNT, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiDebitSubjectAction.DISCOUNT_OVERDUE_FINE.getKey());
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

}
