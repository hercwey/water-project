package com.learnbind.ai.service.cmbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcSettleAccountStatus;
import com.learnbind.ai.common.enumclass.EnumCmbcWhHoldStatus;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.ListUtils;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.CmbcBatchCustomerAccount;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.cmbc
 *
 * @Title: CmbcBatchWithholdService.java
 * @Description: 民生银行批量代扣服务
 *
 * @author Administrator
 * @date 2019年8月25日 上午10:03:29
 * @version V1.0 
 *
 */
@Service
public class CmbcBusinessBatchWithholdService {
	
	private static final Logger logger = LoggerFactory.getLogger(CmbcBusinessBatchWithholdService.class);
	
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	public CmbcBatchCustomerAccountService cmbcBatchCustomerAccountService;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
	@Autowired
	public CmbcBatchWithholdRecordService cmbcBatchWithholdRecordService;
	@Autowired
	public CustomersService customersService;
	@Autowired
	public BankService bankService;
		
	/**
	 * @Title: exportCmbcAccountItem
	 * @Description: 根据用户选择客户ID查询并导出excel文件
	 * @param customerIds
	 * @param period
	 * @param remark
	 * @param prefix	用户选择生成批量代扣文件时输入的前缀
	 * @return 
	 */
	@Transactional
	public int exportCmbcAccountItem(List<Long> customerIdList, String period, String traceIds, String remark, String prefix) {
		int rows = 0;
		
		//批量Excel文件内容集合
		List<CmbcExcelFileRowBean> excelRowList = new ArrayList<>();
		//for(int i=0; i<customerIdLength; i++) {
		for(Long customerId : customerIdList) {
			//查询客户本期欠费账单和往期欠费账单
			//List<CustomerAccountItem> currOweBillList = customerAccountItemService.getWaterFeeOweBillList(Long.valueOf(customerIdStr), period);
			List<CustomerAccountItem> currOweBillList = customerAccountItemService.getCmbcWithholdBill(customerId, period);
			for(CustomerAccountItem item : currOweBillList) {
				//判断账单是否需要再次扣费
				boolean isAgainDeduct = cmbcBatchCustomerAccountService.isAgainDeduct(item.getId());
				if(isAgainDeduct) {//如果需要再次扣费，则加入批量代扣文件
					//获取民生代扣文件一行数据Bean
					CmbcExcelFileRowBean excelRow = this.getCmbcExcelRowBean(item.getCustomerId(), item.getId(), item.getCreditAmount(), item.getDebitAmount());
					if(excelRow!=null) {
						excelRowList.add(excelRow);
					}
//						//查询该账单的违约金账单
//						List<CustomerAccountItem> overdueBillList = customerAccountItemService.getOverdueBillList(Long.valueOf(customerIdStr));
//						for(CustomerAccountItem overdueBill : overdueBillList) {
//							//获取民生代扣文件一行数据Bean
//							excelRow = this.getCmbcExcelRowBean(overdueBill.getCustomerId(), overdueBill.getId(), overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
//							if(excelRow!=null) {
//								excelRowList.add(excelRow);
//							}
//						}
				}
				
			}
//				//查询客户往期欠费账单
//				List<CustomerAccountItem> pastOweBillList = customerAccountItemService.getPastWaterFeeOweBillList(Long.valueOf(customerIdStr), period);
//				for(CustomerAccountItem item : pastOweBillList) {
//					//获取民生代扣文件一行数据Bean
//					CmbcExcelFileRowBean excelRow = this.getCmbcExcelRowBean(item.getCustomerId(), item.getId(), item.getCreditAmount(), item.getDebitAmount());
//					if(excelRow!=null) {
//						excelRowList.add(excelRow);
//					}
////					//查询该账单的违约金账单
////					List<CustomerAccountItem> overdueBillList = customerAccountItemService.getOverdueBillList(Long.valueOf(customerIdStr));
////					for(CustomerAccountItem overdueBill : overdueBillList) {
////						//获取民生代扣文件一行数据Bean
////						excelRow = this.getCmbcExcelRowBean(overdueBill.getCustomerId(), overdueBill.getId(), overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
////						if(excelRow!=null) {
////							excelRowList.add(excelRow);
////						}
////					}
//				}
				
			
		}
		
		//判断需要导出Excel的集合是否为空，如果为空则直接返回，并提示用户，如果不为空则继续执行导出excel功能
		if(excelRowList==null || excelRowList.size()<=0) {
			return -1;//返回-1表示没有需要批量代扣的账单
		}
		
		//获取CMBC批量代扣记录实体类(用户选择账单时，traceIds设置为空)
		CmbcBatchWithholdRecord record = this.getCmbcBatchWithholdRecord(period, traceIds, remark);
		rows = this.addBatchWithholdFile(prefix, record, excelRowList);
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return rows;
	}
	
