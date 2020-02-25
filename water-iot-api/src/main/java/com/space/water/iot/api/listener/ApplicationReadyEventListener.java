package com.space.water.iot.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.rocketmq.Consumer;
import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.service.impl.AuthService;

@Component
public class ApplicationReadyEventListener implements  ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private AuthService authService;
	@Autowired
	Consumer consumer;
	
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	authService.login();
    	consumer.initConsumer();
    }
    
}

