package com.learnbind.ai.service.meters;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.MeterUnusedReceipt;
import com.learnbind.ai.service.common.IBaseService;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterUnusedReceiptService.java
 * @Description: 水表报废单
 *
 * @author Thinkpad
 * @date 2019年10月26日 下午9:28:35
 * @version V1.0 
 *
 */
public interface MeterUnusedReceiptService extends IBaseService<MeterUnusedReceipt, Long> {
	/**
     * 批量删除
     * @param meterIds
     * @return
     */
    public int deleteUnusedReceipt(List<Long> receiptIds);
     
    public List<MeterUnusedReceipt> searchCond(String searchCond, Long meterId);
    
    public MeterUnusedReceipt getUnusedMeter(Long meterId);
    
    public int printScarpReceipt(String FILE_DIR, String TEMPLATE_PREFIX, Long itemId, String TEMPLATE_FILE_NAME, Long printerId) throws IOException, PrinterException, DocumentException;
    
    public int printScarpBatch(String FILE_DIR, String TEMPLATE_PREFIX, String TEMPLATE_FILE_NAME, Long printerId, List<Map<String, Object>> meterMapList) throws IOException, PrinterException, DocumentException;
}
