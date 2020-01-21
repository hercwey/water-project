package com.learnbind.ai.service.notify.impl;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.DocumentException;
import com.learnbind.ai.bean.NotifyGroupBean;
import com.learnbind.ai.bean.NotifyMeterRecordBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumInvoiceType;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.enumclass.EnumWaterNotifyStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.dao.WaterNotifyMapper;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBillInfo;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.NotifyGroup;
import com.learnbind.ai.model.NotifyGroupMeter;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.model.WaterNotify;
import com.learnbind.ai.model.WaterNotifyDetail;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BillService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.notify.GenerateNotify;
import com.learnbind.ai.service.notify.NotifyGroupMeterService;
import com.learnbind.ai.service.notify.NotifyGroupRuleParserService;
import com.learnbind.ai.service.notify.NotifyGroupService;
import com.learnbind.ai.service.notify.WaterNotifyDetailService;
import com.learnbind.ai.service.notify.WaterNotifyService;
import com.learnbind.ai.service.printer.PrinterService;
import com.learnbind.ai.util.pdf.PrintFile;

import tk.mybatis.mapper.entity.Example;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     WaterPriceServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class WaterNotifyServiceImpl extends AbstractBaseService<WaterNotify, Long> implements  WaterNotifyService {
	
	@Autowired
	public WaterNotifyMapper waterNotifyMapper;
	@Autowired
	public CustomerMeterService customerMeterService;
	@Autowired
	public NotifyGroupService notifyGroupService;
	@Autowired
	public NotifyGroupMeterService notifyGroupMeterService;
	@Autowired
	public NotifyGroupRuleParserService notifyGroupRuleParserService;
	@Autowired
	public MeterRecordService meterRecordService;
	@Autowired
	public MetersService metersService;
	@Autowired
	public PartitionWaterService partitionWaterService;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
	@Autowired
	public WaterNotifyDetailService waterNotifyDetailService;
	@Autowired
	public CustomersService customersService;
	@Autowired
	public BillService billService;
	@Autowired
	public GenerateNotify generateNotify;
	@Autowired
	private PrinterService printerService;// 打印机配置
	@Autowired
	private PrintFile printFile;
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;
	@Autowired
	private PartitionRuleUtil partitionRuleUtil;//分水量规则工具类

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WaterNotifyServiceImpl(WaterNotifyMapper mapper) {
		this.waterNotifyMapper=mapper;
		this.setMapper(mapper);
	}


	@Override
	public List<WaterNotify> searchCond(String searchCond, String period, Integer sortMethod, Integer arrearsAmount) {
		return waterNotifyMapper.searchCond(searchCond, period, sortMethod, arrearsAmount);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: generateNotifySingle
	 * @Description: 生成通知单
	 * @param customerId
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.notify.WaterNotifyService#generateNotifySingle(java.lang.Long, java.lang.String)
	 */
	@Override
	public int generateNotifySingle(Long customerId, String period) {
		int rows = -1;
		//获取客户绑定的所有表计ID
		List<Long> meterIdList = this.getCustomerBindMeterIdList(customerId);
		//获取该客户的分组
		List<NotifyGroupBean> groupList = this.getNotifyGroupBeanList(customerId);
		
		//获取分组后的表计ID集合
		List<List<Long>> resultList = notifyGroupRuleParserService.parser(meterIdList, groupList);
		for(List<Long> temp : resultList) {
			Collections.sort(temp);
			//判断通知单是否已经生成，如果已经生成过，则先执行报废
			boolean flag = this.isExistWaterNotify(temp, customerId, period);
			//生成通知单
			rows = this.generateWaterNotify(temp, customerId, period);
		}
		return rows;
	}
	
	/**
	 * @Title: getCustomerBindMeterIdList
	 * @Description: 获取客户的所有表计ID
	 * @param customerId
	 * @return 
	 */
	public List<Long> getCustomerBindMeterIdList(Long customerId){
		List<Long> meterIdList = new ArrayList();
		List<CustomerMeter> cmList = customerMeterService.getListByCustomerId(customerId);
		for(CustomerMeter cm : cmList) {
			meterIdList.add(cm.getMeterId());
		}
		 
		return meterIdList;
	}
	
	/**
	 * @Title: getNotifyGroupBeanList
	 * @Description: 获取客户分组集合
	 * @param customerId
	 * @return 
	 */
	public List<NotifyGroupBean> getNotifyGroupBeanList(Long customerId){
		List<NotifyGroupBean> groupBeanList = new ArrayList();
		List<NotifyGroup> groupList = notifyGroupService.getCustomerGroupList(customerId);
		for(NotifyGroup group : groupList) {
			NotifyGroupBean groupBean = new NotifyGroupBean();
			List<NotifyGroupMeter> groupMeterList = notifyGroupMeterService.getGroupMeterList(group.getId());
			groupBean.setCustomerId(customerId);
			groupBean.setIncludeFlag(group.getIncludeFlag());
			groupBean.setName(group.getName());
			groupBean.setType(group.getType());
			groupBean.setGroupMeterList(groupMeterList);
			groupBeanList.add(groupBean);
		}
		return groupBeanList;
		 
	}
	
	/**
	 * @Title: isExistWaterNotify
	 * @Description:判断通知单是否已经存在
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public boolean isExistWaterNotify(List<Long> meterIdList , Long customerId, String period) {

        
        Example example = new Example(WaterNotify.class);
        String meterIdStr= "";
		for(Long meterId : meterIdList) {
			meterIdStr = meterId+","+meterIdStr;
		}
		meterIdStr = this.getStr(",", meterIdStr);
        example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("meterIds", meterIdStr).
        andEqualTo("status", EnumWaterNotifyStatus.NORMAL.getValue()).andEqualTo("period", period);
        List<WaterNotify> notifyList = this.selectByExample(example);
        List<Long> notifyIds = new ArrayList<>();
       
        if(notifyList.size() > 0) {
	    	 for(WaterNotify notify : notifyList) {
	         	notifyIds.add(notify.getId());
	         }
	    	 this.destoryWaterNotify(notifyIds);
	    	 return true;
        }
		return false;
	}

	/**
	 * @Title: generateWaterNotify
	 * @Description:生成通知单
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public int generateWaterNotify(List<Long> meterIdList , Long customerId, String period) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BigDecimal zero = new BigDecimal("0");
		int rows = 0;
		
		//判断返回的表计中是否包含特殊规则
		List<Long> newMeterIdList = this.getVirtualMeterList(meterIdList, period);
		//查询客户的抄表记录
		List<MeterRecord> meterRecordList = meterRecordService.getCustomerMeterRecord(newMeterIdList, null, period);
		//List<NotifyMeterRecordBean> meterRecordBeanList = new ArrayList<>();
		List<Long> meterRecordIdList = new ArrayList<>();
		for(MeterRecord record : meterRecordList) {
			meterRecordIdList.add(record.getId());
//			Meters meter = metersService.selectByPrimaryKey(record.getMeterId());
//			NotifyMeterRecordBean bean = new NotifyMeterRecordBean();
//			bean.setMeterId(record.getMeterId());
//			bean.setCurrRead(record.getCurrRead());
//			bean.setPreRead(record.getPreRead());
//			bean.setCurrAmount(record.getCurrAmount());
//			bean.setDescription(meter.getDescription());
//			meterRecordBeanList.add(bean);
		}
		
		//查询客户本期的分水量记录
		List<PartitionWater> partWaterList = partitionWaterService.getCustomerPartWater(meterIdList, customerId, period);
		
		//查询本期欠费
		BigDecimal sumBaseAmount = zero;
		BigDecimal sumSewageAmount = zero;
		for(PartitionWater part : partWaterList) {
			if(part.getAccountItemId() == null) {//没有开账不生成通知单
				return -3;
			}
			CustomerAccountItem account = customerAccountItemService.selectByPrimaryKey(part.getAccountItemId());
			//计算这个账单基础水费和污水总欠费
			BigDecimal oweBaseAmount = customerAccountItemService.getBaseFeeOweAmount(account.getDebitAssistant(), account.getBaseWaterFee());
			BigDecimal oweSewageAmount = customerAccountItemService.getSewageFeeOweAmount(account.getDebitAssistant(), account.getSewageWaterFee());
			sumBaseAmount = BigDecimalUtils.add(sumBaseAmount, oweBaseAmount);
			sumSewageAmount = BigDecimalUtils.add(sumSewageAmount, oweSewageAmount);
		}
		//查询客户往期分水量记录
		List<PartitionWater> pastPartWaterList = partitionWaterService.getCustomerPastPartWater(meterIdList, customerId, period);
		//查询往期欠费
		BigDecimal pastBaseOweAmount = zero;
		BigDecimal pastSewageOweAmount = zero;
		for(PartitionWater part : pastPartWaterList) {
			CustomerAccountItem account = customerAccountItemService.selectByPrimaryKey(part.getAccountItemId());
			//计算这个账单基础水费和污水总欠费
			BigDecimal pastOweBaseAmount = customerAccountItemService.getBaseFeeOweAmount(account.getDebitAssistant(), account.getBaseWaterFee());
			BigDecimal pastOweSewageAmount = customerAccountItemService.getSewageFeeOweAmount(account.getDebitAssistant(), account.getSewageWaterFee());
			pastBaseOweAmount = BigDecimalUtils.add(pastBaseOweAmount, pastOweBaseAmount);
			pastSewageOweAmount = BigDecimalUtils.add(pastSewageOweAmount, pastOweSewageAmount);
		}
		
		//总欠费金额
		BigDecimal totalOweAmount = BigDecimalUtils.add(sumBaseAmount, sumSewageAmount);
		totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastBaseOweAmount);
		totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastSewageOweAmount);
		// 查询客户余额
		BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customerId);
		String meterIdStr= "";
		for(Long meterId : meterIdList) {
			meterIdStr = meterId+","+meterIdStr;
		}
		meterIdStr = this.getStr(",", meterIdStr);
		
		//生成通知单
		WaterNotify notify = new WaterNotify();
		notify.setCustomerId(customerId);
		notify.setCreateDate(new Date());
		notify.setOperator(userBean.getRealname());
		notify.setPeriod(period);
		notify.setMeterIds(meterIdStr);
		notify.setMeterRecord(JSON.toJSONString(meterRecordIdList));
		notify.setSumBaseAmount(sumBaseAmount);
		notify.setSumSewageAmount(sumSewageAmount);
		notify.setPastBaseOweAmount(pastBaseOweAmount);
		notify.setPastSewageOweAmount(pastSewageOweAmount);
		notify.setTotalOweAmount(totalOweAmount);
		notify.setCustomerBalance(customerBalance);
		notify.setMeterReader(userBean.getRealname());//抄表员
		notify.setStatus(EnumWaterNotifyStatus.NORMAL.getValue());//水费通知单状态：正常
		rows = this.insertSelective(notify);
		if(rows >0) {
			//添加通知单明细
			List<Map<String, Object>> pwMapList = this.getPartList(partWaterList);
			for(Map<String, Object> partMap : pwMapList) {
				//通知单详情
				WaterNotifyDetail detail = new WaterNotifyDetail();
				detail.setWaterNotifyId(notify.getId());
				detail.setPartWaterId(partMap.get("partWaterId").toString());
				detail.setBasePrice(new BigDecimal(partMap.get("basePrice").toString()));
				detail.setSewagePrice(new BigDecimal(partMap.get("sewagePrice").toString()));
				detail.setRealWaterAmount(new BigDecimal(partMap.get("realWaterAmount").toString()));
				waterNotifyDetailService.insertSelective(detail);
			}
		}
		return rows;
	}
	
	
	/**
	 * @Title: getVirtualMeterList
	 * @Description: 获取虚表配置规则的表计
	 * @param meterIdList
	 * @param period
	 * @return 
	 */
	private List<Long> getVirtualMeterList(List<Long> meterIdList, String period){
		List<Long> newList = new ArrayList<>();
		for(Long temp : meterIdList) {
			newList.add(temp);
		}
		for(Long meterId : meterIdList) {
			Meters meter = metersService.selectByPrimaryKey(meterId);
			if(meter.getVirtualReal() == EnumMeterVirtualReal.VIRTUAL_METER.getValue()) {
				//如果表计中包含虚表，则获取虚表规则的所有记录
				List<MeterPartWaterRule> ruleList = meterPartWaterRuleService.getMeterRule(meterId);
				for(MeterPartWaterRule rule : ruleList) {
					List<String> formalParamList = partitionRuleUtil.getRuleFormalParams(rule.getRuleReal());//获取表达式中的形参
					for(String parm : formalParamList) {
						int begin = parm.indexOf("_")+1;
						int end = parm.length();
						Long id = Long.valueOf(parm.substring(begin, end));
						newList.add(id);
					}
				}
				
			}
		}
		// Set集合去重
		Set set1 = new HashSet();
		List newList1 = new ArrayList();
		for (Long temp : newList) {
			if (set1.add(temp)) {
				newList1.add(temp);
			}
		}
		
		return newList1;
	}
	
	/**
	 * @Title: getPartList
	 * @Description: 获取分水量明细集合
	 * @param partWaterList
	 * @return 
	 */
	public List<Map<String, Object>> getPartList(List<PartitionWater> partWaterList) {
		for(PartitionWater partWater : partWaterList) {
			//判断该客户是否分账
			List<CustomerAccountItem> accountItemList = customerAccountItemService.getSubAccountItem(partWater.getAccountItemId());
			if(accountItemList.size() > 0) {
				for(CustomerAccountItem item : accountItemList) {
					if(item.getCustomerId().equals(partWater.getCustomerId())) {
						BigDecimal waterPrice = BigDecimalUtils.add(partWater.getBasePrice(), partWater.getTreatmentFee());
						partWater.setWaterAmount(BigDecimalUtils.divide(item.getCreditAmount(), waterPrice));
					}
				}
			}
		}
		
		List<PartitionWater> resultPwList = new ArrayList<>();
		List<Map<String, Object>> pwMapList = new ArrayList<>();
		for (int i = 0; i < partWaterList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			List<Long> partIdList = new ArrayList<>();
			PartitionWater pw1 = partWaterList.get(i);
			partIdList.add(pw1.getId());
			
			BigDecimal realWaterAmount = pw1.getRealWaterAmount();
			List<PartitionWater> pwList = new ArrayList<>();
			pwList.addAll(partWaterList);
			int removeCount = 0;
			for (int j = 0; j < pwList.size(); j++) {
				PartitionWater pw2 = pwList.get(j);
				if (!pw1.getId().equals(pw2.getId()) && BigDecimalUtils.equals(pw1.getBasePrice(), pw2.getBasePrice())
						&& BigDecimalUtils.equals(pw1.getTreatmentFee(), pw2.getTreatmentFee())) {
					realWaterAmount = BigDecimalUtils.add(realWaterAmount, pw2.getRealWaterAmount());
					partIdList.add(pw2.getId());
					partWaterList.remove((j - removeCount));
					removeCount = removeCount + 1;
				}

			}
			map.put("partWaterId", partIdList.toString());
			map.put("realWaterAmount", realWaterAmount);
			map.put("basePrice", pw1.getBasePrice());
			map.put("sewagePrice", pw1.getTreatmentFee());
			pwMapList.add(map);
			pw1.setRealWaterAmount(realWaterAmount);
			resultPwList.add(pw1);
		}

		return pwMapList;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: generateNotifyBatch
	 * @Description: 批量生成通知单实体
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param customerIdStr
	 * @return 
	 * @see com.learnbind.ai.service.notify.WaterNotifyService#generateNotifyBatch(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int generateNotifyBatch(String searchCond, String traceIds, String period, String customerIdStr) {
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
		int rows = 0;
		List<Customers> customersList = new ArrayList<>();
		if (StringUtils.isBlank(customerIdStr)) {// 批量打印时根据查询条件打印
			customersList = customersService.getDefaultUnitCustomerList(traceIds, searchCond, customerType);
		} else {// 批量打印时勾选进行打印
			String[] s1 = customerIdStr.split(",");
			for (String customerId : s1) {
				Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
				customersList.add(customer);
			}
		}
		
		for (Customers customer : customersList) {
			rows = this.generateNotifySingle(customer.getId(), period);
		}
		return rows;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: previewNotifyPrepare
	 * @Description: 预览通知单
	 * @param notifyId
	 * @param period
	 * @param serialNo
	 * @return 
	 * @throws IOException 
	 * @throws DocumentException 
	 * @see com.learnbind.ai.service.notify.WaterNotifyService#previewNotifyPrepare(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public int previewNotifyPrepare(String FILE_DIR,String TEMPLATE_PREFIX, String TEMPLATE_FILE_NAME,Long notifyId) throws Exception {
		//获取通知单信息
		Map<String, Object> customerMap = this.getWaterNotifyMap(notifyId, null , null);
		WaterNotify notify = this.selectByPrimaryKey(notifyId);
		String pdfFileName = generateNotify.generateNotifyPDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				customerMap, notifyId.toString(), notify.getPeriod());
		if(StringUtils.isNotBlank(pdfFileName)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * @Title: getWaterNotifyMap
	 * @Description: 获取通知单信息
	 * @param notifyId
	 * @return 
	 * @throws ParseException 
	 */
	public Map<String, Object> getWaterNotifyMap(Long notifyId ,String noticeDate, String chargePeople) throws ParseException{
		Map<String, Object> customerMap = new HashMap<>();
		//通知单时间
		customerMap.put("notifyId", notifyId);
		//获取通知单日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
		if(StringUtils.isNotBlank(noticeDate)) {
			Date time=stringToDate.parse(noticeDate);
			String noticeDateStr = sDateFormat.format(time);
			customerMap.put("noticeDateStr", noticeDateStr);
		} else {
			Date sysdate = new Date();
			customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
		}
		WaterNotify notify = this.selectByPrimaryKey(notifyId);
		// 获取开票信息
		Map<String, Object> billMap = this.getCustomerBillName(notify.getCustomerId());
		String customerName = billMap.get("customerName").toString();
		String billName = billMap.get("billName").toString();
		customerMap.put("customerName", customerName);
		customerMap.put("billName", billName);
		customerMap.put("billNo", billMap.get("billNo"));
		//List<NotifyMeterRecordBean> assistantBeanList = JSON.parseArray(notify.getMeterRecord(), NotifyMeterRecordBean.class);
		List<Long> meterRecordIdList = JSON.parseArray(notify.getMeterRecord(), Long.class);
		List<NotifyMeterRecordBean> assistantBeanList = this.getRecordBeanList(meterRecordIdList);
		
		//抄表记录
		customerMap.put("meterRecord", assistantBeanList);
		//获取通知单明细
		List<WaterNotifyDetail> detailList =  waterNotifyDetailService.getNotifyDetail(notify.getId());
		//获取实收水量、水价等信息
		Map<String, Object> detailMap = this.getNotifyDetailMap(detailList);
		//通知单明细
		customerMap.put("waterAmountStr", detailMap.get("waterAmountStr"));
		customerMap.put("basePriceStr", detailMap.get("basePriceStr"));
		customerMap.put("sumBasePriceFee", detailMap.get("sumBasePriceFee"));
		customerMap.put("treatmentStr", detailMap.get("treatmentStr"));
		customerMap.put("sumTreatmentFee", detailMap.get("sumTreatmentFee"));
		//往期欠费
		customerMap.put("pastBaseOweAmount", notify.getPastBaseOweAmount());
		customerMap.put("pastSewageOweAmount", notify.getPastSewageOweAmount());
		//预存
		customerMap.put("customerBalance", notify.getCustomerBalance());
		//欠费合计
		customerMap.put("totalBaseAmount", BigDecimalUtils.add(notify.getPastBaseOweAmount(),notify.getSumBaseAmount()));
		customerMap.put("totalSewageAmount", BigDecimalUtils.add(notify.getPastSewageOweAmount(), notify.getSumSewageAmount()));
		customerMap.put("totalOweAmount", notify.getTotalOweAmount());
		//收费人名称
		if(chargePeople == null) {
			chargePeople ="";
		}
		customerMap.put("chargePeople", chargePeople);
		//备注
		customerMap.put("remark", notify.getRemark());
		return customerMap;
	}
	
	/**
	 * @Title: getRecordBeanList
	 * @Description: 封装通知单的抄表记录
	 * @return 
	 */
	public List<NotifyMeterRecordBean> getRecordBeanList(List<Long> meterRecordIdList){
		List<NotifyMeterRecordBean> recordBeanList = new ArrayList<>();
		for(Long meterRecordId : meterRecordIdList) {
			NotifyMeterRecordBean bean = new NotifyMeterRecordBean();
			MeterRecord record = meterRecordService.selectByPrimaryKey(meterRecordId);
			Meters meter = metersService.selectByPrimaryKey(record.getMeterId());
			bean.setMeterId(record.getMeterId());
			bean.setPreRead(record.getPreRead());
			bean.setCurrRead(record.getCurrRead());
			bean.setCurrAmount(record.getCurrAmount());
			bean.setDescription(meter.getDescription());
			recordBeanList.add(bean);
		}
		return recordBeanList;
	}
	
	/**
	 * @Title: getNotifyDetailMap
	 * @Description: 获取通知单明细信息
	 * @param detailList
	 * @return 
	 */
	public Map<String, Object> getNotifyDetailMap(List<WaterNotifyDetail> detailList) {
		Map<String, Object> detailMap = new HashMap<>();
		String waterAmountStr = "";//实收水量
		String basePriceStr = "";//基础水费单价
		String treatmentStr = "";//污水处理费单价
		String sumBasePriceFee  = "";//基础水费
		String sumTreatmentFee  = "";//污水处理费
		for(WaterNotifyDetail detail : detailList) {
			BigDecimal basePriceFee = BigDecimalUtils.multiply(detail.getRealWaterAmount(), detail.getBasePrice());
			BigDecimal treatment = BigDecimalUtils.multiply(detail.getRealWaterAmount(), detail.getSewagePrice());
			sumBasePriceFee = basePriceFee + "/" + sumBasePriceFee;
			sumTreatmentFee = treatment + "/" + sumTreatmentFee;
			basePriceStr = detail.getBasePrice() + "/" + basePriceStr;
			treatmentStr = detail.getSewagePrice() + "/" + treatmentStr;
			waterAmountStr = detail.getRealWaterAmount() + "/" + waterAmountStr;
		}
		waterAmountStr = this.getStr("/", waterAmountStr);
		basePriceStr = this.getStr("/", basePriceStr);
		treatmentStr = this.getStr("/", treatmentStr);
		sumBasePriceFee = this.getStr("/", sumBasePriceFee);
		sumTreatmentFee = this.getStr("/", sumTreatmentFee);
		detailMap.put("waterAmountStr", waterAmountStr);// 水量
		
		detailMap.put("basePriceStr", basePriceStr);// 基础水费价格字符串
		detailMap.put("sumBasePriceFee", sumBasePriceFee);// 基础水费字符串
		detailMap.put("treatmentStr", treatmentStr);// 污水处理费价格字符串
		detailMap.put("sumTreatmentFee", sumTreatmentFee);// 污水处理费字符串
		
		return detailMap;
		
	}
	
	/**
	 * @Title: getStr
	 * @Description: 去除字符串最后一位符号
	 * @param separtor
	 * @param str
	 * @return
	 */
	public String getStr(String separtor, String str) {
		if (str.endsWith(separtor)) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	/**
	 * @Title: getCustomerBillName
	 * @Description: 获取客户的开票信息
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> getCustomerBillName(Long customerId) {
		Map<String, Object> billMap = new HashMap<>();
		Customers customer = customersService.selectByPrimaryKey(customerId);
		String billName = "";
		String billNo = "";
		Integer billType = 0;
		Example example = new Example(CustomerBillInfo.class);
		example.createCriteria().andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue()).andEqualTo("customerId",
				customerId);
		List<CustomerBillInfo> billList = billService.selectByExample(example);
		if(billList.size() >= 1){
			billName = billList.get(0).getCustomerName();
			billNo = billList.get(0).getBillNo();
			billType = billList.get(0).getBillType();
			String billTypeStr = EnumInvoiceType.getName(billType);
			billTypeStr = billTypeStr.substring(0, 1);
			billNo = billNo+"("+billTypeStr+")";
		}
		billMap.put("billName", billName);// 开票名称
		billMap.put("customerName", customer.getCustomerName());// 客户名称
		billMap.put("billNo", billNo);// 客户的开票编号
		billMap.put("billType", billType);// 客户的开票类型
		return billMap;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: printNotifySingle
	 * @Description: 单个打印通知单
	 * @param FILE_DIR
	 * @param TEMPLATE_PREFIX
	 * @param notifyId
	 * @param templateFileName
	 * @param printerId
	 * @param noticeDate
	 * @param chargePeople
	 * @return 
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws PrinterException 
	 * @throws ParseException 
	 * @see com.learnbind.ai.service.notify.WaterNotifyService#printNotifySingle(java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public int printNotifySingle(String FILE_DIR, String TEMPLATE_PREFIX, Long notifyId, String templateFileName,
			Long printerId, String noticeDate, String chargePeople) throws DocumentException, IOException, PrinterException, ParseException {
		List<String> pdfFileList = new ArrayList<>();
		WaterNotify notify = this.selectByPrimaryKey(notifyId);
		//获取通知单信息
		Map<String, Object> customerMap = this.getWaterNotifyMap(notifyId, noticeDate, chargePeople);
		//获取通知单的总欠费金额
//		BigDecimal totalOweAmount = new BigDecimal(customerMap.get("customerMap").toString());
//		BigDecimal zero = new BigDecimal("0");
//		if(BigDecimalUtils.equals(totalOweAmount, zero)) {//如果通知单欠费金额为0，则不打印
//			return 0;
//		}
//		
		String pdfFileName = generateNotify.generateNotifyPDF(FILE_DIR, TEMPLATE_PREFIX, templateFileName,
				customerMap, notify.getId().toString(), notify.getPeriod());
		pdfFileList.add(pdfFileName);
		// 查询打印小区编码
		sendPDFToPrinter(pdfFileList, printerId);
		if(pdfFileList.size() > 0) {
			return 1;
		}
		return 0;
	}


	/**
	 * @Title: sendPDFToPrinter
	 * @Description: 将文件发送给打印机
	 * @param pdfFileList
	 * @param printerId
	 * @throws IOException
	 * @throws PrinterException 
	 */
	private void sendPDFToPrinter(List<String> pdfFileList, Long printerId)
			throws IOException, PrinterException {
		PrinterConfig printerConfig = printerService.selectByPrimaryKey(printerId);
		// 打印PDF文件
		for (int i = 0; i < pdfFileList.size(); i++) {
			System.out.println(pdfFileList.get(i));
			printFile.printCustomerPDF(pdfFileList.get(i), printerConfig.getPrinterName());
		}
	}


	@Override
	public List<WaterNotify> getList(Long customerId, String period, String searchCond) {
		return waterNotifyMapper.getList(customerId, period, searchCond);
	}




	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: generateNotifyBatch
	 * @Description: 批量打印通知单
	 * @param FILE_DIR
	 * @param TEMPLATE_PREFIX
	 * @param templateFileName
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param customerIdStr
	 * @param printerId
	 * @param noticeDate
	 * @param chargePeople
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException 
	 * @throws ParseException 
	 * @see com.learnbind.ai.service.notify.WaterNotifyService#generateNotifyBatch(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public int generateNotifyBatch(String FILE_DIR, String TEMPLATE_PREFIX, String templateFileName, String searchCond,
			String traceIds, String period, String notifyIdArr, Long printerId, String noticeDate,
			String chargePeople, Integer sortMethod, Integer arrearsAmount) throws DocumentException, IOException, PrinterException, ParseException {
		int rows = 0;
		List<WaterNotify> notifyList = new ArrayList<>();
		BigDecimal zero = new BigDecimal("0");
		if (StringUtils.isBlank(notifyIdArr)) {// 批量打印时根据查询条件打印
			if(StringUtils.isBlank(traceIds)) {//如果没有选择地理位置查询
				notifyList = this.searchCond(searchCond, period,sortMethod, arrearsAmount);
			} else {
				notifyList = this.getNotifyMeterIdList(period ,searchCond,traceIds, sortMethod, arrearsAmount);
				
			}
		} else {// 批量打印时勾选进行打印
			String[] s1 = notifyIdArr.split(",");
			for (String notifyId : s1) {
				WaterNotify notify = this.selectByPrimaryKey(Long.valueOf(notifyId));
				notifyList.add(notify);
			}
		}
		List<String> pdfFileList = new ArrayList<>();
		for (WaterNotify notify : notifyList) {
			rows = this.printNotifySingle(FILE_DIR, TEMPLATE_PREFIX, notify.getId(), templateFileName, printerId, noticeDate, chargePeople);
		}
		return rows;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: destoryWaterNotify
	 * @Description: 报废通知单
	 * @param notifyIds
	 * @return 
	 * @see com.learnbind.ai.service.notify.WaterNotifyService#destoryWaterNotify(java.util.List)
	 */
	@Override
	public int destoryWaterNotify(List<Long> notifyIds) {
		int rows = 0;
		for(Long notifyId : notifyIds) {
			WaterNotify notify = this.selectByPrimaryKey(notifyId);
			notify.setStatus(EnumWaterNotifyStatus.SCRAP.getValue());
			rows = this.updateByPrimaryKeySelective(notify);
//			if(rows>0) {
//				//获取通知单明细
//				List<WaterNotifyDetail> detailList =  waterNotifyDetailService.getNotifyDetail(notify.getId());
//				for(WaterNotifyDetail detail : detailList) {
//					waterNotifyDetailService.delete(detail);
//				}
//			}
		}
		return rows;
				
	}


	@Override
	public List<WaterNotify> getWaterNotifyList(String searchCond, String period, String traceIds) {
		return waterNotifyMapper.getWaterNotifyList(searchCond, period, traceIds);
	}


	@Override
	public List<WaterNotify> getNotifyMeterIdList(String period, String searchCond, String traceIds, Integer sortMethod, Integer arrearsAmount) {
		return waterNotifyMapper.getNotifyMeterIdList(period, searchCond, traceIds, sortMethod, arrearsAmount);
		
	}
	
	public String getMeterId(String meterIdStr) {
		String str = "";
		//判断字符串是否包含逗号
		if(meterIdStr.contains(",")) {
			str=meterIdStr.substring(1, meterIdStr.indexOf(","));
		} else {
			str=meterIdStr.substring(1, meterIdStr.indexOf("]"));
		}
		return str;
		
	}
	


	@Override
	public int isExistsLocation(String meterIdStr, String traceIds) {
		meterIdStr = this.getMeterId(meterIdStr);
		return waterNotifyMapper.isExistsLocation(meterIdStr, traceIds);
	}
	
}
