package com.learnbind.ai.service.waterprice;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.controller.waterprice.WaterPriceController;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface WaterPriceService extends IBaseService<SysWaterPrice, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysWaterPrice> searchWaterPrice(String searchCond);
	
    /** 
    	* @Title: insertWaterPrice 
    	* @Description: 增加 
    	* @param @param priceType
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertWaterPrice(SysWaterPrice waterPrice);
    
    /**
     * 修改
     * @param role
     * @return
     */
    public int updateWaterPrice(SysWaterPrice waterPrice);
    
    /**
     * 删除
     * @param roleId
     * @return
     */
    public int deleteWaterPrice(long waterPriceId);

    /** 
    	* @Title: findAllWaterPrice
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<SysWaterPrice> findAllWaterPrice(int pageNum, int pageSize);
    
    /**
	 * @Title: getPriceTypeList
	 * @Description: 查询价格类型编码和名称
	 * @return 
	 * 		价格类型编码和名称集合
	 */
	public List<Map<String, Object>> getPriceTypeList();
	
	/**
	 * @Title: getPriceTypeName
	 * @Description: 根据价格类型编码获取价格类型名称
	 * @param priceTypeCode
	 * @return 
	 */
	public String getPriceTypeName(String priceTypeCode);
	
	/**
	 * @Title: getWaterPriceByPriceCode
	 * @Description: 根据priceCode获取水价
	 * @param priceCode
	 * @return 
	 */
	public SysWaterPrice getWaterPriceByPriceCode(String priceCode);
	
	/**
	 * @Title: getNotJMSHYSPriceList
	 * @Description: 查找除了居民生活用水之外的所有价格数据
	 * @return 
	 */
	public List<SysWaterPrice> getNotJmshysPriceList();
    
	/**
	 * @Title: getJmshysPriceList
	 * @Description: 查找居民生活用水所有价格数据
	 * @return 
	 */
	public List<SysWaterPrice> getJmshysPriceList();
	
	/**
	 * @Title: getWaterPrice
	 * @Description: 获取水价
	 * @param ladderName
	 * @return 
	 */
	public SysWaterPrice getWaterPrice(String ladderName);
	
	/**
	 * @Title: getFreeWater
	 * @Description: 获取免费水
	 * @return 
	 */
	public SysWaterPrice getFreeWater();
    
}
