package com.learnbind.ai.service.customers;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface BankService extends IBaseService<CustomerBanks, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerBanks> searchCustomerBanks(Long customerId, String searchCond);
	
    /** 
    	* @Title: insertCustomerBanks
    	* @Description: 增加 
    	* @param @param banks
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertCustomerBanks(CustomerBanks banks);
    
    /**
     * 修改
     * @param banks
     * @return
     */
    public int updateCustomerBanks(CustomerBanks banks);
    
    /**
     * 删除
     * @param banksId
     * @return
     */
    public int deleteCustomerBanks(long banksId);

    /** 
    	* @Title: findAllCustomerBanks
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<CustomerBanks> findAllCustomerBanks(int pageNum, int pageSize);
    
    
    /**
     * @Title: getCustomerBanks
     * @Description: 获取客户银行信息
     * @param customerId
     * @return 
     * 			返回启用状态为启用的银行信息集合
     */
    public List<CustomerBanks> getCustomerBanks(Long customerId);
    
    /**
     * @Title: getCustomerBank
     * @Description: 查询客户-银行
     * @param customerId
     * @return 
     * 			返回启用状态为启用，且账户名和卡号都不为空的最新银行信息
     */
    public CustomerBanks getCustomerBank(Long customerId);
    
}
