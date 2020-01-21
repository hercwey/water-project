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
 * @Title: CzcController.java
 * @Description: 城中村
 *
 * @author Thinkpad
 * @date 2020年1月13日 下午7:17:13
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class CzcController {
	private static Log log = LogFactory.getLog(CzcController.class);
	private static final String TEMPLATE_PATH = "statistic/yyc/czc/"; // 页面目录
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
	
	
		@RequestMapping(value = "/yyc/czc/starter")
		public String statisticFeeReceStatMain(Model model) {
			return TEMPLATE_PATH + "starter";
			
		}
		/**
		 * @Title: main
		 * @Description: 主页面
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/yyc/czc/main")
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
		@RequestMapping(value = "/yyc/czc/table")
		public String statisticFeeDebtStatTable(String searchJSON, 
												  Model model) {

			//取得查询参数
			JSONObject jsonObj=JSON.parseObject(searchJSON);
			String period=jsonObj.getString("period");
			//获取城中村小区欠费
			String priceCode = "price143";
			List<Map<String,Object>> accountItemList = statisticService.getCzcRecord(period, priceCode);
			BigDecimal totalWaterAmount = new BigDecimal("0");
			BigDecimal totalWaterFee = new BigDecimal("0");
			int count=0;
			for(Map<String,Object> map : accountItemList) {
				count = count+1;
				BigDecimal waterAmount = new BigDecimal(map.get("WATER_AMOUNT").toString());
				BigDecimal waterFee = new BigDecimal(map.get("OWE_AMOUNT").toString());
				totalWaterAmount = BigDecimalUtils.add(totalWaterAmount, waterAmount);
				totalWaterFee = BigDecimalUtils.add(totalWaterFee, waterFee);
				map.put("count", count);
			}
			
			//小区欠费数据
			model.addAttribute("period", period); 
			model.addAttribute("totalWaterFee", totalWaterFee);  		//欠费总和
			model.addAttribute("totalWaterAmount", totalWaterAmount);  
			//(1.3)传递如下数据至前台页面
			model.addAttribute("dataList", accountItemList);  		//列表数据
			//(1.4)查询相关
			// 分页数据
			
			return TEMPLATE_PATH+"table";
		}
		

	
}
