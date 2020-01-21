package com.learnbind.ai.service.ccb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.util.SpringUtil;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbScheduleTaskThread.java
 * @Description: 定时任务线程
 *
 * @author Administrator
 * @date 2019年10月28日 上午11:49:21
 * @version V1.0 
 *
 */
//@Component
//@Service
public class CcbScheduleTaskThread implements Runnable {

	/**
	 * @Fields log：日志
	 */
	private final Log log = LogFactory.getLog(CcbScheduleTaskThread.class);
	
	//@Autowired
	private CcbScheduleTask ccbScheduleTask;//ccb定时任务
	
	//@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//建行批量代扣记录服务
	//@Autowired
	private CcbInterfaceService ccbInterfaceService;//建行接口服务
	
	public CcbScheduleTaskThread() {
		ccbScheduleTask = SpringUtil.getBean(CcbScheduleTask.class);
		log.debug("获取CCB定时任务实例："+ccbScheduleTask);
		ccbBatchWithholdRecordService = SpringUtil.getBean(CcbBatchWithholdRecordService.class);
		log.debug("获取批量代扣文件记录Service实例："+ccbScheduleTask);
		ccbInterfaceService = SpringUtil.getBean(CcbInterfaceService.class);
		log.debug("获取CCB接口Service实例："+ccbScheduleTask);
	}

	/** 
	 * @Title: run
	 * @Description: 重写run方法
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		log.debug("----------CCB定时任务线程-开始执行------------------------------");
		
		List<CcbBatchWithholdRecord> recordList = new ArrayList<>();//保存代扣失败的记录，需要重新加入到队列
		
		//开始处理队列中的数据，列表中的数据全部处理完后退出循环
		while (true) {
			CcbBatchWithholdRecord record = CcbWithholdRecordQueue.poll();//从队列中获取批量代扣文件记录
	    	if(record==null) {
	    		log.info("----------批量代扣记录队列为空，不需要处理业务......----------");
	    		break;
	    	}
			
			boolean resFlag = this.process(record);//处理业务部分，返回处理结果boolean类型，true表示处理成功，false表示需要再次处理
			if(!resFlag) {//把需要再次处理的记录，先添加到List，等队列中的数据全部处理后，要把需要再次处理的记录重新添加到队列
				record = this.getCcbBatchWithholdRecord(record.getId());
				recordList.add(record);
			}
			
		}
		
		//如果没有需要再次处理的记录
		if(recordList.size()>0) {
			//把需要再次处理的记录重新添加到队列
			for(CcbBatchWithholdRecord record : recordList) {
				CcbWithholdRecordQueue.offer(record);
			}
			ccbScheduleTask.start();
		}else {
			//停止定时任务
			ccbScheduleTask.stop();
		}
		
	}
	
	/**
	 * @Fields STATUS_FAIL：失败状态
	 */
	final Integer STATUS_FAIL = 0;//失败
	/**
	 * @Fields STATUS_SUCCESS：成功状态
	 */
	final Integer STATUS_SUCCESS = 1;//成功
	
	/**
     * @Title: process
     * @Description: 开始处理申请后的批量代扣文件 
     */
    private boolean process(CcbBatchWithholdRecord record) {
    	
//    	CcbBatchWithholdRecord record = CcbWithholdRecordQueue.poll();//从队列中获取批量代扣文件记录
//    	if(record==null) {
//    		log.info("----------批量代扣记录队列为空，不需要处理业务......----------");
//    		return;
//    	}
    	
    	//签到
    	boolean isLogin = ccbInterfaceService.login();
    	if(!isLogin) {
    		log.info("----------签到失败，不处理业务，等待下次任务执行......----------");
    		return false;
    	}
    	
    	int status = record.getStatus();
//    	批量代扣文件状态：
//		（1）0=已生成；
//		（2）1=已上传；
//		（3）2=已申请处理；
//		（4）3=已代扣；（CCB处理完成）
//		（5）4=已下载回盘文件；
//		（6）5=已预处理回盘；
//		（7）6=已做销账处理；
    	int retStatus = STATUS_FAIL;//返回状态-默认值0=失败
    	switch (status) {
		case 2:
			//（1）查询凭证状态，如果状态为700表示成功（2）下载明细-文件方式（3）下载回盘文件
			retStatus = this.queryCertStatus(record);
			if(retStatus==STATUS_FAIL) {
				//record = this.getCcbBatchWithholdRecord(record.getId());
				//CcbWithholdRecordQueue.offer(record);
				log.info("批量代扣文件记录处理失败，需要再次添加到队列，等候再次处理。");
				return false;
			}
			break;
		case 3:
			//（1）下载明细-文件方式（2）下载回盘文件
			retStatus = this.queryWithholdCertDetailFile(record);
			if(retStatus==STATUS_FAIL) {
				//record = this.getCcbBatchWithholdRecord(record.getId());
				//CcbWithholdRecordQueue.offer(record);
				log.info("批量代扣文件记录处理失败，需要再次添加到队列，等候再次处理。");
				return false;
			}
			break;

		default:
			log.info("批量代扣文件记录的状态为："+status+"，不需要处理，直接从队列中移除。");
			break;
		}
    	return true;
    }
    
