package com.learnbind.ai.service.ccb;

import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbScheduleTask.java
 * @Description: 建设银行定时任务
 *
 * @author Administrator
 * @date 2019年10月29日 上午11:36:08
 * @version V1.0 
 *
 */
//TODO 暂时屏蔽,待发布时放开即可.
@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class CcbScheduleTask {
	
	/**
	 * @Fields log：日志
	 */
	private final Log log = LogFactory.getLog(CcbScheduleTask.class);	
	
	/**
	 * @Fields threadPoolTaskScheduler：线程池任务调度类，能够开启线程池进行任务调度
	 * 
	 * 	ThreadPoolTaskScheduler.schedule()方法会创建一个定时计划ScheduledFuture，在这个方法需要添加两个参数，Runnable（线程接口类） 和CronTrigger（定时任务触发器）
		在ScheduledFuture中有一个cancel可以停止定时任务。
	 * 
	 */
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	/**
	 * @Fields future：定时计划
	 */
	private ScheduledFuture<?> future;
	
	/**
	 * @Title: start
	 * @Description: 启动定时任务
	 * 			默认每分钟执行一次
	 * @return 
	 */
	public ScheduledFuture<?> start() {

		if(future==null) {
			log.debug("----------定时计划为空,启动定时任务...");
			log.debug("----------启动定时任务");
			
			String cron = "0 0/1 * * * *";//每分钟执行一次
			//String cron = "0/5 * * * * *";//每5秒执行一次
			
			future = threadPoolTaskScheduler.schedule(new CcbScheduleTaskThread(), new CronTrigger(cron));
			
		}else {
			log.debug("----------定时计划不为空,不需要重启启动...");
		}
		return future;
	}
	
	/**
	 * @Title: start
	 * @Description: 启动定时任务
	 * @param cron	为空时使用默认值，每分钟执行一次
	 * @return 
	 */
	public ScheduledFuture<?> start(String cron) {

		if(future==null) {
			log.debug("----------定时计划为空,启动定时任务...");
			log.debug("----------启动定时任务");
			
			String defaultCron = "0 0/1 * * * *";//每分钟执行一次
			//String defaultCron = "0/5 * * * * *";//每5秒执行一次
			if(StringUtils.isBlank(cron)) {
				cron = defaultCron;
			}
			
			future = threadPoolTaskScheduler.schedule(new CcbScheduleTaskThread(), new CronTrigger(cron));
			
		}else {
			log.debug("----------定时计划不为空,不需要重启启动...");
		}
		return future;
	}

	/**
	 * @Title: stop
	 * @Description: 停止定时任务
	 * 
	 * 			Future.cancel(true)适用于： 
				1. 长时间处于运行的任务，并且能够处理interruption
				
				Future.cancel(false)适用于： 
				1. 未能处理interruption的任务 
				2. 不清楚任务是否支持取消 
				3. 需要等待已经开始的任务执行完成
	 * 
	 * 
	 * @return 
	 */
	public boolean stop() {

		log.debug("----------停止定时任务");
		
		if (future != null) {
//			while (true) {
//				
//			}
			//取消任务，未开始或已完成返回false，参数表示是否中断执行中的线程
			boolean flag = future.cancel(true);
			log.debug("----------取消任务返回结果："+flag+"，future="+future);
			future = null;
			log.debug("----------设置future为null");
			return true;
		}
		
		return false;
	}

	/**
	 * @Title: getFuture
	 * @Description: 获取定时计划
	 * @return 
	 */
	public ScheduledFuture<?> getFuture() {
		return future;
	}

}
