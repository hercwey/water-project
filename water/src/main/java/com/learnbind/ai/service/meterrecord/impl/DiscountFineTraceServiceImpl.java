package com.learnbind.ai.service.meterrecord.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumOverdueFineStatus;
import com.learnbind.ai.constant.AccountItemConstant;
import com.learnbind.ai.dao.DiscountFineTraceMapper;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.CustomersTrace;
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
public class DiscountFineTraceServiceImpl extends AbstractBaseService<DiscountFineTrace, Long> implements  DiscountFineTraceService {
	
	@Autowired
	public DiscountFineTraceMapper discountFineTraceMapper;
	@Autowired
	public CustomerOverdueFineService overdueService;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public DiscountFineTraceServiceImpl(DiscountFineTraceMapper mapper) {
		this.discountFineTraceMapper=mapper;
		this.setMapper(mapper);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: saveList
	 * @Description: 保存减免违约金
	 * @param customerId
	 * @param accountItemId
	 * @param dfList
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.DiscountFineTraceService#saveList(java.lang.Long, java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public int saveList(Long customerId, Long accountItemId, List<DiscountFineTrace> dfList) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		int rows = 1;
		for (DiscountFineTrace df : dfList) {
			//添加减免违约金日志
			df.setCustomerId(customerId);
			df.setAccountItemId(accountItemId);

			df.setOperationTime(new Date());
			df.setOperatorId(userBean.getId());
			df.setOperatorName(userBean.getRealname());
			rows = discountFineTraceMapper.insertSelective(df);
			if (rows > 0) {
				//修改客户-违约金数据
				CustomerOverdueFine cod = overdueService.selectByPrimaryKey(df.getOverdueFineId());
				cod.setOverdueStatus(EnumOverdueFineStatus.OVERDUE_YES.getValue());
				overdueService.updateByPrimaryKeySelective(cod);
				//生成违约金结算账单
				customerAccountItemService.settleOverdueAccount(accountItemId,AccountItemConstant.CREDIT_SUBJECT_OVERDUE_FINE,userBean.getId(),df.getDiscountAmount());
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	@Override
	public BigDecimal getDiscountAmountSum(Long overdueFineId) {
		return discountFineTraceMapper.getDiscountAmountSum(overdueFineId);
	}
	
	@Override
	public List<DiscountFineTrace> search(String searchCond) {
		return discountFineTraceMapper.search(searchCond);
	}
}