	/**
	 * @Title: exportCmbcAccountItem
	 * @Description: 根据用户选择的条件查询并导出excel文件 	TODO 未用
	 * @param period
	 * @param remark
	 * @param prefix
	 * @param traceIds
	 * @param itemList
	 * @return 
	 */
	@Transactional
	public int exportCmbcAccountItem(String period, String remark, String prefix, String traceIds, List<CustomerAccountItem> itemList) {
		int rows = 0;
		
		//批量代扣文件内容集合
		List<CmbcExcelFileRowBean> excelRowList = new ArrayList<>();
		
		int itemSize = itemList.size();//集合中账目个数
		for(int i=0; i<itemSize; i++) {
			CustomerAccountItem item = itemList.get(i);//账单信息
			//获取民生代扣文件一行数据Bean
			CmbcExcelFileRowBean excelRow = this.getCmbcExcelRowBean(item.getCustomerId(), item.getId(), item.getCreditAmount(), item.getDebitAmount());
			if(excelRow!=null) {
				excelRowList.add(excelRow);
			}
			//查询该账单的违约金账单
//			List<CustomerAccountItem> overdueBillList = customerAccountItemService.getOverdueBillList(item.getId());
//			for(CustomerAccountItem overdueBill : overdueBillList) {
//				//获取民生代扣文件一行数据Bean
//				excelRow = this.getCmbcExcelRowBean(overdueBill.getCustomerId(), overdueBill.getId(), overdueBill.getCreditAmount(), overdueBill.getDebitAmount());
//				if(excelRow!=null) {
//					excelRowList.add(excelRow);
//				}
//			}
			
		}
		
		//判断需要导出Excel的集合是否为空，如果为空则直接返回，并提示用户，如果不为空则继续执行导出excel功能
		if(excelRowList==null || excelRowList.size()<=0) {
			return -1;//返回-1表示没有需要批量代扣的账单
		}
		
		//获取CCB批量代扣记录实体类
		CmbcBatchWithholdRecord record = this.getCmbcBatchWithholdRecord(period, traceIds, remark);
		rows = this.addBatchWithholdFile(prefix, record, excelRowList);
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	/**
	 * @Title: getCmbcExcelRowBean
	 * @Description: 获取民生excel文件一行数据Bean
	 * @param customerId
	 * @param accountItemId
	 * @param creditAmount
	 * @param debitAmount
	 * @return 
	 * 		返回excel文件一行数据Bean或null
	 */
	private CmbcExcelFileRowBean getCmbcExcelRowBean(Long customerId, Long accountItemId, BigDecimal creditAmount, BigDecimal debitAmount) {
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的常量0
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(customerId);
		if(customer.getDeductType()==EnumDeductType.CMBC.getValue()) {//判断客户的代扣方式是否是民生银行代扣，如果是则继续，否则过滤该账单
			//查询客户-银行信息
			CustomerBanks bank = bankService.getCustomerBank(customer.getId());
			if(bank!=null) {
				//计算账单的欠费金额
				BigDecimal oweAmount = BigDecimalUtils.subtract(creditAmount, debitAmount);
				if(BigDecimalUtils.greaterThan(oweAmount, zero)) {//如果欠费金额>0
					CmbcExcelFileRowBean row = new CmbcExcelFileRowBean();
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
	 * @Title: getCmbcBatchWithholdRecord
	 * @Description: 获取CMBC批量代扣记录实体类
	 * @param period
	 * @param traceIds
	 * @param remark
	 * @return 
	 */
	private CmbcBatchWithholdRecord getCmbcBatchWithholdRecord(String period, String traceIds, String remark) {
		
		CmbcBatchWithholdRecord record = new CmbcBatchWithholdRecord();
		//record.setFileType(type);
		//record.setFileSn(sn);
		//record.setWithholdFile(filePath);
		record.setStatus(EnumCcbBatchStatus.GENERATE.getValue());//设置状态为已生成
		record.setPeriod(period);
		record.setTraceId(traceIds);
		record.setRemark(remark);
		logger.info("保存CMBC批量代扣记录："+record);
		return record;
	}
	
	/**
	 * @Title: addBatchWithholdFile
	 * @Description: 导出代扣excel文件并增加批量代扣记录和批量代扣记录-账目关系
	 * @param prefix
	 * @param record
	 * @param excelRowList
	 * @return 
	 */
	@Transactional
	private int addBatchWithholdFile(String prefix, CmbcBatchWithholdRecord record, List<CmbcExcelFileRowBean> excelRowList) {
		
		try {
			
			//获取批量代扣文件唯一编号
			String sn = this.getBatchWithholdSn(prefix);
			
			//保存数据到文件，并返回保存文件路径
			String filePath = this.exportExcelFile(sn, excelRowList);
			
			record.setFileSn(sn);
			record.setWithholdFile(filePath);
			int rows = cmbcBatchWithholdRecordService.insertSelective(record);
			if(rows>0) {
				Long recordId = record.getId();//批量代扣文件记录ID
				//循环批量代扣文件内容数据，增加批量搭扣文件与账目的关系
				int size = excelRowList.size();
				for(int i=0; i<size; i++) {
					CmbcExcelFileRowBean row = excelRowList.get(i);
					if(row.getSn()==null || row.getSn()<=0) {
						row.setSn(i+1);
					}
					//获取批量代扣文件与账目的关系
					CmbcBatchCustomerAccount cmbcBathAccountItem = this.getCmbcBatchCustomerAccount(row.getAccountItemId(), recordId, row.getSn(), row.getCardNo(), row.getAccountName(), row.getTotalAmount());
					rows = cmbcBatchCustomerAccountService.insertSelective(cmbcBathAccountItem);
					if(rows<=0) {
						break;
					}
				}
				logger.info("----------导出民生银行代扣成功，导出数量是【 "+size+"】， 导出文件路径："+filePath);
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
	 * @Title: exportExcelFile
	 * @Description: 导出民生银行代扣excel
	 * @param sn
	 * @param excelRowList 
	 */
	private String exportExcelFile(String sn, List<CmbcExcelFileRowBean> excelRowList) {
		
		//合并List中客户姓名和卡号相同的数据，并把金额相加
		List<CmbcExcelFileRowBean> rowList1 = new ArrayList<>();
		try {
			//List深度复制
			rowList1 = ListUtils.deepCopy(excelRowList);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//rowList1.addAll(excelRowList);
		
		//合并户名和卡号相同的金额，并加放到新的集合中
		List<CmbcExcelFileRowBean> resultList = new ArrayList<>();
		for (int i = 0; i < rowList1.size(); i++) {
			CmbcExcelFileRowBean rowBean = rowList1.get(i);
			BigDecimal totalAmount = rowBean.getTotalAmount();
			//BigDecimal totalAmount = new BigDecimal(0.00);
			List<CmbcExcelFileRowBean> rowList2 = new ArrayList<>();
			rowList2.addAll(rowList1);
			
			int removeCount = 0;
			for (int j = 0; j < rowList2.size(); j++) {
				CmbcExcelFileRowBean rowBean2 = rowList2.get(j);
				if (i!=j && rowBean.getAccountName().equalsIgnoreCase(rowBean2.getAccountName()) && rowBean.getCardNo().equalsIgnoreCase(rowBean2.getCardNo())) {
					totalAmount = BigDecimalUtils.add(totalAmount, rowBean2.getTotalAmount());
					rowList1.remove((j - removeCount));
					removeCount = removeCount + 1;
				}
			}
			rowBean.setTotalAmount(totalAmount);
			resultList.add(rowBean);
		}
		
		
		
		//excel标题
		String[] titles = { "序号", "银行卡号", "账户名", "金额（￥）"};
		//excel列名
		String[] columnNames = { "sn", "cardNo", "accountName", "totalAmount"};
		//sheet名
		String sheetName = "中国民生银行扣费信息";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = new ArrayList<>();
		for(int i=0; i<resultList.size(); i++) {
			CmbcExcelFileRowBean bean = resultList.get(i);
			if(bean.getSn()==null || bean.getSn()<=0) {
				bean.setSn(i+1);
			}
			Map<String, Object> excelData = EntityUtils.entityToMap(bean);
			excelDataList.add(excelData);
		}
		
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath();
		String fileName = sn+".xls";
		String filePath = realPath+fileName;//导出文件全路径
		
		File file = new File(filePath);
		
		//文件输出流
	    FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			wb.write(outStream);
			outStream.flush();
			outStream.close();
			logger.info("----------导出民生银行扣量代扣Excel文件成功，文件路径："+filePath);
			return filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	/**
	 * 获取文件路径
	 * @param request
	 * @return
	 */
	private String getRealPath(){
		
		String separator = File.separator;
		
		String realPath = this.getPath();
		String dateFold = DateUtils.getDate();//获取当前日期字符串 格式（yyyy-MM-dd）
		realPath = realPath+separator+"cmbc_withhold_excel"+separator+dateFold+separator;
		
		File file = new File(realPath);
		//如果文件路径不存在，则创建
		if(!file.exists()){
			file.mkdirs();
		}
		return realPath;
	}
	
	/**
	 * @Title: getBatchWithholdSn
	 * @Description: 获取批量代扣文件的唯一编号
	 * @param prefix	本次生成批量代扣文件的名称前缀
	 * @param number	本次生成批量代扣文件的序号
	 * @return 
	 */
	private String getBatchWithholdSn(String prefix) {
		StringBuffer sb = new StringBuffer();
		String daterandom = DateUtils.getDateRandom();//生成时间随机数（yyyyMMddHHmmssSSS）
		if(StringUtils.isNotBlank(prefix)) {
			sb.append(prefix);
		}
		sb.append(daterandom);
		return sb.toString();
	}
	
	/**
	 * @Title: getCmbcBatchCustomerAccount
	 * @Description: 获取批量代扣记录与账目表关系实体
	 * @param accountItemId
	 * @param withholdId
	 * @param number
	 * @param cardNo
	 * @param accountName
	 * @param totalAmount
	 * @return 
	 */
	private CmbcBatchCustomerAccount getCmbcBatchCustomerAccount(Long accountItemId, Long withholdId, Integer no, String cardNo, String accountName, BigDecimal totalAmount) {
		
		//系统登录用户
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date sysDate = new Date();//系统时间
		
		CmbcBatchCustomerAccount record = new CmbcBatchCustomerAccount();
		record.setAccountItemId(accountItemId);
		record.setBathWithholdingId(withholdId);
		record.setWithholdingStatus(EnumCmbcWhHoldStatus.WITHHOLDING_NO.getValue());
		record.setSettleAccountStatus(EnumCmbcSettleAccountStatus.SETTLE_ACCOUNT_NO.getValue());
		record.setJoinTime(sysDate);
		record.setOperatorId(userBean.getId());
		record.setOperatorName(userBean.getRealname());
		record.setWithholdingNo(no);
		record.setCustomerCardNo(cardNo);
		record.setCustomerName(accountName);
		record.setAccountAmount(totalAmount);
		return record;
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
	
	public static void main(String[] args) {
		List<CmbcExcelFileRowBean> beanList = new ArrayList<>();
		
		CmbcExcelFileRowBean bean1 = new CmbcExcelFileRowBean();
		bean1.setSn(1);
		beanList.add(bean1);
		CmbcExcelFileRowBean bean2 = new CmbcExcelFileRowBean();
		bean2.setSn(2);
		beanList.add(bean2);
		
		System.out.println("----------1:"+beanList);
		
		
		List<CmbcExcelFileRowBean> resultList = new ArrayList<>();
		//List<CmbcExcelFileRowBean> resultList;
		try {
			//深度copy
			resultList = ListUtils.deepCopy(beanList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//list深度拷贝
//        CollectionUtils.addAll(resultList, new Object[beanList.size()]);
//        Collections.copy(resultList, beanList);
		for(int i=0; i<beanList.size(); i++) {
			CmbcExcelFileRowBean bean = beanList.get(i);
			bean.setSn((i+3));
//			try {
//				CmbcExcelFileRowBean newBean = (CmbcExcelFileRowBean)BeanUtils.cloneBean(bean);
//				newBean.setSn((i+3));
//				resultList.add(newBean);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//			}
		}
		System.out.println("----------2:"+resultList);
		System.out.println("----------3:"+beanList);
	}

}
