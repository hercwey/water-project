package com.learnbind.ai.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.listener
 *
 * @Title: ApplicationFailedEventListener.java
 * @Description: 监听应用启动失败事件
 *
 * @author Administrator
 * @date 2019年8月11日 上午11:41:25
 * @version V1.0 
 *
 */
@Component
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {

	private final Log log = LogFactory.getLog(getClass());//日志
	
	@Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        log.info("----------	系统启动失败......");
    	log.info("----------	"+event.getClass());
    }
	
}
