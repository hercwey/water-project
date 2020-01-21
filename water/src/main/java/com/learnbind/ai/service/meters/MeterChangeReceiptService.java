package com.learnbind.ai.service.meters;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.MeterChangeRecepit;
import com.learnbind.ai.service.common.IBaseService;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterChangeReceiptService.java
 * @Description:水表换表领用单
 *
 * @author Thinkpad
 * @date 2019年10月26日 下午12:04:03
 * @version V1.0 
 *
 */
public interface MeterChangeReceiptService extends IBaseService<MeterChangeRecepit, Long> {
	/**
     * 批量删除
     * @param meterIds
     * @return
     */
    public int deleteChangeReceipt(List<Long> receiptIds);
     
    public List<MeterChangeRecepit> searchCond(String searchCond, Long meterId);
    
    public int printChangeReceipt(String FILE_DIR, String TEMPLATE_PREFIX, Long itemId, String TEMPLATE_FILE_NAME, Long printerId) throws IOException, PrinterException, DocumentException;
}
