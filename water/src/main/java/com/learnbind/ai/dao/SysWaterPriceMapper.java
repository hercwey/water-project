package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysWaterPrice;

import tk.mybatis.mapper.common.Mapper;

public interface SysWaterPriceMapper extends Mapper<SysWaterPrice> {
	
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysWaterPrice> searchWaterPrice(@Param("searchCond") String searchCond);
	
	 /**
	 * @Title: getPriceTypeList
	 * @Description: 查询价格类型编码和名称
	 * @return 
	 * 		价格类型编码和名称集合
	 */
	public List<Map<String, Object>> getPriceTypeList();
	/**
	 * @Title: getPriceTypeName
	 * @Description: 根据价格类型编码获取价格类型名称
	 * @param priceTypeCode
	 * @return 
	 */
	public String getPriceTypeName(@Param("priceTypeCode") String priceTypeCode);
	
	/**
	 * @Title: getWaterPriceByPriceCode
	 * @Description: 根据priceCode获取水价
	 * @param priceCode
	 * @return 
	 */
	public SysWaterPrice getWaterPriceByPriceCode(@Param("priceCode") String priceCode);
}