package com.learnbind.ai.service.notify;

import java.util.List;

import com.learnbind.ai.model.WaterNotifyDetail;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface WaterNotifyDetailService extends IBaseService<WaterNotifyDetail, Long> {
	
	public List<WaterNotifyDetail> getNotifyDetail(Long notifyId);

    
}
