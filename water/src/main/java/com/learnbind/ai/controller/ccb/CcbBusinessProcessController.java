package com.learnbind.ai.controller.ccb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.ccb.CcbBusinessUtils;
import com.learnbind.ai.ccb.CcbUploadFileRowBean;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.ccb.keyfile.KeyFileUtil;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.ccb
 *
 * @Title: CcbBusinessProcessController.java
 * @Description: 中国建设银行批量代扣业务流程处理控制器
 *
 * @author Administrator
 * @date 2019年7月14日 下午3:43:51
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/ccb-business")
public class CcbBusinessProcessController {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbBusinessProcessController.class);
	
	private static final String TEMPLATE_PATH = "ccb_business/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//中国建设银行批量代扣记录服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private BankService bankService;//客户银行信息
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "ccb_business_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "ccb_business_main";
	}
	
	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String type) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = 1;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setFileType(type);
		List<CcbBatchWithholdRecord> recordlist = ccbBatchWithholdRecordService.select(record);
		PageInfo<CcbBatchWithholdRecord> pageInfo = new PageInfo<>(recordlist);// (使用了拦截器或是AOP进行查询的再次处理)

		if(recordlist!=null && recordlist.size()>0) {
			// 传递如下数据至前台页面
			model.addAttribute("record", recordlist.get(0));  //列表数据
		}else {
			// 传递如下数据至前台页面
			model.addAttribute("record", null);  //列表数据
		}
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "ccb_business_table";
	}
	
	@RequestMapping(value = "/create-batch-withhold-file", produces = "application/json")
	@ResponseBody
	public Object testCreateBatchWithholdFile(String period, String locationCode) {
		
		try {
			
			String type = CcbBusinessUtils.getDateYYYYMMDD();//获取类型
			
			// TODO CommonKey
			// TODO DesKey：前端增加按钮获取
			// TODO Local RSA
			//linux或windows通用文件目录
			String uploadFolder = uploadFileConfig.getUploadFolder();
			String publicKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			String privateKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
			this.generatorRsaKeyPairToFile(publicKeyFilePath, privateKeyFilePath);
			// TODO CCB RSA public key：前端页面增加按钮获取
			
			List<CcbBatchWithholdRecord> recordList = new ArrayList<CcbBatchWithholdRecord>();//保存创建上传文件后的记录，全部完成后保存到数据库
			
			List<CcbUploadFileRowBean> rowList = new ArrayList<>();
			for(int i=0; i<100; i++) {
				CcbUploadFileRowBean row = new CcbUploadFileRowBean();
				row.setCardNo("6227000132100789567"+i);
				row.setAccountName("用户"+i);
				row.setTotalAmount(new BigDecimal(i));
				rowList.add(row);
			}
			
			String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
			String filePath = this.saveDataToFile(type, sn, rowList);//保存文件
			CcbBatchWithholdRecord record =  this.getCcbBatchWithholdRecord(type, sn, filePath);//创建记录实体
			recordList.add(record);
			
			int rows = ccbBatchWithholdRecordService.saveList(recordList);
			if (rows > 0) {
				return RequestResultUtil.getResultSuccess("创建批量代扣文件成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("创建批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: createBatchWithholdFile
	 * @Description: 创建批量代扣文件
	 * @param period
	 * @param locationCode
	 * @return 
	 */
	//@RequestMapping(value = "/create-batch-withhold-file", produces = "application/json")
	//@ResponseBody
	public Object createBatchWithholdFile(String period, String locationCode) {
		
		try {
			
			// TODO CommonKey
			// TODO DesKey：前端增加按钮获取
			// TODO Local RSA
			//linux或windows通用文件目录
			String uploadFolder = uploadFileConfig.getUploadFolder();
			String publicKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			String privateKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
			this.generatorRsaKeyPairToFile(publicKeyFilePath, privateKeyFilePath);
			// TODO CCB RSA public key：前端页面增加按钮获取
			
			List<CcbBatchWithholdRecord> recordList = new ArrayList<CcbBatchWithholdRecord>();//保存创建上传文件后的记录，全部完成后保存到数据库
			
			String type = CcbBusinessUtils.getDateYYYYMMDD();//获取类型
			
			int pageNum = 1;
			int pageSize = 2000;
			PageInfo<Map<String, Object>> pageInfo = this.getFileDataPager(pageNum, pageSize, period, locationCode);//获取第一页内容
			
			CcbBatchWithholdRecord record = this.getCcbBatchWithholdRecord(type, pageInfo.getList());//获取创建上传文件后的记录，全部完成后需要保存到数据库
			recordList.add(record);
			
			int pages = pageInfo.getPages();//总页数
			if(pages>1) {
				for(int i=2; i<=pages; i++) {
					pageInfo = this.getFileDataPager(pageNum, pageSize, period, locationCode);//获取除第一页内容外的其他页内容
					record = this.getCcbBatchWithholdRecord(type, pageInfo.getList());//获取创建上传文件后的记录，全部完成后需要保存到数据库
					recordList.add(record);
				}
			}
			
			int rows = ccbBatchWithholdRecordService.saveList(recordList);
			if (rows > 0) {
				return RequestResultUtil.getResultSuccess("创建批量代扣文件成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("创建批量代扣文件异常，请检查日志！");
	}

	/**
	 * 自CCB中获取配置参数
	 * 
	 * @param parmKey
	 * @return
	 */
	private String getCCBConfigParm(String parmKey) {
		String parmValue = "";
		SysInterfaceConfig config = new SysInterfaceConfig();
		config.setInterfaceType(InterfaceConstant.INTERFACE_TYPE_CCB);
		config.setKey(parmKey);
		SysInterfaceConfig result = interfaceConfigService.selectOne(config);
		if (result != null) {
			parmValue = result.getValue();
		}
		return parmValue;
	}
	
	/**
	 * @Title: getCcbBatchWithholdRecord
	 * @Description: 获取CCB批量代扣记录实体类
	 * @param accountItemMapList
	 * @return 
	 */
	private CcbBatchWithholdRecord getCcbBatchWithholdRecord(String type, List<Map<String, Object>> accountItemMapList) {
		
		String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
		
		List<CcbUploadFileRowBean> rowList = this.getUploadFileRowList(accountItemMapList);//获取上传文件内容
		String filePath = this.saveDataToFile(type, sn, rowList);
		return this.getCcbBatchWithholdRecord(type, sn, filePath);
	}
	
	/**
	 * @Title: CcbBatchWithholdRecord
	 * @Description: 获取CCB批量代扣记录实体类
	 * @param type
	 * @param sn
	 * @param filePath
	 * @return 
	 */
	private CcbBatchWithholdRecord getCcbBatchWithholdRecord(String type, String sn, String filePath) {
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setFileType(type);
		record.setFileSn(sn);
		record.setWithholdFile(filePath);
		logger.info("保存CCB批量代扣记录："+record);
		return record;
	}
	
	/**
	 * @Title: saveDataToFile
	 * @Description: 保存到文件，并返回文件路径
	 * @param type
	 * @param rowList
	 * @return 
	 */
	private String saveDataToFile(String type, String sn, List<CcbUploadFileRowBean> rowList) {
		String filePath = this.getPath();//根据操作系统类型获取上传文件目录
		filePath = File.separator + type + File.separator;
		
		String fileName = sn+".txt";
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<rowList.size(); i++) {
			CcbUploadFileRowBean row = rowList.get(i);
			row.setSn(i+1);
			
			String rowContent = row.getFileRowContent();
			
			String lineSeparator = CCBFileUtil.lineSeparator();//回车换行符号
			
			sb.append(rowContent).append(lineSeparator);
		}
		
		CCBFileUtil.saveDataToFile(filePath, fileName, sb.toString());
		logger.info("创建上传文件的目录："+filePath+fileName);
		return filePath+fileName;
	}
	
	/**
	 * @Title: getFileDataPager
	 * @Description: 获取上传文件分页数据
	 * @param pageNum
	 * @param pageSize
	 * @param period
	 * @param locationCode
	 * @return 
	 */
	private PageInfo<Map<String, Object>> getFileDataPager(Integer pageNum, Integer pageSize, String period, String locationCode) {
		
		Integer FILE_PAGE_SIZE = 2000;
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = FILE_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, locationCode, null, null, null, operatorId, null, null);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		logger.info("批量代扣的总记录数："+pageInfo.getTotal()+" 共 "+pageInfo.getPages()+" 页 当前处理的是第 "+pageInfo.getPageNum()+" 页。");

		return pageInfo;
	}
	
	/**
	 * @Title: getUploadFileRowList
	 * @Description: 获取上传文件内容
	 * @param accountItemMapList
	 * @return 
	 */
	private List<CcbUploadFileRowBean> getUploadFileRowList(List<Map<String, Object>> accountItemMapList){
		
		List<CcbUploadFileRowBean> rowList = new ArrayList<>();//保存上传文件每行的内容
		
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
			String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			
			//计算违约金总金额
			//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(customerId,periodTemp);
			//计算欠费金额（账单欠费金额+违约金欠费金额）
			BigDecimal owedAmountValue = customerAccountItemService.getCurrBillOwedAmount(customerId);
			
			Customers customer = customersService.selectByPrimaryKey(customerId);//客户信息
			
			Integer deductType = customer.getDeductType();//扣费方式 1=其他；2=建行自动扣费；3=民生银行自动扣费
			
			if(deductType!=null && deductType==EnumDeductType.CCB.getValue()) {
				CustomerBanks bank = this.getCustomerBank(customerId);//获取客户-银行信息
				if(bank!=null) {
					String cardNo = bank.getCardNo();//卡号
					String accountName = bank.getAccountName();//开户名
					if(StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(accountName)) {
						CcbUploadFileRowBean row = new CcbUploadFileRowBean();
						row.setCardNo(cardNo);
						row.setAccountName(accountName);
						row.setTotalAmount(owedAmountValue);
						rowList.add(row);
					}
				}
			}
		}
		return rowList;
	}
	
	/**
	 * @Title: getCustomerBank
	 * @Description: 获取客户-银行信息
	 * @param customerId
	 * @return 
	 */
	private CustomerBanks getCustomerBank(Long customerId) {
		Example example = new Example(CustomerBanks.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue());
		example.setOrderByClause(" ID DESC");
		List<CustomerBanks> bankList = bankService.selectByExample(example);
		if(bankList!=null && bankList.size()>0) {
			CustomerBanks respBank = null;
			for(CustomerBanks tempBank : bankList) {
				String accountName = tempBank.getAccountName();//开户名
				String cardNo = tempBank.getCardNo();//卡号
				if(StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(cardNo)) {
					respBank = tempBank;
					break;
				}
			}
			return respBank;
		}
		return null;
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
	
	/**
	 * @Title: generatorRsaKeyPairToFile
	 * @Description: 自动生成RSA钥匙对并保存到对应文件
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	private void generatorRsaKeyPairToFile(String publicKeyFilePath, String privateKeyFilePath) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		KeyPair keyPair = MD5withRSA.getKeyPair(); // 获取钥匙对
		KeyFileUtil.saveRsaKeyPairToFile(keyPair, publicKeyFilePath, privateKeyFilePath);
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().toUpperCase().replaceAll("-+", ""));
	}
	
}
