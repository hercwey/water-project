package com.learnbind.ai.service.customers.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.CustomerBanksMapper;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BankService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.CustomerBanks.impl
 *
 * @Title: CustomerBanksServiceImpl.java
 * @Description: 客户档案
 *
 * @author Thinkpad
 * @date 2019年5月20日 下午12:29:27
 * @version V1.0 
 *
 */
@Service
public class BankServiceImpl extends AbstractBaseService<CustomerBanks, Long> implements  BankService {
	
	@Autowired
	public CustomerBanksMapper customerBanksMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public BankServiceImpl(CustomerBanksMapper mapper) {
		this.customerBanksMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCustomerBanks
	 * @Description: 查询客户档案
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBanks.CustomerBanksService#searchCustomerBanks(java.lang.String)
	 */
	@Override
	public List<CustomerBanks> searchCustomerBanks(Long customerId, String searchCond) {
		return customerBanksMapper.searchCustomerBanks(customerId, searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertCustomerBanks
	 * @Description: 添加客户档案
	 * @param CustomerBanks
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBanks.CustomerBanksService#insertCustomerBanks(com.learnbind.ai.model.CustomerBanks)
	 */
	@Override
	public int insertCustomerBanks(CustomerBanks customerBanks) {
		return customerBanksMapper.insertSelective(customerBanks);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateCustomerBanks
	 * @Description: 修改客户档案
	 * @param CustomerBanks
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBanks.CustomerBanksService#updateCustomerBanks(com.learnbind.ai.model.CustomerBanks)
	 */
	@Override
	public int updateCustomerBanks(CustomerBanks customerBanks) {
		return customerBanksMapper.updateByPrimaryKeySelective(customerBanks);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCustomerBanks
	 * @Description: 删除客户档案
	 * @param CustomerBanksId
	 * @return 
	 * @see com.learnbind.ai.service.CustomerBanks.CustomerBanksService#deleteCustomerBanks(long)
	 */
	@Override
	public int deleteCustomerBanks(long customerBanksId) {
		try {
			
			int rows = customerBanksMapper.deleteByPrimaryKey(customerBanksId);
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
	public PageInfo<CustomerBanks> findAllCustomerBanks(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<CustomerBanks> bankList = customerBanksMapper.selectAll();
		PageInfo result = new PageInfo(bankList);
		return result;
	}

	@Override
	public List<CustomerBanks> getCustomerBanks(Long customerId) {
		List<CustomerBanks> bankList = customerBanksMapper.getCustomerBanks(customerId);
		return bankList;
	}

	/**
     * @Title: getCustomerBank
     * @Description: 查询客户-银行
     * @param customerId
     * @return 
     * 			返回启用状态为启用，且账户名和卡号都不为空的最新银行信息
     */
	@Override
	public CustomerBanks getCustomerBank(Long customerId) {
		List<CustomerBanks> bankList = this.getCustomerBanks(customerId);
		CustomerBanks bank = null;//返回的银行信息
		for(CustomerBanks bankTemp : bankList) {
			String accountName = bankTemp.getAccountName();//账户名
			String cardNo = bankTemp.getCardNo();//卡号
			if(StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(cardNo)) {
				bank = bankTemp;//返回的银行信息
				break;
			}
		}
		return bank;
	}

}
