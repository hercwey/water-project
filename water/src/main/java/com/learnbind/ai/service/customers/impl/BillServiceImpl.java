package com.learnbind.ai.service.customers.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.dao.CustomerBillInfoMapper;
import com.learnbind.ai.model.CustomerBillInfo;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BillService;



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
public class BillServiceImpl extends AbstractBaseService<CustomerBillInfo, Long> implements  BillService {
	
	@Autowired
	public CustomerBillInfoMapper customerBillInfoMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public BillServiceImpl(CustomerBillInfoMapper mapper) {
		this.customerBillInfoMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCustomerBillInfo
	 * @Description: 查询客户开票信息
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBillInfo.CustomerBillInfoService#searchCustomerBillInfo(java.lang.String)
	 */
	@Override
	public List<CustomerBillInfo> searchCustomerBillInfo(Long customerId, String searchCond) {
		return customerBillInfoMapper.searchCustomerBillInfo(customerId,searchCond);
	}

	@Override
	public List<Map<String, Object>> getCustomerInvoiceList(String searchCond) {
		return customerBillInfoMapper.getCustomerInvoiceList(searchCond);
	}
	
	@Override
	public List<Map<String, Object>> searchCustomerInvoiceList(String searchCond, Integer billType) {
		return customerBillInfoMapper.searchCustomerInvoiceList(searchCond, billType);
	}

	@Override
	public int getCustomerInvoiceCount(Long customerId) {
		CustomerBillInfo record = new CustomerBillInfo();
		record.setCustomerId(customerId);
		record.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
		return customerBillInfoMapper.selectCount(record);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertCustomerBillInfo
	 * @Description: 添加客户开票信息
	 * @param CustomerBillInfo
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBillInfo.CustomerBillInfoService#insertCustomerBillInfo(com.learnbind.ai.model.CustomerBillInfo)
	 */
	@Override
	public int insertCustomerBillInfo(CustomerBillInfo bill) {
		return customerBillInfoMapper.insertSelective(bill);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateCustomerBillInfo
	 * @Description: 修改客户档案
	 * @param CustomerBillInfo
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBillInfo.CustomerBillInfoService#updateCustomerBillInfo(com.learnbind.ai.model.CustomerBillInfo)
	 */
	@Override
	public int updateCustomerBillInfo(CustomerBillInfo bill) {
		return customerBillInfoMapper.updateByPrimaryKeySelective(bill);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCustomerBillInfo
	 * @Description: 删除客户档案
	 * @param CustomerBillInfoId
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBillInfo.CustomerBillInfoService#deleteCustomerBillInfo(long)
	 */
	@Override
	public int deleteCustomerBillInfo(long billId) {
		try {
			
			int rows = customerBillInfoMapper.deleteByPrimaryKey(billId);
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
	public PageInfo<CustomerBillInfo> findAllCustomerBillInfo(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerBillInfo> billList = customerBillInfoMapper.selectAll();
		PageInfo result = new PageInfo(billList);
		return result;
	}


}
