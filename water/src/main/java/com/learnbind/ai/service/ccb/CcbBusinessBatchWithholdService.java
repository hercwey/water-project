package com.learnbind.ai.service.ccb;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.ccb.CcbUploadFileRowBean;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCcbSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCcbWhHoldStatus;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.ListUtils;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CcbBatchCustomerAccount;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbBusinessBatchWithholdService.java
 * @Description: 中国建设银行业务处理-批量代扣服务
 *
 * @author Administrator
 * @date 2019年8月24日 上午1:22:32
 * @version V1.0 
 *
 */
@Service
public class CcbBusinessBatchWithholdService {

	private static final Logger logger = LoggerFactory.getLogger(CcbBusinessBatchWithholdService.class);
	
	//每个批量代扣文件记录条数不能超过2000条，文件大小不能超过625k
	/**
	 * @Fields MAX_ROWS：每个批量代扣文件中最大条数
	 */
	private static final int MAX_ROWS = 2000;//每个批量代扣文件中最大条数
	/**
	 * @Fields MAX_FILE_SIZE：每个体系结构代扣文件大小的最大值
	 */
	private static final Long MAX_FILE_SIZE = 625l*1024;
	
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//中国建设银行批量代扣记录服务
	@Autowired
	private CcbBatchCustomerAccountService ccbBatchCustomerAccountService;//CCB代扣文件与客户账单关系
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private BankService bankService;//客户银行信息
	
