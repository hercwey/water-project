package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CmbcBatchWithholdRecord;
import tk.mybatis.mapper.common.Mapper;

public interface CmbcBatchWithholdRecordMapper extends Mapper<CmbcBatchWithholdRecord> {
	
	/**
	 * @Title: searchCcbHoldFileItem
	 * @Description:条件查找批量代扣文件
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<Map<String, Object>> searchCmbcHoldFileItem(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("holdStatus") Integer holdStatus);
	
	
	/**
	 * @Title: searchCcbHoldFileList
	 * @Description: 查找代扣文件List
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<CmbcBatchWithholdRecord> searchCmbcHoldFileList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("holdStatus") Integer holdStatus);
}