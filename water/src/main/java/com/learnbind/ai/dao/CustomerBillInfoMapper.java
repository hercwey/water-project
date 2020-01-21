package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerBillInfo;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerBillInfoMapper extends Mapper<CustomerBillInfo> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */							
	public List<CustomerBillInfo> searchCustomerBillInfo(@Param("customerId") Long customerId,@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getCustomerInvoiceList
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @return 
	 * 		返回有效状态是有效的开票信息
	 */
	public List<Map<String, Object>> getCustomerInvoiceList(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: searchCustomerInvoiceList
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @param billType
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerInvoiceList(@Param("searchCond") String searchCond, @Param("billType") Integer billType);
	
}