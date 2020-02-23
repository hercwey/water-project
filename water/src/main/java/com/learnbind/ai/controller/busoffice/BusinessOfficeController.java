package com.learnbind.ai.controller.busoffice;

import java.util.ArrayList;
import java.util.Date;
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
import com.learnbind.ai.common.enumclass.EnumBusOfficeNodeType;
import com.learnbind.ai.common.enumclass.EnumBusOfficeStatus;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.BusinessOffice;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.busoffice.BusinessOfficeService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.busoffice
 *
 * @Title: BusinessOfficeController.java
 * @Description: 营业网点管理
 *
 * @author Thinkpad
 * @date 2020年2月21日 下午3:16:49
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/bus-office")
public class BusinessOfficeController {
	private static Log log = LogFactory.getLog(BusinessOfficeController.class);
	private static final String TEMPLATE_PATH = "bus_office/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private BusinessOfficeService businessOfficeService;  
	

	/**
	 * @Title: starter
	 * @Description: 起始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主界面:控制面板及列表容器 
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "main";
	}

	/**
	 * @Title: table
	 * @Description: 加载用户列表(列表页面)
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
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
		List<BusinessOffice> officeList = businessOfficeService.searchCond(searchCond);
		PageInfo<List<BusinessOffice>> pageInfo = new PageInfo(officeList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> officeMapList = new ArrayList<>();
		for(BusinessOffice temp : officeList) {
			Map<String, Object> officeMap = EntityUtils.entityToMap(temp);
			officeMap.put("createDateStr", temp.getCreateDateStr());
			officeMap.put("officeStatusStr", EnumBusOfficeStatus.getName(temp.getOfficeStatus()));
			officeMap.put("nodeTypeStr", EnumBusOfficeNodeType.getName(temp.getNodeType()));
			officeMapList.add(officeMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("officeList", officeMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "table";
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
		return TEMPLATE_PATH + "office_dialog_edit";
	}
	
	/**
	 * @Title: add
	 * @Description: 新增
	 * @param entity
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object add(BusinessOffice entity) {
		entity.setCreateDate(new Date());
		int row = businessOfficeService.insertSelective(entity);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				businessOfficeService.deleteByPrimaryKey(id);
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
		BusinessOffice currItem=businessOfficeService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "office_dialog_edit";
	}
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(BusinessOffice entity) throws Exception {
		businessOfficeService.updateByPrimaryKeySelective(entity);
		return RequestResultUtil.getResultUpdateSuccess();
	}
	

}