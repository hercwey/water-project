package com.learnbind.ai.controller.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.DiscountWaterFeeTraceService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.sms.SMSService;

@Controller
@RequestMapping(value = "/export-test")
public class ExportExcelBillController {
	
	private static Log log = LogFactory.getLog(ExportExcelBillController.class);
	private static final String TEMPLATE_PATH = "bill/"; // 页面目录
	private static final String TEMPLATE_PATH_BILL = "bill/insert_bill/"; // 增加账单
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService; // 客户信息
	@Autowired
	private CustomerAccountService customerAccountService;// 客户-账户服务
	@Autowired
	private CustomerOverdueFineService customerOverdueFineService;// 客户-违约金
	@Autowired
	private DiscountFineTraceService discountFineTraceService;// 客户-违约金减免日志
	@Autowired
	private DiscountWaterFeeTraceService discountWaterFeeTraceService;// 客户-水费减免日志
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户
	@Autowired
	private BankService bankService;//客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计绑定关系表
	@Autowired
	private SMSService smsService;//发送短信
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量
	@Autowired
	private WaterPriceService waterPriceService;//用水性质

	
	//-------------------------------导出账单excel---------------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/load-export-excel-dialog")
	public String loadExportExcelDialog(Model model) {
		
		
		List<Map<String, String>> mapList = getAllExcelColumn();
		List<Map<String, Object>> customerMapList = new ArrayList<>();
//		for (CustomerAccountItem caItem : subAccountItemList) {
//
//			Map<String, Object> accountItemMap = EntityUtils.entityToMap(caItem);
//
//			Customers customers = customersService.selectByPrimaryKey(caItem.getCustomerId());
//			accountItemMap.put("customerName", customers.getCustomerName());
//			accountItemMap.put("creditAmount", caItem.getCreditAmount());
//			accountItemMap.put("creditDigest", caItem.getCreditDigest());
//			accountItemMap.put("creditSubject", caItem.getCreditSubject());
//			accountItemMap.put("debitCredit", caItem.getDebitCredit());
//			customerMapList.add(accountItemMap);
//
//		}
//
//		model.addAttribute("customerList", customerMapList);
//		model.addAttribute("currItem", currItem);
		return TEMPLATE_PATH + "cancel_sub_account_dialog";
	}
	/**
	 * @Title: getAllExcelColumn
	 * @Description: 获取导出所有列名
	 * @return 
	 */
	private List<Map<String, String>> getAllExcelColumn(){
		
//			客户名称
//			期间
//			账单金额
//			已结算金额
//			欠费金额
		
		List<Map<String, String>> columnList = new ArrayList<>();
		Map<String, String> map1 = new HashMap<>();
		map1.put("columnCode", "CUSTOMER_NAME");
		map1.put("columnName", "客户名称");
		columnList.add(map1);
		
		Map<String, String> map2 = new HashMap<>();
		map2.put("columnCode", "PERIOD");
		map2.put("columnName", "期间");
		columnList.add(map2);
		
		Map<String, String> map3 = new HashMap<>();
		map3.put("columnCode", "CREDIT_AMOUNT");
		map3.put("columnName", "账单金额");
		columnList.add(map3);
		
		Map<String, String> map4 = new HashMap<>();
		map4.put("columnCode", "DEBIT_AMOUNT");
		map4.put("columnName", "已结算金额");
		columnList.add(map4);
		
		Map<String, String> map5 = new HashMap<>();
		map5.put("columnCode", "OWE_AMOUNT");
		map5.put("columnName", "欠费金额");
		columnList.add(map5);
		
		return columnList;
	}
	
//	@RequestMapping(value = "/export-bill-excel")
//	@ResponseBody
//	public Object exportBillExcel(HttpServletRequest request, Model model, String exportColumnJSON) {
//		
//		try {
//			
//			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型 0
//			
//			//查询账单
//			CustomerAccountItem item = customerAccountItemService.selectByPrimaryKey(accountItemId);
//			//计算账单欠费金额
//			BigDecimal oweAmount = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
//			
//			//如果欠费金额>0，则发送短信通知
//			if(BigDecimalUtils.greaterThan(oweAmount, zero)) {
//				//发送短信通知
//				//查询客户信息
//				Customers customer = customersService.selectByPrimaryKey(item.getCustomerId());
//				
//				//客户手机号
//				String mobileNo = customer.getPropMobile();
//				if(StringUtils.isBlank(mobileNo)) {
//					mobileNo = customer.getCustomerMobile();
//				}
//				//验证发送短信的手机号码是否为空
//				if(StringUtils.isBlank(mobileNo)) {
//					return RequestResultUtil.getResultFail("客户手机号为空！");
//				}
//				
//				//指定模板所对应的参数值
//				List<String> tpl_parms = new ArrayList<>();
//				//客户姓名
//				String customerName = customer.getPropName();
//				if(StringUtils.isBlank(customerName)) {
//					customerName = customer.getCustomerName();
//				}
//				if(StringUtils.isNotBlank(customerName)) {
//					tpl_parms.add(customerName);
//				}
//				
//				String period = item.getPeriod();//期间
//				if(StringUtils.isNotBlank(period)) {
//					tpl_parms.add(period);
//				}
//				tpl_parms.add(oweAmount.toString());//账单金额
//				
//				//如果手机号不为空，且模版参数正确，则发送短信
//				if(StringUtils.isNotBlank(mobileNo) && tpl_parms.size()==3) {
//					String retJSON = smsService.sendSingleSMS(mobileNo, SMSConstants.SMS_TEMPLATE_ID_FEE, tpl_parms);
//					if(StringUtils.isNotBlank(retJSON)) {
//						SendSingleSMResponse ret = JSON.parseObject(retJSON, SendSingleSMResponse.class);
//						if(ret.getResult()==0) {
//							return RequestResultUtil.getResultSuccess("发送短信通知成功！");
//						}else {
//							return RequestResultUtil.getResultFail("发送短信通知失败，原因："+ret.getErrmsg());
//						}
//					}else {
//						return RequestResultUtil.getResultFail("发送短信通知失败，返回信息为空！");
//					}
//				}else {
//					return RequestResultUtil.getResultFail("缺少参数！");
//				}
//				
//			}else {
//				return RequestResultUtil.getResultFail("此账单已全部结算，不需要发送短信通知！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JSON.toJSONString(RequestResultUtil.getResultFail("上传CMBC文本文件销账异常！请重试！"));
//	}
	
}