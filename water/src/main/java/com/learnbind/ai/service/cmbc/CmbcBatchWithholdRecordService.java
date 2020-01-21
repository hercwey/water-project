package com.learnbind.ai.service.cmbc;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.cmbc
 *
 * @Title: CmbcBatchWithholdRecordService.java
 * @Description:中国民生银行批量代扣记录接口类
 *
 * @author Thinkpad
 * @date 2019年8月24日 下午4:39:02
 * @version V1.0 
 *
 */
public interface CmbcBatchWithholdRecordService extends IBaseService<CmbcBatchWithholdRecord, Long> {
	
	/**
	 * @Title: saveList
	 * @Description: 批量保存
	 * @param recordList
	 * @return 
	 */
	public int saveList(List<CmbcBatchWithholdRecord> recordList);
	
	
	/**
	 * @Title: searchCcbHoldFileItem
	 * @Description:条件查找批量代扣文件
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<Map<String, Object>> searchCmbcHoldFileItem(String searchCond, String traceIds, String period, Integer holdStatus);
	
	
	/**
	 * @Title: searchCcbHoldFileList
	 * @Description: 查找批量代扣文件List
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<CmbcBatchWithholdRecord> searchCmbcHoldFileList(String searchCond, String traceIds, String period, Integer holdStatus);
	
	
	/**
	 * @Title: generateBatchHoldFile
	 * @Description: 生成批量代扣文件
	 * @param accountItemMapList
	 * @param period
	 * @param comment
	 * @param pinYinCode
	 * @return 
	 */
	public int generateBatchHoldFile(List<Map<String, Object>> accountItemMapList, String period, String comment, String pinYinCode, String traceIds);
	
	/**
	 * @Title: singleGenerateBatchHoldFile
	 * @Description: 勾选生成批量代扣文件
	 * @param customerAccountItemId
	 * @param period
	 * @param comment
	 * @param pinYinCode
	 * @param traceIds
	 * @return 
	 */
	public int singleGenerateBatchHoldFile(Long customerAccountItemId, String period, String comment, String pinYinCode, String traceIds, Integer withHoldNo);
	
}
