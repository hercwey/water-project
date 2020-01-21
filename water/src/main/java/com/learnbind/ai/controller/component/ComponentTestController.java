package com.learnbind.ai.controller.component;

import java.text.DecimalFormat;
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

import com.learnbind.ai.service.statistic.StatisticService;


@Controller
@RequestMapping(value = "/component")
public class ComponentTestController {
	private static Log log = LogFactory.getLog(ComponentTestController.class);
	private static final String TEMPLATE_PATH = "/component"; //页面目录
	//private static final int PAGE_SIZE = 8; //页大小
	
	/**
	 * @Title: testDistrictSearcher
	 * @Description: 测试searcher组件
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/searcher")
	public String testDistrictSearcher(Model model) {
		return TEMPLATE_PATH + "/districtsearcher/district_searcher_test";
	}
	
	/**
	 * @Title: testLocationTree
	 * @Description: 查询Tree组件
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/locationtree")
	public String testLocationTree(Model model) {
		return TEMPLATE_PATH + "/locationtree/location_tree_test";
	}
	
	/**
	 * @Title: testDialogUtil
	 * @Description: 测试通用对话框工具JS
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/dialogutil")
	public String testDialogUtil(Model model) {
		return TEMPLATE_PATH + "/dialogutil/dialog_util_test";
	}
	
	/**
	 * @Title: testInvoiceHMConfirmDialog
	 * @Description: 发票号码确认对话框组件测试
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/invoicehmconfirmdialog")
	public String testInvoiceHMConfirmDialog(Model model) {
		return TEMPLATE_PATH + "/invoicehmconfirmdialog/invoicehm_confirm_dialog_test";
	}
	
	/**
	 * @Title: testInvoicePrintDialog
	 * @Description: 发票打印对话框组件 测试.
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/invoiceprintdialog")
	public String testInvoicePrintDialog(Model model) {
		return TEMPLATE_PATH + "/invoiceprintdialog/invoice_print_dialog_test";
	}
	
	
	/**
	 * @Title: testDownloadRedInfoDialog
	 * @Description: 下载红字对话框-test
	 * @param model
	 * @return 
	 * 
	 * 注:此组件已经不再使用,由于web service不支持
	 */
	@RequestMapping(value = "/downloadredinfodialog")
	public String testDownloadRedInfoDialog(Model model) {
		return TEMPLATE_PATH + "/downloadredinfodialog/download_redinfo_dialog_test";
	}
	
	/**
	 * @Title: testInvalidInvoiceDialog
	 * @Description: 未使用发票作废对话框-TEST
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/invalidinvoicedialog")
	public String testInvalidInvoiceDialog(Model model) {
		return TEMPLATE_PATH + "/invalidinvoicedialog/invalid_invoice_dialog_test";
	}
	
	
	/**
	 * @Title: testInvalidInvoiceProgressDialog
	 * @Description: 未使用发票作废进度对话框-TEST
	 * @param model
	 * @return 
	 * 
	 * 此接口不再使用,由于web servicer 不支持
	 */
	@RequestMapping(value = "/invalidinvoiceprogressdialog")
	public String testInvalidInvoiceProgressDialog(Model model) {
		return TEMPLATE_PATH + "/invalidinvoiceprogressdialog/invalid_invoice_progress_dialog_test";
	}
	
	/**
	 * @Title: testInvalidInvoiceResultDialog
	 * @Description: 发票作废结果对话框
	 * @param model
	 * @return 
	 * 		注:是否可以用于己开发票作废结果对话框 ,尽量重用此功能.
	 */
	@RequestMapping(value = "/invalidinvoiceresultdialog")
	public String testInvalidInvoiceResultDialog(Model model) {
		return TEMPLATE_PATH + "/invalidinvoiceresultdialog/invalid_invoice_result_dialog_test";
	}
	
	/**
	 * @Title: testBatchQueryInvoiceCondDialog
	 * @Description: 发票批量查询对话框组件测试
	 * @param model
	 * @return 组件测试页面
	 */
	@RequestMapping(value = "/batchqueryinvoiceconddialog")
	public String testBatchQueryInvoiceCondDialog(Model model) {
		return TEMPLATE_PATH + "/batchqueryinvoiceconddialog/batch_query_invoice_cond_dialog_test";
	}
	
	/**
	 * @Title: testNormalInvoiceVolDialog
	 * @Description: 普通发票卷选择对话框测试.
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/normalinvoicevoldialog")
	public String testNormalInvoiceVolDialog(Model model) {
		return TEMPLATE_PATH + "/normalinvoicevoldialog/normal_invoice_vol_dialog_test";
	}
	
	
	//测试显示专用发票对话框
	@RequestMapping(value = "/displayinvoicespecial")
	public String testDisplayInvoiceSpecialDialog(Model model) {
		return TEMPLATE_PATH + "/displayinvoicespecial/display_invoice_special_test";
	}
	
	//测试显示普通发票对话框
	@RequestMapping(value = "/displayinvoicenormal")
	public String testDisplayInvoiceNormalDialog(Model model) {
		return TEMPLATE_PATH + "/displayinvoicenormal/display_invoice_normal_test";
	}
	
}
