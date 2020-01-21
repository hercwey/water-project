package com.learnbind.ai.service.customers;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface CustomersService extends IBaseService<Customers, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<Customers> searchCustomers(String searchCond, List<Integer> statusList, Integer customerType);
	
    /**
     * @Title: insertCustomers
     * @Description: 增加客户信息，并绑定客户与地理位置的关系
     * @param customers
     * @param locationId
     * @return 
     */
    public int insertCustomers(Customers customers, Long locationId);
    
    /**
     * 修改
     * @param customers
     * @return
     */
    public int updateCustomers(Customers customer, Long prevLocationId, Long locationId);
    
    /**
     * 删除
     * @param customersId
     * @return
     */
    public int deleteCustomers(long customersId);

    /** 
    	* @Title: findAllCustomers
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<Customers> findAllCustomers(int pageNum, int pageSize);
    
    /**
     * @Title: updateWaterStatus
     * @Description: 修改客户的用水状态
     * @param customerId
     * @param waterStatus
     * @return 
     */
    public int updateWaterStatus(Long customerId, Integer waterStatus);
    
    /**
     * @Title: getCustomerListByMeterId
     * @Description: 根据表计id查询客户集合（包含是否默认客户）
     * @param meterId
     * @param searchCond
     * @return 
     */
    public List<Map<String, Object>> getCustomerListByMeterId(Long meterId, String searchCond);
    
    /**
	 * @Title: getCustomerListByLocationId
	 * @Description: 根据地理位置id查询客户信息
	 * @param locationId
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerListByLocationId(Long locationId, String searchCond);
	
	/**
	 * @Title: transferAccount
	 * @Description: 过户
	 * @param oldCustomerId
	 * @param newCustomerId
	 * @return 
	 */
	public int transferAccount(Long oldCustomerId, Long newCustomerId);
	
	
	/**
	 * 根据条件查询
	 * @param 查询带有协议的信息
	 * @return
	 */
	public List<Customers> searchCustomerAgreement(String searchCond,  Integer agreementType);
	
	/**
	 * 根据条件查询
	 * @param 查询带有协议的信息
	 * @return
	 */
	public List<Customers> searchTripartAgreement(String searchCond);
	
	/**
	 * @Title: getCustomerNameById
	 * @Description: 根据id获取客户姓名
	 * @param id
	 * @return 
	 */
	public String getCustomerNameById(Long id) ;
	
	/**
	 * @Title: getCustomersList
	 * @Description: 根据条件和地理位置痕迹ID（ID-ID-ID-ID）查询客户
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Customers> getCustomersList(String searchCond, String traceIds, List<Integer> statusList, Integer customerType);
	
	/**
	 * 查询客户编号
	 * @param telNo  电话号码
	 * @param customerName 客户姓名
	 * @return  返回客户对象列表.
	 */
	public List<Customers> searchCustomerNo(String telNo,String customerName);
	
	/**
	 * @Title: bindBigMeter
	 * @Description: 绑定
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public int bindBigMeter(List<Long> meterIdList, Long customerId);
	
	/**
	 * @Title: unbindBigMeter
	 * @Description: 解绑
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public int unbindBigMeter(List<Long> meterIdList, Long customerId);
	
	/**
	 * @Title: bindMeter
	 * @Description: 建立客户-表计绑定关系
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public int bindMeter(List<Long> meterIdList, Long customerId);
	/**
	 * @Title: unbindMeter
	 * @Description: 解除客户-表计的绑定关系
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public int unbindMeter(List<Long> meterIdList, Long customerId);
	
	/**
	 * @Title: selectCustomerBidMeterInfo
	 * @Description: 根据表计id查询绑定的默认客户信息
	 * @param meterId
	 * @return 
	 */
	public Customers selectCustomerBidMeterInfo(Long meterId);
	
	/**
	 * @Title: getCustomerMessage
	 * @Description: 根据地理位置获取客户信息
	 * @param traceIds
	 * @return 
	 */
	public LocationCustomer getCustomerMessage(String traceIds);
	
	
	/**
	 * @Title: getInsertAccountCustomerList
	 * @Description: 获取增加账单时客户列表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getInsertAccountCustomerList(String searchCond, String traceIds);
    
	/**
	 * @Title: getCustomerListByRoom
	 * @Description: 获取客户列表
	 * @param traceIds
	 * @param room
	 * @return 
	 */
	public List<Customers> getCustomerListByRoom(String traceIds, String room); 
	
	 /**
	 * @Title: getCustomerListByLocationId
	 * @Description: 根据traceIds查询客户信息
	 * @param locationId
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerListByTraceIds(String traceIds, String searchCond);
	
	
	public List<Customers> getCustomerData(String traceIds);
	
	/**
	 * @Title: getDefaultCustomerList
	 * @Description: 打印通知单是获取默认客户
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<Customers> getDefaultCustomerList(String traceIds, String searchCond, Integer customerType, Integer oweMonth); 
	
	/**
	 * @Title: getDefaultCustomerList
	 * @Description: 打印通知单是获取默认客户
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<Customers> getDefaultUnitCustomerList(String traceIds, String searchCond, Integer customerType); 
	
	/**
	 * @Title: getCustomerList
	 * @Description: 根据客户名称/开票名称查询
	 * @param customerName
	 * @return 
	 */
	public List<Customers> getCustomerList(String customerName);
	
	/**
	 * @Title: getNotifyCustomerList
	 * @Description: 生成水费通知单是查询客户
	 * @param traceIds
	 * @param searchCond
	 * @param customerType
	 * @return 
	 */
	public List<Customers> getNotifyCustomerList(String traceIds, String searchCond, Integer customerType, String period); 
	
	/**
	 * @Title: getPrepaymentCustomerList
	 * @Description: 查询预存客户
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Customers> getPrepaymentCustomerList(String searchCond, String traceIds);
	
}
