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
 * @Title: PastArrearsRecordController.java
 * @Description: 拖欠水费用户登记表（往年欠费）
 *
 * @author Thinkpad
 * @date 2020年1月6日 下午4:34:58
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class PastArrearsRecordController {
	private static Log log = LogFactory.getLog(PastArrearsRecordController.class);
	private static final String TEMPLATE_PATH = "statistic/yyc/record/"; // 页面目录
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
	
	
		@RequestMapping(value = "/yyc/record/starter")
		public String statisticFeeReceStatMain(Model model) {
			return TEMPLATE_PATH + "starter";
			
		}
		/**
		 * @Title: main
		 * @Description: 主页面
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/yyc/record/main")
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
		@RequestMapping(value = "/yyc/record/table")
		public String statisticFeeDebtStatTable(String searchJSON, 
												  Model model) {

			//取得查询参数
			JSONObject jsonObj=JSON.parseObject(searchJSON);
			Integer pageNum = jsonObj.getInteger("pageNum");
			Integer pageSize = jsonObj.getInteger("pageSize");
			String period=jsonObj.getString("period");
			String searchCond=jsonObj.getString("searchCond");
			// 判定页码有效性
			if (pageNum == null || pageNum == 0) {
				pageNum = 1;
				pageSize = PAGE_SIZE;
			}
			// (1.1)查询并分页
			PageHelper.startPage(pageNum, pageSize); // PageHelper
			//获取往年拖欠水费用户登记
			List<Map<String,Object>> accountItemList = statisticService.getPastArrearsRecord(period, searchCond);
			//获取往年拖欠水费总和
			BigDecimal debtSum = statisticService.getPastArrearsRecordTotal(period,searchCond);  //获取欠费总和
			
			//查询所有小区的欠费
			List<Map<String,Object>> blockAccountItemList = statisticService.searchYycBlockRecordList(period,EnumCustomerType.CUSTOMER_PEOPLE.getValue());  	//查询指定期间之前所有小区用户的欠费
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(accountItemList); 	   	// (使用了拦截器或是AOP进行查询的再次处理)
			for(Map<String,Object> map : accountItemList) {
				BigDecimal oweAmount = new BigDecimal(map.get("OWE_AMOUNT").toString());
				
				//查询欠费月份
				List<String> list = new ArrayList<>();
				String pastPeriod = map.get("PERIODS").toString();
				String[] s1 = pastPeriod.split(",");
				for (String temp : s1) {
					if(!list.contains(temp)){//判断数组中是否包含重复元素
						list.add(temp);
					}
				}
				
				map.put("periodList", list);//欠费月份
				map.put("oweAmount", oweAmount);//累计欠费
			}
			
			//获取小区用户欠费
			//查询欠费月份
			List<String> blocklist = new ArrayList<>();
			BigDecimal totalBlockAmount = new BigDecimal("0");
			for(Map<String,Object> map : blockAccountItemList) {
				blocklist.add(map.get("PERIOD").toString());
				totalBlockAmount = BigDecimalUtils.add(totalBlockAmount, new BigDecimal(map.get("BASE_WATER_FEE").toString()));
			}
			debtSum = BigDecimalUtils.add(debtSum, totalBlockAmount);
			//小区欠费数据
			model.addAttribute("blocklist", blocklist);  				
			model.addAttribute("totalBlockAmount", totalBlockAmount); 
			model.addAttribute("period", period); 
			model.addAttribute("debtSum", debtSum);  		//欠费总和
			//(1.3)传递如下数据至前台页面
			model.addAttribute("dataList", accountItemList);  		//列表数据
			//(1.4)查询相关
			// 分页数据
			model.addAttribute("pageInfo", pageInfo);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"table";
		}
		

	
}
