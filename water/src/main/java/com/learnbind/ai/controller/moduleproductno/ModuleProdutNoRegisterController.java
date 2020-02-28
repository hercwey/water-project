package com.learnbind.ai.controller.moduleproductno;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.ModuleProductNo;
import com.learnbind.ai.service.moduleproductno.ModuleProductNoService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.controller.moduleproductno
 *
 * @Title: ModuleProdutNoRegisterController.java
 * @Description: 产品登记
 *
 * @author SRD
 * @date 2020年2月27日 下午7:15:13
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/module-product-register")
public class ModuleProdutNoRegisterController {

	private static Log log = LogFactory.getLog(ModuleProdutNoRegisterController.class);

	private static final String TEMPLATE_PATH = "moduleproductno/register/";// 页面目录
	private static final int PAGE_SIZE = 10;// 页大小

	@Autowired
	private ModuleProductNoService moduleProductNoService;// 模组号-出厂编号对照表

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String operatorName = null;
		if(userBean!=null) {
			String realname = userBean.getRealname();
			String nickname = userBean.getNickname();
			if(StringUtils.isNotBlank(realname)) {//如果真实姓名不为空时，设置操作员姓名为真实姓名
				operatorName = userBean.getRealname();
			}
			if(StringUtils.isBlank(operatorName) && StringUtils.isNotBlank(nickname)) {//如果操作员姓名为空，且操作员昵称不为空时，设置操作姓名为昵称
				operatorName = userBean.getRealname();
			}
		}
		model.addAttribute("operatorName", operatorName);
		
		return TEMPLATE_PATH + "main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String dictTypeCode, String searchCond) {

		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0 || pageSize==null || pageSize==0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;//PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Example example = new Example(ModuleProductNo.class);
		example.createCriteria().andEqualTo("operatorId", userBean.getId()).andIsNotNull("productNo");
		example.setOrderByClause(" ID DESC, OPERATOR_DATE DESC");
		List<ModuleProductNo> mpNoList = moduleProductNoService.selectByExample(example);
		PageInfo<ModuleProductNo> pageInfo = new PageInfo<>(mpNoList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("moduleProductNoList", mpNoList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "table";
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
				moduleProductNoService.deleteByPrimaryKey(id);
			}
		} catch (Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: loadModiDialog
	 * @Description: 加载编辑对话框
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadModiDialog(Long itemId, Model model) {

		// 读取需要编辑的条目
		ModuleProductNo currItem = moduleProductNoService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem", currItem);

		return TEMPLATE_PATH + "edit_dialog";
	}

	/**
	 * @Title: update
	 * @Description: 修改出厂编号
	 * @param id
	 * @param productNo
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(Long id, String productNo) {

		try {
			ModuleProductNo mpNo = new ModuleProductNo();
			mpNo.setProductNo(productNo);
			int count = moduleProductNoService.selectCount(mpNo);
			if(count>0) {//count>0表示出厂编号重复
				Map<String, Object> resMap = RequestResultUtil.getResultSuccess("出厂编号重复，请手工录入后重新保存！");
				resMap.put("isAlert", true);
				return resMap;
			}else {
				mpNo = new ModuleProductNo();
				mpNo.setId(id);
				mpNo.setProductNo(productNo);
				int rows = moduleProductNoService.updateByPrimaryKeySelective(mpNo);
				if (rows > 0) {
					Map<String, Object> resMap = RequestResultUtil.getResultUpdateSuccess();
					resMap.put("isAlert", false);
					return resMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	
	/**
	 * @Title: searchMeterNumberIsnull
	 * @Description: 查询出厂编号为空的记录
	 * @return 
	 */
	@RequestMapping(value = "/search-meter-number-isnull", produces = "application/json")
	@ResponseBody
	public Object searchMeterNumberIsnull() {

		try {
			
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(userBean!=null) {
				Example example = new Example(ModuleProductNo.class);
				example.createCriteria().andEqualTo("operatorId", userBean.getId()).andIsNull("productNo");
				example.setOrderByClause(" ID ASC, OPERATOR_DATE ASC");
				List<ModuleProductNo> mpNoList = moduleProductNoService.selectByExample(example);
				
				Map<String, Object> resMap = RequestResultUtil.getResultSuccess("查询成功！");
				ModuleProductNo mpNo = null;
				if(mpNoList!=null && mpNoList.size()>0) {
					mpNo = mpNoList.get(0);
				}
				resMap.put("mpNo", mpNo);
				return resMap;
			}else {
				Map<String, Object> resMap = RequestResultUtil.getResultFail("登录已失效，请重新登录！");
				return resMap;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultSuccess("查询异常，请重新查询");
	}
	
}