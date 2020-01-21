package com.learnbind.ai.service.ccb.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.CcbWithHoldRecordBean;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.common.enumclass.EnumCcbSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumPaymentType;
import com.learnbind.ai.constant.AccountItemConstant;
import com.learnbind.ai.dao.CcbBatchCustomerAccountMapper;
import com.learnbind.ai.model.CcbBatchCustomerAccount;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.service.ccb.CcbBatchCustomerAccountService;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.ccb.CcbInterfaceService;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.ccb.impl
 *
 * @Title: CcbBatchCustomerAccountServiceImpl.java
 * @Description:  CCB代扣文件与客户账单关联表
 *
 * @author Thinkpad
 * @date 2019年8月20日 上午11:28:28
 * @version V1.0 
 *
 */
@Service
public class CcbBatchCustomerAccountServiceImpl extends AbstractBaseService<CcbBatchCustomerAccount, Long> implements  CcbBatchCustomerAccountService {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbInterfaceService.class);
	
	@Autowired
	public CcbBatchCustomerAccountMapper ccbBatchCustomerAccountMapper;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
	@Autowired
	public CcbBatchWithholdRecordService ccbBatchWithholdRecordService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CcbBatchCustomerAccountServiceImpl(CcbBatchCustomerAccountMapper mapper) {
		this.ccbBatchCustomerAccountMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<CcbBatchCustomerAccount> searchAllAccount(String searchCond, Integer settleAccountStatus, String period) {
		return ccbBatchCustomerAccountMapper.searchAllAccount(searchCond, settleAccountStatus, period);
	}
	
	@Override
	public List<CcbBatchCustomerAccount> selectAccountByRecordId(Long withRecordId, String period, Integer settleAccountStatus, String searchCond) {
		return ccbBatchCustomerAccountMapper.selectAccountByRecordId(withRecordId, period, settleAccountStatus, searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: settleAccount
	 * @Description: 销账
	 * @param holdFileId
	 * @param userId
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchCustomerAccountService#settleAccount(java.lang.Long, java.lang.Long)
	 */
	@Override
	public int settleAccount(Long holdFileId, Long userId) {
		int rows = 1;
		//查找代扣文件中已代扣未销账的账单
		Example example = new Example(CcbBatchCustomerAccount.class);
		example.createCriteria().andEqualTo("withholdingStatus", EnumCcbWhHoldStatus.WITHHOLDING_SUCESS.getValue())
			.andEqualTo("settleAccountStatus", EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue()).andEqualTo("bathWithholdingId", holdFileId);
		
		List<CcbBatchCustomerAccount> customerAccountList = this.selectByExample(example);
		rows = this.settleList(customerAccountList, userId);
		
		return rows;
	}
	
	
	public int settleList(List<CcbBatchCustomerAccount> customerAccountList, Long userId) {
		int rows = 1;
		Date sysdate = new Date();
		for(CcbBatchCustomerAccount account : customerAccountList) {
			Long customerAccountItemId = account.getAccountItemId();//账单id
			//调用销账接口进行销账
			rows = customerAccountItemService.settleAccount(customerAccountItemId, userId, account.getAccountAmount(), EnumAiDebitSubjectAction.PAY_WATER_FEE, EnumAiDebitSubjectPayment.PAYMENT_WITHHOLD_CCB, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiDebitSubjectPayment.PAYMENT_WITHHOLD_CCB.getKey());
			
			if(rows>0) {//销账成功后，将 代扣文件中的账单状态改为销账成功
				account.setSettleTime(sysdate);
				account.setSettleAccountStatus(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_SUCESS.getValue());
			} else {//销账失败，修改状态为销账失败
				account.setSettleAccountStatus(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_FAIL.getValue());
			}
			this.updateByPrimaryKeySelective(account);
		}
		
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: advanceWithHold
	 * @Description: 代扣文件预处理
	 * @param holdFileId
	 * @return 
	 * @see com.learnbind.ai.service.ccb.CcbBatchCustomerAccountService#advanceWithHold(java.lang.Long)
	 */
	@Override
	public int advanceWithHold(Long holdFileId) {
		
		int rows = 1;
		List<CcbWithHoldRecordBean> holdRecordList = this.getReturnFileList(holdFileId);
		
		if(holdRecordList!=null && holdRecordList.size()>0) {
			for(CcbWithHoldRecordBean record : holdRecordList) {
				logger.debug("回盘文件内容："+record);
				CcbBatchCustomerAccount searchObj = new CcbBatchCustomerAccount();
				searchObj.setBathWithholdingId(record.getWithRecordId());
				searchObj.setCustomerName(record.getCustomerName());
				searchObj.setCustomerCardNo(record.getCardNo());
				searchObj.setWithholdingNo(record.getWithHoldNo());//代扣文件中的序号 added by srd on 2019/12/02
				searchObj.setWithholdingStatus(EnumCcbWhHoldStatus.WITHHOLDING_NO.getValue());
				searchObj.setSettleAccountStatus(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());
				List<CcbBatchCustomerAccount> recordList = ccbBatchCustomerAccountMapper.select(searchObj);
				for(CcbBatchCustomerAccount ccbBatchCustomerAccount : recordList) {
					//public CcbBatchCustomerAccount getCustomerAccount(@Param("withRecordId") Long withRecordId, @Param("withHoldNo") Integer withHoldNo, @Param("cardNo") String cardNo, @Param("customerName") String customerName, @Param("accountAmount") BigDecimal accountAmount);
					//CcbBatchCustomerAccount ccbBatchCustomerAccount = ccbBatchCustomerAccountMapper.getCustomerAccount(record.getWithRecordId(), record.getWithHoldNo(), record.getCardNo(), record.getCustomerName(), record.getAccountAmount());
					logger.debug("查询批量代扣账目表记录："+ccbBatchCustomerAccount);
					if(ccbBatchCustomerAccount!=null) {
						logger.debug("回盘文件内容-代扣状态（1=成功；2=失败）："+record.getWithHoldStatus());
						if(record.getWithHoldStatus()==1) {//回盘文件显示代扣成功
							ccbBatchCustomerAccount.setWithholdingStatus(EnumCcbWhHoldStatus.WITHHOLDING_SUCESS.getValue());//设置账单为代扣成功
						} else {
							ccbBatchCustomerAccount.setWithholdingStatus(EnumCcbWhHoldStatus.WITHHOLDING_FAIL.getValue());//设置账单为代扣失败
							ccbBatchCustomerAccount.setWithholdFailReason(record.getWithholdFailReason());//代扣失败原因
						}
						rows = this.updateByPrimaryKeySelective(ccbBatchCustomerAccount);
					}
				}
				
			}
		}
		
		return rows;
	}
	
	@Override
	public boolean isAgainDeduct(Long accountItemId) {
		
		boolean isAgainDeduct = true;//是否需要再次扣费
		
		//Example example = new Example(CcbBatchCustomerAccount.class);
		//example.createCriteria().andEqualTo("accountItemId", accountItemId).andNotEqualTo(property, value)
		CcbBatchCustomerAccount searchObj = new CcbBatchCustomerAccount();
		searchObj.setAccountItemId(accountItemId);
		List<CcbBatchCustomerAccount> ccbAccountList = ccbBatchCustomerAccountMapper.select(searchObj);
		if(ccbAccountList!=null && ccbAccountList.size()>0) {
			for(CcbBatchCustomerAccount ccbAccount : ccbAccountList) {
				int withholdingStatus = ccbAccount.getWithholdingStatus();
				if(withholdingStatus!=EnumCcbWhHoldStatus.WITHHOLDING_FAIL.getValue()) {//如果代扣状态!=代扣失败
					isAgainDeduct = false;
					break;
				}
			}
		}
		return isAgainDeduct;
	}

	/**
	 * @Title: getReturnFileList
	 * @Description: 读取回盘文件，处理成List
	 * @param holdFileId
	 * @return 
	 */
	public List<CcbWithHoldRecordBean> getReturnFileList(Long holdFileId){
		
		String lineSeparator = CCBFileUtil.lineSeparator();//回车换行符号
		
		
		//读取代扣文件中的回盘文件
		List<CcbWithHoldRecordBean> holdRecordList = new ArrayList<>();
		//根据代扣文件ID获取自CCB下载的处理后汇总文件保存到本地的文件名
		CcbBatchWithholdRecord ccbBatchWithholdRecord = ccbBatchWithholdRecordService.selectByPrimaryKey(holdFileId);
		
		//TODO 
//		if(ccbBatchWithholdRecord.getStatus()==) {
//			
//		}
		
		String summaryFile = ccbBatchWithholdRecord.getSummaryFile();
		
		//调用CCBFileUtils的自文本文件中读取数据到字符串，读取这个回盘文件
		String returnFileStr = CCBFileUtil.readToString(summaryFile, "UTF-8");
		logger.info("----------回盘文件内容："+returnFileStr);
		
		
		returnFileStr = returnFileStr.trim();
		//将这个字符串分割成数组,空格分割
		String[] returnFileArr = returnFileStr.split(lineSeparator);
		//循环这个数组，获取数组的第0,1,2,3,10,11位数据
		for(String file : returnFileArr) {
			//竖线分割单条数据，需在|前加上两个\\进行转译
			String[] singleFile = file.split("\\|");
			//String[] singleFile = file.split(lineSeparator);
			
			logger.info("----------回盘文件行数据："+singleFile);
			
			String withHoldNo = singleFile[0].toString();//取出第一位，在代扣文件当中的序号
			String customerCardNo = singleFile[1].toString();//取出第二位，客户的卡号
			String customerName = singleFile[2].toString();//取出第三位，客户的名称
			String accountAmount = singleFile[3].toString();//取出第四位。账单金额
//			String withHoldStatus = singleFile[10].toString();//取出第11位，代扣状态（成功）
//			String remark = singleFile[11].toString();//取出第12位，备注（交易成功）
			String withHoldStatus = singleFile[13].toString();//取出第11位，代扣状态（成功）
			String failMsg = singleFile[14].toString();//取出第12位，备注（交易成功）
			//新疆一个bean对象存储数据
			CcbWithHoldRecordBean recordBean = new CcbWithHoldRecordBean();
			
			withHoldNo = this.getWithHoldNo(withHoldNo);
			recordBean.setWithHoldNo(Integer.valueOf(withHoldNo));
			recordBean.setCardNo(customerCardNo);
			recordBean.setCustomerName(customerName);
			recordBean.setAccountAmount(new BigDecimal(accountAmount));
			recordBean.setWithRecordId(holdFileId);
			if(StringUtils.equals(withHoldStatus, AccountItemConstant.RETURN_WITH_HOLD_FILE_STATUS)) {//如果回盘文件返回的是成功
				recordBean.setWithHoldStatus(EnumCcbWhHoldStatus.WITHHOLDING_SUCESS.getValue());
			} else {
				recordBean.setWithHoldStatus(EnumCcbWhHoldStatus.WITHHOLDING_FAIL.getValue());
				recordBean.setWithholdFailReason(failMsg);//代扣失败原因
			}
			//recordBean.setRemark(remark);
			logger.info("----------代扣客户信息及代扣结果："+recordBean);
			//把这个bean对象放到数组当中
			holdRecordList.add(recordBean);
		}
		return holdRecordList;
	}
	
	
	/**
	 * @Title: getRowSn
	 * @Description: 去掉无效字符
	 * @param sn
	 * @return 
	 */
	private String getWithHoldNo(String withHoldNo) {
		String reg2 = "[^\\d]";
		Pattern p=Pattern.compile(reg2);
		withHoldNo = p.matcher(withHoldNo).replaceAll("");
		return withHoldNo;
	}

	@Override
	public List<CcbBatchCustomerAccount> getWithHoldFileDeatil(String searchCond, Long withHoldFileId,
			Integer withHoldStatus, Integer settleAccountStatus) {
		return ccbBatchCustomerAccountMapper.getWithHoldFileDeatil(searchCond,withHoldFileId, withHoldStatus, settleAccountStatus);
	}

	@Override
	public List<Map<String, Object>> selectHoldFileDetail(Long withRecordId, String searchCond,
			Integer withholdingStatus, String withholdFailReason) {
		return ccbBatchCustomerAccountMapper.selectHoldFileDetail(withRecordId, searchCond, withholdingStatus, withholdFailReason);
	}

	@Override
	public List<Map<String, Object>> getAccountStatistic(Long withRecordId, String period, Integer settleAccountStatus,
			String searchCond) {
		return ccbBatchCustomerAccountMapper.getAccountStatistic(withRecordId, period, settleAccountStatus, searchCond);
	}
	

}
