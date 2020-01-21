package com.learnbind.ai.scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTast {
	
	private Log log = LogFactory.getLog(this.getClass());//日志 log
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    /**
     * @Title: scheduled
     * @Description: 定时任务
     * 
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void scheduled(){
    	
    	log.info("----------	【"+sdf.format(new Date())+"】	定时【每5分钟执行一次】开始执行。。。	");
        log.info("----------	【"+sdf.format(new Date())+"】	定时【每5分钟执行一次】任务执行完成。。。	");
        
    }
    
}