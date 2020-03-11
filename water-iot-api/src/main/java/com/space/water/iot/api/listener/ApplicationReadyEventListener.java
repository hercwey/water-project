package com.space.water.iot.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.rocketmq.RocketTopicConfig;
import com.space.water.iot.api.rocketmq.consumers.AccountStatusReadConsumer;
import com.space.water.iot.api.rocketmq.consumers.AccountStatusWriteConsumer;
import com.space.water.iot.api.rocketmq.consumers.AutoReportCacheConsumer;
import com.space.water.iot.api.rocketmq.consumers.ConfigParamsConsumer;
import com.space.water.iot.api.rocketmq.consumers.ConfigThresholdConsumer;
import com.space.water.iot.api.rocketmq.consumers.Consumer;
import com.space.water.iot.api.rocketmq.consumers.ControlValveConsumer;
import com.space.water.iot.api.rocketmq.consumers.DeviceDeleteConsumer;
import com.space.water.iot.api.rocketmq.consumers.DeviceQueryConsumer;
import com.space.water.iot.api.rocketmq.consumers.DeviceRegisterConsumer;
import com.space.water.iot.api.rocketmq.consumers.DeviceUpdateConsumer;
import com.space.water.iot.api.rocketmq.consumers.QueryMonthDataConsumer;
import com.space.water.iot.api.rocketmq.consumers.QueryParamsConsumer;
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
	@Autowired
	AutoReportCacheConsumer autoReportCacheConsumer;
	@Autowired
	ConfigParamsConsumer configParamsConsumer;
	@Autowired
	ConfigThresholdConsumer configThresholdConsumer;
	@Autowired
	ControlValveConsumer controlValveConsumer;
	@Autowired
	QueryParamsConsumer queryParamsConsumer;
	@Autowired
	QueryMonthDataConsumer queryMonthDataConsumer;
	@Autowired
	DeviceRegisterConsumer deviceRegisterConsumer;
	@Autowired
	DeviceUpdateConsumer deviceUpdateConsumer;
	@Autowired
	DeviceQueryConsumer deviceQueryConsumer;
	@Autowired
	DeviceDeleteConsumer deviceDeleteConsumer;
	@Autowired
	AccountStatusReadConsumer accountStatusReadConsumer;
	@Autowired
	AccountStatusWriteConsumer accountStatusWriteConsumer;
	@Autowired
	RocketTopicConfig topicConfig;
	
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	authService.login();
    	subService.subscribeDeviceData();
//    	consumer.initConsumer();
    	initConsumers();
    }
    
    private void initConsumers() {
    	autoReportCacheConsumer.initConsumer();
    	configParamsConsumer.initConsumer();
    	configThresholdConsumer.initConsumer();
    	controlValveConsumer.initConsumer();
    	queryParamsConsumer.initConsumer();
    	queryMonthDataConsumer.initConsumer();
    	deviceRegisterConsumer.initConsumer();
    	deviceUpdateConsumer.initConsumer();
    	deviceQueryConsumer.initConsumer();
    	deviceDeleteConsumer.initConsumer();
    	accountStatusReadConsumer.initConsumer();
    	accountStatusWriteConsumer.initConsumer();
    }
}

