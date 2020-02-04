package com.learnbind;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import tk.mybatis.spring.annotation.MapperScan;

/**
*	Copyright (c) 2018 by Hz
*	@ClassName:     SpringBootWaterApplication.java
*	@Description:   启动类 
* 
*	@author:        lenovo
*	@version:       V1.0  
*	@Date:          2018年9月12日 上午9:51:30 
*/
@SpringBootApplication
@EnableScheduling
//mapper接口类的包名 加上
@MapperScan("com.learnbind.ai.dao")
//@MapperScan(basePackages = "com.learnbind.ai.dao")
@ServletComponentScan("com.learnbind.ai.common.util")
public class SpringBootWaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWaterApplication.class, args);
	}
	
	/**
	 * @Title: threadPoolTaskScheduler
	 * @Description: 配置定时任务线程池Bean
	 * @return 
	 */
	@Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
	     executor.setPoolSize(20);
	     executor.setThreadNamePrefix("taskExecutor-");
	     //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean。
	     executor.setWaitForTasksToCompleteOnShutdown(true);
	     //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
	     executor.setAwaitTerminationSeconds(60);
	     return executor;
    }
	 
	
	/**
	 * 创建TopicExchange
	 * @return
	 */
//	@Bean
//    TopicExchange exchange() {
//        return new TopicExchange(QuartzTaskMQConstant.QUARTZ_TASK_TOPIC_EXCHANGE);
//    }
	
	/**
	 * 创建队列Queue
	 * @return
	 */
//	@Bean
//    public Queue queueSync() {
//        return new Queue(QuartzTaskMQConstant.QUARTZ_TASK_QUEUE);
//    }
	/**
	 * 	将队列topic.quartz.task.queue与exchange绑定，binding_key为topic.quartz.task.queue,就是完全匹配
	 * 		RabbitMQSyncReceiver.java//医嘱信息
	 * @param queueSync
	 * @param exchange
	 * @return 
	 */
//	@Bean
//    Binding bindingExchangeDoctorAdvice(Queue queueSync, TopicExchange exchange) {
//        return BindingBuilder.bind(queueSync).to(exchange).with(QuartzTaskMQConstant.QUARTZ_TASK_QUEUE);
//    }
	
}
