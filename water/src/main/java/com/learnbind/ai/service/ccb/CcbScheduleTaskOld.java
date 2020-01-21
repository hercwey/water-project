package com.learnbind.ai.service.ccb;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.model.CcbBatchWithholdRecord;

//TODO 暂时屏蔽,待发布时放开即可.
//@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class CcbScheduleTaskOld {
	private final Log log = LogFactory.getLog(CcbScheduleTaskOld.class);	
	
	//每10分钟触发一次.600 seconds
	//final String CRON_SET="0/600 * * * * ?";
	//每1分钟触发一次.60 seconds
	final String CRON_SET="0/60 * * * * ?";
	
	//成功/失败状态
	final Integer STATUS_FAIL = 0;//失败
	final Integer STATUS_SUCCESS = 1;//成功
	
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//建行批量代扣记录服务
	@Autowired
	private CcbInterfaceService ccbInterfaceService;//建行接口服务
	
	
	
	//3.添加定时任务
   // @Scheduled(cron = CRON_SET)    
	/*
	 * Cron表达式参数分别表示：
	 * 
	 * 	秒（0~59） 例如0/5表示每5秒 
	 * 	分（0~59） 
	 * 	时（0~23） 
	 * 	日（0~31）的某天，需计算 
	 * 	月（0~11） 
	 * 	周几（ 可填1-7 或 	SUN/MON/TUE/WED/THU/FRI/SAT）
	 * 
	 * 	@Scheduled：除了支持灵活的参数表达式cron之外，还支持简单的延时操作，例如 fixedDelay ，fixedRate 填写相应的毫秒数即可。
	 */
    
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
	@Scheduled(cron = CRON_SET)
    private void configureTasks() {
        //System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    	log.info("----------开始执行查询批量代扣凭证状态任务----------");
    	this.process();
    	log.info("----------执行查询批量代扣凭证状态任务完成----------");
    	
    }
    
    /**
     * @Title: process
     * @Description: 开始处理申请后的批量代扣文件 
     */
    private void process() {
    	
    	CcbBatchWithholdRecord record = CcbWithholdRecordQueue.poll();//从队列中获取批量代扣文件记录
    	if(record==null) {
    		log.info("----------批量代扣记录队列为空，不需要处理业务......----------");
    		return;
    	}
    	
    	//签到
    	boolean isLogin = ccbInterfaceService.login();
    	if(!isLogin) {
    		log.info("----------签到失败，不处理业务，等待下次任务执行......----------");
    		return;
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
    	switch (status) {
		case 2:
			//（1）查询凭证状态，如果状态为700表示成功（2）下载明细-文件方式（3）下载回盘文件
			status = this.queryCertStatus(record);
			if(status==STATUS_FAIL) {
				record = this.getCcbBatchWithholdRecord(record.getId());
				CcbWithholdRecordQueue.offer(record);
				log.info("批量代扣文件记录处理失败，当前状态为："+status+"，需要再次添加到队列，等候再次处理。");
			}
			break;
		case 3:
			//（1）下载明细-文件方式（2）下载回盘文件
			status = this.queryWithholdCertDetailFile(record);
			if(status==STATUS_FAIL) {
				record = this.getCcbBatchWithholdRecord(record.getId());
				CcbWithholdRecordQueue.offer(record);
				log.info("批量代扣文件记录处理失败，当前状态为："+status+"，需要再次添加到队列，等候再次处理。");
			}
			break;

		default:
			log.info("批量代扣文件记录的状态为："+status+"，不需要处理，直接从队列中移除。");
			break;
		}
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
}
