package com.learnbind.ai.service.wechat.task.thread;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.learnbind.ai.service.wechat.task.notify.WxMsgNotifyTask;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.task.thread
 *
 * @Title: NotifyThread.java
 * @Description: 发送消息线程
 *
 * @author Administrator
 * @date 2019年8月2日 下午12:07:14
 * @version V1.0
 *
 */
@Service
public class NotifyThread extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unused")
	private volatile boolean stop = false;

	BlockingQueue<WxMsgNotifyTask> tasks = new ArrayBlockingQueue<WxMsgNotifyTask>(10000);

	public NotifyThread() {
		this.start();
	}

	@Override
	public void start() {
		logger.debug("微信模版消息通知线程启动。。。");
		super.start();
	}

	public void offerTask(WxMsgNotifyTask task) {
		boolean flag = tasks.offer(task);
		if(flag) {
			logger.debug("增加任务成功，现队列中的任务个数："+tasks.size());
		}else {
			logger.debug("增加任务失败，现队列中的任务个数："+tasks.size());
		}
		
	}

	@Override
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				
				try {
					while (true) {
						WxMsgNotifyTask task = tasks.poll();
						//logger.debug("微信消息通知任务："+task);
						if (task!=null) {
							task.exec();
						} else {
							break;
						}
					}
				} catch (Exception e) {
					logger.error("err", e);
				}
			}
		}, 0, 10 * 1000);

	}

}
