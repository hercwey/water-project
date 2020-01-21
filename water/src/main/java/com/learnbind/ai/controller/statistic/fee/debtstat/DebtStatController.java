package com.learnbind.ai.controller.statistic.fee.debtstat;

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
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.service.statistic.StatisticService;
import com.learnbind.ai.util.pdf.PDFGenerator;
import com.learnbind.ai.util.pdf.PdfPathUtil;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic.fee.recestat
 *
 * @Title: ReceStatController.java
 * @Description: 欠费统计(查询指定时间段内欠费金额)
 *
 * @author lenovo
 * @date 2019年9月5日 上午9:43:14
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class DebtStatController {
	private static Log log = LogFactory.getLog(DebtStatController.class);
	private static final String TEMPLATE_PATH = "statistic/fee/debtstat/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	
	
	//------------------欠费统计,指定时间段内-------------------
		
		@RequestMapping(value = "/fee/debtstat/main")
		public String statisticFeeReceStatMain(Model model) {
			return TEMPLATE_PATH + "statistic_fee_debtstat_main";
			
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
		@RequestMapping(value = "/fee/debtstat/table")
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
			String periodStart=jsonObj.getString("periodStart");
			String periodEnd=jsonObj.getString("periodEnd");
			String traceIds=jsonObj.getString("traceIds");
			
			// (1.1)查询并分页
			PageHelper.startPage(pageNum, pageSize); // PageHelper
			List<Map<String,Object>> accountItemList = new ArrayList<>();			
			accountItemList=statisticService.searchDebtDetailList(periodStart,periodEnd, traceIds);  	//查询指定期间欠费客户数据列表			
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(accountItemList); 	   	// (使用了拦截器或是AOP进行查询的再次处理)
			
			//(1.2)查询指定期期间的欠费总和
			BigDecimal  debtSum=calcDebtSum(periodStart,periodEnd, traceIds);
		
			//(1.3)传递如下数据至前台页面
			model.addAttribute("dataList", accountItemList);  		//列表数据
			model.addAttribute("debtSum",debtSum);
			
			//(1.4)查询相关
			model.addAttribute("pageInfo", pageInfo);  				//分页数据
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"statistic_fee_debtstat_table";
		}
		
		
		
		/**
		 * @Title: calcDebtSum
		 * @Description: 计算指定期间的欠费金额
		 * @param periodStart  开始期间  yyyy-mm
		 * @param periodEnd		结束期间  yyyy-mm
		 * @return 
		 * 		如果没有统计值,则返回0,否则返回统计结果(求和)
		 */
		private BigDecimal calcDebtSum(String periodStart,String periodEnd, String traceIds) {
			List<Map<String,Object>> debtSumList=statisticService.statDebtSum(periodStart,periodEnd, traceIds);  //统计指定期间欠费总和			
			BigDecimal debtSum=new BigDecimal(0);			
			Map<String,Object> tempSum=debtSumList.get(0);
			if(tempSum!=null) {
				debtSum=(BigDecimal)tempSum.get("DEBT_AMOUNT_SUM");
			}			
			return debtSum;
		}
		
		/**
		 * @Title: statisticFeeDebtStatPDF
		 * @Description: PDF生成预览
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/fee/debtstat/previewprepare")
		@ResponseBody
		public String statisticFeeDebtStatPDF(Model model) {
			final String TEMPLATE_FILE_NAME="testtemplate";  //模板名称.（单位）
			final String TEMPLATE_PREFIX="templates/statistic/pdftemplate/";	//模板文件所在的目录			
			
			String file_dir = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());  //TODO //PDF所在			
			String pdfFileName = createReportPDF(file_dir,TEMPLATE_PREFIX, TEMPLATE_FILE_NAME);			
			return pdfFileName;
		}
		
		
		public String createReportPDF(String FILE_DIR,String TEMPLATE_PREFIX,String templateFileName) {
			//(2)封装上下文数据
			/*
			 * Map<String, Object> contextData = new HashMap<>();
			 * contextData.put("textData", recordMapList); contextData.put("locationName",
			 * locationName); contextData.put("period", period);
			 */
			
			//(3)水费通知单PDF
			//(3.1)模板文件
			//final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
			
			//(3.2)PDF文件名称
			String resultPDFFileName=genFileName("report_");   //不带路径的文件名称
			String pdfFileName = buildPDFFileName(FILE_DIR,resultPDFFileName);  //带路径的文件名称,用于生成PDF
			
			try {
				//(3.3)生成PDF
				PDFGenerator gen = new PDFGenerator(TEMPLATE_PREFIX, ".html");  //模板文件所在目录
				gen.generate(new File(pdfFileName), templateFileName, null);
			} catch (DocumentException | IOException e) {				
				e.printStackTrace();
			}

			return resultPDFFileName;
		}
		
		/**
		 * @Title: genFileName
		 * @Description: 生成文件名称
		 * @param preFix  文件名前缀
		 * @return 
		 * 			prefix+yyyyMMddHHmmss
		 * 			report20190809.pdf
		 * 		    "report"是前缀部分.
		 * 			"20190809"日期部分  (年月日时分秒毫秒)
		 */
		private String genFileName(String preFix) {
			final String FILE_EXT=".pdf";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	        String fileNameSuffix=dateFormat.format(new Date());
	        String fileName=preFix+fileNameSuffix+FILE_EXT;
	        return fileName;
		}
		
		
		//生成PDF文件名
		private String buildPDFFileName(String filePath,String pdfFileName) {
			String fileName=filePath+pdfFileName;
			
			File path =new File(filePath);    
			//如果文件夹不存在则创建    
			if (!path.exists() && !path.isDirectory()) {
				boolean flag = path.mkdirs();
				System.out.println("目录不存在   创建目录: "+path+" 结果："+flag);
			} else {
				System.out.println("目录存在 : "+path);
			}
			
			return fileName;
		}
		
			
		
		/**
		 * @Title: statisticWaterMonthAmountChart
		 * @Description: 月售水量-统计   (按每天的售水量进行统计)   柱状图
		 * @param period  期间  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/fee/debtstat/chart")
		public String statisticFeeDebtStatChart(String period, Model model) {			
			List<String> dayList = genXAxisDataMonth(period); // 天列表 x轴
			List<Integer> numPerDayList = null;//statWaterAmountPerDay(period); // 每天所售水量列表 y轴
			Map<String, Object> statData = packStatData(dayList, numPerDayList); // 封装统计数据

			model.addAttribute("statData", statData); // 统计数据
			model.addAttribute("period", period); // 期间

			//List<Map<String, Object>> testData = statisticService.test();

			return TEMPLATE_PATH + "statistic_water_month_amount_bar";
			
		}
		
		@RequestMapping(value="/fee/debtstat/exportexcel")
		public void testExeclExport(Model model,HttpServletRequest request,HttpServletResponse response) {
			String period = DateUtils.getYearMonth();//期间默认本月
			
			//excel标题
			String[] titles = { "房间号", "客户姓名", "期间", "上期抄表日期", "上期抄表读数", "本期抄表日期", "本期抄表读数", "本期水量", "抄表方式", "抄表结果", "抄表员", "备注"};
			//excel列名
			String[] columnNames = { "room", "customerName", "period", "preDateStr", "preRead", "currDateStr", "currRead", "currAmount", "readModeStr", "readResultStr", "operatorName", "remark"};
			//sheet名
			String sheetName = period+"抄表记录";
			
			//获取导出EXCEL的数据
			List<Map<String, Object>> excelDataList = new ArrayList<>();
			Map<String,Object> tempMap=new HashMap<>();
			tempMap.put("room", "11-11-11");
			excelDataList.add(tempMap);
			
			
			//获取导出EXCEL的工作簿
			HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
			//获取导出EXCEL的文件路径
			String realPath = this.getRealPath(request);
			//获取导出EXCEL的文件名
			String fileName = this.genFileName(period, null);
			
			File file = new File(realPath+fileName);
			
			//文件输出流
		    FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		    try {
				wb.write(outStream);
				outStream.flush();
				outStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
		    System.out.println("导出2007文件成功！文件导出路径：--"+file);
		    
		    try {
				DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		
		/**
		 * 获取文件路径
		 * @param request
		 * @return
		 */
		private String getRealPath(HttpServletRequest request){
			String realPath = this.getPath();
			realPath = realPath+File.separator+"export excel"+File.separator;
			
			File temp = new File(realPath);
			//如果文件路径不存在，则创建
			if(!temp.exists()){
				temp.mkdirs();
			}
			return realPath;
		}
		
		/**
		 * 获取导出EXCEL文件名
		 * @return
		 */
		private String genFileName(String period, String locationCode){
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String times = sdf.format(new Date());
			
			// excel文件名
			String fileName = "如园小区3号楼";
			if(StringUtils.isNotBlank(period)) {
				fileName = fileName+"-"+period;
			}
			if(StringUtils.isNotBlank(locationCode)) {
				fileName = fileName+"-"+locationCode;
			}
			fileName = fileName+"-"+times+".xls";
			
			return fileName;
		}
		/**
		 * @Title: getPath
		 * @Description: 根据操作系统类型获取上传文件目录
		 * @return 
		 */
		private String getPath() {
			String path = uploadFileConfig.getLinuxUploadFolder();
			if(this.isWindowsOS()) {
				path = uploadFileConfig.getWindowsUploadFolder();
			}
			System.out.println("----------"+path);
			return path;
		}
		/**
		 * @Title: isWindowsOS
		 * @Description: 判断操作系统是否是Windows
		 * @return 
		 * 		true=windows;false=其他
		 */
		private boolean isWindowsOS() {
			String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
	    	if(os.toUpperCase().startsWith("WIN")){  
	    		System.out.println("==============================操作系统："+os);
	    		return true;
	    	}else {
	    		System.out.println("==============================操作系统："+os);
	    		return false;
	    	}
		}
		
		
		
		/**
		 * @Title: statWaterAmountPerDay
		 * @Description: 统计指定月份每天售水量.
		 * @param period 期间  yyyy-mm
		 * @return 天--->水量
		 */
		private List<Integer> statWaterAmountPerDay(String traceIds, String period) {
			String datex = period + "-01";
			int daysPerMonth = StatisticUtil.getDaysOfMonth(datex);
			
			//初始化每天数值列表
			List<Integer> numPerDayList = new ArrayList<>();
			for (int i = 0; i < daysPerMonth; i++) {
				numPerDayList.add(0);
			}
			
			//TODO 统计每天的售水量
			//自数据库中统计指定期间新增客户: 天--->售水量
			List<Map<String,Object>> statList=statisticService.statWaterAmountPerDay(traceIds, period);
			for(Map<String,Object> map:statList) {
				Date settleTime=(Date)map.get("DAY_DATE");   //格式yyyy-mm-dd
				int amount=((BigDecimal)map.get("WATER_AMOUNT")).intValue();
				
				//取出"dd-日"部分,以其为索引置新增人数
				int dayx=DateUtil.get(settleTime, "day");
				numPerDayList.set(dayx-1, amount);
			}
			
			return numPerDayList;
		}
		
		/**
		 * @Title: packStatData
		 * @Description: 封装统计数据
		 * @param xAxisData
		 * @param yAxisData
		 * @return statisticData 统计数据 格式 { periodList:[], //x轴: numPerPeriodList:[]
		 *         //y轴:数据列表 与时间一一对应 }
		 */
		private Map<String, Object> packStatData(List<String> xAxisData, List<Integer> yAxisData) {
			final String X_AXIS = "periodList";
			final String Y_AXIS = "numPerPeriodList";
			Map<String, Object> statData = new HashMap<>();
			statData.put("periodList", xAxisData); // 时间列表 x轴
			statData.put("numPerPeriodList", yAxisData); // 数据列表 y轴
			return statData;
		}
		
		/**
		 * @Title: genXAxisDataMonth
		 * @Description: 生成x轴数据(天/月)
		 * @param daysPerMonth 天数
		 * @return 天列表 1,2,3,4,...31
		 */
		private List<String> genXAxisDataMonth(String period) {
			String datex = period + "-01";
			int daysPerMonth = StatisticUtil.getDaysOfMonth(datex);
			List<String> periodList = new ArrayList<>();
			for (int i = 0; i < daysPerMonth; i++) {
				periodList.add(Integer.toString(i + 1));
			}
			return periodList;
		}

		/**
		 * @Title: genXAxisDataYear
		 * @Description:年月份列表
		 * @return 1,2,3,...12
		 */
		private List<String> genXAxisDataYear() {
			List<String> periodList = new ArrayList<>();
			for (int i = 0; i < 12; i++) {
				periodList.add(Integer.toString(i + 1) + "月");
			}
			return periodList;
		}
	
}
