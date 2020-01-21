package com.learnbind.ai.controller.meterbook;

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
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterbook.UserMeterBookService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.user.UsersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.meterbook
 *
 * @Title: MeterBookAllotController.java
 * @Description: 我的表册
 *
 * @author Administrator
 * @date 2019年6月12日 下午6:49:18
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/my-meter-book")
public class MyMeterBookController {

	private static Log log = LogFactory.getLog(MyMeterBookController.class);
	private static final String TEMPLATE_PATH = "meterbook/mymeter_book/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private UsersService usersService; // 用户
	@Autowired
	private MeterBookService meterBookService;// 表册
	@Autowired
	private UserMeterBookService userMeterBookService;// 抄表员-表册
	@Autowired
	private LocationService locationService;// 地理位置
	@Autowired
	public CustomerMeterService customerMeterService;// 客户-表计关系服务
	@Autowired
	public MeterBookMeterService meterBookMeterService;// 表册包含的客户和表计
	@Autowired
	private CustomersService customersService;// 客户
	@Autowired
	private MetersService metersService;// 表计
	@Autowired
	private DataDictService dataDictService;  //数据字典
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
		return TEMPLATE_PATH + "mymeter_book_starter";
	}

	/**
	 * @Title: main
	 * @Description: 表册主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String loadMeterBookMain(Model model) {
		return TEMPLATE_PATH + "mymeter_book_main";
	}
	// ------------------------------ 我的表册部分 ------------------------------

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/my-meter-book-table")
	public String myMeterBookTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds,  Long userId) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<MeterBook> meterBookList = meterBookService.getAllotBookListByCondition(userBean.getId(), traceIds, searchCond);
		PageInfo<MeterBook> pageInfo = new PageInfo<>(meterBookList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBook meterBook : meterBookList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(meterBook);
			
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

		return TEMPLATE_PATH + "mymeter_book_table";
	}




	
	
	// ------------------------------ 表册包含的表计和客户信息部分 ------------------------------
	
	/**
	 * @Title: main
	 * @Description: 表册表计列表主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-meter-book-meter-main")
	public String loadMeterBookMeterMain(Model model) {
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
			pageSize = PAGE_SIZE;
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
			customerMap.put("propName", customer.getPropName());//用水状态
			customerMap.put("propTel", customer.getPropTel());//用水状态
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
		}

		model.addAttribute("customer", customerMap);
		model.addAttribute("meter", meterMap);

		return TEMPLATE_PATH + "detail_table";
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
	

}