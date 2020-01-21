package com.learnbind.ai.service.waterprice.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumIsLadderPrice;
import com.learnbind.ai.dao.SysWaterPriceMapper;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


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
public class WaterPriceServiceImpl extends AbstractBaseService<SysWaterPrice, Long> implements  WaterPriceService {
	
	@Autowired
	public SysWaterPriceMapper sysWaterPriceMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WaterPriceServiceImpl(SysWaterPriceMapper mapper) {
		this.sysWaterPriceMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchWaterPrice
	 * @Description: 根据条件查询水价
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.waterprice.WaterPriceService#searchWaterPrice(java.lang.String)
	 */
	@Override
	public List<SysWaterPrice> searchWaterPrice(String searchCond) {
		return sysWaterPriceMapper.searchWaterPrice(searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertWaterPrice
	 * @Description: 添加水价配置信息
	 * @param waterPrice
	 * @return 
	 * @see com.learnbind.ai.service.waterprice.WaterPriceService#insertWaterPrice(com.learnbind.ai.model.SysWaterPrice)
	 */
	@Override
	public int insertWaterPrice(SysWaterPrice waterPrice) {
		return sysWaterPriceMapper.insertSelective(waterPrice);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateWaterPrice
	 * @Description: 修改水价配置信息
	 * @param waterPrice
	 * @return 
	 * @see com.learnbind.ai.service.waterprice.WaterPriceService#updateWaterPrice(com.learnbind.ai.model.SysWaterPrice)
	 */
	@Override
	public int updateWaterPrice(SysWaterPrice waterPrice) {
		return sysWaterPriceMapper.updateByPrimaryKeySelective(waterPrice);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteWaterPrice
	 * @Description: 根据id删除用户水价配置
	 * @param waterPriceId
	 * @return 
	 * @see com.learnbind.ai.service.waterprice.WaterPriceService#deleteWaterPrice(long)
	 */
	@Override
	@Transactional
	public int deleteWaterPrice(long waterPriceId) {
		try {
			int rows = sysWaterPriceMapper.deleteByPrimaryKey(waterPriceId);
			if(rows>0) {
				SysWaterPrice waterPrice = new SysWaterPrice();
				waterPrice.setId(waterPriceId);
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
	public PageInfo<SysWaterPrice> findAllWaterPrice(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<SysWaterPrice> waterPriceList = sysWaterPriceMapper.selectAll();
		PageInfo result = new PageInfo(waterPriceList);
		return result;
	}

	
	/**
	 * @Title: getPriceTypeList
	 * @Description: 查询价格类型编码和名称
	 * @return 
	 * 		价格类型编码和名称集合
	 */
	@Override
	public List<Map<String, Object>> getPriceTypeList() {
		return sysWaterPriceMapper.getPriceTypeList();
	}

	@Override
	public String getPriceTypeName(String priceTypeCode) {
		return sysWaterPriceMapper.getPriceTypeName(priceTypeCode);
	}

	@Override
	public SysWaterPrice getWaterPriceByPriceCode(String priceCode) {
		return sysWaterPriceMapper.getWaterPriceByPriceCode(priceCode);
	}

	@Override
	public List<SysWaterPrice> getNotJmshysPriceList() {
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("isLadderPrice",EnumIsLadderPrice.LADDER_PRICE_NO.getValue()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = this.selectByExample(example);
		return waterPriceList;
	}

	@Override
	public List<SysWaterPrice> getJmshysPriceList() {
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("isLadderPrice",EnumIsLadderPrice.LADDER_PRICE_YES.getValue()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = this.selectByExample(example);
		return waterPriceList;
	}
	
	@Override
	public SysWaterPrice getWaterPrice(String ladderName) {
		
		if(StringUtils.isBlank(ladderName)) {
			return null;
		}
		
		SysWaterPrice wp = new SysWaterPrice();
		wp.setLadderName(ladderName);
		List<SysWaterPrice> waterPriceList = sysWaterPriceMapper.select(wp);
		if(waterPriceList!=null && waterPriceList.size()>0) {
			return waterPriceList.get(0);
		}
		return null;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getFreeWater
	 * @Description: 获取免费水
	 * @return 
	 * @see com.learnbind.ai.service.waterprice.WaterPriceService#getFreeWater()
	 */
	@Override
	public SysWaterPrice getFreeWater() {
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("totalPrice", 0l).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = this.selectByExample(example);
		return waterPriceList.get(0);
	}


}
