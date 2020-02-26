package com.learnbind.ai.controller.meters;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.iot.protocol.util.ByteUtil;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.iot.util.StringUtil;
import com.learnbind.ai.model.iot.TestMeterDataBaseBean;
import com.learnbind.ai.model.iot.TestMeterReportBean;
import com.learnbind.ai.model.iot.TestMeterStatusBean;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.service.iot.ICommandService;
import com.learnbind.ai.service.iot.IMeterService;
import com.learnbind.ai.service.iot.WmDeviceService;
import com.learnbind.ai.service.meters.MetersService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterDocController.java
 * @Description: 表计-单据管理
 *
 * @author Thinkpad
 * @date 2019年10月22日 上午11:58:44
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-send-command")
public class MeterDeviceSendCommandController {
	private static Log log = LogFactory.getLog(MeterDeviceSendCommandController.class);
	private static final String TEMPLATE_PATH = "meters/meter_command/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
    ICommandService commandService;
	
	
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

	@RequestMapping(value = "/main")
  	public String loadMain(Model model) {
  		return TEMPLATE_PATH + "main";
  	}
  	
  	//------------------查询发送指令记录----------------------------------------------------------------------------------------------
    @RequestMapping(value = "/search-send-cmd-records")
    public String searchSendCmdRecords(Model model, Integer pageNum, Integer pageSize, Integer searchCommandType, String searchCond) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> commandMapList = commandService.searchList(searchCommandType, searchCond);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(commandMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for(Map<String, Object> commandMap : commandMapList) {
			Object createTime = commandMap.get("CREATE_TIME");
			Object platformIssuedTime = commandMap.get("PLATFORM_ISSUED_TIME");
			
			String createTimeStr = "";
			if(createTime!=null) {
				createTimeStr = sdf.format((Date)createTime);
			}
			String platformIssuedTimeSdf = "";
			if(platformIssuedTime!=null) {
				Date platformIssuedTimeD = StringUtil.timeZoneTrans((String)platformIssuedTime);
				platformIssuedTimeSdf = sdf.format(platformIssuedTimeD);
			}
			commandMap.put("CREATE_TIME_STR", createTimeStr);
			commandMap.put("PLATFORM_ISSUED_TIME_STR", platformIssuedTimeSdf);
		}
		// 传递如下数据至前台页面
		model.addAttribute("commandMapList", commandMapList); // 列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "table";
    	
    }
	
}