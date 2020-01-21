package com.learnbind.ai.service.customers;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.CustomerPledge;
import com.learnbind.ai.model.CustomerPledge;
import com.learnbind.ai.model.CustomerPledge;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface PledgeService extends IBaseService<CustomerPledge, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerPledge> searchCustomerPledge(Long customerId, String searchCond);
	
    /** 
    	* @Title: insertCustomerPledge
    	* @Description: 增加 
    	* @param @param banks
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertCustomerPledge(CustomerPledge pledge);
    
    /**
     * 修改
     * @param banks
     * @return
     */
    public int updateCustomerPledge(CustomerPledge pledge);
    
    /**
     * 删除
     * @param banksId
     * @return
     */
    public int deleteCustomerPledge(long pledgeId);

    /** 
    	* @Title: findAllCustomerPledge
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<CustomerPledge> findAllCustomerPledge(int pageNum, int pageSize);
    
    
}
