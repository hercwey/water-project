package com.learnbind.ai.service.task;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Component
@Controller
public class TestTask {

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

	@RequestMapping("/startCron")
	public String startCron() {

		future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("0 0/1 * * * *"));
		System.out.println("DynamicTask.startCron()");
		return "startCron";
	}

	@RequestMapping("/stopCron")
	public String stopCron() {

		if (future != null) {
			future.cancel(true);
		}
		System.out.println("Task.stopCron()");
		return "stopCron";
	}

	@RequestMapping("/changeCron10")
	public String startCron10() {

		stopCron();// 先停止，在开启.
		future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("*/10 * * * * *"));
		System.out.println("DynamicTask.startCron10()");
		return "changeCron10";
	}

	private class MyRunnable implements Runnable {

		@Override
		public void run() {
			System.out.println("Task.MyRunnable.run()，" + new Date());
		}
	}
	
}
