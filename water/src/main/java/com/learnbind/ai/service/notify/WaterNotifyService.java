package com.learnbind.ai.service.notify;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.WaterNotify;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface WaterNotifyService extends IBaseService<WaterNotify, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<WaterNotify> searchCond(String searchCond, String period, Integer sortMethod, Integer arrearsAmount);
	
	/**
	 * @Title: getNotifyMeterIdList
	 * @Description: 获取所有通知单的表计ID
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public List<WaterNotify> getNotifyMeterIdList(String period, String searchCond, String traceIds, Integer sortMethod, Integer arrearsAmount);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<WaterNotify> getWaterNotifyList(String searchCond, String period, String traceIds);
	
	/**
	 * @Title: generateNotifySingle
	 * @Description: 单个生成水费通知单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public int generateNotifySingle(Long customerId,String period);
    
	/**
	 * @Title: generateNotifyBatch
	 * @Description: 批量生成通知单
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param customerIdStr
	 * @return 
	 */
	public int generateNotifyBatch(String searchCond, String traceIds,  String period,String customerIdStr);
	
	/**
	 * @Title: previewNotifyPrepare
	 * @Description: 预览通知单
	 * @param notifyId
	 * @param period
	 * @param serialNo
	 * @return 
	 * @throws Exception 
	 */
	public int previewNotifyPrepare(String FILE_DIR,String TEMPLATE_PREFIX, String TEMPLATE_FILE_NAME,Long notifyId) throws Exception;
	
	/**
	 * @Title: printNotifySingle
	 * @Description:单个打印水费通知单
	 * @param customerId
	 * @param period
	 * @return 
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws PrinterException 
	 * @throws ParseException 
	 */
	public int printNotifySingle(String FILE_DIR, String TEMPLATE_PREFIX, Long notifyId, String templateFileName, Long printerId, String noticeDate, String chargePeople) throws DocumentException, IOException, PrinterException, ParseException;
	/*
	 * @Title: getList
	 * @Description: 查询某客户的通知单
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<WaterNotify> getList(Long customerId, String period, String searchCond);
	
	
	/**
	 * @Title: generateNotifyBatch
	 * @Description: 批量打印水费通知单
	 * @param FILE_DIR
	 * @param TEMPLATE_PREFIX
	 * @param templateFileName
	 * @param printerId
	 * @param searchCond
	 * @param traceIds
	 * @param period
	 * @param customerIdStr
	 * @param noticeDate
	 * @param chargePeople
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException 
	 * @throws ParseException 
	 */
	public int generateNotifyBatch(String FILE_DIR, String TEMPLATE_PREFIX, String templateFileName ,String searchCond, String traceIds, String period,String customerIdStr, Long printerId, String noticeDate, String chargePeople, Integer sortMethod, Integer arrearsAmount) throws DocumentException, IOException, PrinterException, ParseException;
	
	/**
	 * @Title: destoryWaterNotify
	 * @Description: 报废通知单
	 * @param notifyId
	 * @return 
	 */
	public int destoryWaterNotify(List<Long> notifyId);
	
	public int isExistsLocation(String meterIdStr, String traceIds);
	
	/**
	 * @Title: getWaterNotifyMap
	 * @Description: 获取通知单信息
	 * @param notifyId
	 * @param noticeDate
	 * @param chargePeople
	 * @return 
	 * @throws ParseException 
	 */
	public Map<String, Object> getWaterNotifyMap(Long notifyId ,String noticeDate, String chargePeople) throws ParseException;
	
	
	/**
	 * @Title: isExistWaterNotify
	 * @Description: 判断通知单是否已经生成
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 */
	public boolean isExistWaterNotify(List<Long> meterIdList , Long customerId, String period);
}
