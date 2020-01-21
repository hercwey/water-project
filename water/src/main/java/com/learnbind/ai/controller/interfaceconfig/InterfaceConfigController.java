package com.learnbind.ai.controller.interfaceconfig;

import java.util.ArrayList;
import java.util.List;

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
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.interfaceconfig
 *
 * @Title: InterfaceConfigController.java
 * @Description: 接口配置控制器
 *
 * @author Administrator
 * @date 2019年7月8日 下午5:30:25
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/interface-config")
public class InterfaceConfigController {
	
	private static Log log = LogFactory.getLog(InterfaceConfigController.class);
	
	private static final String TEMPLATE_PATH = "interface_config/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private DataDictService dataDictService;//数据字典服务
	@Autowired
	private InterfaceConfigService interfaceConfigService;//接口配置服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model, String interfaceType) {
		DataDict dict = new DataDict();
		dict.setTypeCode(InterfaceConstant.INTERFACE_TYPE_CODE);
		dict.setKey(interfaceType);
		List<DataDict> dictList = dataDictService.select(dict);
		if(dictList!=null && dictList.size()>0) {
			dict = dictList.get(0);
			model.addAttribute("interfaceTypeCode", dict.getKey());
			model.addAttribute("interfaceTypeName", dict.getValue());
		}
		
		return TEMPLATE_PATH + "config_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model, String interfaceType) {
		
		return TEMPLATE_PATH + "config_main";
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
	public String table(Model model, String interfaceType, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		Example example = new Example(SysInterfaceConfig.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("interfaceType", interfaceType);
		if(StringUtils.isNotBlank(searchCond)) {
			criteria.andLike("key", "%"+searchCond+"%");
		}
		example.setOrderByClause(" ID ASC");
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysInterfaceConfig> configList = interfaceConfigService.selectByExample(example);
		PageInfo<SysInterfaceConfig> pageInfo = new PageInfo<SysInterfaceConfig>(configList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("configList", configList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "config_table";
	}

	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @param interfaceType
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, String interfaceType, Long id) {
		
		model.addAttribute("interfaceType", interfaceType);
		
		if(id==null) {
			model.addAttribute("currItem", null);
		}else {
			SysInterfaceConfig config = interfaceConfigService.selectByPrimaryKey(id);
			model.addAttribute("currItem", config);
		}
		
		return TEMPLATE_PATH + "config_edit_dialog";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param config
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(SysInterfaceConfig config) {
		
		//config.setKey(config.getKey().toUpperCase());
		String key = config.getKey();
		if(StringUtils.isBlank(key)) {
			return RequestResultUtil.getResultFail("配置项不能为空！");
		}else {
			SysInterfaceConfig configTemp = new SysInterfaceConfig();
			configTemp.setInterfaceType(config.getInterfaceType());
			configTemp.setKey(key);
			List<SysInterfaceConfig> configList = interfaceConfigService.select(configTemp);
			if(configList!=null && configList.size()>0) {
				return RequestResultUtil.getResultFail("配置项重复，请重新输入！");
			}
		}
		
		int row = interfaceConfigService.insertSelective(config);
		if (row > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				interfaceConfigService.deleteByPrimaryKey(id);
			}
			return RequestResultUtil.getResultDeleteSuccess();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultDeleteWarn();

	}

	/**
	 * @Title: update
	 * @Description: 修改
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(SysInterfaceConfig config) throws Exception {
		
		//config.setKey(config.getKey().toUpperCase());
		interfaceConfigService.updateByPrimaryKeySelective(config);
		
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
}