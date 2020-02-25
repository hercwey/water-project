package com.space.water.iot.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class IotApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotApiApplication.class, args);
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
}
