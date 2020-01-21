package com.learnbind.ai.service.ccb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.learnbind.ai.model.CcbBatchWithholdRecord;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbWithholdRecordQueue.java
 * @Description: 建设银行批量代扣记录队列
 *
 * @author Administrator
 * @date 2019年8月24日 下午4:32:19
 * @version V1.0 
 *
 */
public class CcbWithholdRecordQueue {

	/**
	 * @Fields recordQueues：建行批量代扣记录队列
	 */
	private static BlockingQueue<CcbBatchWithholdRecord> recordQueues = new LinkedBlockingQueue<>();
	
	/**
	 * @Title: offer
	 * @Description: 添加一个元素并返回true  如果队列已满，则返回false
	 * @param record
	 * @return 
	 */
	public static boolean offer(CcbBatchWithholdRecord record) {
		return recordQueues.offer(record);
	}
	
	/**
	 * @Title: poll
	 * @Description: 移除并返问队列头部的元素    如果队列为空，则返回null
	 * @return 
	 */
	public static CcbBatchWithholdRecord poll() {
		return recordQueues.poll();
	}
	
}