    /**
     * @Title: queryCertStatus
     * @Description: 查询批量代扣文件处理状态（查询凭证状态）
     * @param record
     * @return 
     */
    private int queryCertStatus(CcbBatchWithholdRecord record) {
    	Map<String, Object> retMap = ccbInterfaceService.queryCertStatus(record);
		if(retMap!=null) {
			String resultCode = retMap.get(RequestResultUtil.RESULT_CODE).toString();
			if(resultCode.equalsIgnoreCase(RequestResultUtil.RESULT_CODE_SUCCESS)) {
				record = this.getCcbBatchWithholdRecord(record.getId());
				return this.queryWithholdCertDetailFile(record);
			}
		}
		return STATUS_FAIL;
    }
    /**
     * @Title: queryWithholdCertDetailFile
     * @Description: 查询凭证明细-文件形式
     * @param record
     * @return 
     * 		0=失败；1=成功；
     */
    private int queryWithholdCertDetailFile(CcbBatchWithholdRecord record) {
    	Map<String, Object> retMap = ccbInterfaceService.queryWithholdCertDetailFile(record);
		if(retMap!=null) {
			String resultCode = retMap.get(RequestResultUtil.RESULT_CODE).toString();
			if(resultCode.equalsIgnoreCase(RequestResultUtil.RESULT_CODE_SUCCESS)) {
				record = this.getCcbBatchWithholdRecord(record.getId());
				return this.downloadWithholdFileProcessResult(record);
			}
		}
		return STATUS_FAIL;
    }
    /**
     * @Title: downloadWithholdFileProcessResult
     * @Description: 下载批量代扣文件处理结果
     * @param record
     * @return 
     * 			0=失败；1=成功；
     */
    private int downloadWithholdFileProcessResult(CcbBatchWithholdRecord record) {
    	Map<String, Object> retMap = ccbInterfaceService.downloadWithholdFileProcessResult(record);
		if(retMap!=null) {
			String resultCode = retMap.get(RequestResultUtil.RESULT_CODE).toString();
			if(resultCode.equalsIgnoreCase(RequestResultUtil.RESULT_CODE_SUCCESS)) {
				return STATUS_SUCCESS;
			}
		}
		return STATUS_FAIL;
    }
    
    /**
     * @Title: getCcbBatchWithholdRecord
     * @Description: 根据id查询批量代扣文件记录
     * @param id
     * @return 
     */
    private CcbBatchWithholdRecord getCcbBatchWithholdRecord(Long id) {
    	return ccbBatchWithholdRecordService.selectByPrimaryKey(id);
    }
    
    /**
     * @Title: test
     * @Description: 测试代码
     * @param record
     * @return 
     */
    private boolean test(CcbBatchWithholdRecord record) {
    	if(record.getStatus()==1) {
    		log.debug("----------需要再次加入队列。。。"+record.getFileSn()+":"+record.getStatus());
    		//recordList.add(record);
    	}
    	log.debug("----------开始执行。。。"+record.getFileSn()+":"+record.getStatus());
    	record.setStatus(0);
    	try {
    		log.debug("----------等待3秒");
			Thread.sleep(3*1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	log.debug("----------等待3秒结束");
    	return true;
    }
	
}
