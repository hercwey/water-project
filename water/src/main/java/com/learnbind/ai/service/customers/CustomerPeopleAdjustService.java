package com.learnbind.ai.service.customers;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.CustomerPeopleAdjust;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface CustomerPeopleAdjustService extends IBaseService<CustomerPeopleAdjust, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerPeopleAdjust> searchCustomerPeopleAdjust(Long customerId, String searchCond);
	
    /** 
    	* @Title: insertCustomerPeopleAdjust
    	* @Description: 增加 
    	* @param @param banks
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertCustomerPeopleAdjust(CustomerPeopleAdjust people);
    
    /**
     * 修改
     * @param banks
     * @return
     */
    public int updateCustomerPeopleAdjust(CustomerPeopleAdjust people);
    
    /**
     * 删除
     * @param banksId
     * @return
     */
    public int deleteCustomerPeopleAdjust(long peopleId);

    /** 
    	* @Title: findAllCustomerPeopleAdjust
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<CustomerPeopleAdjust> findAllCustomerPeopleAdjust(int pageNum, int pageSize);
    
    
}
