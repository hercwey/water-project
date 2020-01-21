package com.learnbind.ai.service.meters;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.MeterChangeRecepit;
import com.learnbind.ai.model.MeterOutStockDoc;
import com.learnbind.ai.service.common.IBaseService;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterOutStockDocService.java
 * @Description: 水表出库校验单
 *
 * @author Thinkpad
 * @date 2019年10月26日 下午12:07:59
 * @version V1.0 
 *
 */
public interface MeterOutStockDocService extends IBaseService<MeterOutStockDoc, Long> {
	
	/**
     * 批量删除
     * @param meterIds
     * @return
     */
    public int deleteOutStockDoc(List<Long> receiptIds);
	
	public List<MeterOutStockDoc>  searchCond(String searchCond, Long meterId);
	
	public int printReturnReceipt(String FILE_DIR, String TEMPLATE_PREFIX, Long itemId, String TEMPLATE_FILE_NAME, Long printerId) throws IOException, PrinterException, DocumentException;
}
