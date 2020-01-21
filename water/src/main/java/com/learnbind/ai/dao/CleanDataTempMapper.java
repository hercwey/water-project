package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CleanDataTempTable;
import com.learnbind.ai.model.StatisticTempTable;

import tk.mybatis.mapper.common.Mapper;

public interface CleanDataTempMapper extends Mapper<CleanDataTempTable> {
	//删除客户相关数据
	public int deleteBank(@Param("customerId")Long customerId);
	public int deleteBillInfo(@Param("customerId")Long customerId);
	public int deletePledge(@Param("customerId")Long customerId);
	public int deletePeople(@Param("customerId")Long customerId);
	public int deletdAgreeMent(@Param("customerId")Long customerId);
	public int deletdOverdueFine(@Param("customerId")Long customerId);
	public int deleteAccount(@Param("customerId")Long customerId);
	public int deleteAccountItem(@Param("customerId")Long customerId);
	public int deleteTrace(@Param("customerId")Long customerId);
	public int deleteMeter(@Param("customerId")Long customerId);
	public int deleteLocation(@Param("customerId")Long customerId);
	public int deletedPartWater(@Param("customerId")Long customerId);
	
	//删除表计相关数据
	public int deleteMeterBookMeter(@Param("meterId")Long meterId);
	public int deletedMeterRecordTemp(@Param("meterId")Long meterId);
	public int deletedMeterRecordTempPhoto(@Param("meterId")Long meterId);
	public int deletedMeterRecordPhoto(@Param("meterId")Long meterId);
	public int deletedMeterRecord(@Param("meterId")Long meterId);
	public int deletedPartWaterRule(@Param("meterId")Long meterId);
	public int deletedPartWaterRuleTrace(@Param("meterId")Long meterId);
	public int deletedMeterTree(@Param("meterId")Long meterId);
	public int deletedMeterLocation(@Param("meterId")Long meterId);
	
}