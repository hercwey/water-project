package com.learnbind.ai.controller.statistic.yyc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.statistic.yyc
 *
 * @Title: ArrearsCleanController.java
 * @Description: 清欠统计
 *
 * @author Thinkpad
 * @date 2020年1月6日 下午2:55:26
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class ArrearsCleanController {
	private static Log log = LogFactory.getLog(ArrearsCleanController.class);
	private static final String TEMPLATE_PATH = "statistic/yyc/clean/"; // 页面目录
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
	
	
	//------------------欠费统计,指定时间段内-------------------
		
		@RequestMapping(value = "/yyc/clean/main")
		public String statisticFeeReceStatMain(Model model) {
			return TEMPLATE_PATH + "statistic_clean_main";
			
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
		@RequestMapping(value = "/yyc/clean/table")
		public String statisticFeeDebtStatTable(Integer pageNum, 
												  Integer pageSize,
												  String searchCond, 
												  Model model) {
			// 判定页码有效性
			if (pageNum == null || pageNum == 0) {
				pageNum = 1;
				pageSize = PAGE_SIZE;
			}
			
			log.debug("searchCond:"+searchCond);

			//取得查询参数
			JSONObject jsonObj=JSON.parseObject(searchCond);
			String period=jsonObj.getString("period");
			//String traceIds=jsonObj.getString("traceIds");
			
			// (1.1)查询并分页
			PageHelper.startPage(pageNum, pageSize); // PageHelper
			List<Map<String,Object>> accountItemList = new ArrayList<>();	
			//查询单位用户的清欠
			Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
			accountItemList = statisticService.searchYycCleanList(period,customerType);  	//查询指定期间之前所有大表用户的清欠
			BigDecimal debtSum = statisticService.searchYycCleanTotal(period,customerType);  //获取欠费总和
			
			//查询所有小区的欠费
			List<Map<String,Object>> blockAccountItemList = statisticService.searchYycBlockCleanList(period,EnumCustomerType.CUSTOMER_PEOPLE.getValue());  	//查询指定期间之前所有小区用户的欠费
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(accountItemList); 	   	// (使用了拦截器或是AOP进行查询的再次处理)
			int count = 1;//序号
			for(Map<String,Object> map : accountItemList) {
				count = count+1;
				//剩余欠费
				BigDecimal oweAmount = new BigDecimal(map.get("BASE_WATER_FEE").toString());
				//debtSum = BigDecimalUtils.add(oweAmount, debtSum);
				Long customerId = Long.valueOf(map.get("CUSTOMER_ID").toString());
				//(1.2)查询当月清欠
				BigDecimal cleanAmount = statisticService.getCurrCleanAmount(customerId, period, EnumCustomerType.CUSTOMER_UNIT.getValue());  //获取欠费总和
				//查询欠费月份
				List<String> list = new ArrayList<>();
				String pastPeriod = map.get("PERIODS").toString();
				String[] s1 = pastPeriod.split(",");
				for (String temp : s1) {
					if(!list.contains(temp)){//判断数组中是否包含重复元素
						list.add(temp);
					}
				}
				
				//查询用水性质
				List<String> priceList = new ArrayList<>();
				String[] partWaterStr = (map.get("PART_WATER_IDS").toString()).split(",");
				for (String temp : partWaterStr) {
					PartitionWater water = partitionWaterService.selectByPrimaryKey(Long.valueOf(temp));
					SysWaterPrice price = waterPriceService.getWaterPriceByPriceCode(water.getWaterUse());
					if(!priceList.contains(price.getLadderName())){//判断数组中是否包含重复元素
						priceList.add(price.getLadderName());
					}
				}
				BigDecimal owe = BigDecimalUtils.add(cleanAmount, oweAmount);
				map.put("count", count);//序号
				map.put("owe", owe);//欠费金额
				map.put("oweAmount", oweAmount);//欠费余额
				map.put("periodList", list);//欠费月份
				map.put("cleanAmount", cleanAmount);//当月清欠
				map.put("priceList", priceList);//用水性质
			}
			
			//获取小区用户欠费
			//查询欠费月份
			//(1.2)查询小区当月清欠
			BigDecimal cleanBlockAmount = statisticService.getCurrCleanBlockAmount(EnumCustomerType.CUSTOMER_PEOPLE.getValue(), period);  //获取欠费总和
			List<String> blocklist = new ArrayList<>();
			BigDecimal totalBlockAmount = new BigDecimal("0");
			for(Map<String,Object> map : blockAccountItemList) {
				blocklist.add(map.get("PERIOD").toString());
				totalBlockAmount = BigDecimalUtils.add(totalBlockAmount, new BigDecimal(map.get("BASE_WATER_FEE").toString()));
			}
			BigDecimal oweBlock = BigDecimalUtils.add(cleanBlockAmount, totalBlockAmount);
			
			debtSum = BigDecimalUtils.add(debtSum, totalBlockAmount);
			//小区欠费数据
			model.addAttribute("blocklist", blocklist);  				
			model.addAttribute("cleanBlockAmount", cleanBlockAmount);  
			model.addAttribute("totalBlockAmount", totalBlockAmount); 
			model.addAttribute("oweBlock", oweBlock); 
			//(1.3)传递如下数据至前台页面
			model.addAttribute("dataList", accountItemList);  		//列表数据
			model.addAttribute("debtSum", debtSum);  		//列表数据
			//(1.4)查询相关
			model.addAttribute("pageInfo", pageInfo);  				//分页数据
			model.addAttribute("pageNum", pageNum);  
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"statistic_clean_table";
		}
		

	
}
