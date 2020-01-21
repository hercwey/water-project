package com.learnbind.ai.controller.statistic.finance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.statclassify.StatClassifyService;
import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.statistic.finance
 *
 * @Title: StatWaterFeeIncomeController.java
 * @Description: 水费收缴情况表
 *
 * @author Administrator
 * @date 2019年12月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-water-fee-income")
public class StatWaterFeeIncomeController {
	private static Log log = LogFactory.getLog(StatWaterFeeIncomeController.class);
	private static final String TEMPLATE_PATH = "statistic/finance/waterfeeincome/"; // 页面目录
	
	@Autowired
	private StatClassifyService statClassifyService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private LocationService locationService;//地理位置
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
	public String table(Model model, String searchPeriod) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date periodD = new Date();
		try {
			periodD = sdf.parse(searchPeriod);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long operatorId = this.getOperatorId();//操作员ID
		
		BigDecimal actualAmount = statisticService.getActualWaterFeeAmount(null, searchPeriod);//应收水费金额
		BigDecimal receiveAmount = statisticService.getReceiveWaterFeeAmount(null, searchPeriod);//实收水费金额
		BigDecimal oweAmount = BigDecimalUtils.subtract(actualAmount, receiveAmount);//欠费金额
		//计算实收率
		BigDecimal receiveRate = new BigDecimal(0.00);
		if(BigDecimalUtils.greaterThan(actualAmount, new BigDecimal(0.00))) {
			receiveRate = BigDecimalUtils.divide(receiveAmount, actualAmount, 4);
			receiveRate = BigDecimalUtils.multiply(receiveRate, new BigDecimal(100));
		}
		
		BigDecimal monthDebt = new BigDecimal(0.00);//本月清欠
		monthDebt = statisticService.getCurrCleanAmount(null, searchPeriod, null);//查询本月清欠
		
		sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(periodD);
		BigDecimal pastYearDebt = new BigDecimal(0.00);//往年清欠
		pastYearDebt = statisticService.getPastYearCleanAmount(year);
		
		sdf = new SimpleDateFormat("yyyy年MM月");
		String periodStr = sdf.format(periodD);
		
		//返回页面的参数
		model.addAttribute("period", periodStr);
		model.addAttribute("actualAmount", actualAmount);
		model.addAttribute("receiveAmount", receiveAmount);
		model.addAttribute("oweAmount", oweAmount);
		model.addAttribute("monthDebt", monthDebt);
		model.addAttribute("pastYearDebt", pastYearDebt);
		model.addAttribute("receiveRate", receiveRate);
		
		return TEMPLATE_PATH + "table";
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