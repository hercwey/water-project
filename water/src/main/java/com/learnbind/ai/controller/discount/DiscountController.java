package com.learnbind.ai.controller.discount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.waterprice.WaterPriceService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.discount
 *
 * @Title: DiscountController.java
 * @Description: 收费参数-政策减免规则
 *
 * @author Thinkpad
 * @date 2019年5月14日 下午6:59:17
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/discount")
public class DiscountController {
	private static Log log = LogFactory.getLog(DiscountController.class);
	private static final String TEMPLATE_PATH = "discount/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private DiscountService discountService;  //减免政策配置
	

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
		return TEMPLATE_PATH + "discount_starter";
	}

	/** 
	*	@Title: discountSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String discountSearch(Model model) {
		List<SysDiscount> discountList = discountService.selectAll();
		
		List<SysDiscount> resultList = new ArrayList<>();//保存菜单排序结果
		resultList.clear();
		for(int i=0; i<discountList.size(); i++){
			SysDiscount discount = discountList.get(i);
			if(discount.getId()==0){
				//System.out.println(privilege.getType()+","+privilege.getName());
				resultList.add(discount);
				sortList(resultList, discountList,discount.getId());
			}
		}
		model.addAttribute("discountList", resultList);
		return TEMPLATE_PATH + "discount_main";
	}

	/** 
	*	@Title: discountTable 
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
	public String discountTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysDiscount> discountList = discountService.searchDiscount(searchCond);
		PageInfo<List<SysDiscount>> pageInfo = new PageInfo(discountList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("discountList", discountList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "discount_table";
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
		return TEMPLATE_PATH + "discount_dialog_edit";
	}
	
	/** 
	*	@Title: addDiscount 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addDiscount(SysDiscount discount) {
		//waterPrice.setCreateTime(new Date());
		int row = discountService.insertDiscount(discount);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteDiscount 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteDiscount(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				discountService.deleteDiscount(id);
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
		//读取需要编辑的条目
		SysDiscount currItem=discountService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "discount_dialog_edit";
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
	public Object updateDiscount(SysDiscount discount) throws Exception {
		discountService.updateByPrimaryKeySelective(discount);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	
	/**
	 * List排序
	 * @param categoryList
	 * @param cid
	 */
	private void sortList(List<SysDiscount> resultList, List<SysDiscount> discountList,Long rightId) {
		
		for(int i=0; i<discountList.size(); i++){
			SysDiscount right = discountList.get(i);
        	if(right.getId().equals(rightId)){
        		//System.out.println(privilege.getType()+","+privilege.getName());
                resultList.add(right);
                sortList(resultList, discountList,right.getId());
            }
        }
    }
	

	@RequestMapping(value = "/testdialog")
	public String testdialog(Model model) {
		return "/testdialog";
	}

}