	/**
	 * @Title: getFileData
	 * @Description: 生成批量代扣文件
	 * @param customerIds
	 * @param searchCond
	 * @param traceIds
	 * @param searchPeriod
	 * @param deductMode
	 * @param period
	 * @param comment
	 * @param pinYinCode 
	 */
	@Transactional
	public int generateBatchWithholdRecord(String customerIds, String searchCond, String traceIds, String searchPeriod, Integer deductMode, 
			String period, String remark, String pinYinCode) throws Exception {
		
		int rows = 0;
		
		if(StringUtils.isNotBlank(customerIds)) {//生成代扣文件是勾选客户
			String[] customerIdArr = customerIds.split(",");
			List<String> customerIdList = Arrays.asList(customerIdArr);
			//处理数据生成代扣记录
			rows = this.processDatahGenerateWithholdRecord(period, traceIds, remark, pinYinCode, customerIdList);
			
		} else {//生成代扣文件是没有勾选账单
			if(StringUtils.isNotBlank(traceIds)) {//如果地理位置traceIds==null
				//如果地理位置traceIds不为空时，根据条件查询需要建行代扣，且结算状态!=1的水费账单
				//List<CustomerAccountItem> itemList = customerAccountItemService.getCcbWithholdBill(null, period, searchCond, traceIds);
				List<Map<String, Object>> accountItemMapList = customerAccountItemService.getListGroupByCustomerId(searchCond, traceIds, period, deductMode, null, null,null);
				List<String> customerIdList = new ArrayList<>();
				for(Map<String, Object> item : accountItemMapList) {
					String customerIdStr = item.get("CUSTOMER_ID").toString();//客户ID
					if(StringUtils.isNotBlank(customerIdStr)) {
						customerIdList.add(customerIdStr);
					}
					
				}
				//处理数据生成代扣记录
				rows = this.processDatahGenerateWithholdRecord(period, traceIds, remark, pinYinCode, customerIdList);
				//处理数据生成代扣文件
				//rows = this.processDatahGenerateWithholdRecord(period, traceIds, remark, pinYinCode, itemList);
			}else {//如果地理位置traceIds==null
//				//查询小区
//				List<Location> locationList = locationService.getBlockListByPid(null);
//				boolean isAccountItem = false;//是否有批量处理的账单
//				for(Location location : locationList) {
//					//如果地理位置traceIds不为空时，根据条件查询需要建行代扣，且结算状态!=1的水费账单
//					List<CustomerAccountItem> itemList = customerAccountItemService.getCcbWithholdBill(null, period, searchCond, location.getTraceIds());
//					//处理数据生成代扣文件
//					int rowsx = this.processDatahGenerateWithholdRecord(period, location.getTraceIds(), remark, location.getPycode(), itemList);
//					//批量处理只处理有数据的账单，没有数据的不做处理
//					if(rowsx>0) {//如果返回值>0说明有需要批量处理的账单，设置是否有批量处理账单状态
//						isAccountItem = true;//是否有批量处理的账单
//					}
//				}
//				//rows=1表示有批量代扣的账目，rows=-1表示没有批量代扣的账目
//				if(isAccountItem) {
//					rows = 1;
//				}else {
//					rows = -1;
//				}
			}
		}
		if(rows==0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	/**
	 * @Title: processDatahGenerateWithholdRecord
	 * @Description: 处理数据生成代扣记录
	 * @param period
	 * @param traceIds
	 * @param comment
	 * @param pyCode
	 * @param customerIdArr
	 * @return 
	 */
	@Transactional
	private int processDatahGenerateWithholdRecord(String period, String traceIds, String remark, String pyCode, List<String> customerIdList) throws Exception {
		
		int rows = 1;
		
		boolean isNullList = false;//是否有数据
		
		int number = 1;//记录生成文件的个数
		int customerIdSize = customerIdList.size();//用户选择的客户ID个数
		
		//批量代扣文件内容集合
		List<CcbUploadFileRowBean> fileRowList = new ArrayList<>();
		for(int i=0; i<customerIdSize; i++) {
			String customerId = customerIdList.get(i);
			if(StringUtils.isNotBlank(customerId)) {
				//查询客户本期欠费账单和往期欠费账单 
				//List<CustomerAccountItem> currOweBillList = customerAccountItemService.getWaterFeeOweBillList(Long.valueOf(customerId), period);
				List<CustomerAccountItem> currOweBillList = customerAccountItemService.getCcbWithholdBill(Long.valueOf(customerId), period);
				for(CustomerAccountItem item : currOweBillList) {
					//判断账单是否需要再次扣费
					boolean isAgainDeduct = ccbBatchCustomerAccountService.isAgainDeduct(item.getId());
					if(isAgainDeduct) {//如果需要再次扣费，则加入批量代扣文件
						CcbUploadFileRowBean fileRow = this.getCcbUploadFileRowBean(item.getCustomerId(), item.getId(), item.getCreditAmount(), item.getDebitAmount());
						if(fileRow!=null) {
							fileRowList.add(fileRow);
						}
						//查询该账单的违约金账单 TODO 
//						List<CustomerAccountItem> overdueBillList = customerAccountItemService.getOverdueBillList(Long.valueOf(customerId));
//						for(CustomerAccountItem overdueBill : overdueBillList) {
//							fileRow = this.getCcbUploadFileRowBean(overdueBill.getCustomerId(), overdueBill.getId(), overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
//							if(fileRow!=null) {
//								fileRowList.add(fileRow);
//							}
//						}
					}
				}
//				//查询客户往期欠费账单
//				List<CustomerAccountItem> pastOweBillList = customerAccountItemService.getPastWaterFeeOweBillList(Long.valueOf(customerId), period);
//				for(CustomerAccountItem item : pastOweBillList) {
//					//判断账单是否需要再次扣费
//					boolean isAgainDeduct = ccbBatchCustomerAccountService.isAgainDeduct(item.getId());
//					if(isAgainDeduct) {//如果需要再次扣费，则加入批量代扣文件
//						CcbUploadFileRowBean fileRow = this.getCcbUploadFileRowBean(item.getCustomerId(), item.getId(), item.getCreditAmount(), item.getDebitAmount());
//						if(fileRow!=null) {
//							fileRowList.add(fileRow);
//						}
//						//查询该账单的违约金账单 TODO 
////						List<CustomerAccountItem> overdueBillList = customerAccountItemService.getOverdueBillList(Long.valueOf(customerId));
////						for(CustomerAccountItem overdueBill : overdueBillList) {
////							fileRow = this.getCcbUploadFileRowBean(overdueBill.getCustomerId(), overdueBill.getId(), overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
////							if(fileRow!=null) {
////								fileRowList.add(fileRow);
////							}
////						}
//					}
//				}
			}
			
			if(i==(customerIdSize-1) && (fileRowList==null || fileRowList.size()<=0)) {
				isNullList = true;//是否有数据
				break;
			}
			
			int fileRowListSize = fileRowList.size();//代扣文件内容记录个数
			//判断代扣文件内容记录为2000时，则生成代扣文件
			if(fileRowListSize==MAX_ROWS) {
				//获取CCB批量代扣记录实体类
				CcbBatchWithholdRecord record = this.getCcbBatchWithholdRecord(period, traceIds, remark);
				rows = this.addBatchWithholdFile(pyCode, number, record, fileRowList);
				
				number = number+1;//记录生成文件的个数
				fileRowList = new ArrayList<>();//重新初始化批量代扣文件内容集合
			}else {
				if(i==(customerIdSize-1)) {
					
					//获取CCB批量代扣记录实体类
					CcbBatchWithholdRecord record = this.getCcbBatchWithholdRecord(period, traceIds, remark);
					rows = this.addBatchWithholdFile(pyCode, number, record, fileRowList);
					
					number = number+1;//记录生成文件的个数
					fileRowList = new ArrayList<>();//重新初始化批量代扣文件内容集合
				}
			}
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		if(isNullList) {
			return -1;
		}
		return rows;
	}
	
	
	/**
	 * @Title: processDatahWithholdRecord
	 * @Description: 处理数据生成代扣记录
	 * @param period
	 * @param traceIds
	 * @param remark
	 * @param pyCode
	 * @param itemList
	 * @return 
	 */
//	private int processDatahGenerateWithholdRecord(String period, String traceIds, String remark, String pyCode, List<CustomerAccountItem> itemList) throws Exception {
//		
//		int rows = 1;
//		
//		if(itemList==null || itemList.size()<=0) {
//			return -1;//如果查询结果为空，则提示用户没有需要代扣的账单
//		}
//		
//		boolean isNullList = false;//是否有数据
//		
//		int number = 1;//记录生成文件的个数
//		//批量代扣文件内容集合
//		List<CcbUploadFileRowBean> fileRowList = new ArrayList<>();
//		
//		int itemSize = itemList.size();//集合中账目个数
//		for(int i=0; i<itemSize; i++) {
//			CustomerAccountItem item = itemList.get(i);//账单信息
//			//获取建行代扣文件一行数据Bean
//			CcbUploadFileRowBean fileRow = this.getCcbUploadFileRowBean(item.getCustomerId(), item.getId(), item.getCreditAmount(), item.getDebitAmount());
//			if(fileRow!=null) {
//				fileRowList.add(fileRow);
//			}
//			//查询该账单的违约金账单
//			List<CustomerAccountItem> overdueBillList = customerAccountItemService.getOverdueBillList(item.getId());
//			for(CustomerAccountItem overdueBill : overdueBillList) {
//				fileRow = this.getCcbUploadFileRowBean(overdueBill.getCustomerId(), overdueBill.getId(), overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
//				if(fileRow!=null) {
//					fileRowList.add(fileRow);
//				}
//			}
//			
//			if(i==(itemSize-1) && (fileRowList==null || fileRowList.size()<=0)) {
//				isNullList = true;//是否有数据
//				break;
//			}
//			int fileRowListSize = fileRowList.size();//代扣文件内容记录个数
//			//判断代扣文件内容记录为2000时，则生成代扣文件
//			if(fileRowListSize==MAX_ROWS) {
//				//获取CCB批量代扣记录实体类
//				CcbBatchWithholdRecord record = this.getCcbBatchWithholdRecord(period, traceIds, remark);
//				//增加批量代扣文件记录及批量代扣文件与账目关系
//				rows = this.addBatchWithholdFile(pyCode, number, record, fileRowList);
//				
//				number = number+1;//记录生成文件的个数
//				fileRowList = new ArrayList<>();//重新初始化批量代扣文件内容集合
//			}else {
//				if(i==(itemSize-1)) {
//					//获取CCB批量代扣记录实体类
//					CcbBatchWithholdRecord record = this.getCcbBatchWithholdRecord(period, traceIds, remark);
//					//增加批量代扣文件记录及批量代扣文件与账目关系
//					rows = this.addBatchWithholdFile(pyCode, number, record, fileRowList);
//					
//					number = number+1;//记录生成文件的个数
//					fileRowList = new ArrayList<>();//重新初始化批量代扣文件内容集合
//				}
//			}
//			if(rows==0) {
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//				break;
//			}
//		}
//		if(isNullList) {
//			return -1;
//		}
//		return rows;
//	}
	

	
	/**
	 * @Title: addBatchWithholdFile
	 * @Description: 增加批量代扣文件记录及批量代扣文件与账目关系
	 * @param prefix
	 * @param number
	 * @param record
	 * @param fileRowList
	 * @return 
	 */
	@Transactional
	private int addBatchWithholdFile(String prefix, int number, CcbBatchWithholdRecord record, List<CcbUploadFileRowBean> fileRowList) {
		
		try {
			String fileType = "ccb_withhold_file";//文件类型，用于新建文件夹
			
			//获取批量代扣文件唯一编号
			String sn = this.getBatchWithholdSn(number);
			//获取批量代扣文件名称
			String fileName = this.getBatchWithholdFileName(prefix, number);
			fileName = fileName+".txt";
			//保存数据到文件，并返回保存文件路径
			Map<String, String> resultMap = this.saveDataToFile(fileType, fileName, fileRowList);
			
			String filePath = resultMap.get("filePath");//文件保存路径
			String batchWithFileRowListJson = resultMap.get("batchWithFileRowListJSON");//生成的批量代扣文件中的记录
			//验证文件大小，每个批量代扣文件记录条数不能超过2000条，文件大小不能超过625k
//			File file = new File(filePath);
//			if(file.exists() && file.isFile()){
//				file.length();//返回文件大小（byte）
//			}
			record.setFileSn(sn);
			record.setWithholdFile(filePath);
			int rows = ccbBatchWithholdRecordService.insertSelective(record);
			
			List<CcbUploadFileRowBean> withholdFileRowList = JSON.parseArray(batchWithFileRowListJson, CcbUploadFileRowBean.class);//解析生成的批量代扣文件中数据
			if(rows>0) {
				Long recordId = record.getId();//批量代扣文件记录ID
				//循环批量代扣文件内容数据，增加批量搭扣文件与账目的关系
				for(int i=0; i<fileRowList.size(); i++) {
					CcbUploadFileRowBean row = fileRowList.get(i);
					
					// added by srd on 2019/12/02
					//设置批量代扣文件记录与账目关系表中的序号与批量代扣文件中的序号相同
					for(CcbUploadFileRowBean fileRow : withholdFileRowList) {
						if(row.getAccountName().equalsIgnoreCase(fileRow.getAccountName()) && row.getCardNo().equalsIgnoreCase(fileRow.getCardNo())) {
							row.setSn(fileRow.getSn());
						}
					}
					
					//获取批量搭扣文件与账目的关系
					CcbBatchCustomerAccount ccbBathAccountItem = this.getCcbBatchCustomerAccount(row.getAccountItemId(), recordId, row.getAccountName(), row.getCardNo(), row.getTotalAmount(), row.getSn());
					rows = ccbBatchCustomerAccountService.insertSelective(ccbBathAccountItem);
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
	

	
	/**
	 * @Title: getCcbBatchWithholdRecord
	 * @Description: 获取CCB批量代扣记录实体类
	 * @param period
	 * @param traceIds
	 * @param remark
	 * @return 
	 */
	private CcbBatchWithholdRecord getCcbBatchWithholdRecord(String period, String traceIds, String remark) {
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		//record.setFileType(type);
		//record.setFileSn(sn);
		//record.setWithholdFile(filePath);
		record.setStatus(EnumCcbBatchStatus.GENERATE.getValue());//设置状态为已生成
		record.setPeriod(period);
		record.setTraceId(traceIds);
		record.setRemark(remark);
		logger.info("保存CCB批量代扣记录："+record);
		return record;
	}
	
	/**
	 * @Title: getCcbBatchCustomerAccount
	 * @Description: 获取批量代扣记录与账目表关系实体
	 * @param accountItemId
	 * @param withholdId
	 * @param accountName
	 * @param cardNo
	 * @param accountAmount
	 * @param number
	 * @return 
	 */
	private CcbBatchCustomerAccount getCcbBatchCustomerAccount(Long accountItemId, Long withholdId, String accountName, String cardNo, BigDecimal accountAmount, Integer number) {
		
		//系统登录用户
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date sysDate = new Date();//系统时间
		
		CcbBatchCustomerAccount record = new CcbBatchCustomerAccount();
		record.setAccountItemId(accountItemId);
		record.setBathWithholdingId(withholdId);
		record.setWithholdingStatus(EnumCcbWhHoldStatus.WITHHOLDING_NO.getValue());
		record.setSettleAccountStatus(EnumCcbSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());
		record.setJoinTime(sysDate);
		record.setOperatorId(userBean.getId());
		record.setOperatorName(userBean.getRealname());
		record.setCustomerName(accountName);
		record.setCustomerCardNo(cardNo);
		record.setWithholdingNo(number);
		record.setAccountAmount(accountAmount);
		return record;
	}
	
	/**
	 * @Title: saveDataToFile
	 * @Description: 保存到文件，并返回文件路径
	 * @param fileType
	 * @param fileName
	 * @param rowList
	 * @return 
	 */
	private Map<String, String> saveDataToFile(String fileType, String fileName, List<CcbUploadFileRowBean> rowList) {
		
		//合并List中客户姓名和卡号相同的数据，并把金额相加
		List<CcbUploadFileRowBean> rowList1 = new ArrayList<>();
		//List深度复制
		try {
			rowList1 = ListUtils.deepCopy(rowList);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//rowList1.addAll(rowList);
		
		//合并户名和卡号相同的金额，并加放到新的集合中
		List<CcbUploadFileRowBean> resultList = new ArrayList<>();
		for (int i = 0; i < rowList1.size(); i++) {
			CcbUploadFileRowBean rowBean = rowList1.get(i);
			BigDecimal totalAmount = rowBean.getTotalAmount();
			//BigDecimal totalAmount = new BigDecimal(0.00);
			List<CcbUploadFileRowBean> rowList2 = new ArrayList<>();
			rowList2.addAll(rowList1);
			
			int removeCount = 0;
			for (int j = 0; j < rowList2.size(); j++) {
				CcbUploadFileRowBean rowBean2 = rowList2.get(j);
				if (i!=j && rowBean.getAccountName().equalsIgnoreCase(rowBean2.getAccountName()) && rowBean.getCardNo().equalsIgnoreCase(rowBean2.getCardNo())) {
					totalAmount = BigDecimalUtils.add(totalAmount, rowBean2.getTotalAmount());
					rowList1.remove((j - removeCount));
					removeCount = removeCount + 1;
				}
			}
			rowBean.setTotalAmount(totalAmount);
			resultList.add(rowBean);
		}
		
		//输出到文件
		final String charset = "GBK";//字体集
		
		String filePath = this.getPath();//根据操作系统类型获取上传文件目录
		filePath += File.separator + fileType + File.separator;
		
		String lineSeparator = CCBFileUtil.lineSeparator();//回车换行符号
		
		StringBuffer sb = new StringBuffer();
		sb.append(lineSeparator);
		for(int i=0; i<resultList.size(); i++) {
			CcbUploadFileRowBean row = resultList.get(i);
			row.setSn(i+1);
			String rowContent = row.getFileRowContent();
			sb.append(rowContent).append(lineSeparator);
		}
		
		CCBFileUtil.saveDataToFile(filePath, fileName, sb.toString(), charset);
		logger.info("创建上传文件的目录："+filePath+fileName);
		
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("filePath", filePath+fileName);
		resultMap.put("batchWithFileRowListJSON", JSON.toJSONString(resultList));
		
		return resultMap;
	}
	
	/**
	 * @Title: getCcbUploadFileRowBean
	 * @Description: 获取建行代扣文件一行数据Bean
	 * @param customerId
	 * @param accountItemId
	 * @param creditAmount
	 * @param debitAmount
	 * @return 
	 * 		返回代扣文件一行数据Bean或null
	 */
	private CcbUploadFileRowBean getCcbUploadFileRowBean(Long customerId, Long accountItemId, BigDecimal creditAmount, BigDecimal debitAmount) {
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的常量0
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(customerId);
		if(customer.getDeductType()==EnumDeductType.CCB.getValue()) {//判断客户的代扣方式是否是建设银行代扣，如果是则继续，否则过滤该账单
			//查询客户-银行信息
			CustomerBanks bank = bankService.getCustomerBank(customer.getId());
			if(bank!=null) {
				//计算账单的欠费金额
				BigDecimal oweAmount = BigDecimalUtils.subtract(creditAmount, debitAmount);
				if(BigDecimalUtils.greaterThan(oweAmount, zero)) {//如果欠费金额>0
					CcbUploadFileRowBean row = new CcbUploadFileRowBean();
					row.setAccountItemId(accountItemId);//账目ID
					row.setCardNo(bank.getCardNo());
					row.setAccountName(bank.getAccountName());
					row.setTotalAmount(oweAmount);
					return row;
				}
			}
			
		}
		return null;
	}
	
	/**
	 * @Title: getBatchWithholdSn
	 * @Description: 获取批量代扣文件的唯一编号
	 * @param prefix	本次生成批量代扣文件的名称前缀
	 * @param number	本次生成批量代扣文件的序号
	 * @return 
	 */
	private String getBatchWithholdSn(int number) {
		
		int SN_MAX_LENGTH = 20;
		
		StringBuffer sb = new StringBuffer();
		String daterandom = DateUtils.getDateRandom();//生成时间随机数（yyyyMMddHHmmssSSS）
		sb.append(daterandom).append(number);
		
		//如果序列号长度大于最大长度20时，直接返回时间字符串（年、月、日、时、分、秒、毫秒）+0~9的随机数
		if(sb.toString().length()>SN_MAX_LENGTH) {
			Random random = new Random();
			int randomInt = random.nextInt(10);
			return DateUtils.getDateRandom()+randomInt;//生成时间随机数（yyyyMMddHHmmssSSS）
		}
		
		return sb.toString();
	}
	
	/**
	 * @Title: getBatchWithholdFileName
	 * @Description: 获取批量代扣文件的文件名
	 * @param prefix	本次生成批量代扣文件的名称前缀
	 * @param number	本次生成批量代扣文件的序号
	 * @return 
	 */
	private String getBatchWithholdFileName(String prefix, int number) {
		StringBuffer sb = new StringBuffer();
		String daterandom = DateUtils.getDateRandom();//生成时间随机数（yyyyMMddHHmmssSSS）
		if(StringUtils.isNotBlank(prefix)) {
			sb.append(prefix);
		}
		sb.append(daterandom).append(number);
		return sb.toString();
	}
	
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getUploadFolder();
		System.out.println("----------上传文件目录:"+path);
		return path;
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
	
}
