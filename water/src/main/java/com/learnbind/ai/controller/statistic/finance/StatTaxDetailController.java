package com.learnbind.ai.controller.statistic.finance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.tax.EnumTaxChbz;
import com.learnbind.ai.common.enumclass.tax.EnumTaxFpzl;
import com.learnbind.ai.common.enumclass.tax.EnumTaxZfbz;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.statclassify.StatClassifyService;
import com.learnbind.ai.service.statistic.StatisticService;
import com.learnbind.ai.service.tax.TaxInvoiceService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.statistic.finance
 *
 * @Title: StatTaxDetailController.java
 * @Description: 开票明细表
 *
 * @author Administrator
 * @date 2019年12月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-tax-detail")
public class StatTaxDetailController {
	private static Log log = LogFactory.getLog(StatTaxDetailController.class);
	private static final String TEMPLATE_PATH = "statistic/finance/taxdetail/"; // 页面目录
	
	@Autowired
	private StatClassifyService statClassifyService;
	@Autowired
	private MetersService metersService; //表计信息
	@Autowired
	private CustomerAccountItemService customerAccountItemService; //客户账单信息
	@Autowired
	private PartitionWaterService partitionWaterService; //分水量信息
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private TaxInvoiceService taxInvoiceService;//开票信息
	@Autowired
	private StatisticService statisticService;//统计
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param searchPeriod			查询期间
	 * @param searchSubjectPayment	支付方式
	 * @param periodType			期间类型：1=本期；2=往期
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String searchPeriod, String searchSubjectPayment, Integer periodType) {

		Long operatorId = this.getOperatorId();//操作员ID
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		Date periodD = new Date();
		try {
			periodD = sdf.parse(searchPeriod);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf = new SimpleDateFormat("yyyy年MM月");
		String dayStr = sdf.format(periodD);
		
		/*
		 * zyfpHjje 专用发票合计金额 
		 * zyfpHjse 专用发票合计税额 
		 * ptfpHjje 普通发票合计金额 
		 * ptfpHjse 普通发票合计税额
		 * totalAmount	开票总金额
		 */
		Map<String, Object> statTaxAmountMap = this.getStatTaxAmount(searchPeriod);//统计开票金额
		
		sdf = new SimpleDateFormat("yyyy-MM");
		String period = sdf.format(periodD);
		
		BigDecimal actualAmount = customerAccountItemService.getActualWaterFeeAmount(null, period);//应收水费金额
		BigDecimal receiveAmount = customerAccountItemService.getReceiveWaterFeeAmount(null, period);//实收水费金额
		BigDecimal oweAmount = BigDecimalUtils.subtract(actualAmount, receiveAmount);//欠费金额
		//BigDecimal oweAmount = new BigDecimal(0.00);//欠费金额
		
		BigDecimal overdueOweAmount = customerAccountItemService.getOverdueOweAmount(null, period);//本月违约金欠费金额
		oweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);//欠费金额=欠费金额+违约金欠费金额
		
		String meterType = "CARD_METER";//水表类型：卡表
		BigDecimal cardMeterInvoiceAmount = new BigDecimal(0.00);//卡表开票金额
		BigDecimal personInvoiceAmount = statisticService.getPersonCustomerTaxInvoiceAmount(period, meterType);//个人客户卡表开票金额
		BigDecimal companyInvoiceAmount = statisticService.getCompanyCustomerTaxInvoiceAmount(period, meterType);//单位客户卡表开票金额
		cardMeterInvoiceAmount = BigDecimalUtils.add(personInvoiceAmount, companyInvoiceAmount);
		
		BigDecimal totalAmount = new BigDecimal(0.00);//合计金额
		totalAmount = BigDecimalUtils.add(oweAmount, cardMeterInvoiceAmount);
		
		BigDecimal monthOtherModeIncome = new BigDecimal(0.00);//本月其他方式收入
		
		sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(periodD);
		
		BigDecimal yearDebt = new BigDecimal(0.00);//本年清欠
		yearDebt = statisticService.getYearCleanAmount(year);//查询本年清欠
		BigDecimal pastYearDebt = new BigDecimal(0.00);//往年清欠
		pastYearDebt = statisticService.getPastYearCleanAmount(year);
		
		model.addAttribute("dayStr", dayStr);
		model.addAttribute("statTaxAmountMap", statTaxAmountMap);
		model.addAttribute("monthOtherModeIncome", monthOtherModeIncome);
		model.addAttribute("cardMeterInvoiceAmount", cardMeterInvoiceAmount);
		model.addAttribute("oweAmount", oweAmount);
		model.addAttribute("overdueOweAmount", overdueOweAmount);
		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("yearDebt", yearDebt);
		model.addAttribute("pastYearDebt", pastYearDebt);
		
		return TEMPLATE_PATH + "table";
	}
	
	/**
	 * @Title: getStatTaxAmount
	 * @Description: 统计开票金额
	 * @return 
	 * 		zyfpHjje	专用发票合计金额
			zyfpHjse	专用发票合计税额
			ptfpHjje	普通发票合计金额
			ptfpHjse	普通发票合计税额
			totalAmount	开票总金额
	 */
	private Map<String, Object> getStatTaxAmount(String kprq){
		//统计专用发票金额
		Map<String, Object> zyfpAmountMap = taxInvoiceService.getStatTaxInvoiceAmount(kprq, EnumTaxFpzl.ZYFP.getKey(), EnumTaxZfbz.WZF.getValue(), EnumTaxChbz.WCH.getValue());
		BigDecimal zyfpHjje = new BigDecimal(0.00);//专用发票合计金额
		BigDecimal zyfpHjse = new BigDecimal(0.00);//专用发票合计税额
		if(zyfpAmountMap!=null) {
			BigDecimal zyfpSumHjje = (BigDecimal)zyfpAmountMap.get("SUM_HJJE");
			if(zyfpSumHjje!=null) {
				zyfpHjje = zyfpSumHjje;
			}
			BigDecimal zyfpSumHjse = (BigDecimal)zyfpAmountMap.get("SUM_HJSE");
			if(zyfpSumHjse!=null) {
				zyfpHjse = zyfpSumHjse;
			}
		}
		
		//统计普通发票金额
		Map<String, Object> ptfpAmountMap = taxInvoiceService.getStatTaxInvoiceAmount(kprq, EnumTaxFpzl.PTFP.getKey(), EnumTaxZfbz.WZF.getValue(), EnumTaxChbz.WCH.getValue());
		BigDecimal ptfpHjje = new BigDecimal(0.00);//普通发票合计金额
		BigDecimal ptfpHjse = new BigDecimal(0.00);//普通发票合计税额
		if(ptfpAmountMap!=null) {
			BigDecimal ptfpSumHjje = (BigDecimal)ptfpAmountMap.get("SUM_HJJE");
			if(ptfpSumHjje!=null) {
				ptfpHjje = ptfpSumHjje;
			}
			BigDecimal ptfpSumHjse = (BigDecimal)ptfpAmountMap.get("SUM_HJSE");
			if(ptfpSumHjse!=null) {
				ptfpHjse = ptfpSumHjse;
			}
		}
		
		BigDecimal totalAmount = new BigDecimal(0.00);//开票总金额
		totalAmount = BigDecimalUtils.add(totalAmount, zyfpHjje);
		totalAmount = BigDecimalUtils.add(totalAmount, zyfpHjse);
		totalAmount = BigDecimalUtils.add(totalAmount, ptfpHjje);
		totalAmount = BigDecimalUtils.add(totalAmount, ptfpHjse);
		
		Map<String, Object> statTaxAmountMap = new HashMap<>();
		statTaxAmountMap.put("zyfpHjje", zyfpHjje);
		statTaxAmountMap.put("zyfpHjse", zyfpHjse);
		statTaxAmountMap.put("ptfpHjje", ptfpHjje);
		statTaxAmountMap.put("ptfpHjse", ptfpHjse);
		statTaxAmountMap.put("totalAmount", totalAmount);
		return statTaxAmountMap;
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