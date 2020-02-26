package com.space.water.iot.api.scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.service.IAuthService;
import com.space.water.iot.api.service.ISubscribeService;

/**
 * @author SRD
 *	定时任务执行电信平台Token刷新
 */
@Configuration
@EnableScheduling
public class IotTefreshTokenScheduledTask {
	
	private Log log = LogFactory.getLog(this.getClass());//日志 log
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
    IAuthService authService;
	@Autowired
	ISubscribeService subService;
	
    /**
     * @Title: scheduled
     * @Description: 定时任务
     * 
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scheduled(){
    	
    	log.info("----------	【"+sdf.format(new Date())+"】	定时刷新【电信平台】Token【每30分钟执行一次】开始执行。。。	");
    	
    	JsonResult result = authService.login();
    	log.debug("----------定时任务刷新Token结果："+result.toString());
//    	JsonResult subscribeResult = subService.subscribeDeviceData();
        log.info("----------	【"+sdf.format(new Date())+"】	定时刷新【电信平台】Token【每30分钟执行一次】任务执行完成。。。	");
        
    }
    
}