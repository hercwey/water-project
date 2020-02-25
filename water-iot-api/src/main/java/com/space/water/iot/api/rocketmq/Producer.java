package com.space.water.iot.api.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.config.MQTags;

@Component
public class Producer {
    private String producerGroup = "example_group_name";
    private DefaultMQProducer producer;
    
    public Producer(){
        //示例生产者
        producer = new DefaultMQProducer(producerGroup);
        producer.setSendMsgTimeout(10000);
        //不开启vip通道 开通口端口会减2
        producer.setVipChannelEnabled(false);
        //绑定name server
        producer.setNamesrvAddr(MQConstant.NAME_SERVER);
        start();
    }
    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
  
    public DefaultMQProducer getProducer(){
        return this.producer;
    }
    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown(){
        this.producer.shutdown();
    }
    
    public SendResult sendNorth(String response,String tag) {
    	SendResult sendResult = null;		
		// 创建生产信息
		Message message = new Message("TOPIC", tag, "keys" , response.getBytes());
		// 发送
		try {
    		sendResult = getProducer().send(message);
    		System.out.println("输出生产者信息={}" + sendResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResult;
	}
}