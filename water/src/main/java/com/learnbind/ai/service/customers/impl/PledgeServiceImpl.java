package com.learnbind.ai.service.customers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.dao.CustomerPledgeMapper;
import com.learnbind.ai.model.CustomerPledge;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.PledgeService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: PledgeServiceImpl.java
 * @Description: 客户押金信息
 *
 * @author Thinkpad
 * @date 2019年5月21日 下午5:10:44
 * @version V1.0 
 *
 */
@Service
public class PledgeServiceImpl extends AbstractBaseService<CustomerPledge, Long> implements  PledgeService {
	
	@Autowired
	public CustomerPledgeMapper customerPledgeMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public PledgeServiceImpl(CustomerPledgeMapper mapper) {
		this.customerPledgeMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCustomerPledge
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPledge.CustomerPledgeService#searchCustomerPledge(java.lang.String)
	 */
	@Override
	public List<CustomerPledge> searchCustomerPledge(Long customerId, String searchCond) {
		return customerPledgeMapper.searchCustomerPledge(customerId, searchCond);
	}								  

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertCustomerPledge
	 * @Description: 添加客户开票信息
	 * @param CustomerPledge
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPledge.CustomerPledgeService#insertCustomerPledge(com.learnbind.ai.model.CustomerPledge)
	 */
	@Override
	public int insertCustomerPledge(CustomerPledge pledge) {
		return customerPledgeMapper.insertSelective(pledge);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateCustomerPledge
	 * @Description: 修改客户档案
	 * @param CustomerPledge
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPledge.CustomerPledgeService#updateCustomerPledge(com.learnbind.ai.model.CustomerPledge)
	 */
	@Override
	public int updateCustomerPledge(CustomerPledge pledge) {
		return customerPledgeMapper.updateByPrimaryKeySelective(pledge);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCustomerPledge
	 * @Description: 删除客户档案
	 * @param CustomerPledgeId
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPledge.CustomerPledgeService#deleteCustomerPledge(long)
	 */
	@Override
	public int deleteCustomerPledge(long pledgeId) {
		try {
			CustomerPledge pledge = new CustomerPledge();
			pledge.setId(pledgeId);
			pledge.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			int rows = customerPledgeMapper.updateByPrimaryKeySelective(pledge);
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<CustomerPledge> findAllCustomerPledge(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerPledge> pledgeList = customerPledgeMapper.selectAll();
		PageInfo result = new PageInfo(pledgeList);
		return result;
	}


}
