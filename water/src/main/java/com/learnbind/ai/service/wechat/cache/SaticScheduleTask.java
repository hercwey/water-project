package com.learnbind.ai.service.wechat.cache;

import java.time.LocalDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.learnbind.ai.controller.wechat.WechatPayController;

//TODO 暂时屏蔽,待发布时放开即可.
//@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
	private final Log log = LogFactory.getLog(WechatPayController.class);	
	
	//每10分钟触发一次.600 seconds
	final String CRON_SET="0/600 * * * * ?";
	//3.添加定时任务
    @Scheduled(cron = CRON_SET)    
	/*
	 * Cron表达式参数分别表示：
	 * 
	 * 	秒（0~59） 例如0/5表示每5秒 
	 * 	分（0~59） 
	 * 	时（0~23） 
	 * 	日（0~31）的某天，需计算 
	 * 	月（0~11） 
	 * 	周几（ 可填1-7 或 	SUN/MON/TUE/WED/THU/FRI/SAT）
	 * 
	 * 	@Scheduled：除了支持灵活的参数表达式cron之外，还支持简单的延时操作，例如 fixedDelay ，fixedRate 填写相应的毫秒数即可。
	 */
    
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        //System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    	log.debug("执行清除Cache任务"+LocalDateTime.now());
    	CacheManager.clearCache();
    }
}
