package com.learnbind.ai.service.cmbc.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.CmbcWithHoldRecordBean;
import com.learnbind.ai.cmbc.CMBCAutoDeductReceiptBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcWhHoldStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiTraceOperate;
import com.learnbind.ai.common.enumclass.accountitem.EnumPaymentType;
import com.learnbind.ai.dao.CmbcBatchCustomerAccountMapper;
import com.learnbind.ai.model.CmbcBatchCustomerAccount;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.service.cmbc.CmbcBatchCustomerAccountService;
import com.learnbind.ai.service.cmbc.CmbcBatchWithholdRecordService;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.cmbc.impl
 *
 * @Title: CmbcBatchCustomerAccountServiceImpl.java
 * @Description: CMBC代扣文件与客户账单关联表
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午4:58:27
 * @version V1.0 
 *
 */
@Service
public class CmbcBatchCustomerAccountServiceImpl extends AbstractBaseService<CmbcBatchCustomerAccount, Long> implements  CmbcBatchCustomerAccountService {
	
	@Autowired
	public CmbcBatchCustomerAccountMapper cmbcBatchCustomerAccountMapper;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
	@Autowired
	public CmbcBatchWithholdRecordService cmbcBatchWithholdRecordService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CmbcBatchCustomerAccountServiceImpl(CmbcBatchCustomerAccountMapper mapper) {
		this.cmbcBatchCustomerAccountMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<CmbcBatchCustomerAccount> searchAllAccount(String searchCond, Integer settleAccountStatus, String period) {
		return cmbcBatchCustomerAccountMapper.searchAllAccount(searchCond, settleAccountStatus, period);
	}
	
	@Override
	public List<CmbcBatchCustomerAccount> selectAccountByRecordId(Long withRecordId, String period, String searchCond, Integer settleAccountStatus) {
		return cmbcBatchCustomerAccountMapper.selectAccountByRecordId(withRecordId, period, searchCond, settleAccountStatus);
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
		
		Date sysDate = new Date();
		
		int rows = 1;
		//查找代扣文件中代扣成功未销账的账单
		Example example = new Example(CmbcBatchCustomerAccount.class);
		example.createCriteria().andEqualTo("withholdingStatus", EnumCmbcWhHoldStatus.WITHHOLDING_SUCESS.getValue())
			.andEqualTo("settleAccountStatus", EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue()).andEqualTo("bathWithholdingId", holdFileId);
		
		List<CmbcBatchCustomerAccount> customerAccountList = this.selectByExample(example);
		rows = this.settleList(customerAccountList, userId, sysDate);
		
		this.updateWithholdFailSettleStatus(holdFileId, sysDate);//修改代扣失败的记录销账状态为销账失败
		
		return rows;
	}
	
	private int settleList(List<CmbcBatchCustomerAccount> customerAccountList, Long userId, Date sysDate) {
		int rows = 1;
		for(CmbcBatchCustomerAccount account : customerAccountList) {
			Long customerAccountItemId = account.getAccountItemId();//账单id
			//调用销账接口进行销账
			rows = customerAccountItemService.settleAccount(customerAccountItemId, userId, account.getAccountAmount(), EnumAiDebitSubjectAction.PAY_WATER_FEE, EnumAiDebitSubjectPayment.PAYMENT_WITHHOLD_CMBC, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiTraceOperate.CMBC_SETTLEMENT.getKey());
			
			if(rows>0) {//销账成功后，将 代扣文件中的账单状态改为销账成功
				account.setSettleTime(sysDate);
				account.setSettleAccountStatus(EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_SUCESS.getValue());
			} else {//销账失败，修改状态为销账失败
				account.setSettleAccountStatus(EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_FAIL.getValue());
			}
			this.updateByPrimaryKeySelective(account);
		}
		
		return rows;
	}
	
	/**
	 * @Title: updateWithholdFailSettleStatus
	 * @Description: 修改代扣失败的记录销账状态为销账失败
	 * @param holdFileId
	 * @return 
	 */
	private int updateWithholdFailSettleStatus(Long holdFileId, Date sysDate) {
		
		//条件
		Example example = new Example(CmbcBatchCustomerAccount.class);
		example.createCriteria().andEqualTo("withholdingStatus", EnumCmbcWhHoldStatus.WITHHOLDING_FAIL.getValue())
			.andEqualTo("settleAccountStatus", EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue()).andEqualTo("bathWithholdingId", holdFileId);
		//修改内容
		CmbcBatchCustomerAccount account = new CmbcBatchCustomerAccount();
		account.setSettleAccountStatus(EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_FAIL.getValue());
		account.setSettleTime(sysDate);
		return cmbcBatchCustomerAccountMapper.updateByExampleSelective(account, example);
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
		List<CmbcWithHoldRecordBean> holdRecordList = this.getReturnFileList(holdFileId);
		if(holdRecordList==null || holdRecordList.size()<=0) {
			return -1;
		}
		for(CmbcWithHoldRecordBean record : holdRecordList) {
			List<CmbcBatchCustomerAccount> cmbcBatchCustomerAccountList = cmbcBatchCustomerAccountMapper.getCustomerAccount(record.getWithRecordId(), record.getCardNo(), record.getCustomerName(), null);
			for(CmbcBatchCustomerAccount cmbcBillRecord : cmbcBatchCustomerAccountList) {
				if(cmbcBillRecord!=null) {
					if(record.getWithHoldStatus()==1) {//回盘文件显示代扣成功
						cmbcBillRecord.setWithholdingStatus(EnumCmbcWhHoldStatus.WITHHOLDING_SUCESS.getValue());//设置账单为代扣成功
						cmbcBillRecord.setWithholdFailReason(record.getRemark());
						cmbcBillRecord.setSettleFailReason(record.getRemark());
					} else {
						cmbcBillRecord.setWithholdingStatus(EnumCmbcWhHoldStatus.WITHHOLDING_FAIL.getValue());//设置账单为代扣失败
						cmbcBillRecord.setWithholdFailReason(record.getRemark());
						cmbcBillRecord.setSettleFailReason(record.getRemark());
					}
					rows = this.updateByPrimaryKeySelective(cmbcBillRecord);
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
		CmbcBatchCustomerAccount searchObj = new CmbcBatchCustomerAccount();
		searchObj.setAccountItemId(accountItemId);
		List<CmbcBatchCustomerAccount> cmbcAccountList = cmbcBatchCustomerAccountMapper.select(searchObj);
		if(cmbcAccountList!=null && cmbcAccountList.size()>0) {
			for(CmbcBatchCustomerAccount ccbAccount : cmbcAccountList) {
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
	public List<CmbcWithHoldRecordBean> getReturnFileList(Long holdFileId){
		
		//根据代扣文件ID获取自CCB下载的处理后汇总文件保存到本地的文件名
		CmbcBatchWithholdRecord cmbcBatchWithholdRecord = cmbcBatchWithholdRecordService.selectByPrimaryKey(holdFileId);
		String summaryFile = cmbcBatchWithholdRecord.getSummaryFile();
		if(StringUtils.isBlank(summaryFile)) {
			return null;
		}
		
		List<String> filePathList = JSON.parseArray(summaryFile, String.class);//解析JSON格式的文件路径
		
		//读取代扣文件中的回盘文件
		List<CmbcWithHoldRecordBean> holdRecordList = new ArrayList<>();
		for(String filePath : filePathList) {
			//调用readTxtFile的自文本文件中读取数据到字符串，读取这个回盘文件
			String returnFileStr = this.readTxtFile(filePath);
			if(StringUtils.isBlank(returnFileStr)) {
				continue;
			}
			
			//获取txt文件内容中所需要的内容
			String validContent = this.getValidContent(returnFileStr);
			
			//获取扣费回执单结果
			String[] settleResult = validContent.split("\r\n");
			
			for(String str : settleResult) {
				//如果字符串不为空
				if(StringUtils.isNotBlank(str)) {
					//如果字符串不是以===开始，且字符串不是以===结束
					if(!str.startsWith("=") && !str.endsWith("=")) {
						CmbcWithHoldRecordBean receipt = this.getCMBCAutoDeductReceiptBean(str, holdFileId);
						holdRecordList.add(receipt);
					}
				}
				
			}
		}
		
		return holdRecordList;
	}
	
	/**
	 * @Title: getCMBCAutoDeductReceiptBean
	 * @Description: 获取中国民生银行自动扣费回执单Bean
	 * @param str
	 * @return 
	 */
	private CmbcWithHoldRecordBean getCMBCAutoDeductReceiptBean(String str, Long holdFileId){
		
		str = str.replaceAll(" +", " ");//把多个空格替换成一个空格
		String[] strArr = str.split(" ");//根据空格拆分字符串
		int length = strArr.length;
		
		String cardNo = strArr[0];//卡号
		String accountName = strArr[1];//用户名
		BigDecimal deductionAmount = new BigDecimal(strArr[2]);//扣费金额
		Integer status = null;//扣费成功/失败状态 0=成功；1=失败；
		String errMsg = null;//扣费失败原因
		
		if(length==CMBCAutoDeductReceiptBean.LENGTH_SUCCESS) {
			status = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();//扣费成功/失败状态 0=成功；1=失败；
			errMsg = "交易成功";
		}else {
			status = EnumSettlementStatus.SETTLEMENT_FAIL.getValue();//扣费成功/失败状态 0=成功；1=失败；
			errMsg = strArr[3];
		}
		
		CmbcWithHoldRecordBean receipt = new CmbcWithHoldRecordBean();
		receipt.setCardNo(cardNo);
		receipt.setCustomerName(accountName);
		receipt.setAccountAmount(deductionAmount);
		receipt.setWithHoldStatus(status);
		receipt.setWithRecordId(holdFileId);
		receipt.setRemark(errMsg);
		return receipt;
		
	}
	
	/**
	 * @Title: readTxtFile
	 * @Description: 读取txt文件
	 * @param filePath
	 * @return 
	 */
	private String readTxtFile(String filePath) {
		
        File file = new File(filePath);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			System.out.println("要读取的文件不存在");
			return null;
		}
		
		String encoding = "UTF-8";
		try {
			encoding = this.codeString(file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            String result = new String(filecontent, encoding);
            return result;
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        } 
	}
	/**
	 * @Title: getCode
	 * @Description: 获取编码格式 gb2312,UTF-16,UTF-8,Unicode,UTF-8
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	private String getCode(String path) throws Exception {
		InputStream inputStream = new FileInputStream(path);
		byte[] head = new byte[3];
		inputStream.read(head);
		String code = "GBK"; // gb2312或GBK
		if (head[0] == -1 && head[1] == -2)
			code = "UTF-16";
		else if (head[0] == -2 && head[1] == -1)
			code = "Unicode";
		else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
			code = "UTF-8";
		inputStream.close();
		return code;
	}
	
	/**
	 * @Title: getValiddContent
	 * @Description: 获取txt文件内容中所需要的内容
	 * @param txtContent
	 * @return 
	 */
	private String getValidContent(String txtContent) {
		if(StringUtils.isBlank(txtContent)) {
			return null;
		}
		Pattern pattern = Pattern.compile("=+\r\n([^=]+)=+");
		Matcher matcher = pattern.matcher(txtContent);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){
		    buffer.append(matcher.group());
		    buffer.append("\r\n");              
		}
		return buffer.toString();
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
	public List<Map<String, Object>> selectHoldFileDetail(Long withRecordId, String searchCond,
			Integer withholdingStatus, String withholdFailReason) {
		return cmbcBatchCustomerAccountMapper.selectHoldFileDetail(withRecordId, searchCond, withholdingStatus, withholdFailReason);
	}
	
	/**
	  *  	判断文件的编码格式
	  * @param fileName :file
	  * @return 文件编码格式
	  * @throws Exception
	  */
	public String codeString(File fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}
		System.out.println("------------------------------文件编码："+code);
		IOUtils.closeQuietly(bin);
		return code;
	}
	
}
