package com.learnbind.ai.service.meterrecord;

import java.util.List;

import com.learnbind.ai.model.MeterRecordTempPhoto;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meterrecord
 *
 * @Title: MeterRecordTempPhotoService.java
 * @Description: 抄表记录临时照片服务接口
 *
 * @author Administrator
 * @date 2019年7月24日 上午6:38:54
 * @version V1.0 
 *
 */
public interface MeterRecordTempPhotoService extends IBaseService<MeterRecordTempPhoto, Long> {
	
	/**
	 * @Title: getListBy
	 * @Description: 根据客户ID，表计ID和操作员ID查询APP抄表记录照片集合
	 * @param customerId
	 * @param meterId
	 * @param operatorId
	 * @return 
	 */
	public List<MeterRecordTempPhoto> getList(Long customerId, Long meterId, Long operatorId);
	
}
