package com.learnbind.ai.controller.checkmeter;

import java.util.ArrayList;
import java.util.List;

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
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.checkmeter.CheckMeterService;
import com.learnbind.ai.service.dict.DataDictService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.checkmeter
 *
 * @Title: CheckMeterController.java
 * @Description: 表计检测配置
 *
 * @author Thinkpad
 * @date 2019年5月15日 下午5:56:59
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/check-meter")
public class CheckMeterController {
	private static Log log = LogFactory.getLog(CheckMeterController.class);
	private static final String TEMPLATE_PATH = "check_meter/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CheckMeterService checkMeterService;  //检测配置
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
		return TEMPLATE_PATH + "check_starter";
	}

	/** 
	*	@Title: checkMeterSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String checkMeterSearch(Model model) {
		//加载数据字典-水表口径
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
		
		model.addAttribute("dictList", dictList);
		return TEMPLATE_PATH + "check_main";
	}

	/** 
	*	@Title: checkMeterTable 
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
	public String checkMeterTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysCheckMeter> checkMeterList = checkMeterService.searchCheckMeter(searchCond);
		PageInfo<List<SysCheckMeter>> pageInfo = new PageInfo(checkMeterList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("checkMeterList", checkMeterList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "check_table";
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
		//加载数据字典-水表口径
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
		
		model.addAttribute("dictList", dictList);
		return TEMPLATE_PATH + "check_dialog_edit";
	}
	
	/** 
	*	@Title: addCheckMeter 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addCheckMeter(SysCheckMeter checkMeter) {
		//waterPrice.setCreateTime(new Date());
		int row = checkMeterService.insertCheckMeter(checkMeter);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteCheckMeter 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteCheckMeter(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				checkMeterService.deleteCheckMeter(id);
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
		//加载数据字典-水表口径
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
				
		model.addAttribute("dictList", dictList);
		//读取需要编辑的条目
		SysCheckMeter currItem=checkMeterService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "check_dialog_edit";
	}

	/** 
	*	@Title: updatCheckMeter 
	*	@Description: 修改水价 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateCheckMeter(SysCheckMeter checkMeter) throws Exception {
		checkMeterService.updateByPrimaryKeySelective(checkMeter);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	
	/**
	 * List排序
	 * @param categoryList
	 * @param cid
	 */
	private void sortList(List<SysCheckMeter> resultList, List<SysCheckMeter> checkMeterList,Long rightId) {
		
		for(int i=0; i<checkMeterList.size(); i++){
			SysCheckMeter right = checkMeterList.get(i);
        	if(right.getId().equals(rightId)){
        		//System.out.println(privilege.getType()+","+privilege.getName());
                resultList.add(right);
                sortList(resultList, checkMeterList,right.getId());
            }
        }
    }
	

	@RequestMapping(value = "/testdialog")
	public String testdialog(Model model) {
		return "/testdialog";
	}

}