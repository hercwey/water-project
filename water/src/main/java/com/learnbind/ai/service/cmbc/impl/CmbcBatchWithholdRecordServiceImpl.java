package com.learnbind.ai.service.cmbc.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumCmbcBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcWhHoldStatus;
import com.learnbind.ai.dao.CmbcBatchWithholdRecordMapper;
import com.learnbind.ai.model.CmbcBatchCustomerAccount;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.service.cmbc.CmbcBatchCustomerAccountService;
import com.learnbind.ai.service.cmbc.CmbcBatchWithholdRecordService;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.cmbc.impl
 *
 * @Title: CmbcBatchWithholdRecordServiceImpl.java
 * @Description: 中国民生银行批量代扣记录服务实现类
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午4:44:33
 * @version V1.0 
 *
 */
@Service
public class CmbcBatchWithholdRecordServiceImpl extends AbstractBaseService<CmbcBatchWithholdRecord, Long> implements  CmbcBatchWithholdRecordService {
	
	@Autowired
	public CmbcBatchWithholdRecordMapper cmbcBatchWithholdRecordMapper;
	@Autowired
	public CmbcBatchCustomerAccountService cmbcBatchCustomerAccountService;//代扣文件与账单关系表
	@Autowired
	public BankService bankService;//客户银行信息
	@Autowired
	public CustomersService customersService;//客户信息
	@Autowired
	public CustomerAccountItemService customerAccountItemService;//客户账单信息
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CmbcBatchWithholdRecordServiceImpl(CmbcBatchWithholdRecordMapper mapper) {
		this.cmbcBatchWithholdRecordMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public int saveList(List<CmbcBatchWithholdRecord> recordList) {
		
		int rows = 0;
		for(CmbcBatchWithholdRecord record : recordList) {
			rows = cmbcBatchWithholdRecordMapper.insertSelective(record);
		}
		
		return rows;
	}

	@Override
	public List<Map<String, Object>> searchCmbcHoldFileItem(String searchCond, String traceIds, String period,
			Integer holdStatus) {
		return cmbcBatchWithholdRecordMapper.searchCmbcHoldFileItem(searchCond, traceIds, period, holdStatus);
	}
	
	/**
	 * @Title: addWithholdRecord
	 * @Description: 新增一个代扣文件
	 * @param period
	 * @param comment
	 * @param pinYinCode
	 * @param traceIds
	 * @return 
	 */
	public int addWithholdRecord(String period, String remark, String pinYinCode, String traceIds) {
		int rows = 1;
		CmbcBatchWithholdRecord cmbcBatchWithholdRecord = new CmbcBatchWithholdRecord();
		cmbcBatchWithholdRecord.setPeriod(period);
		cmbcBatchWithholdRecord.setTraceId(traceIds);
		cmbcBatchWithholdRecord.setRemark(remark);
		cmbcBatchWithholdRecord.setPinYinCode(pinYinCode);
		cmbcBatchWithholdRecord.setStatus(EnumCmbcBatchStatus.GENERATE.getValue());//设置代扣文件状态为已生成
		rows = this.insertSelective(cmbcBatchWithholdRecord);
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: generateBatchHoldFile
	 * @Description: 生成批量代扣文件
	 * @param accountItemMapList
	 * @param period
	 * @param comment
	 * @param pinYinCode
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService#generateBatchHoldFile(java.util.List, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public int generateBatchHoldFile(List<Map<String, Object>> accountItemMapList, String period, String remark,
			String pinYinCode, String traceIds) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int rows = 1;
		//新增一个代扣文件
		rows = this.addWithholdRecord(period, remark, pinYinCode, traceIds);
		
		//建立代扣文件与账单的绑定关系
		for(Map<String, Object> accountMap : accountItemMapList) {
			int index = 1;
			CmbcBatchCustomerAccount cmbcBatchCustomerAccount = new CmbcBatchCustomerAccount();
			//ccbBatchCustomerAccount.setBathWithholdingId(ccbBatchWithholdRecord.getId());//批量代扣文件id
			cmbcBatchCustomerAccount.setAccountItemId(Long.valueOf(accountMap.get("ID").toString()));//账单ID
			cmbcBatchCustomerAccount.setSettleAccountStatus(EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());//设置销账状态为未销账
			cmbcBatchCustomerAccount.setWithholdingStatus(EnumCmbcWhHoldStatus.WITHHOLDING_NO.getValue());//设置代扣状态为未代扣
			cmbcBatchCustomerAccount.setJoinTime(new Date());//加入批量代扣时间
			cmbcBatchCustomerAccount.setOperatorId(userBean.getId());//操作员ID
			cmbcBatchCustomerAccount.setOperatorName(userBean.getRealname());//操作员名称
			cmbcBatchCustomerAccount.setCustomerName(accountMap.get("PROP_NAME").toString());//客户名称
			//查询客户绑定的银行卡
			CustomerBanks customerBank = bankService.getCustomerBank(Long.valueOf(accountMap.get("CUSTOMER_ID").toString()));
			
			cmbcBatchCustomerAccount.setCustomerCardNo(customerBank.getCardNo());//获取客户卡号
			//记录账单在代扣文件当中的序号
			cmbcBatchCustomerAccount.setWithholdingNo(index);
			
			rows = cmbcBatchCustomerAccountService.insertSelective(cmbcBatchCustomerAccount);
			if(rows<0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			index++;
		}
		
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: singleGenerateBatchHoldFile
	 * @Description: 勾选框选择生成代扣文件
	 * @param customerAccountItemId
	 * @param period
	 * @param comment
	 * @param pinYinCode
	 * @param traceIds
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService#singleGenerateBatchHoldFile(java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public int singleGenerateBatchHoldFile(Long customerAccountItemId, String period, String comment, String pinYinCode,
			String traceIds, Integer withHoldNo) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int rows = 1;
		//新增一个代扣文件
		rows = this.addWithholdRecord(period, comment, pinYinCode, traceIds);
		//根据客户账单id，查询客户账单信息
		CustomerAccountItem accountItem = customerAccountItemService.selectByPrimaryKey(customerAccountItemId);
		Customers customer = customersService.selectByPrimaryKey(accountItem.getCustomerId());
		
		CmbcBatchCustomerAccount cmbcBatchCustomerAccount = new CmbcBatchCustomerAccount();
		//ccbBatchCustomerAccount.setBathWithholdingId(ccbBatchWithholdRecord.getId());//批量代扣文件id
		cmbcBatchCustomerAccount.setAccountItemId(accountItem.getId());//账单ID
		cmbcBatchCustomerAccount.setSettleAccountStatus(EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());//设置销账状态为未销账
		cmbcBatchCustomerAccount.setWithholdingStatus(EnumCmbcWhHoldStatus.WITHHOLDING_NO.getValue());//设置代扣状态为未代扣
		cmbcBatchCustomerAccount.setJoinTime(new Date());//加入批量代扣时间
		cmbcBatchCustomerAccount.setOperatorId(userBean.getId());//操作员ID
		cmbcBatchCustomerAccount.setOperatorName(userBean.getRealname());//操作员名称
		cmbcBatchCustomerAccount.setCustomerName(customer.getPropName());//客户名称
		//查询客户绑定的银行卡
		CustomerBanks customerBank = bankService.getCustomerBank(accountItem.getCustomerId());
		
		cmbcBatchCustomerAccount.setCustomerCardNo(customerBank.getCardNo());//获取客户卡号
		//记录账单在代扣文件当中的序号
		cmbcBatchCustomerAccount.setWithholdingNo(withHoldNo);
		
		rows = cmbcBatchCustomerAccountService.insertSelective(cmbcBatchCustomerAccount);
		if(rows<0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return 0;
	}

	@Override
	public List<CmbcBatchWithholdRecord> searchCmbcHoldFileList(String searchCond, String traceIds, String period,
			Integer holdStatus) {
		return cmbcBatchWithholdRecordMapper.searchCmbcHoldFileList(searchCond, traceIds, period, holdStatus);
	}

}
