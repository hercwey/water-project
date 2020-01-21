package com.learnbind.ai.controller.notify;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.DocumentException;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.model.WaterNotify;
import com.learnbind.ai.model.WaterNotifyDetail;
import com.learnbind.ai.service.customers.BillService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.notify.NotifyGroupService;
import com.learnbind.ai.service.notify.WaterNotifyDetailService;
import com.learnbind.ai.service.notify.WaterNotifyService;
import com.learnbind.ai.service.printer.PrinterService;
import com.learnbind.ai.util.pdf.CreateLabel;
import com.learnbind.ai.util.pdf.PdfPathUtil;
import com.learnbind.ai.util.pdf.PrintFile;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.notify
 *
 * @Title: NotifyPrintController.java
 * @Description: 打印水费通知单
 *
 * @author Thinkpad
 * @date 2019年12月10日 上午7:18:35
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/notify-print")
public class NotifyPrintController {

	private static Log log = LogFactory.getLog(NotifyPrintController.class);
	private static final String TEMPLATE_PATH = "notify/print/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小


	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系表
	@Autowired
	private CustomersService customersService;// 客户
	@Autowired
	private NotifyGroupService notifyGroupService;// 水费通知单分组
	@Autowired
	private CustomerAccountItemService customerAccountItemService;// 客户账单
	@Autowired
	private WaterNotifyService waterNotifyService;// 水费通知单实体
	@Autowired
	private WaterNotifyDetailService waterNotifyDetailService;// 水费通知单详情实体
	@Autowired
	private PrinterService printConfigService;// 打印机配置
	@Autowired
	private PrintFile printFile;//打印机服务
	@Autowired
	private CreateLabel createLabel;
	@Autowired
	private BillService billService;// 开票信息
	@Autowired
	private UploadFileConfig uploadFileConfig;// 文件上传配置信息
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "print_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "print_main";
	}
	

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond,String period, Integer sortMethod, Integer arrearsAmount) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<WaterNotify> newList = new ArrayList<>();
		if(StringUtils.isBlank(traceIds)) {//如果没有选择地理位置查询
			newList = waterNotifyService.searchCond(searchCond, period, sortMethod, arrearsAmount);
		} else {
			newList = waterNotifyService.getNotifyMeterIdList(period ,searchCond, traceIds, sortMethod, arrearsAmount);
			
		}
		
		PageInfo<List<WaterNotify>> pageInfo = new PageInfo(newList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(WaterNotify notify : newList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(notify);
			List<WaterNotifyDetail> detailList = waterNotifyDetailService.getNotifyDetail(notify.getId());
			Customers customer = customersService.selectByPrimaryKey(notify.getCustomerId());
			customerMap.put("customerName", customer.getCustomerName());
			customerMap.put("createDateStr", notify.getCreateDateStr());
			customerMap.put("detailList", detailList);
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("customerMapList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "print_table";
	}
	
	@RequestMapping(value = "/loadPreviewDialog")
	public String loadPreviewDialog(Model model) {
		return TEMPLATE_PATH + "preview_dialog";
	}
	
	
	/**
	 * @Title: loadEditRemarkDialog
	 * @Description: 打开备注对话框
	 * @param model
	 * @param itemId
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-remark-dialog")
	public String loadEditRemarkDialog(Model model, Long itemId) {
		WaterNotify notify = waterNotifyService.selectByPrimaryKey(itemId);
		model.addAttribute("itemId", itemId);
		model.addAttribute("notify", notify);
		return TEMPLATE_PATH + "edit_remark_dialog";
	}
	
	/**
	 * @Title: saveRemrak
	 * @Description: 保存备注
	 * @param model
	 * @param itemId
	 * @param remark
	 * @return 
	 */
	@RequestMapping(value = "/save-remark")
	@ResponseBody
	public Object saveRemrak(Model model, Long itemId, String remark) {
		WaterNotify notify = waterNotifyService.selectByPrimaryKey(itemId);
		notify.setRemark(remark);
		int rows = waterNotifyService.updateByPrimaryKeySelective(notify);
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess("修改成功！");
		}
		return RequestResultUtil.getResultUpdateWarn("修改失败！");
	}
	
	
	/**
	 * @Title: previewCustomerPrepare
	 * @Description: 预览通知单-客户选项卡
	 * @param model
	 * @param customerId
	 * @param period
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/previewNotifyPrepare")
	@ResponseBody
	public Object previewNotifyPrepare(Long notifyId) throws Exception {
		final String TEMPLATE_FILE_NAME = "A5Template"; // 模板名称.单位
		
		final String TEMPLATE_PREFIX = "templates/notify/print_template/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在
		int rows = waterNotifyService.previewNotifyPrepare(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,notifyId);
		
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess("预览成功！");
		}

		return RequestResultUtil.getResultUpdateWarn("预览失败！");
	}
	
	/**
	 * @Title: loadPrinterDialog
	 * @Description: 通知单打印对话框
	 * @param model
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/loadPrinterDialog")
	public String loadPrinterDialog(Model model, Long notifyId) {
		final String VIEW_NAME = "print_dialog"; // 视图名称
		// 查询打印机配置. 并加入到视图中.
		List<PrinterConfig> printerConfigList = printConfigService.selectAll();
		model.addAttribute("printerConfigList", printerConfigList);
		model.addAttribute("notifyId", notifyId);
		return TEMPLATE_PATH + VIEW_NAME;
	}
	
	
	/**
	 * @Title: printNotifySingle
	 * @Description: 单个打印水费通知单
	 * @param customerId
	 * @param period
	 * @return 
	 * @throws PrinterException 
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printNotifySingle", produces = "application/json")
	@ResponseBody
	public Object printNotifySingle(Long notifyId, Integer paperSize, Long printerId, String period, String serialNo, String noticeDate, String chargePeople) throws DocumentException, IOException, PrinterException, ParseException {
		final String TEMPLATE_FILE_NAME = "A5Template"; // 模板名称.单位
		String templateFileName="";  //模板名称.
		if(paperSize == 1) {//选择的是A4
			templateFileName = "A4Template";
		} else if (paperSize == 2){//选择的是A5
			templateFileName = "A5Template";
		}
		final String TEMPLATE_PREFIX = "templates/notify/print_template/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		int rows = waterNotifyService.printNotifySingle(FILE_DIR, TEMPLATE_PREFIX, notifyId, templateFileName, printerId, noticeDate, chargePeople);
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess("打印通知单成功！");
		}
		return RequestResultUtil.getResultUpdateWarn("打印通知单失败！");
	}
	
	/**
	 * @Title: generateNotifyBatch
	 * @Description: 批量生成通知单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param customerIdStr
	 * @return 
	 * @throws PrinterException 
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printNotifyBatch", produces = "application/json")
	@ResponseBody
	public Object printNotifyBatch(String searchCond, String traceIds,  String period,String notifyIdArr, Integer paperSize, Long printerId, String noticeDate, String chargePeople, Integer sortMethod, Integer arrearsAmount) throws DocumentException, IOException, PrinterException, ParseException {
		final String TEMPLATE_FILE_NAME = "A5Template"; // 模板名称.单位
		String templateFileName="";  //模板名称.
		if(paperSize == 1) {//选择的是A4
			templateFileName = "A4Template";
		} else if (paperSize == 2){//选择的是A5
			templateFileName = "A5Template";
		}
		final String TEMPLATE_PREFIX = "templates/notify/print_template/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在
		int rows = waterNotifyService.generateNotifyBatch(FILE_DIR, TEMPLATE_PREFIX, templateFileName, searchCond, traceIds, period, notifyIdArr, printerId, noticeDate, chargePeople, sortMethod, arrearsAmount);
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess("打印通知单成功！");
		}
		return RequestResultUtil.getResultUpdateWarn("打印通知单失败！");
	}
	
	
	/**
	 * @Title: destoryWaterNotify
	 * @Description: 报废通知单
	 * @param notifyId
	 * @param period
	 * @param serialNo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/destoryWaterNotify")
	@ResponseBody
	public Object destoryWaterNotify(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			
			waterNotifyService.destoryWaterNotify(ids);
		}catch(Exception e) {
			return RequestResultUtil.getResultUpdateWarn("通知单报废失败！");
		}

		return RequestResultUtil.getResultUpdateSuccess("通知单报废成功！");

	}
	
}