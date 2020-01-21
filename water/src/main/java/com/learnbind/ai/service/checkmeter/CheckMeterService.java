package com.learnbind.ai.service.checkmeter;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface CheckMeterService extends IBaseService<SysCheckMeter, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysCheckMeter> searchCheckMeter(String searchCond);
	
    /** 
    	* @Title: insertCheckMeter
    	* @Description: 增加 
    	* @param @param priceType
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertCheckMeter(SysCheckMeter checkMeter);
    
    /**
     * 修改
     * @param role
     * @return
     */
    public int updateCheckMeter(SysCheckMeter checkMeter);
    
    /**
     * 删除
     * @param roleId
     * @return
     */
    public int deleteCheckMeter(long checkMeterId);

    /** 
    	* @Title: findAllCheckMeter
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<SysCheckMeter> findAllCheckMeter(int pageNum, int pageSize);
    
    
}
