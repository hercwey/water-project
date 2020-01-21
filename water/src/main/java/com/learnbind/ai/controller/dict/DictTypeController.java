package com.learnbind.ai.controller.dict;

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
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.dict.DataDictService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.dict
 *
 * @Title: DataDictController.java
 * @Description: 数据字典控制器
 *
 * @author Administrator
 * @date 2019年5月14日 上午9:48:39
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/dict-type")
public class DictTypeController {
	
	private static Log log = LogFactory.getLog(DictTypeController.class);
	
	private static final String TEMPLATE_PATH = "data_dict/dict_type/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private DataDictService dataDictService;//数据字典

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "dict_type_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//获取数据字典类型集合
		List<Map<String, String>> dictTypeList = DataDictType.getDictTypeList();
		model.addAttribute("dictTypeList", dictTypeList);
		
		return TEMPLATE_PATH + "dict_type_main";
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, String>> dictTypeList = dataDictService.searchDictTypeCond(searchCond);
		PageInfo<List<Map<String, String>>> pageInfo = new PageInfo(dictTypeList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("dictTypeList", dictTypeList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "dict_type_table";
	}

	/**
	 * @Title: loadDictCodeDialog
	 * @Description: 加载增加对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loaddictcodedialog")
	public String loadDictCodeDialog(Model model) {
		
		return TEMPLATE_PATH + "dict_type_dialog";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param dict
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(DataDict dict) {
		
		dict.setTypeCode(dict.getTypeCode().toUpperCase());
		
		int row = dataDictService.insertSelective(dict);
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
				//System.out.println(id);
				dataDictService.deleteByPrimaryKey(id);
			}
		}catch(Exception e) {
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
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiDialog(Model model, String typeCode, String typeName) {
		
		model.addAttribute("typeCode", typeCode);
		model.addAttribute("typeName", typeName);
		
		return TEMPLATE_PATH + "dict_type_dialog";
	}

	/**
	 * @Title: update
	 * @Description: 修改
	 * @param dict
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(String oldTypeCode, String typeCode, String typeName) throws Exception {
		
		oldTypeCode = oldTypeCode.toUpperCase();
		typeCode = typeCode.toUpperCase();
		
		//条件
		Example example = new Example(DataDict.class);
		example.createCriteria().andEqualTo("typeCode", oldTypeCode);
		//要修改的内容
		DataDict dict = new DataDict();
		dict.setTypeCode(typeCode);
		dict.setTypeName(typeName);
		
		dataDictService.updateByExampleSelective(dict, example);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	
}