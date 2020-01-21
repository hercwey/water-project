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

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumBookUserType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumMeterBookGenerateStatus;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.util.pinyin4j.Pinyin4jUtils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.meterbook
 *
 * @Title: MeterBookController.java
 * @Description: 表册前端控制器
 *
 * @author Administrator
 * @date 2019年6月10日 下午7:36:53
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/meter-book")
public class MeterBookController {

	private static Log log = LogFactory.getLog(MeterBookController.class);
	private static final String TEMPLATE_PATH = "meterbook/"; // 页面目录
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
			
			customerMap.put("createTimeStr", meterBook.getCreateTimeStr());
			customerMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meterBook.getFactory()));//水表生产厂家
			
			//抄表方式
			String readModeStr = EnumReadMode.getName(meterBook.getReadMode());
			customerMap.put("readModeStr", readModeStr);
			//表册用户类型
			String bookUserTypeStr = EnumBookUserType.getValue(meterBook.getBookUserType());
			customerMap.put("bookUserTypeStr", bookUserTypeStr);
			
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

	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param meterBook
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(String traceIds) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userBean==null) {
			return RequestResultUtil.getResultFail("请重新登录！");
		}
		//Location location = locationService.selectByPrimaryKey(Long.valueOf(traceIds));
		//
		List <Location> locationList = locationService.getUnitListByTraceIds(traceIds);
		int rows = 0;
			
		rows = meterBookService.createMeterBookByUnitId(userBean.getId(), userBean.getRealname(),locationList, null);
		
		if(rows > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn("未绑定表计，不需要创建表册！");
	}

	/**
	 * @Title: loadModiDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadModiDialog(Model model, Long itemId) {

		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		model.addAttribute("readModeList", readModeList);
		List<DataDict> factoryList = dataDictService.getListByTypeCode(DataDictType.METER_FACTORY);
		model.addAttribute("factoryList", factoryList);
		EnumBookUserType[] bookUserTypeArr = EnumBookUserType.values();
		model.addAttribute("bookUserTypeArr", bookUserTypeArr);
		if(itemId!=null) {
			//读取需要编辑的条目
			MeterBook currItem = meterBookService.selectByPrimaryKey(itemId);
			model.addAttribute("currItem", currItem);
			String address = locationService.getPlace(currItem.getTraceIds());
			model.addAttribute("address", address);
		}else {
			model.addAttribute("currItem", null);
		}

		return TEMPLATE_PATH + "meterbook_dialog_edit";
	}
	
	/**
	 * @Title: manualInsert
	 * @Description: 手动添加表册
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/manual-insert", produces = "application/json")
	@ResponseBody
	public Object manualInsert(MeterBook meterBook) {
		int rows = 1;
		Date sysdate = new Date();
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userBean==null) {
			return RequestResultUtil.getResultFail("请重新登录！");
		}
		String pyCodeU = "";
		try {
			pyCodeU = Pinyin4jUtils.toPyFirstUppercase(meterBook.getName());
			
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		} 
		Example example = new Example(MeterBook.class);
		example.createCriteria().andEqualTo("name", meterBook.getName()).orEqualTo("code", pyCodeU);
		List<MeterBook> meterBookList = meterBookService.selectByExample(example);
		if(meterBookList.size()>0) {
			return RequestResultUtil.getResultAddWarn("表册名称重复，请重新输入");
		}
		
		meterBook.setCreateTime(sysdate);
		meterBook.setGenerateStatus(EnumMeterBookGenerateStatus.MANUAL.getValue());//手动添加表册
		meterBook.setOperatorId(userBean.getId());
		meterBook.setOperatorName(userBean.getRealname());
		meterBook.setModifiedDate(sysdate);
		
		String[] traceIdArr = meterBook.getTraceIds().split("-");
		//获取小区信息
		Location locationBlock = this.getLocationBlock(traceIdArr);
		//表册typeCode及typeName为小区简拼及名称
		meterBook.setTypeCode(locationBlock.getPycode());
		meterBook.setTypeName(locationBlock.getName());
		meterBook.setCode(pyCodeU);
		
		rows = meterBookService.insertSelective(meterBook);	
		
		if(rows > 0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn("未绑定表计，不需要创建表册！");
	}
	
	/**
	 * @Title: getLocationBlock
	 * @Description: 获取小区地理位置
	 * @param traceIdArr
	 * @return 
	 */
	private Location getLocationBlock(String[] traceIdArr) {
		
		final String BLOCK = "BLOCK";
		List<Location> locationBlockList = locationService.getLocation(BLOCK, traceIdArr);
		if(locationBlockList!=null && locationBlockList.size()>0) {
			return locationBlockList.get(0);
		}
		return null;
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
		
		Example example = new Example(MeterBook.class);
		example.createCriteria().andEqualTo("name", meterBook.getName()).orEqualTo("code", meterBook.getCode()).andNotEqualTo("id", meterBook.getId());;
		List<MeterBook> meterBookList = meterBookService.selectByExample(example);
		
		
		if(meterBookList.size()>0) {
			return RequestResultUtil.getResultAddWarn("表册名称重复，请重新输入");
		} else {
			meterBookService.updateByPrimaryKeySelective(meterBook);
		}
		
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
	
	//-------------------------------批量修改表册部分------------------------------------------------
	/**
	 * @Title: loadModiDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/load-batch-edit-dialog")
	public String loadBatchModiDialog(Model model, String traceIds, String searchCond) {

		//查询小区
//		List<Location> locationList = locationService.getBlockListByPid(null);
//		model.addAttribute("locationList", locationList);
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		model.addAttribute("readModeList", readModeList);
		List<DataDict> factoryList = dataDictService.getListByTypeCode(DataDictType.METER_FACTORY);
		model.addAttribute("factoryList", factoryList);
		EnumBookUserType[] bookUserTypeArr = EnumBookUserType.values();
		model.addAttribute("bookUserTypeArr", bookUserTypeArr);
		
		model.addAttribute("traceIds", traceIds);
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "meterbook_batch_edit_dialog";
	}
	
	/**
	 * @Title: batchUpdateMeterBook
	 * @Description: 批量更新表册
	 * @param traceIds
	 * @param searchCond
	 * @param factory
	 * @param readMode
	 * @param bookUserType
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/batch-update-meterbook", produces = "application/json")
	@ResponseBody
	public Object batchUpdateMeterBook(String traceIds, String searchCond, String factory, String readMode, Integer bookUserType) throws Exception {
		
		try {
			Date sysDate = new Date();//系统时间
			
			Example example = new Example(MeterBook.class);
			Criteria criteria = example.createCriteria();
			criteria.andLike("traceIds", traceIds+"%");
			if(StringUtils.isNotBlank(searchCond)) {
				criteria.andLike("name", "%"+searchCond+"%");
			}
			List<MeterBook> meterBookList = meterBookService.selectByExample(example);
			if(meterBookList!=null && meterBookList.size()>0) {
				for(MeterBook book : meterBookList) {
					book.setFactory(factory);
					book.setReadMode(readMode);
					book.setBookUserType(bookUserType);
					book.setModifiedDate(sysDate);
					meterBookService.updateByPrimaryKeySelective(book);
				}
			}else {
				return RequestResultUtil.getResultFail("未查询到需要更新的表册！");
			}
			
			return RequestResultUtil.getResultSuccess("批量修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("批量更新失败，请重新操作！");
	}
	
	// ------------------------------ 表册的选择表计部分部分 ------------------------------
	
	/**
	 * @Title: main
	 * @Description: 表册间的表计调整界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-meter-book-select-meter-main")
	public String loadMeterBookSelectMeterMain(Model model, Long meterBookId) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		MeterBook meterBook = meterBookService.selectByPrimaryKey(meterBookId);
		model.addAttribute("unitName", meterBook.getName());
		return TEMPLATE_PATH + "meterbook_select_meter_main";
	}
	
	// ------------------------------ 我的表册包含的表计部分 ------------------------------
	@RequestMapping(value = "/my-meter-book-select-main")
	public String myMeterBookMain(Model model, Long meterBookId) {
		
		//MeterBook meterBook = meterBookService.selectByPrimaryKey(meterBookId);
		//model.addAttribute("unitName", meterBook.getName());
		return TEMPLATE_PATH + "my_meterbook_select_main";
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
	@RequestMapping(value = "/my-meter-book-select-table")
	public String myMeterBookTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long meterBookId) {

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

		return TEMPLATE_PATH + "my_meterbook_select_table";
	}
	
	
	/**
	 * @Title: loadEditMeterSortDialog
	 * @Description:调整表册中表计顺序对话框
	 * @param model
	 * @param meterBookId
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-meter-sort-dialog")
	public String loadEditMeterSortDialog(Model model, Long meterBookId) {

		List<MeterBookMeter>  mbList = meterBookMeterService.searchMeterBookMeterList(null, meterBookId);
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBookMeter mb : mbList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(mb);
			//获取客户信息
			Customers customer = customersService.selectByPrimaryKey(mb.getCustomerId());
			//获取表计信息
			Meters meter = metersService.selectByPrimaryKey(mb.getMeterId());
			customerMap.put("customerName", customer.getCustomerName());//用水状态
			customerMap.put("customerMobile", customer.getCustomerMobile());//用水状态
			customerMap.put("steelSealNo", meter.getSteelSealNo());//用水状态
			customerMap.put("place", meter.getPlace());//用水状态
			customerMap.put("sortValue", meter.getSortValue());//排序字段
			
			customerMapList.add(customerMap);
		}
		model.addAttribute("mbList", customerMapList);
		return TEMPLATE_PATH + "edit_meter_sort_dialog";
	}
	
	@RequestMapping(value = "/meter-sort-table")
	public String meterSortTable(Model model, Long meterBookId) {

		List<MeterBookMeter>  mbList = meterBookMeterService.searchMeterBookMeterList(null, meterBookId);
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(MeterBookMeter mb : mbList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(mb);
			//获取客户信息
			Customers customer = customersService.selectByPrimaryKey(mb.getCustomerId());
			//获取表计信息
			Meters meter = metersService.selectByPrimaryKey(mb.getMeterId());
			customerMap.put("customerName", customer.getCustomerName());//用水状态
			customerMap.put("customerMobile", customer.getCustomerMobile());//用水状态
			customerMap.put("steelSealNo", meter.getSteelSealNo());//用水状态
			customerMap.put("place", meter.getPlace());//用水状态
			customerMap.put("sortValue", meter.getSortValue());//排序字段
			
			customerMapList.add(customerMap);
		}
		model.addAttribute("mbList", customerMapList);
		return TEMPLATE_PATH + "meter_sort_table";
		
	}
	
	/**
	 * @Title: upMeters
	 * @Description: 上移表计
	 * @param moveMeterId
	 * @param preMeterId
	 * @return 
	 */
	@RequestMapping(value = "/edit-meter-sort", produces = "application/json")
	@ResponseBody
	public Object editMeterSort(String sortMeterArr) {
		if(sortMeterArr == null || sortMeterArr == "") {
			return RequestResultUtil.getResultAddWarn("没有找到需要调整顺序的表计");
		}
		int rows = 0;
		List<Meters> metersList = JSON.parseArray(sortMeterArr, Meters.class);
		for(Meters meter : metersList) {
			Meters temp = metersService.selectByPrimaryKey(meter.getId());
			temp.setSortValue(meter.getSortValue());
			rows = metersService.updateByPrimaryKeySelective(temp);
		}
		if(rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess("调整顺序成功");
		}

		return RequestResultUtil.getResultUpdateWarn("调整顺序失败");
	}

	
	// ------------------------------ 全部表册包含的表计部分 ------------------------------
	
	@RequestMapping(value = "/all-meter-book-select-main")
	public String allMeterBookMain(Model model, Long meterBookId) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		return TEMPLATE_PATH + "all_meterbook_select_main";
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
	@RequestMapping(value = "/add-meters", produces = "application/json")
	@ResponseBody
	public Object addMeters(Long meterBookId, String meterIds) {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String[] meterIdArr = meterIds.split(",");
		//在表册中移动表计时，修改表册状态
		MeterBook book = meterBookService.selectByPrimaryKey(meterBookId); 
		book.setGenerateStatus(EnumMeterBookGenerateStatus.MANUAL.getValue());
		
		List<MeterBookMeter> meterBookMeterList = new ArrayList<>();
		for (String id : meterIdArr) {
			if (StringUtils.isNotBlank(id)) {
				Long meterId = Long.valueOf(id);
				Customers customer = customersService.selectCustomerBidMeterInfo(meterId);
				MeterBookMeter meterBookMeter = new MeterBookMeter();
				meterBookMeter.setMeterBookId(meterBookId);
				meterBookMeter.setMeterId(meterId);
				meterBookMeter.setCustomerId(customer.getId());

				meterBookMeterList.add(meterBookMeter);
			}
		}

		int rows = meterBookMeterService.insertBatch(meterBookMeterList);
		
		if (rows > 0) {
			meterBookService.updateByPrimaryKeySelective(book);
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
		//在表册中移动表计时，修改表册状态
		MeterBook book = meterBookService.selectByPrimaryKey(meterBookId); 
		book.setGenerateStatus(EnumMeterBookGenerateStatus.MANUAL.getValue());

		List<MeterBookMeter> meterBookMeterList = new ArrayList<>();
		for (String id : meterBookMeterIdArr) {
			if (StringUtils.isNotBlank(id)) {
				Long meterBookMeterId = Long.valueOf(id);
				MeterBookMeter meterBookMeter = meterBookMeterService.selectByPrimaryKey(meterBookMeterId);

				meterBookMeterList.add(meterBookMeter);
			}
		}

		int rows = meterBookMeterService.deleteBatch(meterBookMeterList);

		if (rows > 0) {
			meterBookService.updateByPrimaryKeySelective(book);
			return RequestResultUtil.getResultUpdateSuccess();
		}

		return RequestResultUtil.getResultUpdateWarn();
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
			
			if(StringUtils.isBlank(typeCode) || StringUtils.isBlank(key)) {
				return null;
			}
			
			DataDict searchObj = new DataDict();
			searchObj.setTypeCode(typeCode);
			searchObj.setKey(key);
			List<DataDict> dictList = dataDictService.select(searchObj);
			if(dictList!=null && dictList.size()>0) {
				DataDict dict = dictList.get(0);
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