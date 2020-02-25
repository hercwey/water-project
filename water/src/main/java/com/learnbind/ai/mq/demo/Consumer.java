package com.learnbind.ai.mq.demo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import com.learnbind.ai.mq.MQConstant;

@Component
public class Consumer {

    /**
     * 消费者实体对象
     */
    //private DefaultMQPushConsumer consumer;
    /**
     * 消费者组
     */
    public static final String CONSUMER_GROUP = "example_group_name";//"test_mq_consumer";
    
    /**
     * 通过构造函数 实例化对象
     */
    public Consumer() throws MQClientException {
    	try {
    		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
            consumer.setNamesrvAddr(MQConstant.NAME_SERVER);
            //消费模式:一个新的订阅组第一次启动从队列的最后位置开始消费 后续再启动接着上次消费的进度开始消费
            //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //set to broadcast mode
            consumer.setMessageModel(MessageModel.BROADCASTING);
            //订阅主题和 标签（ * 代表所有标签)下信息
            consumer.subscribe("TOPIC", "*");
            // //注册消费的监听 并在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener(new MessageListenerConcurrently() {
    
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                    ConsumeConcurrentlyContext context) {
                	
                	System.out.println("Receive Message is "+"--"+msgs);
//                	System.out.println("Receive context is "+"--"+context);
                	
                	// msgs中只收集同一个topic，同一个tag，并且key相同的message
                    // 会把不同的消息分别放置到不同的队列中
                    try {
                        for (MessageExt msg : msgs) {
                        	System.out.println("----------MsgId:"+msg.getMsgId());
                        	System.out.println("----------Keys:"+msg.getKeys());
                            //消费者获取消息 这里只输出 不做后面逻辑处理
                            String body = new String(msg.getBody(), "utf-8");
                            System.out.println("Consumer-获取消息-主题topic为={}, 消费消息为={}"+"--topic:"+msg.getTopic()+"--body:"+body);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    
                }
            });

            consumer.start();
            System.out.println("消费者 启动成功=======");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
    	
    }
    
}
