package com.learnbind.ai.service.notify;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.NotifyGroupBean;
import com.learnbind.ai.common.enumclass.EnumGroupIncludeFlag;
import com.learnbind.ai.model.NotifyGroupMeter;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.notify
 *
 * @Title: NotifyGroupRuleParserService.java
 * @Description: 生成通知单规则解析器服务
 *
 * @author Administrator
 * @date 2019年12月8日 上午10:23:28
 * @version V1.0 
 *
 */
@Service
public class NotifyGroupRuleParserService {

	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(NotifyGroupRuleParserService.class);
	
	/**
	 * @Title: parser
	 * @Description: 生成通知单时解析规则
	 * @param meterIdList
	 * @param groupList
	 * @return 
	 */
	public static List<List<Long>> parser(List<Long> meterIdList, List<NotifyGroupBean> groupList) {
		
		if(groupList==null || groupList.size()<=0) {//未配置规则
			log.debug("未配置规则，返回所有表计ID集合。");
			List<List<Long>> resultList = new ArrayList<>();
			resultList.add(meterIdList);
			return resultList;
		}else if(NotifyGroupRuleParserService.getHaveIncludeRule(groupList) && NotifyGroupRuleParserService.getHaveExcludeRule(groupList)){//已配置规则，且有包含规则，且有排除规则
			log.debug("已配置规则，且有包含规则，且有排除规则，返回包含规则中剔除排除规则后的表计ID集合。");
			return NotifyGroupRuleParserService.getIncludeMeterIdList(groupList);
		}else if(NotifyGroupRuleParserService.getHaveIncludeRule(groupList) && !NotifyGroupRuleParserService.getHaveExcludeRule(groupList)){//已配置规则，且有包含规则，且没有排除规则
			log.debug("已配置规则，且有包含规则，且没有排除规则，返回包含规则中的表计ID集合。");
			return NotifyGroupRuleParserService.getIncludeRuleMeterIdList(groupList);
		}else if(!NotifyGroupRuleParserService.getHaveIncludeRule(groupList) && NotifyGroupRuleParserService.getHaveExcludeRule(groupList)){//已配置规则，且没有包含规则，且有排除规则
			log.debug("已配置规则，且没有包含规则，且有排除规则，返回所有表计ID列表中剔除排除规则中的表计ID集合。");
			return NotifyGroupRuleParserService.getMeterIdList(meterIdList, groupList);
		}else {
			return null;
		}
		
	}
	
	/**
	 * @Title: getIncludeMeterId
	 * @Description: 获取包含关系中的表计ID，把排除规则中表计删除后的表计ID集合
	 * @param groupList
	 * @return 
	 */
	private static List<List<Long>> getIncludeMeterIdList(List<NotifyGroupBean> groupList){
		
		List<Long> exMeterIdList = NotifyGroupRuleParserService.getExcludeRuleMeterIdList(groupList);
		
		List<List<Long>> resultList = new ArrayList<>();
		for(NotifyGroupBean group : groupList) {
			List<NotifyGroupMeter> groupMeterList = group.getGroupMeterList();//获取分组规则中的表计
			
			List<Long> resultMeterIdList = new ArrayList<>();
			for(NotifyGroupMeter groupMeter : groupMeterList) {
				Long meterId = groupMeter.getMeterId();
				boolean isInclude = true;
				for(Long exMeterId : exMeterIdList) {
					if(meterId.equals(exMeterId)) {
						isInclude = false;
						break;
					}
				}
				if(isInclude) {
					resultMeterIdList.add(meterId);
				}
			}
			if(resultMeterIdList!=null && resultMeterIdList.size()>0) {
				resultList.add(resultMeterIdList);
			}
			
		}
		log.debug("规则执行完后的表计ID集合："+resultList);
		return resultList;
	}
	
	/**
	 * @Title: getMeterIdList
	 * @Description: 返回表计ID集合，把排除规则中表计删除后的表计ID集合
	 * @param meterIdList
	 * @param groupList
	 * @return 
	 */
	private static List<List<Long>> getMeterIdList(List<Long> meterIdList, List<NotifyGroupBean> groupList){
		List<Long> exMeterIdList = NotifyGroupRuleParserService.getExcludeRuleMeterIdList(groupList);
		List<Long> resultMeterIdList = new ArrayList<>();
		for(Long meterId : meterIdList) {
			boolean isInclude = true;
			for(Long exMeterId : exMeterIdList) {
				if(meterId.equals(exMeterId)) {
					isInclude = false;
					break;
				}
			}
			if(isInclude) {
				resultMeterIdList.add(meterId);
			}
		}
		
		List<List<Long>> resultList = new ArrayList<>();
		if(resultMeterIdList!=null && resultMeterIdList.size()>0) {
			resultList.add(resultMeterIdList);
		}
		
		log.debug("规则执行完后的表计ID集合："+resultList);
		return resultList;
	}
	
