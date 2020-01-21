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
import com.learnbind.ai.common.enumclass.EnumDefaultRole;
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
 * @Description: 表册分配
 *
 * @author Administrator
 * @date 2019年6月12日 下午6:49:18
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/meter-book-allot")
public class MeterBookAllotController {

	private static Log log = LogFactory.getLog(MeterBookAllotController.class);
	private static final String TEMPLATE_PATH = "meterbook/allot/"; // 页面目录
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
		return TEMPLATE_PATH + "allot_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "allot_main";
	}

	// ------------------------------ 抄表员部分 ------------------------------

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/meter-reader-table")
	public String meterReaderTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<SysUsers> userList = usersService.getUserListByRoleCode(EnumDefaultRole.ROLE_METER_READER.getCode(),
				searchCond);
		PageInfo<SysUsers> pageInfo = new PageInfo<>(userList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("userList", userList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "reader_table";
	}

	// ------------------------------ 表册部分主页 ------------------------------
	/**
	 * @Title: main
	 * @Description: 表册主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-meter-book-main")
	public String loadMeterBookMain(Model model, Long readerId) {
		SysUsers user = usersService.selectByPrimaryKey(readerId);
		model.addAttribute("readerName", user.getRealname());
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		return TEMPLATE_PATH + "meterbook_main";
	}
	// ------------------------------ 我的表册部分 ------------------------------
	
	@RequestMapping(value = "/my-meter-book-main")
	public String myMeterBookMain(Model model, Long meterBookId) {
		
		return TEMPLATE_PATH + "my_meterbook_main";
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
	@RequestMapping(value = "/my-meter-book-table")
	public String myMeterBookTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long readerId, String traceIds) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<MeterBook> meterBookList = meterBookService.getReaderBookListByCondition(readerId, searchCond);
		List<MeterBook> meterBookList = meterBookService.getMyMeterBookList(readerId, searchCond, traceIds);
		PageInfo<MeterBook> pageInfo = new PageInfo<>(meterBookList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBook meterBook : meterBookList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(meterBook);
			
			//customerMap.put("generateStatusStr", meterBook.getGenerateStatusStr());
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

		return TEMPLATE_PATH + "my_meterbook_table";
	}

	// ------------------------------ 全部表册部分 ------------------------------
	@RequestMapping(value = "/all-meter-book-main")
	public String allMeterBookMain(Model model, Long meterBookId) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		return TEMPLATE_PATH + "all_meterbook_main";
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
	@RequestMapping(value = "/all-meter-book-table")
	public String meterBookTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, Long readerId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<MeterBook> meterBookList = meterBookService.getUnAllotBookListByCondition(searchCond, traceIds, readerId);
		PageInfo<MeterBook> pageInfo = new PageInfo<>(meterBookList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBook meterBook : meterBookList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(meterBook);
			
			//customerMap.put("generateStatusStr", meterBook.getGenerateStatusStr());
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

		return TEMPLATE_PATH + "all_meterbook_table";
	}

	/**
	 * @Title: add
	 * @Description: 添加到我的表册
	 * @param userId
	 * @param meterBookIds
	 * @return
	 */
	@RequestMapping(value = "/add", produces = "application/json")
	@ResponseBody
	public Object add(Long userId, String meterBookIds) {

		String[] meterBookIdArr = meterBookIds.split(",");

		List<UserMeterBook> userMeterBookList = new ArrayList<>();
		for (String id : meterBookIdArr) {
			if (StringUtils.isNotBlank(id)) {
				Long meterBookId = Long.valueOf(id);

				UserMeterBook userMeterBook = new UserMeterBook();
				userMeterBook.setMeterBookId(meterBookId);
				userMeterBook.setUserId(userId);

				userMeterBookList.add(userMeterBook);
			}
		}

		int rows = userMeterBookService.insertBatch(userMeterBookList);

		if (rows > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}

		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: addAllMeterBook
	 * @Description: 批量添加表册
	 * @param searchCond
	 * @param traceIds
	 * @param readerId
	 * @return 
	 */
	@RequestMapping(value = "/all-meter-add", produces = "application/json")
	@ResponseBody
	public Object addAllMeterBook(String searchCond, String traceIds, Long readerId) {
		
		List<MeterBook> meterBookList = meterBookService.getUnAllotBookListByCondition(searchCond, traceIds, readerId);
		if(meterBookList.size() <= 0 || meterBookList==null) {
			return RequestResultUtil.getResultFail("表册不存在，请重新查询！");
		}
		List<UserMeterBook> userMeterBookList = new ArrayList<>();
		for (MeterBook temp : meterBookList) {
				
			Long meterBookId = temp.getId();
			
			UserMeterBook userMeterBook = new UserMeterBook();
			userMeterBook.setMeterBookId(meterBookId);
			userMeterBook.setUserId(readerId);
			userMeterBookList.add(userMeterBook);
		}

		int rows = userMeterBookService.insertBatch(userMeterBookList);

		if (rows > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}

		return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: save
	 * @Description: 从我的表册移除
	 * @param userId
	 * @param meterBookId
	 * @return
	 */
	@RequestMapping(value = "/remove", produces = "application/json")
	@ResponseBody
	public Object remove(Long userId, String meterBookIds) {


		String[] meterBookIdArr = meterBookIds.split(",");

		List<UserMeterBook> userMeterBookList = new ArrayList<>();
		for (String id : meterBookIdArr) {
			if (StringUtils.isNotBlank(id)) {
				Long meterBookId = Long.valueOf(id);

				UserMeterBook userMeterBook = new UserMeterBook();
				userMeterBook.setMeterBookId(meterBookId);
				userMeterBook.setUserId(userId);

				userMeterBookList.add(userMeterBook);
			}
		}

		int rows = userMeterBookService.deleteBatch(userMeterBookList);

		if (rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}

		return RequestResultUtil.getResultUpdateWarn();
	}
	@RequestMapping(value = "/all-meter-remove", produces = "application/json")
	@ResponseBody
	public Object removeAllMeterBook(String searchCond, String traceIds, Long readerId) {
		
		List<MeterBook> meterBookList = meterBookService.getAllotBookListByCondition(readerId, traceIds, searchCond);
		if(meterBookList.size() <= 0 || meterBookList==null) {
			return RequestResultUtil.getResultFail("表册不存在，请重新查询！");
		}
		List<UserMeterBook> userMeterBookList = new ArrayList<>();
		for (MeterBook temp : meterBookList) {
				
			Long meterBookId = temp.getId();
			
			UserMeterBook userMeterBook = new UserMeterBook();
			userMeterBook.setMeterBookId(meterBookId);
			userMeterBook.setUserId(readerId);
			userMeterBookList.add(userMeterBook);
		}

		int rows = userMeterBookService.deleteBatch(userMeterBookList);

		if (rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}

		return RequestResultUtil.getResultUpdateWarn();
	}

	/**
	 * @Title: loadAddMeterBookDialog
	 * @Description: 添加表册
	 * @param model
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/load-add-meter-book-dialog")
	public String loadAddMeterBookDialog(Long itemId, Model model) {

		// 查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		// 读取需要编辑的条目
		SysUsers currItem = usersService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem", currItem);

		return TEMPLATE_PATH + "add_meterbook_dialog";
	}

	/**
	 * @Title: addReaderMeterBook
	 * @Description: 添加抄表员的表册
	 * @param readerId
	 * @param locationBlockId
	 * @param locationBuildingId
	 * @param locationUnitId
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(Long readerId, String locationBlockCode, String locationBuildingCode,
			String locationUnitCode) {
		int rows = 0;
		if (StringUtils.isBlank(locationBuildingCode) && StringUtils.isBlank(locationUnitCode)) {// 只选择小区时
			
			rows = userMeterBookService.insertUserMeterBookByBlockCode(readerId, locationBlockCode);

		} else if (StringUtils.isBlank(locationUnitCode) && StringUtils.isNotBlank(locationBuildingCode)) {// 只选择小区和楼号
			
			String code = locationBlockCode + "-" + locationBuildingCode;
			rows = userMeterBookService.insertUserMeterBookByBuildingCode(readerId, code);
		} else {// 选择到单元时
			
			String code = locationBlockCode + "-" + locationBuildingCode + "-" + locationUnitCode;
			
			//验证表册是否已存在
			List<MeterBook> meterBookList = meterBookService.getMeterBookIdByCode(code);
			if(meterBookList==null && meterBookList.size()<0) {
				return RequestResultUtil.getResultAddSuccess("未创建表册，请先创建表册！");
			}
			
			rows = userMeterBookService.insertUserMeterBookByUnitCode(readerId, code);
		}

		return RequestResultUtil.getResultAddSuccess();
	}
	
	/**
	 * @Title: isAllot
	 * @Description: 判断是否分配表册
	 * @param meterBookId
	 * @return 
	 */
	private boolean isAllot(Long meterBookId) {
		UserMeterBook umb = new UserMeterBook();
		umb.setMeterBookId(meterBookId);
		List<UserMeterBook> tempUmbList = userMeterBookService.select(umb);
		if(tempUmbList!=null && tempUmbList.size()>0) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: update
	 * @Description: 修改
	 * @param meterBook
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(MeterBook meterBook) throws Exception {
		meterBookService.updateByPrimaryKeySelective(meterBook);
		return RequestResultUtil.getResultUpdateSuccess();
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
				// System.out.println(id);
				meterBookService.deleteMeterBook(id);
			}
		} catch (Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

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
		MeterBook book = meterBookService.selectByPrimaryKey(meterBookId);
		model.addAttribute("unitName", book.getName());
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
			customerMap.put("customerName", customer.getCustomerName());//
			customerMap.put("customerMobile", customer.getCustomerMobile());//
			customerMap.put("steelSealNo", meter.getSteelSealNo());//
			customerMap.put("place", meter.getPlace());//
			
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