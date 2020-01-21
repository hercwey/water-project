package com.learnbind.ai.service.printer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.PrinterConfigMapper;
import com.learnbind.ai.dao.SysWaterPriceMapper;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.printer.PrinterService;
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
public class PrinterServiceImpl extends AbstractBaseService<PrinterConfig, Long> implements  PrinterService {
	
	@Autowired
	public PrinterConfigMapper printerMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public PrinterServiceImpl(PrinterConfigMapper mapper) {
		this.printerMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchPrinter
	 * @Description: 根据条件查询打印机
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.printer.PrinterService#searchPrinter(java.lang.String)
	 */
	@Override
	public List<PrinterConfig> searchPrinter(String searchCond) {
		return printerMapper.searchPrinter(searchCond);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertPrinter
	 * @Description: 添加打印机配置信息
	 * @param printer
	 * @return 
	 * @see com.learnbind.ai.service.printer.PrinterService#insertPrinter(com.learnbind.ai.model.PrinterConfig)
	 */
	@Override
	public int insertPrinter(PrinterConfig printer) {
		return printerMapper.insertSelective(printer);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updatePrinter
	 * @Description: 修改打印机配置信息
	 * @param printer
	 * @return 
	 * @see com.learnbind.ai.service.printer.PrinterService#updatePrinter(com.learnbind.ai.model.PrinterConfig)
	 */
	@Override
	public int updatePrinter(PrinterConfig printer) {
		return printerMapper.updateByPrimaryKeySelective(printer);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deletePrinter
	 * @Description: 根据id删除打印机配置
	 * @param printerId
	 * @return 
	 * @see com.learnbind.ai.service.printer.PrinterService#deletePrinter(long)
	 */
	@Override
	@Transactional
	public int deletePrinter(long printerId) {
		try {
			int rows = printerMapper.deleteByPrimaryKey(printerId);
			if(rows>0) {
				PrinterConfig printer = new PrinterConfig();
				printer.setId(printerId);
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
	public PageInfo<PrinterConfig> findAllPrinter(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<PrinterConfig> printerList = printerMapper.selectAll();
		PageInfo result = new PageInfo(printerList);
		return result;
	}


}
