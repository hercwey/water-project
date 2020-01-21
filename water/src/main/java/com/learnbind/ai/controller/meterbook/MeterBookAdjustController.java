package com.learnbind.ai.controller.meterbook;

import java.util.ArrayList;
import java.util.Date;
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
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumMeterBookGenerateStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meterbook
 *
 * @Title: MeterBookAdjustController.java
 * @Description: 表册间调整
 *
 * @author Thinkpad
 * @date 2019年8月18日 下午5:05:40
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-book-adjust")
public class MeterBookAdjustController {

	private static Log log = LogFactory.getLog(MeterBookAdjustController.class);
	private static final String TEMPLATE_PATH = "meterbook/adjust/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private MeterBookService meterBookService;//表册
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	public CustomerMeterService customerMeterService;//客户-表计关系服务
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	public MeterBookMeterService meterBookMeterService;// 表册包含的客户和表计
	@Autowired
	private CustomersService customersService;// 客户
	@Autowired
	private MetersService metersService;// 表计
	@Autowired
	private DiscountService discountService;  //政策减免
	@Autowired
	private WaterPriceService waterPriceService;//用水性质

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "meterbook_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "meterbook_main";
	}
	

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond, Integer generateStatus) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<MeterBook> bookList = meterBookService.getMeterBookByTraceIdsAndSearchCond(traceIds, searchCond, generateStatus);
		PageInfo<List<MeterBook>> pageInfo = new PageInfo(bookList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBook meterBook : bookList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(meterBook);
			
			customerMap.put("generateStatusStr", meterBook.getGenerateStatusStr());
			customerMap.put("createTimeStr", meterBook.getCreateTimeStr());
			customerMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meterBook.getFactory()));//水表生产厂家
			
			
			customerMapList.add(customerMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("meterBookList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "meterbook_table";
	}

	
	
	// ------------------------------ 表册的选择表计部分部分 ------------------------------
	
	/**
	 * @Title: main
	 * @Description: 表册间的表计调整界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-meter-book-select-meter-main")
	public String loadMeterBookSelectMeterMain(Model model, String meterBookIds) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		String[] meterIdArr = meterBookIds.split(",");
		Long firstMeterBookId = Long.valueOf(meterIdArr[0]);
		Long secondMeterBookId = Long.valueOf(meterIdArr[1]);
		
		MeterBook first = meterBookService.selectByPrimaryKey(firstMeterBookId);
		MeterBook second = meterBookService.selectByPrimaryKey(secondMeterBookId);
		
		model.addAttribute("firstName", first.getName());
		model.addAttribute("secondName", second.getName());
		
		return TEMPLATE_PATH + "meterbook_select_meter_main";
	}
	
	// ------------------------------ 我的表册包含的表计部分 ------------------------------
	@RequestMapping(value = "/first-meter-book-select-main")
	public String firstMeterBookMain(Model model, Long meterBookId) {
		
		return TEMPLATE_PATH + "first_meterbook_select_main";
	}
	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/first-meter-book-select-table")
	public String firstMeterBookTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String meterBookId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Example example = new Example(MeterBookMeter.class);
		example.createCriteria().andEqualTo("meterBookId", meterBookId);
		Long meterBId = Long.valueOf(meterBookId);
		
		List<MeterBookMeter>  mbList = meterBookMeterService.searchMeterBookMeterList(searchCond, meterBId);
		PageInfo<MeterBookMeter> pageInfo = new PageInfo<>(mbList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBookMeter mb : mbList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(mb);
			//获取客户信息
			Customers customer = customersService.selectByPrimaryKey(mb.getCustomerId());
			//获取表计信息
			Meters meter = metersService.selectByPrimaryKey(mb.getMeterId());
			customerMap.put("customerName", customer.getCustomerName());//用水状态
			customerMap.put("customerTel", customer.getCustomerTel());//用水状态
			customerMap.put("steelSealNo", meter.getSteelSealNo());//用水状态
			customerMap.put("place", meter.getPlace());//用水状态
			
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("mbList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "first_meterbook_select_table";
	}
	
	@RequestMapping(value = "/second-meter-book-select-main")
	public String secondMeterBookMain(Model model, Long meterBookId) {
		
		return TEMPLATE_PATH + "second_meterbook_select_main";
	}
	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/second-meter-book-select-table")
	public String secondMeterBookTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String meterBookId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Example example = new Example(MeterBookMeter.class);
		example.createCriteria().andEqualTo("meterBookId", meterBookId);
		
		Long meterBId = Long.valueOf(meterBookId);
		List<MeterBookMeter>  mbList = meterBookMeterService.searchMeterBookMeterList(searchCond, meterBId);
		PageInfo<MeterBookMeter> pageInfo = new PageInfo<>(mbList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBookMeter mb : mbList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(mb);
			//获取客户信息
			Customers customer = customersService.selectByPrimaryKey(mb.getCustomerId());
			//获取表计信息
			Meters meter = metersService.selectByPrimaryKey(mb.getMeterId());
			customerMap.put("customerName", customer.getCustomerName());//用水状态
			customerMap.put("customerTel", customer.getCustomerTel());//用水状态
			customerMap.put("steelSealNo", meter.getSteelSealNo());//用水状态
			customerMap.put("place", meter.getPlace());//用水状态
			
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("mbList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "second_meterbook_select_table";
	}
	
	// ------------------------------ 全部表册包含的表计部分 ------------------------------
	
	

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/all-meter-book-select-table")
	public String meterBookTable(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> allMeterList = metersService.getMetersBindCustomersList(searchCond, traceIds);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(allMeterList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		for(Map<String, Object> meter : allMeterList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(meter);
			//获取客户信息
			//Customers customer = customersService.selectCustomerBidMeterInfo(meter.getId());
			//获取表计信息
			//customerMap.put("propName", customer.getPropName());//用水状态
			//customerMap.put("propTel", customer.getPropTel());//用水状态
			
			
			//customerMapList.add(customerMap);
		}
		

		// 传递如下数据至前台页面
		model.addAttribute("allMeterList", allMeterList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "all_meterbook_select_table";
	}
	
	/**
	 * @Title: addMeters
	 * @Description: 将表计添加到我的表册当中
	 * @param meterBookId
	 * @param meterIds
	 * @return
	 */
	@RequestMapping(value = "/move-meters", produces = "application/json")
	@ResponseBody
	public Object addMeters(String firstMeterBookId, String secondMeterBookId, String meterIds) {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String[] meterIdArr = meterIds.split(",");
		
		//调整表册间表计时修改表册状态
		MeterBook firstBook = meterBookService.selectByPrimaryKey(Long.valueOf(firstMeterBookId));
		firstBook.setGenerateStatus(EnumMeterBookGenerateStatus.MANUAL.getValue());
		MeterBook secondBook = meterBookService.selectByPrimaryKey(Long.valueOf(secondMeterBookId));
		secondBook.setGenerateStatus(EnumMeterBookGenerateStatus.MANUAL.getValue());
		
		List<MeterBookMeter> meterBookMeterList = new ArrayList<>();
		for (String id : meterIdArr) {
			if (StringUtils.isNotBlank(id)) {
				Long meterBookMeterId = Long.valueOf(id);
				MeterBookMeter meterBookMeter = meterBookMeterService.selectByPrimaryKey(meterBookMeterId);

				meterBookMeterList.add(meterBookMeter);
			}
		}
		
		int rows = meterBookMeterService.moveBatch(meterBookMeterList, firstMeterBookId);

		if (rows > 0) {
			meterBookService.updateByPrimaryKeySelective(firstBook);
			meterBookService.updateByPrimaryKeySelective(secondBook);
			return RequestResultUtil.getResultAddSuccess();
		}

		return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: removeMeters
	 * @Description: 将从我的表册移除
	 * @param userId
	 * @param meterBookId
	 * @return
	 */
	@RequestMapping(value = "/remove-meter", produces = "application/json")
	@ResponseBody
	public Object removeMeters(Long meterBookId, String meterBookMeterIds) {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String[] meterBookMeterIdArr = meterBookMeterIds.split(",");

		List<MeterBookMeter> meterBookMeterList = new ArrayList<>();
		for (String id : meterBookMeterIdArr) {
			if (StringUtils.isNotBlank(id)) {
				Long meterBookMeterId = Long.valueOf(id);

				//Customers customer = customersService.selectCustomerBidMeterInfo(meterId);
				MeterBookMeter meterBookMeter = meterBookMeterService.selectByPrimaryKey(meterBookMeterId);
				//meterBookMeter.setMeterBookId(meterBookId);
				//meterBookMeter.setMeterId(meterId);
				//meterBookMeter.setCustomerId(customer.getId());

				meterBookMeterList.add(meterBookMeter);
			}
		}

		int rows = meterBookMeterService.deleteBatch(meterBookMeterList);

		if (rows > 0) {
			return RequestResultUtil.getResultDeleteSuccess();
		}

		return RequestResultUtil.getResultDeleteWarn();
	}

	
	
	
	// ------------------------------ 表册包含的表计和客户信息部分 ------------------------------
	
		/**
		 * @Title: main
		 * @Description: 表册表计列表主页面
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/load-meter-book-meter-main")
		public String loadMeterBookMeterMain(Model model, Long meterBookId) {
			MeterBook meterBook = meterBookService.selectByPrimaryKey(meterBookId);
			model.addAttribute("unitName", meterBook.getName());
			return TEMPLATE_PATH + "meterbook_meter_main";
		}
		/**
		 * @Title: table
		 * @Description: 列表页面
		 * @param model      ModelView中传递数据的对象
		 * @param pageNum    页号
		 * @param pageSize   页大小
		 * @param searchCond 查询条件
		 * @return
		 */
		@RequestMapping(value = "/meter-book-meter-table")
		public String customerMeterTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long meterBookId) {

			// 判定页码有效性
			if (pageNum == null || pageNum == 0) {
				pageNum = 1;
				pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
			}

			// 查询并分页
			PageHelper.startPage(pageNum, pageSize); // PageHelper
			Example example = new Example(MeterBookMeter.class);
			example.createCriteria().andEqualTo("meterBookId", meterBookId);
			
			List<MeterBookMeter>  mbList = meterBookMeterService.searchMeterBookMeterList(searchCond, meterBookId);
			//List<MeterBookMeter>  mbList = meterBookMeterService.selectByExample(example);
			PageInfo<MeterBookMeter> pageInfo = new PageInfo<>(mbList);// (使用了拦截器或是AOP进行查询的再次处理)
			
			List<Map<String, Object>> customerMapList = new ArrayList<>();
			for(MeterBookMeter mb : mbList) {
				Map<String, Object> customerMap = EntityUtils.entityToMap(mb);
				//获取客户信息
				Customers customer = customersService.selectByPrimaryKey(mb.getCustomerId());
				//获取表计信息
				Meters meter = metersService.selectByPrimaryKey(mb.getMeterId());
				customerMap.put("customerName", customer.getCustomerName());//用水状态
				customerMap.put("customerTel", customer.getCustomerTel());//用水状态
				customerMap.put("steelSealNo", meter.getSteelSealNo());//用水状态
				customerMap.put("place", meter.getPlace());//用水状态
				
				customerMapList.add(customerMap);
			}
			// 传递如下数据至前台页面
			model.addAttribute("mbList", customerMapList); // 列表数据

			// 分页数据
			model.addAttribute("pageInfo", pageInfo);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("pageSize", pageSize);

			// 查询条件回传
			model.addAttribute("searchCond", searchCond);

			return TEMPLATE_PATH + "meterbook_meter_table";
		}
		
		/**
		 * @Title: detail
		 * @Description: 获取表情详情信息
		 * @param model
		 * @param customerId
		 * @param meterId
		 * @return 
		 */
		@RequestMapping(value = "/load-meter-book-meter-detail")
		public String detail(Model model, Long customerId, Long meterId) {

			Map<String, Object> customerMap = null;
			Map<String, Object> meterMap = null;
			if (customerId != null && meterId != null) {
				// 客户详情
				Customers customer = customersService.selectByPrimaryKey(customerId);
				customerMap = EntityUtils.entityToMap(customer);
				customerMap.put("settleTimeStr", customer.getSettleTimeStr());// 立户时间
				customerMap.put("idTypeStr", customer.getIdTypeStr());// 证件类型
				customerMap.put("customerTypeStr", customer.getCustomerTypeStr());// 客户类型
				customerMap.put("waterStatusStr", customer.getWaterStatusStr());// 用水状态
				customerMap.put("notifyModeStr", customer.getNotifyModeStr());// 通知方式
				customerMap.put("deductTypeStr", customer.getDeductTypeStr());// 扣费方式
				
				customerMap.put("industryValue", this.getDataDictValue(EnumDataDictType.CUSTOMER_TRADE_TYPE.getCode(), customer.getIndustry()));//行业性质
				customerMap.put("discountName", this.getDiscountName(customer.getDiscountType()));//政策减免名称
				customerMap.put("waterUseValue", this.getPriceTypeName(customer.getWaterUse()));//用户性质名称

				// 表计详情
				Meters meter = metersService.selectByPrimaryKey(meterId);
				meterMap = EntityUtils.entityToMap(meter);
				meterMap.put("checkDateStr", meter.getCheckDateStr());// 水表检定日期
				meterMap.put("effectiveDateStr", meter.getEffectiveDateStr());// 水表有效日期
				meterMap.put("meterStatusStr", meter.getMeterStatusStr());// 水表状态
				meterMap.put("virtualRealStr", meter.getVirtualRealStr());// 虚实表

				
				meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
				meterMap.put("caliberValue", this.getDataDictValue(EnumDataDictType.METER_WATER_CALIBER.getCode(), meter.getCaliber()));//水表口径
				meterMap.put("meterModelValue", this.getDataDictValue(EnumDataDictType.METER_MODEL.getCode(), meter.getMeterModel()));//水表型号
				meterMap.put("meterTypeValue", this.getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meter.getMeterType()));//水表类型
				meterMap.put("meterUseValue", this.getDataDictValue(EnumDataDictType.METER_USE.getCode(), meter.getMeterUse()));//水表用途
				meterMap.put("readModeValue", this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meter.getReadMode()));//抄表方式
				meterMap.put("waterUseValue", this.getPriceTypeName(meter.getWaterUse()));//用水性质名称
			}

			model.addAttribute("customer", customerMap);
			model.addAttribute("meter", meterMap);

			return TEMPLATE_PATH + "detail_table";
		}
		
		/**
		 * @Title: getDataDictValue
		 * @Description: 根据数据字典类型编码和字典编码查询
		 * @param typeCode
		 * @param key
		 * @return 
		 */
		private String getDataDictValue(String typeCode, String key) {
			
			if(StringUtils.isBlank(typeCode) && StringUtils.isBlank(key)) {
				return null;
			}
			
			DataDict dict = new DataDict();
			if(StringUtils.isNotBlank(typeCode)) {
				dict.setTypeCode(typeCode);
			}
			dict.setKey(key);
			List<DataDict> dictList = dataDictService.select(dict);
			if(dictList!=null && dictList.size()>0) {
				dict = dictList.get(0);
			}
			
			if(dict!=null) {
				return dict.getValue();
			}
			return null;
		}
		
		/**
		 * @Title: getDiscountName
		 * @Description: 获取政策减免名称
		 * @param discountId
		 * @return 
		 */
		private String getPriceTypeName(String priceTypeCode) {
			String priceTypeName = waterPriceService.getPriceTypeName(priceTypeCode);
			if(priceTypeName!=null) {
				return priceTypeName;
			}
			return null;
		}
		/**
		 * @Title: getDiscountName
		 * @Description: 获取水价类型
		 * @param discountId
		 * @return 
		 */
		private String getDiscountName(Long discountId) {
			SysDiscount record = discountService.selectByPrimaryKey(discountId);
			if(record!=null) {
				return record.getName();
			}
			return null;
		}

}