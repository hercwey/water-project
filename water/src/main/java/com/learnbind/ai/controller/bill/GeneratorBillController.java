package com.learnbind.ai.controller.bill;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumMakeBillStatus;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: GeneratorBillController.java
 * @Description: 开账
 *
 * @author Administrator
 * @date 2019年8月16日 上午11:11:38
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/generator-bill")
public class GeneratorBillController {
	
	private static Log log = LogFactory.getLog(GeneratorBillController.class);
	private static final String TEMPLATE_PATH = "generator_bill/"; // 页面目录

	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private CustomersService customersService;  //客户服务
	@Autowired
	private PartitionWaterService partitionWaterService;  //分水量服务
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private MetersService metersService;  //表计服务
	@Autowired
	private AddSubWaterService addSubWaterService;//追加减免水量

	/** 
	*	@Title: RecordStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	// @RequiresPermissions(value = { "PIVAS_MENU_203" })
	public String RecordStarter(Model model, Integer readType) {
		model.addAttribute("readType", readType);
		return TEMPLATE_PATH + "record_starter";
	}

	/** 
	*	@Title: meterRecordSearch 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String meterRecordSearch(Model model, Integer readType) {
		
		model.addAttribute("readType", readType);
		
		//加载数据字典-抄表方式
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		model.addAttribute("readModeList", readModeList);
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "record_main";
	}

	/**
	 * @Title: locationTable
	 * @Description: 加载地理位置
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/location-table")
	public String locationTable(Model model) {

		return TEMPLATE_PATH + "location_table";
	}
	
	/**
	 * @Title: meterRecordTable
	 * @Description: 加载列表页面
	 * @param model			ModelView中传递数据的对象
	 * @param pageNum		页号
	 * @param pageSize		页大小
	 * @param searchCond	用户输入的查询条件
	 * @param traceIds		地理位置-traceIds
	 * @param isMakeBill	开账状态，是否已开账：0=未开账（默认值）；1=已开账
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String meterRecordTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, Integer isMakeBill, String startDate, String endDate, String period) {

		//判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> partitionWaterMapList = partitionWaterService.getPartitionWaterMapList(null, searchCond, traceIds, isMakeBill, null, startDate, endDate, period);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(partitionWaterMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		String totalWaterAmount = "";//总水量
		String totalWaterTee = "";//总金额
		//获取统计的水费及水量
		Map<String, Object> statisticMap = partitionWaterService.getPartitionWaterStatisticMap(null, searchCond, traceIds, isMakeBill, null, startDate, endDate, period);
		if(statisticMap != null) {
			totalWaterAmount = statisticMap.get("TOTAL_WATER_AMOUNT").toString();
			totalWaterTee = statisticMap.get("TOTAL_WATER_FEE").toString();
		}
		for(Map<String, Object> partitionWaterMap : partitionWaterMapList) {
			String priceCode = (String)partitionWaterMap.get("WATER_USE");
			String customerId = partitionWaterMap.get("CUSTOMER_ID").toString();//客户ID
			String currTraceIds = partitionWaterMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			partitionWaterMap.put("place", place);
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			partitionWaterMap.put("customer", customer);
			String currperiod = partitionWaterMap.get("PERIOD").toString();
			String meterIdStr = partitionWaterMap.get("METER_ID").toString();
			String customerName = partitionWaterMap.get("CUSTOMER_NAME").toString();
			//if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue()) {//如果是单位用户
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterIdStr));
				String des = meter.getDescription();//表计描述
				if(StringUtils.isNotBlank(des)) {
					customerName = customerName+"（"+meter.getDescription()+"）";
				}
			//}
			partitionWaterMap.put("CUSTOMER_NAME", customerName);
			
			//partitionWaterMap.put("waterUseValue", this.getPriceTypeName(customer.getWaterUse()));//用水性质名称
			String priceName = this.getLadderName(priceCode);//获取水价名称
			log.debug(customer.getCustomerName()+" 的水价是 "+priceName);
			partitionWaterMap.put("waterUseValue", priceName);//水价名称
			
			String waterAmount = partitionWaterMap.get("WATER_AMOUNT").toString();//水量
			String waterFee = partitionWaterMap.get("WATER_FEE").toString();//水费
			//totalWaterAmount = BigDecimalUtils.add(totalWaterAmount, new BigDecimal(waterAmount));//统计总水量，用于列表统计
			//totalWaterTee = BigDecimalUtils.add(totalWaterTee, new BigDecimal(waterFee));//水量
			
			//是否追加减免水量
			boolean isAddSubWater = false;
			AddSubWater log = addSubWaterService.getAddSubLog(Long.valueOf(customerId), currperiod, Long.valueOf(partitionWaterMap.get("ID").toString()));
			if(log!=null) {
				isAddSubWater = true;
			}
			partitionWaterMap.put("isAddSubWater", isAddSubWater);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("partitionWaterMapList", partitionWaterMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		//回传总水量和总费用
		model.addAttribute("totalWaterAmount", totalWaterAmount);
		model.addAttribute("totalWaterFee", totalWaterTee);
		
		return TEMPLATE_PATH + "record_table";
	}
	
	/**
	 * @Title: getLadderName
	 * @Description: 获取水价名称
	 * @param priceCode
	 * @return 
	 */
	private String getLadderName(String priceCode) {
		
//		String ladder = "阶梯水价";
//		if(waterUse.equalsIgnoreCase(WaterPriceConstant.JMSHYS)) {
//			return ladder;
//		}
		if(StringUtils.isBlank(priceCode)) {
			return null;
		}
		SysWaterPrice wp = new SysWaterPrice();
		wp.setPriceCode(priceCode);
		List<SysWaterPrice> wpList = waterPriceService.select(wp);
		if(wpList!=null && wpList.size()>0) {
			wp = wpList.get(0);
			return wp.getLadderName();
		}
		return null;
	}
	
