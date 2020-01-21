package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterInstallRecepit;
import tk.mybatis.mapper.common.Mapper;

public interface MeterInstallRecepitMapper extends Mapper<MeterInstallRecepit> {
	
	 public List<MeterInstallRecepit> searchCond(@Param("searchCond") String searchCond, @Param("meterId") Long meterId);
}