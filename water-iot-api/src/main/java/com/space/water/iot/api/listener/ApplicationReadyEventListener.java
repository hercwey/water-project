package com.space.water.iot.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.rocketmq.Consumer;
import com.space.water.iot.api.service.IAuthService;
import com.space.water.iot.api.service.ISubscribeService;

@Component
public class ApplicationReadyEventListener implements  ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private IAuthService authService;
	@Autowired
	private ISubscribeService subService;
	@Autowired
	Consumer consumer;
	
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	authService.login();
//    	subService.subscribeDeviceData();
    	consumer.initConsumer();
    }
    
}

