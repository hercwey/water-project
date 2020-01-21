package com.learnbind.ai.service.discount.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.SysDiscountMapper;
import com.learnbind.ai.dao.SysRolesMapper;
import com.learnbind.ai.dao.SysWaterPriceMapper;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.model.SysUsersRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.rolesrights.RolesRightsService;
import com.learnbind.ai.service.usersroles.UsersRolesService;
import com.learnbind.ai.service.waterprice.WaterPriceService;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     WaterPriceServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class DiscountServiceImpl extends AbstractBaseService<SysDiscount, Long> implements  DiscountService {
	
	@Autowired
	public SysDiscountMapper discountMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public DiscountServiceImpl(SysDiscountMapper mapper) {
		this.discountMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchDiscount
	 * @Description: 查询政策减免
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.discount.DiscountService#searchDiscount(java.lang.String)
	 */
	@Override
	public List<SysDiscount> searchDiscount(String searchCond) {
		return discountMapper.searchDiscount(searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertDiscount
	 * @Description: 添加减免政策
	 * @param discount
	 * @return 
	 * @see com.learnbind.ai.service.discount.DiscountService#insertDiscount(com.learnbind.ai.model.SysDiscount)
	 */
	@Override
	public int insertDiscount(SysDiscount discount) {
		return discountMapper.insertSelective(discount);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateDiscount
	 * @Description: 修改减免政策
	 * @param discount
	 * @return 
	 * @see com.learnbind.ai.service.discount.DiscountService#updateDiscount(com.learnbind.ai.model.SysDiscount)
	 */
	@Override
	public int updateDiscount(SysDiscount discount) {
		return discountMapper.updateByPrimaryKeySelective(discount);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteDiscount
	 * @Description: 删除减免政策
	 * @param discountId
	 * @return 
	 * @see com.learnbind.ai.service.discount.DiscountService#deleteDiscount(long)
	 */
	@Override
	@Transactional
	public int deleteDiscount(long discountId) {
		try {
			int rows = discountMapper.deleteByPrimaryKey(discountId);
			if(rows>0) {
				SysDiscount discount = new SysDiscount();
				discount.setId(discountId);
				}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<SysDiscount> findAllDiscount(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<SysDiscount> discountList = discountMapper.selectAll();
		PageInfo result = new PageInfo(discountList);
		return result;
	}


}
