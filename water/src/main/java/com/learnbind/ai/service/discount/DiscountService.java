package com.learnbind.ai.service.discount;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface DiscountService extends IBaseService<SysDiscount, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysDiscount> searchDiscount(String searchCond);
	
    /** 
    	* @Title: insertDiscount 
    	* @Description: 增加 
    	* @param @param discount
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertDiscount(SysDiscount discount);
    
    /**
     * 修改
     * @param discount
     * @return
     */
    public int updateDiscount(SysDiscount discount);
    
    /**
     * 删除
     * @param discountId
     * @return
     */
    public int deleteDiscount(long discountId);

    /** 
    	* @Title: findAllDiscount
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<SysDiscount> findAllDiscount(int pageNum, int pageSize);
    
    

    
    
}
