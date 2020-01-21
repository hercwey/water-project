package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.Customers;

import tk.mybatis.mapper.common.Mapper;

public interface CustomersMapper extends Mapper<Customers> {
	
	/**
	 * 根据条件查询
	 * @param searchCustomers
	 * @param searchCond
	 * @param statusList
	 * @return 
	 */
	public List<Customers> searchCustomers(@Param("searchCond") String searchCond, @Param("statusList") List<Integer> statusList, @Param("customerType") Integer customerType);
	
	/**
     * @Title: getCustomerListByMeterId
     * @Description: 根据表计id查询客户集合（包含是否默认客户）
     * @param meterId
     * @param searchCond
     * @return 
     */
    public List<Map<String, Object>> getCustomerListByMeterId(@Param("meterId") Long meterId, @Param("searchCond") String searchCond);
    
    /**
	 * @Title: getCustomerListByLocationId
	 * @Description: 根据地理位置查询客户信息
	 * @param locationId
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerListByLocationId(@Param("locationId") Long locationId, @Param("searchCond") String searchCond);
	
	/**
	 * 根据条件查询
	 * @param 查询带有协议的信息
	 * @return
	 */
	public List<Customers> searchCustomerAgreement(@Param("searchCond") String searchCond, @Param("agreementType") Integer agreementType);
	
	/**
	 * 根据条件查询
	 * @param 查询带有协议的信息
	 * @return
	 */
	public List<Customers> searchTripartAgreement(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getCustomerNameById
	 * @Description: 根据id获取客户姓名
	 * @param id
	 * @return 
	 */
	public String getCustomerNameById(@Param("id") Long id);
	
	/**
	 * @Title: getCustomersList
	 * @Description: 根据条件和地理位置痕迹ID（ID-ID-ID-ID）查询客户
	 * @param searchCond
	 * @param traceIds
	 * @param statusList
	 * @return 
	 */
	public List<Customers> getCustomersList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("statusList") List<Integer> statusList, @Param("customerType") Integer customerType);
	
	/**
	 * 	查询用户编号(微信端查询用户编号时使用)
	 *  (业主电话 AND 姓名)      or    (客户电话 AND 姓名)   
	 * @param telNo  电话号码
	 * @param customerName 姓名
	 * @return
	 */
	public List<Customers> searchCustomerNo(@Param("telNo") String telNo, @Param("customerName") String customerName);
	
	/**
	 * @Title: selectCustomerBidMeterInfo
	 * @Description: 根据表计id查询绑定的默认客户信息
	 * @param meterId
	 * @return 
	 */
	public Customers selectCustomerBidMeterInfo(@Param("meterId") Long meterId);
	
	/**
	 * @Title: getInsertAccountCustomerList
	 * @Description:  获取增加账单时客户列表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getInsertAccountCustomerList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds);
    
	/**
	 * @Title: getCustomerList
	 * @Description: 获取客户列表
	 * @param traceIds
	 * @param room
	 * @return 
	 */
	public List<Customers> getCustomerListByRoom(@Param("traceIds") String traceIds, @Param("room") String room);
	
	
	public List<Map<String, Object>> getCustomerListByTraceIds(@Param("traceIds") String traceIds, @Param("searchCond") String searchCond);
	
	public List<Customers> getCustomerData(@Param("traceIds") String traceIds);
	
	
	/**
	 * @Title: getDefaultCustomerList
	 * @Description: 打印通知单是获取默认客户
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<Customers> getDefaultCustomerList(@Param("traceIds") String traceIds, @Param("searchCond") String searchCond, @Param("customerType") Integer customerType, @Param("oweMonth") Integer oweMonth);
	
	/**
	 * @Title: getDefaultCustomerList
	 * @Description: 打印通知单是获取默认客户
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<Customers> getDefaultUnitCustomerList(@Param("traceIds") String traceIds, @Param("searchCond") String searchCond, @Param("customerType") Integer customerType); 
	
	/**
	 * @Title: getList
	 * @Description: 根据客户名称/开票名称查询
	 * @param customerName
	 * @return 
	 */
	public List<Customers> getList(@Param("customerName") String customerName);
	
	
	/**
	 * @Title: getNotifyCustomerList
	 * @Description: 打印通知单查询客户
	 * @param traceIds
	 * @param searchCond
	 * @param customerType
	 * @return 
	 */
	public List<Customers> getNotifyCustomerList(@Param("traceIds") String traceIds, @Param("searchCond") String searchCond, @Param("customerType") Integer customerType, @Param("period") String period);
	
	/**
	 * @Title: getPrepaymentCustomerList
	 * @Description: 查询预存客户
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Customers> getPrepaymentCustomerList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds);
	
}