package com.learnbind.ai.controller.abnormalcfg;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.AbnormalQuantity;
import com.learnbind.ai.service.abnormalcfg.AbnormalQuantityService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.abnormalcfg
 *
 * @Title: AbnormalQuantityController.java
 * @Description: 异常水量报警配置控制器
 *
 * @author Administrator
 * @date 2019年5月16日 上午11:04:30
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/abnormal-quantity")
public class AbnormalQuantityController {
	
	private static Log log = LogFactory.getLog(AbnormalQuantityController.class);
	
	private static final String TEMPLATE_PATH = "abnormal_cfg/abnormal_qty/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private AbnormalQuantityService abnormalQuantityServiceService;//异常水量报警配置服务

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "abnormal_qty_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		List<AbnormalQuantity> abnormalQtyList = abnormalQuantityServiceService.selectAll();
		
		AbnormalQuantity abnormalQty = null;
		if(abnormalQtyList!=null && abnormalQtyList.size()>0) {
			abnormalQty = abnormalQtyList.get(0); 
		}
		model.addAttribute("currItem", abnormalQty);
		
		return TEMPLATE_PATH + "abnormal_qty_main";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param abnormalQuantity
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(AbnormalQuantity abnormalQuantity) {
		int rows = abnormalQuantityServiceService.insertSelective(abnormalQuantity);
		if(rows>0) {
			Long abnormalQtyId = 0l;
			List<AbnormalQuantity> abnormalQtyList = abnormalQuantityServiceService.selectAll();
			if(abnormalQtyList!=null && abnormalQtyList.size()>0) {
				AbnormalQuantity record = abnormalQtyList.get(0);
				abnormalQtyId = record.getId();
			}
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("abnormalQtyId", abnormalQtyId);
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param abnormalQuantity
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(AbnormalQuantity abnormalQuantity) {
		int rows = abnormalQuantityServiceService.updateByPrimaryKeySelective(abnormalQuantity);
		if(rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("保存成功！");
			respMap.put("abnormalQtyId", abnormalQuantity.getId());
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("保存异常，请重新保存！");
	}

}