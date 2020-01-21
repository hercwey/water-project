package com.learnbind.ai.service.customers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.CustomerPeopleAdjustMapper;
import com.learnbind.ai.model.CustomerPeopleAdjust;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerPeopleAdjustService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: BillServiceImpl.java
 * @Description: 客户开票信息
 *
 * @author Thinkpad
 * @date 2019年5月21日 下午5:10:44
 * @version V1.0 
 *
 */
@Service
public class CustomerPeopleAdjustServiceImpl extends AbstractBaseService<CustomerPeopleAdjust, Long> implements  CustomerPeopleAdjustService {
	
	@Autowired
	public CustomerPeopleAdjustMapper customerPeopleAdjustMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerPeopleAdjustServiceImpl(CustomerPeopleAdjustMapper mapper) {
		this.customerPeopleAdjustMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCustomerPeopleAdjust
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPeopleAdjust.CustomerPeopleAdjustService#searchCustomerPeopleAdjust(java.lang.String)
	 */
	@Override
	public List<CustomerPeopleAdjust> searchCustomerPeopleAdjust(Long customerId, String searchCond) {
		return customerPeopleAdjustMapper.searchCustomerPeopleAdjust(customerId, searchCond);
	}								  

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertCustomerPeopleAdjust
	 * @Description: 添加客户开票信息
	 * @param CustomerPeopleAdjust
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPeopleAdjust.CustomerPeopleAdjustService#insertCustomerPeopleAdjust(com.learnbind.ai.model.CustomerPeopleAdjust)
	 */
	@Override
	public int insertCustomerPeopleAdjust(CustomerPeopleAdjust people) {
		return customerPeopleAdjustMapper.insertSelective(people);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateCustomerPeopleAdjust
	 * @Description: 修改客户档案
	 * @param CustomerPeopleAdjust
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPeopleAdjust.CustomerPeopleAdjustService#updateCustomerPeopleAdjust(com.learnbind.ai.model.CustomerPeopleAdjust)
	 */
	@Override
	public int updateCustomerPeopleAdjust(CustomerPeopleAdjust people) {
		return customerPeopleAdjustMapper.updateByPrimaryKeySelective(people);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCustomerPeopleAdjust
	 * @Description: 删除客户档案
	 * @param CustomerPeopleAdjustId
	 * @return 
	 * @see com.learnbind.ai.service.CustomerPeopleAdjust.CustomerPeopleAdjustService#deleteCustomerPeopleAdjust(long)
	 */
	@Override
	public int deleteCustomerPeopleAdjust(long peopleId) {
		try {
			
			int rows = customerPeopleAdjustMapper.deleteByPrimaryKey(peopleId);
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
	public PageInfo<CustomerPeopleAdjust> findAllCustomerPeopleAdjust(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerPeopleAdjust> peopleList = customerPeopleAdjustMapper.selectAll();
		PageInfo result = new PageInfo(peopleList);
		return result;
	}


}
