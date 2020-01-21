package com.learnbind.ai.controller.printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

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
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.service.printer.PrinterService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.printer
 *
 * @Title: PrinterController.java
 * @Description: 前端控制-打印机配置
 *
 * @author Thinkpad
 * @date 2019年5月15日 下午12:14:45
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/printer")
public class PrinterController {
	private static Log log = LogFactory.getLog(PrinterController.class);
	private static final String TEMPLATE_PATH = "printer/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小
	
	private static final String OPERATION_ADD = "add";
	private static final String OPERATION_UPDATE = "update";
	
	@Autowired
	private PrinterService printerService;  //打印机配置服务
	

	/** 
	*	@Title: roleStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String roleStarter(Model model) {
		return TEMPLATE_PATH + "printer_starter";
	}

	/** 
	*	@Title: waterPriceSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String waterPriceSearch(Model model) {
		//获取本地打印机名称
		List<Map<String, String>> printerList = this.getPrinterNames();
		model.addAttribute("printerList", printerList);
		return TEMPLATE_PATH + "printer_main";
	}

	/** 
	*	@Title: PrinterTable 
	*	@Description: 加载用户列表(列表页面)
	*	@param @param model ModelView中传递数据的对象
	*	@param @param pageNum 页号
	*	@param @param pageSize 页大小
	*	@param @param searchCond 查询条件
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String PrinterTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<PrinterConfig> printerList = printerService.searchPrinter(searchCond);
		PageInfo<List<PrinterConfig>> pageInfo = new PageInfo(printerList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("printerList", printerList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "printer_table";
	}

	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadadddialog")
	public String loadAddDialog(Model model) {
		//获取本地打印机名称
		List<Map<String, String>> printerList = this.getPrinterNames();
		model.addAttribute("printerList", printerList);
		return TEMPLATE_PATH + "printer_dialog_edit";
	}
	
	/** 
	*	@Title: addPrinter 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addPrinter(PrinterConfig printer) {
		boolean flag = this.checkIpAddress(printer, OPERATION_ADD);
		if(!flag) {
			return RequestResultUtil.getResultFail("IP地址重复！");
		}
		//waterPrice.setCreateTime(new Date());
		int row = printerService.insertPrinter(printer);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deletePrinter 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deletePrinter(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				printerService.deletePrinter(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/** 
	*	@Title: loadModiRoleDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiRoleDialog(Long itemId, Model model) {
		//获取本地打印机名称
		List<Map<String, String>> printerList = this.getPrinterNames();
		model.addAttribute("printerList", printerList);
		//读取需要编辑的条目
		PrinterConfig currItem=printerService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "printer_dialog_edit";
	}

	/** 
	*	@Title: updatePrinter 
	*	@Description: 修改打印机
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updatePrinter(PrinterConfig printer) throws Exception {
		boolean flag = this.checkIpAddress(printer, OPERATION_UPDATE);
		if(!flag) {
			return RequestResultUtil.getResultFail("IP地址重复！");
		}
		
		printerService.updateByPrimaryKeySelective(printer);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	
	/**
	 * List排序
	 * @param categoryList
	 * @param cid
	 */
	private void sortList(List<PrinterConfig> resultList, List<PrinterConfig> printerList,Long rightId) {
		
		for(int i=0; i<printerList.size(); i++){
			PrinterConfig right = printerList.get(i);
        	if(right.getId().equals(rightId)){
        		//System.out.println(privilege.getType()+","+privilege.getName());
                resultList.add(right);
                sortList(resultList, printerList,right.getId());
            }
        }
    }
	

	/**
	 * @Title: checkIpAddress
	 * @Description: 检查IP是否有效
	 * @param ipAddress
	 * @return 
	 * 		可用返回true；不可用返回false
	 */
	private boolean checkIpAddress(PrinterConfig printer, String operation) {
		
		Example example = new Example(PrinterConfig.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ipAddress", printer.getIpAddress());
		if(operation.equals(OPERATION_UPDATE)) {
			criteria.andNotEqualTo("id", printer.getId());
		}
		List<PrinterConfig> itemList = printerService.selectByExample(example);
		if(itemList==null || itemList.size()<=0) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Title: getPrinterNames
	 * @Description: 获取本地打印机列表
	 * @return 
	 */
	private List<Map<String, String>> getPrinterNames() {
        
		List<Map<String, String>> printerList = new ArrayList<>();

		//当前默认打印机
		PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
		String defaultPrinterName = null;//默认打印机名称
		if(ps!=null) {
			defaultPrinterName = ps.getName();
		}
		
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < printService.length; i++) {
			String printerName = printService[i].getName();
			Map<String, String> map = new HashMap<>();
			map.put("printerName", printerName);
			
			if(StringUtils.isNotBlank(defaultPrinterName)) {
				if(printerName.equals(defaultPrinterName)) {
					map.put("isDefault", "true");
					printerList.add(map);
					continue;
				}
			}
			map.put("isDefault", "false");
			printerList.add(map);
		}
        
		return printerList;
    }
	
	public static void main(String[] args) {
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < printService.length; i++) {
			String printerName = printService[i].getName();
			System.out.println(printerName);
		}
	}
}