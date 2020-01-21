package com.learnbind.ai.controller.checkmeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.MeterBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysCheckMeterResult;
import com.learnbind.ai.service.checkmeter.CheckMeterResultService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.checkmeter
 *
 * @Title: CheckMeterResultController.java
 * @Description: 水表检测结果
 *
 * @author Thinkpad
 * @date 2019年8月3日 下午8:14:49
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/check-result")
public class CheckMeterResultController {
	private static Log log = LogFactory.getLog(CheckMeterResultController.class);
	private static final String TEMPLATE_PATH = "check_meter/check_result/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CheckMeterResultService checkMeterResultService;  //检测结果
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private MetersService metersService;  //表计档案
	

	/** 
	*	@Title: starter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "result_starter";
	}

	/** 
	*	@Title: search 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	public String search(Model model) {
		
		return TEMPLATE_PATH + "result_main";
	}

	/** 
	*	@Title: table 
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysCheckMeterResult> checkMeterresultList = checkMeterResultService.searchCond(searchCond);
		PageInfo<List<SysCheckMeterResult>> pageInfo = new PageInfo(checkMeterresultList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(SysCheckMeterResult scr : checkMeterresultList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(scr);
			String checkResult = StringEscapeUtils.unescapeHtml(scr.getCheckResult());
			
			customerMap.put("checkTimeStr", scr.getCheckTimeStr());
			customerMap.put("checkResult", checkResult);
			
			
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("checkMeterresultList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "result_table";
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
		
		String title="增加";
		model.addAttribute("title",title);
		return TEMPLATE_PATH + "result_dialog_edit";
	}
	
	
	
	/** 
	*	@Title: add
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object add(SysCheckMeterResult sysCheckMeterResult) {
		//HTML字符编码
		String result = StringEscapeUtils.escapeHtml(sysCheckMeterResult.getCheckResult());
		sysCheckMeterResult.setCheckResult(result);
		
		int row = checkMeterResultService.insertSelective(sysCheckMeterResult);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: delete
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				checkMeterResultService.deleteByPrimaryKey(id);
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
	*	@Title: loadModiDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiDialog(Long itemId, Model model) {
		//读取需要编辑的条目
		SysCheckMeterResult currItem=checkMeterResultService.selectByPrimaryKey(itemId);
		//HTML转义解码
		String result = StringEscapeUtils.unescapeHtml(currItem.getCheckResult());
		currItem.setCheckResult(result);
		model.addAttribute("currItem",currItem);
		String title="编辑";
		model.addAttribute("title",title);
		return TEMPLATE_PATH + "result_dialog_edit";
	}

	/** 
	*	@Title: update
	*	@Description: 修改水价 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(SysCheckMeterResult checkMeter) throws Exception {
		//HTML字符编码
		String result = StringEscapeUtils.escapeHtml(checkMeter.getCheckResult());
		checkMeter.setCheckResult(result);
		checkMeterResultService.updateByPrimaryKeySelective(checkMeter);
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	
	/** 
	*	@Title: getMeterMessage
	*	@Description: 根据水表钢印号查询水表信息
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/meter-message")
	@ResponseBody
	public Object getMeterMessage(Model model, String steelSealNo) {
		
		Meters meter = metersService.getMeterMessage(steelSealNo);
		return meter;
	}
	

}