	/**
	 * @Title: getHaveIncludeRule
	 * @Description: 判断是否有包含关系的规则
	 * @param groupList
	 * @return 
	 */
	private static boolean getHaveIncludeRule(List<NotifyGroupBean> groupList) {
		boolean flag = false;
		for(NotifyGroupBean group : groupList) {
			Integer includeFlag = group.getIncludeFlag();//包含关系
			if(includeFlag==EnumGroupIncludeFlag.INCLUDE.getValue()) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * @Title: getHaveExcludeRule
	 * @Description: 判断是否有排除关系的规则
	 * @param groupList
	 * @return 
	 */
	private static boolean getHaveExcludeRule(List<NotifyGroupBean> groupList) {
		boolean flag = false;
		for(NotifyGroupBean group : groupList) {
			Integer includeFlag = group.getIncludeFlag();//包含关系
			if(includeFlag==EnumGroupIncludeFlag.EXCLUDE.getValue()) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * @Title: getIncludeRuleMeterIdList
	 * @Description: 获取包含规则所有表计ID集合
	 * @param groupList
	 * @return 
	 */
	private static List<List<Long>> getIncludeRuleMeterIdList(List<NotifyGroupBean> groupList){
		
		List<List<Long>> resultList = new ArrayList<>();
		for(NotifyGroupBean group : groupList) {
			Integer includeFlag = group.getIncludeFlag();//包含关系
			if(includeFlag==EnumGroupIncludeFlag.INCLUDE.getValue()) {
				List<NotifyGroupMeter> groupMeterList = group.getGroupMeterList();
				List<Long> meterIdList = new ArrayList<>();
				for(NotifyGroupMeter groupMeter : groupMeterList) {
					meterIdList.add(groupMeter.getMeterId());
				}
				if(meterIdList!=null && meterIdList.size()>0) {
					resultList.add(meterIdList);
				}
				
			}
		}
		log.debug("包含规则中的表计ID集合："+resultList);
		return resultList;
	}
	
	/**
	 * @Title: getExcludeRuleMeterIdList
	 * @Description: 获取排除规则所有表计ID集合
	 * @param groupList
	 * @return 
	 */
	private static List<Long> getExcludeRuleMeterIdList(List<NotifyGroupBean> groupList){
		
		List<Long> meterIdList = new ArrayList<>();
		for(NotifyGroupBean group : groupList) {
			Integer includeFlag = group.getIncludeFlag();//包含关系
			if(includeFlag==EnumGroupIncludeFlag.EXCLUDE.getValue()) {
				List<NotifyGroupMeter> groupMeterList = group.getGroupMeterList();
				for(NotifyGroupMeter groupMeter : groupMeterList) {
					meterIdList.add(groupMeter.getMeterId());
				}
			}
		}
		log.debug("排除规则中的表计ID集合："+meterIdList);
		return meterIdList;
	}
	
	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args) {
		
		//默认表计ID集合
		List<Long> meterIdList = new ArrayList<>();
		for(int i=20; i<50; i++) {
			meterIdList.add(Long.valueOf(i));
		}
		
		//分组规则
		List<NotifyGroupBean> groupList = new ArrayList<>();
		NotifyGroupBean bean = new NotifyGroupBean();
		bean.setIncludeFlag(EnumGroupIncludeFlag.INCLUDE.getValue());
		//分组-表计
		List<NotifyGroupMeter> meterList = new ArrayList<>();
		for(int i=35; i<45; i++) {
			NotifyGroupMeter meter = new NotifyGroupMeter();
			meter.setMeterId(Long.valueOf(i));
			meterList.add(meter);
		}
		bean.setGroupMeterList(meterList);
		groupList.add(bean);
		
		//分组规则
		NotifyGroupBean bean2 = new NotifyGroupBean();
		bean2.setIncludeFlag(EnumGroupIncludeFlag.EXCLUDE.getValue());
		//分组-表计规则
		List<NotifyGroupMeter> meterList2 = new ArrayList<>();
		for(int i=30; i<40; i++) {
			NotifyGroupMeter meter = new NotifyGroupMeter();
			meter.setMeterId(Long.valueOf(i));
			meterList2.add(meter);
		}
		bean2.setGroupMeterList(meterList2);
		groupList.add(bean2);
		
		NotifyGroupBean bean3 = new NotifyGroupBean();
		bean3.setIncludeFlag(EnumGroupIncludeFlag.INCLUDE.getValue());
		//分组-表计
		List<NotifyGroupMeter> meterList3 = new ArrayList<>();
		for(int i=50; i<60; i++) {
			NotifyGroupMeter meter = new NotifyGroupMeter();
			meter.setMeterId(Long.valueOf(i));
			meterList3.add(meter);
		}
		bean3.setGroupMeterList(meterList3);
		groupList.add(bean3);
		log.debug("分组规则："+groupList);
		
		System.out.println(NotifyGroupRuleParserService.parser(meterIdList, groupList));
	}
	
}
