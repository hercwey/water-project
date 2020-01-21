package com.learnbind.ai.service.importexcel;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.importexcel
 *
 * @Title: ImportExcelService.java
 * @Description: 导入Excel服务接口类
 *
 * @author Administrator
 * @date 2019年6月27日 下午8:27:19
 * @version V1.0 
 *
 */
public interface ImportExcelService {
	
	/**
	 * @Title: parseLocationExcelDataToDb
	 * @Description: 解析地理位置Excel数据并保存到数据库
	 * @param locationId
	 * @param locationStrList
	 * @return 
	 */
	public int parseLocationExcelDataToDb(Long locationId, List<String> locationStrList);
	
	/**
	 * @Title: parseMeterExcelDataToDb
	 * @Description: 解析表计Excel数据并保存到数据库
	 * @param locationBlockCode	地理位置-小区编码
	 * @param pid	父表计ID（可为空）
	 * @param meterList
	 * @return 
	 */
	public int parseMeterExcelDataToDb(String locationBlockCode, Long pid, List<Map> meterList);
	
	/**
	 * @Title: parseMultiMeterExcelDataToDb
	 * @Description: 解析一户多表表计Excel数据并保存到数据库
	 * @param locationBlockCode	地理位置-小区编码
	 * @param pid	父表计ID（可为空）
	 * @param meterList
	 * @return 
	 */
	public int parseMultiMeterExcelDataToDb(String locationBlockCode, Long pid, List<Map> meterList);
	
	/**
	 * @Title: parseCustomerExcelDataToDb
	 * @Description: 解析客户Excel数据并保存到数据库
	 * @param locationBlockCode
	 * @param customerList
	 * @return 
	 */
	public int parseCustomerExcelDataToDb(String locationBlockCode, List<Map> customerList);
	
	/**
	 * @Title: parseMeterReadExcelDataToDb
	 * @Description: 解析更新数据Excel数据到数据库
	 * @param traceIds
	 * @param meterList
	 * @return 
	 */
	public int parseUpdateDataExcelDataToDb(String traceIds, List<Map> meterList);
	
	/**
	 * @Title: parseAppMeterRecordToDb
	 * @Description: 解析app抄表记录excel内容，保存到数据库
	 * @param traceIds
	 * @param meterList
	 * @return 
	 */
	public int parseAppMeterRecordToDb(String traceIds, List<Map> meterList);
	
	/**
	 * @Title: parsePastOweDataToDb
	 * @Description: 解析往期欠费并保存到数据库
	 * @param traceIds
	 * @param meterList
	 * @return 
	 */
	public int parsePastOweDataToDb(String traceIds, List<Map> meterList);
	
	/**
	 * @Title: parsePrepaymentDataToDb
	 * @Description: 解析预存金额并保存到数据库
	 * @param traceIds
	 * @param meterList
	 * @return 
	 */
	public int parsePrepaymentDataToDb(String traceIds, List<Map> meterList);
	
}
