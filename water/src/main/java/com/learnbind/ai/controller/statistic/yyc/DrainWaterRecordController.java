package com.learnbind.ai.controller.statistic.yyc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DrainWaterRecord;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.service.statistic.DrainWaterRecordService;
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.statistic.yyc
 *
 * @Title: DrainWaterRecordController.java
 * @Description: 管网末梢排水情况
 *
 * @author Thinkpad
 * @date 2020年1月18日 下午7:06:06
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class DrainWaterRecordController {
	private static Log log = LogFactory.getLog(DrainWaterRecordController.class);
	private static final String TEMPLATE_PATH = "statistic/yyc/drain/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	DrainWaterRecordService drainWaterRecordService;
	
	
	@RequestMapping(value = "/yyc/drain/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "starter";
		
	}
	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/yyc/drain/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "main";
	}
	
	/**
	 * @Title: table
	 * @Description: 列表显示
	 * @param searchCond
	 * @param period
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/yyc/drain/table")
	public String table(String searchCond, Integer pageNum, Integer pageSize, String period, Model model) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<DrainWaterRecord> dataList = drainWaterRecordService.searchCond(searchCond, period);
		PageInfo<List<DrainWaterRecord>> pageInfo = new PageInfo(dataList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		int count = 0;
		for(DrainWaterRecord record : dataList) {
			count += 1;
			Map<String, Object> tempMap = EntityUtils.entityToMap(record);
			tempMap.put("operationTimeStr", record.getOperationTimeStr());
			tempMap.put("count", count);
			accountItemMapList.add(tempMap);
			
		}
		model.addAttribute("accountItemMapList",accountItemMapList);
		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		return TEMPLATE_PATH+"table";
	}
	
	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加编辑对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/yyc/drain/loadadddialog")
	public String loadAddDialog(Model model, Long itemId) {
		if(itemId != null) {
			DrainWaterRecord currItem=drainWaterRecordService.selectByPrimaryKey(itemId);
			model.addAttribute("currItem",currItem);
		}
		return TEMPLATE_PATH + "drain_dialog_edit";
	}
	
	/**
	 * @Title: add
	 * @Description: 删除
	 * @param record
	 * @return 
	 */
	@RequestMapping(value = "/yyc/drain/insert", produces = "application/json")
	@ResponseBody
	public Object add(DrainWaterRecord record) {
		int row = drainWaterRecordService.insertSelective(record);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: delete
	 * @Description:删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/yyc/drain/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				drainWaterRecordService.deleteByPrimaryKey(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: updatePrinter
	 * @Description: 修改
	 * @param record
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/yyc/drain/update", produces = "application/json")
	@ResponseBody
	public Object update(DrainWaterRecord record) throws Exception {
		int row = drainWaterRecordService.updateByPrimaryKeySelective(record);
		if (row > 0)
			return RequestResultUtil.getResultUpdateSuccess();
		else
			return RequestResultUtil.getResultUpdateWarn();
	}
		

	
}
