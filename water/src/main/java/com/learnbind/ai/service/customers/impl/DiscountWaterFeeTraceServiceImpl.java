package com.learnbind.ai.service.customers.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumPaymentType;
import com.learnbind.ai.dao.DiscountWaterFeeTraceMapper;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.DiscountWaterFeeTraceService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomersTraceServiceImpl.java
 * @Description: 客户档案维护日志
 *
 * @author Thinkpad
 * @date 2019年7月7日 上午9:41:54
 * @version V1.0 
 *
 */
@Service
public class DiscountWaterFeeTraceServiceImpl extends AbstractBaseService<DiscountWaterFeeTrace, Long> implements  DiscountWaterFeeTraceService {
	
	@Autowired
	public DiscountWaterFeeTraceMapper traceMapper;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public DiscountWaterFeeTraceServiceImpl(DiscountWaterFeeTraceMapper mapper) {
		this.traceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<DiscountWaterFeeTrace> search(String searchCond) {
		return traceMapper.search(searchCond);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: saveList
	 * @Description: 添加水费减免日志
	 * @param customerId
	 * @param accountItemId
	 * @param dfList
	 * @return 
	 * @see com.learnbind.ai.service.customers.DiscountWaterFeeTraceService#saveList(java.lang.Long, java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public int saveList(Long customerId, Long accountItemId, List<DiscountWaterFeeTrace> dfList) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		int rows = 1;
		for (DiscountWaterFeeTrace df : dfList) {

			df.setCustomerId(customerId);
			df.setAccountItemId(accountItemId);

			df.setOperationTime(new Date());
			df.setOperatorId(userBean.getId());
			df.setOperatorName(userBean.getRealname());
			rows = traceMapper.insertSelective(df);
			if (rows > 0) {
				
				//结算水费账单
				//customerAccountItemService.settleOverdueAccount(accountItemId,AccountItemConstant.CREDIT_SUBJECT_WATER_FEE,userBean.getId(),df.getDiscountAmount());
				customerAccountItemService.settleAccount(accountItemId, userBean.getId(), df.getDiscountAmount(), EnumAiDebitSubjectAction.DISCOUNT_WATER_FEE, EnumAiDebitSubjectPayment.PAYMENT_DISCOUNT, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiDebitSubjectAction.DISCOUNT_WATER_FEE.getKey());
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

}
