package com.learnbind.ai.service.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.AssistantBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumSubAccountStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.PartitionWater;

@Service
public class BillBusiness {

	
	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(BillBusiness.class);

	
	/**
	 * @Title: getWaterFeeBill
	 * @Description: 获取水费账单
	 * @param pw			分水量
	 * @param accountId		客户账户ID
	 * @param operatorId	操作员ID
	 * @return 
	 */
	public CustomerAccountItem getWaterFeeBill(PartitionWater pw, Long accountId, Long operatorId) {
		
		log.debug("----------获取水费账单----------  分水量:"+pw);
		
		//贷方科目
		String creditSubject = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		//贷方摘要
		//String creditDigest = EnumAiDebitCreditStatus.CREDIT.getValue()+EnumAiCreditSubjectAction.WATER_FEE.getValue();
		String creditDigest = EnumAiCreditSubjectAction.WATER_FEE.getValue();
		
		//登录用户
		//UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BigDecimal zero = new BigDecimal("0.00");//初始化BigDecimal类型的0
		
		Long customerId = pw.getCustomerId();//客户ID
		
		CustomerAccountItem item = new CustomerAccountItem();//账目
		item.setCustomerId(customerId);//客户ID
		item.setAccountId(accountId);//账户ID
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
		
		item.setAccounter(operatorId.toString());//记账人
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
		
		//分账状态-0=不需要分账
		Integer subAccountStatus = EnumSubAccountStatus.NO_NEED_SUB_ACCOUNT.getValue();
		item.setAccountStatus(subAccountStatus);//设置分账状态
		
		log.debug("----------获取水费账单----------  账单:"+item);
		
		return item;
	}
	
	
	
	
	
	/**
	 * @Title: getOweBillDebitAmount
	 * @Description: 获取充值成功后水费账单的借方金额（水费账单的充值总金额）
	 * @param billBalance		充值金额
	 * @param billOweAmount		欠费金额
	 * @param oweDebitAmount	欠费账单已充值金额
	 * @return 
	 */
	public BigDecimal getOweBillDebitAmount(BigDecimal billBalance, BigDecimal billOweAmount, BigDecimal oweDebitAmount) {
		BigDecimal debitAmount = new BigDecimal(0.00);//需要更新欠费账单中的借方金额
		
		if(BigDecimalUtils.equals(billBalance, billOweAmount)) {//如果充值金额=欠费金额
			debitAmount = BigDecimalUtils.add(billBalance, oweDebitAmount);
		}else if(BigDecimalUtils.greaterThan(billBalance, billOweAmount)) {//如果充值金额>欠费金额
			debitAmount = BigDecimalUtils.add(billOweAmount, oweDebitAmount);
		}else {//如果充值金额<欠费金额
			debitAmount = BigDecimalUtils.add(billBalance, oweDebitAmount);
		}
		return debitAmount;
	}
	/**
	 * @Title: getRechargeBillCreditAmount
	 * @Description: 获取充值成功后充值账单的贷方金额（充值账单的消费总金额）
	 * @param billBalance			充值金额
	 * @param billOweAmount			欠费金额
	 * @param rechargeCreditAmount	充值账单的已消费金额
	 * @return 
	 */
	public BigDecimal getRechargeBillCreditAmount(BigDecimal billBalance, BigDecimal billOweAmount, BigDecimal rechargeCreditAmount) {
		BigDecimal creditAmount = new BigDecimal(0.00);//需要更新充值账单中的贷方金额
		if(BigDecimalUtils.equals(billBalance, billOweAmount)) {//如果充值金额=欠费金额
			creditAmount = BigDecimalUtils.add(billOweAmount, rechargeCreditAmount);
		}else if(BigDecimalUtils.greaterThan(billBalance, billOweAmount)) {//如果充值金额>欠费金额
			creditAmount = BigDecimalUtils.add(billOweAmount, rechargeCreditAmount);
		}else {//如果充值金额<欠费金额
			creditAmount = BigDecimalUtils.add(billBalance, rechargeCreditAmount);
		}
		return creditAmount;
	}
	
	/**
	 * @Title: getOweBillAssistantDebitAmount
	 * @Description: 获取水费账单借方辅助核算中金额
	 * @param billBalance		充值金额
	 * @param billOweAmount		欠费金额
	 * @return 
	 */
	public BigDecimal getOweBillAssistantDebitAmount(BigDecimal billBalance, BigDecimal billOweAmount) {
		BigDecimal assistantDebitAmount = new BigDecimal(0.00);//辅助核算的借方金额
		
		if(BigDecimalUtils.equals(billBalance, billOweAmount)) {//如果充值金额=欠费金额
			assistantDebitAmount = billBalance;//辅助核算的借方金额
		}else if(BigDecimalUtils.greaterThan(billBalance, billOweAmount)) {//如果充值金额>欠费金额
			assistantDebitAmount = billOweAmount;//辅助核算的借方金额
		}else {//如果充值金额<欠费金额
			assistantDebitAmount = billBalance;//辅助核算的借方金额
		}
		return assistantDebitAmount;
	}
	
	/**
	 * @Title: getRechargeBillAssistantCreditAmount
	 * @Description: 获取充值账单贷方辅助核算中金额
	 * @param billBalance
	 * @param billOweAmount
	 * @return 
	 */
	public BigDecimal getRechargeBillAssistantCreditAmount(BigDecimal billBalance, BigDecimal billOweAmount) {
		BigDecimal assistantCreditAmount = new BigDecimal(0.00);//辅助核算的贷方金额
		
		if(BigDecimalUtils.equals(billBalance, billOweAmount)) {//如果充值金额=欠费金额
			assistantCreditAmount = billOweAmount;//辅助核算的贷方金额
		}else if(BigDecimalUtils.greaterThan(billBalance, billOweAmount)) {//如果充值金额>欠费金额
			assistantCreditAmount = billOweAmount;//辅助核算的贷方金额
		}else {//如果充值金额<欠费金额
			assistantCreditAmount = billBalance;//辅助核算的贷方金额
		}
		return assistantCreditAmount;
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
	
}
