package com.learnbind.ai.controller.statistic.yyc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.DocumentException;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.StatisticUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.statistic.StatisticService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.util.pdf.PDFGenerator;
import com.learnbind.ai.util.pdf.PdfPathUtil;

import tk.mybatis.mapper.entity.Example;
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.statistic.yyc
 *
 * @Title: WaterFeeCollectController.java
 * @Description: 水费收缴情况
 *
 * @author Thinkpad
 * @date 2020年1月6日 下午4:30:19
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class WaterFeeCollectController {
	private static Log log = LogFactory.getLog(WaterFeeCollectController.class);
	private static final String TEMPLATE_PATH = "statistic/yyc/collect/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量信息
	@Autowired
	private WaterPriceService waterPriceService;//水价信息
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	
	
		
	@RequestMapping(value = "/yyc/collect/starter")
	public String statisticFeeReceStatMain(Model model) {
		return TEMPLATE_PATH + "starter";
		
	}
	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/yyc/collect/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "main";
	}
	
	/**
	 * @Title: statisticFeeDebtStatTable
	 * @Description: 		查询-指定期间的欠费数据列表
	 * @param pageNum  		页号
	 * @param pageSize 		页大小
	 * @param searchCond   查询条件   JSON格式
	 * 		{
	 * 			periodStart:  yyyy-mm
	 * 			periodEnd:  yyyy-mm
	 * 		}
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/yyc/collect/table")
	public String statisticFeeDebtStatTable(String searchJSON, 
											  Model model) {
		BigDecimal zero= new BigDecimal("0");
		//取得查询参数
		JSONObject jsonObj=JSON.parseObject(searchJSON);
		String period=jsonObj.getString("period");
		String searchCond=jsonObj.getString("searchCond");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String periodStr = "";
		try {
			Date periodD = sdf.parse(period);
			sdf = new SimpleDateFormat("yyyy年MM月");
			periodStr = sdf.format(periodD);
		} catch (ParseException e) {
			e.printStackTrace();
			
		}	
		BigDecimal totalWaterAmount = zero;//售水总量
		BigDecimal totalCreditAmount = zero;//应交水费总量
		BigDecimal totalDebitAmount = zero;//实缴水费总量
		BigDecimal totalOweAmount = zero;//欠费金额总量
		// (1.1)查询并分页
		List<Map<String,Object>> accountItemList = statisticService.getWaterFeeCollect(period);	
		for(Map<String,Object> map : accountItemList) {
			
			totalWaterAmount = BigDecimalUtils.add(new BigDecimal(map.get("SUM_REAL_WATER_AMOUNT").toString()), totalWaterAmount);
			totalCreditAmount = BigDecimalUtils.add(new BigDecimal(map.get("SUM_CREDIT_AMOUNT").toString()), totalCreditAmount);
			totalDebitAmount = BigDecimalUtils.add(new BigDecimal(map.get("SUM_DEBIT_AMOUNT").toString()), totalWaterAmount);
			totalOweAmount = BigDecimalUtils.add(new BigDecimal(map.get("SUM_OWE_AMOUNT").toString()), totalWaterAmount);
		}
		//获取免费水用量
		BigDecimal freeWaterAmount = this.getfreeWaterAmount(period);
		//获取电厂水量
		BigDecimal powerPlantAmount = this.getOtherWaterAmount(57233l, period);
		//获取维生药业水量
		BigDecimal wsyyAmount = this.getOtherWaterAmount(57093l, period);
		//获取清欠总额
		BigDecimal totalCleanAmount = statisticService.getCurrCleanBlockAmount(null, period);
		//获取当月总量
		BigDecimal totalMonthAmount = BigDecimalUtils.add(totalCleanAmount, totalDebitAmount);
		model.addAttribute("period", periodStr);  	
		//实收率
		BigDecimal actualRate = zero;
		if(!BigDecimalUtils.equals(zero, totalCreditAmount)) {
			actualRate = BigDecimalUtils.divide(totalMonthAmount, totalCreditAmount, 2);
		}
		actualRate = BigDecimalUtils.multiply(actualRate, new BigDecimal("100"));
		
		model.addAttribute("freeWaterAmount", freeWaterAmount); 
		model.addAttribute("powerPlantAmount", powerPlantAmount); 
		model.addAttribute("wsyyAmount", wsyyAmount); 
		model.addAttribute("totalWaterAmount", totalWaterAmount); 
		model.addAttribute("totalCreditAmount", totalCreditAmount); 
		model.addAttribute("totalDebitAmount", totalDebitAmount); 
		model.addAttribute("totalOweAmount", totalOweAmount); 
		model.addAttribute("totalCleanAmount", totalCleanAmount); 
		model.addAttribute("totalMonthAmount", totalMonthAmount); 
		model.addAttribute("actualRate", actualRate); 
		//(1.3)传递如下数据至前台页面
		model.addAttribute("dataList", accountItemList);  		//列表数据
		//(1.4)查询相关
		model.addAttribute("searchCond", searchCond);  			//查询条件回传
		
		return TEMPLATE_PATH+"table";
	}
		

	/**
	 * @Title: getfreeWaterAmount
	 * @Description: 获取其他用水水量（免费用水）
	 * @return 
	 */
	private BigDecimal getfreeWaterAmount(String period) {
		BigDecimal freeWaterAmount = new BigDecimal("0");
		SysWaterPrice waterPrice = waterPriceService.getFreeWater();
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("period", period).
			andEqualTo("waterUse", waterPrice.getPriceCode());
		
		List<PartitionWater> waterList = partitionWaterService.selectByExample(example);
		for(PartitionWater water : waterList) {
			freeWaterAmount = BigDecimalUtils.add(freeWaterAmount, water.getRealWaterAmount());
		}
		return freeWaterAmount;
	} 
	
	/**
	 * @Title: getOtherWaterAmount
	 * @Description: 获取电厂维生药业水量
	 * @param customerId
	 * @param period
	 * @return 
	 */
	private BigDecimal getOtherWaterAmount(Long customerId, String period) {
		BigDecimal waterAmount = new BigDecimal("0");
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("period", period).
			andEqualTo("customerId", customerId);
		List<PartitionWater> waterList = partitionWaterService.selectByExample(example);
		for(PartitionWater water : waterList) {
			waterAmount = BigDecimalUtils.add(waterAmount, water.getRealWaterAmount());
		}
		return waterAmount;
	}
	
	
	
}
