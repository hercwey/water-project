package com.learnbind.ai.controller.mq;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.common.SnowflakeIdUtil;
import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.mq.demo.Producer;

@RestController
public class MQTestController {

    @Autowired
    private Producer producer;
    @Autowired
    private RocketTopicConfig rocketTopicConfig;

    private List<String> mesList;

    /**
     * 初始化消息
     */
    public MQTestController() {
        mesList = new ArrayList<>();
        mesList.add("小小");
        mesList.add("爸爸");
        mesList.add("妈妈");
        mesList.add("爷爷");
        mesList.add("奶奶");

    }

    @RequestMapping("/test/rocketmq")
    public Object callback() throws Exception {
    	
    	System.out.println("------------------------------------------------------------------------------------------------------------------------");
    	System.out.println("----------"+rocketTopicConfig.toString());
    	System.out.println("------------------------------------------------------------------------------------------------------------------------");
    	
        //总共发送五次消息
        for (String s : mesList) {
        	
        	String keys = SnowflakeIdUtil.genYYYYMMDDAndId();
            //创建生产信息
            Message message = new Message("TOPIC", "testtag", keys, ("小小一家人的称谓:" + s).getBytes());
            //发送
            SendResult sendResult = producer.getProducer().send(message);
            System.out.println("输出生产者信息={}"+sendResult);
        }
        producer.shutdown();
        return "成功";
    } 
}
