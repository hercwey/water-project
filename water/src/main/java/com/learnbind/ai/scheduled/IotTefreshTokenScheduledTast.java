package com.learnbind.ai.scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.iot.IAuthService;

/**
 * @author SRD
 *	定时任务执行电信平台Token刷新
 */
@Component
public class IotTefreshTokenScheduledTast {
	
	private Log log = LogFactory.getLog(this.getClass());//日志 log
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
    IAuthService authService;
	
    /**
     * @Title: scheduled
     * @Description: 定时任务
     * 
     */
    //@Scheduled(cron = "0 0/30 * * * ?")
    public void scheduled(){
    	
    	log.info("----------	【"+sdf.format(new Date())+"】	定时刷新【电信平台】Token【每30分钟执行一次】开始执行。。。	");
    	
    	JsonResult result = authService.taskRefreshToken();
    	log.debug("----------定时任务刷新Token结果："+result.toString());
    	
        log.info("----------	【"+sdf.format(new Date())+"】	定时刷新【电信平台】Token【每30分钟执行一次】任务执行完成。。。	");
        
    }
    
}