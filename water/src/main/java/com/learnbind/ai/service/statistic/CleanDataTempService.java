package com.learnbind.ai.service.statistic;

import java.util.List;

import com.learnbind.ai.model.CleanDataTempTable;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface CleanDataTempService extends IBaseService<CleanDataTempTable, Long> {
	
	/**
	 * @Title: batchCleanData
	 * @Description: 清理客户数据接口
	 * @param locationList
	 * @return 
	 */
	public int batchCleanCustomerData(List<Customers> customersList);
	
	
	/**
	 * @Title: batchCleanMeterData
	 * @Description: 清理表计数据
	 * @param metersList
	 * @return 
	 */
	public int batchCleanMeterData(List<Meters> metersList, String traceIds);
	

	/**
     * @Title: deleteBank
     * @Description: 删除客户的银行卡信息
     * @param customerId
     * @return 
     */
    public int deleteBank(Long customerId);

    /**
     * @Title: deleteBillInfo
     * @Description: 删除开票信息
     * @param customerId
     * @return 
     */
    public int deleteBillInfo(Long customerId);
    
    /**
     * @Title: deletePledge
     * @Description: 删除押金信息
     * @param customerId
     * @return 
     */
    public int deletePledge(Long customerId);
    
    /**
     * @Title: deletePeople
     * @Description: 删除多人口信息
     * @param customerId
     * @return 
     */
    public int deletePeople(Long customerId);
    
    /**
     * @Title: deletdAgreeMent
     * @Description: 删除客户协议信息
     * @param customerId
     * @return 
     */
    public int deletdAgreeMent(Long customerId);
    
    /**
     * @Title: deletdOverdueFine
     * @Description: 删除客户违约金信息
     * @param customerId
     * @return 
     */
    public int deletdOverdueFine(Long customerId);
    
    /**
     * @Title: deleteAccount
     * @Description: 删除客户账户信息
     * @param customerId
     * @return 
     */
    public int deleteAccount(Long customerId);
    
    /**
     * @Title: deleteAccountItem
     * @Description: 删除客户账单信息
     * @param customerId
     * @return 
     */
    public int deleteAccountItem(Long customerId);
    
    /**
     * @Title: deleteTrace
     * @Description: 删除客户日志信息
     * @param customerId
     * @return 
     */
    public int deleteTrace(Long customerId);
    
    /**
     * @Title: deleteMeter
     * @Description: 删除客户表计信息
     * @param customerId
     * @return 
     */
    public int deleteMeter(Long customerId);
    
    /**
     * @Title: deleteLocation
     * @Description: 删除地理位置客户信息
     * @param customerId
     * @return 
     */
    public int deleteLocation(Long customerId);
    
    /**
	 * @Title: deletedPartWater
	 * @Description: 删除分水量记录
	 * @param meterId
	 * @return 
	 */
	public int deletedPartWater(Long customerId);
	
    /**
     * @Title: deleteMeterBookMeter
     * @Description: 删除表册表计关系表信息
     * @param meterId
     * @return 
     */
    public int deleteMeterBookMeter(Long meterId);
    

	/**
	 * @Title: deletedMeterRecordTemp
	 * @Description: 删除APP抄表记录
	 * @param meterId
	 * @return 
	 */
	public int deletedMeterRecordTemp(Long meterId);
	
	/**
	 * @Title: deletedMeterRecordTempPhoto
	 * @Description: 删除APP抄表记录照片
	 * @param meterId
	 * @return 
	 */
	public int deletedMeterRecordTempPhoto(Long meterId);
	
	/**
	 * @Title: deletedMeterRecordPhoto
	 * @Description: 删除抄表记录照片
	 * @param meterId
	 * @return 
	 */
	public int deletedMeterRecordPhoto(Long meterId);
	
	/**
	 * @Title: deletedMeterRecord
	 * @Description: 删除抄表记录
	 * @param meterId
	 * @return 
	 */
	public int deletedMeterRecord(Long meterId);
	
	
	/**
	 * @Title: deletedPartWaterRule
	 * @Description: 删除分水量规则
	 * @param meterId
	 * @return 
	 */
	public int deletedPartWaterRule(Long meterId);
	
	/**
	 * @Title: deletedPartWaterRuleTrace
	 * @Description: 清除分水量规则日志表
	 * @param meterId
	 * @return 
	 */
	public int deletedPartWaterRuleTrace(Long meterId);
	
	/**
	 * @Title: deletedMeterTree
	 * @Description: 删除表计父子关系
	 * @param meterId
	 * @return 
	 */
	public int deletedMeterTree(Long meterId);
	
	/**
	 * @Title: deletedMeterLocation
	 * @Description: 删除地理位置表计
	 * @param meterId
	 * @return 
	 */
	public int deletedMeterLocation(Long meterId);
	
    
	
}
