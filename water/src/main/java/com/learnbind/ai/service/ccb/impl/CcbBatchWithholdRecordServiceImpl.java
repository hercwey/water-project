package com.learnbind.ai.service.ccb.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCcbSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;
import com.learnbind.ai.dao.CcbBatchWithholdRecordMapper;
import com.learnbind.ai.model.CcbBatchCustomerAccount;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.service.ccb.CcbBatchCustomerAccountService;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.ccb.CcbInterfaceService;
import com.learnbind.ai.service.ccb.CcbScheduleTask;
import com.learnbind.ai.service.ccb.CcbWithholdRecordQueue;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb.impl
 *
 * @Title: CcbBatchWithholdRecordServiceImpl.java
 * @Description: 中国建设银行批量代扣记录服务实现类
 *
 * @author Administrator
 * @date 2019年7月14日 下午3:37:31
 * @version V1.0 
 *
 */
@Service
public class CcbBatchWithholdRecordServiceImpl extends AbstractBaseService<CcbBatchWithholdRecord, Long> implements  CcbBatchWithholdRecordService {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbBatchWithholdRecordServiceImpl.class);
	
	@Autowired
	public CcbBatchWithholdRecordMapper ccbBatchWithholdRecordMapper;
	@Autowired
	public CcbBatchCustomerAccountService ccbBatchCustomerAccountService;//代扣文件与账单关系表
	@Autowired
	public BankService bankService;//客户银行信息
	@Autowired
	public CustomersService customersService;//客户信息
	@Autowired
	public CustomerAccountItemService customerAccountItemService;//客户账单信息
	@Autowired
	public CcbInterfaceService ccbInterfaceService;//CCB接口服务
	@Autowired
	private CcbScheduleTask ccbScheduleTask;//ccb定时任务
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CcbBatchWithholdRecordServiceImpl(CcbBatchWithholdRecordMapper mapper) {
		this.ccbBatchWithholdRecordMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public int saveList(List<CcbBatchWithholdRecord> recordList) {
		
		int rows = 0;
		for(CcbBatchWithholdRecord record : recordList) {
			rows = ccbBatchWithholdRecordMapper.insertSelective(record);
		}
		
		return rows;
	}

	@Override
	public List<Map<String, Object>> searchCcbHoldFileItem(String searchCond, String traceIds, String period,
			Integer holdStatus) {
		return ccbBatchWithholdRecordMapper.searchCcbHoldFileItem(searchCond, traceIds, period, holdStatus);
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
		CcbBatchWithholdRecord ccbBatchWithholdRecord = new CcbBatchWithholdRecord();
		ccbBatchWithholdRecord.setPeriod(period);
		ccbBatchWithholdRecord.setTraceId(traceIds);
		ccbBatchWithholdRecord.setRemark(remark);
		ccbBatchWithholdRecord.setPinYinCode(pinYinCode);
		ccbBatchWithholdRecord.setStatus(EnumCcbBatchStatus.GENERATE.getValue());//设置代扣文件状态为已生成
		rows = this.insertSelective(ccbBatchWithholdRecord);
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
			CcbBatchCustomerAccount ccbBatchCustomerAccount = new CcbBatchCustomerAccount();
			//ccbBatchCustomerAccount.setBathWithholdingId(ccbBatchWithholdRecord.getId());//批量代扣文件id
			ccbBatchCustomerAccount.setAccountItemId(Long.valueOf(accountMap.get("ID").toString()));//账单ID
			ccbBatchCustomerAccount.setSettleAccountStatus(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());//设置销账状态为未销账
			ccbBatchCustomerAccount.setWithholdingStatus(EnumCcbWhHoldStatus.WITHHOLDING_NO.getValue());//设置代扣状态为未代扣
			ccbBatchCustomerAccount.setJoinTime(new Date());//加入批量代扣时间
			ccbBatchCustomerAccount.setOperatorId(userBean.getId());//操作员ID
			ccbBatchCustomerAccount.setOperatorName(userBean.getRealname());//操作员名称
			ccbBatchCustomerAccount.setCustomerName(accountMap.get("PROP_NAME").toString());//客户名称
			//查询客户绑定的银行卡
			CustomerBanks customerBank = bankService.getCustomerBank(Long.valueOf(accountMap.get("CUSTOMER_ID").toString()));
			
			ccbBatchCustomerAccount.setCustomerCardNo(customerBank.getCardNo());//获取客户卡号
			//记录账单在代扣文件当中的序号
			ccbBatchCustomerAccount.setWithholdingNo(index);
			
			rows = ccbBatchCustomerAccountService.insertSelective(ccbBatchCustomerAccount);
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
		
		CcbBatchCustomerAccount ccbBatchCustomerAccount = new CcbBatchCustomerAccount();
		//ccbBatchCustomerAccount.setBathWithholdingId(ccbBatchWithholdRecord.getId());//批量代扣文件id
		ccbBatchCustomerAccount.setAccountItemId(accountItem.getId());//账单ID
		ccbBatchCustomerAccount.setSettleAccountStatus(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());//设置销账状态为未销账
		ccbBatchCustomerAccount.setWithholdingStatus(EnumCcbWhHoldStatus.WITHHOLDING_NO.getValue());//设置代扣状态为未代扣
		ccbBatchCustomerAccount.setJoinTime(new Date());//加入批量代扣时间
		ccbBatchCustomerAccount.setOperatorId(userBean.getId());//操作员ID
		ccbBatchCustomerAccount.setOperatorName(userBean.getRealname());//操作员名称
		ccbBatchCustomerAccount.setCustomerName(customer.getPropName());//客户名称
		//查询客户绑定的银行卡
		CustomerBanks customerBank = bankService.getCustomerBank(accountItem.getCustomerId());
		
		ccbBatchCustomerAccount.setCustomerCardNo(customerBank.getCardNo());//获取客户卡号
		//记录账单在代扣文件当中的序号
		ccbBatchCustomerAccount.setWithholdingNo(withHoldNo);
		
		rows = ccbBatchCustomerAccountService.insertSelective(ccbBatchCustomerAccount);
		if(rows<0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return 0;
	}

	@Override
	public List<CcbBatchWithholdRecord> searchCcbHoldFileList(String searchCond, String traceIds, String period,
			Integer holdStatus) {
		return ccbBatchWithholdRecordMapper.searchCcbHoldFileList(searchCond, traceIds, period, holdStatus);
	}

	@Override
	public Map<String,Object> uploadWithholdFileToCcb(CcbBatchWithholdRecord record) {
		return ccbInterfaceService.uploadWithholdFile(record); 
	}

	/** 
	 * 
	 * @Title: uploadWithholdFileToCcb
	 * @Description: 上传批量代扣文件
	 * @param recordList
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService#uploadWithholdFileToCcb(java.util.List)
	 */
	@Override
	public Map<String,Object> uploadWithholdFileToCcb(List<CcbBatchWithholdRecord> recordList) {
		try {
			
			boolean isLogin = ccbInterfaceService.login();//签到
			if(!isLogin) {
				return RequestResultUtil.getResultFail("签到失败！");
			}
			
			int success_num = 0;//上传成功的个数
			int fail_num = 0;//上传失败的个数
			
			for(CcbBatchWithholdRecord record : recordList) {
				Integer status = record.getStatus();//状态：0=已生成；1=已上传；2=已申请处理；3=CCB处理完成；4=已下载回盘；5=已预处理回盘；6=已做销账处理
				logger.debug("当前批量代扣文件ID："+record.getId()+"，当前批量代扣文件状态："+EnumCcbBatchStatus.getName(status));
				if(status!=null && status!=EnumCcbBatchStatus.GENERATE.getValue()) {
					logger.debug("当前批量代扣文件ID："+record.getId()+"，当前批量代扣文件状态不是已生成状态不需要上传！");
					continue;
				}
				Map<String, Object> resMap = ccbInterfaceService.uploadWithholdFile(record);
				if(resMap!=null) {
					String resultCode = resMap.get(RequestResultUtil.RESULT_CODE).toString();
					if(resultCode.equalsIgnoreCase(RequestResultUtil.RESULT_CODE_SUCCESS)) {
						success_num = success_num+1;//上传成功的个数
					}else {
						fail_num = fail_num+1;//上传失败的个数
					}
				}
			}
			return RequestResultUtil.getResultSuccess(success_num+" 个文件上传成功， "+fail_num+" 个文件上传失败！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("上传批量代扣文件异常，请查看相关日志！");
		
	}

	/** 
	 * 
	 * @Title: applyProcessWithholdFile
	 * @Description: 申请处理批量代扣
	 * @param recordList
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService#applyProcessWithholdFile(java.util.List)
	 */
	@Override
	public Map<String, Object> applyProcessWithholdFile(List<CcbBatchWithholdRecord> recordList) {
		
//		----------测试代码开始----------
//		for(int i=0; i<10; i++) {
//			CcbBatchWithholdRecord temp = new CcbBatchWithholdRecord();
//			temp.setFileSn("test-"+i);
//			temp.setStatus(i);
//			CcbWithholdRecordQueue.offer(temp);//把批量代扣文件记录加入到队列，用定时任务的方式去查询凭证状态
//			//TODO 启动定时任务
//			ccbScheduleTask.start(null);//启动定时任务，参数为空时执行默认计划（一分钟执行一次）
//		}
//		return RequestResultUtil.getResultFail("测试申请成功！");
//		----------测试代码结束----------
		
		
		if(recordList==null || recordList.size()<=0) {
			return RequestResultUtil.getResultFail("没有需要申请代扣的文件！");
		}
		
		boolean isLogin = ccbInterfaceService.login();//签到
		if(!isLogin) {
			return RequestResultUtil.getResultFail("签到失败！");
		}
		
		Map<String, Object> retMap = RequestResultUtil.getResultFail("没有需要申请代扣的文件！");
		int applySuccessNum = 0;//记录申请成功的个数
		int applyFailNum = 0;//记录申请失败的个数
		for(CcbBatchWithholdRecord record : recordList) {
			Integer status = record.getStatus();//状态：0=已生成；1=已上传；2=已申请处理；3=CCB处理完成；4=已下载回盘；5=已预处理回盘；6=已做销账处理
			logger.debug("当前批量代扣文件ID："+record.getId()+"，当前批量代扣文件状态："+EnumCcbBatchStatus.getName(status));
			if(status!=null && status!=EnumCcbBatchStatus.UPLOAD.getValue()) {
				logger.debug("当前批量代扣文件ID："+record.getId()+"，当前批量代扣文件状态不是已上传状态不需要申请处理！");
				continue;
			}
			retMap = ccbInterfaceService.applyProcessWithholdFile(record);
			if(retMap!=null) {
				String resultCode = retMap.get(RequestResultUtil.RESULT_CODE).toString();
				if(resultCode.equalsIgnoreCase(RequestResultUtil.RESULT_CODE_SUCCESS)) {
					//查询最新的批量代扣文件记录
					record = ccbBatchWithholdRecordMapper.selectByPrimaryKey(record.getId());
					CcbWithholdRecordQueue.offer(record);//把批量代扣文件记录加入到队列，用定时任务的方式去查询凭证状态
					//启动定时任务
					ccbScheduleTask.start();//启动定时任务，执行默认计划（一分钟执行一次）
					applySuccessNum=applySuccessNum+1;
				}
			}else {
				applyFailNum=applyFailNum+1;
			}
		}
		return RequestResultUtil.getResultSuccess(applySuccessNum+" 个文件申请处理成功，"+applyFailNum+" 个文件申请处理失败！");
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteHoldFile
	 * @Description: 删除批量代扣文件
	 * @param itemId
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService#deleteHoldFile(java.lang.Long)
	 */
	@Override
	@Transactional
	public int deleteHoldFile(Long itemId) {
		int rows = 1;
		//查询代扣文件里的账单
		Example example = new Example(CcbBatchCustomerAccount.class);
		example.createCriteria().andEqualTo("bathWithholdingId", itemId);
		List<CcbBatchCustomerAccount> accountList = ccbBatchCustomerAccountService.selectByExample(example);
		for(CcbBatchCustomerAccount file : accountList) {//判断代扣文件中是否存在代扣成功的账单，若存在则不能删除
			if(file.getWithholdingStatus() != EnumCcbWhHoldStatus.WITHHOLDING_SUCESS.getValue()) {
				rows = ccbBatchCustomerAccountService.deleteByPrimaryKey(file.getId());//删除代扣文件-账单关系表数据
			} else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return -1;
			}
			
		}
		if(rows > 0) {
			rows = this.deleteByPrimaryKey(itemId);
		}
		return rows;
	}

}
