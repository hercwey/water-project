package com.learnbind.ai.controller.waterprice;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.wateruse.EnumDJWater;
import com.learnbind.ai.common.enumclass.wateruse.EnumFJMWater;
import com.learnbind.ai.common.enumclass.wateruse.EnumJMWater;
import com.learnbind.ai.common.enumclass.wateruse.EnumTZHYYSWater;
import com.learnbind.ai.common.enumclass.wateruse.EnumZXJMFJMWater;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.waterprice
 *
 * @Title: WaterPriceController.java
 * @Description: 前端控制-水价管理
 *
 * @author Thinkpad
 * @date 2019年5月13日 下午7:12:05
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/water-price")
public class WaterPriceController {
	private static Log log = LogFactory.getLog(WaterPriceController.class);
	private static final String TEMPLATE_PATH = "water_price/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private WaterPriceService waterPriceService;  //水价管理
	@Autowired
	private DataDictService dataDictService;  //数据字典
	

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
		return TEMPLATE_PATH + "setting_starter";
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
		
		return TEMPLATE_PATH + "setting_main";
	}

	/** 
	*	@Title: waterPriceTable 
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
	public String waterPriceTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysWaterPrice> waterPriceList = waterPriceService.searchWaterPrice(searchCond);
		PageInfo<List<SysWaterPrice>> pageInfo = new PageInfo(waterPriceList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		// 传递如下数据至前台页面
		model.addAttribute("waterPriceList", waterPriceList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "setting_table";
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
		//加载数据字典-抄表方式
		List<DataDict> waterUseList = dataDictService.getListByTypeCode(DataDictType.WATER_USE);
		model.addAttribute("waterUseList", waterUseList);
		
		return TEMPLATE_PATH + "setting_dialog_edit";
	}
	
	/** 
	*	@Title: addWaterPrice 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addWaterPrice(SysWaterPrice waterPrice) {
		
		waterPrice.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		
		String priceCode = "price";
		int row = waterPriceService.insertWaterPrice(waterPrice);
		priceCode= priceCode+waterPrice.getId().toString();
		waterPrice.setPriceCode(priceCode);
		waterPriceService.updateByPrimaryKeySelective(waterPrice);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteWaterPrice 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteWaterPrice(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(id);
				waterPrice.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				waterPriceService.updateByPrimaryKeySelective(waterPrice);
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
		
		//加载数据字典-抄表方式
		List<DataDict> waterUseList = dataDictService.getListByTypeCode(DataDictType.WATER_USE);
		model.addAttribute("waterUseList", waterUseList);
		//读取需要编辑的条目
		SysWaterPrice currItem=waterPriceService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "setting_dialog_edit";
	}

	/** 
	*	@Title: updatWaterPrice 
	*	@Description: 修改水价 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateWaterPrice(SysWaterPrice waterPrice) throws Exception {
		waterPriceService.updateByPrimaryKeySelective(waterPrice);
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	@RequestMapping(value = "/get-water-price")
	@ResponseBody
	public List<SysWaterPrice> getWaterPrice(String waterUse) {
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("priceTypeCode", waterUse).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
		
		return waterPriceList;
	}
	


}