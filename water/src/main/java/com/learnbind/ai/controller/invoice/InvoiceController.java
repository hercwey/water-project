package com.learnbind.ai.controller.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Invoice;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.invoice.InvoiceService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.invoice
 *
 * @Title: InvoiceController.java
 * @Description: 发票信息前端控制器
 *
 * @author Administrator
 * @date 2019年8月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/invoice")
public class InvoiceController {
	private static Log log = LogFactory.getLog(InvoiceController.class);
	private static final String TEMPLATE_PATH = "invoice/"; // 页面目录
	private static final String TEMPLATE_INSERT_PATH = "invoice/insert/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	private InvoiceService invoiceService;//发票管理
	@Autowired
	private CustomersService customersService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LocationCustomerService locationCustomerService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService;
	

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "invoice_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "invoice_main";
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer invoiceType, String period) {

		Long operatorId = this.getOperatorId();//操作员ID
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Invoice> invoiceList = invoiceService.searchInvoice(searchCond, invoiceType, period);
		PageInfo<Invoice> pageInfo = new PageInfo<>(invoiceList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> invoiceMapList = new ArrayList<>();
		for(Invoice invoice : invoiceList) {
			Map<String, Object> knowMap = EntityUtils.entityToMap(invoice);
			knowMap.put("invoiceDateStr", invoice.getInvoiceDateStr());//开票日期
			
			invoiceMapList.add(knowMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("invoiceList", invoiceMapList);//列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "invoice_table";
	}

	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long itemId) {
		
		Invoice invoice = null;
		if(itemId!=null) {
			invoice = invoiceService.selectByPrimaryKey(itemId);
		}
		model.addAttribute("currItem", invoice);
		
		return TEMPLATE_PATH + "invoice_dialog_edit";
	}
	
	/**
	 * @Title: loadInsertInvoiceItem
	 * @Description:跳转增加发票信息选项卡
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-insert-invoice-item")
	public String loadInsertInvoiceItem(Model model, Long itemId) {
		Invoice invoice = null;
		String propName = "";
		if(itemId!=null) {
			invoice = invoiceService.selectByPrimaryKey(itemId);
			CustomerAccountItem account = customerAccountItemService.selectByPrimaryKey(invoice.getAccountItemId());
			Customers customer = customersService.selectByPrimaryKey(account.getCustomerId());
			propName = customer.getCustomerName();
		}
		
		model.addAttribute("currItem", invoice);
		model.addAttribute("propName", propName);
		return TEMPLATE_PATH + "insert_invoice_item";
	}
	
	/**
	 * @Title: accountItemMain
	 * @Description: 加载增加发票信息的账单界面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/account-item-main")
	public String accountItemMain(Model model) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_INSERT_PATH + "account_item_main";
	}
	
	/**
	 * @Title: accountItemTable
	 * @Description: 加载增加发票信息的账单列表
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/account-item-table")
	public String accountItemTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String period) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, traceIds, null, null, searchCond, null, null, null);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String accountItemIdStr = accountItemMap.get("ID").toString();//账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);//账单ID
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			//String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置traceIds
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationCustomerService.getPlace(customerId);
			accountItemMap.put("place", place);//地理位置
			
			accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			accountItemMap.put("overdueValue", overdueValue.setScale(2));//违约金总金额
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，欠费金额+违约金欠款
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount.setScale(2));
			//获取贷方摘要
			String creditSubject = "";
			if(accountItemMap.get("CREDIT_SUBJECT") != null) {
				creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());

			}
			accountItemMap.put("creditSubject", creditSubject);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_INSERT_PATH + "account_item_table";
	}
	
	/**
	 * @Title: getMeterMessage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param model
	 * @param meterId
	 * @param place
	 * @return 
	 */
	@RequestMapping(value = "/get-customer-message")
	@ResponseBody
	public Object getMeterMessage(Model model, Long accountItemId) {
		CustomerAccountItem account = customerAccountItemService.selectByPrimaryKey(accountItemId);
		Customers customer = customersService.selectByPrimaryKey(account.getCustomerId());
		return customer;
	}
	
	/** 
	*	@Title: addPrinter 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(Invoice invoice) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		invoice.setInvoicePeople(userBean.getRealname());
		//invoice.setInvoiceDate(new Date());
		
		int row = invoiceService.insertSelective(invoice);
		if (row > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param engineering
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(Invoice invoice) throws Exception {
		
		int row = invoiceService.updateByPrimaryKeySelective(invoice);
		if (row > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		
		return RequestResultUtil.getResultUpdateSuccess();
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				invoiceService.deleteByPrimaryKey(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 
	 * 		为null时是管理员角色，查询所有；不为null时是工程公司角色，只查询此工程公司创建的安装工程
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;//操作员ID
		if (userBean!=null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for(SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}
			
			//indexOf() 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
			if(roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_ADMIN)==-1) {
				operatorId = userBean.getId();//操作员ID
			}
			
		}
		return operatorId;
	}

}