package com.learnbind.ai.controller.workorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumWorkOrderStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.WorkOrderFunctionModuleConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.WorkOrder;
import com.learnbind.ai.model.WorkOrderSale;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.user.UsersService;
import com.learnbind.ai.service.usersroles.UsersRolesService;
import com.learnbind.ai.service.workorder.WorkOrderSaleService;
import com.learnbind.ai.service.workorder.WorkOrderService;


@Controller
@RequestMapping(value = "/work-order-allot")
public class WorkOrderAllotController {
	private static Log log = LogFactory.getLog(WorkOrderAllotController.class);
	private static final String TEMPLATE_PATH = "work_order/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private WorkOrderService workOrderService;  //工单配置
	@Autowired
	private UsersService usersService;  //用户
	@Autowired
	private WorkOrderSaleService workOrderSaleService;  //工单-用户配置
	@Autowired
	private DataDictService dataDictService;  //数据字典
	

	/** 
	*	@Title: starter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	public String starter(Model model, String functionModule, Integer status) {
		String title = "";
		if(StringUtils.isBlank(functionModule)) {
			functionModule = WorkOrderFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(WorkOrderFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		if(status ==0) {
			title = "分配工单";
		} else if (status == 1) {
			title = "处理工单";
		} else if (status == 2) {
			title = "完成工单";
		} else if (status == 3) {
			title = "结束工单";
		} else if (status == -1) {
			title = "工单管理";
		} else if (status == -2) {
			title = "创建工单";
		}
		model.addAttribute("status", status);
		model.addAttribute("title", title);
		return TEMPLATE_PATH + "work_order_starter";
	}

	/** 
	*	@Title: search 
	*	@Description: 主界面:控制面板及列表容器 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/main")
	/* @ResponseBody */
	public String search(Model model, String functionModule, Integer status) {
		if(StringUtils.isBlank(functionModule)) {
			functionModule = WorkOrderFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(WorkOrderFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		model.addAttribute("status", status);
		return TEMPLATE_PATH + "work_order_main";
	}

	/** 
	*	@Title: table 
	*	@Description: 加载用户列表(列表页面)
	*	@param @param model ModelView中传递数据的对象
	*	@param @param pageNum 页号
	*	@param @param pageSize 页大小
	*	@param @param searchCond 查询条件
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer status) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		if(status == -1 || status == -2) {
			status = null;
		}
		List<WorkOrder> workOrderList = new ArrayList<>();
		
		if(StringUtils.equals("售后人员", userBean.getRealname())) {
			workOrderList =  workOrderService.searchWorkOrderSale(searchCond, userBean.getId(), status);
		} else {
			workOrderList =  workOrderService.searchWorkOrder(searchCond, status);
		}
		
		PageInfo<WorkOrder> pageInfo = new PageInfo<>(workOrderList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> workOrderMapList = new ArrayList<>();
		for(WorkOrder workOrder : workOrderList) {
			Map<String, Object> workOrderMap = EntityUtils.entityToMap(workOrder);
			
			String users = workOrderSaleService.selectRealnameByWorkOrderId(workOrder.getId());
			//HTML字符解码
			String dealSituation = StringEscapeUtils.unescapeHtml(workOrder.getDealSituation());
			workOrderMap.put("typeStr", this.getDataDictValue(EnumDataDictType.WORK_ORDER_TYPE.getCode(), workOrder.getType()));//工单类型
			workOrderMap.put("statusStr", workOrder.getStatusStr());//工单状态
			workOrderMap.put("startTimeStr", workOrder.getStartTimeStr());//开始时间
			workOrderMap.put("endTimeStr", workOrder.getEndTimeStr());//开始时间
			workOrderMap.put("users", users);//分配人员
			workOrderMap.put("dealSituationStr", dealSituation);//工单执行情况
			
			workOrderMapList.add(workOrderMap);
		}
		

		// 传递如下数据至前台页面
		model.addAttribute("workOrderList", workOrderMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		//model.addAttribute("pageNum", pageNum);
		//model.addAttribute("pageSize", pageSize);
		model.addAttribute("status", status);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "work_order_table";
	}

	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadadddialog")
	public String loadAddDialog(Model model) {
		//加载数据字典-工单类型
		List<DataDict> workOrderTypeList = dataDictService.getListByTypeCode(DataDictType.WORK_ORDER_TYPE);
		model.addAttribute("workOrderTypeList", workOrderTypeList);
		
		return TEMPLATE_PATH + "order_dialog_add";
	}
	
	/** 
	*	@Title: addCheckMeter 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addWorkOrder(WorkOrder workOrder) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date date = new Date();
		workOrder.setStartTime(date);
		workOrder.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		workOrder.setOperator(userBean.getRealname());
		//HTML字符编码
		String dealSituation = StringEscapeUtils.escapeHtml(workOrder.getDealSituation());
		workOrder.setDealSituation(dealSituation);
		int row = workOrderService.insertSelective(workOrder);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteCheckMeter 
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteWorkOrder(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				WorkOrder workOrder = workOrderService.selectByPrimaryKey(id);
				workOrder.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				workOrderService.updateByPrimaryKeySelective(workOrder);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/** 
	*	@Title: loadModiDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiDialog(Long itemId, Model model) {
		//加载数据字典-工单类型
		List<DataDict> workOrderTypeList = dataDictService.getListByTypeCode(DataDictType.WORK_ORDER_TYPE);
		model.addAttribute("workOrderTypeList", workOrderTypeList);
		//读取需要编辑的条目
		WorkOrder currItem = workOrderService.selectByPrimaryKey(itemId);
		//HTML转义解码
		String dealSituation = StringEscapeUtils.unescapeHtml(currItem.getDealSituation());
		currItem.setDealSituation(dealSituation);
		model.addAttribute("currItem",currItem);
		
		List<SysUsers> userList = usersService.selectAll();
		model.addAttribute("userList",userList);
		Map<String, Object> customerMap = null;
		if(itemId!=null && itemId>0) {
			
			customerMap = EntityUtils.entityToMap(currItem);
			String userNames = workOrderSaleService.selectRealnameByWorkOrderId(itemId);
			customerMap.put("userNames", userNames);
			
		}
		
		model.addAttribute("workOrder", customerMap);
		
		
		return TEMPLATE_PATH + "order_dialog_edit";
	}

	/** 
	*	@Title: updatCheckMeter 
	*	@Description: 修改
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateWorkOrder(WorkOrder workOrder) throws Exception {
		//HTML转义编码
		String dealSituation = StringEscapeUtils.escapeHtml(workOrder.getDealSituation());
		workOrder.setDealSituation(dealSituation);
		workOrderService.updateByPrimaryKeySelective(workOrder);
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	
	//------------------分配工单--------------------
	/** 
	*	@Title: loadModiDialog 
	*	@Description: 加载分配工单对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadallotdialog")
	public String loadAllotDialog(Long workOrderId, Model model) {
		//读取需要编辑的条目
		WorkOrder currItem = workOrderService.selectByPrimaryKey(workOrderId);
		//Html转义解码
		String dealSituation = StringEscapeUtils.unescapeHtml(currItem.getDealSituation());
		currItem.setDealSituation(dealSituation);
		model.addAttribute("currItem",currItem);
		
		List<SysUsers> userList = usersService.selectAll();
		model.addAttribute("userList",userList);
		Map<String, Object> customerMap = null;
		if(workOrderId!=null && workOrderId>0) {
			
			customerMap = EntityUtils.entityToMap(currItem);
			String userNames = workOrderSaleService.selectRealnameByWorkOrderId(workOrderId);
			customerMap.put("userNames", userNames);
			
		}
		
		model.addAttribute("workOrder", customerMap);
		
		return TEMPLATE_PATH + "allot_dialog_edit";
	}
	
	
	/**
	 * @Title: addWorkorderUser
	 * @Description: 增加工单用户关联表数据
	 * @param groupId
	 * @param workId
	 * @return 
	 */
	@RequestMapping(value = "/add-workorder-user", produces = "application/json")
	@ResponseBody
	public Object addWorkorderUser(Long workOrderId, Long userId) {
		
		WorkOrderSale workOrderSale = new WorkOrderSale();
		workOrderSale.setWorkOrderId(workOrderId);
		workOrderSale.setUserId(userId);
		int rows = workOrderSaleService.insertSelective(workOrderSale);
		if(rows>0) {
			return RequestResultUtil.getResultAddSuccess();
		}
		
		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: deleteWorkorderUser
	 * @Description: 删除工单用户关联表数据
	 * @param groupId
	 * @param workId
	 * @return 
	 */
	@RequestMapping(value = "/delete-workorder-user", produces = "application/json")
	@ResponseBody
	public Object deleteWorkorderUser(Long workOrderId, Long userId) {
		
		WorkOrderSale workOrderSale = new WorkOrderSale();
		workOrderSale.setWorkOrderId(workOrderId);
		workOrderSale.setUserId(userId);
		int rows = workOrderSaleService.delete(workOrderSale);
		if(rows>0) {
			return RequestResultUtil.getResultDeleteSuccess();
		}
		
		return RequestResultUtil.getResultDeleteWarn();
	}
	
	
	/** 
	*	@Title: allotWorkOrder 
	*	@Description: 分配工单
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/allot-work", produces = "application/json")
	@ResponseBody
	public Object allotWorkOrder(Long workOrderId, String remark, String dealSituation) throws Exception {
		int rows = 0;
		WorkOrder workOrder = workOrderService.selectByPrimaryKey(workOrderId);
		//工单变为已分配状态
		workOrder.setStatus(EnumWorkOrderStatus.ALLOTED.getValue());
		//HTML转义编码
		String newdealSituation = StringEscapeUtils.escapeHtml(dealSituation);
		workOrder.setDealSituation(newdealSituation);
		workOrder.setRemark(remark);
		rows = workOrderService.updateByPrimaryKeySelective(workOrder);
		if (rows>0) {
			return RequestResultUtil.getResultUpdateSuccess();
		} else {
			return RequestResultUtil.getResultUpdateWarn();
		}
		
	}
	//------------------处理工单
	/** 
	*	@Title: loadDealDialog 
	*	@Description: 加载处理工单对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loaddealdialog")
	public String loadDealDialog(Long workOrderId, Model model) {
		//读取需要编辑的条目
		WorkOrder currItem = workOrderService.selectByPrimaryKey(workOrderId);
		//Html转义解码
		String dealSituation = StringEscapeUtils.unescapeHtml(currItem.getDealSituation());
		currItem.setDealSituation(dealSituation);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "deal_dialog_edit";
	}
	
	/** 
	*	@Title: dealWorkOrder 
	*	@Description: 处理工单
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/deal-work", produces = "application/json")
	@ResponseBody
	public Object dealWorkOrder(Long workOrderId, String dealSituation) throws Exception {
		int rows = 0;
		WorkOrder workOrder = workOrderService.selectByPrimaryKey(workOrderId);
		//工单变为处理中状态
		workOrder.setStatus(EnumWorkOrderStatus.DEALING.getValue());
		//HTML转义编码
		String newdealSituation = StringEscapeUtils.escapeHtml(dealSituation);
		workOrder.setDealSituation(newdealSituation);
		rows = workOrderService.updateByPrimaryKeySelective(workOrder);
		if (rows<0) {
			return RequestResultUtil.getResultUpdateWarn();
		}
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	//------------------完成工单--------------------
	/** 
	*	@Title: loadApplyDialog 
	*	@Description: 加载完成工单对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadapplydialog")
	public String loadApplyDialog(Long workOrderId, Model model) {
		//读取需要编辑的条目
		WorkOrder currItem = workOrderService.selectByPrimaryKey(workOrderId);
		//Html转义解码
		String dealSituation = StringEscapeUtils.unescapeHtml(currItem.getDealSituation());
		currItem.setDealSituation(dealSituation);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "apply_dialog_edit";
	}
	
	/** 
	*	@Title: applyWorkOrder 
	*	@Description: 完成工单
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/apply-work", produces = "application/json")
	@ResponseBody
	public Object applyWorkOrder(Long workOrderId, String dealSituation) throws Exception {
		int rows = 0;
		WorkOrder workOrder = workOrderService.selectByPrimaryKey(workOrderId);
		//工单变为完成状态
		workOrder.setStatus(EnumWorkOrderStatus.APPLYING.getValue());
		//HTML转义编码
		String newdealSituation = StringEscapeUtils.escapeHtml(dealSituation);
		workOrder.setDealSituation(newdealSituation);
		rows = workOrderService.updateByPrimaryKeySelective(workOrder);
		if (rows<0) {
			return RequestResultUtil.getResultUpdateWarn();
		}
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	//******************结束工单
	/** 
	*	@Title: loadCompleteDialog 
	*	@Description: 加载完成工单对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadcompletedialog")
	public String loadCompleteDialog(Long workOrderId, Model model) {
		//读取需要编辑的条目
		WorkOrder currItem = workOrderService.selectByPrimaryKey(workOrderId);
		//Html转义解码
		String dealSituation = StringEscapeUtils.unescapeHtml(currItem.getDealSituation());
		currItem.setDealSituation(dealSituation);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "complete_dialog_edit";
	}
	/** 
	*	@Title: completeWorkOrder 
	*	@Description: 结束工单
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/complete-work", produces = "application/json")
	@ResponseBody
	public Object completeWorkOrder(Long workOrderId, String dealSituation) throws Exception {
		int rows = 0;
		WorkOrder workOrder = workOrderService.selectByPrimaryKey(workOrderId);
		//工单变为已完成状态
		workOrder.setStatus(EnumWorkOrderStatus.COMPLETED.getValue());
		//HTML转义编码
		String newdealSituation = StringEscapeUtils.escapeHtml(dealSituation);
		workOrder.setDealSituation(newdealSituation);
		rows = workOrderService.updateByPrimaryKeySelective(workOrder);
		if (rows<0) {
			return RequestResultUtil.getResultUpdateWarn();
		}
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	/** 
	*	@Title: loadDealSituationDialog 
	*	@Description: 加载工单处理情况详情对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/load-dealsituation-detail")
	public String loadDealSituationDialog(Long workOrderId, Model model) {
		//读取需要编辑的条目
		WorkOrder currItem = workOrderService.selectByPrimaryKey(workOrderId);
		//Html转义解码
		String dealSituation = StringEscapeUtils.unescapeHtml(currItem.getDealSituation());
		currItem.setDealSituation(dealSituation);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "deal_situation_detail";
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