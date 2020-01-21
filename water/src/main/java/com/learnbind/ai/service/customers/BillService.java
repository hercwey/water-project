package com.learnbind.ai.service.customers;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.CustomerBillInfo;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface BillService extends IBaseService<CustomerBillInfo, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerBillInfo> searchCustomerBillInfo(Long customerId, String searchCond);
	
	/**
	 * @Title: getCustomerInvoiceList
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> getCustomerInvoiceList(String searchCond);
	
	/**
	 * @Title: searchCustomerInvoiceList
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @param billType
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerInvoiceList(String searchCond, Integer billType);
	
	/**
	 * @Title: getCustomerInvoiceCount
	 * @Description: 获取客户开票信息个数
	 * @param customerId
	 * @return 
	 */
	public int getCustomerInvoiceCount(Long customerId);
	
    /** 
    	* @Title: insertCustomerBillInfo
    	* @Description: 增加 
    	* @param @param banks
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertCustomerBillInfo(CustomerBillInfo bill);
    
    /**
     * 修改
     * @param banks
     * @return
     */
    public int updateCustomerBillInfo(CustomerBillInfo bill);
    
    /**
     * 删除
     * @param banksId
     * @return
     */
    public int deleteCustomerBillInfo(long billId);

    /** 
    	* @Title: findAllCustomerBillInfo
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<CustomerBillInfo> findAllCustomerBillInfo(int pageNum, int pageSize);
    
    
}