	//------------------------------	处理账单业务	------------------------------
	
	//------------------------------	批量开账	------------------------------
	/**
	 * @Title: generateBill
	 * @Description: 批量开账
	 * @param model
	 * @param customerId
	 * @param period
	 * @return 
	 */
	@RequestMapping(value = "/batch-generate-bill", produces = "application/json")
	@ResponseBody
	public Object batchGenerateBill(Model model, String searchCond, String traceIds, String pwIds) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
		
		//判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();
		
		List<PartitionWater> pwList = null;
		if(StringUtils.isNotBlank(pwIds) && !pwIds.equalsIgnoreCase("[]")) {
			List<Long> pwIdList = JSON.parseArray(pwIds, Long.class);//需要开账的分水量ID集合
			Example example = new Example(PartitionWater.class);
			example.createCriteria().andEqualTo("isMakeBill", EnumMakeBillStatus.MAKE_BILL_NO.getValue()).andIn("id", pwIdList);
			pwList = partitionWaterService.selectByExample(example);
		}else {
			pwList = partitionWaterService.getPartitionWaterList(null, searchCond, traceIds, EnumMakeBillStatus.MAKE_BILL_NO.getValue());
		}
		
		if(pwList==null || pwList.size()<=0) {
			return RequestResultUtil.getResultSaveWarn("没有需要开账的数据，不需要开账！");
		}
		
		int rows = partitionWaterService.batchGeneratorBill(pwList);
		if (rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("批量开账成功！");
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("开账异常，请重新尝试！");
	}
	
	//------------------------------	单个开账	------------------------------
	/**
	 * @Title: generateBill
	 * @Description: 单个开账
	 * @param model
	 * @param partitionWaterId
	 * @return 
	 */
	@RequestMapping(value = "/generate-bill", produces = "application/json")
	@ResponseBody
	public Object generateBill(Model model, Long partitionWaterId) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录!");
		}
		
		int rows = partitionWaterService.generatorBill(partitionWaterId);
		if (rows > 0) {
			Map<String, Object> respMap = RequestResultUtil.getResultSaveSuccess("开账成功！");
			return respMap;
		}
		return RequestResultUtil.getResultSaveWarn("开账异常，请重新尝试！");
	}
	
	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 
	 * 		为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;//操作员ID
		if (userBean!=null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for(SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}
			
			if(roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_METER_READER)!=-1) {
				operatorId = userBean.getId();//操作员ID
			}
			
		}
		return operatorId;
	}
	
}