package com.learnbind.ai.service.printer;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface PrinterService extends IBaseService<PrinterConfig, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<PrinterConfig> searchPrinter(String searchCond);
	
    /** 
    	* @Title: insertPrinter 
    	* @Description: 增加 
    	* @param @param printer
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertPrinter(PrinterConfig printer);
    
    /**
     * 修改
     * @param printer
     * @return
     */
    public int updatePrinter(PrinterConfig printer);
    
    /**
     * 删除
     * @param printerId
     * @return
     */
    public int deletePrinter(long printerId);

    /** 
    	* @Title: findAllPrinter
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<PrinterConfig> findAllPrinter(int pageNum, int pageSize);
    
    
}
