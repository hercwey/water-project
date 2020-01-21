package com.learnbind.ai.service.importexcel;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.importexcel
 *
 * @Title: ImportBigMeterExcelService.java
 * @Description: 导入大表Excel服务接口类
 *
 * @author Administrator
 * @date 2019年6月27日 下午8:27:19
 * @version V1.0 
 *
 */
public interface ImportBigMeterExcelService {
	
	/**
	 * @Title: parseBigMeterExcelDataToDb
	 * @Description: 解析大表Excel数据并保存到数据库
	 * @param locationBlockCode
	 * @param bigMeterMapList
	 * @return 
	 */
	public int parseBigMeterExcelDataToDb(String locationBlockCode, List<Map> bigMeterMapList);
	
}
