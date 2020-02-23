package com.learnbind.ai.listener;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.mq.north.AutoReportConsumer;
import com.learnbind.ai.mq.north.ConfigParmsConsumer;
import com.learnbind.ai.mq.north.ConfigThresholdConsumer;
import com.learnbind.ai.mq.north.ControlValveConsumer;
import com.learnbind.ai.mq.north.DeviceDeleteConsumer;
import com.learnbind.ai.mq.north.DeviceQueryConsumer;
import com.learnbind.ai.mq.north.DeviceRegisterConsumer;
import com.learnbind.ai.mq.north.DeviceUpdateConsumer;
import com.learnbind.ai.mq.north.OrderStatusConsumer;
import com.learnbind.ai.mq.north.QueryMonthDataConsumer;
import com.learnbind.ai.mq.north.QueryParmsConsumer;
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
//	@Autowired
//	private ConfigParmsProducer configParmsProducer;
	//---------------注入消费者类部分-------------------------------------------------------------------
	@Autowired
	private AutoReportConsumer autoReportConsumer;//消费者-设备自动上报数据
	@Autowired
	private ConfigParmsConsumer configParmsConsumer;//消费者-配置设备参数返回数据
	@Autowired
	private ConfigThresholdConsumer configThresholdConsumer;//消费者-设置阈值返回数据
	@Autowired
	private ControlValveConsumer controlValveConsumer;//消费者-控制设备（开关阀控制）返回数据
	@Autowired
	private DeviceDeleteConsumer deviceDeleteConsumer;//消费者-删除设备返回数据
	@Autowired
	private DeviceQueryConsumer deviceQueryConsumer;//消费者-查询设备返回数据
	@Autowired
	private DeviceRegisterConsumer deviceRegisterConsumer;//消费者-注册设备返回数据
	@Autowired
	private DeviceUpdateConsumer deviceUpdateConsumer;//消费者-修改设备返回数据
	@Autowired
	private OrderStatusConsumer orderStatusConsumer;//消费者-命令执行状态返回数据
	@Autowired
	private QueryMonthDataConsumer queryMonthDataConsumer;//消费者-查询月冻结返回数据
	@Autowired
	private QueryParmsConsumer queryParmsConsumer;//消费者-查询参数返回数据
	
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //EventUtil.dispatcher(IStartupEvent.class);
    	log.info("----------	系统启动成功......");
    	log.info("----------	"+event.getClass());
    	
    	//this.setDataToQueue();//查询已上传的批量代扣文件记录，并增加到待处理队列
    	//authService.login();
//    	try {
//			configParmsProducer.sendMsg("0", "1");
//		} catch (MQClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RemotingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MQBrokerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	//this.startConsumer();//启动消费者监听
    	
    }
    
    /**
     * @Title: startConsumer
     * @Description: 启动消费者监听
     */
    private void startConsumer() {
    	try {
			autoReportConsumer.start();//消费者-设备自动上报数据
			configParmsConsumer.start();//消费者-配置设备参数返回数据
	    	configThresholdConsumer.start();//消费者-设置阈值返回数据
	    	controlValveConsumer.start();//消费者-控制设备（开关阀控制）返回数据
	    	deviceDeleteConsumer.start();//消费者-删除设备返回数据
	    	deviceQueryConsumer.start();//消费者-查询设备返回数据
	    	deviceRegisterConsumer.start();//消费者-注册设备返回数据
	    	deviceUpdateConsumer.start();//消费者-修改设备返回数据
	    	orderStatusConsumer.start();//消费者-命令执行状态返回数据
	    	queryMonthDataConsumer.start();//消费者-查询月冻结返回数据
	    	queryParmsConsumer.start();//消费者-查询参数返回数据
		} catch (MQClientException e) {
			e.printStackTrace();
		}
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

