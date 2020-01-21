package com.learnbind.ai.controller.meterreadconfig;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysMeterReadConfig;
import com.learnbind.ai.model.SysPeopleAdjust;
import com.learnbind.ai.service.meterreadconfig.MeterReadConfigService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meterreadconfig
 *
 * @Title: MeterReadConfigController.java
 * @Description: 抄表配置
 *
 * @author Thinkpad
 * @date 2019年7月4日 上午11:45:02
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-read-config")
public class MeterReadConfigController {
	
	private static Log log = LogFactory.getLog(MeterReadConfigController.class);
	
	private static final String TEMPLATE_PATH = "meter_read_config/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MeterReadConfigService meterReadConfigService;//抄表调整配置服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "meter_read_config_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		List<SysMeterReadConfig> configList = meterReadConfigService.selectAll();
		
		if(configList==null || configList.size()<=0) {
			model.addAttribute("estimateCurrItem", null);
			model.addAttribute("preditCurrItem", null);
			return TEMPLATE_PATH + "meter_read_config_main";
		}
		
		for(SysMeterReadConfig config : configList) {
			if(config.getType()==EnumReadType.ESTIMATE_READ.getValue()) {
				model.addAttribute("estimateCurrItem", config);
			}else {
				model.addAttribute("preditCurrItem", config);
			}
		}
		
		return TEMPLATE_PATH + "meter_read_config_main";
	}
	
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param peopleAdjust
	 * @return 
	 */
	@RequestMapping(value = "/save", produces = "application/json")
	@ResponseBody
	public Object insert(String meterReadConfigJSON) {
		
		try {
			List<SysMeterReadConfig> mList = JSON.parseArray(meterReadConfigJSON, SysMeterReadConfig.class);
			
			for(SysMeterReadConfig config : mList) {
				Long id = config.getId();
				if(id==null) {
					meterReadConfigService.insertSelective(config);
				}else {
					meterReadConfigService.updateByPrimaryKeySelective(config);
				}
				
			}
			
			return RequestResultUtil.getResultSaveSuccess("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param peopleAdjust
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(SysMeterReadConfig sysMeterReadConfig) {
		int rows = meterReadConfigService.updateByPrimaryKeySelective(sysMeterReadConfig);
		if(rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("meterReadConfigId", sysMeterReadConfig.getId());
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}

}