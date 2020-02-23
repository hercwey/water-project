package com.learnbind.ai.listener;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.ccb.CcbScheduleTask;
import com.learnbind.ai.service.ccb.CcbWithholdRecordQueue;
import com.learnbind.ai.service.iot.impl.AuthService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.listener
 *
 * @Title: ApplicationReadyEventListener.java
 * @Description: 监听应用启动事件
 *
 * @author Administrator
 * @date 2019年8月11日 上午11:41:10
 * @version V1.0 
 *
 */
@Component
public class ApplicationReadyEventListener implements  ApplicationListener<ApplicationReadyEvent> {

	private final Log log = LogFactory.getLog(getClass());//日志
	
	//@Autowired
	//private QuartzTaskMQSender quartzTaskMQSender;//发送MQ消息
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//CCB批量代扣记录
	@Autowired
	private CcbScheduleTask ccbScheduleTask;//ccb定时任务
	@Autowired
	private AuthService authService;//iot
	
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //EventUtil.dispatcher(IStartupEvent.class);
    	log.info("----------	系统启动成功......");
    	log.info("----------	"+event.getClass());
    	
    	//this.setDataToQueue();//查询已上传的批量代扣文件记录，并增加到待处理队列
    	//authService.login();
    	
    }
    
    /**
     * @Title: setQueue
     * @Description: 查询已上传的批量代扣文件记录，并增加到待处理队列
     */
    private void setDataToQueue() {

    	Example example = new Example(CcbBatchWithholdRecord.class);
    	example.createCriteria().andEqualTo("status", EnumCcbBatchStatus.APPLY_PROCESS.getValue()).orEqualTo("status", EnumCcbBatchStatus.PROCESS_COMPLETE.getValue());
    	List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectByExample(example);
    	if(recordList!=null && recordList.size()>0) {
    		for(CcbBatchWithholdRecord record : recordList) {
        		CcbWithholdRecordQueue.offer(record);//把批量代扣文件记录加入到队列，用定时任务的方式去查询凭证状态
        		log.info("----------批量代扣文件记录已增加到队列："+record);
        	}
        	ccbScheduleTask.start();//启动定时任务
    	}
    	
    }
    
}

