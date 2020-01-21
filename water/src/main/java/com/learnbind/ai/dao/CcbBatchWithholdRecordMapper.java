package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CcbBatchWithholdRecord;
import tk.mybatis.mapper.common.Mapper;

public interface CcbBatchWithholdRecordMapper extends Mapper<CcbBatchWithholdRecord> {
	
	/**
	 * @Title: searchCcbHoldFileItem
	 * @Description:条件查找批量代扣文件
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<Map<String, Object>> searchCcbHoldFileItem(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("holdStatus") Integer holdStatus);
	
	
	/**
	 * @Title: searchCcbHoldFileList
	 * @Description: 查找代扣文件List
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param holdStatus
	 * @return 
	 */
	public List<CcbBatchWithholdRecord> searchCcbHoldFileList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("period") String period, @Param("holdStatus") Integer holdStatus);
}