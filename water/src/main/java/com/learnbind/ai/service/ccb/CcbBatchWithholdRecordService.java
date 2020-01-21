package com.learnbind.ai.service.ccb;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbBatchWithholdRecordService.java
 * @Description: 中国建设银行批量代扣记录接口类
 *
 * @author Administrator
 * @date 2019年7月14日 下午3:32:09
 * @version V1.0 
 *
 */
public interface CcbBatchWithholdRecordService extends IBaseService<CcbBatchWithholdRecord, Long> {
	
	/**
	 * @Title: saveList
	 * @Description: 批量保存
	 * @param recordList
	 * @return 
	 */
	public int saveList(List<CcbBatchWithholdRecord> recordList);
	
	
	/**
	 * @Title: searchCcbHoldFileItem
	 * @Description:条件查找批量代扣文件
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<Map<String, Object>> searchCcbHoldFileItem(String searchCond, String traceIds, String period, Integer holdStatus);
	
	
	/**
	 * @Title: searchCcbHoldFileList
	 * @Description: 查找批量代扣文件List
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<CcbBatchWithholdRecord> searchCcbHoldFileList(String searchCond, String traceIds, String period, Integer holdStatus);
	
	
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
	
	/**
	 * @Title: uploadWithholdFileToCcb
	 * @Description: 单个上传批量代扣文件到CCB
	 * @param record
	 * @return 
	 */
	public Map<String,Object> uploadWithholdFileToCcb(CcbBatchWithholdRecord recordList);
	
	/**
	 * @Title: uploadWithholdFileToCcb
	 * @Description: 批量上传批量代扣文件到CCB
	 * @param recordList
	 * @return 
	 */
	public Map<String,Object> uploadWithholdFileToCcb(List<CcbBatchWithholdRecord> recordList);
	
	/**
	 * @Title: applyProcessWithholdFile
	 * @Description: 申请处理批量代扣文件
	 * @param recordList
	 * @return 
	 */
	public Map<String,Object> applyProcessWithholdFile(List<CcbBatchWithholdRecord> recordList);
	
	public int deleteHoldFile(Long itemId);
	
}
